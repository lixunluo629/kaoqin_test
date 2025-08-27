package com.itextpdf.kernel.geom;

import com.itextpdf.io.util.HashCode;
import com.itextpdf.io.util.MessageFormatUtil;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/Point.class */
public class Point implements Serializable, Cloneable {
    private static final long serialVersionUID = -5276940640259749850L;
    public double x;
    public double y;

    public Point() {
        setLocation(0, 0);
    }

    public Point(int x, int y) {
        setLocation(x, y);
    }

    public Point(double x, double y) {
        setLocation(x, y);
    }

    public Point(Point p) {
        setLocation(p.x, p.y);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    public String toString() {
        return MessageFormatUtil.format("Point: [x={0},y={1}]", Double.valueOf(this.x), Double.valueOf(this.y));
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Point getLocation() {
        return new Point(this.x, this.y);
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    public void setLocation(int x, int y) {
        setLocation(x, y);
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double x, double y) {
        setLocation(x, y);
    }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public int hashCode() {
        HashCode hash = new HashCode();
        hash.append(getX());
        hash.append(getY());
        return hash.hashCode();
    }

    public static double distanceSq(double x1, double y1, double x2, double y2) {
        double x22 = x2 - x1;
        double y22 = y2 - y1;
        return (x22 * x22) + (y22 * y22);
    }

    public double distanceSq(double px, double py) {
        return distanceSq(getX(), getY(), px, py);
    }

    public double distanceSq(Point p) {
        return distanceSq(getX(), getY(), p.getX(), p.getY());
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSq(x1, y1, x2, y2));
    }

    public double distance(double px, double py) {
        return Math.sqrt(distanceSq(px, py));
    }

    public double distance(Point p) {
        return Math.sqrt(distanceSq(p));
    }

    public Object clone() {
        return new Point(this.x, this.y);
    }
}
