package org.springframework.data.support;

import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/support/IsNewStrategyFactorySupport.class */
public abstract class IsNewStrategyFactorySupport implements IsNewStrategyFactory {
    protected abstract IsNewStrategy doGetIsNewStrategy(Class<?> cls);

    @Override // org.springframework.data.support.IsNewStrategyFactory
    public final IsNewStrategy getIsNewStrategy(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        if (Persistable.class.isAssignableFrom(type)) {
            return PersistableIsNewStrategy.INSTANCE;
        }
        IsNewStrategy strategy = doGetIsNewStrategy(type);
        if (strategy != null) {
            return strategy;
        }
        throw new IllegalArgumentException(String.format("Unsupported entity %s! Could not determine IsNewStrategy.", type.getName()));
    }
}
