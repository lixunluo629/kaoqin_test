package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.model.domain.MemberAuthInfo;
import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/MemberAuthInfoMapper.class */
public interface MemberAuthInfoMapper {
    int insert(MemberAuthInfo memberAuthInfo);

    int updateByMemberId(MemberAuthInfo memberAuthInfo);

    int updateByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("permissionsGroupId") Long l3, @Param("checkInGroupId") Long l4, @Param("attendanceGroupId") Long l5, @Param("deviceSns") List<String> list, @Param("type") Integer num);

    int updateBatchByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("permissionsGroupIds") List<Long> list, @Param("checkInGroupIds") List<Long> list2, @Param("attendanceGroupIds") List<Long> list3, @Param("deviceSns") List<String> list4, @Param("type") Integer num);

    int updatePermissionsByMemberId(@Param("permissionsGroupId") Long l, @Param("memberId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int updatePermissionsByMemberIds(@Param("permissionsGroupId") Long l, @Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l2, @Param("type") Integer num);

    int updatePermissionsByDeptId(@Param("permissionsGroupId") Long l, @Param("deptId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int updatePermissionsByPermissionsGroupId(@Param("permissionsGroupId") Long l, @Param("orgId") Long l2);

    int updateAttendanceByMemberId(@Param("attendanceGroupId") Long l, @Param("memberId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int updateAttendanceByMemberIds(@Param("attendanceGroupId") Long l, @Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l2, @Param("type") Integer num);

    int updateAttendanceByDeptId(@Param("attendanceGroupId") Long l, @Param("deptId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int updateAttendanceByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int updateCheckInByMemberId(@Param("checkInGroupId") Long l, @Param("memberId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int updateCheckInByDeptId(@Param("checkInGroupId") Long l, @Param("deptId") Long l2, @Param("orgId") Long l3, @Param("type") Integer num);

    int deleteByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    int deleteByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    MemberAuthInfo getOneByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    int updateDeviceSn(@Param("orgId") Long l, @Param("date") String str, @Param("deviceSn") String str2);

    int updateDeviceSnByDeptId(@Param("deviceSn") String str, @Param("deptId") Long l, @Param("orgId") Long l2, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) int i, @Param("type") int i2);

    int updateDeviceSnByMemberId(@Param("deviceSn") String str, @Param("memberId") Long l, @Param("orgId") Long l2, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) int i, @Param("type") int i2);

    int updateDeviceSnByMemberIds(@Param("deviceSn") String str, @Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) int i, @Param("type") int i2, @Param("lastSyncDeviceFlag") int i3);

    int updateDeviceSnByGroupId(@Param("groupId") Long l, @Param("orgId") Long l2, @Param("deviceSn") String str, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) int i, @Param("type") int i2, @Param("lastSyncDeviceFlag") int i3);

    int updateDeviceSnByGroupIds(@Param("groupIds") List<Long> list, @Param("orgId") Long l, @Param("deviceSn") String str, @Param(AuthConstants.AUTH_PARAM_APP_TYPE_KEY) int i, @Param("type") int i2);

    List<MemberAuthInfo> listByMemberIds(@Param("deleteMemberIds") List<Long> list, @Param("orgId") Long l);

    List<Long> listByGroupIds(@Param("permissionsGroupIds") List<GroupDeviceGroupDto> list, @Param("orgId") Long l);

    int deleteDeviceSnByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);
}
