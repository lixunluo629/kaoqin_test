package org.springframework.data.geo;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.Serializable;
import org.springframework.data.domain.Range;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Distance.class */
public class Distance implements Serializable, Comparable<Distance> {
    private static final long serialVersionUID = 2460886201934027744L;
    private final double value;
    private final Metric metric;

    public Distance(double value) {
        this(value, Metrics.NEUTRAL);
    }

    public Distance(double value, Metric metric) {
        this.value = value;
        this.metric = metric == null ? Metrics.NEUTRAL : metric;
    }

    public static Range<Distance> between(Distance min, Distance max) {
        return new Range<>(min, max);
    }

    public static Range<Distance> between(double minValue, Metric minMetric, double maxValue, Metric maxMetric) {
        return between(new Distance(minValue, minMetric), new Distance(maxValue, maxMetric));
    }

    public double getValue() {
        return this.value;
    }

    public double getNormalizedValue() {
        return this.value / this.metric.getMultiplier();
    }

    public Metric getMetric() {
        return this.metric;
    }

    public String getUnit() {
        return this.metric.getAbbreviation();
    }

    public Distance add(Distance other) {
        Assert.notNull(other, "Distance to add must not be null!");
        double newNormalizedValue = getNormalizedValue() + other.getNormalizedValue();
        return new Distance(newNormalizedValue * this.metric.getMultiplier(), this.metric);
    }

    public Distance add(Distance other, Metric metric) {
        Assert.notNull(other, "Distance to must not be null!");
        Assert.notNull(metric, "Result metric must not be null!");
        double newLeft = getNormalizedValue() * metric.getMultiplier();
        double newRight = other.getNormalizedValue() * metric.getMultiplier();
        return new Distance(newLeft + newRight, metric);
    }

    public Distance in(Metric metric) {
        Assert.notNull(metric, "Metric must not be null!");
        return this.metric.equals(metric) ? this : new Distance(getNormalizedValue() * metric.getMultiplier(), metric);
    }

    @Override // java.lang.Comparable
    public int compareTo(Distance o) {
        double difference = getNormalizedValue() - o.getNormalizedValue();
        if (difference == 0.0d) {
            return 0;
        }
        return difference > 0.0d ? 1 : -1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Distance)) {
            return false;
        }
        Distance that = (Distance) obj;
        return this.value == that.value && this.metric.equals(that.metric);
    }

    public int hashCode() {
        int result = (int) (17 + (31 * Double.doubleToLongBits(this.value)));
        return result + (31 * this.metric.hashCode());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.value);
        if (this.metric != Metrics.NEUTRAL) {
            builder.append(SymbolConstants.SPACE_SYMBOL).append(this.metric.toString());
        }
        return builder.toString();
    }
}
