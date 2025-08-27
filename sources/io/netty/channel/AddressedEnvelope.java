package io.netty.channel;

import io.netty.util.ReferenceCounted;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/AddressedEnvelope.class */
public interface AddressedEnvelope<M, A extends SocketAddress> extends ReferenceCounted {
    M content();

    A sender();

    A recipient();

    @Override // io.netty.util.ReferenceCounted
    AddressedEnvelope<M, A> retain();

    @Override // io.netty.util.ReferenceCounted
    AddressedEnvelope<M, A> retain(int i);

    @Override // io.netty.util.ReferenceCounted
    AddressedEnvelope<M, A> touch();

    @Override // io.netty.util.ReferenceCounted
    AddressedEnvelope<M, A> touch(Object obj);
}
