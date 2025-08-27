package org.springframework.data.geo;

import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/Box.class */
public class Box implements Shape {
    private static final long serialVersionUID = 8198095179084040711L;
    private final Point first;
    private final Point second;

    public Box(Point first, Point second) {
        Assert.notNull(first, "First point must not be null!");
        Assert.notNull(second, "Second point must not be null!");
        this.first = first;
        this.second = second;
    }

    public Box(double[] first, double[] second) {
        Assert.isTrue(first.length == 2, "Point array has to have 2 elements!");
        Assert.isTrue(second.length == 2, "Point array has to have 2 elements!");
        this.first = new Point(first[0], first[1]);
        this.second = new Point(second[0], second[1]);
    }

    public Point getFirst() {
        return this.first;
    }

    public Point getSecond() {
        return this.second;
    }

    public String toString() {
        return String.format("Box [%s, %s]", this.first, this.second);
    }

    public int hashCode() {
        int result = 31 + (17 * this.first.hashCode());
        return result + (17 * this.second.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Box)) {
            return false;
        }
        Box that = (Box) obj;
        return this.first.equals(that.first) && this.second.equals(that.second);
    }
}
