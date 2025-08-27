package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.record.VerifyRecordService;
import com.moredian.onpremise.core.utils.RuntimeSystemUtils;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import java.math.BigDecimal;
import org.hyperic.sigar.FileSystemUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/DeleteOverflowDataSchedule.class */
public class DeleteOverflowDataSchedule extends AbstractCustomSchedule {

    @Autowired
    private VerifyRecordService recordService;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        FileSystemUsage mem = RuntimeSystemUtils.memory();
        if (BigDecimal.valueOf(mem.getFree()).divide(BigDecimal.valueOf(mem.getTotal()), 3, 4).compareTo(BigDecimal.valueOf(0.2d)) < 0) {
        }
    }
}
