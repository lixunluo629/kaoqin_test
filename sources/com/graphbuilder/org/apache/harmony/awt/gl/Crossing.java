package com.graphbuilder.org.apache.harmony.awt.gl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/org/apache/harmony/awt/gl/Crossing.class */
public class Crossing {
    static final double DELTA = 1.0E-5d;
    static final double ROOT_DELTA = 1.0E-10d;
    public static final int CROSSING = 255;
    static final int UNKNOWN = 254;

    public static int solveQuad(double[] eqn, double[] res) {
        int rc;
        double a = eqn[2];
        double b = eqn[1];
        double c = eqn[0];
        if (a != 0.0d) {
            double d = (b * b) - ((4.0d * a) * c);
            if (d < 0.0d) {
                return 0;
            }
            double d2 = Math.sqrt(d);
            rc = 0 + 1;
            res[0] = ((-b) + d2) / (a * 2.0d);
            if (d2 != 0.0d) {
                rc++;
                res[rc] = ((-b) - d2) / (a * 2.0d);
            }
        } else if (b != 0.0d) {
            rc = 0 + 1;
            res[0] = (-c) / b;
        } else {
            return -1;
        }
        return fixRoots(res, rc);
    }

    public static int solveCubic(double[] eqn, double[] res) {
        int rc;
        double d = eqn[3];
        if (d == 0.0d) {
            return solveQuad(eqn, res);
        }
        double a = eqn[2] / d;
        double b = eqn[1] / d;
        double c = eqn[0] / d;
        double Q = ((a * a) - (3.0d * b)) / 9.0d;
        double R = (((((2.0d * a) * a) * a) - ((9.0d * a) * b)) + (27.0d * c)) / 54.0d;
        double Q3 = Q * Q * Q;
        double R2 = R * R;
        double n = (-a) / 3.0d;
        if (R2 < Q3) {
            double t = Math.acos(R / Math.sqrt(Q3)) / 3.0d;
            double m = (-2.0d) * Math.sqrt(Q);
            int rc2 = 0 + 1;
            res[0] = (m * Math.cos(t)) + n;
            int rc3 = rc2 + 1;
            res[rc2] = (m * Math.cos(t + 2.0943951023931953d)) + n;
            rc = rc3 + 1;
            res[rc3] = (m * Math.cos(t - 2.0943951023931953d)) + n;
        } else {
            double A = Math.pow(Math.abs(R) + Math.sqrt(R2 - Q3), 0.3333333333333333d);
            if (R > 0.0d) {
                A = -A;
            }
            if (-1.0E-10d < A && A < ROOT_DELTA) {
                rc = 0 + 1;
                res[0] = n;
            } else {
                double B = Q / A;
                rc = 0 + 1;
                res[0] = A + B + n;
                double delta = R2 - Q3;
                if (-1.0E-10d < delta && delta < ROOT_DELTA) {
                    rc++;
                    res[rc] = ((-(A + B)) / 2.0d) + n;
                }
            }
        }
        return fixRoots(res, rc);
    }

    static int fixRoots(double[] res, int rc) {
        int tc = 0;
        for (int i = 0; i < rc; i++) {
            int j = i + 1;
            while (true) {
                if (j < rc) {
                    if (isZero(res[i] - res[j])) {
                        break;
                    }
                    j++;
                } else {
                    int i2 = tc;
                    tc++;
                    res[i2] = res[i];
                    break;
                }
            }
        }
        return tc;
    }

    /* loaded from: curvesapi-1.04.jar:com/graphbuilder/org/apache/harmony/awt/gl/Crossing$QuadCurve.class */
    public static class QuadCurve {
        double ax;
        double ay;
        double bx;
        double by;
        double Ax;
        double Ay;
        double Bx;
        double By;

        public QuadCurve(double x1, double y1, double cx, double cy, double x2, double y2) {
            this.ax = x2 - x1;
            this.ay = y2 - y1;
            this.bx = cx - x1;
            this.by = cy - y1;
            this.Bx = this.bx + this.bx;
            this.Ax = this.ax - this.Bx;
            this.By = this.by + this.by;
            this.Ay = this.ay - this.By;
        }

