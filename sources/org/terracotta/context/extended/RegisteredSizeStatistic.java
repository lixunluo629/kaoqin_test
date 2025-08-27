package org.terracotta.context.extended;

import org.terracotta.statistics.extended.ExpiringSampledStatistic;
import org.terracotta.statistics.extended.SampledStatistic;
import org.terracotta.statistics.extended.SamplingSupport;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredSizeStatistic.class */
public class RegisteredSizeStatistic implements RegisteredStatistic {
    private final ExpiringSampledStatistic<Long> sampledStatistic;

    public RegisteredSizeStatistic(ExpiringSampledStatistic<Long> sampledStatistic) {
        this.sampledStatistic = sampledStatistic;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public RegistrationType getType() {
        return RegistrationType.SIZE;
    }

    public SampledStatistic<Long> getSampledStatistic() {
        return this.sampledStatistic;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public SamplingSupport getSupport() {
        return this.sampledStatistic;
    }
}
