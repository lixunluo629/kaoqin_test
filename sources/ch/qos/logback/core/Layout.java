package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/Layout.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/Layout.class */
public interface Layout<E> extends ContextAware, LifeCycle {
    String doLayout(E e);

    String getFileHeader();

    String getPresentationHeader();

    String getPresentationFooter();

    String getFileFooter();

    String getContentType();
}
