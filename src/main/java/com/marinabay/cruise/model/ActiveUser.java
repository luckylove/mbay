package com.marinabay.cruise.model;

import com.marinabay.cruise.constant.USERTYPE;
import com.marinabay.cruise.utils.RequestUtls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class ActiveUser extends GenericModel{

    private Long userId;
    private int activeCount;
    private Date activeDate;

    private String userName;
    private String userType;
    private String userLicense;
    private String activeDateStr;

    public String getUserTypeView() {
        return USERTYPE.valueOf(userType).getView();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getActiveDateStr() {
        if (activeDate != null) {
            activeDateStr = RequestUtls.date2Str(activeDate, new SimpleDateFormat("yyyy/MM/dd"));
        }
        return activeDateStr;
    }

    public void setActiveDateStr(String activeDateStr) {
        this.activeDateStr = activeDateStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLicense() {
        return userLicense;
    }

    public void setUserLicense(String userLicense) {
        this.userLicense = userLicense;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }
}
