package ch.qos.logback.core.joran.spi;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/spi/ActionException.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/spi/ActionException.class */
public class ActionException extends Exception {
    private static final long serialVersionUID = 2743349809995319806L;

    public ActionException() {
    }

    public ActionException(Throwable rootCause) {
        super(rootCause);
    }
}
