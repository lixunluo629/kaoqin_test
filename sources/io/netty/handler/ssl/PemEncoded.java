package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/PemEncoded.class */
interface PemEncoded extends ByteBufHolder {
    boolean isSensitive();

    @Override // io.netty.buffer.ByteBufHolder
    PemEncoded copy();

    @Override // io.netty.buffer.ByteBufHolder
    PemEncoded duplicate();

    @Override // io.netty.buffer.ByteBufHolder
    PemEncoded retainedDuplicate();

    @Override // io.netty.buffer.ByteBufHolder
    PemEncoded replace(ByteBuf byteBuf);

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    PemEncoded retain();

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    PemEncoded retain(int i);

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    PemEncoded touch();

    @Override // io.netty.buffer.ByteBufHolder, io.netty.util.ReferenceCounted
    PemEncoded touch(Object obj);
}
