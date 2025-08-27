package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterNodeResourceProvider.class */
public interface ClusterNodeResourceProvider {
    <S> S getResourceForSpecificNode(RedisClusterNode redisClusterNode);

    void returnResourceForSpecificNode(RedisClusterNode redisClusterNode, Object obj);
}
