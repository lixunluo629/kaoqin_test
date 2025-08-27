package io.netty.channel;

import java.nio.channels.ClosedChannelException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ExtendedClosedChannelException.class */
final class ExtendedClosedChannelException extends ClosedChannelException {
    ExtendedClosedChannelException(Throwable cause) {
        if (cause != null) {
            initCause(cause);
        }
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }
}