        int cross(double[] res, int rc, double py1, double py2) {
            int cross = 0;
            for (int i = 0; i < rc; i++) {
                double t = res[i];
                if (t >= -1.0E-5d && t <= 1.00001d) {
                    if (t < Crossing.DELTA) {
                        if (py1 < 0.0d) {
                            if ((this.bx != 0.0d ? this.bx : this.ax - this.bx) < 0.0d) {
                                cross--;
                            }
                        }
                    } else if (t <= 0.99999d) {
                        double ry = t * ((t * this.Ay) + this.By);
                        if (ry > py2) {
                            double rxt = (t * this.Ax) + this.bx;
                            if (rxt <= -1.0E-5d || rxt >= Crossing.DELTA) {
                                cross += rxt > 0.0d ? 1 : -1;
                            }
                        }
                    } else if (py1 < this.ay) {
                        if ((this.ax != this.bx ? this.ax - this.bx : this.bx) > 0.0d) {
                            cross++;
                        }
                    }
                }
            }
            return cross;
        }

        int solvePoint(double[] res, double px) {
            double[] eqn = {-px, this.Bx, this.Ax};
            return Crossing.solveQuad(eqn, res);
        }

        int solveExtrem(double[] res) {
            int rc = 0;
            if (this.Ax != 0.0d) {
                rc = 0 + 1;
                res[0] = (-this.Bx) / (this.Ax + this.Ax);
            }
            if (this.Ay != 0.0d) {
                int i = rc;
                rc++;
                res[i] = (-this.By) / (this.Ay + this.Ay);
            }
            return rc;
        }

