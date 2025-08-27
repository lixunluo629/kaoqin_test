package org.springframework.data.redis.hash;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.core.convert.CustomConversions;
import org.springframework.data.redis.core.convert.IndexResolver;
import org.springframework.data.redis.core.convert.IndexedData;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.data.redis.core.convert.ReferenceResolver;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/ObjectHashMapper.class */
public class ObjectHashMapper implements HashMapper<Object, byte[], byte[]> {
    private final MappingRedisConverter converter;

    public ObjectHashMapper() {
        this(new CustomConversions());
    }

    public ObjectHashMapper(CustomConversions customConversions) {
        MappingRedisConverter mappingConverter = new MappingRedisConverter(new RedisMappingContext(), new NoOpIndexResolver(), new NoOpReferenceResolver());
        mappingConverter.setCustomConversions(customConversions == null ? new CustomConversions() : customConversions);
        mappingConverter.afterPropertiesSet();
        this.converter = mappingConverter;
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Map<byte[], byte[]> toHash(Object source) {
        if (source == null) {
            return Collections.emptyMap();
        }
        RedisData sink = new RedisData();
        this.converter.write(source, sink);
        return sink.getBucket().rawMap();
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Object fromHash(Map<byte[], byte[]> hash) {
        if (hash == null || hash.isEmpty()) {
            return null;
        }
        return this.converter.read(Object.class, new RedisData(hash));
    }

    public <T> T fromHash(Map<byte[], byte[]> map, Class<T> cls) {
        return (T) fromHash(map);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/ObjectHashMapper$NoOpReferenceResolver.class */
    private static class NoOpReferenceResolver implements ReferenceResolver {
        private static final Map<byte[], byte[]> NO_REFERENCE = Collections.emptyMap();

        private NoOpReferenceResolver() {
        }

        @Override // org.springframework.data.redis.core.convert.ReferenceResolver
        public Map<byte[], byte[]> resolveReference(Serializable id, String keyspace) {
            return NO_REFERENCE;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/ObjectHashMapper$NoOpIndexResolver.class */
    private static class NoOpIndexResolver implements IndexResolver {
        private static final Set<IndexedData> NO_INDEXES = Collections.emptySet();

        private NoOpIndexResolver() {
        }

        @Override // org.springframework.data.redis.core.convert.IndexResolver
        public Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, Object value) {
            return NO_INDEXES;
        }

        @Override // org.springframework.data.redis.core.convert.IndexResolver
        public Set<IndexedData> resolveIndexesFor(String keyspace, String path, TypeInformation<?> typeInformation, Object value) {
            return NO_INDEXES;
        }
    }
}
