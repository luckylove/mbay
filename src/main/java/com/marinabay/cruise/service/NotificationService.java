package com.marinabay.cruise.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.dao.UserDao;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.job.CheckSMSJob;
import com.marinabay.cruise.service.job.PushJob;
import com.marinabay.cruise.service.job.SMSJob;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class NotificationService extends GenericService<Notification>{

    private Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Value("${cruise.sms.user}")
    protected String username ;

    @Value("${cruise.sms.pass}")
    protected String password ;

    @Value("${cruise.sms.port}")
    protected String port ;

    @Value("${cruise.sms.url}")
    protected String url ;

    @Value("${cruise.sms.checkurl}")
    protected String checkUrl ;



    private ExecutorService sendService = Executors.newFixedThreadPool(15);
    private ScheduledExecutorService checkService = Executors.newScheduledThreadPool(1);

    @Override
    public NotificationDao getDao() {
        return notificationDao;
    }

    @PostConstruct
    public void initService() {
        checkService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    LOG.info("Go to check sms status");
                    List<UserNotification> allSendMsg = notificationDao.getAllSendMsg();
                    for (UserNotification nf : allSendMsg) {
                        if ("SMS".equals(nf.getType())) {
                            CheckSMSJob smsJob = new CheckSMSJob(notificationDao, nf, getSMSCheckURL(), getCheckSMSParams(nf.getSendId()));
                            sendService.submit(smsJob);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("", e);
                }
            }
        }, 3, 1, TimeUnit.MINUTES);
    }

    public JSonPagingResult<Notification> list(PagingModel model) {
        Long count = notificationDao.count(model);
        List<Notification> userGroups = notificationDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<Notification> listAlll(PagingModel model) {
        return notificationDao.select(model);
    }


    @Transactional
    public void insertAndSend(final Notification message, User sendUser) {
        if (StringUtils.isNotEmpty(message.getMessage())) {
            List<String> idstr = Splitter.on(',').omitEmptyStrings().splitToList(message.getUserIds());
            List<Long> userIDs = new ArrayList<Long>(idstr.size());
            for (String userID : idstr) {
                userIDs.add(Long.valueOf(userID));
            }
            int sendCnt = 0;
            if (!userIDs.isEmpty()) {

                message.setSenderId(sendUser.getId());
                message.setSenderName(sendUser.getUserName());
                message.setUserCnt(userIDs.size());
                notificationDao.insert(message);

                final List<User> users = userService.loadAllUserByIds(userIDs);
                if ("WEB".equals(message.getSendType())) {

                    for (User user : users) {
                        //store into db
                        UserNotification nf = new UserNotification();
                        nf.setNotificationId(message.getId());
                        nf.setType(message.getSendType());
                        nf.setUserId(user.getId());
                        nf.setStatus(SEND_STATUS.NOT_SEND);
                        PushJob smsJob = new PushJob(notificationDao, nf, getPUSH_URL(user.getUserName(), sendUser.getUserName(), message.getMessage()));
                        //store to db and send to push job
                        sendCnt ++;
                        sendService.submit(smsJob);
                    }
                } else if ("SMS".equals(message.getSendType())) {
                    for (User user : users) {
                        if (StringUtils.isNotEmpty(user.getMobile())) {
                            UserNotification nf = new UserNotification();
                            nf.setNotificationId(message.getId());
                            nf.setType(message.getSendType());
                            nf.setUserId(user.getId());
                            nf.setStatus(SEND_STATUS.NOT_SEND);
                            SMSJob smsJob = new SMSJob(notificationDao, nf, getSMSURL()
                            ,getSMSParams(user.getMobile(), sendUser.getUserName(), message.getMessage()));
                            sendService.submit(smsJob);
                            sendCnt ++;
                        }
                    }
                }
                message.setUserCnt(sendCnt);
                notificationDao.update(message);
            }
        }
    }

    private String getSMSURL() {
        return String.format(url, port);
    }

    private List<NameValuePair>  getSMSParams(String to, String from, String message) {
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("to", to));
        params.add(new BasicNameValuePair("from", from));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("message-type", "sms.automatic"));
        return params;
    }

    private String getSMSCheckURL() {
        return String.format(checkUrl, port);
    }

    private List<NameValuePair>  getCheckSMSParams(String messageId) {
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("message-id", messageId));
        return params;
    }

    private String getPUSH_URL(String to, String from, String message) {
        return "";
    }


    public JSonPagingResult<UserNotificationView> getAllSentNotification(PagingModel model) {
        Long total = notificationDao.countAllSentNotification(model);
        return JSonPagingResult.ofSuccess(total, notificationDao.getAllSentNotification(model));
    }

    public void deleteUserNotification(Long val) {
        notificationDao.deleteUserNotification(val);
        notificationDao.deleteByID(val);
    }

    public void shutDownService() {
        checkService.shutdownNow();
        sendService.shutdownNow();
    }




}
