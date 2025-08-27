package org.ehcache.jsr107;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.cache.management.CacheStatisticsMXBean;
import org.apache.ibatis.ognl.OgnlContext;
import org.ehcache.Cache;
import org.ehcache.core.InternalCache;
import org.ehcache.core.statistics.BulkOps;
import org.ehcache.core.statistics.CacheOperationOutcomes;
import org.ehcache.core.statistics.StoreOperationOutcomes;
import org.terracotta.context.ContextManager;
import org.terracotta.context.TreeNode;
import org.terracotta.context.query.Matchers;
import org.terracotta.context.query.Query;
import org.terracotta.context.query.QueryBuilder;
import org.terracotta.statistics.OperationStatistic;
import org.terracotta.statistics.derived.LatencySampling;
import org.terracotta.statistics.derived.MinMaxAverage;
import org.terracotta.statistics.jsr166e.LongAdder;
import org.terracotta.statistics.observer.ChainedOperationObserver;
import redis.clients.jedis.Protocol;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheStatisticsMXBean.class */
class Eh107CacheStatisticsMXBean extends Eh107MXBean implements CacheStatisticsMXBean {
    private final CompensatingCounters compensatingCounters;
    private final OperationStatistic<CacheOperationOutcomes.GetOutcome> get;
    private final OperationStatistic<CacheOperationOutcomes.PutOutcome> put;
    private final OperationStatistic<CacheOperationOutcomes.RemoveOutcome> remove;
    private final OperationStatistic<CacheOperationOutcomes.PutIfAbsentOutcome> putIfAbsent;
    private final OperationStatistic<CacheOperationOutcomes.ReplaceOutcome> replace;
    private final OperationStatistic<CacheOperationOutcomes.ConditionalRemoveOutcome> conditionalRemove;
    private final OperationStatistic<StoreOperationOutcomes.EvictionOutcome> lowestTierEviction;
    private final Map<BulkOps, LongAdder> bulkMethodEntries;
    private final LatencyMonitor<CacheOperationOutcomes.GetOutcome> averageGetTime;
    private final LatencyMonitor<CacheOperationOutcomes.PutOutcome> averagePutTime;
    private final LatencyMonitor<CacheOperationOutcomes.RemoveOutcome> averageRemoveTime;

    Eh107CacheStatisticsMXBean(String cacheName, Eh107CacheManager cacheManager, InternalCache<?, ?> cache) {
        super(cacheName, cacheManager, "CacheStatistics");
        this.compensatingCounters = new CompensatingCounters();
        this.bulkMethodEntries = cache.getBulkMethodEntries();
        this.get = findCacheStatistic(cache, CacheOperationOutcomes.GetOutcome.class, BeanUtil.PREFIX_GETTER_GET);
        this.put = findCacheStatistic(cache, CacheOperationOutcomes.PutOutcome.class, "put");
        this.remove = findCacheStatistic(cache, CacheOperationOutcomes.RemoveOutcome.class, Protocol.SENTINEL_REMOVE);
        this.putIfAbsent = findCacheStatistic(cache, CacheOperationOutcomes.PutIfAbsentOutcome.class, "putIfAbsent");
        this.replace = findCacheStatistic(cache, CacheOperationOutcomes.ReplaceOutcome.class, Parser.REPLACE_CONVERTER_WORD);
        this.conditionalRemove = findCacheStatistic(cache, CacheOperationOutcomes.ConditionalRemoveOutcome.class, "conditionalRemove");
        this.lowestTierEviction = findLowestTierStatistic(cache, StoreOperationOutcomes.EvictionOutcome.class, "eviction");
        this.averageGetTime = new LatencyMonitor<>(EnumSet.allOf(CacheOperationOutcomes.GetOutcome.class));
        this.get.addDerivedStatistic(this.averageGetTime);
        this.averagePutTime = new LatencyMonitor<>(EnumSet.allOf(CacheOperationOutcomes.PutOutcome.class));
        this.put.addDerivedStatistic(this.averagePutTime);
        this.averageRemoveTime = new LatencyMonitor<>(EnumSet.allOf(CacheOperationOutcomes.RemoveOutcome.class));
        this.remove.addDerivedStatistic(this.averageRemoveTime);
    }

    public void clear() {
        this.compensatingCounters.snapshot();
        this.averageGetTime.clear();
        this.averagePutTime.clear();
        this.averageRemoveTime.clear();
    }

    public long getCacheHits() {
        return normalize((getHits() - this.compensatingCounters.cacheHits) - this.compensatingCounters.bulkGetHits);
    }

