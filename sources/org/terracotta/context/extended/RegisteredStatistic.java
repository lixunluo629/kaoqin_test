package org.terracotta.context.extended;

import org.terracotta.statistics.extended.SamplingSupport;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredStatistic.class */
public interface RegisteredStatistic {
    SamplingSupport getSupport();

    RegistrationType getType();
}
