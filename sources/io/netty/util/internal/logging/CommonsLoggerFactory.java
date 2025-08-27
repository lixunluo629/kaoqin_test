package io.netty.util.internal.logging;

import org.apache.commons.logging.LogFactory;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/CommonsLoggerFactory.class */
public class CommonsLoggerFactory extends InternalLoggerFactory {
    public static final InternalLoggerFactory INSTANCE = new CommonsLoggerFactory();

    @Deprecated
    public CommonsLoggerFactory() {
    }

    @Override // io.netty.util.internal.logging.InternalLoggerFactory
    public InternalLogger newInstance(String name) {
        return new CommonsLogger(LogFactory.getLog(name), name);
    }
}
