package com.moredian.onpremise.task.api;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/api/AbstractCustomSchedule.class */
public abstract class AbstractCustomSchedule implements CustomSchedule, Runnable {
    private ScheduledFuture<?> future;
    public static ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AbstractCustomSchedule.class);
    private static Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap();

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(Runnable runnable, String cron) {
        String className = runnable.getClass().getName();
        logger.info("backups scheduled:{}=================,cron :{}", className, cron);
        stopSchedule(className);
        this.future = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(cron));
        futures.put(className, this.future);
        return true;
    }

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean stopSchedule(String scheduleName) {
        this.future = futures.get(scheduleName);
        if (this.future != null) {
            logger.info("stop schedule :{}", scheduleName);
            this.future.cancel(true);
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean stopAllSchedule() {
        Iterator<Map.Entry<String, ScheduledFuture<?>>> entries = futures.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ScheduledFuture<?>> entry = entries.next();
            logger.info("stop schedule, key:{}", entry.getKey());
            entries.remove();
        }
        return true;
    }

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    @Bean
    public boolean initSchedulePool() {
        if (threadPoolTaskScheduler == null) {
            logger.info("init schedule pool ===================");
            threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.initialize();
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean isCanceled() {
        return true;
    }

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean isDone() {
        return true;
    }
}
