package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.util.AbstractSubscription;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceSubscription.class */
class LettuceSubscription extends AbstractSubscription {
    final StatefulRedisPubSubConnection<byte[], byte[]> pubsub;
    private LettuceMessageListener listener;

    LettuceSubscription(MessageListener listener, StatefulRedisPubSubConnection<byte[], byte[]> pubsubConnection) {
        super(listener);
        this.pubsub = pubsubConnection;
        this.listener = new LettuceMessageListener(listener);
        this.pubsub.addListener(this.listener);
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [byte[], java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r1v5, types: [byte[], java.lang.Object[]] */
    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doClose() {
        if (!getChannels().isEmpty()) {
            this.pubsub.sync().unsubscribe((Object[]) new byte[]{new byte[0]});
        }
        if (!getPatterns().isEmpty()) {
            this.pubsub.sync().punsubscribe((Object[]) new byte[]{new byte[0]});
        }
        this.pubsub.removeListener(this.listener);
        this.pubsub.close();
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPsubscribe(byte[]... patterns) {
        this.pubsub.sync().psubscribe(patterns);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doPUnsubscribe(boolean all, byte[]... patterns) {
        this.pubsub.sync().punsubscribe(patterns);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doSubscribe(byte[]... channels) {
        this.pubsub.sync().subscribe(channels);
    }

    @Override // org.springframework.data.redis.connection.util.AbstractSubscription
    protected void doUnsubscribe(boolean all, byte[]... channels) {
        this.pubsub.sync().unsubscribe(channels);
    }
}
