package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.UserGroup;

public interface UserGroupDao extends GenericDao<UserGroup> {

    public UserGroup findByName(String name);
}