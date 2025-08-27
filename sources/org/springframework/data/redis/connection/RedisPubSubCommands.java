package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisPubSubCommands.class */
public interface RedisPubSubCommands {
    boolean isSubscribed();

    Subscription getSubscription();

    Long publish(byte[] bArr, byte[] bArr2);

    void subscribe(MessageListener messageListener, byte[]... bArr);

    void pSubscribe(MessageListener messageListener, byte[]... bArr);
}
