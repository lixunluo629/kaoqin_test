package ch.qos.logback.core.util;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/util/InvocationGate.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/util/InvocationGate.class */
public interface InvocationGate {
    public static final long TIME_UNAVAILABLE = -1;

    boolean isTooSoon(long j);
}
