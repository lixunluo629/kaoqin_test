package io.netty.channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/EventLoopException.class */
public class EventLoopException extends ChannelException {
    private static final long serialVersionUID = -8969100344583703616L;

    public EventLoopException() {
    }

    public EventLoopException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventLoopException(String message) {
        super(message);
    }

    public EventLoopException(Throwable cause) {
        super(cause);
    }
}
