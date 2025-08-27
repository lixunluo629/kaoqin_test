package io.netty.handler.codec.redis;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/redis/AbstractStringRedisMessage.class */
public abstract class AbstractStringRedisMessage implements RedisMessage {
    private final String content;

    AbstractStringRedisMessage(String content) {
        this.content = (String) ObjectUtil.checkNotNull(content, "content");
    }

    public final String content() {
        return this.content;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[content=" + this.content + ']';
    }
}
