package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao extends GenericDao<User> {

	public User findUserByEmail(String email);

    public void assignGroup(Map<String, Object> map) ;

    public void resetUserGroup(Long userGroupId) ;

    public List<User> selectByLicense(String license) ;

    public List<User> selectAllByGroup(Long groupId) ;

    public List<User> loadAllUserByIds(List<Long> userIds) ;

    public List<Long> selectAllId() ;

}