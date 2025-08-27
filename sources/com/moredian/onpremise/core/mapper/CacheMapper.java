package com.moredian.onpremise.core.mapper;

import com.moredian.onpremise.core.model.domain.Cache;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/mapper/CacheMapper.class */
public interface CacheMapper {
    int insert(Cache cache);

    int updateCacheValve(@Param("cacheKey") String str, @Param("cacheValue") String str2);

    int deleteByCacheKey(@Param("cacheKey") String str);

    Cache getByCacheKey(@Param("cacheKey") String str);

    List<Cache> getByCacheType(@Param("cacheType") String str);

    int deleteAll();
}
