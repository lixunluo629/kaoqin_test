package org.springframework.data.redis.core;

import java.util.List;
import java.util.Map;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundGeoOperations.class */
public interface BoundGeoOperations<K, M> extends BoundKeyOperations<K> {
    Long geoAdd(Point point, M m);

    Long geoAdd(RedisGeoCommands.GeoLocation<M> geoLocation);

    Long geoAdd(Map<M, Point> map);

    Long geoAdd(Iterable<RedisGeoCommands.GeoLocation<M>> iterable);

    Distance geoDist(M m, M m2);

    Distance geoDist(M m, M m2, Metric metric);

    List<String> geoHash(M... mArr);

    List<Point> geoPos(M... mArr);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle circle);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle circle, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K k, M m, double d);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M m, Distance distance);

    GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M m, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    Long geoRemove(M... mArr);
}
