package org.terracotta.statistics;

import java.lang.Number;
import java.util.HashMap;
import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/ConstantValueStatistic.class */
public class ConstantValueStatistic<T extends Number> implements ValueStatistic<T> {
    private static final Map<Object, ValueStatistic<?>> common = new HashMap();
    private final T value;

    static {
        common.put(0, new ConstantValueStatistic(0));
        common.put(0L, new ConstantValueStatistic(0L));
        common.put(null, new ConstantValueStatistic(null));
    }

    public static <T extends Number> ValueStatistic<T> instance(T value) {
        ValueStatistic<T> interned = (ValueStatistic) common.get(value);
        if (interned == null) {
            return new ConstantValueStatistic(value);
        }
        return interned;
    }

    private ConstantValueStatistic(T value) {
        this.value = value;
    }

    @Override // org.terracotta.statistics.ValueStatistic
    public T value() {
        return this.value;
    }
}
