package com.marinabay.cruise.controller;

import com.google.common.base.Splitter;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.JSonResult;
import com.marinabay.cruise.model.Notification;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.service.CruiseService;
import com.marinabay.cruise.service.NotificationService;
import com.marinabay.cruise.service.UserGroupService;
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
public class NotificationController {

    private Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    private final String VIEW_TYPE = "viewType";

    @Autowired
    private CruiseService cruiseService;


    @Autowired
    private UserGroupService userGroupService;


    @Autowired
    private NotificationService notificationService;


    @RequestMapping(value = {"/notification.html"}, method = RequestMethod.GET)
	public String notification(HttpServletRequest request, ModelMap model) {
        model.addAttribute(VIEW_TYPE, "notification");
        model.addAttribute("userGroup", userGroupService.listAlll(new PagingModel()));
        return "/index";
    }

    @RequestMapping(value = {"/listNotification.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<Notification> listNotification(HttpServletRequest request, PagingModel model) {
        return notificationService.list(model);
    }


    @RequestMapping(value = {"/sendMessage.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult sendMessage(HttpServletRequest request, Notification message) {

        if (StringUtils.isEmpty(message.getMessage())) {
            return JSonResult.ofError("Message is required");
        }
        if (StringUtils.isEmpty(message.getUserIds())) {
            return JSonResult.ofError("Users is required");
        }

        notificationService.insertAndSend(message);
        return JSonResult.ofSuccess("Send message success");
    }

    @RequestMapping(value = {"/deleteMessage.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteCruise(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    notificationService.deleteByID(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete message");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }





}