package ch.qos.logback.core.joran.spi;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/joran/spi/JoranException.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/joran/spi/JoranException.class */
public class JoranException extends Exception {
    private static final long serialVersionUID = 1112493363728774021L;

    public JoranException(String msg) {
        super(msg);
    }

    public JoranException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
