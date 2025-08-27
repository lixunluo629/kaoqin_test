package org.springframework.data.redis.connection.jedis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.util.AbstractSubscription;
import redis.clients.jedis.BinaryJedisPubSub;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisSubscription.class */
class JedisSubscription extends AbstractSubscription {
    private final BinaryJedisPubSub jedisPubSub;

    JedisSubscription(MessageListener listener, BinaryJedisPubSub jedisPubSub, byte[][] channels, byte[][] patterns) {
        super(listener, channels, patterns);
        this.jedisPubSub = jedisPubSub;
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doClose() {
        if (!getChannels().isEmpty()) {
            this.jedisPubSub.unsubscribe();
        }
        if (!getPatterns().isEmpty()) {
            this.jedisPubSub.punsubscribe();
        }
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPsubscribe(byte[]... patterns) {
        this.jedisPubSub.psubscribe(patterns);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPUnsubscribe(boolean all, byte[]... patterns) {
        if (all) {
            this.jedisPubSub.punsubscribe();
        } else {
            this.jedisPubSub.punsubscribe(patterns);
        }
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doSubscribe(byte[]... channels) {
        this.jedisPubSub.subscribe(channels);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doUnsubscribe(boolean all, byte[]... channels) {
        if (all) {
            this.jedisPubSub.unsubscribe();
        } else {
            this.jedisPubSub.unsubscribe(channels);
        }
    }
}
