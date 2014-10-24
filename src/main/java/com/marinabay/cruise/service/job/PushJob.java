package com.marinabay.cruise.service.job;

import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.model.UserNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
public class PushJob implements Callable {

    private Logger LOG = LoggerFactory.getLogger(PushJob.class);

    private NotificationDao notificationDao;
    private UserNotification nf;
    private String sendUrl;

    public PushJob(NotificationDao notificationDao, UserNotification nf, String sendUrl) {
        this.notificationDao = notificationDao;
        this.nf = nf;
        this.sendUrl = sendUrl;

    }
    @Override
    public Object call() throws Exception {
        LOG.info("insert to db {}", nf.getUserId());
        notificationDao.insertUserNotification(nf);
        /*LOG.info("send push", sendUrl);
        try {
            String result = sendGet(sendUrl);
            nf.setStatus(SEND_STATUS.SENT);
            nf.setSendId(result);
        } catch (Exception e) {
            nf.setStatus(SEND_STATUS.ERROR);
        }
        //update status to db
        notificationDao.updateUserNotification(nf);*/
        return null;
    }


}
