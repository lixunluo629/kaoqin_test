package org.springframework.data.geo;

import java.io.Serializable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Metric.class */
public interface Metric extends Serializable {
    double getMultiplier();

    String getAbbreviation();
}
