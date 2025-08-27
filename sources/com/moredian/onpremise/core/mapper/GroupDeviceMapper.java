package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.GroupDevice;
import com.moredian.onpremise.core.model.dto.DeviceDto;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/GroupDeviceMapper.class */
public interface GroupDeviceMapper {
    List<DeviceDto> listDeviceByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<DeviceDto> listDeviceByGroupIdNotDelete(@Param("groupId") Long l, @Param("orgId") Long l2);

    int countDeviceByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    int insertGroupDevice(GroupDevice groupDevice);

    int deleteByGroup(@Param("groupId") Long l, @Param("orgId") Long l2);

    int deleteGroupDevice(@Param("groupId") Long l, @Param("deviceSn") String str, @Param("orgId") Long l2);

    List<GroupDevice> getByDeviceId(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<Long> listGroupIdByDeviceId(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<GroupDevice> listGroupByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<Long> listGroupIdByDeviceIdNotDelete(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<GroupDeviceGroupDto> listByDeviceId(@Param("deviceSn") String str, @Param("orgId") Long l);

    GroupDevice getOneByDeviceAndGroup(@Param("deviceSn") String str, @Param("orgId") Long l, @Param("groupId") Long l2);

    List<String> listDeviceSnByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2);

    List<String> listDeviceSnByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    List<String> listDeviceSnByStringGroupIds(@Param("groupIds") List<String> list, @Param("orgId") Long l);
}
