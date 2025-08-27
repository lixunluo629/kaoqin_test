package io.netty.channel.socket;

import java.io.IOException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/socket/ChannelOutputShutdownException.class */
public final class ChannelOutputShutdownException extends IOException {
    private static final long serialVersionUID = 6712549938359321378L;

    public ChannelOutputShutdownException(String msg) {
        super(msg);
    }

    public ChannelOutputShutdownException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
