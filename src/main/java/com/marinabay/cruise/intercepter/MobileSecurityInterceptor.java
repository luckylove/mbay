package com.marinabay.cruise.intercepter;

import com.marinabay.cruise.model.User;
import com.marinabay.cruise.utils.RequestUtls;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 8:08 PM
 */
public class MobileSecurityInterceptor extends HandlerInterceptorAdapter {

    private final static String LOGIN_URL = "error.json";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!RequestUtls.isLogged(request)) {
            response.sendRedirect(LOGIN_URL);
            return false;
        }
        User loggedUser = RequestUtls.getLoggedUser(request);
        request.setAttribute("loggedUser", loggedUser);
        return true;
    }
}
