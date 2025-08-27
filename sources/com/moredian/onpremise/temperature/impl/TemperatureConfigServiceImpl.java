package com.moredian.onpremise.temperature.impl;

import com.moredian.onpremise.api.temperature.TemperatureConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.enums.DeviceOnlineStatusEnum;
import com.moredian.onpremise.core.common.enums.DeviceTypeEnum;
import com.moredian.onpremise.core.mapper.ConfigMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.TemperatureDeviceMapper;
import com.moredian.onpremise.core.model.domain.Config;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.TemperatureDevice;
import com.moredian.onpremise.core.model.dto.TemperatureDeviceDto;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.request.TemperatureConfigRequest;
import com.moredian.onpremise.core.model.response.ListSearchDeviceResponse;
import com.moredian.onpremise.core.model.response.TemperatureConfigResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.SyncTemperatureConfigRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-temperature-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/temperature/impl/TemperatureConfigServiceImpl.class */
public class TemperatureConfigServiceImpl implements TemperatureConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private TemperatureDeviceMapper temperatureDeviceMapper;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TemperatureConfigServiceImpl.class);
    private static final BigDecimal DEFAULT_TEMPERATURE_ALERT = new BigDecimal(37.3d);
    private static final Integer DEFAULT_OPENFLAG = 0;
    private static final Integer DEFAULT_DEVICE_ENABLE = 1;
    private static final Integer DEFAULT_STRANGER_OPENFLAG = 0;
    private static final BigDecimal DEFAULT_FACTOR = new BigDecimal(1.0d);

    @Override // com.moredian.onpremise.api.temperature.TemperatureConfigService
    public TemperatureConfigResponse queryConfigWithoutDevice() {
        TemperatureConfigResponse result = new TemperatureConfigResponse();
        Config configAlert = CacheAdapter.getConfig(ConfigConstants.TEMPERATURE_ALERT_KEY);
        if (configAlert == null) {
            configAlert = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_ALERT_KEY);
        }
        Config configOpenFlag = CacheAdapter.getConfig(ConfigConstants.TEMPERATURE_OPENFLAG_KEY);
        if (configOpenFlag == null) {
            configOpenFlag = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_OPENFLAG_KEY);
        }
        Config strangerOpenDoorFlag = CacheAdapter.getConfig(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY);
        if (strangerOpenDoorFlag == null) {
            strangerOpenDoorFlag = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY);
        }
        result.setTemperatureAlert(configAlert != null ? NumberUtils.createBigDecimal(configAlert.getConfigValue()) : DEFAULT_TEMPERATURE_ALERT);
        result.setOpenDoorFlag(configOpenFlag != null ? NumberUtils.createInteger(configOpenFlag.getConfigValue()) : DEFAULT_OPENFLAG);
        result.setStrangerOpenDoorFlag(strangerOpenDoorFlag != null ? NumberUtils.createInteger(strangerOpenDoorFlag.getConfigValue()) : DEFAULT_STRANGER_OPENFLAG);
        return result;
    }

    @Override // com.moredian.onpremise.api.temperature.TemperatureConfigService
    public TemperatureConfigResponse queryConfig(TemperatureConfigRequest request) throws BeansException {
        TemperatureConfigResponse result = queryConfigWithoutDevice();
        request.setManageDeviceSnList(UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()));
        Map<String, TemperatureDeviceDto> devices = new TreeMap<>();
        List<TemperatureDevice> listDevice = this.temperatureDeviceMapper.listDevice(request);
        if (!CollectionUtils.isEmpty(listDevice)) {
            for (TemperatureDevice device : listDevice) {
                TemperatureDeviceDto tmpDeviceDto = new TemperatureDeviceDto();
                BeanUtils.copyProperties(device, tmpDeviceDto);
                Device deviceInfo = this.deviceMapper.getDeviceInfoByDeviceSn(device.getDeviceSn());
                if (deviceInfo == null || (!deviceInfo.getDeviceType().equals(8) && !deviceInfo.getDeviceType().equals(9))) {
                    this.temperatureDeviceMapper.delete(device);
                } else {
                    tmpDeviceDto.setDeviceName(deviceInfo.getDeviceName());
                    devices.put(tmpDeviceDto.getDeviceSn(), tmpDeviceDto);
                }
            }
        }
        List<ListSearchDeviceResponse> listMometerA = this.deviceMapper.listSearchDevice(request.getOrgId(), null, null, null, Integer.valueOf(DeviceTypeEnum.ENTRANCE_THERMOMETER_A.getValue()), "", "", null);
        List<ListSearchDeviceResponse> listMometerB = this.deviceMapper.listSearchDevice(request.getOrgId(), null, null, null, Integer.valueOf(DeviceTypeEnum.ENTRANCE_THERMOMETER_B.getValue()), "", "", null);
        Map<String, TemperatureDeviceDto> devices2 = appendDeviceList(appendDeviceList(devices, listMometerA, request.getOrgId()), listMometerB, request.getOrgId());
        for (TemperatureDeviceDto tempDevice : devices2.values()) {
            CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(tempDevice.getDeviceSn()));
            if (heartBeatInfo != null) {
                tempDevice.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
            } else {
                tempDevice.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()));
            }
        }
        result.setDevices(new ArrayList(devices2.values()));
        return result;
    }

    @Override // com.moredian.onpremise.api.temperature.TemperatureConfigService
    @Transactional
    public Long updateConfig(TemperatureConfigRequest request) {
        if (request.getTemperatureAlert() != null) {
            if (this.configMapper.update(ConfigConstants.TEMPERATURE_ALERT_KEY, request.getTemperatureAlert().toString()) == 0) {
                this.configMapper.insert(ConfigConstants.TEMPERATURE_ALERT_KEY, request.getTemperatureAlert().toString());
            }
            Config configAlert = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_ALERT_KEY);
            CacheAdapter.cacheConfig(ConfigConstants.TEMPERATURE_ALERT_KEY, configAlert);
        }
        if (request.getOpenDoorFlag() != null) {
            if (this.configMapper.update(ConfigConstants.TEMPERATURE_OPENFLAG_KEY, request.getOpenDoorFlag().toString()) == 0) {
                this.configMapper.insert(ConfigConstants.TEMPERATURE_OPENFLAG_KEY, request.getOpenDoorFlag().toString());
            }
            Config configOpenFlag = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_OPENFLAG_KEY);
            CacheAdapter.cacheConfig(ConfigConstants.TEMPERATURE_OPENFLAG_KEY, configOpenFlag);
        }
        if (request.getStrangerOpenDoorFlag() != null) {
            if (this.configMapper.update(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY, request.getStrangerOpenDoorFlag().toString()) == 0) {
                this.configMapper.insert(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY, request.getStrangerOpenDoorFlag().toString());
            }
            Config strangerOpenDoorFlag = this.configMapper.getOneByKey(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY);
            CacheAdapter.cacheConfig(ConfigConstants.TEMPERATURE_STRANGER_OPENFLAG_KEY, strangerOpenDoorFlag);
        }
        if (!CollectionUtils.isEmpty(request.getDevices())) {
            for (TemperatureDeviceDto deviceDto : request.getDevices()) {
                TemperatureDevice device = new TemperatureDevice();
                device.setOrgId(request.getOrgId());
                device.setDeviceSn(deviceDto.getDeviceSn());
                device.setTemperatureEnable(deviceDto.getTemperatureEnable());
                device.setTemperatureFactor(deviceDto.getTemperatureFactor());
                if (this.temperatureDeviceMapper.update(device) == 0) {
                    this.temperatureDeviceMapper.insert(device);
                }
                this.nettyMessageApi.sendMsg(new SyncTemperatureConfigRequest(0L, request.getOrgId(), request.getOpenDoorFlag(), request.getStrangerOpenDoorFlag(), deviceDto.getTemperatureEnable(), request.getTemperatureAlert(), deviceDto.getTemperatureFactor()), Integer.valueOf(SyncTemperatureConfigRequest.MODEL_TYPE.type()), deviceDto.getDeviceSn());
            }
            return null;
        }
        return null;
    }

    private Map<String, TemperatureDeviceDto> appendDeviceList(Map<String, TemperatureDeviceDto> targetMap, List<ListSearchDeviceResponse> deviceResponseList, Long orgId) {
        if (!CollectionUtils.isEmpty(deviceResponseList)) {
            for (ListSearchDeviceResponse tmpDevice : deviceResponseList) {
                if (!targetMap.containsKey(tmpDevice.getDeviceSn())) {
                    TemperatureDeviceDto appendDevice = new TemperatureDeviceDto();
                    appendDevice.setDeviceSn(tmpDevice.getDeviceSn());
                    appendDevice.setDeviceName(tmpDevice.getDeviceName());
                    appendDevice.setTemperatureEnable(DEFAULT_DEVICE_ENABLE);
                    appendDevice.setTemperatureFactor(DEFAULT_FACTOR);
                    targetMap.put(tmpDevice.getDeviceSn(), appendDevice);
                    TemperatureDevice device = new TemperatureDevice();
                    device.setOrgId(orgId);
                    device.setDeviceSn(tmpDevice.getDeviceSn());
                    device.setTemperatureEnable(DEFAULT_DEVICE_ENABLE);
                    device.setTemperatureFactor(DEFAULT_FACTOR);
                    this.temperatureDeviceMapper.insert(device);
                }
            }
        }
        return targetMap;
    }
}
