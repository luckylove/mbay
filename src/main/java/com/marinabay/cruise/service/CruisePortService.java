package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.CruisePortDao;
import com.marinabay.cruise.model.CruisePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class CruisePortService extends GenericService<CruisePort>{

    @Autowired
    private CruisePortDao cruiseDao;

    @Override
    public CruisePortDao getDao() {
        return cruiseDao;
    }


}
