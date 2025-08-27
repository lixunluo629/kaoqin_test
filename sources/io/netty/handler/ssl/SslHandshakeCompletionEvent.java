package io.netty.handler.ssl;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslHandshakeCompletionEvent.class */
public final class SslHandshakeCompletionEvent extends SslCompletionEvent {
    public static final SslHandshakeCompletionEvent SUCCESS = new SslHandshakeCompletionEvent();

    private SslHandshakeCompletionEvent() {
    }

    public SslHandshakeCompletionEvent(Throwable cause) {
        super(cause);
    }
}
