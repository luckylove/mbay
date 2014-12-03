package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.User;
import com.marinabay.cruise.model.UserGroupUser;

import java.util.List;
import java.util.Map;

public interface UserDao extends GenericDao<User> {

	public User findUserByEmail(String email);

	public User findByUserName(String userName);

    public void resetUserGroup(Long userGroupId) ;

    public void insertUserGroup(UserGroupUser userGroup) ;

    public void updateToken(Map userGroup) ;

    public void updatePushNotification(Map userGroup) ;

    public Long hasByUserAndGroup(UserGroupUser userGroup) ;

    public List<User> selectByLicense(String license) ;

    public List<User> selectAllByGroup(Map map) ;

    public List<User> loadAllUserByIds(Map map) ;

    public List<Long> selectAllId() ;

}