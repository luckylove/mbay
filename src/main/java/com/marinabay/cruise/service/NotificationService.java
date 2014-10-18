package com.marinabay.cruise.service;

import com.google.common.base.Splitter;
import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.dao.UserDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.Notification;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class NotificationService extends GenericService<Notification>{

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserDao userDao;

    @Value("${cruise.sms.user}")
    protected String username ;

    @Value("${cruise.sms.pass}")
    protected String password ;

    @Value("${cruise.sms.port}")
    protected String port ;

    @Value("${cruise.sms.url}")
    protected String url ;


    private ExecutorService exeService = Executors.newFixedThreadPool(10);

    @Override
    public NotificationDao getDao() {
        return notificationDao;
    }

    public JSonPagingResult<Notification> list(PagingModel model) {
        Long count = notificationDao.count(model);
        List<Notification> userGroups = notificationDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<Notification> listAlll(PagingModel model) {
        return notificationDao.select(model);
    }


    public void insertAndSend(final Notification message) {
        if (StringUtils.isNotEmpty(message.getMessage())) {
            List<String> idstr = Splitter.on(',').omitEmptyStrings().splitToList(message.getUserIds());
            List<Long> userIDs = new ArrayList<Long>(idstr.size());
            for (String userID : idstr) {
                userIDs.add(Long.valueOf(userID));
            }
            int sendCnt = 0;
            final List<User> users = userDao.loadAllUserByIds(userIDs);
            if ("WEB".equals(message.getSendType())) {
                for (User user : users) {

                }
            } else if ("SMS".equals(message.getSendType())) {
                for (User user : users) {
                    if (StringUtils.isNotEmpty(user.getMobile())) {

                    }
                }
            }
            message.setUserCnt(userIDs.size());
            notificationDao.insert(message);
        }
        //should be insert table ??

    }

    private void sendToDB(User user, String messgage){

    }

    private void sendToSMS(User user, String messgage){

    }



}
