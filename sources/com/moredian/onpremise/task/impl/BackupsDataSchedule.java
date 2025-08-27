package com.moredian.onpremise.task.impl;

import com.moredian.onpremise.api.server.DataBackupsService;
import com.moredian.onpremise.core.common.enums.BackupsTypeEnum;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.model.request.BackupsDataRecordRequest;
import com.moredian.onpremise.task.api.AbstractCustomSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-task-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/task/impl/BackupsDataSchedule.class */
public class BackupsDataSchedule extends AbstractCustomSchedule {

    @Autowired
    private DataBackupsService backupsService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override // com.moredian.onpremise.task.api.CustomSchedule
    public boolean saveScheduled(String corn) {
        super.saveScheduled(this, corn);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        BackupsDataRecordRequest recordRequest = new BackupsDataRecordRequest();
        recordRequest.setBackupsType(Integer.valueOf(BackupsTypeEnum.AUTOMATIC_BACKUPS.getValue()));
        recordRequest.setOrgId(this.organizationMapper.getOne().getOrgId());
        this.backupsService.executeBackups(recordRequest);
    }
}
