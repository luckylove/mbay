package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Notification;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.UserNotification;
import com.marinabay.cruise.model.UserNotificationView;

import java.util.List;

public interface NotificationDao extends GenericDao<Notification> {

    public void insertUserNotification(UserNotification record);

    public void updateUserNotification(UserNotification record);

    public void decreaseCheckCnt(UserNotification record);

    public void deleteUserNotification(Long val);

    public List<UserNotification> getAllSendMsg();

    public List<UserNotificationView> getAllSentNotification(PagingModel model);

    public Long countAllSentNotification(PagingModel model);

}