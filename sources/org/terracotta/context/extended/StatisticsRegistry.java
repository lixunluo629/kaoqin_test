package org.terracotta.context.extended;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.ibatis.ognl.OgnlContext;
import org.terracotta.context.ContextManager;
import org.terracotta.context.TreeNode;
import org.terracotta.context.query.Matcher;
import org.terracotta.context.query.Matchers;
import org.terracotta.context.query.QueryBuilder;
import org.terracotta.statistics.OperationStatistic;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.extended.CompoundOperation;
import org.terracotta.statistics.extended.CompoundOperationImpl;
import org.terracotta.statistics.extended.ExpiringSampledStatistic;
import org.terracotta.statistics.extended.Result;
import org.terracotta.statistics.extended.SampleType;
import org.terracotta.statistics.extended.SampledStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/StatisticsRegistry.class */
public class StatisticsRegistry {
    private final Object contextObject;
    private final ScheduledExecutorService executor;
    private volatile long timeToDisable;
    private volatile TimeUnit timeToDisableUnit;
    private volatile ScheduledFuture<?> disableStatus;
    private final long averageWindowDuration;
    private final TimeUnit averageWindowUnit;
    private final int historySize;
    private final long historyInterval;
    private final TimeUnit historyIntervalUnit;
    private final Map<String, RegisteredStatistic> registrations = new ConcurrentHashMap();
    private final Runnable disableTask = createDisableTask();

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/StatisticsRegistry$Function.class */
    private interface Function<T, R> {
        R apply(T t);
    }

    public StatisticsRegistry(Object contextObject, ScheduledExecutorService executor, long averageWindowDuration, TimeUnit averageWindowUnit, int historySize, long historyInterval, TimeUnit historyIntervalUnit, long timeToDisable, TimeUnit timeToDisableUnit) {
        this.contextObject = contextObject;
        this.averageWindowDuration = averageWindowDuration;
        this.averageWindowUnit = averageWindowUnit;
        this.historySize = historySize;
        this.historyInterval = historyInterval;
        this.historyIntervalUnit = historyIntervalUnit;
        this.executor = executor;
        this.timeToDisable = timeToDisable;
        this.timeToDisableUnit = timeToDisableUnit;
        this.disableStatus = this.executor.scheduleAtFixedRate(this.disableTask, timeToDisable, timeToDisable, timeToDisableUnit);
    }

    private Runnable createDisableTask() {
        return new Runnable() { // from class: org.terracotta.context.extended.StatisticsRegistry.1
            @Override // java.lang.Runnable
            public void run() {
                long expireThreshold = Time.absoluteTime() - StatisticsRegistry.this.timeToDisableUnit.toMillis(StatisticsRegistry.this.timeToDisable);
                for (RegisteredStatistic registeredStatistic : StatisticsRegistry.this.registrations.values()) {
                    registeredStatistic.getSupport().expire(expireThreshold);
                }
            }
        };
    }

    public synchronized void setTimeToDisable(long time, TimeUnit unit) {
        this.timeToDisable = time;
        this.timeToDisableUnit = unit;
        if (this.disableStatus != null) {
            this.disableStatus.cancel(false);
            this.disableStatus = this.executor.scheduleAtFixedRate(this.disableTask, this.timeToDisable, this.timeToDisable, this.timeToDisableUnit);
        }
    }

    public synchronized void setAlwaysOn(boolean enabled) {
        if (enabled) {
            if (this.disableStatus != null) {
                this.disableStatus.cancel(false);
                this.disableStatus = null;
            }
            for (RegisteredStatistic registeredStatistic : this.registrations.values()) {
                registeredStatistic.getSupport().setAlwaysOn(true);
            }
            return;
        }
        if (this.disableStatus == null) {
            this.disableStatus = this.executor.scheduleAtFixedRate(this.disableTask, 0L, this.timeToDisable, this.timeToDisableUnit);
        }
        for (RegisteredStatistic registeredStatistic2 : this.registrations.values()) {
            registeredStatistic2.getSupport().setAlwaysOn(false);
        }
    }

    public void registerSize(String name, ValueStatisticDescriptor descriptor) {
        registerStatistic(name, descriptor, SampleType.SIZE, new Function<ExpiringSampledStatistic<Long>, RegisteredStatistic>() { // from class: org.terracotta.context.extended.StatisticsRegistry.2
            @Override // org.terracotta.context.extended.StatisticsRegistry.Function
            public RegisteredStatistic apply(ExpiringSampledStatistic<Long> expiringSampledStatistic) {
                return new RegisteredSizeStatistic(expiringSampledStatistic);
            }
        });
    }

