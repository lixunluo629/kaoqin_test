package org.springframework.boot.autoconfigure.cache;

import org.springframework.cache.CacheManager;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheManagerCustomizer.class */
public interface CacheManagerCustomizer<T extends CacheManager> {
    void customize(T t);
}
