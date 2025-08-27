package org.springframework.data.geo;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Metrics.class */
public enum Metrics implements Metric {
    KILOMETERS(6378.137d, "km"),
    MILES(3963.191d, "mi"),
    NEUTRAL(1.0d, "");

    private final double multiplier;
    private final String abbreviation;

    Metrics(double multiplier, String abbreviation) {
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
