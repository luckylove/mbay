package com.marinabay.cruise.service.job;

import com.marinabay.cruise.constant.SEND_STATUS;
import com.marinabay.cruise.dao.NotificationDao;
import com.marinabay.cruise.model.UserNotification;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
public class SMSJob implements Callable {

    private Logger LOG = LoggerFactory.getLogger(SMSJob.class);

    private NotificationDao notificationDao;
    private UserNotification nf;
    private String sendUrl;
    private List<NameValuePair> params;

    public SMSJob(NotificationDao notificationDao, UserNotification nf, String sendUrl, List<NameValuePair> params) {
        this.notificationDao = notificationDao;
        this.nf = nf;
        this.params = params;
        this.sendUrl = sendUrl;
    }
    @Override
    public Object call() throws Exception {
        LOG.info("insert to db {}", nf.getUserId());
        notificationDao.insertUserNotification(nf);
        LOG.info("send sms", sendUrl);
        try {
            //String result = sendGet(sendUrl);
            nf.setStatus(SEND_STATUS.SENT);
            //nf.setSendId(parseText(result));
        } catch (Exception e) {
            LOG.error("",e);
            nf.setStatus(SEND_STATUS.ERROR);
        }
        //update status to db
        notificationDao.updateUserNotification(nf);
        return null;
    }

    private String parseText(String string) {
        if (StringUtils.isNotEmpty(string)) {
            String[] split = string.split(":");
            if (split.length > 1) {
                return split[1].trim();
            }
        }
        return null;
    }

    public String sendGet(String url) throws Exception {
        String result = null;
        HttpClient httpclient = new DefaultHttpClient();
        URIBuilder uriBuilder = new URIBuilder(url);
        for (NameValuePair param : params) {
            uriBuilder.addParameter(param.getName(), param.getValue());
        }

        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        LOG.info("send sms to: " + uri.getQuery());
        HttpResponse response = httpclient.execute(httpGet);
        try {
            StatusLine statusLine = response.getStatusLine();
            LOG.info("Respone status: " + statusLine.getStatusCode());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }
}
