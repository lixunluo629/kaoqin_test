package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/redis/BulkStringRedisContent.class */
public interface BulkStringRedisContent extends RedisMessage, ByteBufHolder {
    @Override // io.netty.buffer.ByteBufHolder
    BulkStringRedisContent copy();

    @Override // io.netty.buffer.ByteBufHolder
    BulkStringRedisContent duplicate();

    @Override // io.netty.buffer.ByteBufHolder
    BulkStringRedisContent retainedDuplicate();

    @Override // io.netty.buffer.ByteBufHolder
    BulkStringRedisContent replace(ByteBuf byteBuf);

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    BulkStringRedisContent retain();

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    BulkStringRedisContent retain(int i);

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    BulkStringRedisContent touch();

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    BulkStringRedisContent touch(Object obj);
}
