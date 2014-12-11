package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.marinabay.cruise.dao.UserDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.User;
import com.marinabay.cruise.model.UserGroupUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public User findByUserName(String userName) {
        return getDao().findByUserName(userName);
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
        for (Long userId : ids) {
            UserGroupUser userGroupUser = new UserGroupUser(userId, groupId);
            Long aLong = userDao.hasByUserAndGroup(userGroupUser);
            if (aLong == null || aLong == 0) {
                userDao.insertUserGroup(userGroupUser);
            }
        }
    }

    public void resetUserGroup(Long userGroupId) {
        getDao().resetUserGroup(userGroupId);
    }

    public List<User> selectByLicense(String license) {
        return getDao().selectByLicense(license);
    }

    public List<User> selectAllByGroup(Long groupId, String userType) {
        return userDao.selectAllByGroup(ImmutableMap.of("groupId", groupId, "userType", userType));
    }

    public List<User> loadAllUserByIds(List<Long> userIds) {
        return userDao.loadAllUserByIds(ImmutableMap.of("userIds", userIds));
    }

    public void updateToken(Long id, String token, String devicetype) {
        HashMap<String, Object> objectHashMap = new HashMap<String, Object>(3);
        objectHashMap.put("id", id);
        objectHashMap.put("deviceToken", token);
        objectHashMap.put("devicetype", devicetype);
        userDao.updateToken(objectHashMap);
    }

    public void updatePassword(Long id, String password) {
        userDao.updatePassword(ImmutableMap.of("id", id, "password", password));
    }

    public void updatePushNotification(Long id, Boolean enble) {
        userDao.updatePushNotification(ImmutableMap.of("id", id, "push", enble ? "1":"0" ));
    }

    public User checkDupUsername(Long userId, String userName) {
        return userDao.checkDupUsername(ImmutableMap.of("userId", userId, "username", userName));
    }

    public User checkDupEmail(Long userId, String email){
        return userDao.checkDupEmail(ImmutableMap.of("userId", userId, "email", email));
    }
}
