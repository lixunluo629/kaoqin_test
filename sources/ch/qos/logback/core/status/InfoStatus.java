package ch.qos.logback.core.status;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/InfoStatus.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/InfoStatus.class */
public class InfoStatus extends StatusBase {
    public InfoStatus(String msg, Object origin) {
        super(0, msg, origin);
    }

    public InfoStatus(String msg, Object origin, Throwable t) {
        super(0, msg, origin, t);
    }
}