    public float getCacheHitPercentage() {
        long cacheHits = getCacheHits();
        return normalize(cacheHits / (cacheHits + getCacheMisses())) * 100.0f;
    }

    public long getCacheMisses() {
        return normalize((getMisses() - this.compensatingCounters.cacheMisses) - this.compensatingCounters.bulkGetMiss);
    }

    public float getCacheMissPercentage() {
        long cacheMisses = getCacheMisses();
        return normalize(cacheMisses / (getCacheHits() + cacheMisses)) * 100.0f;
    }

    public long getCacheGets() {
        return normalize((((getHits() + getMisses()) - this.compensatingCounters.cacheGets) - this.compensatingCounters.bulkGetHits) - this.compensatingCounters.bulkGetMiss);
    }

    public long getCachePuts() {
        return normalize((((((getBulkCount(BulkOps.PUT_ALL) - this.compensatingCounters.bulkPuts) + this.put.sum(EnumSet.of(CacheOperationOutcomes.PutOutcome.PUT))) + this.put.sum(EnumSet.of(CacheOperationOutcomes.PutOutcome.UPDATED))) + this.putIfAbsent.sum(EnumSet.of(CacheOperationOutcomes.PutIfAbsentOutcome.PUT))) + this.replace.sum(EnumSet.of(CacheOperationOutcomes.ReplaceOutcome.HIT))) - this.compensatingCounters.cachePuts);
    }

    public long getCacheRemovals() {
        return normalize((((getBulkCount(BulkOps.REMOVE_ALL) - this.compensatingCounters.bulkRemovals) + this.remove.sum(EnumSet.of(CacheOperationOutcomes.RemoveOutcome.SUCCESS))) + this.conditionalRemove.sum(EnumSet.of(CacheOperationOutcomes.ConditionalRemoveOutcome.SUCCESS))) - this.compensatingCounters.cacheRemovals);
    }

    public long getCacheEvictions() {
        return normalize(this.lowestTierEviction.sum(EnumSet.of(StoreOperationOutcomes.EvictionOutcome.SUCCESS)) - this.compensatingCounters.cacheEvictions);
    }

    public float getAverageGetTime() {
        return (float) this.averageGetTime.value();
    }

    public float getAveragePutTime() {
        return (float) this.averagePutTime.value();
    }

    public float getAverageRemoveTime() {
        return (float) this.averageRemoveTime.value();
    }

    private long getMisses() {
        return getBulkCount(BulkOps.GET_ALL_MISS) + this.get.sum(EnumSet.of(CacheOperationOutcomes.GetOutcome.MISS)) + this.putIfAbsent.sum(EnumSet.of(CacheOperationOutcomes.PutIfAbsentOutcome.PUT)) + this.replace.sum(EnumSet.of(CacheOperationOutcomes.ReplaceOutcome.MISS_NOT_PRESENT)) + this.conditionalRemove.sum(EnumSet.of(CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_MISSING));
    }

    private long getHits() {
        return getBulkCount(BulkOps.GET_ALL_HITS) + this.get.sum(EnumSet.of(CacheOperationOutcomes.GetOutcome.HIT)) + this.putIfAbsent.sum(EnumSet.of(CacheOperationOutcomes.PutIfAbsentOutcome.HIT)) + this.replace.sum(EnumSet.of(CacheOperationOutcomes.ReplaceOutcome.HIT, CacheOperationOutcomes.ReplaceOutcome.MISS_PRESENT)) + this.conditionalRemove.sum(EnumSet.of(CacheOperationOutcomes.ConditionalRemoveOutcome.SUCCESS, CacheOperationOutcomes.ConditionalRemoveOutcome.FAILURE_KEY_PRESENT));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getBulkCount(BulkOps bulkOps) {
        return this.bulkMethodEntries.get(bulkOps).longValue();
    }

    private static long normalize(long value) {
        return Math.max(0L, value);
    }

    private static float normalize(float value) {
        if (Float.isNaN(value)) {
            return 0.0f;
        }
        return Math.min(1.0f, Math.max(0.0f, value));
    }

    static <T extends Enum<T>> OperationStatistic<T> findCacheStatistic(Cache<?, ?> cache, Class<T> type, String statName) {
        Query query = QueryBuilder.queryBuilder().children().filter(Matchers.context(Matchers.attributes(Matchers.allOf(Matchers.hasAttribute("name", statName), Matchers.hasAttribute("type", type))))).build();
        Set<TreeNode> result = query.execute(Collections.singleton(ContextManager.nodeFor(cache)));
        if (result.size() > 1) {
            throw new RuntimeException("result must be unique");
        }
        if (result.isEmpty()) {
            throw new RuntimeException("result must not be null");
        }
        OperationStatistic<T> statistic = (OperationStatistic) result.iterator().next().getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY);
        return statistic;
    }

