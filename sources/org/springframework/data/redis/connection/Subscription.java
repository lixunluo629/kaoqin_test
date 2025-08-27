package org.springframework.data.redis.connection;

import java.util.Collection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/Subscription.class */
public interface Subscription {
    void subscribe(byte[]... bArr) throws RedisInvalidSubscriptionException;

    void pSubscribe(byte[]... bArr) throws RedisInvalidSubscriptionException;

    void unsubscribe();

    void unsubscribe(byte[]... bArr);

    void pUnsubscribe();

    void pUnsubscribe(byte[]... bArr);

    Collection<byte[]> getChannels();

    Collection<byte[]> getPatterns();

    MessageListener getListener();

    boolean isAlive();

    void close();
}
