package com.marinabay.cruise.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.dao.UserDao;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.model.push.PushMessage;
import com.marinabay.cruise.service.job.AndroidPushJob;
import com.marinabay.cruise.service.job.IosPushJob;
import com.marinabay.cruise.service.job.SMSJob;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    @Value("${gcm.apikey}")
    public String gcmApikey;

    @Value("${ios.pass}")
    public String iosPass;

    @Value("${ios.keystore}")
    public String keystore;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public NotificationDao getDao() {
        return notificationDao;
    }

    @PostConstruct
    private void init(){
    }

    @Scheduled(fixedDelay = 180000)
    public void initService() {
        try {
            LOG.info("Go to check sms status");
            List<UserNotification> allSendMsg = notificationDao.getResendSentNotification();
            for (UserNotification nf : allSendMsg) {
                //only for push type
                //taskExecutor.submit(new PushJob(NotificationService.this, nf, getPUSH_URL(null, null, null)));
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public JSonPagingResult<Notification> list(PagingModel model) {
        Long count = notificationDao.count(model);
        List<Notification> userGroups = notificationDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<Notification> listAlll(PagingModel model) {
        return notificationDao.select(model);
    }


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
                //web mean push
                if ("PUSH".equals(message.getSendType())) {

                    for (User user : users) {
                        //store into db
                        UserNotification nf = new UserNotification();
                        nf.setNotificationId(message.getId());
                        nf.setType(message.getSendType());
                        nf.setUserId(user.getId());
                        nf.setStatus(SEND_STATUS.NOT_SEND);
                        notificationDao.insertUserNotification(nf);
                        if("1".equals(user.getSendPush()) && StringUtils.isNotEmpty(user.getDeviceToken())) {
                            if ("1".equals(user.getDeviceType())) {// ios
                                PushMessage msg = PushMessage.getInstance(message.getMessage(), user.getDeviceToken(), this.keystore, this.iosPass);
                                IosPushJob smsJob = new IosPushJob(this, nf, msg);
                                taskExecutor.submit(smsJob);
                            } else if ("2".equals(user.getDeviceType())) { //android
                                //send to android device
                                PushMessage msg = PushMessage.getInstance(message.getMessage(), user.getDeviceToken(), null, this.gcmApikey);
                                AndroidPushJob smsJob = new AndroidPushJob(this, nf, msg);
                                taskExecutor.submit(smsJob);
                            }
                        }
                        //store to db and send to push job
                        //sendCnt ++;

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
                            taskExecutor.submit(smsJob);
                            //sendCnt ++;
                        }
                    }
                }
                //message.setUserCnt(sendCnt);
                //notificationDao.update(message);
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

    public void  updateUserNotification(UserNotification nf) {
       notificationDao.updateUserNotification(nf);
    }

    public void  inactiveNotification(Long id) {
       notificationDao.inactiveNotification(id);
    }

    public void deleteUserNotification(Long val) {
        notificationDao.deleteUserNotification(val);
        notificationDao.deleteByID(val);
    }

    public void shutDownService() {
    }




}
