package org.ehcache.impl.internal.store.offheap;

import org.ehcache.config.EvictionAdvisor;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/SwitchableEvictionAdvisor.class */
public interface SwitchableEvictionAdvisor<K, V> extends EvictionAdvisor<K, V> {
    boolean isSwitchedOn();

    void setSwitchedOn(boolean z);
}
