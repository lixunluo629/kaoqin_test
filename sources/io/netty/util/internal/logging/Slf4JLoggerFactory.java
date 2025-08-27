package io.netty.util.internal.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/Slf4JLoggerFactory.class */
public class Slf4JLoggerFactory extends InternalLoggerFactory {
    public static final InternalLoggerFactory INSTANCE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Slf4JLoggerFactory.class.desiredAssertionStatus();
        INSTANCE = new Slf4JLoggerFactory();
    }

    @Deprecated
    public Slf4JLoggerFactory() {
    }

    Slf4JLoggerFactory(boolean failIfNOP) {
        if (!$assertionsDisabled && !failIfNOP) {
            throw new AssertionError();
        }
        if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new NoClassDefFoundError("NOPLoggerFactory not supported");
        }
    }

    @Override // io.netty.util.internal.logging.InternalLoggerFactory
    public InternalLogger newInstance(String name) {
        return wrapLogger(LoggerFactory.getLogger(name));
    }

    static InternalLogger wrapLogger(Logger logger) {
        return logger instanceof LocationAwareLogger ? new LocationAwareSlf4JLogger((LocationAwareLogger) logger) : new Slf4JLogger(logger);
    }
}
