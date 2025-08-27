package org.springframework.data.geo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoResults.class */
public class GeoResults<T> implements Iterable<GeoResult<T>>, Serializable {
    private static final long serialVersionUID = 8347363491300219485L;
    private final List<? extends GeoResult<T>> results;
    private final Distance averageDistance;

    public GeoResults(List<? extends GeoResult<T>> results) {
        this(results, (Metric) null);
    }

    public GeoResults(List<? extends GeoResult<T>> results, Metric metric) {
        this(results, calculateAverageDistance(results, metric));
    }

    @PersistenceConstructor
    public GeoResults(List<? extends GeoResult<T>> results, Distance averageDistance) {
        Assert.notNull(results, "Results must not be null!");
        this.results = results;
        this.averageDistance = averageDistance;
    }

    public Distance getAverageDistance() {
        return this.averageDistance;
    }

    public List<GeoResult<T>> getContent() {
        return Collections.unmodifiableList(this.results);
    }

    @Override // java.lang.Iterable
    public Iterator<GeoResult<T>> iterator() {
        return this.results.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeoResults)) {
            return false;
        }
        GeoResults<?> that = (GeoResults) obj;
        return this.results.equals(that.results) && this.averageDistance.equals(that.averageDistance);
    }

    public int hashCode() {
        int result = 17 + (31 * this.results.hashCode());
        return result + (31 * this.averageDistance.hashCode());
    }

    public String toString() {
        return String.format("GeoResults: [averageDistance: %s, results: %s]", this.averageDistance.toString(), StringUtils.collectionToCommaDelimitedString(this.results));
    }

    private static Distance calculateAverageDistance(List<? extends GeoResult<?>> results, Metric metric) {
        if (results.isEmpty()) {
            return new Distance(0.0d, metric);
        }
        double averageDistance = 0.0d;
        for (GeoResult<?> result : results) {
            averageDistance += result.getDistance().getValue();
        }
        return new Distance(averageDistance / results.size(), metric);
    }
}
