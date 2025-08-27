package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceHoliday;
import com.moredian.onpremise.core.model.request.AttendanceHolidayListRequest;
import com.moredian.onpremise.core.model.response.AttendanceHolidayListResponse;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceHolidayMapper.class */
public interface AttendanceHolidayMapper extends Mapper<AttendanceHoliday> {
    int insertOne(AttendanceHoliday attendanceHoliday);

    int softDeleteById(@Param("orgId") Long l, @Param("attendanceHolidayId") Long l2);

    int updateOne(AttendanceHoliday attendanceHoliday);

    AttendanceHoliday findById(@Param("orgId") Long l, @Param("attendanceHolidayId") Long l2);

    List<AttendanceHolidayListResponse> pageFind(AttendanceHolidayListRequest attendanceHolidayListRequest);

    List<AttendanceHoliday> listByAttendanceGroupId(@Param("orgId") Long l, @Param("attendanceGroupId") Long l2);

    AttendanceHoliday getLastOne(@Param("orgId") Long l);

    List<AttendanceHoliday> listSyncAttendanceHoliday(@Param("orgId") Long l, @Param("lastSyncTime") String str);
}
