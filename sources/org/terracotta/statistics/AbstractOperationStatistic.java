package org.terracotta.statistics;

import java.lang.Enum;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.ognl.OgnlContext;
import org.terracotta.context.annotations.ContextAttribute;
import org.terracotta.statistics.observer.ChainedOperationObserver;

@ContextAttribute(OgnlContext.THIS_CONTEXT_KEY)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/AbstractOperationStatistic.class */
public abstract class AbstractOperationStatistic<T extends Enum<T>> extends AbstractSourceStatistic<ChainedOperationObserver<? super T>> implements OperationStatistic<T> {

    @ContextAttribute("name")
    public final String name;

    @ContextAttribute("tags")
    public final Set<String> tags;

    @ContextAttribute("properties")
    public final Map<String, Object> properties;

    @ContextAttribute("type")
    public final Class<T> type;

    AbstractOperationStatistic(String name, Set<String> tags, Map<String, ? extends Object> properties, Class<T> type) {
        this.name = name;
        this.tags = Collections.unmodifiableSet(new HashSet(tags));
        this.properties = Collections.unmodifiableMap(new HashMap(properties));
        this.type = type;
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public Class<T> type() {
        return this.type;
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(final T result) {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.AbstractOperationStatistic.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return Long.valueOf(AbstractOperationStatistic.this.count(result));
            }
        };
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(final Set<T> results) {
        return new ValueStatistic<Long>() { // from class: org.terracotta.statistics.AbstractOperationStatistic.2
            @Override // org.terracotta.statistics.ValueStatistic
            public Long value() {
                return Long.valueOf(AbstractOperationStatistic.this.sum(results));
            }
        };
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum() {
        return sum(EnumSet.allOf(this.type));
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void begin() {
        if (!this.derivedStatistics.isEmpty()) {
            long time = Time.time();
            for (T observer : this.derivedStatistics) {
                observer.begin(time);
            }
        }
    }
}
