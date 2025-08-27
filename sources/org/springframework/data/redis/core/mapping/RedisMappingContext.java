package org.springframework.data.redis.core.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.MutablePersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.TimeToLiveAccessor;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisMappingContext.class */
public class RedisMappingContext extends KeyValueMappingContext {
    private final MappingConfiguration mappingConfiguration;
    private final TimeToLiveAccessor timeToLiveAccessor;
    private KeySpaceResolver fallbackKeySpaceResolver;

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ MutablePersistentEntity getPersistentEntity(TypeInformation typeInformation) {
        return getPersistentEntity((TypeInformation<?>) typeInformation);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ MutablePersistentEntity getPersistentEntity(Class cls) {
        return getPersistentEntity((Class<?>) cls);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ PersistentEntity getPersistentEntity(TypeInformation typeInformation) {
        return getPersistentEntity((TypeInformation<?>) typeInformation);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public /* bridge */ /* synthetic */ PersistentEntity getPersistentEntity(Class cls) {
        return getPersistentEntity((Class<?>) cls);
    }

    public RedisMappingContext() {
        this(new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration()));
    }

    public RedisMappingContext(MappingConfiguration mappingConfiguration) {
        this.mappingConfiguration = mappingConfiguration != null ? mappingConfiguration : new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration());
        setFallbackKeySpaceResolver(new ConfigAwareKeySpaceResolver(this.mappingConfiguration.getKeyspaceConfiguration()));
        this.timeToLiveAccessor = new ConfigAwareTimeToLiveAccessor(this.mappingConfiguration.getKeyspaceConfiguration(), this);
    }

    @Override // org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext
    public void setFallbackKeySpaceResolver(KeySpaceResolver fallbackKeySpaceResolver) {
        this.fallbackKeySpaceResolver = fallbackKeySpaceResolver;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext, org.springframework.data.mapping.context.AbstractMappingContext
    public <T> RedisPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
        return new BasicRedisPersistentEntity(typeInformation, this.fallbackKeySpaceResolver, this.timeToLiveAccessor);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public RedisPersistentEntity<?> getPersistentEntity(Class<?> type) {
        return (RedisPersistentEntity) super.getPersistentEntity(type);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public RedisPersistentEntity<?> getPersistentEntity(KeyValuePersistentProperty persistentProperty) {
        return (RedisPersistentEntity) super.getPersistentEntity((RedisMappingContext) persistentProperty);
    }

    @Override // org.springframework.data.mapping.context.AbstractMappingContext, org.springframework.data.mapping.context.MappingContext
    public RedisPersistentEntity<?> getPersistentEntity(TypeInformation<?> type) {
        return (RedisPersistentEntity) super.getPersistentEntity(type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext, org.springframework.data.mapping.context.AbstractMappingContext
    public KeyValuePersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor, KeyValuePersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
        return new RedisPersistentProperty(field, descriptor, owner, simpleTypeHolder);
    }

    public MappingConfiguration getMappingConfiguration() {
        return this.mappingConfiguration;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisMappingContext$ConfigAwareKeySpaceResolver.class */
    static class ConfigAwareKeySpaceResolver implements KeySpaceResolver {
        private final KeyspaceConfiguration keyspaceConfig;

        public ConfigAwareKeySpaceResolver(KeyspaceConfiguration keyspaceConfig) {
            this.keyspaceConfig = keyspaceConfig;
        }

        @Override // org.springframework.data.keyvalue.core.mapping.KeySpaceResolver
        public String resolveKeySpace(Class<?> type) {
            Assert.notNull(type, "Type must not be null!");
            if (this.keyspaceConfig.hasSettingsFor(type)) {
                String value = this.keyspaceConfig.getKeyspaceSettings(type).getKeyspace();
                if (StringUtils.hasText(value)) {
                    return value;
                }
            }
            return ClassNameKeySpaceResolver.INSTANCE.resolveKeySpace(type);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisMappingContext$ClassNameKeySpaceResolver.class */
    enum ClassNameKeySpaceResolver implements KeySpaceResolver {
        INSTANCE;

        @Override // org.springframework.data.keyvalue.core.mapping.KeySpaceResolver
        public String resolveKeySpace(Class<?> type) {
            Assert.notNull(type, "Type must not be null!");
            return ClassUtils.getUserClass(type).getName();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/RedisMappingContext$ConfigAwareTimeToLiveAccessor.class */
    static class ConfigAwareTimeToLiveAccessor implements TimeToLiveAccessor {
        private final Map<Class<?>, Long> defaultTimeouts;
        private final Map<Class<?>, PersistentProperty<?>> timeoutProperties;
        private final Map<Class<?>, Method> timeoutMethods;
        private final KeyspaceConfiguration keyspaceConfig;
        private final RedisMappingContext mappingContext;

        ConfigAwareTimeToLiveAccessor(KeyspaceConfiguration keyspaceConfig, RedisMappingContext mappingContext) {
            Assert.notNull(keyspaceConfig, "KeyspaceConfiguration must not be null!");
            Assert.notNull(mappingContext, "MappingContext must not be null!");
            this.defaultTimeouts = new HashMap();
            this.timeoutProperties = new HashMap();
            this.timeoutMethods = new HashMap();
            this.keyspaceConfig = keyspaceConfig;
            this.mappingContext = mappingContext;
        }

        @Override // org.springframework.data.redis.core.TimeToLiveAccessor
        public Long getTimeToLive(Object source) throws SecurityException, IllegalArgumentException {
            Class<?> target;
            Assert.notNull(source, "Source must not be null!");
            if (source instanceof Class) {
                target = (Class) source;
            } else {
                target = source instanceof PartialUpdate ? ((PartialUpdate) source).getTarget() : source.getClass();
            }
            Class<?> type = target;
            Long defaultTimeout = resolveDefaultTimeOut(type);
            TimeUnit unit = TimeUnit.SECONDS;
            PersistentProperty<?> ttlProperty = resolveTtlProperty(type);
            if (ttlProperty != null && ttlProperty.findAnnotation(TimeToLive.class) != null) {
                unit = ((TimeToLive) ttlProperty.findAnnotation(TimeToLive.class)).unit();
            }
            if (source instanceof PartialUpdate) {
                PartialUpdate<?> update = (PartialUpdate) source;
                if (ttlProperty != null && !update.getPropertyUpdates().isEmpty()) {
                    for (PartialUpdate.PropertyUpdate pUpdate : update.getPropertyUpdates()) {
                        if (PartialUpdate.UpdateCommand.SET.equals(pUpdate.getCmd()) && ttlProperty.getName().equals(pUpdate.getPropertyPath())) {
                            return Long.valueOf(TimeUnit.SECONDS.convert(((Long) NumberUtils.convertNumberToTargetClass((Number) pUpdate.getValue(), Long.class)).longValue(), unit));
                        }
                    }
                }
            } else if (ttlProperty != null) {
                RedisPersistentEntity entity = this.mappingContext.getPersistentEntity(type);
                Number timeout = (Number) entity.getPropertyAccessor(source).getProperty(ttlProperty);
                if (timeout != null) {
                    return Long.valueOf(TimeUnit.SECONDS.convert(timeout.longValue(), unit));
                }
            } else {
                Method timeoutMethod = resolveTimeMethod(type);
                if (timeoutMethod != null) {
                    TimeToLive ttl = (TimeToLive) AnnotationUtils.findAnnotation(timeoutMethod, TimeToLive.class);
                    try {
                        Number timeout2 = (Number) timeoutMethod.invoke(source, new Object[0]);
                        if (timeout2 != null) {
                            return Long.valueOf(TimeUnit.SECONDS.convert(timeout2.longValue(), ttl.unit()));
                        }
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Not allowed to access method '" + timeoutMethod.getName() + "': " + e.getMessage(), e);
                    } catch (IllegalArgumentException e2) {
                        throw new IllegalStateException("Cannot invoke method '" + timeoutMethod.getName() + " without arguments': " + e2.getMessage(), e2);
                    } catch (InvocationTargetException e3) {
                        throw new IllegalStateException("Cannot access method '" + timeoutMethod.getName() + "': " + e3.getMessage(), e3);
                    }
                }
            }
            return defaultTimeout;
        }

        private Long resolveDefaultTimeOut(Class<?> type) {
            if (this.defaultTimeouts.containsKey(type)) {
                return this.defaultTimeouts.get(type);
            }
            Long defaultTimeout = null;
            if (this.keyspaceConfig.hasSettingsFor(type)) {
                defaultTimeout = this.keyspaceConfig.getKeyspaceSettings(type).getTimeToLive();
            }
            RedisHash hash = (RedisHash) this.mappingContext.getPersistentEntity(type).findAnnotation(RedisHash.class);
            if (hash != null && hash.timeToLive() > 0) {
                defaultTimeout = Long.valueOf(hash.timeToLive());
            }
            this.defaultTimeouts.put(type, defaultTimeout);
            return defaultTimeout;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.Object, org.springframework.data.mapping.PersistentProperty, org.springframework.data.mapping.PersistentProperty<?>] */
        /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.Object, org.springframework.data.mapping.PersistentProperty, org.springframework.data.mapping.PersistentProperty<?>] */
        private PersistentProperty<?> resolveTtlProperty(Class<?> type) {
            if (this.timeoutProperties.containsKey(type)) {
                return this.timeoutProperties.get(type);
            }
            RedisPersistentEntity entity = this.mappingContext.getPersistentEntity(type);
            ?? persistentProperty = entity.getPersistentProperty(TimeToLive.class);
            if (persistentProperty != 0) {
                this.timeoutProperties.put(type, persistentProperty);
                return persistentProperty;
            }
            if (this.keyspaceConfig.hasSettingsFor(type)) {
                KeyspaceConfiguration.KeyspaceSettings settings = this.keyspaceConfig.getKeyspaceSettings(type);
                if (StringUtils.hasText(settings.getTimeToLivePropertyName())) {
                    ?? persistentProperty2 = entity.getPersistentProperty(settings.getTimeToLivePropertyName());
                    this.timeoutProperties.put(type, persistentProperty2);
                    return persistentProperty2;
                }
            }
            this.timeoutProperties.put(type, null);
            return null;
        }

        private Method resolveTimeMethod(final Class<?> type) throws SecurityException, IllegalArgumentException {
            if (this.timeoutMethods.containsKey(type)) {
                return this.timeoutMethods.get(type);
            }
            this.timeoutMethods.put(type, null);
            ReflectionUtils.doWithMethods(type, new ReflectionUtils.MethodCallback() { // from class: org.springframework.data.redis.core.mapping.RedisMappingContext.ConfigAwareTimeToLiveAccessor.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                    ConfigAwareTimeToLiveAccessor.this.timeoutMethods.put(type, method);
                }
            }, new ReflectionUtils.MethodFilter() { // from class: org.springframework.data.redis.core.mapping.RedisMappingContext.ConfigAwareTimeToLiveAccessor.2
                @Override // org.springframework.util.ReflectionUtils.MethodFilter
                public boolean matches(Method method) {
                    return ClassUtils.isAssignable(Number.class, method.getReturnType()) && AnnotationUtils.findAnnotation(method, TimeToLive.class) != null;
                }
            });
            return this.timeoutMethods.get(type);
        }
    }
}
