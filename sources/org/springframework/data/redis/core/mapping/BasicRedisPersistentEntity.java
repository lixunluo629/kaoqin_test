package org.springframework.data.redis.core.mapping;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.core.mapping.BasicKeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.TimeToLiveAccessor;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/mapping/BasicRedisPersistentEntity.class */
public class BasicRedisPersistentEntity<T> extends BasicKeyValuePersistentEntity<T> implements RedisPersistentEntity<T> {
    private TimeToLiveAccessor timeToLiveAccessor;

    public BasicRedisPersistentEntity(TypeInformation<T> information, KeySpaceResolver fallbackKeySpaceResolver, TimeToLiveAccessor timeToLiveAccessor) {
        super(information, fallbackKeySpaceResolver);
        Assert.notNull(timeToLiveAccessor, "TimeToLiveAccessor must not be null");
        this.timeToLiveAccessor = timeToLiveAccessor;
    }

    @Override // org.springframework.data.redis.core.mapping.RedisPersistentEntity
    public TimeToLiveAccessor getTimeToLiveAccessor() {
        return this.timeToLiveAccessor;
    }

    @Override // org.springframework.data.redis.core.mapping.RedisPersistentEntity
    public boolean hasExplictTimeToLiveProperty() {
        return getExplicitTimeToLiveProperty() != null;
    }

    @Override // org.springframework.data.redis.core.mapping.RedisPersistentEntity
    public RedisPersistentProperty getExplicitTimeToLiveProperty() {
        return (RedisPersistentProperty) getPersistentProperty(TimeToLive.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.data.mapping.model.BasicPersistentEntity
    public KeyValuePersistentProperty returnPropertyIfBetterIdPropertyCandidateOrNull(KeyValuePersistentProperty property) {
        Assert.notNull(property, "Property must not be null!");
        if (!property.isIdProperty()) {
            return null;
        }
        KeyValuePersistentProperty currentIdProperty = getIdProperty();
        boolean currentIdPropertyIsSet = currentIdProperty != null;
        if (!currentIdPropertyIsSet) {
            return property;
        }
        boolean currentIdPropertyIsExplicit = currentIdProperty.isAnnotationPresent(Id.class);
        boolean newIdPropertyIsExplicit = property.isAnnotationPresent(Id.class);
        if (currentIdPropertyIsExplicit && newIdPropertyIsExplicit) {
            throw new MappingException(String.format("Attempt to add explicit id property %s but already have an property %s registered as explicit id. Check your mapping configuration!", property.getField(), currentIdProperty.getField()));
        }
        if (!currentIdPropertyIsExplicit && !newIdPropertyIsExplicit) {
            throw new MappingException(String.format("Attempt to add id property %s but already have an property %s registered as id. Check your mapping configuration!", property.getField(), currentIdProperty.getField()));
        }
        if (newIdPropertyIsExplicit) {
            return property;
        }
        return null;
    }
}
