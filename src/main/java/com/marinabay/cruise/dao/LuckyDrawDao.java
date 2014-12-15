package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.LuckyDraw;

import java.util.List;

public interface LuckyDrawDao extends GenericDao<LuckyDraw> {

    public void randomUser(Long id);

    public List<LuckyDraw> selectNotRun();

}