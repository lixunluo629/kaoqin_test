package com.moredian.onpremise.device.service.netty;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.DeviceUpgradeStatusEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleStatusEnum;
import com.moredian.onpremise.core.mapper.UpgradeRecordMapper;
import com.moredian.onpremise.core.mapper.UpgradeScheduleMapper;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceRecord;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceSchedule;
import com.moredian.onpremise.core.model.info.CacheUpgradeStatusInfo;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractModelEventHandler;
import com.moredian.onpremise.model.DeviceUpgradeStartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/netty/UpgradeStartHandler.class */
public class UpgradeStartHandler extends AbstractModelEventHandler<DeviceUpgradeStartRequest> {

    @Autowired
    private UpgradeRecordMapper upgradeRecordMapper;

    @Autowired
    private UpgradeScheduleMapper upgradeScheduleMapper;

    @Override // com.moredian.onpremise.iot.handle.ModelEventHandler
    public void handle(DeviceUpgradeStartRequest model, IOTContext context) {
        String deviceSn = model.getDeviceSn();
        CacheUpgradeStatusInfo upgradeStatusInfo = CacheAdapter.getUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(deviceSn));
        Long upgradeScheduleId = 0L;
        if (upgradeStatusInfo != null) {
            if (upgradeStatusInfo.getExpireTime().longValue() < System.currentTimeMillis()) {
                UpgradeDeviceRecord record = this.upgradeRecordMapper.getByDeviceSn(deviceSn);
                if (record != null && record.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.WAITING_STATUS.getValue()) {
                    upgradeScheduleId = record.getUpgradeScheduleId();
                }
            } else if (upgradeStatusInfo.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.WAITING_STATUS.getValue()) {
                upgradeScheduleId = upgradeStatusInfo.getUpgradeScheduleId();
            }
        }
        this.upgradeRecordMapper.updateStatusByDeviceSn(deviceSn, DeviceUpgradeStatusEnum.WAITING_STATUS.getValue(), upgradeScheduleId, DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue(), "");
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getOneByScheduleId(upgradeScheduleId);
        if (schedule != null) {
            this.upgradeScheduleMapper.updateScheduleStatus(schedule.getUpgradeScheduleId(), schedule.getUpgradeStatus(), UpgradeScheduleStatusEnum.UPGRADING.getValue());
        }
        CacheAdapter.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, Integer.valueOf(DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue()));
    }
}
