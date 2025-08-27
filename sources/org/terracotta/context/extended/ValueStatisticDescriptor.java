package org.terracotta.context.extended;

import java.util.Set;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/ValueStatisticDescriptor.class */
public final class ValueStatisticDescriptor {
    private final String observerName;
    private final Set<String> tags;

    private ValueStatisticDescriptor(String observerName, Set<String> tags) {
        this.observerName = observerName;
        this.tags = tags;
    }

    public String getObserverName() {
        return this.observerName;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public static ValueStatisticDescriptor descriptor(String observerName, Set<String> tags) {
        return new ValueStatisticDescriptor(observerName, tags);
    }
}
