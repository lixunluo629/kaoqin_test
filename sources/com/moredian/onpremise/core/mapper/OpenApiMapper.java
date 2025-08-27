package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.OpenApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/OpenApiMapper.class */
public interface OpenApiMapper {
    OpenApi getLastOne(@Param("orgId") Long l);

    OpenApi getByAppKey(@Param("appKey") String str);

    int insert(OpenApi openApi);

    int delete(@Param("orgId") Long l);
}
