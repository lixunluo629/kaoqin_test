package io.netty.channel.unix;

import io.netty.channel.ChannelOption;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/unix/UnixChannelOption.class */
public class UnixChannelOption<T> extends ChannelOption<T> {
    public static final ChannelOption<Boolean> SO_REUSEPORT = valueOf(UnixChannelOption.class, "SO_REUSEPORT");
    public static final ChannelOption<DomainSocketReadMode> DOMAIN_SOCKET_READ_MODE = ChannelOption.valueOf(UnixChannelOption.class, "DOMAIN_SOCKET_READ_MODE");

    protected UnixChannelOption() {
        super(null);
    }
}
