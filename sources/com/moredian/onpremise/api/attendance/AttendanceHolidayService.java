package com.moredian.onpremise.api.attendance;

import com.moredian.onpremise.core.model.domain.AttendanceHoliday;
import com.moredian.onpremise.core.model.request.AttendanceHolidayListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceHolidayRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.AttendanceHolidayListResponse;
import com.moredian.onpremise.core.model.response.AttendanceHolidayResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceHolidayResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/attendance/AttendanceHolidayService.class */
public interface AttendanceHolidayService {
    Boolean insertAttendanceHoliday(SaveAttendanceHolidayRequest saveAttendanceHolidayRequest);

    Boolean deleteAttendanceHoliday(DeleteAttendanceHolidayRequest deleteAttendanceHolidayRequest);

    Boolean updateAttendanceHoliday(SaveAttendanceHolidayRequest saveAttendanceHolidayRequest);

    AttendanceHolidayResponse findAttendanceHolidayById(QueryAttendanceHolidayRequest queryAttendanceHolidayRequest);

    PageList<AttendanceHolidayListResponse> pageFindAttendanceHoliday(AttendanceHolidayListRequest attendanceHolidayListRequest);

    List<AttendanceHoliday> getAttendanceHolidayByGroupId(Long l, Long l2);

    TerminalSyncResponse<TerminalSyncAttendanceHolidayResponse> syncAttendanceHoliday(TerminalSyncRequest terminalSyncRequest);
}
