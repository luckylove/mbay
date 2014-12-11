package com.marinabay.cruise.service.job;

import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.model.UserNotification;
import com.marinabay.cruise.model.push.PushMessage;
import com.marinabay.cruise.service.NotificationService;
import javapns.Push;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
public class IosPushJob implements Callable {

    private Logger LOG = LoggerFactory.getLogger(IosPushJob.class);

    private NotificationService notificationService;
    private UserNotification nf;
    private PushMessage message;



    public IosPushJob(NotificationService notificationDao, UserNotification nf, PushMessage msg) {
        this.notificationService = notificationDao;
        this.message = msg;
        this.nf = nf;
    }
    @Override
    public Object call() throws Exception {
        LOG.info("send push {} {} ks: {} ps:{} tk:{}", new Object[]{nf.getUserId(), nf.getId(), message.getKeyStore(), message.getKeyPass(), message.getToken()});
        try {
            PushedNotifications alert = Push.alert(message.getMessage(), message.getKeyStore(), message.getKeyPass(), true, message.getToken());
            if (alert != null) {
                PushedNotification pushedNotification = alert.get(0);
                if (pushedNotification != null && pushedNotification.isSuccessful()) {
                    nf.setStatus(SEND_STATUS.SENT);
                    nf.setCheckCount(nf.getCheckCount() + 1);
                    LOG.info("------------ Push is ok: {} -----------------", pushedNotification.getDevice().getToken());
                } else {
                    String invalidToken = pushedNotification.getDevice().getToken();
                    LOG.error("************* Push is fail: invalid token {} **************", invalidToken);
                    ResponsePacket theErrorResponse = pushedNotification.getResponse();
                    if (theErrorResponse != null){
                        LOG.error("************* Push is fail: invalid token {} - packet response {} **************", invalidToken, theErrorResponse.getMessage());
                    }
                }
            }
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
