package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.VisitDevice;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/VisitDeviceMapper.class */
public interface VisitDeviceMapper extends Mapper<VisitDevice> {
    int insertOne(VisitDevice visitDevice);

    int softDeleteOneByConfigIdAndDeviceSn(@Param("orgId") Long l, @Param("visitConfigId") Long l2, @Param("deviceSn") String str);

    List<VisitDevice> getListByConfigId(@Param("orgId") Long l, @Param("visitConfigId") Long l2);

    int insertBatch(List<VisitDevice> list);

    List<String> getListDeviceSnByConfigId(@Param("orgId") Long l, @Param("visitConfigId") Long l2);

    List<Long> getListVisitConfigIdByDeviceSn(@Param("orgId") Long l, @Param("deviceSn") String str);

    VisitDevice getLatestByDeviceAndVisitConfig(@Param("orgId") Long l, @Param("deviceSn") String str, @Param("visitConfigId") Long l2);
}
