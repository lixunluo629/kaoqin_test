package org.springframework.boot.autoconfigure.cache;

import javax.cache.CacheManager;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/JCacheManagerCustomizer.class */
public interface JCacheManagerCustomizer {
    void customize(CacheManager cacheManager);
}
