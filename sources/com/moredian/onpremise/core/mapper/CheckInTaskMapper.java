package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInTask;
import com.moredian.onpremise.core.model.request.CheckInTaskListRequest;
import com.moredian.onpremise.core.model.response.CheckInTaskListResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInTaskMapper.class */
public interface CheckInTaskMapper extends Mapper<CheckInTask> {
    int insertCheckInTask(CheckInTask checkInTask);

    int deleteById(@Param("orgId") Long l, @Param("id") Long l2);

    int updateCheckInTask(CheckInTask checkInTask);

    CheckInTask findById(@Param("orgId") Long l, @Param("id") Long l2);

    List<CheckInTaskListResponse> pageFind(CheckInTaskListRequest checkInTaskListRequest);

    List<CheckInTask> getAllCheckInTask();

    List<CheckInTask> getCheckInTaskByDeviceSnAndTaskTime(@Param("orgId") Long l, @Param("deviceSn") String str, @Param("taskTime") String str2);

    List<CheckInTask> getAllSingleCheckInTask(@Param("cycle") Integer num);

    List<CheckInTask> findForSync(Map map);

    int updateModify(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("id") Long l2);

    List<CheckInTask> getCheckInTaskByDevices(@Param("orgId") Long l, @Param("deviceSns") List<String> list);

    CheckInTask findLast(@Param("orgId") Long l);
}
