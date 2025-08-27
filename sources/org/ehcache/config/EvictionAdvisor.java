package org.ehcache.config;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/EvictionAdvisor.class */
public interface EvictionAdvisor<K, V> {
    boolean adviseAgainstEviction(K k, V v);
}
