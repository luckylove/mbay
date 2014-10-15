package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.ActiveUser;
import com.marinabay.cruise.model.PagingModel;

import java.util.List;
import java.util.Map;

public interface ActiveUserDao extends GenericDao<ActiveUser> {

    public void increase(Long id) ;

    public ActiveUser selectByDate(Map<String, Object> map);

    public Long countTopActive(PagingModel model);

    public List<ActiveUser> topActiveUser(PagingModel model);


}