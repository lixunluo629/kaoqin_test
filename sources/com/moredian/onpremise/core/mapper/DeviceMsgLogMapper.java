package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.DeviceMsgLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/DeviceMsgLogMapper.class */
public interface DeviceMsgLogMapper extends Mapper<DeviceMsgLog> {
    int inserOne(DeviceMsgLog deviceMsgLog);

    int insertBatch(List<DeviceMsgLog> list);

    List<DeviceMsgLog> getListByUuid(@Param("orgId") Long l, @Param("uuid") String str);

    int updateByUuidAndDeviceSn(DeviceMsgLog deviceMsgLog);
}
