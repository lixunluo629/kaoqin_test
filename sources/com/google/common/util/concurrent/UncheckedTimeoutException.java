package com.google.common.util.concurrent;

import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/UncheckedTimeoutException.class */
public class UncheckedTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public UncheckedTimeoutException() {
    }

    public UncheckedTimeoutException(@Nullable String message) {
        super(message);
    }

    public UncheckedTimeoutException(@Nullable Throwable cause) {
        super(cause);
    }

    public UncheckedTimeoutException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
