package org.terracotta.context.extended;

import java.lang.Enum;
import org.terracotta.statistics.extended.CompoundOperation;
import org.terracotta.statistics.extended.SamplingSupport;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredCompoundOperationStatistic.class */
public abstract class RegisteredCompoundOperationStatistic<T extends Enum<T>> implements RegisteredStatistic {
    private final CompoundOperation<T> compoundOperation;

    public RegisteredCompoundOperationStatistic(CompoundOperation<T> compoundOperation) {
        this.compoundOperation = compoundOperation;
    }

    public CompoundOperation<T> getCompoundOperation() {
        return this.compoundOperation;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public SamplingSupport getSupport() {
        return this.compoundOperation;
    }
}
