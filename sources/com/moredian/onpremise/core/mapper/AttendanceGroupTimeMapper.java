package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceGroupTime;
import com.moredian.onpremise.core.model.dto.AttendanceGroupTimeDto;
import com.moredian.onpremise.core.model.response.QueryAttendanceGroupDateFrameResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceGroupTimeMapper.class */
public interface AttendanceGroupTimeMapper {
    List<AttendanceGroupTimeDto> listByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int deleteByAttendanceGroupId(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    QueryAttendanceGroupDateFrameResponse getDateFrame(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2);

    int insertAttendanceGroupTime(AttendanceGroupTime attendanceGroupTime);

    int batchInsertAttendanceGroupTime(@Param("times") List<AttendanceGroupTime> list);

    int deleteByIds(@Param("deleteAuths") List<AttendanceGroupTimeDto> list, @Param("orgId") Long l);

    List<AttendanceGroupTimeDto> listByAttendanceMemberId(@Param("memberId") Long l, @Param("orgId") Long l2);

    AttendanceGroupTime getOneByAttendanceGroupIdAndDate(@Param("attendanceGroupId") Long l, @Param("orgId") Long l2, @Param("date") Date date);
}
