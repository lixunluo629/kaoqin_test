package org.springframework.data.geo;

import java.io.Serializable;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoResult.class */
public class GeoResult<T> implements Serializable {
    private static final long serialVersionUID = 1637452570977581370L;
    private final T content;
    private final Distance distance;

    public GeoResult(T content, Distance distance) {
        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(distance, "Distance must not be null!");
        this.content = content;
        this.distance = distance;
    }

    public T getContent() {
        return this.content;
    }

    public Distance getDistance() {
        return this.distance;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeoResult)) {
            return false;
        }
        GeoResult<?> that = (GeoResult) obj;
        return this.content.equals(that.content) && this.distance.equals(that.distance);
    }

    public int hashCode() {
        int result = 17 + (31 * this.distance.hashCode());
        return result + (31 * this.content.hashCode());
    }

    public String toString() {
        return String.format("GeoResult [content: %s, distance: %s, ]", this.content.toString(), this.distance.toString());
    }
}
