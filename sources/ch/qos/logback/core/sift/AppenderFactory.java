package ch.qos.logback.core.sift;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/sift/AppenderFactory.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/sift/AppenderFactory.class */
public interface AppenderFactory<E> {
    Appender<E> buildAppender(Context context, String str) throws JoranException;
}
