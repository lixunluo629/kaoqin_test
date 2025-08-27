package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/spi/LoggerContextListener.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/spi/LoggerContextListener.class */
public interface LoggerContextListener {
    boolean isResetResistant();

    void onStart(LoggerContext loggerContext);

    void onReset(LoggerContext loggerContext);

    void onStop(LoggerContext loggerContext);

    void onLevelChange(Logger logger, Level level);
}
