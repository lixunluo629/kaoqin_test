package com.moredian.onpremise.visit.impl;

import com.moredian.onpremise.api.visit.VisitConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.DeviceTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.ExternalContactMapper;
import com.moredian.onpremise.core.mapper.VisitConfigMapper;
import com.moredian.onpremise.core.mapper.VisitDeviceMapper;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.ExternalContact;
import com.moredian.onpremise.core.model.domain.VisitConfig;
import com.moredian.onpremise.core.model.domain.VisitDevice;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.PassTimeDto;
import com.moredian.onpremise.core.model.info.DeviceLastModifyTimeInfo;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.VisitConfigDeviceRequest;
import com.moredian.onpremise.core.model.request.VisitConfigRequest;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncVisitConfigResponse;
import com.moredian.onpremise.core.model.response.VisitConfigResponse;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncExternalContactRequest;
import com.moredian.onpremise.model.SyncVisitConfigRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
/* loaded from: onpremise-visit-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/visit/impl/VisitConfigServiceImpl.class */
public class VisitConfigServiceImpl implements VisitConfigService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) VisitConfigServiceImpl.class);

    @Autowired
    private VisitConfigMapper visitConfigMapper;

    @Autowired
    private VisitDeviceMapper visitDeviceMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private ExternalContactMapper externalContactMapper;

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public Long addVisitConfig(VisitConfigRequest request) {
        return null;
    }

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public Long updateVisitConfig(VisitConfigRequest request) throws BeansException {
        checkParam(request);
        VisitConfig visitConfig = new VisitConfig();
        BeanUtils.copyProperties(request, visitConfig);
        if (request.getAllDayFlag().equals(0)) {
            visitConfig.setPassTime(JsonUtils.toJson(request.getPassTimeList()));
        }
        if (request.getCycleFlag().equals(0)) {
            visitConfig.setScope(JsonUtils.toJson(request.getScope()));
        }
        if (request.getScreensaverFlag().equals(1)) {
            visitConfig.setScreensaverUrl(JsonUtils.toJson(request.getScreensaverUrl()));
        }
        this.visitConfigMapper.updateOneById(visitConfig);
        VisitConfig visitConfigNew = this.visitConfigMapper.getOneById(request.getOrgId(), request.getId());
        doSendNettyMsg(request.getOrgId(), Long.valueOf(visitConfigNew.getGmtModify().getTime()));
        return visitConfig.getId();
    }

    private void doSendNettyMsg(Long orgId, Long lastModifyTime) {
        List<DeviceResponse> deviceList = this.deviceMapper.listDevice(orgId, Integer.valueOf(DeviceTypeEnum.ENTRANCE_VISIT.getValue()), null, null, null, null, null, null);
        List<VisitDevice> visitDeviceList = this.visitDeviceMapper.getListByConfigId(orgId, 1L);
        List<String> deviceSnList = new ArrayList<>();
        for (DeviceResponse deviceResponse : deviceList) {
            deviceSnList.add(deviceResponse.getDeviceSn());
        }
        for (VisitDevice visitDevice : visitDeviceList) {
            deviceSnList.add(visitDevice.getDeviceSn());
        }
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        this.nettyMessageApi.sendMsg(new SyncVisitConfigRequest(lastModifyTime, orgId), Integer.valueOf(SyncVisitConfigRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void doSendNettyMsg(Long orgId, List<String> deviceSnList) {
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        for (String deviceSn : deviceSnList) {
            VisitDevice visitDevice = this.visitDeviceMapper.getLatestByDeviceAndVisitConfig(orgId, deviceSn, 1L);
            this.nettyMessageApi.sendMsg(new SyncVisitConfigRequest(Long.valueOf(visitDevice.getGmtModify().getTime()), orgId), Integer.valueOf(SyncVisitConfigRequest.MODEL_TYPE.type()), deviceSn);
        }
    }

    private void doSendNettyMsgExternalContact(Long orgId) {
        ExternalContact externalContact = this.externalContactMapper.getLastModify(orgId);
        List<String> deviceSnList = this.visitDeviceMapper.getListDeviceSnByConfigId(orgId, 1L);
        if (CollectionUtils.isEmpty(deviceSnList)) {
            return;
        }
        logger.info("=========== lastExternalContact: " + JsonUtils.toJson(externalContact));
        this.nettyMessageApi.sendMsg(new SyncExternalContactRequest(Long.valueOf(externalContact == null ? 0L : externalContact.getGmtModify().getTime()), orgId), Integer.valueOf(SyncExternalContactRequest.MODEL_TYPE.type()), deviceSnList);
    }

    private void checkParam(VisitConfigRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getId(), OnpremiseErrorEnum.VISIT_CONFIG_ID_MUST_NOT_NULL);
        if (request.getUpdateScreensaver().equals(1)) {
            AssertUtil.isNullOrEmpty(request.getScreensaverUrl(), OnpremiseErrorEnum.VISIT_CONFIG_SCREENSAVER_MUST_NOT_NULL);
        }
        if (request.getAllDayFlag().equals(0)) {
            AssertUtil.isNullOrEmpty(request.getPassTimeList(), OnpremiseErrorEnum.VISIT_CONFIG_PASSTIME_MUST_NOT_NULL);
        }
        if (request.getCycleFlag().equals(0)) {
            AssertUtil.isNullOrEmpty(request.getScope(), OnpremiseErrorEnum.VISIT_CONFIG_SCOPE_MUST_NOT_NULL);
        }
    }

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public VisitConfigResponse getOneVisitConfigById(Long orgId, Long id) throws BeansException {
        AssertUtil.isNullOrEmpty(orgId, OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(id, OnpremiseErrorEnum.VISIT_CONFIG_ID_MUST_NOT_NULL);
        VisitConfigResponse visitConfigResponse = new VisitConfigResponse();
        VisitConfig visitConfig = this.visitConfigMapper.getOneById(orgId, id);
        AssertUtil.isNullOrEmpty(visitConfig, OnpremiseErrorEnum.VISIT_CONFIG_NOT_FOUND);
        BeanUtils.copyProperties(visitConfig, visitConfigResponse);
        List<String> screensaverUrl = new ArrayList();
        if (!StringUtils.isEmpty(visitConfig.getScreensaverUrl())) {
            screensaverUrl = JsonUtils.jsoncastListType(String.class, visitConfig.getScreensaverUrl());
        }
        visitConfigResponse.setScreensaverUrl(screensaverUrl);
        List<PassTimeDto> passTimeDtos = new ArrayList();
        if (!StringUtils.isEmpty(visitConfig.getPassTime())) {
            passTimeDtos = JsonUtils.jsoncastListType(PassTimeDto.class, visitConfig.getPassTime());
        }
        visitConfigResponse.setPassTimeList(passTimeDtos);
        List<Integer> scope = new ArrayList();
        if (!StringUtils.isEmpty(visitConfig.getScope())) {
            scope = JsonUtils.jsoncastListType(Integer.class, visitConfig.getScope());
        }
        visitConfigResponse.setScope(scope);
        List<DeviceDto> deviceDtoList = new ArrayList();
        List<VisitDevice> visitDevices = this.visitDeviceMapper.getListByConfigId(orgId, id);
        if (!CollectionUtils.isEmpty(visitDevices)) {
            List<String> deviceSns = new ArrayList<>();
            for (VisitDevice visitDevice : visitDevices) {
                deviceSns.add(visitDevice.getDeviceSn());
            }
            deviceDtoList = this.deviceMapper.listDeviceDtoByDeviceSn(deviceSns, orgId);
        }
        visitConfigResponse.setDevices(deviceDtoList);
        return visitConfigResponse;
    }

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public Long updateVisitConfigDevice(VisitConfigDeviceRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVisitConfigId(), OnpremiseErrorEnum.VISIT_CONFIG_ID_MUST_NOT_NULL);
        List<DeviceDto> newDevices = request.getDeviceSns() == null ? new ArrayList<>() : request.getDeviceSns();
        List<DeviceDto> arrayList = new ArrayList<>();
        List<VisitDevice> visitDevices = this.visitDeviceMapper.getListByConfigId(request.getOrgId(), request.getVisitConfigId());
        if (!CollectionUtils.isEmpty(visitDevices)) {
            for (VisitDevice visitDevice : visitDevices) {
                DeviceDto deviceDto = new DeviceDto();
                deviceDto.setDeviceSn(visitDevice.getDeviceSn());
                arrayList.add(deviceDto);
            }
        }
        MyListUtils<DeviceDto> deviceUtils = new MyListUtils<>();
        List<DeviceDto> insertDevices = deviceUtils.difference(newDevices, arrayList);
        List<DeviceDto> deleteDevices = deviceUtils.difference(arrayList, newDevices);
        List<String> deviceSns = new ArrayList<>();
        if (MyListUtils.listIsEmpty(deleteDevices)) {
            for (DeviceDto dto : deleteDevices) {
                deviceSns.add(dto.getDeviceSn());
                AssertUtil.isTrue(Boolean.valueOf(this.visitDeviceMapper.softDeleteOneByConfigIdAndDeviceSn(request.getOrgId(), request.getVisitConfigId(), dto.getDeviceSn()) > 0), OnpremiseErrorEnum.DELETE_VISIT_CONFIG_DEVICE_FAIL);
            }
        }
        if (MyListUtils.listIsEmpty(insertDevices)) {
            List<VisitDevice> insertVisitDevices = new ArrayList<>();
            for (DeviceDto dto2 : insertDevices) {
                deviceSns.add(dto2.getDeviceSn());
                VisitDevice visitDevice2 = new VisitDevice();
                visitDevice2.setOrgId(request.getOrgId());
                visitDevice2.setVisitConfigId(request.getVisitConfigId());
                visitDevice2.setDeviceSn(dto2.getDeviceSn());
                insertVisitDevices.add(visitDevice2);
            }
            this.visitDeviceMapper.insertBatch(insertVisitDevices);
        }
        doSendNettyMsg(request.getOrgId(), deviceSns);
        doSendNettyMsgExternalContact(request.getOrgId());
        return request.getVisitConfigId();
    }

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public TerminalSyncResponse<TerminalSyncVisitConfigResponse> syncVisitConfig(TerminalSyncRequest request) throws BeansException {
        TerminalSyncResponse<TerminalSyncVisitConfigResponse> terminalSyncResponse = new TerminalSyncResponse<>();
        Device device = this.deviceMapper.getDeviceDetail(request.getDeviceSn(), request.getOrgId());
        VisitConfigResponse visitConfigResponse = getOneVisitConfigById(request.getOrgId(), 1L);
        TerminalSyncVisitConfigResponse terminalSyncVisitConfigResponse = new TerminalSyncVisitConfigResponse();
        BeanUtils.copyProperties(visitConfigResponse, terminalSyncVisitConfigResponse);
        List<TerminalSyncVisitConfigResponse> syncInsertVisitConfigList = new ArrayList<>();
        List<TerminalSyncVisitConfigResponse> syncModifyVisitConfigList = new ArrayList<>();
        List<TerminalSyncVisitConfigResponse> syncDeleteVisitConfigList = new ArrayList<>();
        if (device != null && device.getDeviceType().equals(Integer.valueOf(DeviceTypeEnum.ENTRANCE_VISIT.getValue()))) {
            syncModifyVisitConfigList.add(terminalSyncVisitConfigResponse);
        } else {
            List<Long> visitConfigIds = this.visitDeviceMapper.getListVisitConfigIdByDeviceSn(request.getOrgId(), request.getDeviceSn());
            if (CollectionUtils.isEmpty(visitConfigIds)) {
                if (CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(request.getDeviceSn())) == null) {
                    terminalSyncResponse.setSyncTime(0L);
                    doCacheLastModifyTime(CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
                } else {
                    terminalSyncResponse.setSyncTime(CacheAdapter.getLastModifyTime(CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(request.getDeviceSn())).getLastModifyTime());
                }
                return terminalSyncResponse;
            }
            VisitDevice visitDevice = this.visitDeviceMapper.getLatestByDeviceAndVisitConfig(request.getOrgId(), request.getDeviceSn(), visitConfigResponse.getId());
            if (visitDevice != null && !visitDevice.getDeleteOrNot().equals(1)) {
                syncModifyVisitConfigList.add(terminalSyncVisitConfigResponse);
            } else if (request.getLastSyncTime().longValue() > 0) {
                syncDeleteVisitConfigList.add(terminalSyncVisitConfigResponse);
            }
        }
        VisitConfig visitConfig = this.visitConfigMapper.getOneById(request.getOrgId(), 1L);
        if (device != null && device.getDeviceType().equals(Integer.valueOf(DeviceTypeEnum.ENTRANCE_VISIT.getValue()))) {
            terminalSyncResponse.setSyncTime(Long.valueOf(visitConfig.getGmtModify().getTime()));
        } else {
            VisitDevice visitDevice2 = this.visitDeviceMapper.getLatestByDeviceAndVisitConfig(request.getOrgId(), request.getDeviceSn(), 1L);
            terminalSyncResponse.setSyncTime(Long.valueOf(visitConfig.getGmtModify().getTime() > visitDevice2.getGmtModify().getTime() ? visitConfig.getGmtModify().getTime() : visitDevice2.getGmtModify().getTime()));
        }
        terminalSyncResponse.setSyncDeleteResult(syncDeleteVisitConfigList);
        terminalSyncResponse.setSyncInsertResult(syncInsertVisitConfigList);
        terminalSyncResponse.setSyncModifyResult(syncModifyVisitConfigList);
        doCacheLastModifyTime(CacheKeyGenerateUtils.getVisitConfigLastModifyTimeKey(request.getDeviceSn()), terminalSyncResponse.getSyncTime());
        return terminalSyncResponse;
    }

    private void doCacheLastModifyTime(String key, Long lastModifyTime) {
        DeviceLastModifyTimeInfo info = CacheAdapter.getLastModifyTime(key);
        if (info == null) {
            info = new DeviceLastModifyTimeInfo();
        }
        info.setLastModifyTime(lastModifyTime);
        info.setExpireTime(Long.valueOf(MyDateUtils.addYears(new Date(), 30).getTime()));
        CacheAdapter.cacheLastModifyTime(key, info);
    }

    @Override // com.moredian.onpremise.api.visit.VisitConfigService
    public List<String> uploadConfigFile(MultipartFile file) {
        List<String> absoUrls = this.uploadConfig.uploadVisitConfigFileForReturnUrl(file);
        List<String> relaUrls = new ArrayList<>();
        for (String absoUrl : absoUrls) {
            relaUrls.add(this.uploadConfig.getRelativePath(absoUrl));
        }
        return relaUrls;
    }
}
