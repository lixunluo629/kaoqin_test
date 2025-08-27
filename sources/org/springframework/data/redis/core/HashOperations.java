package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/HashOperations.class */
public interface HashOperations<H, HK, HV> {
    Long delete(H h, Object... objArr);

    Boolean hasKey(H h, Object obj);

    HV get(H h, Object obj);

    List<HV> multiGet(H h, Collection<HK> collection);

    Long increment(H h, HK hk, long j);

    Double increment(H h, HK hk, double d);

    Set<HK> keys(H h);

    Long size(H h);

    void putAll(H h, Map<? extends HK, ? extends HV> map);

    void put(H h, HK hk, HV hv);

    Boolean putIfAbsent(H h, HK hk, HV hv);

    List<HV> values(H h);

    Map<HK, HV> entries(H h);

    Cursor<Map.Entry<HK, HV>> scan(H h, ScanOptions scanOptions);

    RedisOperations<H, ?> getOperations();
}
