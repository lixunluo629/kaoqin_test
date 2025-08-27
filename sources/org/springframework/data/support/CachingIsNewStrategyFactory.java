package org.springframework.data.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/support/CachingIsNewStrategyFactory.class */
public class CachingIsNewStrategyFactory implements IsNewStrategyFactory {
    private final Map<Class<?>, IsNewStrategy> cache = new ConcurrentHashMap();
    private final IsNewStrategyFactory delegate;

    public CachingIsNewStrategyFactory(IsNewStrategyFactory delegate) {
        Assert.notNull(delegate, "IsNewStrategyFactory delegate must not be null!");
        this.delegate = delegate;
    }

    @Override // org.springframework.data.support.IsNewStrategyFactory
    public IsNewStrategy getIsNewStrategy(Class<?> type) {
        IsNewStrategy strategy = this.cache.get(type);
        if (strategy != null) {
            return strategy;
        }
        IsNewStrategy isNewStrategy = this.delegate.getIsNewStrategy(type);
        this.cache.put(type, isNewStrategy);
        return isNewStrategy;
    }
}
