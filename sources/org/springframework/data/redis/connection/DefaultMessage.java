package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultMessage.class */
public class DefaultMessage implements Message {
    private final byte[] channel;
    private final byte[] body;
    private String toString;

    public DefaultMessage(byte[] channel, byte[] body) {
        this.body = body;
        this.channel = channel;
    }

    @Override // org.springframework.data.redis.connection.Message
    public byte[] getChannel() {
        if (this.channel != null) {
            return (byte[]) this.channel.clone();
        }
        return null;
    }

    @Override // org.springframework.data.redis.connection.Message
    public byte[] getBody() {
        if (this.body != null) {
            return (byte[]) this.body.clone();
        }
        return null;
    }

    public String toString() {
        if (this.toString == null) {
            this.toString = new String(this.body);
        }
        return this.toString;
    }
}
