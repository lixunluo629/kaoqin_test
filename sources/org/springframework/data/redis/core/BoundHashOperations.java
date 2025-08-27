package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundHashOperations.class */
public interface BoundHashOperations<H, HK, HV> extends BoundKeyOperations<H> {
    Long delete(Object... objArr);

    Boolean hasKey(Object obj);

    HV get(Object obj);

    List<HV> multiGet(Collection<HK> collection);

    Long increment(HK hk, long j);

    Double increment(HK hk, double d);

    Set<HK> keys();

    Long size();

    void putAll(Map<? extends HK, ? extends HV> map);

    void put(HK hk, HV hv);

    Boolean putIfAbsent(HK hk, HV hv);

    List<HV> values();

    Map<HK, HV> entries();

    Cursor<Map.Entry<HK, HV>> scan(ScanOptions scanOptions);

    RedisOperations<H, ?> getOperations();
}
