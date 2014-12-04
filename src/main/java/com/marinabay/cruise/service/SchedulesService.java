package com.marinabay.cruise.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.marinabay.cruise.dao.CruiseDao;
import com.marinabay.cruise.dao.SchedulesDao;
import com.marinabay.cruise.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    public void importSchedueles(List<Schedules> schedulesList, String fileName) {
        String yyyyMMddHHmmss = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        if (StringUtils.isNotEmpty(fileName)) {
            int indexOf = fileName.lastIndexOf(".");
            if (indexOf > 1) {
                yyyyMMddHHmmss += "++" + fileName.substring(0, indexOf).replaceAll("[\\s\\+]","");
            }
        }
        for (Schedules schedules : schedulesList) {
            Cruise byName = cruiseDao.findByName(schedules.getCruiseName().trim());
            if (byName == null) {
                //create cruise
                byName = new Cruise();
                byName.setName(schedules.getCruiseName().trim());
                cruiseDao.insert(byName);
            }
            schedules.setImportKey(yyyyMMddHHmmss);
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

    public Integer updatePassOnQueue(Long id, String type) {
        if ("decrease".equals(type)) {
            Integer taxiOnQueue = schedulesDao.getPassOnQueue(id);
            if (taxiOnQueue == 0) {
                return 0;
            }
        }
        schedulesDao.updatePassOnQueue(ImmutableMap.of("id", id, "type", type));
        return schedulesDao.getPassOnQueue(id);
    }

    public List<Schedules> listMobile(String startdate, String enddate) {
        HashMap<Object,Object> hashMap = Maps.newHashMap();
        hashMap.put("startdate", startdate);
        hashMap.put("enddate", enddate);
        return schedulesDao.selectMobile(hashMap);
    }

    public List<Schedules> listCurrentMobile(int page, int limit) {
        HashMap<Object,Object> hashMap = Maps.newHashMap();
        hashMap.put("offset", (page-1)*limit);
        hashMap.put("limit", limit);
        return schedulesDao.selectCurrentMobile(hashMap);
    }


    public  JSonPagingResult<SchedulesHistory> selectHistory(){
        List<SchedulesHistory> strings = schedulesDao.selectHistory();
        List<SchedulesHistory> results = new ArrayList<SchedulesHistory>(strings.size());
        if (!strings.isEmpty()) {
            for (SchedulesHistory key : strings) {
                key.parse();
                results.add(key);
            }
        }
        return JSonPagingResult.ofSuccess(strings.size(), results);
    }

    public void removeSchedule(String key) {
        schedulesDao.removeSchedule(key);
    }

}
