package org.apache.commons.logging;

/* JADX WARN: Classes with same name are omitted:
  commons-logging-1.0.4.jar:org/apache/commons/logging/Log.class
 */
/* loaded from: jcl-over-slf4j-1.7.26.jar:org/apache/commons/logging/Log.class */
public interface Log {
    boolean isDebugEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

    boolean isInfoEnabled();

    boolean isTraceEnabled();

    boolean isWarnEnabled();

    void trace(Object obj);

    void trace(Object obj, Throwable th);

    void debug(Object obj);

    void debug(Object obj, Throwable th);

    void info(Object obj);

    void info(Object obj, Throwable th);

    void warn(Object obj);

    void warn(Object obj, Throwable th);

    void error(Object obj);

    void error(Object obj, Throwable th);

    void fatal(Object obj);

    void fatal(Object obj, Throwable th);
}
