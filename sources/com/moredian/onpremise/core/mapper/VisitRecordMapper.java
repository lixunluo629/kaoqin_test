package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.VisitRecord;
import com.moredian.onpremise.core.model.request.VisitRecordListRequest;
import com.moredian.onpremise.core.model.response.VisitRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/VisitRecordMapper.class */
public interface VisitRecordMapper extends Mapper<VisitRecord> {
    int insertOne(VisitRecord visitRecord);

    List<VisitRecordResponse> pageListVisitRecord(VisitRecordListRequest visitRecordListRequest);

    int deleteExpiredVisitorFaceUrl(@Param("orgId") Long l, @Param("expiredTimestamp") Long l2);
}
