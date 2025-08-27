package com.moredian.onpremise.device.service.netty;

import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.DeviceStatusEventEnum;
import com.moredian.onpremise.core.common.enums.DeviceUpgradeStatusEnum;
import com.moredian.onpremise.core.common.enums.NettyErrorEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleStatusEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleTypeEnum;
import com.moredian.onpremise.core.mapper.ConfigMapper;
import com.moredian.onpremise.core.mapper.UpgradeRecordMapper;
import com.moredian.onpremise.core.mapper.UpgradeScheduleMapper;
import com.moredian.onpremise.core.model.domain.Config;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceRecord;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceSchedule;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.CacheUpgradeStatusInfo;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.DeviceCallbackRequest;
import com.moredian.onpremise.core.model.request.UpdateDeviceRequest;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.event.DeviceHeartbeatEvent;
import com.moredian.onpremise.event.IOTEvent;
import com.moredian.onpremise.event.ModelTransferEvent;
import com.moredian.onpremise.iot.IOTContext;
import com.moredian.onpremise.iot.handle.AbstractIOTEventListener;
import com.moredian.onpremise.model.DeviceHeartBeatResponse;
import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/netty/HeartBeatEventListener.class */
public class HeartBeatEventListener extends AbstractIOTEventListener<DeviceHeartbeatEvent> {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UpgradeRecordMapper upgradeRecordMapper;

