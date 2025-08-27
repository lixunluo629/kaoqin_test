package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceGroupDevice;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceGroupDeviceMapper.class */
public interface AttendanceGroupDeviceMapper {
    int insertAttendanceGroupDevice(AttendanceGroupDevice attendanceGroupDevice);

    List<String> listDeviceSnByTypeAndGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2, @Param("deviceType") Integer num);

    List<String> listDeviceSnByTypeAndGroupIds(@Param("attendanceGroupIds") List<Long> list, @Param("orgId") Long l, @Param("deviceType") Integer num);

    int deleteByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int deleteByAttendanceGroupIdAndDeviceSn(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2, @Param("deviceType") Integer num, @Param("deviceSn") String str);

    List<Long> listGroupIdByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);

    List<AttendanceGroupDevice> getByDeviceAndGroup(@Param("deviceSn") String str, @Param("orgId") Long l, @Param("attendanceGroupId") Long l2);

    List<String> listDeviceSnByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l);

    List<AttendanceGroupDevice> getByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);

    int deleteGroupDevice(@Param("attendanceGroupId") Long l, @Param("deviceSn") String str, @Param("orgId") Long l2);
}
