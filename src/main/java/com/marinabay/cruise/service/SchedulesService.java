package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.marinabay.cruise.dao.SchedulesDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.Schedules;
import com.marinabay.cruise.model.SchelduePagingModel;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class SchedulesService extends GenericService<Schedules>{

    @Autowired
    private SchedulesDao schedulesDao;

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

}
