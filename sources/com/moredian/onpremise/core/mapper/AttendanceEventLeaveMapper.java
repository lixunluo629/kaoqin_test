package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEventLeave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventLeaveMapper.class */
public interface AttendanceEventLeaveMapper {
    int insertOne(AttendanceEventLeave attendanceEventLeave);

    int softDeleteByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    AttendanceEventLeave findByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    int update(AttendanceEventLeave attendanceEventLeave);
}
