package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import com.moredian.onpremise.core.model.domain.Device;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.request.SaveDeviceConfigRequest;
import com.moredian.onpremise.core.model.request.UpdateDeviceRequest;
import com.moredian.onpremise.core.model.response.DeviceResponse;
import com.moredian.onpremise.core.model.response.GroupDeviceResponse;
import com.moredian.onpremise.core.model.response.ListSearchDeviceResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/DeviceMapper.class */
public interface DeviceMapper {
    List<DeviceDto> listByDeviceIds(List<Long> list);

    int deleteDevice(@Param("deviceId") Long l, @Param("orgId") Long l2);

    int deleteDeviceByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);

    Device getDeviceDetail(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<GroupDeviceResponse> groupDeviceList(@Param("orgId") Long l, @Param("deviceTypes") List<Integer> list, @Param("deviceName") String str, @Param("deviceSns") List<String> list2, @Param("deviceModel") String str2, @Param("keywords") String str3, @Param("deviceGroupIds") List<Long> list3);

    List<DeviceResponse> listDevice(@Param("orgId") Long l, @Param("deviceType") Integer num, @Param("deviceName") String str, @Param("deviceSn") String str2, @Param("keywords") String str3, @Param("deviceModel") String str4, @Param("deviceSns") List<String> list, @Param("deviceGroupIds") List<Long> list2);

    List<DeviceResponse> listDeviceV2(@Param("orgId") Long l, @Param("deviceType") Integer num, @Param("deviceName") String str, @Param("deviceSn") String str2, @Param("keywords") String str3, @Param("onlineStatus") Integer num2, @Param("deviceModel") String str4, @Param("groupName") String str5, @Param("deviceSns") List<String> list, @Param("deviceGroupIds") List<Long> list2);

    List<ListSearchDeviceResponse> listSearchDevice(@Param("orgId") Long l, @Param("deviceSns") List<String> list, @Param("deviceName") String str, @Param("deviceSn") String str2, @Param("deviceType") Integer num, @Param("deviceModel") String str3, @Param("keywords") String str4, @Param("deviceGroupIds") List<Long> list2);

    Device getDeviceInfoByDeviceSn(@Param("deviceSn") String str);

    Device getDeviceInfoByDeviceId(@Param("deviceId") Long l, @Param("orgId") Long l2);

    int insertDevice(Device device);

    int saveDeviceConfig(SaveDeviceConfigRequest saveDeviceConfigRequest);

    Device getDeviceInfoByDeviceName(@Param("deviceName") String str);

    int countDevice(@Param("orgId") Long l);

    List<String> getDeviceSnByDeviceModel(@Param(UpgradeConstants.UPGRADE_PARAM_SUPPORT_DEVICE_KEY) String[] strArr, @Param("orgId") Long l);

    int updateDevice(UpdateDeviceRequest updateDeviceRequest);

    List<DeviceDto> listDeviceDtoByDeviceSn(@Param("deviceSns") List<String> list, @Param("orgId") Long l);

    List<Device> listDeviceByDeviceSns(@Param("orgId") Long l, @Param("deviceSns") List<String> list);

    List<ListSearchDeviceResponse> listUpgradeDevice(@Param(UpgradeConstants.UPGRADE_PARAM_SUPPORT_DEVICE_KEY) String[] strArr, @Param("orgId") Long l, @Param("deviceSns") List<String> list, @Param("searchKey") String str, @Param("deviceType") Integer num, @Param("deviceGroupId") Long l2);

    int disableOnlineVerify(@Param("deviceSn") String str);

    int enableOnlineVerify(@Param("deviceSn") String str, @Param("onlineVerifyUrl") String str2);

    List<String> listDeviceModel(@Param("orgId") Long l);

    List<Integer> listDeviceType(@Param("orgId") Long l);

    List<String> listDeviceSn(@Param("orgId") Long l);

    int updateOnlineStatusBatch(@Param("orgId") Long l, @Param("onlineStatus") Integer num, @Param("deviceSns") List<String> list);

    int updateDeviceGroup(@Param("orgId") Long l, @Param("deviceSns") List<String> list, @Param("deviceGroupId") Long l2);

    List<Device> listDeviceByDeviceGroupId(@Param("orgId") Long l, @Param("deviceGroupId") Long l2);
}
