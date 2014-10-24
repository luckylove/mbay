package com.marinabay.cruise.model;

import com.marinabay.cruise.constant.SEND_STATUS;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class UserNotification extends GenericModel{

    private Long userId;
    private Long notificationId;
    private SEND_STATUS status;
    private String type;
    private String sendId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public SEND_STATUS getStatus() {
        return status;
    }

    public void setStatus(SEND_STATUS status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
}
