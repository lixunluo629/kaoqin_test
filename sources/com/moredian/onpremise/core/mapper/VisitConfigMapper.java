package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.VisitConfig;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/VisitConfigMapper.class */
public interface VisitConfigMapper extends Mapper<VisitConfig> {
    int insertOne(VisitConfig visitConfig);

    int softDeleteOneById(@Param("orgId") Long l, @Param("id") Long l2);

    int updateOneById(VisitConfig visitConfig);

    VisitConfig getOneById(@Param("orgId") Long l, @Param("id") Long l2);
}
