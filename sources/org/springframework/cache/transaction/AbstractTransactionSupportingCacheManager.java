package org.springframework.cache.transaction;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/transaction/AbstractTransactionSupportingCacheManager.class */
public abstract class AbstractTransactionSupportingCacheManager extends AbstractCacheManager {
    private boolean transactionAware = false;

    public void setTransactionAware(boolean transactionAware) {
        this.transactionAware = transactionAware;
    }

    public boolean isTransactionAware() {
        return this.transactionAware;
    }

    @Override // org.springframework.cache.support.AbstractCacheManager
    protected Cache decorateCache(Cache cache) {
        return isTransactionAware() ? new TransactionAwareCacheDecorator(cache) : cache;
    }
}
