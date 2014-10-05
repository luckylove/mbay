package com.marinabay.cruise.intercepter;

import com.marinabay.cruise.utils.RequestUtls;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 8:08 PM
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private final static String LOGIN_URL = "login.html";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!RequestUtls.isLogged(request)) {
            response.sendRedirect(LOGIN_URL);
            return false;
        }
        //add to request attribute
        request.setAttribute("loggedUser", RequestUtls.getLoggedUser(request));
        return true;
    }
}
