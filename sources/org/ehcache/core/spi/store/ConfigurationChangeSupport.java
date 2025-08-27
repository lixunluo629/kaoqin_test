package org.ehcache.core.spi.store;

import java.util.List;
import org.ehcache.core.CacheConfigurationChangeListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/ConfigurationChangeSupport.class */
public interface ConfigurationChangeSupport {
    List<CacheConfigurationChangeListener> getConfigurationChangeListeners();
}
