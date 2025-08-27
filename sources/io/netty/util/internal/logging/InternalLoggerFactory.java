package io.netty.util.internal.logging;

import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/logging/InternalLoggerFactory.class */
public abstract class InternalLoggerFactory {
    private static volatile InternalLoggerFactory defaultFactory;

    protected abstract InternalLogger newInstance(String str);

    private static InternalLoggerFactory newDefaultFactory(String name) {
        InternalLoggerFactory f;
        try {
            f = new Slf4JLoggerFactory(true);
            f.newInstance(name).debug("Using SLF4J as the default logging framework");
        } catch (Throwable th) {
            try {
                f = Log4J2LoggerFactory.INSTANCE;
                f.newInstance(name).debug("Using Log4J2 as the default logging framework");
            } catch (Throwable th2) {
                try {
                    f = Log4JLoggerFactory.INSTANCE;
                    f.newInstance(name).debug("Using Log4J as the default logging framework");
                } catch (Throwable th3) {
                    f = JdkLoggerFactory.INSTANCE;
                    f.newInstance(name).debug("Using java.util.logging as the default logging framework");
                }
            }
        }
        return f;
    }

    public static InternalLoggerFactory getDefaultFactory() {
        if (defaultFactory == null) {
            defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
        }
        return defaultFactory;
    }

    public static void setDefaultFactory(InternalLoggerFactory defaultFactory2) {
        defaultFactory = (InternalLoggerFactory) ObjectUtil.checkNotNull(defaultFactory2, "defaultFactory");
    }

    public static InternalLogger getInstance(Class<?> clazz) {
        return getInstance(clazz.getName());
    }

    public static InternalLogger getInstance(String name) {
        return getDefaultFactory().newInstance(name);
    }
}
