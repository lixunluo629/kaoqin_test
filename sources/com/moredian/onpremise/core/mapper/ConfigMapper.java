package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/ConfigMapper.class */
public interface ConfigMapper {
    Config getOneByKey(@Param("key") String str);

    int insert(@Param("key") String str, @Param("value") String str2);

    int update(@Param("key") String str, @Param("value") String str2);
}
