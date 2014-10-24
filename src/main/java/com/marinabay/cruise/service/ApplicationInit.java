package com.marinabay.cruise.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: son.nguyen
 * Date: 8/20/14
 * Time: 10:52 AM
 */

@Service
public class ApplicationInit {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationInit.class);

    private static NotificationService notificationService;


    @Autowired
    public static void setNotificationService(NotificationService notificationService) {
        ApplicationInit.notificationService = notificationService;
    }

    public static void init(){
        LOG.info("**************************Init application service***************************");
        LOG.info("**************************Done init application service***************************");
    }


    public static void shutdown(){
        LOG.info("**************************Shutdown application service**************************");
        notificationService.shutDownService();
        LOG.info("**************************Done shutdown application service**************************");
    }

}
