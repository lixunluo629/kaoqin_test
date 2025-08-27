package org.ehcache.spi.persistence;

import java.io.Serializable;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/persistence/StateRepository.class */
public interface StateRepository {
    <K extends Serializable, V extends Serializable> StateHolder<K, V> getPersistentStateHolder(String str, Class<K> cls, Class<V> cls2);
}
