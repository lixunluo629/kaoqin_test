package org.springframework.data.redis.core;

import java.nio.charset.Charset;
import org.springframework.data.redis.core.convert.MappingRedisConverter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyExpiredEvent.class */
public class RedisKeyExpiredEvent<T> extends RedisKeyspaceEvent {
    static final Charset CHARSET = Charset.forName("UTF-8");
    private final MappingRedisConverter.BinaryKeyspaceIdentifier objectId;
    private final Object value;

    public RedisKeyExpiredEvent(byte[] key) {
        this(key, null);
    }

    public RedisKeyExpiredEvent(byte[] key, Object value) {
        this(null, key, value);
    }

    public RedisKeyExpiredEvent(String channel, byte[] key, Object value) {
        super(channel, key);
        if (MappingRedisConverter.BinaryKeyspaceIdentifier.isValid(key)) {
            this.objectId = MappingRedisConverter.BinaryKeyspaceIdentifier.of(key);
        } else {
            this.objectId = null;
        }
        this.value = value;
    }

    public String getKeyspace() {
        if (this.objectId != null) {
            return new String(this.objectId.getKeyspace(), CHARSET);
        }
        return null;
    }

    public byte[] getId() {
        return this.objectId != null ? this.objectId.getId() : getSource();
    }

    public Object getValue() {
        return this.value;
    }

    @Override // java.util.EventObject
    public String toString() {
        return "RedisKeyExpiredEvent [keyspace=" + getKeyspace() + ", id=" + getId() + "]";
    }
}
