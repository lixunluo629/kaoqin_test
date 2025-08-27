package com.itextpdf.kernel.geom;

import java.util.ArrayList;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/Line.class */
public class Line implements IShape {
    private static final long serialVersionUID = 4796508543986646437L;
    private final Point p1;
    private final Point p2;

    public Line() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Line(float x1, float y1, float x2, float y2) {
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x2, y2);
    }

    public Line(Point p1, Point p2) {
        this((float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY());
    }

    @Override // com.itextpdf.kernel.geom.IShape
    public List<Point> getBasePoints() {
        List<Point> basePoints = new ArrayList<>(2);
        basePoints.add(this.p1);
        basePoints.add(this.p2);
        return basePoints;
    }
}
