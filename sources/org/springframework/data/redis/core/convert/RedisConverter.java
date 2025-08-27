package org.springframework.data.redis.core.convert;

import org.springframework.data.convert.EntityConverter;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.redis.core.mapping.RedisMappingContext;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/RedisConverter.class */
public interface RedisConverter extends EntityConverter<KeyValuePersistentEntity<?>, KeyValuePersistentProperty, Object, RedisData> {
    @Override // org.springframework.data.convert.EntityConverter
    RedisMappingContext getMappingContext();
}
