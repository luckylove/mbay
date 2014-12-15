package com.marinabay.cruise.model;

import com.marinabay.cruise.utils.RequestUtls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class LuckyDraw extends GenericModel {

    private String content;
    private Date triggerTime;
    private String triggerTimeStr;
    private Long userId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTriggerTimeStr() {
        if (triggerTime != null) {
            return  RequestUtls.date2Str(triggerTime, new SimpleDateFormat("yyyy/MM/dd HH:mm"));
        }
        return "";
    }

    public void setTriggerTimeStr(String triggerTimeStr) {
        this.triggerTimeStr = triggerTimeStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
