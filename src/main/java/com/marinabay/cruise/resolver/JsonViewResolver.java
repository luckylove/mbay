package com.marinabay.cruise.resolver;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

/**
 * User: son.nguyen
 * Date: 7/14/14
 * Time: 5:42 PM
 */
public class JsonViewResolver implements ViewResolver , Ordered {

    private int order = Integer.MAX_VALUE;  // default: same as non-Ordered
    /**
     * Get the view to use.
     *
     * @return Always returns an instance of {@link org.springframework.web.servlet.view.json.MappingJackson2JsonView}.
     */
    public View resolveViewName(String viewName, Locale locale)
            throws Exception {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.getObjectMapper().configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        view.getObjectMapper().setDateFormat(new ISO8601DateFormat());
        view.setPrettyPrint(true);
        return view;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
