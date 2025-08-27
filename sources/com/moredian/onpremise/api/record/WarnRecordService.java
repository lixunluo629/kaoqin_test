package com.moredian.onpremise.api.record;

import com.moredian.onpremise.core.model.request.BaseRequest;
import com.moredian.onpremise.core.model.request.WarnRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.WarnRecordListRequest;
import com.moredian.onpremise.core.model.request.WarnRecordSaveRequest;
import com.moredian.onpremise.core.model.response.WarnRecordResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/record/WarnRecordService.class */
public interface WarnRecordService {
    PageList<WarnRecordResponse> recordList(WarnRecordListRequest warnRecordListRequest);

    boolean saveRecord(WarnRecordSaveRequest warnRecordSaveRequest);

    String excelRecord(WarnRecordListRequest warnRecordListRequest);

    List<String> getWarnTypes(BaseRequest baseRequest);

    boolean batchSaveRecord(WarnRecordBatchSaveRequest warnRecordBatchSaveRequest);
}
