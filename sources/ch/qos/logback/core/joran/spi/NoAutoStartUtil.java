package ch.qos.logback.core.joran.spi;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/spi/NoAutoStartUtil.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/spi/NoAutoStartUtil.class */
public class NoAutoStartUtil {
    public static boolean notMarkedWithNoAutoStart(Object o) {
        if (o == null) {
            return false;
        }
        Class<?> clazz = o.getClass();
        NoAutoStart a = (NoAutoStart) clazz.getAnnotation(NoAutoStart.class);
        return a == null;
    }
}
