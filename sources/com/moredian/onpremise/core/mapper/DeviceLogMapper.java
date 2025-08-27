package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.DeviceLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/DeviceLogMapper.class */
public interface DeviceLogMapper extends Mapper<DeviceLog> {
    int insertDeviceLog(DeviceLog deviceLog);

    int updateDeviceLogById(DeviceLog deviceLog);

    DeviceLog getLatestDeviceLogByDeviceSn(@Param("deviceSn") String str, @Param("orgId") Long l);
}
