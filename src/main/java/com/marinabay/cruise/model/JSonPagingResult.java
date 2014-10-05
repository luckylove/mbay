package com.marinabay.cruise.model;

import java.util.List;

/**
 * User: son.nguyen
 * Date: 9/24/14
 * Time: 10:29 PM
 */
public class JSonPagingResult<T> {

    private long total;
    private List<T> rows;

   public static JSonPagingResult ofSuccess(long total, List result) {
        JSonPagingResult rs = new JSonPagingResult();
        rs.setTotal(total);
        rs.setRows(result);
        return rs;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
