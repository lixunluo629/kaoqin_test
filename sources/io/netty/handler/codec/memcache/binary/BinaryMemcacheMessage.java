package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.MemcacheMessage;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/memcache/binary/BinaryMemcacheMessage.class */
public interface BinaryMemcacheMessage extends MemcacheMessage {
    byte magic();

    BinaryMemcacheMessage setMagic(byte b);

    byte opcode();

    BinaryMemcacheMessage setOpcode(byte b);

    short keyLength();

    byte extrasLength();

    byte dataType();

    BinaryMemcacheMessage setDataType(byte b);

    int totalBodyLength();

    BinaryMemcacheMessage setTotalBodyLength(int i);

    int opaque();

    BinaryMemcacheMessage setOpaque(int i);

    long cas();

    BinaryMemcacheMessage setCas(long j);

    ByteBuf key();

    BinaryMemcacheMessage setKey(ByteBuf byteBuf);

    ByteBuf extras();

    BinaryMemcacheMessage setExtras(ByteBuf byteBuf);

    @Override // io.netty.handler.codec.memcache.MemcacheMessage, io.netty.util.ReferenceCounted
    BinaryMemcacheMessage retain();

    @Override // io.netty.handler.codec.memcache.MemcacheMessage, io.netty.util.ReferenceCounted
    BinaryMemcacheMessage retain(int i);

    @Override // io.netty.handler.codec.memcache.MemcacheMessage, io.netty.util.ReferenceCounted
    BinaryMemcacheMessage touch();

    @Override // io.netty.handler.codec.memcache.MemcacheMessage, io.netty.util.ReferenceCounted
    BinaryMemcacheMessage touch(Object obj);
}
