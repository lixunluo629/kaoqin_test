package ch.qos.logback.core.status;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/status/WarnStatus.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/status/WarnStatus.class */
public class WarnStatus extends StatusBase {
    public WarnStatus(String msg, Object origin) {
        super(1, msg, origin);
    }

    public WarnStatus(String msg, Object origin, Throwable t) {
        super(1, msg, origin, t);
    }
}
