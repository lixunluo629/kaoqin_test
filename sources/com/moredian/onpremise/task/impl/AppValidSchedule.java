package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.core.mapper.AppMapper;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/AppValidSchedule.class */
public class AppValidSchedule extends AbstractCustomSchedule {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AppValidSchedule.class);

    @Autowired
    private AppMapper appMapper;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        super.saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        logger.info("定时任务：关闭过期app模块任务开始！");
        this.appMapper.updateForClose(1L);
        logger.info("定时任务：关闭过期app模块任务结束！");
    }
}
