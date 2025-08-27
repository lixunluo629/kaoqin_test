package org.springframework.cache;

import java.util.Collection;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/CacheManager.class */
public interface CacheManager {
    Cache getCache(String str);

    Collection<String> getCacheNames();
}
