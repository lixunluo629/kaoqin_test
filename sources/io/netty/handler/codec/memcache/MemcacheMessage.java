package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/memcache/MemcacheMessage.class */
public interface MemcacheMessage extends MemcacheObject, ReferenceCounted {
    MemcacheMessage retain();

    MemcacheMessage retain(int i);

    MemcacheMessage touch();

    MemcacheMessage touch(Object obj);
}
