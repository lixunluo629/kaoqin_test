package org.springframework.data.keyvalue.core;

import java.util.Comparator;
import java.util.Iterator;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;
import org.springframework.util.comparator.CompoundComparator;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/SpelSortAccessor.class */
class SpelSortAccessor implements SortAccessor<Comparator<?>> {
    private final SpelExpressionParser parser;

    @Override // org.springframework.data.keyvalue.core.SortAccessor
    public /* bridge */ /* synthetic */ Comparator<?> resolve(KeyValueQuery keyValueQuery) {
        return resolve((KeyValueQuery<?>) keyValueQuery);
    }

    public SpelSortAccessor(SpelExpressionParser parser) {
        Assert.notNull(parser, "SpelExpressionParser must not be null!");
        this.parser = parser;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.keyvalue.core.SortAccessor
    public Comparator<?> resolve(KeyValueQuery<?> query) {
        if (query == null || query.getSort() == null) {
            return null;
        }
        CompoundComparator compoundComperator = new CompoundComparator();
        Iterator<Sort.Order> it = query.getSort().iterator();
        while (it.hasNext()) {
            Sort.Order order = it.next();
            SpelPropertyComparator<?> spelSort = new SpelPropertyComparator<>(order.getProperty(), this.parser);
            if (Sort.Direction.DESC.equals(order.getDirection())) {
                spelSort.desc();
                if (order.getNullHandling() != null && !Sort.NullHandling.NATIVE.equals(order.getNullHandling())) {
                    spelSort = Sort.NullHandling.NULLS_FIRST.equals(order.getNullHandling()) ? spelSort.nullsFirst() : spelSort.nullsLast();
                }
            }
            compoundComperator.addComparator(spelSort);
        }
        return compoundComperator;
    }
}
