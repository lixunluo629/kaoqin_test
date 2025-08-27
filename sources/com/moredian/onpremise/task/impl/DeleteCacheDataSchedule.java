package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/DeleteCacheDataSchedule.class */
public class DeleteCacheDataSchedule extends AbstractCustomSchedule {

    @Autowired
    private DeviceService deviceService;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        super.saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        CacheAdapter.removeExpireHeartBeatInfo();
    }
}