        int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
            for (int i = 0; i < rc; i++) {
                double t = res[i];
                if (t > -1.0E-5d && t < 1.00001d) {
                    double rx = t * ((t * this.Ax) + this.Bx);
                    if (minX <= rx && rx <= maxX) {
                        int i2 = bc;
                        int bc2 = bc + 1;
                        bound[i2] = t;
                        int bc3 = bc2 + 1;
                        bound[bc2] = rx;
                        int bc4 = bc3 + 1;
                        bound[bc3] = t * ((t * this.Ay) + this.By);
                        bc = bc4 + 1;
                        bound[bc4] = id;
                        if (changeId) {
                            id++;
                        }
                    }
                }
            }
            return bc;
        }
    }

    /* loaded from: curvesapi-1.04.jar:com/graphbuilder/org/apache/harmony/awt/gl/Crossing$CubicCurve.class */
    public static class CubicCurve {
        double ax;
        double ay;
        double bx;
        double by;
        double cx;
        double cy;
        double Ax;
        double Ay;
        double Bx;
        double By;
        double Cx;
        double Cy;
        double Ax3;
        double Bx2;

        public CubicCurve(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
            this.ax = x2 - x1;
            this.ay = y2 - y1;
            this.bx = cx1 - x1;
            this.by = cy1 - y1;
            this.cx = cx2 - x1;
            this.cy = cy2 - y1;
            this.Cx = this.bx + this.bx + this.bx;
            this.Bx = (((this.cx + this.cx) + this.cx) - this.Cx) - this.Cx;
            this.Ax = (this.ax - this.Bx) - this.Cx;
            this.Cy = this.by + this.by + this.by;
            this.By = (((this.cy + this.cy) + this.cy) - this.Cy) - this.Cy;
            this.Ay = (this.ay - this.By) - this.Cy;
            this.Ax3 = this.Ax + this.Ax + this.Ax;
            this.Bx2 = this.Bx + this.Bx;
        }

        /* JADX WARN: Removed duplicated region for block: B:52:0x0143  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x0147  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        int cross(double[] r10, int r11, double r12, double r14) {
            /*
                Method dump skipped, instructions count: 340
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.graphbuilder.org.apache.harmony.awt.gl.Crossing.CubicCurve.cross(double[], int, double, double):int");
        }

        int solvePoint(double[] res, double px) {
            double[] eqn = {-px, this.Cx, this.Bx, this.Ax};
            return Crossing.solveCubic(eqn, res);
        }

        int solveExtremX(double[] res) {
            double[] eqn = {this.Cx, this.Bx2, this.Ax3};
            return Crossing.solveQuad(eqn, res);
        }

        int solveExtremY(double[] res) {
            double[] eqn = {this.Cy, this.By + this.By, this.Ay + this.Ay + this.Ay};
            return Crossing.solveQuad(eqn, res);
        }

        int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
            for (int i = 0; i < rc; i++) {
                double t = res[i];
                if (t > -1.0E-5d && t < 1.00001d) {
                    double rx = t * ((t * ((t * this.Ax) + this.Bx)) + this.Cx);
                    if (minX <= rx && rx <= maxX) {
                        int i2 = bc;
                        int bc2 = bc + 1;
                        bound[i2] = t;
                        int bc3 = bc2 + 1;
                        bound[bc2] = rx;
                        int bc4 = bc3 + 1;
                        bound[bc3] = t * ((t * ((t * this.Ay) + this.By)) + this.Cy);
                        bc = bc4 + 1;
                        bound[bc4] = id;
                        if (changeId) {
                            id++;
                        }
                    }
                }
            }
            return bc;
        }
    }

    public static int crossLine(double x1, double y1, double x2, double y2, double x, double y) {
        if (x < x1 && x < x2) {
            return 0;
        }
        if (x > x1 && x > x2) {
            return 0;
        }
        if ((y > y1 && y > y2) || x1 == x2) {
            return 0;
        }
        if ((y >= y1 || y >= y2) && ((y2 - y1) * (x - x1)) / (x2 - x1) <= y - y1) {
            return 0;
        }
        return x == x1 ? x1 < x2 ? 0 : -1 : x == x2 ? x1 < x2 ? 1 : 0 : x1 < x2 ? 1 : -1;
    }

    public static int crossQuad(double x1, double y1, double cx, double cy, double x2, double y2, double x, double y) {
        if (x < x1 && x < cx && x < x2) {
            return 0;
        }
        if (x > x1 && x > cx && x > x2) {
            return 0;
        }
        if (y > y1 && y > cy && y > y2) {
            return 0;
        }
        if (x1 == cx && cx == x2) {
            return 0;
        }
        if (y < y1 && y < cy && y < y2 && x != x1 && x != x2) {
            return x1 < x2 ? (x1 >= x || x >= x2) ? 0 : 1 : (x2 >= x || x >= x1) ? 0 : -1;
        }
        QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
        double px = x - x1;
        double py = y - y1;
        double[] res = new double[3];
        int rc = c.solvePoint(res, px);
        return c.cross(res, rc, py, py);
    }

    public static int crossCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double x, double y) {
        if (x < x1 && x < cx1 && x < cx2 && x < x2) {
            return 0;
        }
        if (x > x1 && x > cx1 && x > cx2 && x > x2) {
            return 0;
        }
        if (y > y1 && y > cy1 && y > cy2 && y > y2) {
            return 0;
        }
        if (x1 == cx1 && cx1 == cx2 && cx2 == x2) {
            return 0;
        }
        if (y < y1 && y < cy1 && y < cy2 && y < y2 && x != x1 && x != x2) {
            return x1 < x2 ? (x1 >= x || x >= x2) ? 0 : 1 : (x2 >= x || x >= x1) ? 0 : -1;
        }
        CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
        double px = x - x1;
        double py = y - y1;
        double[] res = new double[3];
        int rc = c.solvePoint(res, px);
        return c.cross(res, rc, py, py);
    }

    public static int crossPath(PathIterator pathIterator, double d, double d2) {
        int iCrossLine = 0;
        double d3 = 0.0d;
        double d4 = 0.0d;
        double d5 = 0.0d;
        double d6 = 0.0d;
        double[] dArr = new double[6];
        while (true) {
            if (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(dArr)) {
                    case 0:
                        if (d4 != d6 || d3 != d5) {
                            iCrossLine += crossLine(d4, d3, d6, d5, d, d2);
                        }
                        double d7 = dArr[0];
                        d4 = d7;
                        d6 = d7;
                        double d8 = dArr[1];
                        d3 = d8;
                        d5 = d8;
                        break;
                    case 1:
                        int i = iCrossLine;
                        double d9 = d4;
                        double d10 = d3;
                        double d11 = dArr[0];
                        d4 = i == true ? 1 : 0;
                        double d12 = dArr[1];
                        d3 = i == true ? 1 : 0;
                        iCrossLine = (i == true ? 1 : 0) + crossLine(d9, d10, d11, d12, d, d2);
                        break;
                    case 2:
                        int i2 = iCrossLine;
                        double d13 = d4;
                        double d14 = d3;
                        double d15 = dArr[0];
                        double d16 = dArr[1];
                        double d17 = dArr[2];
                        d4 = i2 == true ? 1 : 0;
                        double d18 = dArr[3];
                        d3 = i2 == true ? 1 : 0;
                        iCrossLine = (i2 == true ? 1 : 0) + crossQuad(d13, d14, d15, d16, d17, d18, d, d2);
                        break;
                    case 3:
                        int i3 = iCrossLine;
                        double d19 = d4;
                        double d20 = d3;
                        double d21 = dArr[0];
                        double d22 = dArr[1];
                        double d23 = dArr[2];
                        double d24 = dArr[3];
                        double d25 = dArr[4];
                        d4 = i3 == true ? 1 : 0;
                        double d26 = dArr[5];
                        d3 = i3 == true ? 1 : 0;
                        iCrossLine = (i3 == true ? 1 : 0) + crossCubic(d19, d20, d21, d22, d23, d24, d25, d26, d, d2);
                        break;
                    case 4:
                        if (d3 != d5 || d4 != d6) {
                            int i4 = iCrossLine;
                            double d27 = d3;
                            double d28 = d6;
                            d4 = d28;
                            double d29 = d5;
                            d3 = d29;
                            iCrossLine = i4 + crossLine(i4, d27, d28, d29, d, d2);
                            break;
                        }
                        break;
                }
                if (d == d4 && d2 == d3) {
                    iCrossLine = 0;
                    d3 = d5;
                } else {
                    pathIterator.next();
                }
            }
        }
        if (d3 != d5) {
            iCrossLine += crossLine(d4, d3, d6, d5, d, d2);
        }
        return iCrossLine;
    }

    public static int crossShape(Shape s, double x, double y) {
        if (!s.getBounds2D().contains(x, y)) {
            return 0;
        }
        return crossPath(s.getPathIterator((AffineTransform) null), x, y);
    }

    public static boolean isZero(double val) {
        return -1.0E-5d < val && val < DELTA;
    }

    static void sortBound(double[] bound, int bc) {
        for (int i = 0; i < bc - 4; i += 4) {
            int k = i;
            for (int j = i + 4; j < bc; j += 4) {
                if (bound[k] > bound[j]) {
                    k = j;
                }
            }
            if (k != i) {
                double tmp = bound[i];
                bound[i] = bound[k];
                bound[k] = tmp;
                double tmp2 = bound[i + 1];
                bound[i + 1] = bound[k + 1];
                bound[k + 1] = tmp2;
                double tmp3 = bound[i + 2];
                bound[i + 2] = bound[k + 2];
                bound[k + 2] = tmp3;
                double tmp4 = bound[i + 3];
                bound[i + 3] = bound[k + 3];
                bound[k + 3] = tmp4;
            }
        }
    }

    static int crossBound(double[] bound, int bc, double py1, double py2) {
        if (bc == 0) {
            return 0;
        }
        int up = 0;
        int down = 0;
        for (int i = 2; i < bc; i += 4) {
            if (bound[i] < py1) {
                up++;
            } else if (bound[i] > py2) {
                down++;
            } else {
                return 255;
            }
        }
        if (down == 0) {
            return 0;
        }
        if (up != 0) {
            sortBound(bound, bc);
            boolean sign = bound[2] > py2;
            for (int i2 = 6; i2 < bc; i2 += 4) {
                boolean sign2 = bound[i2] > py2;
                if (sign != sign2 && bound[i2 + 1] != bound[i2 - 3]) {
                    return 255;
                }
                sign = sign2;
            }
            return 254;
        }
        return 254;
    }

    public static int intersectLine(double x1, double y1, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        double bx1;
        double bx2;
        if (rx2 < x1 && rx2 < x2) {
            return 0;
        }
        if (rx1 > x1 && rx1 > x2) {
            return 0;
        }
        if (ry1 > y1 && ry1 > y2) {
            return 0;
        }
        if (ry2 >= y1 || ry2 >= y2) {
            if (x1 == x2) {
                return 255;
            }
            if (x1 < x2) {
                bx1 = x1 < rx1 ? rx1 : x1;
                bx2 = x2 < rx2 ? x2 : rx2;
            } else {
                bx1 = x2 < rx1 ? rx1 : x2;
                bx2 = x1 < rx2 ? x1 : rx2;
            }
            double k = (y2 - y1) / (x2 - x1);
            double by1 = (k * (bx1 - x1)) + y1;
            double by2 = (k * (bx2 - x1)) + y1;
            if (by1 < ry1 && by2 < ry1) {
                return 0;
            }
            if (by1 <= ry2 || by2 <= ry2) {
                return 255;
            }
        }
        if (x1 == x2) {
            return 0;
        }
        return rx1 == x1 ? x1 < x2 ? 0 : -1 : rx1 == x2 ? x1 < x2 ? 1 : 0 : x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
    }

    public static int intersectQuad(double x1, double y1, double cx, double cy, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        if (rx2 < x1 && rx2 < cx && rx2 < x2) {
            return 0;
        }
        if (rx1 > x1 && rx1 > cx && rx1 > x2) {
            return 0;
        }
        if (ry1 > y1 && ry1 > cy && ry1 > y2) {
            return 0;
        }
        if (ry2 < y1 && ry2 < cy && ry2 < y2 && rx1 != x1 && rx1 != x2) {
            return x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
        }
        QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
        double px1 = rx1 - x1;
        double py1 = ry1 - y1;
        double px2 = rx2 - x1;
        double py2 = ry2 - y1;
        double[] res1 = new double[3];
        double[] res2 = new double[3];
        int rc1 = c.solvePoint(res1, px1);
        int rc2 = c.solvePoint(res2, px2);
        if (rc1 == 0 && rc2 == 0) {
            return 0;
        }
        double minX = px1 - DELTA;
        double maxX = px2 + DELTA;
        double[] bound = new double[28];
        int bc = c.addBound(bound, c.addBound(bound, c.addBound(bound, 0, res1, rc1, minX, maxX, false, 0), res2, rc2, minX, maxX, false, 1), res2, c.solveExtrem(res2), minX, maxX, true, 2);
        if (rx1 < x1 && x1 < rx2) {
            int bc2 = bc + 1;
            bound[bc] = 0.0d;
            int bc3 = bc2 + 1;
            bound[bc2] = 0.0d;
            int bc4 = bc3 + 1;
            bound[bc3] = 0.0d;
            bc = bc4 + 1;
            bound[bc4] = 4.0d;
        }
        if (rx1 < x2 && x2 < rx2) {
            int i = bc;
            int bc5 = bc + 1;
            bound[i] = 1.0d;
            int bc6 = bc5 + 1;
            bound[bc5] = c.ax;
            int bc7 = bc6 + 1;
            bound[bc6] = c.ay;
            bc = bc7 + 1;
            bound[bc7] = 5.0d;
        }
        int cross = crossBound(bound, bc, py1, py2);
        if (cross != 254) {
            return cross;
        }
        return c.cross(res1, rc1, py1, py2);
    }

    public static int intersectCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        if (rx2 < x1 && rx2 < cx1 && rx2 < cx2 && rx2 < x2) {
            return 0;
        }
        if (rx1 > x1 && rx1 > cx1 && rx1 > cx2 && rx1 > x2) {
            return 0;
        }
        if (ry1 > y1 && ry1 > cy1 && ry1 > cy2 && ry1 > y2) {
            return 0;
        }
        if (ry2 < y1 && ry2 < cy1 && ry2 < cy2 && ry2 < y2 && rx1 != x1 && rx1 != x2) {
            return x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
        }
        CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
        double px1 = rx1 - x1;
        double py1 = ry1 - y1;
        double px2 = rx2 - x1;
        double py2 = ry2 - y1;
        double[] res1 = new double[3];
        double[] res2 = new double[3];
        int rc1 = c.solvePoint(res1, px1);
        int rc2 = c.solvePoint(res2, px2);
        if (rc1 == 0 && rc2 == 0) {
            return 0;
        }
        double minX = px1 - DELTA;
        double maxX = px2 + DELTA;
        double[] bound = new double[40];
        int bc = c.addBound(bound, c.addBound(bound, c.addBound(bound, c.addBound(bound, 0, res1, rc1, minX, maxX, false, 0), res2, rc2, minX, maxX, false, 1), res2, c.solveExtremX(res2), minX, maxX, true, 2), res2, c.solveExtremY(res2), minX, maxX, true, 4);
        if (rx1 < x1 && x1 < rx2) {
            int bc2 = bc + 1;
            bound[bc] = 0.0d;
            int bc3 = bc2 + 1;
            bound[bc2] = 0.0d;
            int bc4 = bc3 + 1;
            bound[bc3] = 0.0d;
            bc = bc4 + 1;
            bound[bc4] = 6.0d;
        }
        if (rx1 < x2 && x2 < rx2) {
            int i = bc;
            int bc5 = bc + 1;
            bound[i] = 1.0d;
            int bc6 = bc5 + 1;
            bound[bc5] = c.ax;
            int bc7 = bc6 + 1;
            bound[bc6] = c.ay;
            bc = bc7 + 1;
            bound[bc7] = 7.0d;
        }
        int cross = crossBound(bound, bc, py1, py2);
        if (cross != 254) {
            return cross;
        }
        return c.cross(res1, rc1, py1, py2);
    }

    public static int intersectPath(PathIterator p, double x, double y, double w, double h) {
        int cross = 0;
        double cy = 0.0d;
        double cx = 0.0d;
        double my = 0.0d;
        double mx = 0.0d;
        double[] coords = new double[6];
        double rx2 = x + w;
        double ry2 = y + h;
        while (!p.isDone()) {
            int count = 0;
            switch (p.currentSegment(coords)) {
                case 0:
                    if (cx != mx || cy != my) {
                        count = intersectLine(cx, cy, mx, my, x, y, rx2, ry2);
                    }
                    double d = coords[0];
                    cx = d;
                    mx = d;
                    double d2 = coords[1];
                    cy = d2;
                    my = d2;
                    break;
                case 1:
                    double d3 = cx;
                    cx = d3;
                    cy = d3;
                    count = intersectLine(d3, cy, coords[0], coords[1], x, y, rx2, ry2);
                    break;
                case 2:
                    double d4 = cx;
                    cx = d4;
                    cy = d4;
                    count = intersectQuad(d4, cy, coords[0], coords[1], coords[2], coords[3], x, y, rx2, ry2);
                    break;
                case 3:
                    double d5 = cx;
                    cx = d5;
                    cy = d5;
                    count = intersectCubic(d5, cy, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], x, y, rx2, ry2);
                    break;
                case 4:
                    if (cy != my || cx != mx) {
                        count = intersectLine(cx, cy, mx, my, x, y, rx2, ry2);
                    }
                    cx = mx;
                    cy = my;
                    break;
            }
            if (count == 255) {
                return 255;
            }
            cross += count;
            p.next();
        }
        if (cy != my) {
            int count2 = intersectLine(cx, cy, mx, my, x, y, rx2, ry2);
            if (count2 == 255) {
                return 255;
            }
            cross += count2;
        }
        return cross;
    }

    public static int intersectShape(Shape s, double x, double y, double w, double h) {
        if (!s.getBounds2D().intersects(x, y, w, h)) {
            return 0;
        }
        return intersectPath(s.getPathIterator((AffineTransform) null), x, y, w, h);
    }

    public static boolean isInsideNonZero(int cross) {
        return cross != 0;
    }

    public static boolean isInsideEvenOdd(int cross) {
        return (cross & 1) != 0;
    }
}
