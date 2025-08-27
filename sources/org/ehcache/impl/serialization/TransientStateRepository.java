package org.ehcache.impl.serialization;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.spi.persistence.StateHolder;
import org.ehcache.spi.persistence.StateRepository;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/TransientStateRepository.class */
public class TransientStateRepository implements StateRepository {
    private ConcurrentMap<String, StateHolder<?, ?>> knownHolders = new ConcurrentHashMap();

    @Override // org.ehcache.spi.persistence.StateRepository
    public <K extends Serializable, V extends Serializable> StateHolder<K, V> getPersistentStateHolder(String name, Class<K> keyClass, Class<V> valueClass) {
        StateHolder<K, V> stateHolder = (StateHolder) this.knownHolders.get(name);
        if (stateHolder != null) {
            return stateHolder;
        }
        TransientStateHolder transientStateHolder = new TransientStateHolder();
        StateHolder<K, V> stateHolder2 = (StateHolder) this.knownHolders.putIfAbsent(name, transientStateHolder);
        if (stateHolder2 == null) {
            return transientStateHolder;
        }
        return stateHolder2;
    }
}