    <T extends Enum<T>> OperationStatistic<T> findLowestTierStatistic(Cache<?, ?> cache, Class<T> type, String statName) {
        Query statQuery = QueryBuilder.queryBuilder().descendants().filter(Matchers.context(Matchers.attributes(Matchers.allOf(Matchers.hasAttribute("name", statName), Matchers.hasAttribute("type", type))))).build();
        Set<TreeNode> statResult = statQuery.execute(Collections.singleton(ContextManager.nodeFor(cache)));
        if (statResult.size() < 1) {
            throw new RuntimeException("Failed to find lowest tier statistic: " + statName + " , valid result Set sizes must 1 or more.  Found result Set size of: " + statResult.size());
        }
        if (statResult.size() == 1) {
            OperationStatistic<T> statistic = (OperationStatistic) statResult.iterator().next().getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY);
            return statistic;
        }
        String lowestStoreType = "onheap";
        TreeNode lowestTierNode = null;
        for (TreeNode treeNode : statResult) {
            if (((Set) treeNode.getContext().attributes().get("tags")).size() != 1) {
                throw new RuntimeException("Failed to find lowest tier statistic. \"tags\" set must be size 1");
            }
            String storeType = treeNode.getContext().attributes().get("tags").toString();
            if (storeType.compareToIgnoreCase(lowestStoreType) < 0) {
                lowestStoreType = treeNode.getContext().attributes().get("tags").toString();
                lowestTierNode = treeNode;
            }
        }
        OperationStatistic<T> statistic2 = (OperationStatistic) lowestTierNode.getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY);
        return statistic2;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheStatisticsMXBean$CompensatingCounters.class */
    class CompensatingCounters {
        volatile long cacheHits;
        volatile long cacheMisses;
        volatile long cacheGets;
        volatile long bulkGetHits;
        volatile long bulkGetMiss;
        volatile long cachePuts;
        volatile long bulkPuts;
        volatile long cacheRemovals;
        volatile long bulkRemovals;
        volatile long cacheEvictions;

        CompensatingCounters() {
        }

        void snapshot() {
            this.cacheHits += Eh107CacheStatisticsMXBean.this.getCacheHits();
            this.cacheMisses += Eh107CacheStatisticsMXBean.this.getCacheMisses();
            this.cacheGets += Eh107CacheStatisticsMXBean.this.getCacheGets();
            this.bulkGetHits += Eh107CacheStatisticsMXBean.this.getBulkCount(BulkOps.GET_ALL_HITS);
            this.bulkGetMiss += Eh107CacheStatisticsMXBean.this.getBulkCount(BulkOps.GET_ALL_MISS);
            this.cachePuts += Eh107CacheStatisticsMXBean.this.getCachePuts();
            this.bulkPuts += Eh107CacheStatisticsMXBean.this.getBulkCount(BulkOps.PUT_ALL);
            this.cacheRemovals += Eh107CacheStatisticsMXBean.this.getCacheRemovals();
            this.bulkRemovals += Eh107CacheStatisticsMXBean.this.getBulkCount(BulkOps.REMOVE_ALL);
            this.cacheEvictions += Eh107CacheStatisticsMXBean.this.getCacheEvictions();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheStatisticsMXBean$LatencyMonitor.class */
    private static class LatencyMonitor<T extends Enum<T>> implements ChainedOperationObserver<T> {
        private final LatencySampling<T> sampling;
        private volatile MinMaxAverage average = new MinMaxAverage();

        public LatencyMonitor(Set<T> targets) {
            this.sampling = new LatencySampling<>(targets, 1.0d);
            this.sampling.addDerivedStatistic(this.average);
        }

        @Override // org.terracotta.statistics.observer.ChainedOperationObserver
        public void begin(long time) {
            this.sampling.begin(time);
        }

        @Override // org.terracotta.statistics.observer.ChainedOperationObserver
        public void end(long time, T result) {
            this.sampling.end(time, result);
        }

        @Override // org.terracotta.statistics.observer.ChainedOperationObserver
        public void end(long time, T result, long... parameters) {
            this.sampling.end(time, result, parameters);
        }

        public double value() {
            Double value = this.average.mean();
            if (value == null) {
                return 0.0d;
            }
            return value.doubleValue() / 1000.0d;
        }

        public synchronized void clear() {
            this.sampling.removeDerivedStatistic(this.average);
            this.average = new MinMaxAverage();
            this.sampling.addDerivedStatistic(this.average);
        }
    }
}
