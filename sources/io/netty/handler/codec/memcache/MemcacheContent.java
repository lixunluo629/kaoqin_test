package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/memcache/MemcacheContent.class */
public interface MemcacheContent extends MemcacheObject, ByteBufHolder {
    @Override // io.netty.buffer.ByteBufHolder
    MemcacheContent copy();

    @Override // io.netty.buffer.ByteBufHolder
    MemcacheContent duplicate();

    @Override // io.netty.buffer.ByteBufHolder
    MemcacheContent retainedDuplicate();

    @Override // io.netty.buffer.ByteBufHolder
    MemcacheContent replace(ByteBuf byteBuf);

    MemcacheContent retain();

    MemcacheContent retain(int i);

    MemcacheContent touch();

    MemcacheContent touch(Object obj);
}
