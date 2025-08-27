package org.terracotta.statistics.extended;

import java.lang.Enum;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/CountOperationImpl.class */
class CountOperationImpl<T extends Enum<T>> implements CountOperation<T> {
    private final CompoundOperation<T> compoundOperation;

    CountOperationImpl(CompoundOperation<T> compoundOperation) {
        this.compoundOperation = compoundOperation;
    }

    @Override // org.terracotta.statistics.extended.CountOperation
    public long value(T result) {
        return ((Long) this.compoundOperation.component(result).count().value()).longValue();
    }

    @Override // org.terracotta.statistics.extended.CountOperation
    public long value(T... results) {
        return ((Long) this.compoundOperation.compound(EnumSet.copyOf((Collection) Arrays.asList(results))).count().value()).longValue();
    }
}
