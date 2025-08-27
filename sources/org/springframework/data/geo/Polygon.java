package org.springframework.data.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Polygon.class */
public class Polygon implements Iterable<Point>, Shape {
    private static final long serialVersionUID = -2705040068154648988L;
    private final List<Point> points;

    public Polygon(Point x, Point y, Point z, Point... others) {
        Assert.notNull(x, "X coordinate must not be null!");
        Assert.notNull(y, "Y coordinate must not be null!");
        Assert.notNull(z, "Z coordinate must not be null!");
        Assert.notNull(others, "Others must not be null!");
        List<Point> points = new ArrayList<>(3 + others.length);
        points.addAll(Arrays.asList(x, y, z));
        points.addAll(Arrays.asList(others));
        this.points = Collections.unmodifiableList(points);
    }

    @PersistenceConstructor
    public Polygon(List<? extends Point> points) {
        Assert.notNull(points, "Points must not be null!");
        List<Point> pointsToSet = new ArrayList<>(points.size());
        for (Point point : points) {
            Assert.notNull(point, "Single Point in Polygon must not be null!");
            pointsToSet.add(point);
        }
        this.points = Collections.unmodifiableList(pointsToSet);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    @Override // java.lang.Iterable
    public Iterator<Point> iterator() {
        return this.points.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Polygon)) {
            return false;
        }
        Polygon that = (Polygon) obj;
        return this.points.equals(that.points);
    }

    public int hashCode() {
        return this.points.hashCode();
    }

    public String toString() {
        return String.format("Polygon: [%s]", StringUtils.collectionToCommaDelimitedString(this.points));
    }
}
