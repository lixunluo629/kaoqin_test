package org.hibernate.validator.internal.util.logging;

import org.jboss.logging.Logger;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/logging/LoggerFactory.class */
public final class LoggerFactory {
    public static Log make() {
        Throwable t = new Throwable();
        StackTraceElement directCaller = t.getStackTrace()[1];
        return (Log) Logger.getMessageLogger(Log.class, directCaller.getClassName());
    }

    private LoggerFactory() {
    }
}
