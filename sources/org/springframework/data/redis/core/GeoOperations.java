package org.springframework.data.redis.core;

import java.util.List;
import java.util.Map;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/GeoOperations.class */
public interface GeoOperations<K, M> {
    Long geoAdd(K k, Point point, M m);

    Long geoAdd(K k, RedisGeoCommands.GeoLocation<M> geoLocation);

    Long geoAdd(K k, Map<M, Point> map);

    Long geoAdd(K k, Iterable<RedisGeoCommands.GeoLocation<M>> iterable);

    Distance geoDist(K k, M m, M m2);

    Distance geoDist(K k, M m, M m2, Metric metric);

    List<String> geoHash(K k, M... mArr);

    List<Point> geoPos(K k, M... mArr);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(K k, Circle circle);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(K k, Circle circle, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K k, M m, double d);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K k, M m, Distance distance);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K k, M m, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    Long geoRemove(K k, M... mArr);
}
