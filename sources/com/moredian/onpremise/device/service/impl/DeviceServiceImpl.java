package com.moredian.onpremise.device.service.impl;

import com.github.pagehelper.PageHelper;
import com.moredian.onpremise.api.checkIn.CheckInService;
import com.moredian.onpremise.api.device.DeviceService;
import com.moredian.onpremise.api.meal.MealCanteenService;
import com.moredian.onpremise.api.server.ConfigService;
import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.ConfigConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.constants.DateFormatConstants;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import com.moredian.onpremise.core.common.enums.CallbackTagEnum;
import com.moredian.onpremise.core.common.enums.CheckDeviceCodeEnum;
import com.moredian.onpremise.core.common.enums.CheckInTaskNotifyTypeEnum;
import com.moredian.onpremise.core.common.enums.CommonStatusEnum;
import com.moredian.onpremise.core.common.enums.DeleteOrNotEnum;
import com.moredian.onpremise.core.common.enums.DeviceGenTypeEnum;
import com.moredian.onpremise.core.common.enums.DeviceIdentifyDistanceEnum;
import com.moredian.onpremise.core.common.enums.DeviceIdentifyModuleEnum;
import com.moredian.onpremise.core.common.enums.DeviceOnlineStatusEnum;
import com.moredian.onpremise.core.common.enums.DeviceStatusEnum;
import com.moredian.onpremise.core.common.enums.DeviceStatusEventEnum;
import com.moredian.onpremise.core.common.enums.DeviceSyncModeEnum;
import com.moredian.onpremise.core.common.enums.DeviceTypeEnum;
import com.moredian.onpremise.core.common.enums.DeviceUpgradeStatusEnum;
import com.moredian.onpremise.core.common.enums.DeviceVoiceEnum;
import com.moredian.onpremise.core.common.enums.FireWarnStatusEnum;
import com.moredian.onpremise.core.common.enums.GroupDeviceStatusEnum;
import com.moredian.onpremise.core.common.enums.MealCanteenNotifyTypeEnum;
import com.moredian.onpremise.core.common.enums.MemberTypeEnum;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleStatusEnum;
import com.moredian.onpremise.core.common.enums.UpgradeScheduleTypeEnum;
import com.moredian.onpremise.core.config.UploadConfig;
import com.moredian.onpremise.core.mapper.AccessKeyMapper;
import com.moredian.onpremise.core.mapper.AccountAuthMapper;
import com.moredian.onpremise.core.mapper.AccountMapper;
import com.moredian.onpremise.core.mapper.AttendanceGroupDeviceMapper;
import com.moredian.onpremise.core.mapper.AuthCodeMapper;
import com.moredian.onpremise.core.mapper.CallbackServerMapper;
import com.moredian.onpremise.core.mapper.DeviceGroupMapper;
import com.moredian.onpremise.core.mapper.DeviceLogMapper;
import com.moredian.onpremise.core.mapper.DeviceMapper;
import com.moredian.onpremise.core.mapper.DeviceMsgLogMapper;
import com.moredian.onpremise.core.mapper.GroupDeviceMapper;
import com.moredian.onpremise.core.mapper.GroupMapper;
import com.moredian.onpremise.core.mapper.GroupMemberMapper;
import com.moredian.onpremise.core.mapper.MemberAuthInfoMapper;
import com.moredian.onpremise.core.mapper.MemberMapper;
import com.moredian.onpremise.core.mapper.OrganizationMapper;
import com.moredian.onpremise.core.mapper.UpgradeRecordMapper;
import com.moredian.onpremise.core.mapper.UpgradeScheduleMapper;
import com.moredian.onpremise.core.model.domain.AccessKey;
import com.moredian.onpremise.core.model.domain.Account;
import com.moredian.onpremise.core.model.domain.AccountAuth;
import com.moredian.onpremise.core.model.domain.CallbackServer;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.domain.DeviceGroup;
import com.moredian.onpremise.core.model.domain.DeviceLog;
import com.moredian.onpremise.core.model.domain.DeviceMsgLog;
import com.moredian.onpremise.core.model.domain.Group;
import com.moredian.onpremise.core.model.domain.GroupDevice;
import com.moredian.onpremise.core.model.domain.GroupMember;
import com.moredian.onpremise.core.model.domain.Organization;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceRecord;
import com.moredian.onpremise.core.model.domain.UpgradeDeviceSchedule;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import com.moredian.onpremise.core.model.dto.SaveDeviceDto;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.model.info.CacheUpgradeStatusInfo;
import com.moredian.onpremise.core.model.info.FireWarnStatusInfo;
import com.moredian.onpremise.core.model.request.ActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.BatchSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.BindDeviceGroupRequest;
import com.moredian.onpremise.core.model.request.CancelUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.DeleteDeviceRequest;
import com.moredian.onpremise.core.model.request.DeviceCallbackRequest;
import com.moredian.onpremise.core.model.request.DeviceDetailRequest;
import com.moredian.onpremise.core.model.request.DeviceListRequest;
import com.moredian.onpremise.core.model.request.DevicePushLogRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgQueryRequest;
import com.moredian.onpremise.core.model.request.DevicePushMsgRequest;
import com.moredian.onpremise.core.model.request.DeviceResetDataRequest;
import com.moredian.onpremise.core.model.request.DeviceRestartRequest;
import com.moredian.onpremise.core.model.request.DeviceShowDataRequest;
import com.moredian.onpremise.core.model.request.DownloadDeviceLogRequest;
import com.moredian.onpremise.core.model.request.GetGroupDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeDeviceRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeRecordRequest;
import com.moredian.onpremise.core.model.request.ListUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.MemberListRequest;
import com.moredian.onpremise.core.model.request.OpenDoorRequest;
import com.moredian.onpremise.core.model.request.PreActiveDeviceRequest;
import com.moredian.onpremise.core.model.request.PullDeviceLogRequest;
import com.moredian.onpremise.core.model.request.RetryUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.SaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.SaveUpgradeScheduleRequest;
import com.moredian.onpremise.core.model.request.TerminalCheckDeviceActiveRequest;
import com.moredian.onpremise.core.model.request.TerminalSaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.UpdateDeviceRequest;
import com.moredian.onpremise.core.model.request.UploadUpgradePackageRequest;
import com.moredian.onpremise.core.model.response.ActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.DeviceConfigResponse;
import com.moredian.onpremise.core.model.response.DevicePushMsgResponse;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.model.response.DownloadDeviceLogResponse;
import com.moredian.onpremise.core.model.response.GroupDeviceResponse;
import com.moredian.onpremise.core.model.response.ListSearchDeviceResponse;
import com.moredian.onpremise.core.model.response.MemberListResponse;
import com.moredian.onpremise.core.model.response.PreActiveDeviceResponse;
import com.moredian.onpremise.core.model.response.QueryConfigResponse;
import com.moredian.onpremise.core.model.response.TerminalCheckDeviceActiveResponse;
import com.moredian.onpremise.core.model.response.UpgradeRecordResponse;
import com.moredian.onpremise.core.model.response.UpgradeScheduleResponse;
import com.moredian.onpremise.core.model.response.UploadUpgradePackageResponse;
import com.moredian.onpremise.core.model.response.UserLoginResponse;
import com.moredian.onpremise.core.utils.ApkPaserUtils;
import com.moredian.onpremise.core.utils.AssertUtil;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.HttpUtils;
import com.moredian.onpremise.core.utils.JsonUtils;
import com.moredian.onpremise.core.utils.JwtUtils;
import com.moredian.onpremise.core.utils.MyBase64Utils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.core.utils.MyFileUtils;
import com.moredian.onpremise.core.utils.MyListUtils;
import com.moredian.onpremise.core.utils.PageList;
import com.moredian.onpremise.core.utils.Paginator;
import com.moredian.onpremise.core.utils.RSAUtils;
import com.moredian.onpremise.core.utils.VersionUtils;
import com.moredian.onpremise.iot.IOTSession;
import com.moredian.onpremise.iot.handle.NettyMessageApi;
import com.moredian.onpremise.model.DeviceUnbindRequest;
import com.moredian.onpremise.model.DeviceVoiceConfigRequest;
import com.moredian.onpremise.model.PullDeviceLogEvent;
import com.moredian.onpremise.model.SyncGroupRequest;
import com.moredian.onpremise.model.TerminalDeviceUpgradeRequest;
import com.moredian.onpremise.model.TerminalOpenDoorRequest;
import com.moredian.onpremise.model.TerminalPushMsgRequest;
import com.moredian.onpremise.model.TerminalResetDataRequest;
import com.moredian.onpremise.model.TerminalRestartRequest;
import com.moredian.onpremise.model.TerminalShowDataRequest;
import io.netty.channel.Channel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
/* loaded from: onpremise-device-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/device/service/impl/DeviceServiceImpl.class */
public class DeviceServiceImpl implements DeviceService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DeviceServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private GroupDeviceMapper groupDeviceMapper;

    @Autowired
    private AccessKeyMapper accessKeyMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private OrganizationMapper orgMapper;

    @Value("${onpremise.save.apk.path}")
    private String saveApkPath;

    @Value("${onpremise.save.log.path}")
    private String saveLogPath;

    @Value("${onpremise.license.private.key}")
    private String privateKey;

    @Autowired
    private UploadConfig uplocadConfig;

    @Autowired
    private NettyMessageApi nettyMessageApi;

    @Autowired
    private UpgradeRecordMapper upgradeRecordMapper;

    @Autowired
    private UpgradeScheduleMapper upgradeScheduleMapper;

    @Autowired
    private AuthCodeMapper authCodeMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MemberAuthInfoMapper memberAuthInfoMapper;

    @Autowired
    private DeviceLogMapper deviceLogMapper;

    @Autowired
    private AttendanceGroupDeviceMapper attendanceGroupDeviceMapper;

    @Autowired
    private MealCanteenService mealCanteenService;

    @Autowired
    private CallbackServerMapper callbackServerMapper;

    @Autowired
    private DeviceMsgLogMapper deviceMsgLogMapper;

    @Autowired
    private DeviceGroupMapper deviceGroupMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountAuthMapper accountAuthMapper;

    @Override // com.moredian.onpremise.api.device.DeviceService
    public PageList<DeviceResponse> listDevice(DeviceListRequest request) {
        Paginator paginator = request.getPaginator();
        List<String> deviceSns = getManagerDevices(request);
        List<String> deviceSnsOffline = new ArrayList<>();
        List<String> deviceSnsOnline = new ArrayList<>();
        if (deviceSns != null) {
            for (String deviceSn : deviceSns) {
                CacheHeartBeatInfo info = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(deviceSn));
                if (info == null) {
                    deviceSnsOffline.add(deviceSn);
                } else {
                    deviceSnsOnline.add(deviceSn);
                }
            }
            if (!CollectionUtils.isEmpty(deviceSnsOnline)) {
                this.deviceMapper.updateOnlineStatusBatch(request.getOrgId(), Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()), deviceSnsOnline);
            }
            if (!CollectionUtils.isEmpty(deviceSnsOffline)) {
                this.deviceMapper.updateOnlineStatusBatch(request.getOrgId(), Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()), deviceSnsOffline);
            }
        }
        List<Long> deviceGroupIds = new ArrayList<>();
        if (request.getDeviceGroupId() != null) {
            if (request.getNeedSubDevice() != null && request.getNeedSubDevice().intValue() == 1) {
                getChildDeviceGroupIds(deviceGroupIds, request.getDeviceGroupId(), request.getOrgId());
            }
            deviceGroupIds.add(request.getDeviceGroupId());
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<DeviceResponse> responses = this.deviceMapper.listDeviceV2(request.getOrgId(), request.getDeviceType(), request.getDeviceName(), request.getDeviceSn(), request.getKeywords(), request.getOnlineStatus(), request.getDeviceModel(), request.getGroupName(), deviceSns, deviceGroupIds);
            packagingDeviceResponseBatch(responses, request.getOrgId());
            return new PageList<>(responses);
        }
        List<DeviceResponse> responses2 = this.deviceMapper.listDeviceV2(request.getOrgId(), request.getDeviceType(), request.getDeviceName(), request.getDeviceSn(), request.getKeywords(), request.getOnlineStatus(), request.getDeviceModel(), request.getGroupName(), deviceSns, deviceGroupIds);
        packagingDeviceResponseBatch(responses2, request.getOrgId());
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<ListSearchDeviceResponse> listSearchDevice(DeviceListRequest request) {
        List<Long> deviceGroupIds = new ArrayList<>();
        if (request.getDeviceGroupId() != null) {
            if (request.getNeedSubDevice() != null && request.getNeedSubDevice().intValue() == 1) {
                getChildDeviceGroupIds(deviceGroupIds, request.getDeviceGroupId(), request.getOrgId());
            }
            deviceGroupIds.add(request.getDeviceGroupId());
        }
        List<String> deviceSns = getManagerDevices(request);
        List<ListSearchDeviceResponse> responses = this.deviceMapper.listSearchDevice(request.getOrgId(), deviceSns, request.getDeviceName(), request.getDeviceSn(), request.getDeviceType(), request.getDeviceModel(), request.getKeywords(), deviceGroupIds);
        for (ListSearchDeviceResponse response : responses) {
            CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
            if (heartBeatInfo != null) {
                response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
            } else {
                response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()));
            }
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<ListSearchDeviceResponse> listOnlineDevice(DeviceListRequest request) {
        List<Long> deviceGroupIds = new ArrayList<>();
        if (request.getDeviceGroupId() != null) {
            if (request.getNeedSubDevice() != null && request.getNeedSubDevice().intValue() == 1) {
                getChildDeviceGroupIds(deviceGroupIds, request.getDeviceGroupId(), request.getOrgId());
            }
            deviceGroupIds.add(request.getDeviceGroupId());
        }
        List<String> deviceSns = getManagerDevices(request);
        List<ListSearchDeviceResponse> responses = this.deviceMapper.listSearchDevice(request.getOrgId(), deviceSns, request.getDeviceName(), request.getDeviceSn(), request.getDeviceType(), request.getDeviceModel(), request.getKeywords(), deviceGroupIds);
        List<ListSearchDeviceResponse> result = new ArrayList<>();
        for (ListSearchDeviceResponse response : responses) {
            CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
            if (heartBeatInfo != null) {
                result.add(response);
            }
        }
        return result;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<ListSearchDeviceResponse> listUpgradeDevice(ListUpgradeDeviceRequest request) {
        AssertUtil.isNullOrEmpty(request.getPackagePath(), OnpremiseErrorEnum.UPGRADE_PATH_MUST_NOT_NULL);
        UploadUpgradePackageResponse packageResponse = CacheAdapter.getUpgradePackageComments(request.getPackagePath());
        List<ListSearchDeviceResponse> responses = this.deviceMapper.listUpgradeDevice(packageResponse.getSupportDevice().split(","), request.getOrgId(), UserLoginResponse.getAccountManageDeviceSn(request.getSessionId()), request.getSearchKey(), request.getDeviceType(), request.getDeviceGroupId());
        Iterator<ListSearchDeviceResponse> iterator = responses.iterator();
        while (iterator.hasNext()) {
            ListSearchDeviceResponse response = iterator.next();
            CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
            if (heartBeatInfo != null) {
                response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
                if (UpgradeScheduleTypeEnum.APK.getValue() == NumberUtils.toInt(packageResponse.getScheduleType())) {
                    if (VersionUtils.compareVersion(packageResponse.getVersionCode(), heartBeatInfo.getSoftVersion()) <= 0) {
                        iterator.remove();
                    }
                } else if (VersionUtils.compareVersion(packageResponse.getVersionCode(), heartBeatInfo.getRomVersion()) <= 0) {
                    iterator.remove();
                }
            }
        }
        Collections.sort(responses, new Comparator<ListSearchDeviceResponse>() { // from class: com.moredian.onpremise.device.service.impl.DeviceServiceImpl.1
            @Override // java.util.Comparator
            public int compare(ListSearchDeviceResponse arg0, ListSearchDeviceResponse arg1) {
                if (arg0.getOnlineStatus().compareTo(arg1.getOnlineStatus()) == 0) {
                    if (arg0.getDeviceName().compareTo(arg1.getDeviceName()) == 0) {
                        return arg0.getDeviceSn().compareTo(arg1.getDeviceSn());
                    }
                    return arg0.getDeviceName().compareTo(arg1.getDeviceName());
                }
                return arg0.getOnlineStatus().compareTo(arg1.getOnlineStatus());
            }
        });
        return responses;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public GroupDeviceResponse getGroupDeviceByDeviceId(GetGroupDeviceRequest request) throws BeansException {
        Device device = this.deviceMapper.getDeviceInfoByDeviceId(request.getDeviceId(), request.getOrgId());
        GroupDeviceResponse response = new GroupDeviceResponse();
        BeanUtils.copyProperties(device, response);
        packagingDeviceGroup(response, request.getOrgId());
        CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
        if (heartBeatInfo != null) {
            response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
        } else {
            response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()));
        }
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean pullDeviceLog(PullDeviceLogRequest request) {
        DeviceLog deviceLog = new DeviceLog();
        deviceLog.setOrgId(request.getOrgId());
        deviceLog.setDeviceSn(request.getDeviceSn());
        deviceLog.setStatus(Integer.valueOf(CommonStatusEnum.NO.getValue()));
        this.deviceLogMapper.insertDeviceLog(deviceLog);
        if (StringUtils.isEmpty(request.getDeviceSn())) {
            List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
            if (CollectionUtils.isEmpty(deviceSnList)) {
                return true;
            }
            this.nettyMessageApi.sendMsg(new PullDeviceLogEvent(request.getPullLogDate()), Integer.valueOf(PullDeviceLogEvent.MODEL_TYPE.type()), deviceSnList);
            return true;
        }
        AssertUtil.isNullOrEmpty(this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn()), OnpremiseErrorEnum.DEVICE_NOT_FIND);
        this.nettyMessageApi.sendMsg(new PullDeviceLogEvent(request.getPullLogDate()), Integer.valueOf(PullDeviceLogEvent.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean devicePushLog(DevicePushLogRequest request) {
        String url = MyFileUtils.upload(request.getFile(), this.saveLogPath + DateFormatUtils.format(new Date(), DateFormatConstants.DATE_FOR_DIAGONAL) + "/", request.getDeviceSn() + "-" + request.getFileName());
        logger.info("设备日志文件" + this.uplocadConfig.getDeviceLogUrl(url));
        DeviceLog deviceLog = this.deviceLogMapper.getLatestDeviceLogByDeviceSn(request.getDeviceSn(), request.getOrgId());
        if (deviceLog == null) {
            DeviceLog deviceLog2 = new DeviceLog();
            deviceLog2.setOrgId(request.getOrgId());
            deviceLog2.setDeviceSn(request.getDeviceSn());
            deviceLog2.setLogUrl(this.uplocadConfig.getDeviceLogUrl(url));
            deviceLog2.setStatus(Integer.valueOf(CommonStatusEnum.YES.getValue()));
            this.deviceLogMapper.insertDeviceLog(deviceLog2);
            return true;
        }
        deviceLog.setLogUrl(this.uplocadConfig.getDeviceLogUrl(url));
        deviceLog.setStatus(Integer.valueOf(CommonStatusEnum.YES.getValue()));
        this.deviceLogMapper.updateDeviceLogById(deviceLog);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean openTheDoor(OpenDoorRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        List<String> manageDeviceSn = UserLoginResponse.getAccountManageDeviceSn(request.getSessionId());
        if (MyListUtils.listIsEmpty(manageDeviceSn)) {
            AssertUtil.isTrue(Boolean.valueOf(manageDeviceSn.contains(request.getDeviceSn())), OnpremiseErrorEnum.DEVICE_NOT_FIND);
        }
        Device info = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(info, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        AccessKey accessKey = this.accessKeyMapper.getOneByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(accessKey, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        Map<String, Object> params = new HashMap<>(3);
        params.put("deviceSn", request.getDeviceSn());
        params.put("secretKey", accessKey.getAccessKeySecret());
        params.put("timestamp", Long.valueOf(System.currentTimeMillis()));
        String encode = MyBase64Utils.encodeStringForString(RSAUtils.encryptByPrivateKey(JsonUtils.toJson(params).getBytes(), this.privateKey));
        this.nettyMessageApi.sendMsg(new TerminalOpenDoorRequest(encode), Integer.valueOf(TerminalOpenDoorRequest.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public PageList<GroupDeviceResponse> groupDeviceList(DeviceListRequest request) {
        Paginator paginator = request.getPaginator();
        List<String> deviceSns = UserLoginResponse.getAccountManageDeviceSn(request.getSessionId());
        List<Integer> deviceTypes = new ArrayList<>();
        if (request.getDeviceType() != null) {
            deviceTypes.add(request.getDeviceType());
        }
        if (!CollectionUtils.isEmpty(request.getDeviceTypes())) {
            deviceTypes.addAll(request.getDeviceTypes());
        }
        List<Long> deviceGroupIds = new ArrayList<>();
        if (request.getDeviceGroupId() != null) {
            if (request.getNeedSubDevice() != null && request.getNeedSubDevice().intValue() == 1) {
                getChildDeviceGroupIds(deviceGroupIds, request.getDeviceGroupId(), request.getOrgId());
            }
            deviceGroupIds.add(request.getDeviceGroupId());
        }
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<GroupDeviceResponse> responses = this.deviceMapper.groupDeviceList(request.getOrgId(), deviceTypes, request.getDeviceName(), deviceSns, request.getDeviceModel(), request.getKeywords(), deviceGroupIds);
            packagingDeviceOnlineStatus(responses);
            return new PageList<>(responses);
        }
        List<GroupDeviceResponse> responses2 = this.deviceMapper.groupDeviceList(request.getOrgId(), deviceTypes, request.getDeviceName(), deviceSns, request.getDeviceModel(), request.getKeywords(), deviceGroupIds);
        packagingDeviceOnlineStatus(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    private void packagingDeviceOnlineStatus(List<GroupDeviceResponse> responses) {
        for (GroupDeviceResponse response : responses) {
            CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
            if (heartBeatInfo != null) {
                response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
            } else {
                response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()));
            }
        }
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public DeviceResponse deviceDetail(DeviceDetailRequest request) throws BeansException {
        Device device = this.deviceMapper.getDeviceDetail(request.getDeviceSn(), request.getOrgId());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        DeviceResponse response = new DeviceResponse();
        BeanUtils.copyProperties(device, response);
        packagingDeviceGroupName(response, request.getOrgId());
        packagingDeviceOnlineStatus(response);
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public DeviceResponse deviceDetailBySn(DeviceDetailRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        DeviceResponse response = new DeviceResponse();
        BeanUtils.copyProperties(device, response);
        packagingDeviceGroupName(response, request.getOrgId());
        packagingDeviceOnlineStatus(response);
        return response;
    }

    private void packagingDeviceOnlineStatus(DeviceResponse response) {
        CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
        if (heartBeatInfo != null) {
            response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
        } else {
            response.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_NO.getValue()));
        }
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteDevice(DeleteDeviceRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getDeviceId() != null && request.getDeviceId().longValue() > 0), OnpremiseErrorEnum.DEVICE_ID_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceId(request.getDeviceId(), request.getOrgId());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.deviceMapper.deleteDevice(request.getDeviceId(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_DEVICE_FAIL);
        doDeleteDevice(device);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean deleteDeviceByDeviceSn(DeleteDeviceRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.deviceMapper.deleteDeviceByDeviceSn(request.getDeviceSn(), request.getOrgId()) > 0), OnpremiseErrorEnum.DELETE_DEVICE_FAIL);
        doDeleteDevice(device);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean updateDevice(UpdateDeviceRequest request) {
        this.deviceMapper.updateDevice(request);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean saveDeviceConfig(SaveDeviceConfigRequest request, Boolean groupFlag) {
        Group group;
        CacheHeartBeatInfo info = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(request.getDeviceSn()));
        AssertUtil.isNullOrEmpty(info, OnpremiseErrorEnum.NOT_FIND_ONLINE_DEVICE);
        Device device = checkSaveDeviceConfigParams(request.getDeviceSn(), request.getDeviceName());
        int result = this.deviceMapper.saveDeviceConfig(request);
        AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.UPDATE_DEVICE_INFO_FAIL);
        if (groupFlag.booleanValue()) {
            List<GroupDeviceGroupDto> oldGroups = this.groupDeviceMapper.listByDeviceId(device.getDeviceSn(), device.getOrgId());
            MyListUtils<GroupDeviceGroupDto> utils = new MyListUtils<>();
            List<GroupDeviceGroupDto> deleteGroups = utils.difference(oldGroups, request.getGroups());
            List<GroupDeviceGroupDto> insertGroups = utils.difference(request.getGroups(), oldGroups);
            List<GroupDeviceGroupDto> repeatGroups = utils.union(request.getGroups(), oldGroups);
            logger.info("=================deleteGroups:{},insertGroups:{}============,repeatGroups:{}============", Integer.valueOf(deleteGroups.size()), Integer.valueOf(insertGroups.size()), Integer.valueOf(repeatGroups.size()));
            Long time = Long.valueOf(System.currentTimeMillis());
            logger.info("==========start :{}", time);
            if (deleteGroups != null && deleteGroups.size() > 0) {
                List<Long> groupIds = new ArrayList<>();
                for (GroupDeviceGroupDto deviceGroupDto : deleteGroups) {
                    this.groupMapper.updateModifyTime(deviceGroupDto.getGroupId(), request.getOrgId());
                    this.groupDeviceMapper.deleteGroupDevice(deviceGroupDto.getGroupId(), device.getDeviceSn(), device.getOrgId());
                    groupIds.add(deviceGroupDto.getGroupId());
                }
            }
            logger.info("==========delete end  :{}", Long.valueOf(System.currentTimeMillis() - time.longValue()));
            if (insertGroups != null && insertGroups.size() > 0) {
                Iterator<GroupDeviceGroupDto> it = insertGroups.iterator();
                while (it.hasNext()) {
                    doInsertGroupDevice(device, it.next().getGroupId());
                }
            }
            logger.info("==========insert end  :{}", Long.valueOf(System.currentTimeMillis() - time.longValue()));
            if ((MyListUtils.listIsEmpty(deleteGroups) || MyListUtils.listIsEmpty(insertGroups)) && (group = this.groupMapper.getLastModify(request.getOrgId())) != null) {
                this.nettyMessageApi.sendMsg(new SyncGroupRequest(Long.valueOf(group.getGmtModify().getTime()), group.getOrgId()), Integer.valueOf(SyncGroupRequest.MODEL_TYPE.type()), device.getDeviceSn());
            }
        }
        if (request.getSyncMode() != null) {
            if (request.getSyncMode().equals(Integer.valueOf(DeviceSyncModeEnum.REALTIME.getValue()))) {
                CacheAdapter.cacheSetMemberAdd(ConfigConstants.DEVICE_SYNCMODE_KEY, request.getDeviceSn());
            } else {
                CacheAdapter.cacheSetMemberRemove(ConfigConstants.DEVICE_SYNCMODE_KEY, request.getDeviceSn());
            }
        }
        Device deviceConfig = this.deviceMapper.getDeviceDetail(request.getDeviceSn(), request.getOrgId());
        this.nettyMessageApi.sendMsg(new DeviceVoiceConfigRequest(request.getDeviceSn(), request.getGenType(), request.getDeviceName(), request.getDeviceVoice(), request.getOpeningTime(), request.getIdentifyDistance(), request.getIdentifyModule(), request.getThresholdScore(), request.getFastMode(), request.getLight(), request.getMaskMode(), request.getRingMode(), request.getFontSize(), request.getShowTime(), request.getDirection(), request.getFocus(), request.getMaskRemindPicture(), request.getMaskRemindVoice(), Long.valueOf(deviceConfig.getGmtModify().getTime())), Integer.valueOf(DeviceVoiceConfigRequest.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean batchSaveDeviceConfig(BatchSaveDeviceConfigRequest request) throws BeansException {
        for (SaveDeviceDto deviceDto : request.getDeviceList()) {
            SaveDeviceConfigRequest saveDeviceConfigRequest = new SaveDeviceConfigRequest();
            BeanUtils.copyProperties(request, saveDeviceConfigRequest);
            saveDeviceConfigRequest.setDeviceName(deviceDto.getDeviceName());
            saveDeviceConfigRequest.setDeviceSn(deviceDto.getDeviceSn());
            saveDeviceConfig(saveDeviceConfigRequest, false);
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean terminalSaveDeviceConfig(TerminalSaveDeviceConfigRequest request) throws BeansException {
        logger.info("request :{}", JsonUtils.toJson(request));
        checkSaveDeviceConfigParams(request.getDeviceSn(), request.getDeviceName());
        SaveDeviceConfigRequest configRequest = new SaveDeviceConfigRequest();
        BeanUtils.copyProperties(request, configRequest);
        AssertUtil.isTrue(Boolean.valueOf(this.deviceMapper.saveDeviceConfig(configRequest) > 0), OnpremiseErrorEnum.UPDATE_DEVICE_INFO_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public synchronized ActiveDeviceResponse activeDevice(ActiveDeviceRequest request) throws BeansException {
        Organization organization = this.orgMapper.getOne();
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getSecret(), OnpremiseErrorEnum.SECRET_MUST_NOT_NULL);
        checkActiveParams(request);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            int deviceNum = this.deviceMapper.countDevice(organization.getOrgId());
            int deviceAllowMax = this.authCodeMapper.countAllowDeviceNum(organization.getOrgId());
            logger.info("===========deviceNum :{} , deviceAllowMax {}", Integer.valueOf(deviceNum), Integer.valueOf(deviceAllowMax));
            AssertUtil.isTrue(Boolean.valueOf(deviceAllowMax > deviceNum), OnpremiseErrorEnum.DEVICE_EXCEED_MAX_ALLOW);
            device = requestToDevice(request, organization.getOrgId());
            int result = this.deviceMapper.insertDevice(device);
            AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.INSERT_DEVICE_FAIL);
            bindDefaultGroup(device);
        }
        AccessKey accessKey = this.accessKeyMapper.getOneBySecret(request.getSecret(), request.getDeviceSn());
        if (accessKey == null) {
            accessKey = new AccessKey();
            accessKey.setAccessKeySecret(request.getSecret());
            accessKey.setDeviceSn(request.getDeviceSn());
            accessKey.setKeyStatus(2);
            AssertUtil.isTrue(Boolean.valueOf(this.accessKeyMapper.insert(accessKey) > 0), OnpremiseErrorEnum.INSERT_DEVICE_FAIL);
        }
        try {
            String key = CacheKeyGenerateUtils.getHeartBeatCacheKey(device.getDeviceSn());
            if (CacheAdapter.getHeartBeatInfo(key) != null) {
                CacheAdapter.removeHeartBeatInfo(key);
            }
            CacheHeartBeatInfo heartBeatInfo = new CacheHeartBeatInfo();
            heartBeatInfo.setIpAddress(device.getDeviceIp());
            heartBeatInfo.setMacAddress(device.getMacAddress());
            heartBeatInfo.setDeviceSn(device.getDeviceSn());
            heartBeatInfo.setSoftVersion(device.getVersion());
            heartBeatInfo.setRomVersion(device.getRomVersion());
            heartBeatInfo.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 35).getTime()));
            CacheAdapter.cacheHeartBeatInfo(key, heartBeatInfo);
        } catch (Exception e) {
        }
        DeviceCallbackRequest deviceCallbackRequest = new DeviceCallbackRequest();
        deviceCallbackRequest.setOrgId(device.getOrgId());
        deviceCallbackRequest.setDeviceSn(device.getDeviceSn());
        deviceCallbackRequest.setDeviceName(device.getDeviceName());
        deviceCallbackRequest.setEvent(Integer.valueOf(DeviceStatusEventEnum.ACTIVE.getStatus()));
        deviceCallbackRequest.setTimestamp(Long.valueOf(System.currentTimeMillis()));
        sendCallback(deviceCallbackRequest);
        return packagingActiveResponse(device, accessKey);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public TerminalCheckDeviceActiveResponse checkDeviceActive(TerminalCheckDeviceActiveRequest request) {
        TerminalCheckDeviceActiveResponse response = new TerminalCheckDeviceActiveResponse();
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            response.setActiveFlag(false);
            response.setCode(Integer.valueOf(CheckDeviceCodeEnum.NOT_AVTIVE.getValue()));
        } else {
            response.setActiveFlag(true);
            response.setOrgId(device.getOrgId());
            response.setCode(Integer.valueOf(CheckDeviceCodeEnum.NORMAL.getValue()));
            String uuid = UUID.randomUUID().toString();
            response.setToken(JwtUtils.token(uuid, uuid, MyDateUtils.addYears(new Date(), 50)));
        }
        response.setSystemCurrentTime(Long.valueOf(System.currentTimeMillis()));
        QueryConfigResponse config = this.configService.getOneByKey(ConfigConstants.TIME_ZONE_KEY);
        response.setTimeZone(config == null ? ConfigConstants.DEFAULT_TIME_ZONE : config.getConfigValue());
        response.setOrgCode(CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY));
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public UploadUpgradePackageResponse uploadUpgradePackage(UploadUpgradePackageRequest request) throws IOException {
        UploadUpgradePackageResponse response = new UploadUpgradePackageResponse();
        String path = MyFileUtils.upload(request.getFile(), this.saveApkPath);
        AssertUtil.isNullOrEmpty(path, OnpremiseErrorEnum.FILE_UPLOAD_FAIL);
        Map<String, String> result = ApkPaserUtils.parseUpgradeDescFile(this.saveApkPath + path);
        logger.info("parse result :{}", JsonUtils.toJson(result));
        response.setReleaseNote(result.get(UpgradeConstants.UPGRADE_PARAM_RELEASE_NOTE_KEY));
        response.setReleaseTime(result.get(UpgradeConstants.UPGRADE_PARAM_RELEASE_TIME_KEY));
        response.setScheduleType(result.get(UpgradeConstants.UPGRADE_PARAM_SCHEDULE_TYPE_KEY));
        response.setSupportDevice(result.get(UpgradeConstants.UPGRADE_PARAM_SUPPORT_DEVICE_KEY));
        response.setVersionCode(result.get(UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY));
        response.setPackagePath(this.saveApkPath + path);
        response.setDeviceType(Integer.valueOf(NumberUtils.toInt(result.get(UpgradeConstants.UPGRADE_PARAM_BUSINESS_TYPE_KEY))));
        CacheAdapter.cacheUpgradePackageComments(response.getPackagePath(), response);
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public UploadUpgradePackageResponse uploadUpgradePackageV2(UploadUpgradePackageRequest request) throws IOException {
        UploadUpgradePackageResponse response = new UploadUpgradePackageResponse();
        String path = MyFileUtils.copyFileUsingFileChannels(request.getFilePath(), this.saveApkPath);
        AssertUtil.isNullOrEmpty(path, OnpremiseErrorEnum.FILE_UPLOAD_FAIL);
        Map<String, String> result = ApkPaserUtils.parseUpgradeDescFile(this.saveApkPath + path);
        logger.info("parse result :{}", JsonUtils.toJson(result));
        response.setReleaseNote(result.get(UpgradeConstants.UPGRADE_PARAM_RELEASE_NOTE_KEY));
        response.setReleaseTime(result.get(UpgradeConstants.UPGRADE_PARAM_RELEASE_TIME_KEY));
        response.setScheduleType(result.get(UpgradeConstants.UPGRADE_PARAM_SCHEDULE_TYPE_KEY));
        response.setSupportDevice(result.get(UpgradeConstants.UPGRADE_PARAM_SUPPORT_DEVICE_KEY));
        response.setVersionCode(result.get(UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY));
        response.setPackagePath(this.saveApkPath + path);
        response.setDeviceType(Integer.valueOf(NumberUtils.toInt(result.get(UpgradeConstants.UPGRADE_PARAM_BUSINESS_TYPE_KEY))));
        CacheAdapter.cacheUpgradePackageComments(response.getPackagePath(), response);
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public String saveUpgradeSchedule(SaveUpgradeScheduleRequest request) {
        AssertUtil.isNullOrEmpty(request.getPackagePath(), OnpremiseErrorEnum.UPGRADE_PATH_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getUpgradeTime(), OnpremiseErrorEnum.UPGRADE_TIME_MUST_NOT_NULL);
        int scheduleNum = this.upgradeScheduleMapper.countNotCompleteSchedule(request.getOrgId());
        AssertUtil.isTrue(Boolean.valueOf(scheduleNum < 1), OnpremiseErrorEnum.UPGRADE_SCHEDULE_ALREADY_EXIST);
        UploadUpgradePackageResponse response = CacheAdapter.getUpgradePackageComments(request.getPackagePath());
        AssertUtil.isNullOrEmpty(response, OnpremiseErrorEnum.UPGRADE_COMMENTS_NOT_FIND);
        CacheAdapter.removeUpgradePackageComments(request.getPackagePath());
        List<String> totalDevice = passLowerVersionDevice(Integer.valueOf(NumberUtils.toInt(response.getScheduleType())), response.getVersionCode(), request.getDeviceSns());
        UpgradeDeviceSchedule schedule = saveUpgradeSchedule(response, request, totalDevice.size());
        for (String deviceSn : totalDevice) {
            saveUpgradeRecord(deviceSn, schedule.getUpgradeScheduleId(), Integer.valueOf(DeviceUpgradeStatusEnum.WAITING_STATUS.getValue()), schedule.getUpgradeType(), request.getOrgId());
        }
        logger.info("schedule time :{}", request.getUpgradeTime());
        return timeToCron(request.getUpgradeTime());
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean executeUpgradeSchedule() {
        Organization organization = this.orgMapper.getOne();
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getWaitingSchedule(organization.getOrgId());
        AssertUtil.isNullOrEmpty(schedule, OnpremiseErrorEnum.UPGRADE_SCHEDULE_NOT_FIND);
        List<String> totalDevice = passLowerVersionDevice(schedule.getUpgradeType(), schedule.getUpgradeVersion(), this.upgradeRecordMapper.getDeviceSnByScheduleId(schedule.getUpgradeScheduleId(), DeviceUpgradeStatusEnum.WAITING_STATUS.getValue()));
        List<String> onlineDevice = upgradeOnlineDeviceBatch(schedule, schedule.getUpgradePackageUrl(), totalDevice, schedule.getUpgradeType());
        upgradeOfflineDeviceBatch(schedule.getUpgradeScheduleId(), new MyListUtils().difference(totalDevice, onlineDevice), schedule.getUpgradeType(), DeviceUpgradeStatusEnum.WAITING_STATUS.getValue());
        if (onlineDevice.size() == 0) {
            int result = this.upgradeScheduleMapper.updateScheduleStatus(schedule.getUpgradeScheduleId(), schedule.getUpgradeStatus(), UpgradeScheduleStatusEnum.COMPLETED.getValue());
            AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.UPDATE_SCHEDULE_STATUS_FAIL);
            return true;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean cancelUpgradeSchedule(CancelUpgradeScheduleRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getUpgradeScheduleId() != null && request.getUpgradeScheduleId().longValue() > 0), OnpremiseErrorEnum.UPGRADE_SCHEDULE_ID_MUST_NOT_NULL);
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getOneByScheduleId(request.getUpgradeScheduleId());
        AssertUtil.isNullOrEmpty(schedule, OnpremiseErrorEnum.UPGRADE_SCHEDULE_NOT_FIND);
        int result = this.upgradeScheduleMapper.updateScheduleStatus(request.getUpgradeScheduleId(), Integer.valueOf(UpgradeScheduleStatusEnum.WAITING.getValue()), UpgradeScheduleStatusEnum.CANCEL.getValue());
        AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.UPDATE_SCHEDULE_STATUS_FAIL);
        int result2 = this.upgradeRecordMapper.updateStatusByScheduleId(request.getUpgradeScheduleId(), DeviceUpgradeStatusEnum.WAITING_STATUS.getValue(), DeviceUpgradeStatusEnum.CANCEL_STATUS.getValue(), "");
        AssertUtil.isTrue(Boolean.valueOf(result2 > 0), OnpremiseErrorEnum.CANCEL_SCHEDULE_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public void forceCloseUpgradeSchedule(CancelUpgradeScheduleRequest request) {
        AssertUtil.isTrue(Boolean.valueOf(request.getUpgradeScheduleId() != null && request.getUpgradeScheduleId().longValue() > 0), OnpremiseErrorEnum.UPGRADE_SCHEDULE_ID_MUST_NOT_NULL);
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getOneByScheduleId(request.getUpgradeScheduleId());
        AssertUtil.isNullOrEmpty(schedule, OnpremiseErrorEnum.UPGRADE_SCHEDULE_NOT_FIND);
        this.upgradeScheduleMapper.forceCloseScheduleStatus(request.getUpgradeScheduleId(), UpgradeScheduleStatusEnum.CANCEL.getValue());
        this.upgradeRecordMapper.forceCloseByScheduleId(request.getUpgradeScheduleId(), DeviceUpgradeStatusEnum.CANCEL_STATUS.getValue(), "");
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public boolean retryUpgradeSchedule(RetryUpgradeScheduleRequest request) {
        Long upgradeScheduleId = request.getUpgradeScheduleId();
        AssertUtil.isTrue(Boolean.valueOf(upgradeScheduleId != null && upgradeScheduleId.longValue() > 0), OnpremiseErrorEnum.UPGRADE_SCHEDULE_ID_MUST_NOT_NULL);
        UpgradeDeviceSchedule schedule = this.upgradeScheduleMapper.getOneByScheduleId(upgradeScheduleId);
        AssertUtil.isNullOrEmpty(schedule, OnpremiseErrorEnum.UPGRADE_SCHEDULE_NOT_FIND);
        List<String> failDevices = passLowerVersionDevice(schedule.getUpgradeType(), schedule.getUpgradeVersion(), this.upgradeRecordMapper.getDeviceSnByScheduleId(upgradeScheduleId, DeviceUpgradeStatusEnum.FAIL_STATUS.getValue()));
        AssertUtil.isTrue(Boolean.valueOf(MyListUtils.listIsEmpty(failDevices)), OnpremiseErrorEnum.UPGRADE_ALLOW_DEVICE_NOT_FIND);
        int count = 0;
        for (String deviceSn : failDevices) {
            CacheHeartBeatInfo info = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(deviceSn));
            if (info != null) {
                Channel channel = IOTSession.getChannelByTarget(deviceSn);
                if (channel != null) {
                    count++;
                    upgradeOnlineDevice(deviceSn, upgradeScheduleId, schedule.getUpgradeType(), schedule.getUpgradePackageUrl());
                    this.upgradeRecordMapper.updateStatusByDeviceSn(deviceSn, DeviceUpgradeStatusEnum.FAIL_STATUS.getValue(), upgradeScheduleId, DeviceUpgradeStatusEnum.WAITING_STATUS.getValue(), "");
                }
            }
        }
        logger.info("Update count :{}", Integer.valueOf(count));
        AssertUtil.isTrue(Boolean.valueOf(count > 0), OnpremiseErrorEnum.UPGRADE_ALLOW_DEVICE_NOT_FIND);
        AssertUtil.isTrue(Boolean.valueOf(this.upgradeScheduleMapper.updateScheduleStatus(schedule.getUpgradeScheduleId(), schedule.getUpgradeStatus(), UpgradeScheduleStatusEnum.WAITING.getValue()) > 0), OnpremiseErrorEnum.UPDATE_SCHEDULE_STATUS_FAIL);
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public PageList<UpgradeRecordResponse> listUpgradeRecord(ListUpgradeRecordRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            return new PageList<>(this.upgradeRecordMapper.listUpgradeRecord(request));
        }
        List<UpgradeRecordResponse> responses = this.upgradeRecordMapper.listUpgradeRecord(request);
        return new PageList<>(Paginator.initPaginator(responses), responses);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public PageList<UpgradeScheduleResponse> listUpgradeSchedule(ListUpgradeScheduleRequest request) {
        Paginator paginator = request.getPaginator();
        if (Paginator.checkPaginator(paginator)) {
            PageHelper.startPage(paginator.getPageNum(), paginator.getPageSize());
            List<UpgradeScheduleResponse> responses = this.upgradeScheduleMapper.listUpgradeSchedule(request);
            packaingListScheduleResponse(responses);
            return new PageList<>(responses);
        }
        List<UpgradeScheduleResponse> responses2 = this.upgradeScheduleMapper.listUpgradeSchedule(request);
        packaingListScheduleResponse(responses2);
        return new PageList<>(Paginator.initPaginator(responses2), responses2);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean checkDeviceFailure(String deviceSn) {
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(deviceSn);
        if (device == null) {
            return false;
        }
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public DownloadDeviceLogResponse downloadDeviceLog(DownloadDeviceLogRequest request) throws BeansException {
        DownloadDeviceLogResponse response = new DownloadDeviceLogResponse();
        DeviceLog deviceLog = this.deviceLogMapper.getLatestDeviceLogByDeviceSn(request.getDeviceSn(), request.getOrgId());
        if (deviceLog != null) {
            BeanUtils.copyProperties(deviceLog, response);
        }
        return response;
    }

    private Device requestToDevice(ActiveDeviceRequest request, Long orgId) throws BeansException {
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        if (request.getDeviceGroupId() == null) {
            device.setDeviceGroupId(1L);
        } else {
            device.setDeviceGroupId(request.getDeviceGroupId());
        }
        device.setDeviceStatus(Integer.valueOf(DeviceStatusEnum.ACTIVE_YES.getValue()));
        device.setDeviceVoice(Integer.valueOf(DeviceVoiceEnum.STANDARD_VOICE.getValue()));
        device.setOrgId(orgId);
        device.setDeviceIp(request.getIpAddress());
        device.setOnlineStatus(Integer.valueOf(DeviceOnlineStatusEnum.ONLINE_YES.getValue()));
        device.setDeviceName(StringUtils.isEmpty(request.getDeviceName()) ? defaultDeviceName(device.getDeviceSn(), device.getDeviceType()) : request.getDeviceName());
        device.setGenType(Integer.valueOf(DeviceGenTypeEnum.BIT_34.getValue()));
        device.setIdentifyDistance(Integer.valueOf(DeviceIdentifyDistanceEnum.TEN.getValue()));
        device.setOpeningTime(4);
        device.setIdentifyModule(Integer.valueOf(DeviceIdentifyModuleEnum.MORE_PERSON.getValue()));
        device.setThresholdScore(72);
        device.setFastMode(1);
        device.setLight(70);
        device.setMaskMode(0);
        device.setRingMode(1);
        device.setFontSize(3);
        device.setShowTime(800);
        return device;
    }

    private String defaultDeviceName(String deviceSn, Integer deviceType) {
        return DeviceTypeEnum.getByValue(deviceType.intValue()).getDescription() + "-" + deviceSn.substring(deviceSn.length() - 6);
    }

    private boolean checkActiveParams(ActiveDeviceRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getIpAddress(), OnpremiseErrorEnum.DEVICE_IP_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMacAddress(), OnpremiseErrorEnum.DEVICE_MAC_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getVersion(), OnpremiseErrorEnum.DEVICE_VERSION_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getRomVersion(), OnpremiseErrorEnum.DEVICE_ROM_VERSION_MUST_NOT_NULL);
        return true;
    }

    private void bindDefaultGroup(Device device) {
        Group group = this.groupMapper.getDefaultGroup(device.getOrgId());
        if (group != null) {
            doInsertGroupDevice(device, group.getGroupId());
        }
    }

    private void doInsertGroupDevice(Device device, Long groupId) {
        GroupDevice groupDevice = new GroupDevice();
        groupDevice.setOrgId(device.getOrgId());
        groupDevice.setGroupId(groupId);
        groupDevice.setDeviceSn(device.getDeviceSn());
        int result = this.groupDeviceMapper.insertGroupDevice(groupDevice);
        AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.INSERT_GROUP_DEVICE_FAIL);
        this.groupMapper.updateModifyTime(groupId, device.getOrgId());
    }

    private ActiveDeviceResponse packagingActiveResponse(Device device, AccessKey accessKey) throws BeansException {
        ActiveDeviceResponse response = new ActiveDeviceResponse();
        BeanUtils.copyProperties(device, response);
        String uuid = UUID.randomUUID().toString();
        response.setToken(JwtUtils.token(uuid, uuid, MyDateUtils.addYears(new Date(), 50)));
        response.setOrgCode(CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY));
        QueryConfigResponse one = this.configService.getOneByKey(ConfigConstants.STRANGER_SHOW_REMINDER_INFO_KEY);
        response.setShowReminderInfo(one == null ? "" : one.getConfigValue());
        QueryConfigResponse one2 = this.configService.getOneByKey(ConfigConstants.STRANGER_SPEECH_REMINDER_INFO_KEY);
        response.setSpeechReminderInfo(one2 == null ? "" : one2.getConfigValue());
        QueryConfigResponse one3 = this.configService.getOneByKey(ConfigConstants.STRANGER_FRAME_KEY);
        response.setFrame(Integer.valueOf((one3 == null || StringUtils.isEmpty(one3.getConfigValue())) ? 3 : Integer.valueOf(one3.getConfigValue()).intValue()));
        response.setGenType(Integer.valueOf((device.getGenType() == null || device.getGenType().intValue() == 0) ? DeviceGenTypeEnum.BIT_34.getValue() : device.getGenType().intValue()));
        response.setSnapMode(this.configService.getSnapMode());
        return response;
    }

    private void packagingDeviceGroupBatch(List<GroupDeviceResponse> responses, Long orgId) {
        if (responses != null && responses.size() > 0) {
            for (GroupDeviceResponse response : responses) {
                packagingDeviceGroup(response, orgId);
            }
        }
    }

    private void packagingDeviceGroup(GroupDeviceResponse response, Long orgId) {
        List<GroupDeviceGroupDto> groups = new ArrayList();
        List<Long> groupIds = this.groupDeviceMapper.listGroupIdByDeviceIdNotDelete(response.getDeviceSn(), orgId);
        if (MyListUtils.listIsEmpty(groupIds)) {
            groups = this.groupMapper.listByGroupId(groupIds, orgId);
        }
        response.setGroups(groups);
    }

    private void sendUpgradeMsg(String deviceSn, String path) {
        TerminalDeviceUpgradeRequest upgradeRequest = new TerminalDeviceUpgradeRequest();
        upgradeRequest.setDeviceSn(deviceSn);
        upgradeRequest.setUpgradePackageUrl(this.uplocadConfig.getImageUrlForAbsolutePath(path));
        this.nettyMessageApi.sendMsg(upgradeRequest, Integer.valueOf(TerminalDeviceUpgradeRequest.MODEL_TYPE.type()), deviceSn);
    }

    private void cacheUpgradeStatus(String deviceSn, int status, Long upgradeScheduleId, Integer upgradeType) {
        CacheAdapter.updateUpgradeCacheStatus(deviceSn, upgradeScheduleId, Integer.valueOf(status), upgradeType);
    }

    private void packagingDeviceResponseBatch(Object objects, Long orgId) {
        if (objects instanceof List) {
            List list = (List) objects;
            if (MyListUtils.listIsEmpty(list)) {
                for (Object object : list) {
                    if (object instanceof GroupDeviceResponse) {
                        GroupDeviceResponse response = (GroupDeviceResponse) object;
                        packagingGroupDeviceStatus(response);
                    } else if (object instanceof DeviceResponse) {
                        DeviceResponse response2 = (DeviceResponse) object;
                        packagingDeviceStatus(response2);
                    }
                }
            }
        }
    }

    private void packagingDeviceGroupName(DeviceResponse response, Long orgId) {
        List<Long> groupIds = this.groupDeviceMapper.listGroupIdByDeviceIdNotDelete(response.getDeviceSn(), orgId);
        if (groupIds != null && groupIds.size() > 0) {
            List<String> groupNames = this.groupMapper.listGroupNameByGroupIds(groupIds, orgId);
            if (MyListUtils.listIsEmpty(groupNames)) {
                StringBuffer buffer = new StringBuffer();
                for (String name : groupNames) {
                    buffer.append(name);
                    buffer.append(",");
                }
                response.setGroupName(buffer.substring(0, buffer.length() - 1));
            }
        } else {
            response.setGroupName("");
        }
        response.setGroupIds(groupIds);
    }

    private void packagingGroupDeviceStatus(GroupDeviceResponse response) {
        if (!checkFireWarnStatus(response) && !checkHeartBeatStatus(response) && !checkUpgradeStatus(response)) {
            response.setSign(Integer.valueOf(GroupDeviceStatusEnum.NORMAL_STATUS.getValue()));
        }
    }

    private void packagingDeviceStatus(DeviceResponse response) {
        CacheHeartBeatInfo info = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
        if (info != null && !StringUtils.isEmpty(response.getVersion()) && !response.getVersion().equals(info.getSoftVersion())) {
            response.setUpgradeStatus(Integer.valueOf(GroupDeviceStatusEnum.NEED_UP_STATUS.getValue()));
            return;
        }
        CacheUpgradeStatusInfo upgradeStatusInfo = CacheAdapter.getUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(response.getDeviceSn()));
        if (upgradeStatusInfo != null && upgradeStatusInfo.getExpireTime().longValue() < System.currentTimeMillis() && upgradeStatusInfo.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue()) {
            response.setUpgradeStatus(Integer.valueOf(GroupDeviceStatusEnum.UPDATING_STATUS.getValue()));
        } else {
            response.setUpgradeStatus(Integer.valueOf(GroupDeviceStatusEnum.NORMAL_STATUS.getValue()));
        }
    }

    private boolean checkFireWarnStatus(GroupDeviceResponse response) {
        FireWarnStatusInfo fireWarnStatusInfo = CacheAdapter.getFireWarnStatusInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
        if (fireWarnStatusInfo != null && fireWarnStatusInfo.getStatus().intValue() == FireWarnStatusEnum.FIRE_STATUS.getValue()) {
            response.setSign(Integer.valueOf(GroupDeviceStatusEnum.FIRE_STATUS.getValue()));
            return true;
        }
        return false;
    }

    private boolean checkHeartBeatStatus(GroupDeviceResponse response) {
        CacheHeartBeatInfo heartBeatInfo = CacheAdapter.getHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(response.getDeviceSn()));
        if (heartBeatInfo == null) {
            response.setSign(Integer.valueOf(GroupDeviceStatusEnum.OFFLINE_STATUS.getValue()));
            return true;
        }
        logger.info("====device is {}, heart beat version:{},persistence version {}", response.getDeviceSn(), heartBeatInfo.getSoftVersion(), response.getVersion());
        if (!heartBeatInfo.getSoftVersion().equals(response.getVersion())) {
            response.setSign(Integer.valueOf(GroupDeviceStatusEnum.NEED_UP_STATUS.getValue()));
            return true;
        }
        return false;
    }

    private boolean checkUpgradeStatus(GroupDeviceResponse response) {
        CacheUpgradeStatusInfo upgradeStatusInfo = CacheAdapter.getUpgradeStatusInfo(CacheKeyGenerateUtils.getUpgradeStatusCacheKey(response.getDeviceSn()));
        if (upgradeStatusInfo != null && upgradeStatusInfo.getExpireTime().longValue() < System.currentTimeMillis() && upgradeStatusInfo.getUpgradeStatus().intValue() == DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue()) {
            response.setSign(Integer.valueOf(GroupDeviceStatusEnum.UPDATING_STATUS.getValue()));
            return true;
        }
        return false;
    }

    private boolean checkIsUsed(Date startTime, Date endTime) {
        return (startTime == null || startTime.getTime() <= System.currentTimeMillis()) && (endTime == null || endTime.getTime() >= System.currentTimeMillis());
    }

    private UpgradeDeviceSchedule saveUpgradeSchedule(UploadUpgradePackageResponse response, SaveUpgradeScheduleRequest request, int upgradeDeviceNum) {
        UpgradeDeviceSchedule schedule = new UpgradeDeviceSchedule();
        schedule.setOrgId(request.getOrgId());
        schedule.setUpgradeDeviceNum(Integer.valueOf(upgradeDeviceNum));
        schedule.setUpgradePackageUrl(request.getPackagePath());
        schedule.setUpgradeStatus(Integer.valueOf(UpgradeScheduleStatusEnum.WAITING.getValue()));
        schedule.setUpgradeTime(MyDateUtils.getDate(request.getUpgradeTime().longValue()));
        schedule.setUpgradeType(Integer.valueOf(NumberUtils.toInt(response.getScheduleType())));
        schedule.setUpgradeVersion(response.getVersionCode());
        AssertUtil.isTrue(Boolean.valueOf(this.upgradeScheduleMapper.insert(schedule) > 0), OnpremiseErrorEnum.SAVE_UPGRADE_SCHEDULE_FAIL);
        return schedule;
    }

    private UpgradeDeviceRecord saveUpgradeRecord(String deviceSn, Long scheduleId, Integer status, Integer type, Long orgId) {
        UpgradeDeviceRecord record = new UpgradeDeviceRecord();
        record.setOrgId(orgId);
        record.setDeviceSn(deviceSn);
        record.setUpgradeScheduleId(scheduleId);
        record.setUpgradeStatus(status);
        record.setUpgradeType(type);
        AssertUtil.isTrue(Boolean.valueOf(this.upgradeRecordMapper.insert(record) > 0), OnpremiseErrorEnum.SAVE_UPGRADE_SCHEDULE_FAIL);
        return record;
    }

    private List<String> upgradeOnlineDeviceBatch(UpgradeDeviceSchedule schedule, String packagePath, List<String> deviceList, Integer upgradeType) {
        List<String> onlineDevice = new ArrayList<>();
        Map<String, CacheHeartBeatInfo> map = CacheAdapter.getHeartBeatInfoAll();
        logger.info("===========Upgrade devices :{}", Integer.valueOf(map.size()));
        for (String key : map.keySet()) {
            String deviceSn = CacheKeyGenerateUtils.restoreHeartBeatCacheKey(key);
            logger.info("=======Upgrade device :{}", deviceSn);
            if (deviceList.contains(deviceSn)) {
                onlineDevice.add(deviceSn);
                upgradeOnlineDevice(deviceSn, schedule.getUpgradeScheduleId(), upgradeType, packagePath);
            }
        }
        return onlineDevice;
    }

    private void upgradeOnlineDevice(String deviceSn, Long upgradeScheduleId, Integer upgradeType, String packagePath) {
        sendUpgradeMsg(deviceSn, packagePath);
        cacheUpgradeStatus(deviceSn, DeviceUpgradeStatusEnum.WAITING_STATUS.getValue(), upgradeScheduleId, upgradeType);
    }

    private boolean upgradeOfflineDeviceBatch(Long upgradeScheduleId, List<String> offlineDevice, Integer upgradeType, int oldStatus) {
        logger.info("Upgrade offline devices :{}", JsonUtils.toJson(offlineDevice));
        for (String deviceSn : offlineDevice) {
            upgradeOfflineDevice(upgradeScheduleId, deviceSn, upgradeType, oldStatus);
        }
        return true;
    }

    private void upgradeOfflineDevice(Long upgradeScheduleId, String deviceSn, Integer upgradeType, int oldStatus) {
        logger.info("Upgrade offline device :{}", deviceSn);
        cacheUpgradeStatus(deviceSn, DeviceUpgradeStatusEnum.FAIL_STATUS.getValue(), upgradeScheduleId, upgradeType);
        this.upgradeRecordMapper.updateStatusByDeviceSn(deviceSn, oldStatus, upgradeScheduleId, DeviceUpgradeStatusEnum.FAIL_STATUS.getValue(), Constants.DEVICE_OFF_LINE);
    }

    private void packaingListScheduleResponse(List<UpgradeScheduleResponse> responses) {
        if (MyListUtils.listIsEmpty(responses)) {
            for (UpgradeScheduleResponse response : responses) {
                packaingScheduleResponse(response);
            }
        }
    }

    private String packaingScheduleResponse(UpgradeScheduleResponse response) {
        StringBuffer buffer = new StringBuffer();
        boolean isAll = packagingUpgradeResultByStatus(buffer, response, DeviceUpgradeStatusEnum.CANCEL_STATUS);
        if (!isAll) {
            boolean isAll2 = packagingUpgradeResultByStatus(buffer, response, DeviceUpgradeStatusEnum.WAITING_STATUS);
            if (!isAll2) {
                boolean isAll3 = packagingUpgradeResultByStatus(buffer, response, DeviceUpgradeStatusEnum.SUCCESS_STATUS);
                if (!isAll3) {
                    updateUpgradeStatusForFail(response.getUpgradeScheduleId());
                    boolean isAll4 = packagingUpgradeResultByStatus(buffer, response, DeviceUpgradeStatusEnum.UPDATING_STATUS);
                    if (!isAll4) {
                        packagingUpgradeResultByStatus(buffer, response, DeviceUpgradeStatusEnum.FAIL_STATUS);
                    }
                }
            }
        }
        response.setUpgradeResult(buffer.toString());
        return buffer.toString();
    }

    private boolean packagingUpgradeResultByStatus(StringBuffer buffer, UpgradeScheduleResponse response, DeviceUpgradeStatusEnum statusEnum) {
        int num = this.upgradeRecordMapper.countDeviceByStatus(response.getUpgradeScheduleId(), Integer.valueOf(statusEnum.getValue()));
        boolean flag = false;
        if (num != 0) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            if (num == response.getUpgradeDeviceNum().intValue()) {
                buffer.append(Constants.DEVICE_ALL);
                buffer.append(statusEnum.getDescription());
                flag = true;
            } else {
                buffer.append(num);
                buffer.append(Constants.DEVICE_UNIT);
                buffer.append(statusEnum.getDescription());
            }
        }
        return flag;
    }

    private Device checkSaveDeviceConfigParams(String deviceSn, String deviceName) {
        Device device = this.deviceMapper.getDeviceInfoByDeviceName(deviceName);
        AssertUtil.isTrue(Boolean.valueOf(device == null || device.getDeviceSn().equals(deviceSn)), OnpremiseErrorEnum.DEVICE_NAME_ALREADY_EXIST);
        Device device2 = this.deviceMapper.getDeviceInfoByDeviceSn(deviceSn);
        AssertUtil.isNullOrEmpty(device2, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        return device2;
    }

    private String timeToCron(Long time) {
        StringBuffer buffer = new StringBuffer();
        Date date = MyDateUtils.getDate(time.longValue());
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            buffer.append(calendar.get(13));
            buffer.append(SymbolConstants.SPACE_SYMBOL);
            buffer.append(calendar.get(12));
            buffer.append(SymbolConstants.SPACE_SYMBOL);
            buffer.append(calendar.get(11));
            buffer.append(SymbolConstants.SPACE_SYMBOL);
            buffer.append(calendar.get(5));
            buffer.append(SymbolConstants.SPACE_SYMBOL);
            buffer.append(calendar.get(2) + 1);
            buffer.append(SymbolConstants.SPACE_SYMBOL);
            buffer.append("?");
        }
        AssertUtil.isTrue(Boolean.valueOf(buffer.toString().trim().length() > 0), OnpremiseErrorEnum.UPGRADE_TIME_FORMAT_ERROR);
        return buffer.toString();
    }

    private List<String> passLowerVersionDevice(Integer upgradeType, String version, List<String> deviceList) {
        logger.info("pass start list size :{}", Integer.valueOf(deviceList.size()));
        Map<String, CacheHeartBeatInfo> map = CacheAdapter.getHeartBeatInfoAll();
        for (String key : map.keySet()) {
            String deviceSn = CacheKeyGenerateUtils.restoreHeartBeatCacheKey(key);
            doPassLowerVersionDevice(upgradeType, version, deviceList, deviceSn, map.get(key));
        }
        logger.info("pass end list size :{}", Integer.valueOf(deviceList.size()));
        return deviceList;
    }

    private List<String> doPassLowerVersionDevice(Integer upgradeType, String version, List<String> totalDevice, String deviceSn, CacheHeartBeatInfo info) {
        logger.info("pass device sn :{}", deviceSn);
        if (totalDevice.contains(deviceSn)) {
            if (UpgradeScheduleTypeEnum.APK.getValue() == upgradeType.intValue()) {
                logger.info("apk version :{},info version :{}", version, info.getSoftVersion());
                if (VersionUtils.compareVersion(version, info.getSoftVersion()) <= 0) {
                    logger.info("remove device :{} from upgrade list ", deviceSn);
                    totalDevice.remove(deviceSn);
                }
            } else {
                logger.info("rom version :{},info version :{}", version, info.getRomVersion());
                if (VersionUtils.compareVersion(version, info.getRomVersion()) <= 0) {
                    logger.info("remove device :{} from upgrade list ", deviceSn);
                    totalDevice.remove(deviceSn);
                }
            }
        }
        return totalDevice;
    }

    private void updateUpgradeStatusForFail(Long upgradeScheduleId) {
        List<String> deviceSns = this.upgradeRecordMapper.getDeviceSnByScheduleId(upgradeScheduleId, DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue());
        for (String deviceSn : deviceSns) {
            String key = CacheKeyGenerateUtils.getUpgradeStatusCacheKey(deviceSn);
            CacheUpgradeStatusInfo info = CacheAdapter.getUpgradeStatusInfo(key);
            logger.info("cache upgrade status info :{}", JsonUtils.toJson(info));
            if (info != null && info.getExpireTime().longValue() < System.currentTimeMillis() && DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue() == info.getUpgradeStatus().intValue()) {
                this.upgradeRecordMapper.updateStatusByDeviceSn(deviceSn, DeviceUpgradeStatusEnum.UPDATING_STATUS.getValue(), upgradeScheduleId, DeviceUpgradeStatusEnum.FAIL_STATUS.getValue(), "");
                CacheAdapter.removeUpgradeStatusInfo(key);
            }
        }
    }

    private void doDeleteGroupMember(Long groupId, Long orgId) {
        Group group = this.groupMapper.getOneById(groupId, orgId);
        if (group != null && group.getAllMemberFlag().intValue() == 1) {
            MemberListRequest memberListRequest = new MemberListRequest();
            memberListRequest.setOrgId(orgId);
            List<MemberListResponse> memberListResponses = this.memberMapper.memberList(memberListRequest);
            List<GroupMember> insert = new ArrayList<>();
            List<Long> delete = new ArrayList<>();
            Long startTime = Long.valueOf(System.currentTimeMillis());
            logger.info("start time :{}", startTime);
            Map<Long, GroupMember> maps = listToMap(this.groupMemberMapper.listByMemberAndGroup(memberListResponses, groupId, orgId));
            for (MemberListResponse response : memberListResponses) {
                if (maps.get(response.getMemberId()) == null) {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setGroupId(groupId);
                    groupMember.setOrgId(orgId);
                    groupMember.setMemberId(response.getMemberId());
                    groupMember.setDeleteOrNot(Integer.valueOf(DeleteOrNotEnum.DELETE_YES.getValue()));
                    groupMember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                    insert.add(groupMember);
                } else {
                    delete.add(response.getMemberId());
                }
            }
            logger.info("foreach end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            if (insert.size() > 0) {
                this.groupMemberMapper.batchInsert(insert);
            }
            logger.info("insert end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            if (delete.size() > 0) {
                this.groupMemberMapper.batchDeleteGroupMember(groupId, delete, orgId);
            }
            logger.info("delete end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            return;
        }
        this.groupMemberMapper.updateModifyTime(groupId, null, orgId);
    }

    private void doInsertGroupMember(Long groupId, Long orgId) {
        Group group = this.groupMapper.getOneById(groupId, orgId);
        if (group != null && group.getAllMemberFlag().intValue() == 1) {
            MemberListRequest memberListRequest = new MemberListRequest();
            memberListRequest.setOrgId(orgId);
            List<MemberListResponse> memberListResponses = this.memberMapper.memberList(memberListRequest);
            List<GroupMember> insert = new ArrayList<>();
            List<Long> modify = new ArrayList<>();
            Long startTime = Long.valueOf(System.currentTimeMillis());
            logger.info("start time :{}", startTime);
            Map<Long, GroupMember> maps = listToMap(this.groupMemberMapper.listByMemberAndGroup(memberListResponses, groupId, orgId));
            for (MemberListResponse response : memberListResponses) {
                if (maps.get(response.getMemberId()) == null) {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setGroupId(groupId);
                    groupMember.setOrgId(orgId);
                    groupMember.setMemberId(response.getMemberId());
                    groupMember.setDeleteOrNot(Integer.valueOf(DeleteOrNotEnum.DELETE_NO.getValue()));
                    groupMember.setType(Integer.valueOf(MemberTypeEnum.MEMBER.getValue()));
                    insert.add(groupMember);
                } else {
                    modify.add(response.getMemberId());
                }
            }
            logger.info("foreach end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            if (insert.size() > 0) {
                this.groupMemberMapper.batchInsert(insert);
            }
            logger.info("insert end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            if (modify.size() > 0) {
                this.groupMemberMapper.batchUpdateModifyTime(groupId, modify, orgId);
            }
            logger.info("modify end  time :{}", Long.valueOf(System.currentTimeMillis() - startTime.longValue()));
            return;
        }
        this.groupMemberMapper.updateModifyTime(groupId, null, orgId);
    }

    private Map<Long, GroupMember> listToMap(List<GroupMember> groupMembers) {
        Map<Long, GroupMember> map = new HashMap<>();
        if (groupMembers != null && groupMembers.size() > 0) {
            for (GroupMember groupMember : groupMembers) {
                if (groupMember != null && groupMember.getMemberId() != null) {
                    map.put(groupMember.getMemberId(), groupMember);
                }
            }
        }
        return map;
    }

    private void doDeleteDevice(Device device) {
        this.groupDeviceMapper.deleteGroupDevice(null, device.getDeviceSn(), device.getOrgId());
        this.attendanceGroupDeviceMapper.deleteGroupDevice(null, device.getDeviceSn(), device.getOrgId());
        this.checkInService.notify(device.getOrgId(), device.getDeviceId(), Integer.valueOf(CheckInTaskNotifyTypeEnum.DEVICE.getValue()));
        this.mealCanteenService.notify(device.getOrgId(), device.getDeviceId(), Integer.valueOf(MealCanteenNotifyTypeEnum.DEVICE.getValue()));
        CacheAdapter.removeHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(device.getDeviceSn()));
        DeviceUnbindRequest unbindRequest = new DeviceUnbindRequest();
        unbindRequest.setDeviceSn(device.getDeviceSn());
        this.nettyMessageApi.sendMsg(unbindRequest, Integer.valueOf(DeviceUnbindRequest.MODEL_TYPE.type()), device.getDeviceSn());
        DeviceCallbackRequest request = new DeviceCallbackRequest();
        request.setOrgId(device.getOrgId());
        request.setDeviceSn(device.getDeviceSn());
        request.setDeviceName(device.getDeviceName());
        request.setEvent(Integer.valueOf(DeviceStatusEventEnum.UNBIND.getStatus()));
        request.setTimestamp(Long.valueOf(System.currentTimeMillis()));
        sendCallback(request);
    }

    private boolean isNeedSendNetty(Device device, SaveDeviceConfigRequest request) {
        boolean needSendNetty = false;
        if (!device.getDeviceName().equals(request.getDeviceName()) || !device.getDeviceVoice().equals(request.getDeviceVoice()) || device.getOpeningTime().intValue() != request.getOpeningTime().intValue() || device.getIdentifyDistance().intValue() != request.getIdentifyDistance().intValue()) {
            needSendNetty = true;
        } else if (request.getGenType() != null && request.getGenType().intValue() != 0) {
            needSendNetty = true;
        } else if (request.getIdentifyModule() != null && request.getIdentifyModule().intValue() != 0) {
            needSendNetty = true;
        } else if (request.getThresholdScore() != null && request.getThresholdScore().intValue() != 0) {
            needSendNetty = true;
        } else if (request.getFastMode() != null && request.getFastMode().intValue() != 0) {
            needSendNetty = true;
        }
        return needSendNetty;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.moredian.onpremise.device.service.impl.DeviceServiceImpl$2] */
    @Override // com.moredian.onpremise.api.device.DeviceService
    public void sendCallback(final DeviceCallbackRequest request) {
        new Thread() { // from class: com.moredian.onpremise.device.service.impl.DeviceServiceImpl.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String[] urls;
                try {
                    CallbackServer callbackServer = DeviceServiceImpl.this.callbackServerMapper.getOneByTag(request.getOrgId(), CallbackTagEnum.DEVICE_STATUS_EVENT.getTag());
                    if (callbackServer != null && (urls = callbackServer.getCallbackUrl().split(",")) != null && urls.length > 0) {
                        for (String url : urls) {
                            HttpUtils.sendURLPost(url, JsonUtils.toJson(request));
                        }
                    }
                } catch (Exception e) {
                    DeviceServiceImpl.logger.error("error:{}", (Throwable) e);
                }
            }
        }.start();
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean restart(DeviceRestartRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        List<String> manageDeviceSn = UserLoginResponse.getAccountManageDeviceSn(request.getSessionId());
        if (MyListUtils.listIsEmpty(manageDeviceSn)) {
            AssertUtil.isTrue(Boolean.valueOf(manageDeviceSn.contains(request.getDeviceSn())), OnpremiseErrorEnum.DEVICE_NOT_FIND);
        }
        Device info = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(info, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        this.nettyMessageApi.sendMsg(new TerminalRestartRequest(), Integer.valueOf(TerminalRestartRequest.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean resetData(DeviceResetDataRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        List<String> manageDeviceSn = UserLoginResponse.getAccountManageDeviceSn(request.getSessionId());
        if (MyListUtils.listIsEmpty(manageDeviceSn)) {
            AssertUtil.isTrue(Boolean.valueOf(manageDeviceSn.contains(request.getDeviceSn())), OnpremiseErrorEnum.DEVICE_NOT_FIND);
        }
        Device info = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(info, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        this.nettyMessageApi.sendMsg(new TerminalResetDataRequest(), Integer.valueOf(TerminalResetDataRequest.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean pushShowData(DeviceShowDataRequest request) {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        Device info = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        AssertUtil.isNullOrEmpty(info, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        this.nettyMessageApi.sendMsg(new TerminalShowDataRequest(request.getTipsText(), request.getTipsSpeech(), request.getTime()), Integer.valueOf(TerminalShowDataRequest.MODEL_TYPE.type()), request.getDeviceSn());
        return true;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<String> listDeviceModel(Long orgId) {
        return this.deviceMapper.listDeviceModel(orgId);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<Integer> listDeviceType(Long orgId) {
        return this.deviceMapper.listDeviceType(orgId);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public String pushDeviceMsg(DevicePushMsgRequest request) {
        checkPushDeviceMsgParams(request);
        String uuid = UUID.randomUUID().toString();
        request.setUuid(uuid);
        if (request.getAllDevice().equals(1)) {
            List<String> deviceSnList = this.deviceMapper.listDeviceSn(request.getOrgId());
            AssertUtil.isNullOrEmpty(deviceSnList, OnpremiseErrorEnum.PUSH_MSG_DEVICE_MUST_NOT_NULL);
            request.setDeviceSns(deviceSnList);
        }
        this.nettyMessageApi.sendMsg(new TerminalPushMsgRequest(request.getType(), request.getMsg(), uuid), Integer.valueOf(TerminalPushMsgRequest.MODEL_TYPE.type()), request.getDeviceSns());
        List<DeviceMsgLog> deviceMsgLogs = new ArrayList<>();
        for (String deviceSn : request.getDeviceSns()) {
            DeviceMsgLog deviceMsgLog = new DeviceMsgLog();
            deviceMsgLog.setOrgId(request.getOrgId());
            deviceMsgLog.setUuid(request.getUuid());
            deviceMsgLog.setType(request.getType());
            deviceMsgLog.setDeviceSn(deviceSn);
            deviceMsgLog.setMsg(request.getMsg());
            deviceMsgLog.setStatus(1);
            deviceMsgLogs.add(deviceMsgLog);
        }
        this.deviceMsgLogMapper.insertBatch(deviceMsgLogs);
        return uuid;
    }

    private void checkPushDeviceMsgParams(DevicePushMsgRequest request) {
        if (request.getAllDevice().equals(0)) {
            AssertUtil.isNullOrEmpty(request.getAllDevice(), OnpremiseErrorEnum.PUSH_MSG_DEVICE_MUST_NOT_NULL);
        }
        AssertUtil.isNullOrEmpty(request.getType(), OnpremiseErrorEnum.PUSH_MSG_TYPE_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getMsg(), OnpremiseErrorEnum.PUSH_MSG_MUST_NOT_NULL);
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public List<DevicePushMsgResponse> queryPushDeviceMsgResult(DevicePushMsgQueryRequest request) throws BeansException {
        List<DeviceMsgLog> deviceMsgLogs = this.deviceMsgLogMapper.getListByUuid(request.getOrgId(), request.getUuid());
        List<DevicePushMsgResponse> responses = new ArrayList<>();
        if (CollectionUtils.isEmpty(deviceMsgLogs)) {
            return responses;
        }
        for (DeviceMsgLog deviceMsgLog : deviceMsgLogs) {
            DevicePushMsgResponse devicePushMsgResponse = new DevicePushMsgResponse();
            BeanUtils.copyProperties(deviceMsgLog, devicePushMsgResponse);
            responses.add(devicePushMsgResponse);
        }
        return responses;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    @Transactional(rollbackFor = {RuntimeException.class})
    public synchronized PreActiveDeviceResponse preActiveDevice(PreActiveDeviceRequest request) throws BeansException {
        AssertUtil.isNullOrEmpty(request.getDeviceSn(), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeviceType(), OnpremiseErrorEnum.DEVICE_TYPE_MUST_NOT_NULL);
        Organization organization = this.orgMapper.getOne();
        AssertUtil.isNullOrEmpty(organization, OnpremiseErrorEnum.ORG_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(request.getDeviceSn());
        if (device == null) {
            int deviceNum = this.deviceMapper.countDevice(organization.getOrgId());
            int deviceAllowMax = this.authCodeMapper.countAllowDeviceNum(organization.getOrgId());
            logger.info("===========deviceNum :{} , deviceAllowMax {}", Integer.valueOf(deviceNum), Integer.valueOf(deviceAllowMax));
            AssertUtil.isTrue(Boolean.valueOf(deviceAllowMax > deviceNum), OnpremiseErrorEnum.DEVICE_EXCEED_MAX_ALLOW);
            ActiveDeviceRequest activeDeviceRequest = new ActiveDeviceRequest();
            BeanUtils.copyProperties(request, activeDeviceRequest);
            Long deviceGroupId = parseDeviceGroupId(request.getOrgId(), request.getFullDeviceGroupName());
            activeDeviceRequest.setDeviceGroupId(deviceGroupId);
            device = requestToDevice(activeDeviceRequest, organization.getOrgId());
            int result = this.deviceMapper.insertDevice(device);
            AssertUtil.isTrue(Boolean.valueOf(result > 0), OnpremiseErrorEnum.INSERT_DEVICE_FAIL);
            DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getByValue(request.getDeviceType().intValue());
            switch (deviceTypeEnum) {
                case ENTRANCE_GUARD:
                    bindDefaultGroup(device);
                    break;
                case ENTRANCE_LIFT:
                    bindDefaultGroup(device);
                    break;
                case ENTRANCE_THERMOMETER_A:
                    bindDefaultGroup(device);
                    break;
                case ENTRANCE_THERMOMETER_B:
                    bindDefaultGroup(device);
                    break;
            }
        }
        try {
            String key = CacheKeyGenerateUtils.getHeartBeatCacheKey(device.getDeviceSn());
            if (CacheAdapter.getHeartBeatInfo(key) != null) {
                CacheAdapter.removeHeartBeatInfo(key);
            }
            CacheHeartBeatInfo heartBeatInfo = new CacheHeartBeatInfo();
            heartBeatInfo.setIpAddress(device.getDeviceIp());
            heartBeatInfo.setMacAddress(device.getMacAddress());
            heartBeatInfo.setDeviceSn(device.getDeviceSn());
            heartBeatInfo.setSoftVersion(device.getVersion());
            heartBeatInfo.setRomVersion(device.getRomVersion());
            heartBeatInfo.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 35).getTime()));
            CacheAdapter.cacheHeartBeatInfo(key, heartBeatInfo);
        } catch (Exception e) {
        }
        DeviceCallbackRequest deviceCallbackRequest = new DeviceCallbackRequest();
        deviceCallbackRequest.setOrgId(device.getOrgId());
        deviceCallbackRequest.setDeviceSn(device.getDeviceSn());
        deviceCallbackRequest.setDeviceName(device.getDeviceName());
        deviceCallbackRequest.setEvent(Integer.valueOf(DeviceStatusEventEnum.ACTIVE.getStatus()));
        deviceCallbackRequest.setTimestamp(Long.valueOf(System.currentTimeMillis()));
        sendCallback(deviceCallbackRequest);
        Account account = this.accountMapper.getAccountInfo(request.getLoginAccountId(), request.getOrgId());
        if (AccountGradeEnum.SUPER_ACCOUNT.getValue() != account.getAccountGrade().intValue() && AccountGradeEnum.MAIN_ACCOUNT.getValue() != account.getAccountGrade().intValue()) {
            AccountAuth accountAuth = this.accountAuthMapper.getOneByAccountId(request.getLoginAccountId(), request.getOrgId());
            String tempManageDeviceSn = accountAuth.getManageDeviceSn();
            if (StringUtils.isEmpty(tempManageDeviceSn)) {
                accountAuth.setManageDeviceSn(request.getDeviceSn());
            } else {
                accountAuth.setManageDeviceSn(tempManageDeviceSn + "," + request.getDeviceSn());
            }
            this.accountAuthMapper.updateAccountAuth(accountAuth);
        }
        return new PreActiveDeviceResponse(device.getDeviceName(), device.getDeviceSn(), 1, "");
    }

    private Long parseDeviceGroupId(Long orgId, String fullDeviceGroupName) {
        String[] deviceGroupNames = fullDeviceGroupName.split("-");
        int length = deviceGroupNames.length;
        Long superId = 1L;
        if (length == 0) {
            return 1;
        }
        for (int i = 1; i < length; i++) {
            DeviceGroup deviceGroup = this.deviceGroupMapper.getOneByNameAndSuperId(orgId, deviceGroupNames[i], superId);
            if (deviceGroup == null) {
                deviceGroup = new DeviceGroup();
                deviceGroup.setOrgId(orgId);
                deviceGroup.setName(deviceGroupNames[i]);
                deviceGroup.setSuperId(superId);
                this.deviceGroupMapper.insertOne(deviceGroup);
            }
            superId = deviceGroup.getId();
        }
        return superId;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public DeviceConfigResponse getDeviceConfig(String deviceSn, Long orgId) throws BeansException {
        AssertUtil.isNullOrEmpty(deviceSn, OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        Device device = this.deviceMapper.getDeviceInfoByDeviceSn(deviceSn);
        AssertUtil.isNullOrEmpty(device, OnpremiseErrorEnum.DEVICE_NOT_FIND);
        DeviceConfigResponse response = new DeviceConfigResponse();
        BeanUtils.copyProperties(device, response);
        return response;
    }

    @Override // com.moredian.onpremise.api.device.DeviceService
    public boolean bindDeviceGroup(BindDeviceGroupRequest request) {
        AssertUtil.isNullOrEmpty(request.getOrgId(), OnpremiseErrorEnum.ORG_ID_MUST_NOT_NULL);
        AssertUtil.isTrue(Boolean.valueOf(!CollectionUtils.isEmpty(request.getDeviceSns())), OnpremiseErrorEnum.DEVICE_SN_MUST_NOT_NULL);
        AssertUtil.isNullOrEmpty(request.getDeviceGroupId(), OnpremiseErrorEnum.DEVICE_GROUP_ID_MUST_NOT_NULL);
        this.deviceMapper.updateDeviceGroup(request.getOrgId(), request.getDeviceSns(), request.getDeviceGroupId());
        return true;
    }

    private void getChildDeviceGroupIds(List<Long> childDeviceGroupIds, Long deviceGroupId, Long orgId) {
        List<DeviceGroup> deviceGroups = this.deviceGroupMapper.getChildList(deviceGroupId, orgId);
        if (CollectionUtils.isEmpty(deviceGroups)) {
            return;
        }
        for (DeviceGroup deviceGroup : deviceGroups) {
            childDeviceGroupIds.add(deviceGroup.getId());
            getChildDeviceGroupIds(childDeviceGroupIds, deviceGroup.getId(), orgId);
        }
    }

    private List<String> getManagerDevices(BaseRequest request) {
        List<String> deviceSns = UserLoginResponse.getAccountManageDeviceSn(request.getSessionId());
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(request.getSessionId());
        if (loginInfo != null) {
            if (loginInfo.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.CHILDREN_ACCOUNT.getValue()))) {
                Account account = this.accountMapper.getAccountInfo(loginInfo.getAccountId(), loginInfo.getOrgId());
                this.accountAuthMapper.getOneByAccountId(loginInfo.getAccountId(), loginInfo.getOrgId());
                if (account.getModuleManager().equals(1) && loginInfo.getManageAppId().contains("12345678913")) {
                    List<DeviceResponse> deviceTempList = this.deviceMapper.listDevice(request.getOrgId(), null, null, null, null, null, null, null);
                    if (!CollectionUtils.isEmpty(deviceTempList)) {
                        deviceSns = new ArrayList(deviceSns);
                        for (DeviceResponse deviceTemp : deviceTempList) {
                            switch (deviceTemp.getDeviceType().intValue()) {
                                case 2:
                                case 5:
                                case 6:
                                    if (deviceSns == null) {
                                        deviceSns = new ArrayList();
                                        deviceSns.add(deviceTemp.getDeviceSn());
                                        break;
                                    } else if (deviceSns.contains(deviceTemp.getDeviceSn())) {
                                        break;
                                    } else {
                                        deviceSns.add(deviceTemp.getDeviceSn());
                                        break;
                                    }
                            }
                        }
                    }
                }
            } else if (loginInfo.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.SUPER_ACCOUNT.getValue())) || loginInfo.getAccountGrade().equals(Integer.valueOf(AccountGradeEnum.MAIN_ACCOUNT.getValue()))) {
                deviceSns = this.deviceMapper.listDeviceSn(request.getOrgId());
            }
        }
        return deviceSns;
    }
}
