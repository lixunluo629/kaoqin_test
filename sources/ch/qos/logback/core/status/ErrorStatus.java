package ch.qos.logback.core.status;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/ErrorStatus.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/ErrorStatus.class */
public class ErrorStatus extends StatusBase {
    public ErrorStatus(String msg, Object origin) {
        super(2, msg, origin);
    }

    public ErrorStatus(String msg, Object origin, Throwable t) {
        super(2, msg, origin, t);
    }
}
