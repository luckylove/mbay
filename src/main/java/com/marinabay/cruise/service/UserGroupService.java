package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.UserGroupDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class UserGroupService extends GenericService<UserGroup>{

    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public UserGroupDao getDao() {
        return userGroupDao;
    }

    public JSonPagingResult<UserGroup> list(PagingModel model) {
        Long count = userGroupDao.count(model);
        List<UserGroup> userGroups = userGroupDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<UserGroup> listAlll(PagingModel model) {
        return userGroupDao.select(model);
    }

    public UserGroup findByName(String name) {
        return userGroupDao.findByName(name);
    }


}
