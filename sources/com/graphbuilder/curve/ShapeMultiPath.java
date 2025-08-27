package com.graphbuilder.curve;

import com.graphbuilder.geom.Geom;
import com.graphbuilder.org.apache.harmony.awt.gl.Crossing;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/curve/ShapeMultiPath.class */
public class ShapeMultiPath extends MultiPath implements Shape {
    private int windingRule;
    private int ai0;
    private int ai1;

    public ShapeMultiPath() {
        super(2);
        this.windingRule = 0;
        this.ai0 = 0;
        this.ai1 = 1;
    }

    public ShapeMultiPath(int dimension) {
        super(dimension);
        this.windingRule = 0;
        this.ai0 = 0;
        this.ai1 = 1;
        if (dimension < 2) {
            throw new IllegalArgumentException("dimension >= 2 required");
        }
    }

    public void setBasisVectors(int[] b) {
        int b0 = b[0];
        int b1 = b[1];
        int dimension = getDimension();
        if (b0 < 0 || b1 < 0 || b0 >= dimension || b1 >= dimension) {
            throw new IllegalArgumentException("basis vectors must be >= 0 and < dimension");
        }
        this.ai0 = b0;
        this.ai1 = b1;
    }

    public int[] getBasisVectors() {
        return new int[]{this.ai0, this.ai1};
    }

    public double getDistSq(double x, double y) {
        int n = getNumPoints();
        if (n == 0) {
            return Double.MAX_VALUE;
        }
        double[] p = get(0);
        double x2 = p[this.ai0];
        double y2 = p[this.ai1];
        double dist = Double.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            double[] p2 = get(i);
            double x1 = p2[this.ai0];
            double y1 = p2[this.ai1];
            if (getType(i) == MultiPath.LINE_TO) {
                double d = Geom.ptSegDistSq(x1, y1, x2, y2, x, y, null);
                if (d < dist) {
                    dist = d;
                }
            }
            x2 = x1;
            y2 = y1;
        }
        return dist;
    }

    public int getWindingRule() {
        return this.windingRule;
    }

    public void setWindingRule(int rule) {
        if (rule != 0 && rule != 1) {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        }
        this.windingRule = rule;
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return new ShapeMultiPathIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new ShapeMultiPathIterator(this, at);
    }

    public Rectangle getBounds() {
        Rectangle2D r = getBounds2D();
        if (r == null) {
            return null;
        }
        return r.getBounds();
    }

    public Rectangle2D getBounds2D() {
        int n = getNumPoints();
        double x1 = Double.MAX_VALUE;
        double y1 = Double.MAX_VALUE;
        double x2 = -1.7976931348623157E308d;
        double y2 = -1.7976931348623157E308d;
        boolean defined = false;
        for (int i = 0; i < n; i++) {
            double[] p = get(i);
            boolean b = false;
            if (getType(i) == MultiPath.MOVE_TO) {
                if (i < n - 1 && getType(i + 1) == MultiPath.LINE_TO) {
                    b = true;
                }
            } else {
                b = true;
            }
            if (b) {
                defined = true;
                if (p[this.ai0] < x1) {
                    x1 = p[this.ai0];
                }
                if (p[this.ai1] < y1) {
                    y1 = p[this.ai1];
                }
                if (p[this.ai0] > x2) {
                    x2 = p[this.ai0];
                }
                if (p[this.ai1] > y2) {
                    y2 = p[this.ai1];
                }
            }
        }
        if (!defined) {
            return null;
        }
        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    public boolean contains(double x, double y) {
        int cross = Crossing.crossPath(getPathIterator(null), x, y);
        return this.windingRule == 1 ? cross != 0 : (cross & 1) != 0;
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(double x1, double y1, double w, double h) {
        int n;
        double x2 = x1 + w;
        double y2 = y1 + h;
        if (!contains(x1, y1) || !contains(x1, y2) || !contains(x2, y1) || !contains(x2, y2) || (n = getNumPoints()) == 0) {
            return false;
        }
        double[] p = get(0);
        double xb = p[this.ai0];
        double yb = p[this.ai1];
        for (int i = 1; i < n; i++) {
            double[] p2 = get(i);
            double xa = p2[this.ai0];
            double ya = p2[this.ai1];
            if (getType(i) == MultiPath.LINE_TO && (Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y1, x2, y1, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y1, x1, y2, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y2, x2, y2, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x2, y1, x2, y2, null) == Geom.INTERSECT)) {
                return false;
            }
            xb = xa;
            yb = ya;
        }
        return true;
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean intersects(double x1, double y1, double w, double h) {
        double x2 = x1 + w;
        double y2 = y1 + h;
        if (contains(x1, y1) || contains(x1, y2) || contains(x2, y1) || contains(x2, y2)) {
            return true;
        }
        int n = getNumPoints();
        if (n == 0) {
            return false;
        }
        double[] p = get(0);
        double xb = p[this.ai0];
        double yb = p[this.ai1];
        for (int i = 1; i < n; i++) {
            double[] p2 = get(i);
            double xa = p2[this.ai0];
            double ya = p2[this.ai1];
            if (getType(i) == MultiPath.LINE_TO) {
                if (Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y1, x2, y1, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y1, x1, y2, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x1, y2, x2, y2, null) == Geom.INTERSECT || Geom.getSegSegIntersection(xa, ya, xb, yb, x2, y1, x2, y2, null) == Geom.INTERSECT) {
                    return true;
                }
                if (xa >= x1 && ya >= y1 && xa <= x2 && ya <= y2) {
                    return true;
                }
                if (xb >= x1 && yb >= y1 && xb <= x2 && yb <= y2) {
                    return true;
                }
            }
            xb = xa;
            yb = ya;
        }
        return false;
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
}
