package org.springframework.data.redis.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultGeoOperations.class */
public class DefaultGeoOperations<K, M> extends AbstractOperations<K, M> implements GeoOperations<K, M> {
    @Override // org.springframework.data.redis.core.AbstractOperations
    public /* bridge */ /* synthetic */ RedisOperations getOperations() {
        return super.getOperations();
    }

    DefaultGeoOperations(RedisTemplate<K, M> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Long geoAdd(K key, final Point point, M member) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember = rawValue(member);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.geoAdd(rawKey, point, rawMember);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Long geoAdd(K key, RedisGeoCommands.GeoLocation<M> location) {
        return geoAdd(key, location.getPoint(), location.getName());
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Long geoAdd(K key, Map<M, Point> memberCoordinateMap) {
        final byte[] rawKey = rawKey(key);
        final Map<byte[], Point> rawMemberCoordinateMap = new HashMap<>();
        for (M member : memberCoordinateMap.keySet()) {
            byte[] rawMember = rawValue(member);
            rawMemberCoordinateMap.put(rawMember, memberCoordinateMap.get(member));
        }
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.geoAdd(rawKey, rawMemberCoordinateMap);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Long geoAdd(K key, Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
        Map<M, Point> memberCoordinateMap = new LinkedHashMap<>();
        for (RedisGeoCommands.GeoLocation<M> location : locations) {
            memberCoordinateMap.put(location.getName(), location.getPoint());
        }
        return geoAdd((DefaultGeoOperations<K, M>) key, memberCoordinateMap);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Distance geoDist(K key, M member1, M member2) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember1 = rawValue(member1);
        final byte[] rawMember2 = rawValue(member2);
        return (Distance) execute(new RedisCallback<Distance>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Distance doInRedis2(RedisConnection connection) {
                return connection.geoDist(rawKey, rawMember1, rawMember2);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Distance geoDist(K key, M member1, M member2, final Metric metric) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember1 = rawValue(member1);
        final byte[] rawMember2 = rawValue(member2);
        return (Distance) execute(new RedisCallback<Distance>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Distance doInRedis2(RedisConnection connection) {
                return connection.geoDist(rawKey, rawMember1, rawMember2, metric);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public List<String> geoHash(K key, M... members) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawMembers = rawValues(members);
        return (List) execute(new RedisCallback<List<String>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<String> doInRedis2(RedisConnection connection) {
                return connection.geoHash(rawKey, rawMembers);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public List<Point> geoPos(K key, M... members) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawMembers = rawValues(members);
        return (List) execute(new RedisCallback<List<Point>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<Point> doInRedis2(RedisConnection connection) {
                return connection.geoPos(rawKey, rawMembers);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(K key, final Circle within) {
        final byte[] rawKey = rawKey(key);
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = (GeoResults) execute(new RedisCallback<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> doInRedis2(RedisConnection connection) {
                return connection.geoRadius(rawKey, within);
            }
        }, true);
        return deserializeGeoResults(raw);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(K key, final Circle within, final RedisGeoCommands.GeoRadiusCommandArgs args) {
        final byte[] rawKey = rawKey(key);
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = (GeoResults) execute(new RedisCallback<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> doInRedis2(RedisConnection connection) {
                return connection.geoRadius(rawKey, within, args);
            }
        }, true);
        return deserializeGeoResults(raw);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K key, M member, final double radius) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember = rawValue(member);
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = (GeoResults) execute(new RedisCallback<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> doInRedis2(RedisConnection connection) {
                return connection.geoRadiusByMember(rawKey, rawMember, radius);
            }
        }, true);
        return deserializeGeoResults(raw);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K key, M member, final Distance distance) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember = rawValue(member);
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = (GeoResults) execute(new RedisCallback<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> doInRedis2(RedisConnection connection) {
                return connection.geoRadiusByMember(rawKey, rawMember, distance);
            }
        }, true);
        return deserializeGeoResults(raw);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K key, M member, final Distance distance, final RedisGeoCommands.GeoRadiusCommandArgs param) {
        final byte[] rawKey = rawKey(key);
        final byte[] rawMember = rawValue(member);
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = (GeoResults) execute(new RedisCallback<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> doInRedis2(RedisConnection connection) {
                return connection.geoRadiusByMember(rawKey, rawMember, distance, param);
            }
        }, true);
        return deserializeGeoResults(raw);
    }

    @Override // org.springframework.data.redis.core.GeoOperations
    public Long geoRemove(K key, M... members) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawMembers = rawValues(members);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultGeoOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.zRem(rawKey, rawMembers);
            }
        }, true);
    }
}
