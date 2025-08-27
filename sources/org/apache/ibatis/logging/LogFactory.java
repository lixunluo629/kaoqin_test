package org.apache.ibatis.logging;

import java.lang.reflect.Constructor;
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/LogFactory.class */
public final class LogFactory {
    public static final String MARKER = "MYBATIS";
    private static Constructor<? extends Log> logConstructor;

    static {
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.1
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useSlf4jLogging();
            }
        });
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.2
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useCommonsLogging();
            }
        });
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.3
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useLog4J2Logging();
            }
        });
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.4
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useLog4JLogging();
            }
        });
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.5
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useJdkLogging();
            }
        });
        tryImplementation(new Runnable() { // from class: org.apache.ibatis.logging.LogFactory.6
            @Override // java.lang.Runnable
            public void run() {
                LogFactory.useNoLogging();
            }
        });
    }

    private LogFactory() {
    }

    public static Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public static Log getLog(String logger) {
        try {
            return logConstructor.newInstance(logger);
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
        setImplementation(clazz);
    }

    public static synchronized void useSlf4jLogging() {
        setImplementation(Slf4jImpl.class);
    }

    public static synchronized void useCommonsLogging() {
        setImplementation(JakartaCommonsLoggingImpl.class);
    }

    public static synchronized void useLog4JLogging() {
        setImplementation(Log4jImpl.class);
    }

    public static synchronized void useLog4J2Logging() {
        setImplementation(Log4j2Impl.class);
    }

    public static synchronized void useJdkLogging() {
        setImplementation(Jdk14LoggingImpl.class);
    }

    public static synchronized void useStdOutLogging() {
        setImplementation(StdOutImpl.class);
    }

    public static synchronized void useNoLogging() {
        setImplementation(NoLoggingImpl.class);
    }

    private static void tryImplementation(Runnable runnable) {
        if (logConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable th) {
            }
        }
    }

    private static void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation.  Cause: " + t, t);
        }
    }
}
