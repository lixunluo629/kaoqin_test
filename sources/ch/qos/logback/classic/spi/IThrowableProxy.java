package ch.qos.logback.classic.spi;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/spi/IThrowableProxy.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/spi/IThrowableProxy.class */
public interface IThrowableProxy {
    String getMessage();

    String getClassName();

    StackTraceElementProxy[] getStackTraceElementProxyArray();

    int getCommonFrames();

    IThrowableProxy getCause();

    IThrowableProxy[] getSuppressed();
}
