package org.terracotta.statistics;

import java.lang.Enum;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.ognl.OgnlContext;
import org.terracotta.context.ContextManager;
import org.terracotta.context.TreeNode;
import org.terracotta.context.annotations.ContextAttribute;
import org.terracotta.context.query.Matcher;
import org.terracotta.context.query.Matchers;
import org.terracotta.context.query.Query;
import org.terracotta.context.query.QueryBuilder;
import org.terracotta.statistics.observer.ChainedOperationObserver;

@ContextAttribute(OgnlContext.THIS_CONTEXT_KEY)
/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/MappedOperationStatistic.class */
public class MappedOperationStatistic<S extends Enum<S>, D extends Enum<D>> implements OperationStatistic<D> {

    @ContextAttribute("name")
    public final String name;

    @ContextAttribute("tags")
    public final Set<String> tags = Collections.singleton("tier");

    @ContextAttribute("properties")
    public final Map<String, Object> properties = new HashMap();

    @ContextAttribute("type")
    public final Class<D> outcomeType;
    private final StatisticMapper<S, D> mapper;

    public MappedOperationStatistic(Object tier, Map<D, Set<S>> translation, String statisticName, int tierHeight, String targetName, String discriminator) {
        this.name = statisticName;
        this.properties.put("tierHeight", Integer.valueOf(tierHeight));
        this.properties.put("discriminator", discriminator);
        Map.Entry<D, Set<S>> first = translation.entrySet().iterator().next();
        Class<S> outcomeType = first.getValue().iterator().next().getDeclaringClass();
        this.outcomeType = first.getKey().getDeclaringClass();
        this.mapper = new StatisticMapper<>(translation, findOperationStat(tier, outcomeType, targetName));
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public Class<D> type() {
        return this.outcomeType;
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(D result) {
        return this.mapper.statistic((StatisticMapper<S, D>) result);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public ValueStatistic<Long> statistic(Set<D> results) {
        return this.mapper.statistic(results);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long count(D type) {
        return this.mapper.count(type);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum(Set<D> types) {
        return this.mapper.sum(types);
    }

    @Override // org.terracotta.statistics.OperationStatistic
    public long sum() {
        return this.mapper.sum();
    }

    @Override // org.terracotta.statistics.SourceStatistic
    public void addDerivedStatistic(ChainedOperationObserver<? super D> derived) {
        this.mapper.addDerivedStatistic((ChainedOperationObserver) derived);
    }

    @Override // org.terracotta.statistics.SourceStatistic
    public void removeDerivedStatistic(ChainedOperationObserver<? super D> derived) {
        this.mapper.removeDerivedStatistic((ChainedOperationObserver) derived);
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void begin() {
        this.mapper.begin();
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(D result) {
        this.mapper.end(result);
    }

    @Override // org.terracotta.statistics.observer.OperationObserver
    public void end(D result, long... parameters) {
        this.mapper.end(result, parameters);
    }

    private static <S extends Enum<S>> OperationStatistic<S> findOperationStat(Object rootNode, final Class<S> statisticType, String statName) {
        Query q = QueryBuilder.queryBuilder().descendants().filter(Matchers.context(Matchers.identifier(Matchers.subclassOf(OperationStatistic.class)))).filter(Matchers.context(Matchers.attributes(Matchers.allOf(Matchers.hasAttribute("name", statName), Matchers.hasAttribute(OgnlContext.THIS_CONTEXT_KEY, (Matcher<? extends Object>) new Matcher<OperationStatistic>() { // from class: org.terracotta.statistics.MappedOperationStatistic.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.terracotta.context.query.Matcher
            public boolean matchesSafely(OperationStatistic object) {
                return object.type().equals(statisticType);
            }
        }))))).build();
        Set<TreeNode> result = q.execute(Collections.singleton(ContextManager.nodeFor(rootNode)));
        if (result.size() != 1) {
            throw new RuntimeException("a single stat was expected; found " + result.size());
        }
        TreeNode node = result.iterator().next();
        return (OperationStatistic) node.getContext().attributes().get(OgnlContext.THIS_CONTEXT_KEY);
    }
}
