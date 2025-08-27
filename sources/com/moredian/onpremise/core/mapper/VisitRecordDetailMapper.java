package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.VisitRecordDetail;
import com.moredian.onpremise.core.model.request.VisitRecordDetailListRequest;
import com.moredian.onpremise.core.model.response.VisitRecordDetailResponse;
import java.util.List;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/VisitRecordDetailMapper.class */
public interface VisitRecordDetailMapper extends Mapper<VisitRecordDetail> {
    int insertOne(VisitRecordDetail visitRecordDetail);

    List<VisitRecordDetailResponse> pageListVisitRecordDetail(VisitRecordDetailListRequest visitRecordDetailListRequest);
}
