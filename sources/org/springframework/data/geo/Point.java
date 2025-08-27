package org.springframework.data.geo;

import java.io.Serializable;
import java.util.Locale;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Point.class */
public class Point implements Serializable {
    private static final long serialVersionUID = 3583151228933783558L;
    private final double x;
    private final double y;

    @PersistenceConstructor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        Assert.notNull(point, "Source point must not be null!");
        this.x = point.x;
        this.y = point.y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (31 * 1) + ((int) (temp ^ (temp >>> 32)));
        long temp2 = Double.doubleToLongBits(this.y);
        return (31 * result) + ((int) (temp2 ^ (temp2 >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point other = (Point) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x) || Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "Point [x=%f, y=%f]", Double.valueOf(this.x), Double.valueOf(this.y));
    }
}
