package org.springframework.data.redis.core.index;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.index.RedisIndexDefinition;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/GeoIndexDefinition.class */
public class GeoIndexDefinition extends RedisIndexDefinition implements PathBasedRedisIndexDefinition {
    public GeoIndexDefinition(String keyspace, String path) {
        this(keyspace, path, path);
    }

    public GeoIndexDefinition(String keyspace, String path, String name) {
        super(keyspace, path, name);
        addCondition(new RedisIndexDefinition.PathCondition(path));
        setValueTransformer(new PointValueTransformer());
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/GeoIndexDefinition$PointValueTransformer.class */
    static class PointValueTransformer implements IndexValueTransformer {
        PointValueTransformer() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: merged with bridge method [inline-methods] */
        public Object convert2(Object source) {
            if (source == null || (source instanceof Point)) {
                return (Point) source;
            }
            if (source instanceof RedisGeoCommands.GeoLocation) {
                return ((RedisGeoCommands.GeoLocation) source).getPoint();
            }
            throw new IllegalArgumentException(String.format("Cannot convert %s to %s. GeoIndexed property needs to be of type Point or GeoLocation!", source.getClass(), Point.class));
        }
    }
}
