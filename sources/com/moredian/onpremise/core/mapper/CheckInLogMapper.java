package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInLog;
import com.moredian.onpremise.core.model.request.CheckInLogListRequest;
import com.moredian.onpremise.core.model.response.CheckInLogListResponse;
import com.moredian.onpremise.core.model.response.CheckInLogMemberListResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInLogMapper.class */
public interface CheckInLogMapper extends Mapper<CheckInLog> {
    int insertCheckInLog(CheckInLog checkInLog);

    int insertBatch(@Param("list") List<CheckInLog> list);

    int checkIn(CheckInLog checkInLog);

    CheckInLog findById(@Param("orgId") Long l, @Param("id") Long l2);

    List<CheckInLogListResponse> pageFind(CheckInLogListRequest checkInLogListRequest);

    List<CheckInLogMemberListResponse> pageCheckInLogMember(CheckInLogListRequest checkInLogListRequest);

    int countCheckInLog(Map map);

    CheckInLog findByCond(CheckInLog checkInLog);

    int deleteByTask(Map map);

    int deleteByDate(@Param("date") Date date);

    List<String> listSnapUrl(@Param("date") Date date);
}
