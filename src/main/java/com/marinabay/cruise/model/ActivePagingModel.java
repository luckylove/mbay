package com.marinabay.cruise.model;

/**
 * User: son.nguyen
 * Date: 9/24/14
 * Time: 10:29 PM
 */
public class ActivePagingModel extends PagingModel{

    private String startDate;
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
