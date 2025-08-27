package io.netty.handler.ssl;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SniCompletionEvent.class */
public final class SniCompletionEvent extends SslCompletionEvent {
    private final String hostname;

    SniCompletionEvent(String hostname) {
        this.hostname = hostname;
    }

    SniCompletionEvent(String hostname, Throwable cause) {
        super(cause);
        this.hostname = hostname;
    }

    SniCompletionEvent(Throwable cause) {
        this(null, cause);
    }

    public String hostname() {
        return this.hostname;
    }

    @Override // io.netty.handler.ssl.SslCompletionEvent
    public String toString() {
        Throwable cause = cause();
        return cause == null ? getClass().getSimpleName() + "(SUCCESS='" + this.hostname + "'\")" : getClass().getSimpleName() + '(' + cause + ')';
    }
}
