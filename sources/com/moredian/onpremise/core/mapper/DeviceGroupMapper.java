package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.DeviceGroup;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/DeviceGroupMapper.class */
public interface DeviceGroupMapper extends Mapper<DeviceGroup> {
    int insertOne(DeviceGroup deviceGroup);

    int softDeleteById(@Param("id") Long l, @Param("orgId") Long l2);

    int updateOneById(DeviceGroup deviceGroup);

    DeviceGroup getOneById(@Param("id") Long l, @Param("orgId") Long l2);

    List<DeviceGroup> getChildList(@Param("id") Long l, @Param("orgId") Long l2);

    List<DeviceGroup> getList(@Param("orgId") Long l);

    DeviceGroup getOneByNameAndSuperId(@Param("orgId") Long l, @Param("name") String str, @Param("superId") Long l2);
}
