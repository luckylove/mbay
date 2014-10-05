package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Cruise;

public interface CruiseDao extends GenericDao<Cruise> {

    public Cruise findByName(String name);
}