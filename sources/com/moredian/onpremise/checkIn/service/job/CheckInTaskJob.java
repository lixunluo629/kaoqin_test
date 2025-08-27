package com.moredian.onpremise.checkIn.service.job;

import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.utils.MyDateUtils;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-checkIn-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/checkIn/service/job/CheckInTaskJob.class */
public class CheckInTaskJob {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) CheckInTaskJob.class);

    @Autowired
    private CheckInService checkInService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void generateCheckInLog() {
        logger.info("定时任务：生成当天待签到记录开始！！！");
        Calendar cal = Calendar.getInstance();
        try {
            this.checkInService.generateCheckInLog(cal.getTime());
        } catch (Exception e) {
            logger.error("定时任务：生成当天待签到记录失败！！！日期 = " + MyDateUtils.formatDate(cal.getTime(), "yyyy-MM-dd") + " ;错误：" + e.toString());
        }
        logger.info("定时任务：生成当天待签到记录结束！！！");
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void closeSingleCheckInTask() {
        logger.info("定时任务：关闭过时的单次签到任务开始！！！");
        try {
            Map<String, List> res = this.checkInService.closeSingleCheckInTask();
            logger.info("关闭过时的单次签到任务，成功：" + res.get(Constants.SUCCESS).size() + " 个 ; 失败：" + res.get(Constants.FAIL).size() + " 个！！！");
        } catch (Exception e) {
            logger.error("定时任务：关闭过时的单次签到任务失败！！！错误：" + e.toString());
        }
        logger.info("定时任务：关闭过时的单次签到任务结束！！！");
    }
}
