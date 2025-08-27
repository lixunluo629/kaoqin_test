package org.ehcache.core;

import java.util.List;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/InternalRuntimeConfiguration.class */
interface InternalRuntimeConfiguration {
    boolean addCacheConfigurationListener(List<CacheConfigurationChangeListener> list);

    boolean removeCacheConfigurationListener(CacheConfigurationChangeListener cacheConfigurationChangeListener);
}
