package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.marinabay.cruise.dao.CruiseDao;
import com.marinabay.cruise.dao.SchedulesDao;
import com.marinabay.cruise.model.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class SchedulesService extends GenericService<Schedules>{

    @Autowired
    private SchedulesDao schedulesDao;


    @Autowired
    private CruiseDao cruiseDao;

    @Override
    public SchedulesDao getDao() {
        return schedulesDao;
    }

    public JSonPagingResult<Schedules> list(SchelduePagingModel model) {
        Long count = schedulesDao.count(model);

        //need translate filed to column
        if ("arrivalTimeStr".equals(model.getName())) {
            model.setName("arrival_time");
        } else if ("departureTimeStr".equals(model.getName())) {
            model.setName("departure_time");
        } else if ("passengers".equals(model.getName())) {
            model.setName("passengers");
        } else if ("callType".equals(model.getName())) {
            model.setName("call_type");
        }
        if (StringUtils.isNotEmpty(model.getDepartureTime())) {

        }
        return JSonPagingResult.ofSuccess(count, schedulesDao.select(model));
    }

    public JSonPagingResult<Schedules> listDashboard(PagingModel model) {
        DateTime dt = new DateTime();
        DateTime nDt = dt.withTimeAtStartOfDay();
        Date now = nDt.toDate();
        dt = dt.plusDays(5);
        Date next = dt.withTime(23, 59, 59, 99).toDate();
        ImmutableMap<String, ? extends Serializable> immutableMap = ImmutableMap.of("startTime", now, "endTime", next, "limit", model.getLimit(), "offset", model.getOffset());
        Long count = schedulesDao.countDashboard(immutableMap);
        return JSonPagingResult.ofSuccess(count, schedulesDao.selectDashboard(immutableMap));
    }


    @Transactional
    public void importSchedueles(List<Schedules> schedulesList) {
        for (Schedules schedules : schedulesList) {
            Cruise byName = cruiseDao.findByName(schedules.getCruiseName().trim());
            if(byName == null){
                //create cruise
                byName = new Cruise();
                byName.setName(schedules.getCruiseName().trim());
                cruiseDao.insert(byName);
            }
            schedules.setCruiseId(byName.getId());
            schedulesDao.insert(schedules);
        }
    }

    public Integer updateTaxiOnQueue(Long id, String type) {
        if ("decrease".equals(type)) {
            Integer taxiOnQueue = schedulesDao.getTaxiOnQueue(id);
            if (taxiOnQueue == 0) {
                return 0;
            }
        }
        schedulesDao.updateTaxiOnQueue(ImmutableMap.of("id", id, "type", type));
        return schedulesDao.getTaxiOnQueue(id);
    }

}
