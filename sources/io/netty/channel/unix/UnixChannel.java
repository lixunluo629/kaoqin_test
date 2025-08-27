package io.netty.channel.unix;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/UnixChannel.class */
public interface UnixChannel extends Channel {
    FileDescriptor fd();
}
