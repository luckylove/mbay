package com.marinabay.cruise.controller;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.marinabay.cruise.constant.ROLE;
import com.marinabay.cruise.constant.USERTYPE;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.ActiveUserService;
import com.marinabay.cruise.service.UserGroupService;
import com.marinabay.cruise.service.UserService;
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
import java.util.List;

@Controller
public class UserController {

    private Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final String VIEW_TYPE = "viewType";
    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ActiveUserService activeUserService;


	@RequestMapping(value = {"/userGroup.html"}, method = RequestMethod.GET)
	public String userGroup(HttpServletRequest request, ModelMap model) {
        model.addAttribute(VIEW_TYPE, "userGroup");
        return "/index";
    }

    @RequestMapping(value = {"/addUserGroup.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult addUserGroup(HttpServletRequest request, UserGroup userGroup) {
        if (userGroup.getId() != null && userGroup.getId() > 0) {
            userGroupService.update(userGroup);
            return JSonResult.ofSuccess("Update group success");
        } else {
            if (StringUtils.isNotEmpty(userGroup.getName())) {

                if (userGroupService.findByName(userGroup.getName()) != null) {
                    return JSonResult.ofError("This name is already used");
                } else {
                    userGroupService.insert(userGroup);
                    return JSonResult.ofSuccess("Add group success");
                }
            } else {
                return JSonResult.ofError("Name of group can not empty");
            }
        }
    }

    @RequestMapping(value = {"/listUserGroup.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<UserGroup> listUserGroup(HttpServletRequest request, PagingModel model) {
        return userGroupService.list(model);
    }


    @RequestMapping(value = {"/deleteUserGroup.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteUserGroup(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    //update user
                    Long aLong = Long.valueOf(id);
                    userService.resetUserGroup(aLong);
                    userGroupService.deleteByID(aLong);
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete usergroup");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }

    /**
     * part of user
     * @param request
     * @param model
     * @return
     */

    @RequestMapping(value = {"/userList.html"}, method = RequestMethod.GET)
    public String userList(HttpServletRequest request, ModelMap model) {
        model.addAttribute(VIEW_TYPE, "userList");
        //load all user group
        model.addAttribute("userGroup", userGroupService.listAlll(new PagingModel()));
        return "/index";
    }

    @RequestMapping(value = {"/addUser.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult addUser(HttpServletRequest request, User user) {
        if (user.getId() != null && user.getId() > 0) {
            userService.update(user);
            return JSonResult.ofSuccess("Update user success");
        } else {
            if (StringUtils.isNotEmpty(user.getEmail()) || StringUtils.isNotEmpty(user.getUserName())) {
                if (userService.findUserByEmail(user.getEmail()) != null) {
                    return JSonResult.ofError("This email is already used");
                } else {
                    user.setRole(ROLE.USER);
                    user.setUserType(USERTYPE.WEB);
                    userService.insert(user);
                    return JSonResult.ofSuccess("Add group success");
                }
            } else {
                return JSonResult.ofError("Some of fields can not empty");
            }
        }
    }


    @RequestMapping(value = {"/addUser.html"}, method = RequestMethod.POST)
    public String registerUser(HttpServletRequest request, ModelMap model, User user) {
        if (StringUtils.isNotEmpty(user.getEmail()) || StringUtils.isNotEmpty(user.getUserName())) {
            if (userService.findUserByEmail(user.getEmail()) != null) {
                return "redirect:/login.html?st=register&error=1";
            } else {
                user.setRole(ROLE.USER);
                user.setUserType(USERTYPE.WEB);
                userService.insert(user);
                return "redirect:/login.html";
            }
        } else {
            model.addAttribute("st", "register");
            model.addAttribute("error", "Some fields is required");
            return "redirect:/login.html?st=register&error=2";
        }
    }

    @RequestMapping(value = {"/listUser.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<User> listUser(HttpServletRequest request, PagingModel model) {
        return userService.list(model);
    }

    @RequestMapping(value = {"/deleteUser.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteUser(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    userService.deleteByID(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete users");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }

    @RequestMapping(value = {"/assignUserGroup.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> assignUserGroup(HttpServletRequest request, String ids, Long groupId) {
        if (StringUtils.isNotEmpty(ids) && groupId != null) {
            try {
                List<String> strings = Splitter.on(",").omitEmptyStrings().splitToList(ids);
                List<Long> longs = Lists.transform(strings, new Function<String, Long>() {
                    @Override
                    public Long apply(String input) {
                        return Long.valueOf(input);
                    }
                });
                userService.assignGroup(longs, groupId);
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not assign users");
            }
        }
        return JSonResult.ofSuccess("Assign success");
    }



    @RequestMapping(value = {"/listUserByLicense.json"}, method = RequestMethod.GET)
    @ResponseBody
    public List<User> listUserByLicense(HttpServletRequest request, String license) {
        return userService.selectByLicense(license);
    }

    @RequestMapping(value = {"/addActiveUser.json"}, method = RequestMethod.POST)
    @ResponseBody
    public JSonResult<String> addActiveUser(HttpServletRequest request, Long userId) {
        activeUserService.insertOrUpdate(userId);
        return JSonResult.ofSuccess("");
    }

    @RequestMapping(value = {"/listActiveUser.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<ActiveUser> listActiveUser(HttpServletRequest request, PagingModel model) {
        return activeUserService.list(model);
    }



}