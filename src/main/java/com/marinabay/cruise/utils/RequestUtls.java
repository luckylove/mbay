package com.marinabay.cruise.utils;

import com.marinabay.cruise.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 8:09 PM
 */
public class RequestUtls {

    private final static String USER_LOGIN = "_USER_LOGIN";


    public static boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null && session.getAttribute(USER_LOGIN) != null;
    }

    public static void logged(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN, user);
    }


    public static User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (User)session.getAttribute(USER_LOGIN);
    }

    public static void clearLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_LOGIN);
        request.removeAttribute("loggedUser");
    }

    public static String date2Str(Date date) {
        DateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sf.format(date);
    }

    public static String date2Str(Date date, DateFormat sf) {
        return sf.format(date);
    }
}
