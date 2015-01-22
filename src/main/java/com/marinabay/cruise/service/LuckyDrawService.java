package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.LuckyDrawDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.LuckyDraw;
import com.marinabay.cruise.model.PagingModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: son.nguyen
 * Date: 9/23/14
 * Time: 7:15 PM
 */
@Service
public class LuckyDrawService extends GenericService<LuckyDraw>{

    private Logger LOG = LoggerFactory.getLogger(LuckyDrawService.class);

    @Autowired
    private LuckyDrawDao luckyDrawDao;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public LuckyDrawDao getDao() {
        return luckyDrawDao;
    }


    @PostConstruct
    public void init() {
        List<LuckyDraw> luckyDraws = luckyDrawDao.selectNotRun();
        for (final LuckyDraw luckyDraw : luckyDraws) {
            final Long luckyId = luckyDraw.getId();
            final int users = luckyDraw.getNumberUser();
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    LOG.info("*************** Trigger Lucky Draw: {} {} ******************", luckyId, users);
                    List<Long> userids = new ArrayList<Long>(users);
                    for (int i = 0; i < users; i++) {
                        Long userId = luckyDrawDao.randomUser(userids);
                        if (userId != null && userId > 0) {
                            userids.add(userId);
                        }
                    }
                    luckyDraw.setUserIds(StringUtils.join(userids, ","));
                    luckyDrawDao.updateLuckyUser(luckyDraw);
                    LOG.info("*************** Done trigger Lucky Draw: {} - result {} ******************", luckyId, userids.size());
                }
            }, luckyDraw.getTriggerTime());
        }
    }

    public JSonPagingResult<LuckyDraw> list(PagingModel model) {
        Long count = luckyDrawDao.count(model);
        List<LuckyDraw> userGroups = luckyDrawDao.select(model);
        return JSonPagingResult.ofSuccess(count, userGroups);
    }

    public List<LuckyDraw> listAll() {
        return luckyDrawDao.select(new PagingModel());
    }

    public void insert(final LuckyDraw luckyDraw) {
        luckyDrawDao.insert(luckyDraw);
        final int users = luckyDraw.getNumberUser();
        //put it in schedules
        final Long luckyId = luckyDraw.getId();
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                LOG.info("*************** Trigger Lucky Draw: {} {} ******************", luckyId, users);
                List<Long> userids = new ArrayList<Long>(users);
                for (int i = 0; i < users; i++) {
                    Long userId = luckyDrawDao.randomUser(userids);
                    if (userId != null && userId > 0) {
                        userids.add(userId);
                    }
                }
                luckyDraw.setUserIds(StringUtils.join(userids, ","));
                luckyDrawDao.updateLuckyUser(luckyDraw);
                LOG.info("*************** Done trigger Lucky Draw: {} - result {} ******************", luckyId, userids.size());
            }
        }, luckyDraw.getTriggerTime());
    }


}