    @Autowired
    private UpgradeScheduleMapper upgradeScheduleMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Override // com.moredian.onpremise.iot.handle.IOTEventListener
    public void handleEvent(IOTContext<DeviceHeartbeatEvent> context) throws Exception {
        DeviceHeartbeatEvent event = context.getSourceEvent();
        DeviceHeartBeatResponse response = packagingHeartBeatResponse(event.getSerialNumber());
        try {
            try {
                AssertUtil.isTrue(Boolean.valueOf(event.getOrgCode() != null && event.getOrgCode().equals(CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY))), OnpremiseErrorEnum.SYSTEM_AUTH_CODE_ERROR);
                AssertUtil.isTrue(Boolean.valueOf(this.deviceService.checkDeviceFailure(event.getSerialNumber())), OnpremiseErrorEnum.DEVICE_NOT_FIND);
                updateUpgradeStatus(event);
                cacheDeviceInfo(event);
                response.setCode(0);
                response.setSystemCurrentTime(Long.valueOf(System.currentTimeMillis()));
                response.setTimeZone(getTimeZone());
                this.logger.info("=====> deviceSn : {}, response :{}", event.getSerialNumber(), JsonUtils.toJson(response));
                ModelTransferEvent responseEvent = new ModelTransferEvent(DeviceHeartBeatResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent);
            } catch (Exception e) {
                this.logger.error("=====> deviceSn : {}, 处理心跳异常：{}", event.getSerialNumber(), e.getMessage());
                response.setCode(Integer.valueOf(NettyErrorEnum.DEVICE_HEART_ERROR.getValue()));
                response.setErrorMessage(NettyErrorEnum.DEVICE_HEART_ERROR.getDescription());
                response.setSystemCurrentTime(Long.valueOf(System.currentTimeMillis()));
                response.setTimeZone(getTimeZone());
                this.logger.info("=====> deviceSn : {}, response :{}", event.getSerialNumber(), JsonUtils.toJson(response));
                ModelTransferEvent responseEvent2 = new ModelTransferEvent(DeviceHeartBeatResponse.MODEL_TYPE.type(), response);
                context.getSession().getChannel().writeAndFlush(responseEvent2);
            }
        } catch (Throwable th) {
            response.setSystemCurrentTime(Long.valueOf(System.currentTimeMillis()));
            response.setTimeZone(getTimeZone());
            this.logger.info("=====> deviceSn : {}, response :{}", event.getSerialNumber(), JsonUtils.toJson(response));
            ModelTransferEvent responseEvent3 = new ModelTransferEvent(DeviceHeartBeatResponse.MODEL_TYPE.type(), response);
            context.getSession().getChannel().writeAndFlush(responseEvent3);
            throw th;
        }
    }

    private String getTimeZone() {
        String timeZone = CacheAdapter.getServerTimeZone(Constants.SERVER_TIME_ZONE_KEY);
        if (timeZone == null) {
            Config config = this.configMapper.getOneByKey(ConfigConstants.TIME_ZONE_KEY);
            if (config == null) {
                timeZone = "";
            } else {
                timeZone = config.getConfigValue();
                CacheAdapter.cacheServerTimeZone(Constants.SERVER_TIME_ZONE_KEY, timeZone);
            }
        }
        return timeZone;
    }

    private DeviceHeartBeatResponse packagingHeartBeatResponse(String deviceSn) {
        DeviceLastModifyTimeInfo groupInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getGroupLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo memberInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getMemberLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo checkInInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCheckInLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo checkInTaskMemberInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCheckInTaskMemberLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo attendanceGroupInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceGroupLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo attendanceHolidayInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAttendanceHolidayLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo canteenInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getCanteenLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo canteenMemberInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getMealCanteenMemberLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo visitConfigInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo externalContactInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getExternalContactLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo accountInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getAccountLastModifyTimeKey(deviceSn));
        DeviceLastModifyTimeInfo deviceConfigInfo = CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getDeviceConfigLastModifyTimeKey(deviceSn));
        DeviceHeartBeatResponse response = new DeviceHeartBeatResponse();
        response.setGroupLastModifyTime(Long.valueOf(groupInfo == null ? 0L : groupInfo.getLastModifyTime().longValue()));
        response.setMemberLastModifyTime(Long.valueOf(memberInfo == null ? 0L : memberInfo.getLastModifyTime().longValue()));
        response.setCheckInLastModifyTime(Long.valueOf(checkInInfo == null ? 0L : checkInInfo.getLastModifyTime().longValue()));
        response.setCheckInTaskMemberLastModifyTime(Long.valueOf(checkInTaskMemberInfo == null ? 0L : checkInTaskMemberInfo.getLastModifyTime().longValue()));
        response.setAttendanceGroupLastModifyTime(Long.valueOf(attendanceGroupInfo == null ? 0L : attendanceGroupInfo.getLastModifyTime().longValue()));
        response.setAttendanceHolidayLastModifyTime(Long.valueOf(attendanceHolidayInfo == null ? 0L : attendanceHolidayInfo.getLastModifyTime().longValue()));
        response.setCanteenLastModifyTime(Long.valueOf(canteenInfo == null ? 0L : canteenInfo.getLastModifyTime().longValue()));
        response.setCanteenMemberLastModifyTime(Long.valueOf(canteenMemberInfo == null ? 0L : canteenMemberInfo.getLastModifyTime().longValue()));
        response.setVisitConfigLastModifyTime(Long.valueOf(visitConfigInfo == null ? 0L : visitConfigInfo.getLastModifyTime().longValue()));
        response.setExternalContactLastModifyTime(Long.valueOf(externalContactInfo == null ? 0L : externalContactInfo.getLastModifyTime().longValue()));
        response.setAccountLastModifyTime(Long.valueOf(accountInfo == null ? 0L : accountInfo.getLastModifyTime().longValue()));
        response.setDeviceConfigLastModifyTime(Long.valueOf(deviceConfigInfo == null ? 0L : deviceConfigInfo.getLastModifyTime().longValue()));
        return response;
    }

    private void updateUpgradeStatus(DeviceHeartbeatEvent event) {
        CacheUpgradeStatusInfo upgradeStatusInfo = CacheAdapter.getUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(event.getSerialNumber()));
        Long upgradeScheduleId = 0L;
        if (upgradeStatusInfo != null) {
            if (upgradeStatusInfo.getExpireTime().longValue() < System.currentTimeMillis()) {
                UpgradeDeviceRecord record = this.upgradeRecordMapper.getByDeviceSn(event.getSerialNumber());
                if (record != null && (record.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue() || record.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.FAIL_STATUS.getValue())) {
                    upgradeScheduleId = record.getUpgradeScheduleId();
                }
            } else if (upgradeStatusInfo.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue()) {
                upgradeScheduleId = upgradeStatusInfo.getUpgradeScheduleId();
            }
        }
        if (upgradeScheduleId.longValue() > 0) {
            doUpdateStatus(upgradeScheduleId, event);
        }
    }

    private void doUpdateStatus(Long upgradeScheduleId, IOTEvent event) {
        int status;
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getOneByScheduleId(upgradeScheduleId);
        if (schedule != null) {
            this.logger.info("Do update status, upgrade schedule id :{}, device :{}", upgradeScheduleId, event.getSerialNumber());
            int unfinished = this.upgradeRecordMapper.countUnfinishedByScheduleId(upgradeScheduleId);
            if (unfinished == 1) {
                this.upgradeScheduleMapper.updateScheduleStatus(upgradeScheduleId, schedule.getUpgradeStatus(), UpgradeScheduleStatusEnum.COMPLETED.getValue());
            }
            if (UpgradeScheduleTypeEnum.APK.getValue() == schedule.getUpgradeType().intValue()) {
                if (schedule.getUpgradeVersion().compareTo(event.getSoftwareVersion()) == 0) {
                    status = DeviceUpgradeStatusEnum.SUCCESS_STATUS.getValue();
                } else {
                    status = DeviceUpgradeStatusEnum.FAIL_STATUS.getValue();
                }
            } else if (schedule.getUpgradeVersion().compareTo(event.getRomVersion()) == 0) {
                status = DeviceUpgradeStatusEnum.SUCCESS_STATUS.getValue();
            } else {
                status = DeviceUpgradeStatusEnum.FAIL_STATUS.getValue();
            }
            this.logger.info("Update status :{}", Integer.valueOf(status));
            this.upgradeRecordMapper.updateStatusByDeviceSn(event.getSerialNumber(), 0, upgradeScheduleId, status, "");
            CacheAdapter.updateUpgradeCacheStatus(event.getSerialNumber(), upgradeScheduleId, Integer.valueOf(status));
        }
    }

    private void cacheDeviceInfo(DeviceHeartbeatEvent event) throws BeansException {
        this.logger.debug("to do cache device info ");
        String key = CacheKeyGenerateUtils.getHeartBeatCacheKey(event.getSerialNumber());
        CacheHeartBeatInfo heartBeatInfo1 = CacheAdapter.getHeartBeatInfo(key);
        CacheHeartBeatInfo heartBeatInfo2 = new CacheHeartBeatInfo();
        heartBeatInfo2.setIpAddress(event.getIpAddress());
        heartBeatInfo2.setMacAddress(event.getMacAddress());
        heartBeatInfo2.setDeviceSn(event.getSerialNumber());
        heartBeatInfo2.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 35).getTime()));
        heartBeatInfo2.setSoftVersion(event.getSoftwareVersion());
        heartBeatInfo2.setRomVersion(event.getRomVersion());
        this.logger.debug("heartBeatInfo1 ： {}", JsonUtils.toJson(heartBeatInfo1));
        this.logger.debug("heartBeatInfo2 ： {}", JsonUtils.toJson(heartBeatInfo2));
        if (isNeedUpdate(heartBeatInfo1, heartBeatInfo2)) {
            this.logger.debug("to do update device info ");
            UpdateDeviceRequest request = new UpdateDeviceRequest();
            BeanUtils.copyProperties(heartBeatInfo2, request);
            this.deviceService.updateDevice(request);
        }
        if (heartBeatInfo1 == null) {
            DeviceCallbackRequest request2 = new DeviceCallbackRequest();
            request2.setDeviceSn(event.getSerialNumber());
            request2.setEvent(Integer.valueOf(DeviceStatusEventEnum.ONLINE.getStatus()));
            request2.setTimestamp(Long.valueOf(System.currentTimeMillis()));
            this.deviceService.sendCallback(request2);
        }
        CacheAdapter.cacheHeartBeatInfo(key, heartBeatInfo2);
    }

    private boolean isNeedUpdate(CacheHeartBeatInfo heartBeatInfo1, CacheHeartBeatInfo heartBeatInfo2) {
        return !(heartBeatInfo1 == null || heartBeatInfo1.equals(heartBeatInfo2)) || heartBeatInfo1 == null;
    }
}
