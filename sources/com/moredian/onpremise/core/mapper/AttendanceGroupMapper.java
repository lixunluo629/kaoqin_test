package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceGroup;
import com.moredian.onpremise.core.model.dto.AttendanceGroupDto;
import com.moredian.onpremise.core.model.request.ListAttendanceGroupRequest;
import com.moredian.onpremise.core.model.response.AttendanceGroupListResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceGroupResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceGroupMapper.class */
public interface AttendanceGroupMapper {
    List<AttendanceGroupListResponse> listAttendanceGroup(ListAttendanceGroupRequest listAttendanceGroupRequest);

    AttendanceGroup getOneById(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    AttendanceGroup getOneByGroupName(@Param("groupName") String str, @Param("orgId") Long l);

    int deleteById(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int insertAttendanceGroup(AttendanceGroup attendanceGroup);

    int updateAttendanceGroup(AttendanceGroup attendanceGroup);

    AttendanceGroup getLastOne(@Param("orgId") Long l);

    AttendanceGroup getOneByIdIncludeDelete(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    List<TerminalSyncAttendanceGroupResponse> listSyncAttendanceGroup(@Param("orgId") Long l, @Param("lastSyncTime") String str, @Param("groupIds") List<Long> list);

    int batchUpdateModifyTime(@Param("orgId") Long l, @Param("attendanceGroupIds") List<Long> list);

    List<AttendanceGroupDto> listAttendanceGroupByIds(@Param("orgId") Long l, @Param("attendanceGroupIds") List<Long> list);
}
