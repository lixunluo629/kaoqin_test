package org.ehcache.core.internal.resilience;

import org.ehcache.Cache;
import org.ehcache.CacheIterationException;
import org.ehcache.core.spi.store.StoreAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/resilience/LoggingRobustResilienceStrategy.class */
public class LoggingRobustResilienceStrategy<K, V> extends RobustResilienceStrategy<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) LoggingRobustResilienceStrategy.class);

    public LoggingRobustResilienceStrategy(RecoveryCache<K> store) {
        super(store);
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Cache.Entry<K, V> iteratorFailure(StoreAccessException e) {
        LOGGER.error("Ehcache iterator terminated early due to exception", (Throwable) e);
        throw new CacheIterationException(e);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void recovered(K key, StoreAccessException from) {
        LOGGER.info("Ehcache key {} recovered from", key, from);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void recovered(Iterable<? extends K> keys, StoreAccessException from) {
        LOGGER.info("Ehcache keys {} recovered from", keys, from);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void recovered(StoreAccessException from) {
        LOGGER.info("Ehcache recovered from", (Throwable) from);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void inconsistent(K key, StoreAccessException because, StoreAccessException... cleanup) {
        LOGGER.error("Ehcache key {} in possible inconsistent state due to ", key, because);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void inconsistent(Iterable<? extends K> keys, StoreAccessException because, StoreAccessException... cleanup) {
        LOGGER.error("Ehcache keys {} in possible inconsistent state due to ", keys, because);
    }

    @Override // org.ehcache.core.internal.resilience.RobustResilienceStrategy
    protected void inconsistent(StoreAccessException because, StoreAccessException... cleanup) {
        LOGGER.error("Ehcache in possible inconsistent state due to ", (Throwable) because);
    }
}
