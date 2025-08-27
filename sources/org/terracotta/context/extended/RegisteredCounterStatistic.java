package org.terracotta.context.extended;

import org.terracotta.statistics.extended.ExpiringSampledStatistic;
import org.terracotta.statistics.extended.SampledStatistic;
import org.terracotta.statistics.extended.SamplingSupport;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredCounterStatistic.class */
public class RegisteredCounterStatistic implements RegisteredStatistic {
    private final ExpiringSampledStatistic<Long> sampledStatistic;

    public RegisteredCounterStatistic(ExpiringSampledStatistic<Long> sampledStatistic) {
        this.sampledStatistic = sampledStatistic;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public RegistrationType getType() {
        return RegistrationType.COUNTER;
    }

    public SampledStatistic<Long> getSampledStatistic() {
        return this.sampledStatistic;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public SamplingSupport getSupport() {
        return this.sampledStatistic;
    }
}
