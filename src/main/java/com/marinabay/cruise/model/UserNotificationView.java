package com.marinabay.cruise.model;

import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.utils.RequestUtls;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class UserNotificationView extends GenericModel{

    private String senderName;
    private String receiverName;
    private SEND_STATUS status;
    private String type;
    private String sendDate;

    public String getSendDate() {
        if(this.getRegDate() != null) {
            return RequestUtls.date2Str(getRegDate());
        }
        return "";
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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
}
