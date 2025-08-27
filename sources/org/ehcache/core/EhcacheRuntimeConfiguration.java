package org.ehcache.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.core.internal.events.EventListenerWrapper;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.event.EventType;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.service.ServiceConfiguration;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheRuntimeConfiguration.class */
class EhcacheRuntimeConfiguration<K, V> implements CacheRuntimeConfiguration<K, V>, InternalRuntimeConfiguration, HumanReadable {
    private final Collection<ServiceConfiguration<?>> serviceConfigurations;
    private final CacheConfiguration<? super K, ? super V> config;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final ClassLoader classLoader;
    private final Expiry<? super K, ? super V> expiry;
    private volatile ResourcePools resourcePools;
    private final List<CacheConfigurationChangeListener> cacheConfigurationListenerList = new CopyOnWriteArrayList();

    /* JADX WARN: Multi-variable type inference failed */
    EhcacheRuntimeConfiguration(CacheConfiguration<K, V> cacheConfiguration) {
        this.config = cacheConfiguration;
        this.serviceConfigurations = copy(cacheConfiguration.getServiceConfigurations());
        this.keyType = cacheConfiguration.getKeyType();
        this.valueType = cacheConfiguration.getValueType();
        this.evictionAdvisor = cacheConfiguration.getEvictionAdvisor();
        this.classLoader = cacheConfiguration.getClassLoader();
        this.expiry = cacheConfiguration.getExpiry();
        this.resourcePools = cacheConfiguration.getResourcePools();
    }

    @Override // org.ehcache.config.CacheRuntimeConfiguration
    public synchronized void updateResourcePools(ResourcePools pools) throws UnsupportedOperationException, IllegalArgumentException {
        if (pools == null) {
            throw new NullPointerException("Pools to be updated cannot be null");
        }
        ResourcePools updatedResourcePools = this.config.getResourcePools().validateAndMerge(pools);
        fireCacheConfigurationChange(CacheConfigurationProperty.UPDATE_SIZE, this.config.getResourcePools(), updatedResourcePools);
        this.resourcePools = updatedResourcePools;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Collection<ServiceConfiguration<?>> getServiceConfigurations() {
        return this.serviceConfigurations;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Class<K> getKeyType() {
        return this.keyType;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Class<V> getValueType() {
        return this.valueType;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public EvictionAdvisor<? super K, ? super V> getEvictionAdvisor() {
        return this.evictionAdvisor;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Expiry<? super K, ? super V> getExpiry() {
        return this.expiry;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public ResourcePools getResourcePools() {
        return this.resourcePools;
    }

    @Override // org.ehcache.core.InternalRuntimeConfiguration
    public boolean addCacheConfigurationListener(List<CacheConfigurationChangeListener> listeners) {
        return this.cacheConfigurationListenerList.addAll(listeners);
    }

    @Override // org.ehcache.core.InternalRuntimeConfiguration
    public boolean removeCacheConfigurationListener(CacheConfigurationChangeListener listener) {
        return this.cacheConfigurationListenerList.remove(listener);
    }

    @Override // org.ehcache.config.CacheRuntimeConfiguration
    public synchronized void deregisterCacheEventListener(CacheEventListener<? super K, ? super V> listener) {
        fireCacheConfigurationChange(CacheConfigurationProperty.REMOVE_LISTENER, listener, listener);
    }

    @Override // org.ehcache.config.CacheRuntimeConfiguration
    public synchronized void registerCacheEventListener(CacheEventListener<? super K, ? super V> listener, EventOrdering ordering, EventFiring firing, Set<EventType> forEventTypes) {
        EventListenerWrapper<K, V> listenerWrapper = new EventListenerWrapper<>(listener, firing, ordering, EnumSet.copyOf((Collection) forEventTypes));
        fireCacheConfigurationChange(CacheConfigurationProperty.ADD_LISTENER, listenerWrapper, listenerWrapper);
    }

    @Override // org.ehcache.config.CacheRuntimeConfiguration
    public void registerCacheEventListener(CacheEventListener<? super K, ? super V> listener, EventOrdering ordering, EventFiring firing, EventType eventType, EventType... eventTypes) {
        EventListenerWrapper<K, V> listenerWrapper = new EventListenerWrapper<>(listener, firing, ordering, EnumSet.of(eventType, eventTypes));
        fireCacheConfigurationChange(CacheConfigurationProperty.ADD_LISTENER, listenerWrapper, listenerWrapper);
    }

    private <T> Collection<T> copy(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(new ArrayList(collection));
    }

    private <T> void fireCacheConfigurationChange(CacheConfigurationProperty prop, T oldValue, T newValue) {
        if ((oldValue != null && !oldValue.equals(newValue)) || newValue != null) {
            for (CacheConfigurationChangeListener cacheConfigurationListener : this.cacheConfigurationListenerList) {
                cacheConfigurationListener.cacheConfigurationChange(new CacheConfigurationChangeEvent(prop, oldValue, newValue));
            }
        }
    }

    @Override // org.ehcache.core.HumanReadable
    public String readableString() {
        StringBuilder serviceConfigurationsToStringBuilder = new StringBuilder();
        for (ServiceConfiguration<?> serviceConfiguration : this.serviceConfigurations) {
            serviceConfigurationsToStringBuilder.append("\n    ").append(com.itextpdf.layout.element.List.DEFAULT_LIST_SYMBOL);
            if (serviceConfiguration instanceof HumanReadable) {
                serviceConfigurationsToStringBuilder.append(((HumanReadable) serviceConfiguration).readableString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            } else {
                serviceConfigurationsToStringBuilder.append(serviceConfiguration.getClass().getName()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        if (serviceConfigurationsToStringBuilder.length() > 0) {
            serviceConfigurationsToStringBuilder.deleteCharAt(serviceConfigurationsToStringBuilder.length() - 1);
        } else {
            serviceConfigurationsToStringBuilder.append(" None");
        }
        return "keyType: " + this.keyType.getName() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "valueType: " + this.valueType.getName() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "serviceConfigurations:" + serviceConfigurationsToStringBuilder.toString().replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ") + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "evictionAdvisor: " + (this.evictionAdvisor != null ? this.evictionAdvisor.getClass().getName() : "None") + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "expiry: " + (this.expiry != null ? this.expiry.getClass().getSimpleName() : "") + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "resourcePools: \n    " + (this.resourcePools instanceof HumanReadable ? ((HumanReadable) this.resourcePools).readableString() : "").replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ");
    }
}
