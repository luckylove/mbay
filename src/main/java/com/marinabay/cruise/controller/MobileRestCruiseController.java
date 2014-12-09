package com.marinabay.cruise.controller;

import com.marinabay.cruise.constant.ROLE;
import com.marinabay.cruise.constant.USERTYPE;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.*;
import com.marinabay.cruise.utils.RequestUtls;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    private SchedulesService schedulesService;

    @Autowired
    private CruisePortService cruisePortService;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private SimpleMailMessage alertMailMessage;

    @RequestMapping(value = {"/error.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult error(HttpServletRequest request) {
        return JSonResult.ofError("You need login!", 500);
    }

    @RequestMapping(value = {"/login.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
	public JSonResult login(HttpServletRequest request, String username, String password, String pushtoken, String devicetype) {
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            User userByEmail = userService.findByUserName(username);
            if (userByEmail != null) {
                if (userByEmail.getPassword().equals(password)) {
                    RequestUtls.logged(request, userByEmail);
                    userService.updateToken(userByEmail.getId(), pushtoken, devicetype);
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
        User loggedUser = RequestUtls.getLoggedUser(request);
        return JSonResult.ofSuccess(userService.selectByID(loggedUser.getId()));
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
    public JSonResult getSchedules(HttpServletRequest request, String startdate, String enddate) {
        if (StringUtils.isEmpty(startdate) || StringUtils.isEmpty(enddate)) {
            return JSonResult.ofError("startdate, enddate is required", 400);
        }
        try {
            return JSonResult.ofSuccess(schedulesService.listMobile(startdate, enddate));
        } catch (Exception e) {
            LOG.error("", e);
            return JSonResult.ofError("Has error form server", 401);
        }
    }

    @RequestMapping(value = {"/pageShedules.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult pageShedules(HttpServletRequest request, Integer page, Integer limit) {
        if (page== null || page == 0) {
            page = 1;
        }
        if (limit== null ||limit == 0) {
            limit = 10;
        }
        try {
            return JSonResult.ofSuccess(schedulesService.listCurrentMobile(page, limit));
        } catch (Exception e) {
            LOG.error("", e);
            return JSonResult.ofError("Has error form server", 401);
        }
    }



    @RequestMapping(value = {"/addUser.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult registerUser(HttpServletRequest request, ModelMap model, User user) {

        if (StringUtils.isEmpty(user.getUserName())) {
            return JSonResult.ofError("Username can not empty", 400);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return JSonResult.ofError("Password can not empty", 400);
        }
        if (StringUtils.isNotEmpty(user.getUserName()) && userService.findByUserName(user.getUserName()) != null) {
            return JSonResult.ofError("Duplicate username", 400);
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && userService.findUserByEmail(user.getEmail()) != null) {
            return JSonResult.ofError("Duplicate email", 400);
        }

        try {
            user.setRole(ROLE.USER);
            user.setUserType(USERTYPE.MOBILE);
            userService.insert(user);
            return JSonResult.ofSuccess(user);
        } catch (Exception e) {
            LOG.error("",e);
            return JSonResult.ofError(e.getMessage());
        }
    }

    @RequestMapping(value = {"/updateUser.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult updateUser(HttpServletRequest request, ModelMap model, User user) {
        try {
            User loggedUser = RequestUtls.getLoggedUser(request);
            if (loggedUser == null) {
                return JSonResult.ofError("Please login", 400);
            }
            User dbUser = userService.selectByID(loggedUser.getId());

            if (StringUtils.isNotEmpty(user.getUserName())) {
                if (userService.checkDupUsername(loggedUser.getId(), user.getUserName()) != null) {
                    return JSonResult.ofError("Username is duplicated", 400);
                } else {
                    dbUser.setUserName(user.getUserName());
                }
            }
            if (StringUtils.isNotEmpty(user.getEmail())) {
                if (userService.checkDupEmail(loggedUser.getId(), user.getEmail()) != null) {
                    return JSonResult.ofError("Email is duplicated", 400);
                } else {
                    dbUser.setEmail(user.getEmail());
                }
            }
            if (StringUtils.isNotEmpty(user.getPassword())) {
                dbUser.setPassword(user.getPassword());
            }
            if (StringUtils.isNotEmpty(user.getName())) {
                dbUser.setName(user.getName());
            }
            if (StringUtils.isNotEmpty(user.getMobile())) {
                dbUser.setMobile(user.getMobile());
            }

            if (StringUtils.isNotEmpty(user.getUiNum())) {
                dbUser.setUiNum(user.getUiNum());
            }

            if (StringUtils.isNotEmpty(user.getTaxiLicense())) {
                dbUser.setTaxiLicense(user.getTaxiLicense());
            }

            if (StringUtils.isNotEmpty(user.getTaxiCompany())) {
                dbUser.setTaxiCompany(user.getTaxiCompany());
            }
            userService.update(dbUser);
            return JSonResult.ofSuccess(userService.selectByID(loggedUser.getId()));
        } catch (Exception e) {
            LOG.error("",e);
            return JSonResult.ofError(e.getMessage());
        }
    }

    @RequestMapping(value = {"/forgotPass.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult forgotPass(HttpServletRequest request, String email) {

        if (StringUtils.isEmpty(email)) {
            return JSonResult.ofError("Email can not empty", 400);
        }
        User userByEmail = userService.findUserByEmail(email);
        if (userByEmail == null) {
            return JSonResult.ofError("Can not found user with this email: " + email);
        }

        try {
            String random = RandomStringUtils.randomAlphabetic(5);
            LOG.info("Change passord {}", random);
            //send mail
            SimpleMailMessage mailMessage = new SimpleMailMessage(alertMailMessage);
            mailMessage.setSubject("[Cruise recovery password]");
            mailMessage.setText(String.format("Here is your new password: %s", random));
            mailMessage.setTo(email);
            mailSender.send(mailMessage);
            userService.updatePassword(userByEmail.getId(), random);

            return JSonResult.ofSuccess("Mail is sent");
        } catch (Exception e) {
            LOG.error("",e);
            return JSonResult.ofError(e.getMessage());
        }
    }

    @RequestMapping(value = {"/enablePushNotification.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult enablePushNotification(HttpServletRequest request,  Boolean enable) {
        User loggedUser = RequestUtls.getLoggedUser(request);
        User user = userService.selectByID(loggedUser.getId());
        if (user == null) {
            return JSonResult.ofError("User not found", 400);
        }
        //update
        userService.updatePushNotification(user.getId(), enable);
        return JSonResult.ofSuccess("Update is ok");
    }

    @RequestMapping(value = {"/surCharge.html"}, method = RequestMethod.GET)
    public String surCharge(HttpServletRequest request, ModelMap model) {
        model.addAttribute("type", "surcharge");
        model.addAttribute("data", cruisePortService.selectByID(1));
        return "/staticContent";
    }

    @RequestMapping(value = {"/direction.html"}, method = RequestMethod.GET)
    public String direction(HttpServletRequest request, ModelMap model) {
        model.addAttribute("type", "direction");
        model.addAttribute("data", cruisePortService.selectByID(1));
        return "/staticContent";
    }

    @RequestMapping(value = {"/information.html"}, method = RequestMethod.GET)
    public String information(HttpServletRequest request, ModelMap model) {
        model.addAttribute("type", "information");
        model.addAttribute("data", cruisePortService.selectByID(1));
        return "/staticContent";
    }

    @RequestMapping(value = {"/aboutus.html"}, method = RequestMethod.GET)
    public String aboutus(HttpServletRequest request, ModelMap model) {
        model.addAttribute("type", "aboutus");
        model.addAttribute("data", cruisePortService.selectByID(1));
        return "/staticContent";
    }

    @RequestMapping(value = {"/luckydraw.html"}, method = RequestMethod.GET)
    public String luckydraw(HttpServletRequest request, ModelMap model) {
        model.addAttribute("type", "luckydraw");
        model.addAttribute("data", cruisePortService.selectByID(1));
        return "/staticContent";
    }
}