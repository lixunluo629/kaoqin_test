package com.moredian.onpremise.api.visit;

import com.moredian.onpremise.core.model.request.SaveVisitRecordDetailRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncExternalContactDetailRequest;
import com.moredian.onpremise.core.model.request.TerminalSyncRequest;
import com.moredian.onpremise.core.model.request.TerminalVisitRecordRequest;
import com.moredian.onpremise.core.model.request.VisitRecordDetailListRequest;
import com.moredian.onpremise.core.model.request.VisitRecordListRequest;
import com.moredian.onpremise.core.model.response.TerminalSyncExternalContactDetailResponse;
import com.moredian.onpremise.core.model.response.TerminalSyncResponse;
import com.moredian.onpremise.core.model.response.VisitRecordDetailResponse;
import com.moredian.onpremise.core.model.response.VisitRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/visit/VisitRecordService.class */
public interface VisitRecordService {
    Long addVisitRecord(TerminalVisitRecordRequest terminalVisitRecordRequest);

    PageList<VisitRecordResponse> pageVisitRecord(VisitRecordListRequest visitRecordListRequest);

    TerminalSyncResponse<Long> syncExternalContact(TerminalSyncRequest terminalSyncRequest);

    List<TerminalSyncExternalContactDetailResponse> syncExternalContactDetail(TerminalSyncExternalContactDetailRequest terminalSyncExternalContactDetailRequest);

    Long addVisitRecordDetail(SaveVisitRecordDetailRequest saveVisitRecordDetailRequest);

    void deleteExpiredVisit(Long l);

    PageList<VisitRecordDetailResponse> pageVisitRecordDetail(VisitRecordDetailListRequest visitRecordDetailListRequest);
}
