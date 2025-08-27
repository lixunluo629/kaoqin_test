package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.OperLog;
import com.moredian.onpremise.core.model.request.OperLogListRequest;
import com.moredian.onpremise.core.model.response.OperLogResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/OperLogMapper.class */
public interface OperLogMapper {
    int insert(OperLog operLog);

    List<OperLogResponse> pageList(OperLogListRequest operLogListRequest);
}
