package com.marinabay.cruise.model;

import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/24/14
 * Time: 10:29 PM
 */
public class UserPagingModel extends PagingModel {


    private String notInIds;
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNotInIds() {
        return notInIds;
    }

    public List<Long> getNotInIdsArr() {
        List<Long> rs = new ArrayList<Long>();
        if (StringUtils.isNotEmpty(notInIds)) {
            List<String> strings = Splitter.on(',').trimResults().splitToList(notInIds);
            for (String id : strings) {
                rs.add(Long.valueOf(id));
            }
        }
        return rs;
    }

    public void setNotInIds(String notInIds) {
        this.notInIds = notInIds;
    }
}
