package com.marinabay.cruise.constant;

/**
 * User: son.nguyen
 * Date: 9/22/14
 * Time: 10:10 PM
 */
public enum SENDTYPE {

    PUSH("InApp"), SMS("NonApp") ;

    String view;

    SENDTYPE(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
