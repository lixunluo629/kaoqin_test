package org.springframework.data.geo;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Circle.class */
public class Circle implements Shape {
    private static final long serialVersionUID = 5215611530535947924L;
    private final Point center;
    private final Distance radius;

    @PersistenceConstructor
    public Circle(Point center, Distance radius) {
        Assert.notNull(center, "Center point must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        Assert.isTrue(radius.getValue() >= 0.0d, "Radius must not be negative!");
        this.center = center;
        this.radius = radius;
    }

    public Circle(Point center, double radius) {
        this(center, new Distance(radius));
    }

    public Circle(double centerX, double centerY, double radius) {
        this(new Point(centerX, centerY), new Distance(radius));
    }

    public Point getCenter() {
        return this.center;
    }

    public Distance getRadius() {
        return this.radius;
    }

    public String toString() {
        return String.format("Circle: [center=%s, radius=%s]", this.center, this.radius);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Circle)) {
            return false;
        }
        Circle that = (Circle) obj;
        return this.center.equals(that.center) && this.radius.equals(that.radius);
    }

    public int hashCode() {
        int result = 17 + (31 * this.center.hashCode());
        return result + (31 * this.radius.hashCode());
    }
}
