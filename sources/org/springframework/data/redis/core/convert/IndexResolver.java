package org.springframework.data.redis.core.convert;

import java.util.Set;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/IndexResolver.class */
public interface IndexResolver {
    Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, Object obj);

    Set<IndexedData> resolveIndexesFor(String str, String str2, TypeInformation<?> typeInformation, Object obj);
}
