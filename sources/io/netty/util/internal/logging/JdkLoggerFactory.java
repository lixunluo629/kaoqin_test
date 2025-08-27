package io.netty.util.internal.logging;

import java.util.logging.Logger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/JdkLoggerFactory.class */
public class JdkLoggerFactory extends InternalLoggerFactory {
    public static final InternalLoggerFactory INSTANCE = new JdkLoggerFactory();

    @Deprecated
    public JdkLoggerFactory() {
    }

    @Override // io.netty.util.internal.logging.InternalLoggerFactory
    public InternalLogger newInstance(String name) {
        return new JdkLogger(Logger.getLogger(name));
    }
}
