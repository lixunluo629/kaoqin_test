package com.moredian.onpremise.api.attendance;

import com.moredian.onpremise.core.model.request.CheckMemberHasAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.DeleteAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.ListAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.QueryAttendanceGroupDetailRequest;
import com.moredian.onpremise.core.model.request.SaveAttendanceGroupRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.AttendanceGroupDetailResponse;
import com.moredian.onpremise.core.model.response.AttendanceGroupListResponse;
import com.moredian.onpremise.core.model.response.CheckMemberHasAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.SaveAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncAttendanceGroupResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/attendance/AttendanceGroupService.class */
public interface AttendanceGroupService {
    SaveAttendanceGroupResponse saveAttendanceGroup(SaveAttendanceGroupRequest saveAttendanceGroupRequest);

    AttendanceGroupDetailResponse queryAttendanceGroupDetail(QueryAttendanceGroupDetailRequest queryAttendanceGroupDetailRequest);

    PageList<AttendanceGroupListResponse> listAttendanceGroup(ListAttendanceGroupRequest listAttendanceGroupRequest);

    boolean deleteAttendanceGroup(DeleteAttendanceGroupRequest deleteAttendanceGroupRequest);

    TerminalSyncResponse<TerminalSyncAttendanceGroupResponse> syncAttendanceGroup(TerminalSyncRequest terminalSyncRequest);

    List<CheckMemberHasAttendanceGroupResponse> checkMemberHasAttendanceGroup(CheckMemberHasAttendanceGroupRequest checkMemberHasAttendanceGroupRequest);
}
