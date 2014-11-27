package com.marinabay.cruise.service.job;

import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.model.UserNotification;
import com.marinabay.cruise.service.NotificationService;
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

    private NotificationService notificationService;
    private UserNotification nf;
    private String sendUrl;
    private String pushReceive;


    public PushJob(NotificationService notificationDao, UserNotification nf, String sendUrl) {
        this.notificationService = notificationDao;
        this.nf = nf;
        this.sendUrl = sendUrl;

    }
    @Override
    public Object call() throws Exception {
        LOG.info("send push {} {}", nf.getUserId(), nf.getId());
        try {
            //String result = sendGet(sendUrl);
            nf.setStatus(SEND_STATUS.SENT);
            nf.setCheckCount(nf.getCheckCount() + 1);
            //nf.setSendId(result);
        } catch (Exception e) {
            LOG.error("", e);
            nf.setStatus(SEND_STATUS.ERROR);
        }
        //update status to db
        notificationService.updateUserNotification(nf);
        LOG.info("update to db {} {}", nf.getUserId(), nf.getId());
        return null;
    }


}
