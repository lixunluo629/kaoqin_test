package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/redis/RedisMessagePool.class */
public interface RedisMessagePool {
    SimpleStringRedisMessage getSimpleString(String str);

    SimpleStringRedisMessage getSimpleString(ByteBuf byteBuf);

    ErrorRedisMessage getError(String str);

    ErrorRedisMessage getError(ByteBuf byteBuf);

    IntegerRedisMessage getInteger(long j);

    IntegerRedisMessage getInteger(ByteBuf byteBuf);

    byte[] getByteBufOfInteger(long j);
}
