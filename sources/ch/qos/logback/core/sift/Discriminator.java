package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/sift/Discriminator.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/sift/Discriminator.class */
public interface Discriminator<E> extends LifeCycle {
    String getDiscriminatingValue(E e);

    String getKey();
}
