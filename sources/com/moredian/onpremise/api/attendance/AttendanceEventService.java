package com.moredian.onpremise.api.attendance;

import com.moredian.onpremise.core.model.request.AttendanceEventListRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceEventRequest;
import com.moredian.onpremise.core.model.request.UpdateAttendanceEventRequest;
import com.moredian.onpremise.core.model.response.AttendanceEventDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceEventListResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/attendance/AttendanceEventService.class */
public interface AttendanceEventService {
    boolean insertAttendanceEvent(SaveAttendanceEventRequest saveAttendanceEventRequest);

    boolean deleteAttendanceEvent(DeleteAttendanceEventRequest deleteAttendanceEventRequest);

    boolean updateAttendanceEvent(UpdateAttendanceEventRequest updateAttendanceEventRequest);

    AttendanceEventDetailResponse findAttendanceEventById(QueryAttendanceEventRequest queryAttendanceEventRequest);

    PageList<AttendanceEventListResponse> pageFindAttendanceEvent(AttendanceEventListRequest attendanceEventListRequest);

    List<AttendanceEventDetailResponse> getAttendanceEventByMemberId(Long l, Long l2);
}
