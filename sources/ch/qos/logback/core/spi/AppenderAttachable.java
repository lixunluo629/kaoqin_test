package ch.qos.logback.core.spi;

import ch.qos.logback.core.Appender;
import java.util.Iterator;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/AppenderAttachable.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/AppenderAttachable.class */
public interface AppenderAttachable<E> {
    void addAppender(Appender<E> appender);

    Iterator<Appender<E>> iteratorForAppenders();

    Appender<E> getAppender(String str);

    boolean isAttached(Appender<E> appender);

    void detachAndStopAllAppenders();

    boolean detachAppender(Appender<E> appender);

    boolean detachAppender(String str);
}
