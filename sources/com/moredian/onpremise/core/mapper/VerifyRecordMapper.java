package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInLog;
import com.moredian.onpremise.core.model.domain.VerifyRecord;
import com.moredian.onpremise.core.model.request.StatisticsVerifyScoreRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordExcelRequest;
import com.moredian.onpremise.core.model.request.VerifyRecordListRequest;
import com.moredian.onpremise.core.model.response.StatisticsVerifyScoreDetailResponse;
import com.moredian.onpremise.core.model.response.VerifyRecordResponse;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/VerifyRecordMapper.class */
public interface VerifyRecordMapper extends tk.mybatis.mapper.common.Mapper<CheckInLog> {
    List<VerifyRecordResponse> getVerifyList(VerifyRecordListRequest verifyRecordListRequest);

    int saveVerifyRecord(VerifyRecord verifyRecord);

    List<VerifyRecord> getVerifyExcelList(VerifyRecordExcelRequest verifyRecordExcelRequest);

    int deleteByDate(@Param("date") Date date);

    List<String> listSnapUrl(@Param("date") Date date);

    List<VerifyRecordResponse> statisticsVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    Integer countStatisticsVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    List<StatisticsVerifyScoreDetailResponse> statisticsFirstVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    List<StatisticsVerifyScoreDetailResponse> statisticsSecondVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    List<StatisticsVerifyScoreDetailResponse> statisticsMirrorVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    Integer countStatisticsMirrorVerifyScore(StatisticsVerifyScoreRequest statisticsVerifyScoreRequest);

    Integer countVerifyRecord(VerifyRecord verifyRecord);

    VerifyRecord getOneByVerifyRecordId(@Param("orgId") Long l, @Param("verifyRecordId") Long l2);
}
