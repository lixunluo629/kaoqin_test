package org.springframework.data.redis.connection.jedis;

import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.Assert;
import redis.clients.jedis.BinaryJedisPubSub;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisMessageListener.class */
class JedisMessageListener extends BinaryJedisPubSub {
    private final MessageListener listener;

    JedisMessageListener(MessageListener listener) {
        Assert.notNull(listener, "message listener is required");
        this.listener = listener;
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onMessage(byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), null);
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), pattern);
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onSubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override // redis.clients.jedis.BinaryJedisPubSub
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
    }
}
