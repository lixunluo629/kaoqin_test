package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/RefreshCacheSchedule.class */
public class RefreshCacheSchedule extends AbstractCustomSchedule {
    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String cron) {
        super.saveScheduled(this, cron);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
    }
}