    public void registerCounter(String name, ValueStatisticDescriptor descriptor) {
        registerStatistic(name, descriptor, SampleType.COUNTER, new Function<ExpiringSampledStatistic<Long>, RegisteredStatistic>() { // from class: org.terracotta.context.extended.StatisticsRegistry.3
            @Override // org.terracotta.context.extended.StatisticsRegistry.Function
            public RegisteredStatistic apply(ExpiringSampledStatistic<Long> expiringSampledStatistic) {
                return new RegisteredCounterStatistic(expiringSampledStatistic);
            }
        });
    }

    private <N extends Number> void registerStatistic(String name, ValueStatisticDescriptor descriptor, SampleType type, Function<ExpiringSampledStatistic<N>, RegisteredStatistic> registeredStatisticFunction) {
        Map<String, RegisteredStatistic> registeredStatistics = new HashMap<>();
        Map<String, ValueStatistic<N>> valueStatistics = findValueStatistics(this.contextObject, name, descriptor.getObserverName(), descriptor.getTags());
        Set<String> duplicates = new HashSet<>();
        for (Map.Entry<String, ValueStatistic<N>> entry : valueStatistics.entrySet()) {
            String key = entry.getKey();
            ValueStatistic<N> value = entry.getValue();
            if (this.registrations.containsKey(key)) {
                duplicates.add(key);
            }
            ExpiringSampledStatistic<N> expiringSampledStatistic = new ExpiringSampledStatistic<>(value, this.executor, this.historySize, this.historyInterval, this.historyIntervalUnit, type);
            RegisteredStatistic registeredStatistic = registeredStatisticFunction.apply(expiringSampledStatistic);
            registeredStatistics.put(key, registeredStatistic);
        }
        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException("Found duplicate value statistic(s) " + duplicates);
        }
        this.registrations.putAll(registeredStatistics);
    }

    public <T extends Enum<T>> void registerCompoundOperations(String name, OperationStatisticDescriptor<T> descriptor, EnumSet<T> compound) {
        Map<String, CompoundOperation<T>> compoundOperations = createCompoundOperations(name, descriptor.getObserverName(), descriptor.getTags(), descriptor.getType());
        Map<String, RegisteredCompoundStatistic<T>> registeredStatistics = new HashMap<>();
        Set<String> duplicates = new HashSet<>();
        for (Map.Entry<String, CompoundOperation<T>> entry : compoundOperations.entrySet()) {
            String key = entry.getKey();
            if (this.registrations.containsKey(key)) {
                duplicates.add(key);
            }
            registeredStatistics.put(key, new RegisteredCompoundStatistic<>(entry.getValue(), compound));
        }
        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException("Found duplicate operation statistic(s) " + duplicates);
        }
        for (CompoundOperation<T> compoundOperation : compoundOperations.values()) {
            compoundOperation.compound(compound);
        }
        this.registrations.putAll(registeredStatistics);
    }

    public <T extends Enum<T>> void registerRatios(String name, OperationStatisticDescriptor<T> descriptor, EnumSet<T> ratioNumerator, EnumSet<T> ratioDenominator) {
        Map<String, CompoundOperation<T>> compoundOperations = createCompoundOperations(name, descriptor.getObserverName(), descriptor.getTags(), descriptor.getType());
        Map<String, RegisteredRatioStatistic<T>> registeredStatistics = new HashMap<>();
        Set<String> duplicates = new HashSet<>();
        for (Map.Entry<String, CompoundOperation<T>> entry : compoundOperations.entrySet()) {
            String key = entry.getKey();
            if (this.registrations.containsKey(key)) {
                duplicates.add(key);
            }
            registeredStatistics.put(key, new RegisteredRatioStatistic<>(entry.getValue(), ratioNumerator, ratioDenominator));
        }
        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException("Found duplicate operation statistic(s) " + duplicates);
        }
        for (CompoundOperation<T> compoundOperation : compoundOperations.values()) {
            compoundOperation.ratioOf(ratioNumerator, ratioDenominator);
        }
        this.registrations.putAll(registeredStatistics);
    }

    public Map<String, RegisteredStatistic> getRegistrations() {
        return Collections.unmodifiableMap(this.registrations);
    }

    public void clearRegistrations() {
        this.registrations.clear();
    }

    public SampledStatistic<? extends Number> findSampledStatistic(String statisticName) {
        RegisteredStatistic registeredStatistic = this.registrations.get(statisticName);
        if (registeredStatistic != null) {
            switch (registeredStatistic.getType()) {
                case COUNTER:
                    return ((RegisteredCounterStatistic) registeredStatistic).getSampledStatistic();
                case RATIO:
                    return ((RegisteredRatioStatistic) registeredStatistic).getSampledStatistic();
                case SIZE:
                    return ((RegisteredSizeStatistic) registeredStatistic).getSampledStatistic();
                default:
                    return null;
            }
        }
        return null;
    }

    public SampledStatistic<? extends Number> findSampledCompoundStatistic(String statisticName, SampleType sampleType) {
        RegisteredStatistic registeredStatistic = this.registrations.get(statisticName);
        if (registeredStatistic != null && registeredStatistic.getType() == RegistrationType.COMPOUND) {
            Result result = ((RegisteredCompoundStatistic) registeredStatistic).getResult();
            switch (sampleType) {
                case COUNTER:
                    return result.count();
                case RATE:
                    return result.rate();
                case LATENCY_MIN:
                    return result.latency().minimum();
                case LATENCY_MAX:
                    return result.latency().maximum();
                case LATENCY_AVG:
                    return result.latency().average();
                default:
                    return null;
            }
        }
        return null;
    }

    private <T extends Enum<T>> Map<String, CompoundOperation<T>> createCompoundOperations(String name, String observerName, Set<String> tags, Class<T> type) {
        HashMap map = new HashMap();
        Map<String, OperationStatistic<T>> operationObservers = findOperationStatistics(this.contextObject, name, type, observerName, tags);
        if (operationObservers.isEmpty()) {
            throw new IllegalArgumentException("Required statistic observer '" + observerName + "' with tags " + tags + " and type '" + type + "' not found under '" + this.contextObject + "'");
        }
        for (Map.Entry<String, OperationStatistic<T>> entry : operationObservers.entrySet()) {
            map.put(entry.getKey(), new CompoundOperationImpl(entry.getValue(), type, this.averageWindowDuration, this.averageWindowUnit, this.executor, this.historySize, this.historyInterval, this.historyIntervalUnit));
        }
        return map;
    }

    private static <T extends Enum<T>> Map<String, OperationStatistic<T>> findOperationStatistics(Object contextObject, String name, Class<T> type, String observerName, final Set<String> tags) {
        Set<TreeNode> result = QueryBuilder.queryBuilder().descendants().filter(Matchers.context(Matchers.attributes(Matchers.allOf(Matchers.hasAttribute("type", type), Matchers.hasAttribute("name", observerName), Matchers.hasAttribute("tags", (Matcher<? extends Object>) new Matcher<Set<String>>() { // from class: org.terracotta.context.extended.StatisticsRegistry.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(Set<String> object) {
                return object.containsAll(tags);
            }
        }))))).filter(Matchers.context(Matchers.identifier(Matchers.subclassOf(OperationStatistic.class)))).build().execute(Collections.singleton(ContextManager.nodeFor(contextObject)));
        if (result.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, OperationStatistic<T>> observers = new HashMap<>();
        for (TreeNode node : result) {
            String discriminator = null;
            Map<String, Object> properties = (Map) node.getContext().attributes().get("properties");
            if (properties != null && properties.containsKey("discriminator")) {
                discriminator = properties.get("discriminator").toString();
            }
            String completeName = (discriminator == null ? "" : discriminator + ":") + name;
            OperationStatistic<T> existing = observers.put(completeName, (OperationStatistic) node.getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY));
            if (existing != null) {
                throw new IllegalStateException("Duplicate OperationStatistic found for '" + completeName + "'");
            }
        }
        return observers;
    }

    private static <N extends Number> Map<String, ValueStatistic<N>> findValueStatistics(Object contextObject, String name, String observerName, final Set<String> tags) {
        Set<TreeNode> result = QueryBuilder.queryBuilder().descendants().filter(Matchers.context(Matchers.attributes(Matchers.allOf(Matchers.hasAttribute("name", observerName), Matchers.hasAttribute("tags", (Matcher<? extends Object>) new Matcher<Set<String>>() { // from class: org.terracotta.context.extended.StatisticsRegistry.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(Set<String> object) {
                return object.containsAll(tags);
            }
        }))))).filter(Matchers.context(Matchers.identifier(Matchers.subclassOf(ValueStatistic.class)))).build().execute(Collections.singleton(ContextManager.nodeFor(contextObject)));
        if (result.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, ValueStatistic<N>> observers = new HashMap<>();
        for (TreeNode node : result) {
            String discriminator = null;
            Map<String, Object> properties = (Map) node.getContext().attributes().get("properties");
            if (properties != null && properties.containsKey("discriminator")) {
                discriminator = properties.get("discriminator").toString();
            }
            String completeName = (discriminator == null ? "" : discriminator + ":") + name;
            if (observers.put(completeName, (ValueStatistic) node.getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY)) != null) {
                throw new IllegalStateException("Duplicate ValueStatistic found for '" + completeName + "'");
            }
        }
        return observers;
    }
}
