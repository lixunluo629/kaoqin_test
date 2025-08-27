package org.terracotta.statistics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.terracotta.statistics.observer.OperationObserver;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticBuilder.class */
public final class StatisticBuilder {
    private StatisticBuilder() {
    }

    public static <T extends Enum<T>> OperationStatisticBuilder<T> operation(Class<T> type) {
        return new OperationStatisticBuilder<>(type);
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticBuilder$OperationStatisticBuilder.class */
    public static class OperationStatisticBuilder<T extends Enum<T>> extends AbstractStatisticBuilder<OperationStatisticBuilder<T>> {
        private final Class<T> type;

        public OperationStatisticBuilder(Class<T> type) {
            this.type = type;
        }

        public OperationObserver<T> build() {
            if (this.context == null || this.name == null) {
                throw new IllegalStateException();
            }
            return StatisticsManager.createOperationStatistic(this.context, this.name, this.tags, this.type);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/StatisticBuilder$AbstractStatisticBuilder.class */
    public static class AbstractStatisticBuilder<T extends AbstractStatisticBuilder> {
        protected final Set<String> tags = new HashSet();
        protected Object context;
        protected String name;

        AbstractStatisticBuilder() {
        }

        public T of(Object of) {
            if (this.context == null) {
                this.context = of;
                return this;
            }
            throw new IllegalStateException("Context already defined");
        }

        public T named(String name) {
            if (this.name == null) {
                this.name = name;
                return this;
            }
            throw new IllegalStateException("Name already defined");
        }

        public T tag(String... tags) {
            Collections.addAll(this.tags, tags);
            return this;
        }
    }
}
