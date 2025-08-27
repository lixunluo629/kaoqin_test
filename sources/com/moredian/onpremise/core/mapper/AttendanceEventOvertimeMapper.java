package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEventOvertime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventOvertimeMapper.class */
public interface AttendanceEventOvertimeMapper {
    int insertOne(AttendanceEventOvertime attendanceEventOvertime);

    int softDeleteByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    AttendanceEventOvertime findByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    int update(AttendanceEventOvertime attendanceEventOvertime);

    int getWorkOvertimeEventByMemberIdAndTime(Long l, Long l2, Long l3);
}
