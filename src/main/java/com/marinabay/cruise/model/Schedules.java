package com.marinabay.cruise.model;

import com.marinabay.cruise.utils.RequestUtls;

import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class Schedules extends GenericModel{

    private Long cruiseId;
    private Date arrivalTime;
    private Date departureTime;
    private Integer passengers;
    private String callType;

    //custom field
    private String cruiseName;

    private String arrivalTimeStr;
    private String departureTimeStr;

    public String getArrivalTimeStr() {
        if (arrivalTime != null) {
            arrivalTimeStr = RequestUtls.date2Str(arrivalTime);
        }
        return arrivalTimeStr;
    }

    public void setArrivalTimeStr(String arrivalTimeStr) {
        this.arrivalTimeStr = arrivalTimeStr;
    }

    public String getDepartureTimeStr() {
        if (departureTime != null) {
            departureTimeStr = RequestUtls.date2Str(departureTime);
        }
        return departureTimeStr;
    }

    public void setDepartureTimeStr(String departureTimeStr) {
        this.departureTimeStr = departureTimeStr;
    }

    public Schedules() {
    }

    public Long getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(Long cruiseId) {
        this.cruiseId = cruiseId;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCruiseName() {
        return cruiseName;
    }

    public void setCruiseName(String cruiseName) {
        this.cruiseName = cruiseName;
    }
}
