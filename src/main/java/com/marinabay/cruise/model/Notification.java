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

    public Notification() {
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
