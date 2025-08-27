package ch.qos.logback.core.util;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/util/IncompatibleClassException.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/util/IncompatibleClassException.class */
public class IncompatibleClassException extends Exception {
    private static final long serialVersionUID = -5823372159561159549L;
    Class<?> requestedClass;
    Class<?> obtainedClass;

    IncompatibleClassException(Class<?> requestedClass, Class<?> obtainedClass) {
        this.requestedClass = requestedClass;
        this.obtainedClass = obtainedClass;
    }
}
