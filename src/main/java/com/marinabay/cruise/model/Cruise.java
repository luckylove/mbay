package com.marinabay.cruise.model;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class Cruise extends GenericModel{

    private String name;
    private Integer passenger;

    public Integer getPassenger() {
        return passenger;
    }

    public void setPassenger(Integer passenger) {
        this.passenger = passenger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
