package com.graphbuilder.geom;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/geom/PointFactory.class */
public class PointFactory {

    /* loaded from: curvesapi-1.04.jar:com/graphbuilder/geom/PointFactory$Point2D.class */
    static class Point2D implements Point2d {
        double[] pts;

        public Point2D(double x, double y) {
            this.pts = new double[]{x, y};
        }

        @Override // com.graphbuilder.geom.Point2d
        public double getX() {
            return this.pts[0];
        }

        @Override // com.graphbuilder.geom.Point2d
        public double getY() {
            return this.pts[1];
        }

        @Override // com.graphbuilder.curve.Point
        public void setLocation(double[] p) {
            this.pts[0] = p[0];
            this.pts[1] = p[1];
        }

        @Override // com.graphbuilder.geom.Point2d
        public void setLocation(double x, double y) {
            this.pts[0] = x;
            this.pts[1] = y;
        }

        @Override // com.graphbuilder.curve.Point
        public double[] getLocation() {
            return this.pts;
        }
    }

    public static Point2d create(double x, double y) {
        return new Point2D(x, y);
    }
}
