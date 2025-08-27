package org.springframework.data.geo;

import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/CustomMetric.class */
public class CustomMetric implements Metric {
    private static final long serialVersionUID = -2972074177454114228L;
    private final double multiplier;
    private final String abbreviation;

    public CustomMetric(double multiplier) {
        this(multiplier, "");
    }

    public CustomMetric(double multiplier, String abbreviation) {
        Assert.notNull(abbreviation, "Abbreviation must not be null!");
        this.multiplier = multiplier;
        this.abbreviation = abbreviation;
    }

    @Override // org.springframework.data.geo.Metric
    public double getMultiplier() {
        return this.multiplier;
    }

    @Override // org.springframework.data.geo.Metric
    public String getAbbreviation() {
        return this.abbreviation;
    }
}
