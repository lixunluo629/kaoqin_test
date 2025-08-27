package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceGroupMember;
import com.moredian.onpremise.core.model.dto.GroupMemberDto;
import com.moredian.onpremise.core.model.info.AttendanceGroupMemberInfo;
import com.moredian.onpremise.core.model.request.CountInfoForMonthRequest;
import com.moredian.onpremise.core.model.response.CountInfoForMonthResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceGroupMemberMapper.class */
public interface AttendanceGroupMemberMapper {
    int deleteByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int countByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    List<GroupMemberDto> listByAttendanceGroupIdAndType(@Param("type") Integer num, @Param("orgId") Long l, @Param("attendanceGroupId") Long l2);

    List<GroupMemberDto> listConfirmByAttendanceGroupIdAndType(@Param("type") Integer num, @Param("orgId") Long l, @Param("attendanceGroupId") Long l2);

    int insertAttendanceGroupMember(AttendanceGroupMember attendanceGroupMember);

    int deleteGroupDept(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("attendanceGroupId") Long l3);

    int deleteGroupDeptByGroupIdList(@Param("deptId") Long l, @Param("orgId") Long l2, @Param("attendanceGroupIdList") List<Long> list);

    int deleteGroupMember(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("attendanceGroupId") Long l3);

    List<CountInfoForMonthResponse> listCountForMonthMember(CountInfoForMonthRequest countInfoForMonthRequest);

    List<AttendanceGroupMember> listByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listGroupIdByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listGroupIdByDeptIds(@Param("deptIds") List<Long> list, @Param("orgId") Long l);

    List<AttendanceGroupMember> listByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    AttendanceGroupMemberInfo getOneByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    AttendanceGroupMemberInfo getOneByDeptId(@Param("deptId") Long l, @Param("orgId") Long l2);

    List<Long> listAttendanceGroupIdByMemberIdAndTime(@Param("memberId") Long l, @Param("attendanceGroupId") Long l2, @Param("orgId") Long l3, @Param("queryDate") String str);

    int batchInsert(@Param("memberLists") List<AttendanceGroupMember> list);

    List<AttendanceGroupMember> listByMemberIdsAndDeptIds(@Param("memberIds") List<Long> list, @Param("deptIds") List<Long> list2, @Param("orgId") Long l);
}
