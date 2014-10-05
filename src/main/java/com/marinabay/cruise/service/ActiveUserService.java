package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.marinabay.cruise.dao.ActiveUserDao;
import com.marinabay.cruise.model.ActiveUser;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class ActiveUserService extends GenericService<ActiveUser>{

    @Autowired
    private ActiveUserDao activeUserDao;

    @Override
    public ActiveUserDao getDao() {
        return activeUserDao;
    }


    public JSonPagingResult<ActiveUser> list(PagingModel model) {
        Long count = activeUserDao.count(model);
        if (StringUtils.isEmpty(model.getName())) {
            model.setName("active_count");
            model.setOrder("desc");
        }
        return JSonPagingResult.ofSuccess(count, activeUserDao.select(model));
    }


    public ActiveUser selectByDate(Long id) {
        LocalDate lc = new LocalDate();
        return activeUserDao.selectByDate(ImmutableMap.<String, Object>of("id", id, "activeDate", lc.toDate()));
    }

    public void insertOrUpdate(Long id) {
        ActiveUser activeUser = selectByDate(id);
        if (activeUser == null) {
            activeUser = new ActiveUser();
            activeUser.setUserId(id);
            activeUser.setActiveCount(1);
            LocalDate lc = new LocalDate();
            activeUser.setActiveDate(lc.toDate());
            activeUserDao.insert(activeUser);
        } else {
            activeUserDao.increase(id);
        }
    }



}
