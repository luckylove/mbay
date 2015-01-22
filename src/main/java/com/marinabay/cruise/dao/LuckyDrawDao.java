package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.LuckyDraw;

import java.util.List;

public interface LuckyDrawDao extends GenericDao<LuckyDraw> {

    public Long randomUser(List<Long> userIds);

    public void updateLuckyUser(LuckyDraw luckyDraw);

    public List<LuckyDraw> selectNotRun();

}