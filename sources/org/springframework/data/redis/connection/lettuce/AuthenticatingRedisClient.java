package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.resource.ClientResources;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/AuthenticatingRedisClient.class */
public class AuthenticatingRedisClient extends RedisClient {
    public AuthenticatingRedisClient(String host, int port, String password) {
        super((ClientResources) null, RedisURI.builder().withHost(host).withPort(port).withPassword(password).build());
    }

    public AuthenticatingRedisClient(String host, String password) {
        super((ClientResources) null, RedisURI.builder().withHost(host).withPassword(password).build());
    }

    public <K, V> StatefulRedisConnection<K, V> connect(RedisCodec<K, V> codec) {
        return super.connect(codec);
    }

    public <K, V> RedisAsyncCommands<K, V> connectAsync(RedisCodec<K, V> codec) {
        return super.connectAsync(codec);
    }

    public <K, V> StatefulRedisPubSubConnection<K, V> connectPubSub(RedisCodec<K, V> codec) {
        return super.connectPubSub(codec);
    }
}
