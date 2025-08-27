package com.moredian.onpremise.task.factory;

import com.moredian.onpremise.task.api.CustomSchedule;
import com.moredian.onpremise.task.impl.RefreshCacheSchedule;

/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/factory/RefreshCacheScheduleFactory.class */
public class RefreshCacheScheduleFactory extends AbstractScheduleFactory {
    @Override // com.moredian.onpremise.task.factory.ScheduleFactory
    public CustomSchedule getSchedule() {
        return new RefreshCacheSchedule();
    }
}
