package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.CheckInTaskDevice;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CheckInTaskDeviceMapper.class */
public interface CheckInTaskDeviceMapper extends Mapper<CheckInTaskDevice> {
    int insertCheckInTaskDevice(CheckInTaskDevice checkInTaskDevice);

    int insertBatch(@Param("list") List<CheckInTaskDevice> list);

    int deleteById(@Param("orgId") Long l, @Param("id") Long l2);

    int deleteByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    int softDeleteById(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("id") Long l2);

    int softDeleteByTaskId(@Param("gmtModify") Date date, @Param("orgId") Long l, @Param("taskId") Long l2);

    CheckInTaskDevice findById(@Param("orgId") Long l, @Param("id") Long l2);

    List<CheckInTaskDevice> getByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    int countByTaskId(@Param("orgId") Long l, @Param("taskId") Long l2);

    int countByTaskIdAndDeviceSn(@Param("orgId") Long l, @Param("taskId") Long l2, @Param("deviceSn") String str);

    List<CheckInTaskDevice> getAllByDeviceId(@Param("orgId") Long l, @Param("deviceId") Long l2);

    List<CheckInTaskDevice> getByDeviceId(@Param("orgId") Long l, @Param("deviceId") Long l2);
}
