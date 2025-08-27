package org.springframework.boot.autoconfigure.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.CacheManager;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheManagerCustomizers.class */
public class CacheManagerCustomizers {
    private static final Log logger = LogFactory.getLog(CacheManagerCustomizers.class);
    private final List<CacheManagerCustomizer<?>> customizers;

    public CacheManagerCustomizers(List<? extends CacheManagerCustomizer<?>> customizers) {
        this.customizers = customizers != null ? new ArrayList<>(customizers) : Collections.emptyList();
    }

    public <T extends CacheManager> T customize(T cacheManager) {
        for (CacheManagerCustomizer<?> customizer : this.customizers) {
            Class<?> generic = ResolvableType.forClass(CacheManagerCustomizer.class, customizer.getClass()).resolveGeneric(new int[0]);
            if (generic.isInstance(cacheManager)) {
                customize(cacheManager, customizer);
            }
        }
        return cacheManager;
    }

    private void customize(CacheManager cacheManager, CacheManagerCustomizer customizer) {
        try {
            customizer.customize(cacheManager);
        } catch (ClassCastException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Non-matching cache manager type for customizer: " + customizer, ex);
            }
        }
    }
}
