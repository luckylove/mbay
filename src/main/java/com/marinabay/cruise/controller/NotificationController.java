package com.marinabay.cruise.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.NotificationService;
import com.marinabay.cruise.service.UserGroupService;
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

import javax.management.ImmutableDescriptor;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController {

    private Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    private final String VIEW_TYPE = "viewType";

    @Autowired
    private UserService userService;


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

    @RequestMapping(value = {"/sentUserList.html"}, method = RequestMethod.GET)
	public String sentUserList(HttpServletRequest request, ModelMap model, Long sendId) {
        model.addAttribute(VIEW_TYPE, "sentUserList");
        model.addAttribute("sendId", sendId);
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

        notificationService.insertAndSend(message, RequestUtls.getLoggedUser(request));
        return JSonResult.ofSuccess("Send message success");
    }

    @RequestMapping(value = {"/deleteMessage.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteCruise(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    notificationService.deleteUserNotification(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete message");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }

    @RequestMapping(value = {"/listUserOfGroup.json"}, method = RequestMethod.GET)
    @ResponseBody
    public List<User> listUser(HttpServletRequest request, Long groupId, String userType) {
        return userService.selectAllByGroup(groupId, userType);
    }

    @RequestMapping(value = {"/inactivePushNotification.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<Boolean> inactivePushNotification(HttpServletRequest request, Long id) {
        notificationService.inactiveNotification(id);
        return JSonResult.ofSuccess(true);
    }

    @RequestMapping(value = {"/sentUserList.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<UserNotificationView>  sentUserList(HttpServletRequest request, PagingModel model) {
        return notificationService.getAllSentNotification(model);
    }





}