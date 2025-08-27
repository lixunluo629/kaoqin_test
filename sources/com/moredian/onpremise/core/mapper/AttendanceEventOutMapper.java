package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEventOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventOutMapper.class */
public interface AttendanceEventOutMapper {
    int insertOne(AttendanceEventOut attendanceEventOut);

    int softDeleteByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    AttendanceEventOut findByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    int update(AttendanceEventOut attendanceEventOut);
}
