package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceEventSupplement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceEventSupplementMapper.class */
public interface AttendanceEventSupplementMapper {
    int insertOne(AttendanceEventSupplement attendanceEventSupplement);

    int softDeleteByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    AttendanceEventSupplement findByEventIdAndMemberId(@Param("orgId") Long l, @Param("eventId") Long l2, @Param("memberId") Long l3);

    int update(AttendanceEventSupplement attendanceEventSupplement);
}
