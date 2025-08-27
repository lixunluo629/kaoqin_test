package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEventTrip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventTripMapper.class */
public interface AttendanceEventTripMapper {
    int insertOne(AttendanceEventTrip attendanceEventTrip);

    int softDeleteByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    AttendanceEventTrip findByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    int update(AttendanceEventTrip attendanceEventTrip);
}
