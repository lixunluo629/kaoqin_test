package io.netty.util.internal.logging;

import org.apache.logging.log4j.LogManager;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/Log4J2LoggerFactory.class */
public final class Log4J2LoggerFactory extends InternalLoggerFactory {
    public static final InternalLoggerFactory INSTANCE = new Log4J2LoggerFactory();

    @Deprecated
    public Log4J2LoggerFactory() {
    }

    @Override // io.netty.util.internal.logging.InternalLoggerFactory
    public InternalLogger newInstance(String name) {
        return new Log4J2Logger(LogManager.getLogger(name));
    }
}
