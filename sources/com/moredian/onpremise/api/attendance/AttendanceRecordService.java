package com.moredian.onpremise.api.attendance;

import com.moredian.onpremise.core.model.request.AttendanceStatisticsForMonthExportRequest;
import com.moredian.onpremise.core.model.request.CountInfoForDayRequest;
import com.moredian.onpremise.core.model.request.CountInfoForMonthRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceRecordRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceRecordRequest;
import com.moredian.onpremise.core.model.response.CountInfoForMonthResponse;
import com.moredian.onpremise.core.model.response.ListAttendanceRecordResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayDetailResponse;
import com.moredian.onpremise.core.model.response.ListInfoForDayResponse;
import com.moredian.onpremise.core.utils.PageList;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/attendance/AttendanceRecordService.class */
public interface AttendanceRecordService {
    PageList<ListAttendanceRecordResponse> listAttendanceRecord(ListAttendanceRecordRequest listAttendanceRecordRequest);

    PageList<ListInfoForDayResponse> listInfoForDay(CountInfoForDayRequest countInfoForDayRequest);

    PageList<CountInfoForMonthResponse> listInfoForMonth(CountInfoForMonthRequest countInfoForMonthRequest);

    boolean saveRecord(SaveAttendanceRecordRequest saveAttendanceRecordRequest);

    ListInfoForDayResponse statisticsForDay(CountInfoForDayRequest countInfoForDayRequest);

    PageList<ListInfoForDayDetailResponse> statisticsForDayDetail(CountInfoForDayRequest countInfoForDayRequest);

    String statisticsForMonthExport(AttendanceStatisticsForMonthExportRequest attendanceStatisticsForMonthExportRequest);
}
