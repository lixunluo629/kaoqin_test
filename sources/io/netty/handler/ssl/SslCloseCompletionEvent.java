package io.netty.handler.ssl;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslCloseCompletionEvent.class */
public final class SslCloseCompletionEvent extends SslCompletionEvent {
    public static final SslCloseCompletionEvent SUCCESS = new SslCloseCompletionEvent();

    private SslCloseCompletionEvent() {
    }

    public SslCloseCompletionEvent(Throwable cause) {
        super(cause);
    }
}
