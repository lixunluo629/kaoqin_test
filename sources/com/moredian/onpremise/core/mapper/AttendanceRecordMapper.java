package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.AttendanceRecord;
import com.moredian.onpremise.core.model.request.AttendanceStatisticsForMonthExportRequest;
import com.moredian.onpremise.core.model.request.CountInfoForDayRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceRecordRequest;
import com.moredian.onpremise.core.model.response.CountInfoForMonthResponse;
import com.moredian.onpremise.core.model.response.ListAttendanceRecordResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayDetailResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/AttendanceRecordMapper.class */
public interface AttendanceRecordMapper {
    List<ListAttendanceRecordResponse> listAttendanceRecord(ListAttendanceRecordRequest listAttendanceRecordRequest);

    int insert(AttendanceRecord attendanceRecord);

    CountInfoForMonthResponse countForMonthByMemberId(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("queryStartDate") String str, @Param("queryEndDate") String str2);

    int countTimesByRecordType(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("recordType") int i, @Param("queryStartDate") String str, @Param("queryEndDate") String str2);

    AttendanceRecord getTodayWorkAttendance(@Param("earliestWorkRuleTime") Long l, @Param("latestWorkRuleTime") Long l2, @Param("memberId") Long l3, @Param("orgId") Long l4, @Param("attendanceGroupId") Long l5, @Param("recordType") Integer num);

    int deleteById(@Param("attendanceRecordId") Long l);

    List<AttendanceRecord> getRecordStatusByMemberIdAndTime(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("attendanceGroupId") Long l3, @Param("queryDate") String str);

    AttendanceRecord getRecordByMemberIdAndTime(@Param("memberId") Long l, @Param("orgId") Long l2, @Param("attendanceDate") String str, @Param("type") int i);

    int updateAttendanceTime(@Param("attendanceRecordId") Long l, @Param("attendanceResult") Integer num, @Param("attendanceTime") long j, @Param("attendanceDay") Integer num2);

    List<ListInfoForDayDetailResponse> listStatisticsForDayDetail(CountInfoForDayRequest countInfoForDayRequest);

    List<ListAttendanceRecordResponse> listStatisticsForMonth(AttendanceStatisticsForMonthExportRequest attendanceStatisticsForMonthExportRequest);
}
