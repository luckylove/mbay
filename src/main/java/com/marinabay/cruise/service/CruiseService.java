package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.CruiseDao;
import com.marinabay.cruise.model.Cruise;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class CruiseService extends GenericService<Cruise>{

    @Autowired
    private CruiseDao cruiseDao;

    @Override
    public CruiseDao getDao() {
        return cruiseDao;
    }

    public JSonPagingResult<Cruise> list(PagingModel model) {
        Long count = cruiseDao.count(model);
        List<Cruise> userGroups = cruiseDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }


    public Cruise findByName(String name) {
        return cruiseDao.findByName(name);
    }


}
