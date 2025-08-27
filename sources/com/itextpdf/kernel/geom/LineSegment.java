package com.itextpdf.kernel.geom;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/LineSegment.class */
public class LineSegment {
    private final Vector startPoint;
    private final Vector endPoint;

    public LineSegment(Vector startPoint, Vector endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Vector getStartPoint() {
        return this.startPoint;
    }

    public Vector getEndPoint() {
        return this.endPoint;
    }

    public float getLength() {
        return this.endPoint.subtract(this.startPoint).length();
    }

    public Rectangle getBoundingRectangle() {
        float x1 = getStartPoint().get(0);
        float y1 = getStartPoint().get(1);
        float x2 = getEndPoint().get(0);
        float y2 = getEndPoint().get(1);
        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public LineSegment transformBy(Matrix m) {
        Vector newStart = this.startPoint.cross(m);
        Vector newEnd = this.endPoint.cross(m);
        return new LineSegment(newStart, newEnd);
    }

    public boolean containsSegment(LineSegment other) {
        return other != null && containsPoint(other.startPoint) && containsPoint(other.endPoint);
    }

    public boolean containsPoint(Vector point) {
        if (point == null) {
            return false;
        }
        Vector diff1 = point.subtract(this.startPoint);
        if (diff1.get(0) < 0.0f || diff1.get(1) < 0.0f || diff1.get(2) < 0.0f) {
            return false;
        }
        Vector diff2 = this.endPoint.subtract(point);
        if (diff2.get(0) < 0.0f || diff2.get(1) < 0.0f || diff2.get(2) < 0.0f) {
            return false;
        }
        return true;
    }
}
