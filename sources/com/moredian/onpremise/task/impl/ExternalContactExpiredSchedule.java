package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.visit.VisitRecordService;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/ExternalContactExpiredSchedule.class */
public class ExternalContactExpiredSchedule extends AbstractCustomSchedule {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ExternalContactExpiredSchedule.class);

    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private UploadConfig uploadConfig;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        logger.info("定时任务：删除过期外部联系人任务开始！");
        this.visitRecordService.deleteExpiredVisit(1L);
        logger.info("定时任务：删除过期外部联系人任务结束！");
    }
}
