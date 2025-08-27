package ch.qos.logback.core.spi;

import java.io.Serializable;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/PreSerializationTransformer.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/PreSerializationTransformer.class */
public interface PreSerializationTransformer<E> {
    Serializable transform(E e);
}
