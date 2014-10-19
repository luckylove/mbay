package com.marinabay.cruise.model;

import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class UserGroupUser {

    private Long groupId;
    private Date regDate;
    private Long userId;

    public UserGroupUser(){}

    public UserGroupUser(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
