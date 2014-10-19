package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.TaxiDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.Taxi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class TaxiService extends GenericService<Taxi>{

    @Autowired
    private TaxiDao taxiDao;

    @Override
    public TaxiDao getDao() {
        return taxiDao;
    }

    public JSonPagingResult<Taxi> list(PagingModel model) {
        Long count = taxiDao.count(model);
        List<Taxi> userGroups = taxiDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<Taxi> listAll() {
        return taxiDao.select(new PagingModel());
    }


    public Taxi findByName(String name) {
        return taxiDao.findByName(name);
    }


}
