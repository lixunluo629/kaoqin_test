package org.springframework.data.redis.core.convert;

import java.io.Serializable;
import java.util.Map;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/ReferenceResolver.class */
public interface ReferenceResolver {
    Map<byte[], byte[]> resolveReference(Serializable serializable, String str);
}
