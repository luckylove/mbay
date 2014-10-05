package com.marinabay.cruise.model;

import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:12 PM
 */
public class GenericModel {

    private Long id;
    private Date regDate;
    private Date modDate;

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
