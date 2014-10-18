package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.marinabay.cruise.dao.UserDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class UserService extends GenericService<User>{

    @Autowired
    private UserDao userDao;

    @Override
    public UserDao getDao() {
        return userDao;
    }

    public User findUserByEmail(String email) {
        return getDao().findUserByEmail(email);
    }

    public JSonPagingResult<User> list(PagingModel model) {
        Long count = userDao.count(model);
        if (StringUtils.isEmpty(model.getName())) {
            model.setName("userName");
        }
        //need translate filed to column
        if ("userName".equals(model.getName())) {
            model.setName("user_name");
        } else if ("taxiLicense".equals(model.getName())) {
            model.setName("taxi_license");
        } else if ("taxiLicense".equals(model.getName())) {
            model.setName("taxi_license");
        }
        return JSonPagingResult.ofSuccess(count, userDao.select(model));
    }

    public void assignGroup(List<Long> ids, Long groupId) {
        getDao().assignGroup(ImmutableMap.of("ids", ids, "userGroupId", groupId));
    }

    public void resetUserGroup(Long userGroupId) {
        getDao().resetUserGroup(userGroupId);
    }

    public List<User> selectByLicense(String license) {
        return getDao().selectByLicense(license);
    }

    public List<User> selectAllByGroup(Long groupId) {
        return userDao.selectAllByGroup(groupId);
    }
}
