package com.marinabay.cruise.model;

import com.marinabay.cruise.utils.RequestUtls;

import java.text.SimpleDateFormat;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class Notification extends GenericModel{

    private String message;
    private String userIds;
    private Integer userCnt;
    private String type;
    private String sendType;
    private Long senderId;
    private String senderName;


    public Notification() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSendTypeView() {
        if ("SMS".equals(sendType)) {
            return "NonApp";
        }
        return "InApp";
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserCnt() {
        return userCnt;
    }

    public void setUserCnt(Integer userCnt) {
        this.userCnt = userCnt;
    }

    public String getRegDateStr() {
        if (getRegDate() != null) {
            return RequestUtls.date2Str(getRegDate(), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        }
        return "";
    }

}
