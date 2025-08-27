package org.springframework.data.redis.connection.srp;

import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.Assert;
import redis.client.ReplyListener;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpMessageListener.class */
class SrpMessageListener implements ReplyListener {
    private final MessageListener listener;

    SrpMessageListener(MessageListener listener) {
        Assert.notNull(listener, "message listener is required");
        this.listener = listener;
    }

    public void message(byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), null);
    }

    public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
        this.listener.onMessage(new DefaultMessage(channel, message), pattern);
    }

    public void psubscribed(byte[] arg0, int arg1) {
    }

    public void punsubscribed(byte[] arg0, int arg1) {
    }

    public void subscribed(byte[] arg0, int arg1) {
    }

    public void unsubscribed(byte[] arg0, int arg1) {
    }
}
