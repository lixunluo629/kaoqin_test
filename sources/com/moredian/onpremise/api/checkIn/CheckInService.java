package com.moredian.onpremise.api.checkIn;

import com.moredian.onpremise.core.model.domain.CheckInLog;
import com.moredian.onpremise.core.model.domain.CheckInTask;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.request.CheckInLogRequest;
import com.moredian.onpremise.core.model.request.CheckInTaskListRequest;
import com.moredian.onpremise.core.model.request.CheckInTaskOpenOrCloseRequest;
import com.moredian.onpremise.core.model.request.DeleteCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.ListCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.QueryCheckInSupplementDetailRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInSupplementRequest;
import com.moredian.onpremise.core.model.request.SaveCheckInTaskRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncCheckInTaskRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.response.BizCheckResponse;
import com.moredian.onpremise.core.model.response.CheckInLogDayListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogMemberListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogResponse;
import com.moredian.onpremise.core.model.response.CheckInResponse;
import com.moredian.onpremise.core.model.response.CheckInSupplementResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskListResponse;
import com.moredian.onpremise.core.model.response.CheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncMemberCheckInTaskResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/checkIn/CheckInService.class */
public interface CheckInService {
    Long insertCheckInTask(SaveCheckInTaskRequest saveCheckInTaskRequest);

    boolean deleteCheckInTask(Long l, Long l2);

    boolean updateCheckInTask(SaveCheckInTaskRequest saveCheckInTaskRequest);

    boolean openOrCloseCheckInTask(CheckInTaskOpenOrCloseRequest checkInTaskOpenOrCloseRequest);

    PageList<CheckInTaskListResponse> pageCheckInTask(CheckInTaskListRequest checkInTaskListRequest);

    CheckInTaskResponse findCheckInTaskById(Long l, Long l2);

    boolean generateCheckInLog(Date date);

    boolean insertCheckInTaskLog(CheckInTask checkInTask);

    boolean insertCheckInLogBatch(List<CheckInLog> list);

    List<CheckInResponse> checkInList(CheckInLogRequest checkInLogRequest);

    PageList<CheckInLogDayListResponse> pageCheckInLogDay(CheckInLogListRequest checkInLogListRequest);

    PageList<CheckInLogMemberListResponse> pageCheckInLogMember(CheckInLogListRequest checkInLogListRequest);

    List<CheckInLogMemberListResponse> exportCheckInLogMember(CheckInLogListRequest checkInLogListRequest);

    PageList<CheckInLogListResponse> pageCheckInLogDetail(CheckInLogListRequest checkInLogListRequest);

    List<CheckInLogListResponse> exportCheckInLogDetail(CheckInLogListRequest checkInLogListRequest);

    CheckInLogResponse findCheckInLogById(Long l, Long l2);

    Map<String, List> closeSingleCheckInTask();

    TerminalSyncResponse<TerminalSyncCheckInTaskResponse> syncCheckInTask(TerminalSyncCheckInTaskRequest terminalSyncCheckInTaskRequest);

    void notify(Long l, Long l2, Integer num);

    void saveCheckInLog(CheckInLogRequest checkInLogRequest);

    CheckInResponse checkIn(CheckInLogRequest checkInLogRequest);

    BizCheckResponse hasCheck(CheckInLogRequest checkInLogRequest);

    void generateCheckInTaskBaseLibraryByMember(Long l, Long l2, boolean z);

    void generateCheckInTaskBaseLibraryByDept(Long l, Long l2);

    void generateCheckInTaskBaseLibraryByTask(Long l, Long l2, boolean z);

    void generateCheckInTaskBaseLibraryByDevice(String str, Long l);

    TerminalSyncResponse<TerminalSyncMemberCheckInTaskResponse> syncCheckInMember(TerminalSyncRequest terminalSyncRequest);

    void doSendNettyMessageForSyncMember(Long l);

    boolean delete(Date date);

    PageList<CheckInSupplementResponse> listCheckInSupplementResponse(ListCheckInSupplementRequest listCheckInSupplementRequest);

    CheckInSupplementResponse queryCheckInSupplementResponse(QueryCheckInSupplementDetailRequest queryCheckInSupplementDetailRequest);

    void saveCheckInSupplement(SaveCheckInSupplementRequest saveCheckInSupplementRequest);

    void deleteCheckInSupplement(DeleteCheckInSupplementRequest deleteCheckInSupplementRequest);
}
