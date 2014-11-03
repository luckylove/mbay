package com.marinabay.cruise.constant;

/**
 * User: son.nguyen
 * Date: 9/22/14
 * Time: 10:10 PM
 */
public enum USERTYPE {

    WEB("NonApp"), MOBILE("InApp") ;

    String view;

    USERTYPE(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
