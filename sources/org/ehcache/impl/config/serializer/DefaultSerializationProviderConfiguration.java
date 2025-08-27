package org.ehcache.impl.config.serializer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.ehcache.core.spi.service.FileBasedPersistenceContext;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/serializer/DefaultSerializationProviderConfiguration.class */
public class DefaultSerializationProviderConfiguration implements ServiceCreationConfiguration<SerializationProvider> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) DefaultSerializationProviderConfiguration.class);
    private final Map<Class<?>, Class<? extends Serializer<?>>> defaultSerializers = new LinkedHashMap();

    public DefaultSerializationProviderConfiguration() {
    }

    public DefaultSerializationProviderConfiguration(DefaultSerializationProviderConfiguration other) {
        this.defaultSerializers.putAll(other.defaultSerializers);
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<SerializationProvider> getServiceType() {
        return SerializationProvider.class;
    }

    public <T> DefaultSerializationProviderConfiguration addSerializerFor(Class<T> serializableClass, Class<? extends Serializer<T>> serializerClass) {
        return addSerializerFor(serializableClass, serializerClass, false);
    }

    public <T> DefaultSerializationProviderConfiguration addSerializerFor(Class<T> serializableClass, Class<? extends Serializer<T>> serializerClass, boolean overwrite) {
        if (serializableClass == null) {
            throw new NullPointerException("Serializable class cannot be null");
        }
        if (serializerClass == null) {
            throw new NullPointerException("Serializer class cannot be null");
        }
        if (!isConstructorPresent(serializerClass, ClassLoader.class)) {
            throw new IllegalArgumentException("The serializer: " + serializerClass.getName() + " does not have a constructor that takes in a ClassLoader.");
        }
        if (isConstructorPresent(serializerClass, ClassLoader.class, FileBasedPersistenceContext.class)) {
            LOGGER.warn(serializerClass.getName() + " class has a constructor that takes in a FileBasedPersistenceContext. Support for this constructor has been removed since version 3.2. Consider removing it.");
        }
        if (this.defaultSerializers.containsKey(serializableClass) && !overwrite) {
            throw new IllegalArgumentException("Duplicate serializer for class : " + serializableClass.getName());
        }
        this.defaultSerializers.put(serializableClass, serializerClass);
        return this;
    }

    private static boolean isConstructorPresent(Class<?> clazz, Class<?>... args) throws NoSuchMethodException, SecurityException {
        try {
            clazz.getConstructor(args);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Map<Class<?>, Class<? extends Serializer<?>>> getDefaultSerializers() {
        return Collections.unmodifiableMap(this.defaultSerializers);
    }
}
