package io.netty.handler.ssl;

import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/SslCompletionEvent.class */
public abstract class SslCompletionEvent {
    private final Throwable cause;

    SslCompletionEvent() {
        this.cause = null;
    }

    SslCompletionEvent(Throwable cause) {
        this.cause = (Throwable) ObjectUtil.checkNotNull(cause, "cause");
    }

    public final boolean isSuccess() {
        return this.cause == null;
    }

    public final Throwable cause() {
        return this.cause;
    }

    public String toString() {
        Throwable cause = cause();
        return cause == null ? getClass().getSimpleName() + "(SUCCESS)" : getClass().getSimpleName() + '(' + cause + ')';
    }
}
