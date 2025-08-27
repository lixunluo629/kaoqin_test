package io.netty.handler.logging;

import io.netty.util.internal.logging.InternalLogLevel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/logging/LogLevel.class */
public enum LogLevel {
    TRACE(InternalLogLevel.TRACE),
    DEBUG(InternalLogLevel.DEBUG),
    INFO(InternalLogLevel.INFO),
    WARN(InternalLogLevel.WARN),
    ERROR(InternalLogLevel.ERROR);

    private final InternalLogLevel internalLevel;

    LogLevel(InternalLogLevel internalLevel) {
        this.internalLevel = internalLevel;
    }

    public InternalLogLevel toInternalLevel() {
        return this.internalLevel;
    }
}
