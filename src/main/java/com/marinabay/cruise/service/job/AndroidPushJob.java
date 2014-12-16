package com.marinabay.cruise.service.job;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.model.UserNotification;
import com.marinabay.cruise.model.push.PushMessage;
import com.marinabay.cruise.service.NotificationService;
import javapns.Push;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
public class AndroidPushJob implements Callable {

    private Logger LOG = LoggerFactory.getLogger(AndroidPushJob.class);

    private NotificationService notificationService;
    private UserNotification nf;
    private PushMessage message;



    public AndroidPushJob(NotificationService notificationDao, UserNotification nf, PushMessage msg) {
        this.notificationService = notificationDao;
        this.message = msg;
        this.nf = nf;
    }
    @Override
    public Object call() throws Exception {
        LOG.info("android push {} {} - ps:{} tk:{}", new Object[]{nf.getUserId(), nf.getId(), message.getKeyPass(), message.getToken()});
        try {
            Message.Builder gcmMsgBuilder = new Message.Builder();
            gcmMsgBuilder.addData("message", message.getMessage());
            Result gcmResult = new Sender(message.getKeyPass()).send(gcmMsgBuilder.build(), message.getToken(), 5);
            if (gcmResult != null) {
                String messageId = gcmResult.getMessageId();
                if (StringUtils.isNotEmpty(messageId)) {
                    LOG.info("----------------- Sent successfully to " + messageId + " -----------------------");
                    nf.setStatus(SEND_STATUS.SENT);
                    nf.setCheckCount(nf.getCheckCount() + 1);
                } else {
                    String gcmMsgErrorCode = gcmResult.getErrorCodeName();
                    nf.setStatus(SEND_STATUS.ERROR);
                    LOG.error("******************* Send is fail with code: " + gcmMsgErrorCode + " ********************");
                }
            } else {
                nf.setStatus(SEND_STATUS.ERROR);
                LOG.error("The GCM result is null");
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
