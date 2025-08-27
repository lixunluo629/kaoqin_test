package org.terracotta.statistics;

import java.lang.Number;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.ibatis.ognl.OgnlContext;
import org.terracotta.context.WeakIdentityHashMap;
import org.terracotta.context.annotations.ContextAttribute;

@ContextAttribute(OgnlContext.THIS_CONTEXT_KEY)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/PassThroughStatistic.class */
class PassThroughStatistic<T extends Number> implements ValueStatistic<T> {
    private static final WeakIdentityHashMap<Object, Collection<PassThroughStatistic<?>>> BINDING = new WeakIdentityHashMap<>();

    @ContextAttribute("name")
    public final String name;

    @ContextAttribute("tags")
    public final Set<String> tags;

    @ContextAttribute("properties")
    public final Map<String, Object> properties;
    private final Callable<T> source;

    private static void bindStatistic(PassThroughStatistic<?> stat, Object to) {
        Collection<PassThroughStatistic<?>> collection = BINDING.get(to);
        if (collection == null) {
            collection = new CopyOnWriteArrayList();
            Collection<PassThroughStatistic<?>> racer = BINDING.putIfAbsent(to, collection);
            if (racer != null) {
                collection = racer;
            }
        }
        collection.add(stat);
    }

    public static void removeStatistics(Object to) {
        BINDING.remove(to);
    }

    static boolean hasStatisticsFor(Object to) {
        return BINDING.get(to) != null;
    }

    public PassThroughStatistic(Object context, String name, Set<String> tags, Map<String, ? extends Object> properties, Callable<T> source) {
        this.name = name;
        this.tags = Collections.unmodifiableSet(new HashSet(tags));
        this.properties = Collections.unmodifiableMap(new HashMap(properties));
        this.source = source;
        bindStatistic(this, context);
    }

    @Override // org.terracotta.statistics.ValueStatistic
    public T value() {
        try {
            return this.source.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
