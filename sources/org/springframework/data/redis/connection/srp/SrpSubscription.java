package org.springframework.data.redis.connection.srp;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.util.AbstractSubscription;
import redis.client.RedisClient;
import redis.client.ReplyListener;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpSubscription.class */
class SrpSubscription extends AbstractSubscription {
    private final RedisClient client;
    private final ReplyListener listener;

    SrpSubscription(MessageListener listener, RedisClient client) {
        super(listener);
        this.client = client;
        this.listener = new SrpMessageListener(listener);
        client.addListener(this.listener);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doClose() {
        if (!getChannels().isEmpty()) {
            this.client.unsubscribe((Object[]) null);
        }
        if (!getPatterns().isEmpty()) {
            this.client.punsubscribe((Object[]) null);
        }
        this.client.removeListener(this.listener);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPsubscribe(byte[]... patterns) {
        this.client.psubscribe(patterns);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPUnsubscribe(boolean all, byte[]... patterns) {
        if (all) {
            this.client.punsubscribe((Object[]) null);
        } else {
            this.client.punsubscribe(patterns);
        }
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doSubscribe(byte[]... channels) {
        this.client.subscribe(channels);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doUnsubscribe(boolean all, byte[]... channels) {
        if (all) {
            this.client.unsubscribe((Object[]) null);
        } else {
            this.client.unsubscribe(channels);
        }
    }
}
