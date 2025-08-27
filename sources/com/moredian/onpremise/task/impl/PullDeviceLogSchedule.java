package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.model.request.PullDeviceLogRequest;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/PullDeviceLogSchedule.class */
public class PullDeviceLogSchedule extends AbstractCustomSchedule {

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeviceService deviceService;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        PullDeviceLogRequest request = new PullDeviceLogRequest();
        request.setPullLogDate(MyDateUtils.formatDate(MyDateUtils.getLastDay(new Date()), "yyyy-MM-dd"));
        request.setOrgId(1L);
        this.deviceService.pullDeviceLog(request);
    }
}
