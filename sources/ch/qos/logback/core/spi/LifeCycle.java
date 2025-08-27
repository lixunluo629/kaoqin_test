package ch.qos.logback.core.spi;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/LifeCycle.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/LifeCycle.class */
public interface LifeCycle {
    void start();

    void stop();

    boolean isStarted();
}
