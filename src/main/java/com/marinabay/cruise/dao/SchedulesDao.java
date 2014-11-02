package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Schedules;

import java.util.List;
import java.util.Map;

public interface SchedulesDao extends GenericDao<Schedules> {

    public List<Schedules> selectDashboard(Map map);

    public Long countDashboard(Map map);

    public void updateTaxiOnQueue(Map map);

    public Integer getTaxiOnQueue(Long id);

}