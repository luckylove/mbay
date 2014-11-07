package com.marinabay.cruise.controller;

import com.google.common.base.Splitter;
import com.marinabay.cruise.model.*;
import com.marinabay.cruise.service.CruiseService;
import com.marinabay.cruise.service.SchedulesService;
import com.marinabay.cruise.service.imports.impl.ExcelReader;
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
import java.util.List;

@Controller
public class SchedulesController {

    private Logger LOG = LoggerFactory.getLogger(SchedulesController.class);

    private final String VIEW_TYPE = "viewType";

    @Autowired
    private CruiseService cruiseService;

    @Autowired
    private SchedulesService schedulesService;

	@RequestMapping(value = {"/schedules.html"}, method = RequestMethod.GET)
	public String userGroup(HttpServletRequest request, ModelMap model) {
        model.addAttribute(VIEW_TYPE, "schedules");
        model.addAttribute("cruises", cruiseService.list(new PagingModel()).getRows());
        return "/index";
    }

    @RequestMapping(value = {"/listDashboard.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<Schedules> listDashboard(HttpServletRequest request, PagingModel model) {
        return schedulesService.listDashboard(model);
    }

    @RequestMapping(value = {"/listSchedules.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<Schedules> listSchedules(HttpServletRequest request, SchelduePagingModel model) {
        User loggedUser = RequestUtls.getLoggedUser(request);
        if (loggedUser != null && loggedUser.isQCordinator()) {
            model.setToday(true);
        }
        return schedulesService.list(model);
    }


    @RequestMapping(value = {"/addSchedules.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult addSchedules(HttpServletRequest request, Schedules schedules) {
        if (schedules.getId() != null && schedules.getId() > 0) {
            schedulesService.update(schedules);
            return JSonResult.ofSuccess("Update schedules success");
        } else {
            schedulesService.insert(schedules);
            return JSonResult.ofSuccess("Add cruise success");
        }
    }

    @RequestMapping(value = {"/deleteSchedules.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteSchedules(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    schedulesService.deleteByID(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete users");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }

    @RequestMapping(value = "/importSchedules.html", method = RequestMethod.POST)
    public String importSchedules(FileUploadBean uploadItem) {
        try {
            List<Schedules> endResults = ExcelReader.getEndResults(uploadItem.getFile().getInputStream(), uploadItem.getFile().getOriginalFilename());
            schedulesService.importSchedueles(endResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/schedules.html";
    }


    @RequestMapping(value = {"/addTaxiOnQueue.json"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JSonResult addTaxiOnQueue(HttpServletRequest request, Long id, String type) {
        return JSonResult.ofSuccess(schedulesService.updateTaxiOnQueue(id, type));
    }


}