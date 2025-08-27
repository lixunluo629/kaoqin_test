package org.terracotta.statistics;

import java.lang.Enum;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.terracotta.statistics.observer.ChainedObserver;
import org.terracotta.statistics.observer.ChainedOperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticMapper.class */
public class StatisticMapper<SOURCE extends Enum<SOURCE>, TARGET extends Enum<TARGET>> implements OperationStatistic<TARGET> {
    private final Class<TARGET> targetType;
    private final Class<SOURCE> sourceType;
    private final OperationStatistic<SOURCE> statistic;
    private final Map<TARGET, Set<SOURCE>> translation;
    private final Map<SOURCE, TARGET> reverseTranslation;
    private final ConcurrentMap<ChainedOperationObserver<? super TARGET>, ChainedOperationObserver<SOURCE>> derivedStats = new ConcurrentHashMap();

    public StatisticMapper(Map<TARGET, Set<SOURCE>> translation, OperationStatistic<SOURCE> statistic) {
        Map.Entry<TARGET, Set<SOURCE>> first = translation.entrySet().iterator().next();
        this.targetType = first.getKey().getDeclaringClass();
        this.sourceType = first.getValue().iterator().next().getDeclaringClass();
        this.statistic = statistic;
        this.translation = translation;
        Set<TARGET> unmappedTierOutcomes = EnumSet.allOf(this.targetType);
        unmappedTierOutcomes.removeAll(translation.keySet());
        if (!unmappedTierOutcomes.isEmpty()) {
            throw new IllegalArgumentException("Translation does not contain target outcomes " + unmappedTierOutcomes);
        }
        this.reverseTranslation = reverse(translation);
        Set<SOURCE> unmappedStoreOutcomes = EnumSet.allOf(this.sourceType);
        unmappedStoreOutcomes.removeAll(this.reverseTranslation.keySet());
        if (!unmappedStoreOutcomes.isEmpty()) {
            throw new IllegalArgumentException("Translation does not contain source outcomes " + unmappedStoreOutcomes);
        }
    }

    private static <B extends Enum<B>, A extends Enum<A>> Map<B, A> reverse(Map<A, Set<B>> map) {
        Map<B, A> reverse = Collections.emptyMap();
        for (Map.Entry<A, Set<B>> e : map.entrySet()) {
            for (B b : e.getValue()) {
                if (reverse.isEmpty()) {
                    reverse = new EnumMap(b.getDeclaringClass());
                }
                if (reverse.put(b, e.getKey()) != null) {
                    throw new IllegalArgumentException("Reverse statistic outcome mapping is ill-defined: " + map);
                }
            }
        }
        return reverse;
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public Class<TARGET> type() {
        return this.targetType;
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(TARGET target) {
        return this.statistic.statistic(this.translation.get(target));
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(Set<TARGET> results) {
        EnumSet enumSetNoneOf = EnumSet.noneOf(this.sourceType);
        for (TARGET result : results) {
            enumSetNoneOf.addAll(this.translation.get(result));
        }
        return this.statistic.statistic(enumSetNoneOf);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long count(TARGET target) {
        return this.statistic.sum(this.translation.get(target));
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum(Set<TARGET> types) {
        EnumSet enumSetNoneOf = EnumSet.noneOf(this.sourceType);
        for (TARGET type : types) {
            enumSetNoneOf.addAll(this.translation.get(type));
        }
        return this.statistic.sum(enumSetNoneOf);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum() {
        return this.statistic.sum();
    }

    @Override // org.terracotta.statistics.SourceStatistic
    public void addDerivedStatistic(final ChainedOperationObserver<? super TARGET> chainedOperationObserver) {
        ChainedObserver chainedObserver = new ChainedOperationObserver<SOURCE>() { // from class: org.terracotta.statistics.StatisticMapper.1
            @Override // org.terracotta.statistics.observer.ChainedOperationObserver
            public void begin(long time) {
                chainedOperationObserver.begin(time);
            }

            @Override // org.terracotta.statistics.observer.ChainedOperationObserver
            public void end(long time, SOURCE result) {
                chainedOperationObserver.end(time, (Enum) StatisticMapper.this.reverseTranslation.get(result));
            }

            @Override // org.terracotta.statistics.observer.ChainedOperationObserver
            public void end(long time, SOURCE result, long... parameters) {
                chainedOperationObserver.end(time, (Enum) StatisticMapper.this.reverseTranslation.get(result), parameters);
            }
        };
        if (this.derivedStats.putIfAbsent(chainedOperationObserver, chainedObserver) == null) {
            this.statistic.addDerivedStatistic(chainedObserver);
        }
    }

    @Override // org.terracotta.statistics.SourceStatistic
    public void removeDerivedStatistic(ChainedOperationObserver<? super TARGET> derived) {
        ChainedOperationObserver<SOURCE> translator = this.derivedStats.remove(derived);
        if (translator != null) {
            this.statistic.removeDerivedStatistic(translator);
        }
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void begin() {
        throw new UnsupportedOperationException();
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(TARGET result) {
        throw new UnsupportedOperationException();
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(TARGET result, long... parameters) {
        throw new UnsupportedOperationException();
    }
}
