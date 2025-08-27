package org.terracotta.context.extended;

import java.lang.Enum;
import java.util.Set;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/OperationStatisticDescriptor.class */
public final class OperationStatisticDescriptor<T extends Enum<T>> {
    private final String observerName;
    private final Set<String> tags;
    private final Class<T> type;

    private OperationStatisticDescriptor(String observerName, Set<String> tags, Class<T> type) {
        this.observerName = observerName;
        this.tags = tags;
        this.type = type;
    }

    public String getObserverName() {
        return this.observerName;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public Class<T> getType() {
        return this.type;
    }

    public static <T extends Enum<T>> OperationStatisticDescriptor<T> descriptor(String observerName, Set<String> tags, Class<T> type) {
        return new OperationStatisticDescriptor<>(observerName, tags, type);
    }
}
