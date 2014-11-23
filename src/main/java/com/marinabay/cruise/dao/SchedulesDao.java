package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Schedules;

import java.util.List;
import java.util.Map;

public interface SchedulesDao extends GenericDao<Schedules> {

    public List<Schedules> selectDashboard(Map map);

    public List<Schedules> selectMobile(Map map);

    public Long countDashboard(Map map);

    public void updateTaxiOnQueue(Map map);

    public void updatePassOnQueue(Map map);

    public Integer getTaxiOnQueue(Long id);

    public Integer getPassOnQueue(Long id);

}