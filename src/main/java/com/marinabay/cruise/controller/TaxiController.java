package com.marinabay.cruise.controller;

import com.google.common.base.Splitter;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.JSonResult;
import com.marinabay.cruise.model.PagingModel;
import com.marinabay.cruise.model.Taxi;
import com.marinabay.cruise.service.TaxiService;
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
public class TaxiController {

    private Logger LOG = LoggerFactory.getLogger(TaxiController.class);

    private final String VIEW_TYPE = "viewType";

    @Autowired
    private TaxiService taxiService;

	@RequestMapping(value = {"/taxi.html"}, method = RequestMethod.GET)
	public String taxi(HttpServletRequest request, ModelMap model) {
        model.addAttribute(VIEW_TYPE, "taxi");
        return "/index";
    }

    @RequestMapping(value = {"/listTaxi.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonPagingResult<Taxi> listTaxi(HttpServletRequest request, PagingModel model) {
        return taxiService.list(model);
    }


    @RequestMapping(value = {"/addTaxi.json"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSonResult addCruise(HttpServletRequest request, Taxi cruise) {
        if (cruise.getId() != null && cruise.getId() > 0) {
            taxiService.update(cruise);
            return JSonResult.ofSuccess("Update cruise success");
        } else {
            if (StringUtils.isNotEmpty(cruise.getName())) {

                if (taxiService.findByName(cruise.getName()) != null) {
                    return JSonResult.ofError("This name is already used");
                } else {
                    taxiService.insert(cruise);
                    return JSonResult.ofSuccess("Add cruise success");
                }
            } else {
                return JSonResult.ofError("Name of cruise can not empty");
            }
        }
    }

    @RequestMapping(value = {"/deleteTaxi.json"}, method = RequestMethod.GET)
    @ResponseBody
    public JSonResult<String> deleteCruise(HttpServletRequest request, String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            try {
                Iterable<String> strings = Splitter.on(",").omitEmptyStrings().split(ids);
                for (String id : strings) {
                    taxiService.deleteByID(Long.valueOf(id));
                }
            } catch (Exception e) {
                LOG.error("", e);
                return JSonResult.ofError("Can not delete users");
            }
        }
        return JSonResult.ofSuccess("Delete success");
    }





}