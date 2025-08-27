package io.netty.util.internal.logging;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/FormattingTuple.class */
final class FormattingTuple {
    private final String message;
    private final Throwable throwable;

    FormattingTuple(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return this.message;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}
