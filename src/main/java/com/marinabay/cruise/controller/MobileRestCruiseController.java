package com.marinabay.cruise.controller;

import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.CruisePortService;
import com.marinabay.cruise.service.CruiseService;
import com.marinabay.cruise.service.TaxiService;
import com.marinabay.cruise.service.UserService;
import com.marinabay.cruise.utils.RequestUtls;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = {"/m"})
public class MobileRestCruiseController {

    private Logger LOG = LoggerFactory.getLogger(MobileRestCruiseController.class);

    @Autowired
    private CruiseService cruiseService;

    @Autowired
    private CruisePortService cruisePortService;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = {"/login.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
	public JSonResult login(HttpServletRequest request, String username, String password) {
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            User userByEmail = userService.findByUserName(username);
            if (userByEmail != null) {
                if (userByEmail.getPassword().equals(password)) {
                    RequestUtls.logged(request, userByEmail);
                    return JSonResult.ofSuccess(request.getSession().getId());
                } else {
                    return JSonResult.ofError("Password doesn't match", 402);
                }
            } else {
                return JSonResult.ofError("Username not found", 401);
            }
        }
        return JSonResult.ofError("Please input required fields", 400);
    }

    @RequestMapping(value = {"/userInfo.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult userInfo(HttpServletRequest request) {
        return JSonResult.ofSuccess(RequestUtls.getLoggedUser(request));
    }

    @RequestMapping(value = {"/taxis.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult taxis(HttpServletRequest request) {
        return JSonResult.ofSuccess(taxiService.listAll());
    }

    @RequestMapping(value = {"/register.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult register(HttpServletRequest request, User user) {
        return JSonResult.ofSuccess(taxiService.listAll());
    }

    @RequestMapping(value = {"/getCruiseInfo.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult getCruiseInfo(HttpServletRequest request) {
        return JSonResult.ofSuccess(cruisePortService.selectByID(1));
    }

    @RequestMapping(value = {"/getSchedules.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult getSchedules(HttpServletRequest request, String fromdate, String enddate) {
        if (StringUtils.isEmpty(fromdate)) {
            return JSonResult.ofError("startdate is required", 400);
        }

        return JSonResult.ofSuccess(taxiService.listAll());
    }





}