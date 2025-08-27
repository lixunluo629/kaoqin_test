package ch.qos.logback.core;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/LogbackException.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/LogbackException.class */
public class LogbackException extends RuntimeException {
    private static final long serialVersionUID = -799956346239073266L;

    public LogbackException(String msg) {
        super(msg);
    }

    public LogbackException(String msg, Throwable nested) {
        super(msg, nested);
    }
}
