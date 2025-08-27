package org.ehcache.impl.internal.classes;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.internal.classes.commonslang.reflect.ConstructorUtils;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/ClassInstanceProvider.class */
public class ClassInstanceProvider<K, T> {
    protected final Map<K, ClassInstanceConfiguration<T>> preconfigured;
    protected final ConcurrentWeakIdentityHashMap<T, AtomicInteger> providedVsCount;
    protected final Set<T> instantiated;
    private final Class<? extends ClassInstanceConfiguration<T>> cacheLevelConfig;
    private final boolean uniqueClassLevelConfig;

    protected ClassInstanceProvider(ClassInstanceProviderConfiguration<K, T> factoryConfig, Class<? extends ClassInstanceConfiguration<T>> cacheLevelConfig) {
        this(factoryConfig, cacheLevelConfig, false);
    }

    protected ClassInstanceProvider(ClassInstanceProviderConfiguration<K, T> factoryConfig, Class<? extends ClassInstanceConfiguration<T>> cacheLevelConfig, boolean uniqueClassLevelConfig) {
        this.preconfigured = Collections.synchronizedMap(new LinkedHashMap());
        this.providedVsCount = new ConcurrentWeakIdentityHashMap<>();
        this.instantiated = Collections.newSetFromMap(new ConcurrentWeakIdentityHashMap());
        this.uniqueClassLevelConfig = uniqueClassLevelConfig;
        if (factoryConfig != null) {
            this.preconfigured.putAll(factoryConfig.getDefaults());
        }
        this.cacheLevelConfig = cacheLevelConfig;
    }

    protected ClassInstanceConfiguration<T> getPreconfigured(K alias) {
        return this.preconfigured.get(alias);
    }

    protected T newInstance(K alias, CacheConfiguration<?, ?> cacheConfiguration) {
        ClassInstanceConfiguration<T> config = null;
        if (this.uniqueClassLevelConfig) {
            config = (ClassInstanceConfiguration) ServiceUtils.findSingletonAmongst(this.cacheLevelConfig, cacheConfiguration.getServiceConfigurations());
        } else {
            Iterator<? extends ClassInstanceConfiguration<T>> iterator = ServiceUtils.findAmongst(this.cacheLevelConfig, cacheConfiguration.getServiceConfigurations()).iterator();
            if (iterator.hasNext()) {
                config = (ClassInstanceConfiguration) iterator.next();
            }
        }
        return newInstance((ClassInstanceProvider<K, T>) alias, config);
    }

    protected T newInstance(K alias, ServiceConfiguration<?> serviceConfiguration) {
        ClassInstanceConfiguration<T> config = null;
        if (serviceConfiguration != null && this.cacheLevelConfig.isAssignableFrom(serviceConfiguration.getClass())) {
            config = this.cacheLevelConfig.cast(serviceConfiguration);
        }
        return newInstance((ClassInstanceProvider<K, T>) alias, config);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private T newInstance(K alias, ClassInstanceConfiguration<T> config) {
        T instance;
        if (config == null) {
            config = getPreconfigured(alias);
            if (config == null) {
                return null;
            }
        }
        if (config.getInstance() != null) {
            instance = config.getInstance();
        } else {
            try {
                instance = ConstructorUtils.invokeConstructor(config.getClazz(), config.getArguments());
                this.instantiated.add(instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e2) {
                throw new RuntimeException(e2);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException(e4);
            }
        }
        AtomicInteger currentCount = this.providedVsCount.putIfAbsent(instance, new AtomicInteger(1));
        if (currentCount != null) {
            currentCount.incrementAndGet();
        }
        return instance;
    }

    protected void releaseInstance(T instance) throws IOException {
        AtomicInteger currentCount = this.providedVsCount.get(instance);
        if (currentCount != null) {
            if (currentCount.decrementAndGet() < 0) {
                currentCount.incrementAndGet();
                throw new IllegalArgumentException("Given instance of " + instance.getClass().getName() + " is not managed by this provider");
            }
            if (this.instantiated.remove(instance) && (instance instanceof Closeable)) {
                ((Closeable) instance).close();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Given instance of " + instance.getClass().getName() + " is not managed by this provider");
    }

    public void start(ServiceProvider<Service> serviceProvider) {
    }

    public void stop() {
    }
}
