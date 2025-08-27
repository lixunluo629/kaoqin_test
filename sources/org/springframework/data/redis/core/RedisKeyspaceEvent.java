package org.springframework.data.redis.core;

import org.springframework.context.ApplicationEvent;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyspaceEvent.class */
public class RedisKeyspaceEvent extends ApplicationEvent {
    private final String channel;

    public RedisKeyspaceEvent(byte[] key) {
        this(null, key);
    }

    public RedisKeyspaceEvent(String channel, byte[] key) {
        super(key);
        this.channel = channel;
    }

    @Override // java.util.EventObject
    public byte[] getSource() {
        return (byte[]) super.getSource();
    }

    public String getChannel() {
        return this.channel;
    }
}
