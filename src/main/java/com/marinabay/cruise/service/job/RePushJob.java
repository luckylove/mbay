package com.marinabay.cruise.service.job;

import com.marinabay.cruise.constant.SEND_STATUS;
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
public class RePushJob implements Callable {

    private Logger LOG = LoggerFactory.getLogger(RePushJob.class);

    private NotificationDao notificationDao;
    private UserNotification nf;
    private String sendUrl;
    private String pushReceive;


    public RePushJob(NotificationDao notificationDao, UserNotification nf, String sendUrl) {
        this.notificationDao = notificationDao;
        this.nf = nf;
        this.sendUrl = sendUrl;

    }
    @Override
    public Object call() throws Exception {
        LOG.info("send push", sendUrl);
        try {
            //String result = sendGet(sendUrl);
            nf.setStatus(SEND_STATUS.SENT);
            //nf.setSendId(result);
        } catch (Exception e) {
            nf.setStatus(SEND_STATUS.ERROR);
        }
        //update status to db
        notificationDao.updateUserNotification(nf);
        LOG.info("update to db {} {}", nf.getUserId(), nf.getId());
        return null;
    }


}
