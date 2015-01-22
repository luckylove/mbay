package com.marinabay.cruise.controller;

import com.google.common.base.Splitter;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.LuckyDrawService;
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

@Controller
public class LuckyDrawController {

    private Logger LOG = LoggerFactory.getLogger(LuckyDrawController.class);

    private final String VIEW_TYPE = "viewType";

    @Autowired
    private LuckyDrawService luckyDrawService;

    @Autowired
    private UserService userService;

	@RequestMapping(value = {"/luckyDraw.html"}, method = RequestMethod.GET)
	public String luckyDraw(HttpServletRequest request, ModelMap model, Long id) {
        if (id != null) {
            model.addAttribute(VIEW_TYPE, "luckyDrawUser");
            model.addAttribute("luckyId", id);
        } else {
            model.addAttribute(VIEW_TYPE, "luckydraw");
        }
        return "/index";
    }

    @RequestMapping(value = {"/luckyDrawUser.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<User>  luckyDrawUser(HttpServletRequest request, ModelMap model, Long luckyId) {
        return userService.listLuckyUser(luckyId);
    }

    @RequestMapping(value = {"/listLuckyDraw.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<LuckyDraw> listLuckyDraw(HttpServletRequest request, PagingModel model) {
        return luckyDrawService.list(model);
    }

    @RequestMapping(value = {"/addLuckyDraw.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult addLuckyDraw(HttpServletRequest request, LuckyDraw luckyDraw) {
        if (StringUtils.isNotEmpty(luckyDraw.getContent()) && luckyDraw.getTriggerTime() != null) {
            luckyDrawService.insert(luckyDraw);
            return JSonResult.ofSuccess("Insert is ok");
        } else {
            return JSonResult.ofError("All fields can not empty");
        }
    }

    @RequestMapping(value = {"/deleteLuckyDraw.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteLuckyDraw(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    luckyDrawService.deleteByID(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete luckyDraw");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }








}