package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.WarnRecord;
import com.moredian.onpremise.core.model.request.WarnRecordListRequest;
import com.moredian.onpremise.core.model.response.WarnRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/WarnRecordMapper.class */
public interface WarnRecordMapper {
    List<WarnRecordResponse> getWarnRecordList(WarnRecordListRequest warnRecordListRequest);

    int saveWarnRecord(WarnRecord warnRecord);

    List<String> getWarnTypes(@Param("orgId") Long l);
}
