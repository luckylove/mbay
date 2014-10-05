package com.marinabay.cruise.dao;


import com.marinabay.cruise.model.Taxi;

public interface TaxiDao extends GenericDao<Taxi> {

    public Taxi findByName(String name);
}