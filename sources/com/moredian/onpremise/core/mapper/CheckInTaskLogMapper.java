package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInTaskLog;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.response.CheckInLogDayListResponse;
import java.util.List;
import java.util.Map;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInTaskLogMapper.class */
public interface CheckInTaskLogMapper extends Mapper<CheckInTaskLog> {
    int insertCheckInTaskLog(CheckInTaskLog checkInTaskLog);

    List<CheckInLogDayListResponse> pageFind(CheckInLogListRequest checkInLogListRequest);

    int updateCheckInCount(Map map);

    int countByTaskIdAndTaskTime(Map map);

    CheckInTaskLog findCond(Map map);

    int deleteByTask(Map map);
}
