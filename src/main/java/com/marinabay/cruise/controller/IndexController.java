package com.marinabay.cruise.controller;

import com.marinabay.cruise.model.CruisePort;
import com.marinabay.cruise.model.JSonResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.User;
import com.marinabay.cruise.service.CruisePortService;
import com.marinabay.cruise.service.TaxiService;
import com.marinabay.cruise.service.UserService;
import com.marinabay.cruise.utils.RequestUtls;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller

public class IndexController {

    private Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CruisePortService cruisePortService;

    @Autowired
    private TaxiService taxiService;


	@RequestMapping(value = {"/index.html"}, method = RequestMethod.GET)
	public String home(HttpServletRequest request, ModelMap model) {
        User loggedUser = RequestUtls.getLoggedUser(request);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("viewType", "profile");
        model.addAttribute("taxis", taxiService.listAll());
        return "/index";
    }

    @RequestMapping(value = {"/profile.html"}, method = RequestMethod.GET)
    public String profile(HttpServletRequest request, ModelMap model) {
        model.addAttribute("loggedUser", RequestUtls.getLoggedUser(request));
        model.addAttribute("viewType", "profile");
        model.addAttribute("taxis", taxiService.listAll());
        return "/index";
    }

    @RequestMapping(value = {"/cruisePort.html"}, method = RequestMethod.GET)
    public String cruisePort(HttpServletRequest request, ModelMap model) {
        model.addAttribute("viewType", "cruisePort");
        return "/index";
    }


    @RequestMapping(value = {"/getCruisePort.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<User> getCruisePort(HttpServletRequest request, PagingModel model) {
        return JSonResult.ofSuccess(cruisePortService.selectByID(1));
    }

    @RequestMapping(value = {"/updateCruisePort.json"}, method = RequestMethod.POST)
    @ResponseBody
    public JSonResult<User> updateCruisePort(HttpServletRequest request, CruisePort cruisePort) {
        cruisePortService.update(cruisePort);
        return JSonResult.ofSuccess("Update is ok");
    }


    @RequestMapping(value = {"/getLogUser.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<User> getLogUser(HttpServletRequest request, PagingModel model) {
        User loggedUser = RequestUtls.getLoggedUser(request);
        return JSonResult.ofSuccess(userService.selectByID(loggedUser.getId()));
    }

    @RequestMapping(value = {"/updateProfile.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult updateProfile(HttpServletRequest request, User user) {
        if (user.getId() != null && user.getId() > 0) {
            userService.update(user);
            RequestUtls.logged(request, user);
            return JSonResult.ofSuccess("Update user success");
        } else {
            return JSonResult.ofError("Some of fields can not empty");
        }
    }

    @RequestMapping(value = {"/login.html"}, method = RequestMethod.GET)
	public String login(ModelMap model, String st, String error) {
        model.addAttribute("st", st);
        model.addAttribute("error", error);
        model.addAttribute("taxis", taxiService.listAll());
        return "/login";
    }

    @RequestMapping(value = {"/logout.html"}, method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        RequestUtls.clearLoggedUser(request);
        return "/login";
    }

    @RequestMapping(value = {"/loginSubmit.html"}, method = RequestMethod.POST)
	public String loginSubmit(HttpServletRequest request, ModelMap model, String userName, String password) {
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            User userByEmail = userService.findByUserName(userName);
            if (userByEmail != null) {
                if (userByEmail.getPassword().equals(password)) {
                    RequestUtls.logged(request, userByEmail);
                    return "redirect:index.html";
                }
            }
        }
        return "redirect:login.html?login=false";
    }

    @RequestMapping(value = {"/error.html"}, method = RequestMethod.GET)
    public String error(ModelMap model, Integer page) {
        return "/error";
    }




    @RequestMapping(value = {"/activeUser.html"}, method = RequestMethod.GET)
    public String activeUser(HttpServletRequest request, ModelMap model) {
        model.addAttribute("viewType", "activeUser");
        return "/index";
    }

    @RequestMapping(value = {"/map.html"}, method = RequestMethod.GET)
    public String map(HttpServletRequest request, ModelMap model) {
        model.addAttribute("viewType", "activeUser");
        return "/map";
    }





}