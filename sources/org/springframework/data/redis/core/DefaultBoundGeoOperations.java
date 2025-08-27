package org.springframework.data.redis.core;

import java.util.List;
import java.util.Map;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisGeoCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultBoundGeoOperations.class */
class DefaultBoundGeoOperations<K, M> extends DefaultBoundKeyOperations<K> implements BoundGeoOperations<K, M> {
    private final GeoOperations<K, M> ops;

    public DefaultBoundGeoOperations(K key, RedisOperations<K, M> operations) {
        super(key, operations);
        this.ops = operations.opsForGeo();
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Long geoAdd(Point point, M member) {
        return this.ops.geoAdd(getKey(), point, member);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Long geoAdd(RedisGeoCommands.GeoLocation<M> location) {
        return this.ops.geoAdd((GeoOperations<K, M>) getKey(), location);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Long geoAdd(Map<M, Point> memberCoordinateMap) {
        return this.ops.geoAdd((GeoOperations<K, M>) getKey(), memberCoordinateMap);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Long geoAdd(Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
        return this.ops.geoAdd((GeoOperations<K, M>) getKey(), locations);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Distance geoDist(M member1, M member2) {
        return this.ops.geoDist(getKey(), member1, member2);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Distance geoDist(M member1, M member2, Metric unit) {
        return this.ops.geoDist(getKey(), member1, member2, unit);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public List<String> geoHash(M... members) {
        return this.ops.geoHash(getKey(), members);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public List<Point> geoPos(M... members) {
        return this.ops.geoPos(getKey(), members);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle within) {
        return this.ops.geoRadius(getKey(), within);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle within, RedisGeoCommands.GeoRadiusCommandArgs param) {
        return this.ops.geoRadius(getKey(), within, param);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K key, M member, double radius) {
        return this.ops.geoRadiusByMember((GeoOperations<K, M>) getKey(), (K) member, radius);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M member, Distance distance) {
        return this.ops.geoRadiusByMember((GeoOperations<K, M>) getKey(), (K) member, distance);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs param) {
        return this.ops.geoRadiusByMember(getKey(), member, distance, param);
    }

    @Override // org.springframework.data.redis.core.BoundGeoOperations
    public Long geoRemove(M... members) {
        return this.ops.geoRemove(getKey(), members);
    }

    @Override // org.springframework.data.redis.core.BoundKeyOperations
    public DataType getType() {
        return DataType.ZSET;
    }
}
