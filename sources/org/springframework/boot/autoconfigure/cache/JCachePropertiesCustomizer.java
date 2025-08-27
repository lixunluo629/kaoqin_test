package org.springframework.boot.autoconfigure.cache;

import java.util.Properties;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/JCachePropertiesCustomizer.class */
interface JCachePropertiesCustomizer {
    void customize(CacheProperties cacheProperties, Properties properties);
}
