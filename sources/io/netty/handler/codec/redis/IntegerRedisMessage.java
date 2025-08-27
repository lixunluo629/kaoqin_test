package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/redis/IntegerRedisMessage.class */
public final class IntegerRedisMessage implements RedisMessage {
    private final long value;

    public IntegerRedisMessage(long value) {
        this.value = value;
    }

    public long value() {
        return this.value;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[value=" + this.value + ']';
    }
}
