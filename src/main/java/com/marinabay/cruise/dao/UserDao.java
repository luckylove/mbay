package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.User;
import com.marinabay.cruise.model.UserGroupUser;

import java.util.List;

public interface UserDao extends GenericDao<User> {

	public User findUserByEmail(String email);

    public void resetUserGroup(Long userGroupId) ;

    public void insertUserGroup(UserGroupUser userGroup) ;

    public Long hasByUserAndGroup(UserGroupUser userGroup) ;

    public List<User> selectByLicense(String license) ;

    public List<User> selectAllByGroup(Long groupId) ;

    public List<User> loadAllUserByIds(List<Long> userIds) ;

    public List<Long> selectAllId() ;

}