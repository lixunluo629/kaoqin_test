package ch.qos.logback.core.rolling;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.spi.LifeCycle;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/rolling/RollingPolicy.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/rolling/RollingPolicy.class */
public interface RollingPolicy extends LifeCycle {
    void rollover() throws RolloverFailure;

    String getActiveFileName();

    CompressionMode getCompressionMode();

    void setParent(FileAppender<?> fileAppender);
}
