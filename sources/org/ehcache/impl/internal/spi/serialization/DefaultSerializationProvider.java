package org.ehcache.impl.internal.spi.serialization;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.spi.service.FileBasedPersistenceContext;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializerConfiguration;
import org.ehcache.impl.serialization.ByteArraySerializer;
import org.ehcache.impl.serialization.CharSerializer;
import org.ehcache.impl.serialization.CompactJavaSerializer;
import org.ehcache.impl.serialization.DoubleSerializer;
import org.ehcache.impl.serialization.FloatSerializer;
import org.ehcache.impl.serialization.IntegerSerializer;
import org.ehcache.impl.serialization.LongSerializer;
import org.ehcache.impl.serialization.StringSerializer;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.UnsupportedTypeException;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/spi/serialization/DefaultSerializationProvider.class */
public class DefaultSerializationProvider implements SerializationProvider {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) DefaultSerializationProvider.class);
    protected final Map<Class<?>, Class<? extends Serializer<?>>> serializers;
    final ConcurrentWeakIdentityHashMap<Serializer<?>, AtomicInteger> providedVsCount = new ConcurrentWeakIdentityHashMap<>();
    final Set<Serializer<?>> instantiated = Collections.newSetFromMap(new ConcurrentWeakIdentityHashMap());

    public DefaultSerializationProvider(DefaultSerializationProviderConfiguration configuration) {
        if (configuration != null) {
            this.serializers = new LinkedHashMap(configuration.getDefaultSerializers());
        } else {
            this.serializers = new LinkedHashMap(Collections.emptyMap());
        }
    }

    @Override // org.ehcache.spi.serialization.SerializationProvider
    public <T> Serializer<T> createKeySerializer(Class<T> clazz, ClassLoader classLoader, ServiceConfiguration<?>... configs) throws NoSuchMethodException, UnsupportedTypeException, SecurityException {
        DefaultSerializerConfiguration<T> configuration = find(DefaultSerializerConfiguration.Type.KEY, configs);
        Serializer<T> serializer = getUserProvidedSerializer(configuration);
        if (serializer == null) {
            serializer = createSerializer(clazz, classLoader, configuration, configs);
            this.instantiated.add(serializer);
        }
        updateProvidedInstanceCounts(serializer);
        return serializer;
    }

    @Override // org.ehcache.spi.serialization.SerializationProvider
    public <T> Serializer<T> createValueSerializer(Class<T> clazz, ClassLoader classLoader, ServiceConfiguration<?>... configs) throws NoSuchMethodException, UnsupportedTypeException, SecurityException {
        DefaultSerializerConfiguration<T> configuration = find(DefaultSerializerConfiguration.Type.VALUE, configs);
        Serializer<T> serializer = getUserProvidedSerializer(configuration);
        if (serializer == null) {
            serializer = createSerializer(clazz, classLoader, configuration, configs);
            this.instantiated.add(serializer);
        }
        updateProvidedInstanceCounts(serializer);
        return serializer;
    }

    private <T> Serializer<T> createSerializer(Class<T> clazz, ClassLoader classLoader, DefaultSerializerConfiguration<T> config, ServiceConfiguration<?>... configs) throws NoSuchMethodException, UnsupportedTypeException, SecurityException {
        Class<? extends Serializer<T>> klazz = getSerializerClassFor(clazz, config);
        try {
            klazz.getConstructor(ClassLoader.class, FileBasedPersistenceContext.class);
            LOG.warn(klazz.getName() + " class has a constructor that takes in a FileBasedPersistenceContext. Support for this constructor has been removed since version 3.2. Consider removing it.");
        } catch (NoSuchMethodException e) {
        }
        try {
            return constructSerializer(clazz, klazz.getConstructor(ClassLoader.class), classLoader);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(klazz + " does not have a constructor that takes in a ClassLoader.", e2);
        }
    }

    private <T> Class<? extends Serializer<T>> getSerializerClassFor(Class<T> cls, DefaultSerializerConfiguration<T> defaultSerializerConfiguration) throws UnsupportedTypeException {
        Class<? extends T> clazz;
        if (defaultSerializerConfiguration != null && (clazz = defaultSerializerConfiguration.getClazz()) != null) {
            return clazz;
        }
        Class<? extends Serializer<T>> cls2 = (Class) this.serializers.get(cls);
        if (cls2 != null) {
            return cls2;
        }
        for (Map.Entry<Class<?>, Class<? extends Serializer<?>>> entry : this.serializers.entrySet()) {
            if (entry.getKey().isAssignableFrom(cls)) {
                return (Class) entry.getValue();
            }
        }
        throw new UnsupportedTypeException("No serializer found for type '" + cls.getName() + "'");
    }

    private <T> Serializer<T> constructSerializer(Class<T> clazz, Constructor<? extends Serializer<T>> constructor, Object... args) throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {
        try {
            Serializer<T> serializer = constructor.newInstance(args);
            LOG.debug("Serializer for <{}> : {}", clazz.getName(), serializer);
            return serializer;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e2) {
            throw new AssertionError(e2);
        } catch (InstantiationException e3) {
            throw new RuntimeException(e3);
        } catch (InvocationTargetException e4) {
            throw new RuntimeException(e4);
        }
    }

    private void updateProvidedInstanceCounts(Serializer<?> serializer) {
        AtomicInteger currentCount = this.providedVsCount.putIfAbsent(serializer, new AtomicInteger(1));
        if (currentCount != null) {
            currentCount.incrementAndGet();
        }
    }

    @Override // org.ehcache.spi.serialization.SerializationProvider
    public void releaseSerializer(Serializer<?> serializer) throws IOException {
        AtomicInteger currentCount = this.providedVsCount.get(serializer);
        if (currentCount != null) {
            if (currentCount.decrementAndGet() < 0) {
                currentCount.incrementAndGet();
                throw new IllegalArgumentException("Given serializer:" + serializer.getClass().getName() + " is not managed by this provider");
            }
            if (this.instantiated.remove(serializer) && (serializer instanceof Closeable)) {
                ((Closeable) serializer).close();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Given serializer:" + serializer.getClass().getName() + " is not managed by this provider");
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
        addDefaultSerializerIfNoneRegistered(this.serializers, Serializable.class, CompactJavaSerializer.asTypedSerializer());
        addDefaultSerializerIfNoneRegistered(this.serializers, Long.class, LongSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, Integer.class, IntegerSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, Float.class, FloatSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, Double.class, DoubleSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, Character.class, CharSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, String.class, StringSerializer.class);
        addDefaultSerializerIfNoneRegistered(this.serializers, byte[].class, ByteArraySerializer.class);
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void addDefaultSerializerIfNoneRegistered(Map<Class<?>, Class<? extends Serializer<?>>> serializers, Class<T> cls, Class<? extends Serializer<T>> cls2) {
        if (!serializers.containsKey(cls)) {
            serializers.put(cls, cls2);
        }
    }

    private static <T> Serializer<T> getUserProvidedSerializer(DefaultSerializerConfiguration<T> conf) {
        Serializer<T> instance;
        if (conf != null && (instance = (Serializer) conf.getInstance()) != null) {
            return instance;
        }
        return null;
    }

    private static <T> DefaultSerializerConfiguration<T> find(DefaultSerializerConfiguration.Type type, ServiceConfiguration<?>... serviceConfigurations) {
        DefaultSerializerConfiguration<T> result = null;
        Collection<DefaultSerializerConfiguration> serializationProviderConfigurations = ServiceUtils.findAmongst(DefaultSerializerConfiguration.class, serviceConfigurations);
        for (DefaultSerializerConfiguration serializationProviderConfiguration : serializationProviderConfigurations) {
            if (serializationProviderConfiguration.getType() == type) {
                if (result != null) {
                    throw new IllegalArgumentException("Duplicate " + type + " serialization provider : " + serializationProviderConfiguration);
                }
                result = serializationProviderConfiguration;
            }
        }
        return result;
    }
}
