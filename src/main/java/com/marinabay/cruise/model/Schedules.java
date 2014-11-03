package com.marinabay.cruise.model;

import com.marinabay.cruise.utils.RequestUtls;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private Integer taxiOnQueue;
    private String callType;

    //custom field
    private String cruiseName;

    private String arrivalTimeStr;
    private String departureTimeStr;
    private boolean isToday;

    public boolean isToday() {
        if (departureTime != null) {
            return DateUtils.isSameDay(Calendar.getInstance().getTime(), departureTime);
        }
        return false;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public Integer getTaxiOnQueue() {
        return taxiOnQueue;
    }

    public void setTaxiOnQueue(Integer taxiOnQueue) {
        this.taxiOnQueue = taxiOnQueue;
    }

    public String getArrivalTimeStr() {
        if (arrivalTime != null) {
            arrivalTimeStr = RequestUtls.date2Str(arrivalTime,  new SimpleDateFormat("yyyy/MM/dd HH:mm"));
        }
        return arrivalTimeStr;
    }

    public void setArrivalTimeStr(String arrivalTimeStr) {
        this.arrivalTimeStr = arrivalTimeStr;
    }

    public String getDepartureTimeStr() {
        if (departureTime != null) {
            departureTimeStr = RequestUtls.date2Str(departureTime,  new SimpleDateFormat("yyyy/MM/dd HH:mm"));
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

    public Date toDate(String departureTimeStr) {
        if (StringUtils.isNotEmpty(departureTimeStr)) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                return sf.parse(departureTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void setData(int cell, String data){
        //mapping data here
        switch (cell){
            case 0:
                this.cruiseName = data;
                break;
            case 1:
                this.arrivalTime = toDate(data);
                break;

            case 2:
                this.departureTime = toDate(data);
                break;

            case 4:
                this.callType = data;
                break;
        }
    }
}
