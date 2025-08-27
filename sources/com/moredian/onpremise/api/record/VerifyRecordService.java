package com.moredian.onpremise.api.record;

import com.moredian.onpremise.core.model.request.StatisticsVerifyScoreRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordBatchSaveRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordExcelRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordListRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordSaveRequest;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreResponse;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreV2Response;
import com.moredian.onpremise.core.model.response.VerifyRecordExcelResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordSaveResponse;
import com.moredian.onpremise.core.utils.PageList;
import java.util.Date;
import java.util.List;

/* loaded from: onpremise-api-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/api/record/VerifyRecordService.class */
public interface VerifyRecordService {
    PageList<VerifyRecordResponse> recordList(VerifyRecordListRequest verifyRecordListRequest);

    List<VerifyRecordSaveResponse> saveRecord(VerifyRecordSaveRequest verifyRecordSaveRequest);

    List<VerifyRecordExcelResponse> excelRecord(VerifyRecordExcelRequest verifyRecordExcelRequest);

    boolean batchSaveRecord(VerifyRecordBatchSaveRequest verifyRecordBatchSaveRequest);

    boolean delete(Date date, Date date2);

    StatisticsVerifyScoreResponse statisticsVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    StatisticsVerifyScoreV2Response statisticsVerifyScoreV2(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);
}
