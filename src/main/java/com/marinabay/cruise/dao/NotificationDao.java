package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Notification;
import com.marinabay.cruise.model.UserNotification;

import java.util.List;

public interface NotificationDao extends GenericDao<Notification> {

    public void insertUserNotification(UserNotification record);
    public void updateUserNotification(UserNotification record);

    public List<UserNotification> getAllSendMsg();

}