package com.marinabay.cruise.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTimeUtils;

/**
 * User: son.nguyen
 * Date: 9/21/14
 * Time: 9:19 PM
 */
public class SchedulesHistory {


    private String datetime;
    private String name;
    private String importKey;

    public void parse() {
        if (StringUtils.isNotEmpty(importKey)) {
            String[] split = importKey.split("\\+\\+");
            this.datetime = split[0].substring(0, 4) + "/" + split[0].substring(4, 6)+ "/" + split[0].substring(6, 8)
                    + " " + split[0].substring(8, 10) + ":" + split[0].substring(10, 12) ;
            this.name = split[1];
        }
    }

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
