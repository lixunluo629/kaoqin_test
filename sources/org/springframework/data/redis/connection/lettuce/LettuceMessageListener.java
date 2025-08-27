package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.pubsub.RedisPubSubListener;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceMessageListener.class */
class LettuceMessageListener implements RedisPubSubListener<byte[], byte[]> {
    private final MessageListener listener;

    LettuceMessageListener(MessageListener listener) {
        Assert.notNull(listener, "message listener is required");
        this.listener = listener;
    }

    public void message(byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), null);
    }

    public void message(byte[] pattern, byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), pattern);
    }

    public void subscribed(byte[] channel, long count) {
    }

    public void psubscribed(byte[] pattern, long count) {
    }

    public void unsubscribed(byte[] channel, long count) {
    }

    public void punsubscribed(byte[] pattern, long count) {
    }
}
