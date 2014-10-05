package com.marinabay.cruise.model;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class CruisePort extends GenericModel{

    private String name;
    private String address;
    private String longti;
    private String lati;

    public CruisePort() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongti() {
        return longti;
    }

    public void setLongti(String longti) {
        this.longti = longti;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }
}
