package com.marinabay.cruise.listener;

import com.marinabay.cruise.service.ApplicationInit;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * User: son.nguyen
 * Date: 7/22/14
 * Time: 9:58 AM
 */
public class SnsContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.initWebApplicationContext(event.getServletContext());
        ApplicationInit.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        ApplicationInit.shutdown();
    }


}
