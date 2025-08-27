package org.ehcache.config;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/Eviction.class */
public final class Eviction {
    private static final EvictionAdvisor<Object, Object> NO_ADVICE = new EvictionAdvisor<Object, Object>() { // from class: org.ehcache.config.Eviction.1
        @Override // org.ehcache.config.EvictionAdvisor
        public boolean adviseAgainstEviction(Object key, Object value) {
            return false;
        }
    };

    public static EvictionAdvisor<Object, Object> noAdvice() {
        return NO_ADVICE;
    }
}
