package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.meal.MealRecordService;
import com.moredian.onpremise.api.record.VerifyRecordService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/DeleteExpireDataSchedule.class */
public class DeleteExpireDataSchedule extends AbstractCustomSchedule {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DeleteExpireDataSchedule.class);

    @Autowired
    private VerifyRecordService recordService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private MealRecordService mealRecordService;

    @Autowired
    private ConfigService configService;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        logger.info("=====> 删除抓拍照开始");
        Long beginTime = Long.valueOf(System.currentTimeMillis());
        QueryConfigResponse record = this.configService.queryRecordPeriod();
        QueryConfigResponse snap = this.configService.querySnapPeriod();
        if (record != null && record.getConfigValue() != null && record.getConfigValue().trim().length() > 0) {
            Date recordDate = MyDateUtils.addDays(new Date(), -Integer.valueOf(record.getConfigValue()).intValue());
            Date snapDate = MyDateUtils.addDays(new Date(), -Integer.valueOf(snap.getConfigValue()).intValue());
            this.recordService.delete(recordDate, snapDate);
        }
        Long endTime = Long.valueOf(System.currentTimeMillis());
        logger.info("=====> 删除抓拍照结束，耗时：{}秒", Long.valueOf((endTime.longValue() - beginTime.longValue()) / 1000));
    }
}
