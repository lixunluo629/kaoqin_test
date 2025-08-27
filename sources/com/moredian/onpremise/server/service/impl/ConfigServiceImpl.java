package com.moredian.onpremise.server.service.impl;

import com.moredian.FaceDet;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.SnapModeEnum;
import com.moredian.onpremise.core.mapper.ConfigMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.model.domain.Config;
import com.moredian.onpremise.core.model.request.SaveAdvertisingRequest;
import com.moredian.onpremise.core.model.request.SavePoseThresholdRequest;
import com.moredian.onpremise.core.model.request.SaveRecordPeriodRequest;
import com.moredian.onpremise.core.model.request.SaveSnapModeRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerShowInfoRequest;
import com.moredian.onpremise.core.model.request.SaveStrangerSpeechInfoRequest;
import com.moredian.onpremise.core.model.request.SaveTimeZoneRequest;
import com.moredian.onpremise.core.model.request.SystemBasicConfigRequest;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.model.response.SystemBasicConfigResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.RuntimeSystemUtils;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncAdvertisingInfoRequest;
import com.moredian.onpremise.model.SyncSnapModeRequest;
import com.moredian.onpremise.model.SyncStrangerReminderInfoRequest;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hyperic.sigar.FileSystemUsage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-server-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/server/service/impl/ConfigServiceImpl.class */
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private DeviceMapper deviceMapper;

    @Value("${onpremise.file.path}")
    private String fileRootPath;

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveTimeZone(SaveTimeZoneRequest request) {
        AssertUtil.isNullOrEmpty(request.getTimeZoneStr(), OnpremiseErrorEnum.TIME_ZONE_MUST_NOT_NULL);
        doSave(ConfigConstants.TIME_ZONE_KEY, request.getTimeZoneStr());
        CacheAdapter.cacheServerTimeZone(Constants.SERVER_TIME_ZONE_KEY, request.getTimeZoneStr());
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public String getTimeZone() {
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

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveRecordPeriod(SaveRecordPeriodRequest request) {
        AssertUtil.isNullOrEmpty(request.getDayNum(), OnpremiseErrorEnum.RECORD_SAVE_PERIOD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(Integer.valueOf(request.getDayNum()).intValue() > 0), OnpremiseErrorEnum.RECORD_SAVE_PERIOD_MUST_NOT_NULL);
        doSave(ConfigConstants.RECORD_PERIOD_KEY, request.getDayNum());
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveStrangerSpeechReminderInfo(SaveStrangerSpeechInfoRequest request) throws NumberFormatException {
        AssertUtil.isTrue(Boolean.valueOf(request.getSpeechReminderInfo() != null), OnpremiseErrorEnum.SPEECH_CONTENT_MUST_NOT_NULL);
        doSave(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY, request.getSpeechReminderInfo());
        doSendNetty(request.getOrgId(), null, "", request.getSpeechReminderInfo());
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveStrangerShowReminderInfo(SaveStrangerShowInfoRequest request) throws NumberFormatException {
        AssertUtil.isTrue(Boolean.valueOf(request.getShowReminderInfo() != null), OnpremiseErrorEnum.SHOW_CONTENT_MUST_NOT_NULL);
        doSave(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY, request.getShowReminderInfo());
        doSendNetty(request.getOrgId(), null, request.getShowReminderInfo(), "");
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public boolean saveStrangerReminderInfo(SaveStrangerInfoRequest request) throws NumberFormatException {
        AssertUtil.isTrue(Boolean.valueOf(request.getFrame() != null), OnpremiseErrorEnum.STRANGER_FRAME_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getShowReminderInfo() != null), OnpremiseErrorEnum.SHOW_CONTENT_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getSpeechReminderInfo() != null), OnpremiseErrorEnum.SPEECH_CONTENT_MUST_NOT_NULL);
        doSave(ConfigConstants.STRANGER_FRAME_KEY, String.valueOf(request.getFrame()));
        doSave(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY, request.getSpeechReminderInfo());
        doSave(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY, request.getShowReminderInfo());
        doSendNetty(request.getOrgId(), request.getFrame(), request.getShowReminderInfo(), request.getSpeechReminderInfo());
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public QueryConfigResponse queryRecordPeriod() throws BeansException {
        QueryConfigResponse queryConfigResponse = getOneByKey(ConfigConstants.RECORD_PERIOD_KEY);
        if (queryConfigResponse == null || StringUtils.isEmpty(queryConfigResponse.getConfigValue())) {
            queryConfigResponse = new QueryConfigResponse();
            queryConfigResponse.setConfigKey(ConfigConstants.RECORD_PERIOD_KEY);
            queryConfigResponse.setConfigValue(String.valueOf(90));
        }
        return queryConfigResponse;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public QueryConfigResponse querySnapPeriod() throws BeansException {
        QueryConfigResponse queryConfigResponse = getOneByKey(ConfigConstants.SNAP_PERIOD_KEY);
        if (queryConfigResponse == null || StringUtils.isEmpty(queryConfigResponse.getConfigValue())) {
            queryConfigResponse = new QueryConfigResponse();
            queryConfigResponse.setConfigKey(ConfigConstants.SNAP_PERIOD_KEY);
            queryConfigResponse.setConfigValue(String.valueOf(90));
        }
        return queryConfigResponse;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public QueryConfigResponse getOneByKey(String key) throws BeansException {
        AssertUtil.isNullOrEmpty(key, OnpremiseErrorEnum.CONFIG_KEY_NOT_NULL);
        QueryConfigResponse response = new QueryConfigResponse();
        Config config = this.configMapper.getOneByKey(key);
        if (config != null) {
            BeanUtils.copyProperties(config, response);
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public boolean savePoseThreshold(SavePoseThresholdRequest request) {
        AssertUtil.isNullOrEmpty(request.getPoseThreshold(), OnpremiseErrorEnum.POSE_THRESHOLD_MUST_NOT_NULL);
        doSave(ConfigConstants.POSE_THRESHOLD_KEY, request.getPoseThreshold());
        FaceDet.SetPoseThreshold(30, Integer.valueOf(request.getPoseThreshold()).intValue(), 30);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveAdvertising(SaveAdvertisingRequest request) {
        StringBuffer var = new StringBuffer();
        for (String base64 : request.getAdvertisingPictureBase64List()) {
            var.append(MyFileUtils.uploadImageForReturnPath(base64, this.fileRootPath)).append(",");
        }
        String value = var.length() == 0 ? "" : var.substring(0, var.length() - 1);
        doSave(ConfigConstants.ADVERTISING_PICTURE_KEY, value);
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return true;
        }
        this.nettyMessageApi.sendMsg(new SyncAdvertisingInfoRequest(value), Integer.valueOf(SyncAdvertisingInfoRequest.MODEL_TYPE.type()), deviceSnList);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveSnapMode(SaveSnapModeRequest request) {
        doSave(ConfigConstants.SNAP_MODE_KEY, String.valueOf(request.getMode()));
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return true;
        }
        Config config = this.configMapper.getOneByKey(ConfigConstants.SNAP_MODE_KEY);
        this.nettyMessageApi.sendMsg(new SyncSnapModeRequest(request.getMode(), Long.valueOf(config.getGmtModify().getTime())), Integer.valueOf(SyncSnapModeRequest.MODEL_TYPE.type()), deviceSnList);
        return true;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public Integer getSnapMode() {
        Config config = this.configMapper.getOneByKey(ConfigConstants.SNAP_MODE_KEY);
        if (config == null) {
            doSave(ConfigConstants.SNAP_MODE_KEY, String.valueOf(SnapModeEnum.BIG.getValue()));
            return Integer.valueOf(SnapModeEnum.BIG.getValue());
        }
        return Integer.valueOf(config.getConfigValue());
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public Integer getRepeatFace() {
        Config config = this.configMapper.getOneByKey(ConfigConstants.REPEAT_FACE_KEY);
        if (config == null) {
            doSave(ConfigConstants.REPEAT_FACE_KEY, String.valueOf(1));
            return 1;
        }
        return Integer.valueOf(config.getConfigValue());
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public SystemBasicConfigResponse getSystemBasicConfig() throws BeansException {
        SystemBasicConfigResponse response = new SystemBasicConfigResponse();
        FileSystemUsage memory = RuntimeSystemUtils.memory();
        response.setMemoryFree(Long.valueOf(memory.getFree()));
        response.setMemoryTotal(Long.valueOf(memory.getTotal()));
        QueryConfigResponse recordPeriod = queryRecordPeriod();
        response.setRecordPeriod(Integer.valueOf(recordPeriod.getConfigValue()));
        Integer snapMode = getSnapMode();
        response.setSnapMode(snapMode);
        String strangerFrame = getOneByKey(ConfigConstants.STRANGER_FRAME_KEY).getConfigValue();
        if (StringUtils.isEmpty(strangerFrame)) {
            response.setStrangerFrame(3);
        } else {
            response.setStrangerFrame(Integer.valueOf(strangerFrame));
        }
        String strangerTextTips = getOneByKey(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY).getConfigValue();
        response.setStrangerTextTips(strangerTextTips);
        String strangerSpeechTips = getOneByKey(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY).getConfigValue();
        response.setStrangerSpeechTips(strangerSpeechTips);
        String timezoneStr = getTimeZone();
        response.setTimeZoneStr(timezoneStr);
        Integer repeatFace = getRepeatFace();
        response.setRepeatFace(repeatFace);
        QueryConfigResponse snapPeriod = querySnapPeriod();
        response.setSnapPeriod(Integer.valueOf(snapPeriod.getConfigValue()));
        return response;
    }

    @Override // com.moredian.onpremise.api.server.ConfigService
    public void saveSystemBasicConfig(SystemBasicConfigRequest request) throws NumberFormatException {
        AssertUtil.isTrue(Boolean.valueOf(request.getRecordPeriod() != null), OnpremiseErrorEnum.RECORD_SAVE_PERIOD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getSnapPeriod() != null), OnpremiseErrorEnum.SNAP_PERIOD_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(request.getSnapPeriod().intValue() <= request.getRecordPeriod().intValue()), OnpremiseErrorEnum.SNAP_PERIOD_CANNOT_OVER_RECORD_PERIOD);
        SaveRecordPeriodRequest saveRecordPeriodRequest = new SaveRecordPeriodRequest();
        saveRecordPeriodRequest.setOrgId(request.getOrgId());
        saveRecordPeriodRequest.setDayNum(String.valueOf(request.getRecordPeriod()));
        saveRecordPeriod(saveRecordPeriodRequest);
        SaveSnapModeRequest saveSnapModeRequest = new SaveSnapModeRequest();
        saveSnapModeRequest.setOrgId(request.getOrgId());
        saveSnapModeRequest.setMode(request.getSnapMode());
        saveSnapMode(saveSnapModeRequest);
        SaveStrangerInfoRequest saveStrangerInfoRequest = new SaveStrangerInfoRequest();
        saveStrangerInfoRequest.setOrgId(request.getOrgId());
        saveStrangerInfoRequest.setFrame(request.getStrangerFrame());
        saveStrangerInfoRequest.setShowReminderInfo(request.getStrangerTextTips());
        saveStrangerInfoRequest.setSpeechReminderInfo(request.getStrangerSpeechTips());
        saveStrangerReminderInfo(saveStrangerInfoRequest);
        AssertUtil.isTrue(Boolean.valueOf(request.getRepeatFace() != null), OnpremiseErrorEnum.REPEAT_FACE_MUST_NOT_NULL);
        doSave(ConfigConstants.REPEAT_FACE_KEY, String.valueOf(request.getRepeatFace()));
        doSave(ConfigConstants.SNAP_PERIOD_KEY, String.valueOf(request.getSnapPeriod()));
    }

    private void doSave(String key, String value) {
        Config config = this.configMapper.getOneByKey(key);
        if (config == null) {
            this.configMapper.insert(key, value);
        } else {
            this.configMapper.update(key, value);
        }
    }

    private void doSendNetty(Long orgId, Integer frame, String showReminderInfo, String speechReminderInfo) throws NumberFormatException {
        Long lastModifyTime = 0L;
        if (frame == null) {
            Config config = this.configMapper.getOneByKey(ConfigConstants.STRANGER_FRAME_KEY);
            if (config != null) {
                frame = Integer.valueOf(config.getConfigValue());
                lastModifyTime = Long.valueOf(config.getGmtModify().getTime());
            } else {
                frame = 3;
            }
        }
        if (showReminderInfo == null) {
            Config config2 = this.configMapper.getOneByKey(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY);
            if (config2 != null) {
                showReminderInfo = config2.getConfigValue();
                lastModifyTime = Long.valueOf(config2.getGmtModify().getTime() > lastModifyTime.longValue() ? config2.getGmtModify().getTime() : lastModifyTime.longValue());
            } else {
                showReminderInfo = "";
            }
        }
        if (speechReminderInfo == null) {
            Config config3 = this.configMapper.getOneByKey(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY);
            if (config3 != null) {
                speechReminderInfo = config3.getConfigValue();
                lastModifyTime = Long.valueOf(config3.getGmtModify().getTime() > lastModifyTime.longValue() ? config3.getGmtModify().getTime() : lastModifyTime.longValue());
            } else {
                speechReminderInfo = "";
            }
        }
        List<String> deviceSnList = this.deviceMapper.listDeviceSn(orgId);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncStrangerReminderInfoRequest(frame, showReminderInfo, speechReminderInfo, lastModifyTime), Integer.valueOf(SyncStrangerReminderInfoRequest.MODEL_TYPE.type()), deviceSnList);
    }
}
