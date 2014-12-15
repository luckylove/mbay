package com.marinabay.cruise.service;

import com.marinabay.cruise.dao.LuckyDrawDao;
import com.marinabay.cruise.model.JSonPagingResult;
import com.marinabay.cruise.model.LuckyDraw;
import com.marinabay.cruise.model.PagingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
        for (LuckyDraw luckyDraw : luckyDraws) {
            final Long luckyId = luckyDraw.getId();
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    LOG.info("*************** Trigger Lucky Draw: {} ******************", luckyId);
                    luckyDrawDao.randomUser(luckyId);
                    LOG.info("*************** Done trigger Lucky Draw: {} ******************", luckyId);
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

    public void insert(LuckyDraw luckyDraw) {
        luckyDrawDao.insert(luckyDraw);
        //put it in schedules
        final Long luckyId = luckyDraw.getId();
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                LOG.info("*************** Trigger Lucky Draw: {} ******************", luckyId);
                luckyDrawDao.randomUser(luckyId);
                LOG.info("*************** Done trigger Lucky Draw: {} ******************", luckyId);
            }
        }, luckyDraw.getTriggerTime());
    }


}
