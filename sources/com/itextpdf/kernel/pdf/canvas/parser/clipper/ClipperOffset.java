package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/ClipperOffset.class */
public class ClipperOffset {
    private Paths destPolys;
    private Path srcPoly;
    private Path destPoly;
    private final List<Point.DoublePoint> normals;
    private double delta;
    private double inA;
    private double sin;
    private double cos;
    private double miterLim;
    private double stepsPerRad;
    private Point.LongPoint lowest;
    private final PolyNode polyNodes;
    private final double arcTolerance;
    private final double miterLimit;
    private static final double TWO_PI = 6.283185307179586d;
    private static final double DEFAULT_ARC_TOLERANCE = 0.25d;
    private static final double TOLERANCE = 1.0E-20d;

    private static boolean nearZero(double val) {
        return val > -1.0E-20d && val < TOLERANCE;
    }

    public ClipperOffset() {
        this(2.0d, DEFAULT_ARC_TOLERANCE);
    }

    public ClipperOffset(double miterLimit) {
        this(miterLimit, DEFAULT_ARC_TOLERANCE);
    }

    public ClipperOffset(double miterLimit, double arcTolerance) {
        this.miterLimit = miterLimit;
        this.arcTolerance = arcTolerance;
        this.lowest = new Point.LongPoint();
        this.lowest.setX(-1L);
        this.polyNodes = new PolyNode();
        this.normals = new ArrayList();
    }

    public void addPath(Path path, IClipper.JoinType joinType, IClipper.EndType endType) {
        int highI = path.size() - 1;
        if (highI < 0) {
            return;
        }
        PolyNode newNode = new PolyNode();
        newNode.setJoinType(joinType);
        newNode.setEndType(endType);
        if (endType == IClipper.EndType.CLOSED_LINE || endType == IClipper.EndType.CLOSED_POLYGON) {
            while (highI > 0 && path.get(0).equals(path.get(highI))) {
                highI--;
            }
        }
        newNode.getPolygon().add(path.get(0));
        int j = 0;
        int k = 0;
        for (int i = 1; i <= highI; i++) {
            if (!newNode.getPolygon().get(j).equals(path.get(i))) {
                j++;
                newNode.getPolygon().add(path.get(i));
                if (path.get(i).getY() > newNode.getPolygon().get(k).getY() || (path.get(i).getY() == newNode.getPolygon().get(k).getY() && path.get(i).getX() < newNode.getPolygon().get(k).getX())) {
                    k = j;
                }
            }
        }
        if (endType == IClipper.EndType.CLOSED_POLYGON && j < 2) {
            return;
        }
        this.polyNodes.addChild(newNode);
        if (endType != IClipper.EndType.CLOSED_POLYGON) {
            return;
        }
        if (this.lowest.getX() < 0) {
            this.lowest = new Point.LongPoint(this.polyNodes.getChildCount() - 1, k);
            return;
        }
        Point.LongPoint ip = this.polyNodes.getChilds().get((int) this.lowest.getX()).getPolygon().get((int) this.lowest.getY());
        if (newNode.getPolygon().get(k).getY() > ip.getY() || (newNode.getPolygon().get(k).getY() == ip.getY() && newNode.getPolygon().get(k).getX() < ip.getX())) {
            this.lowest = new Point.LongPoint(this.polyNodes.getChildCount() - 1, k);
        }
    }

    public void addPaths(Paths paths, IClipper.JoinType joinType, IClipper.EndType endType) {
        Iterator<Path> it = paths.iterator();
        while (it.hasNext()) {
            Path p = it.next();
            addPath(p, joinType, endType);
        }
    }

    public void clear() {
        this.polyNodes.getChilds().clear();
        this.lowest.setX(-1L);
    }

    private void doMiter(int j, int k, double r) {
        double q = this.delta / r;
        this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + ((this.normals.get(k).getX() + this.normals.get(j).getX()) * q)), Math.round(this.srcPoly.get(j).getY() + ((this.normals.get(k).getY() + this.normals.get(j).getY()) * q))));
    }

    private void doOffset(double delta) {
        double y;
        this.destPolys = new Paths();
        this.delta = delta;
        if (nearZero(delta)) {
            for (int i = 0; i < this.polyNodes.getChildCount(); i++) {
                PolyNode node = this.polyNodes.getChilds().get(i);
                if (node.getEndType() == IClipper.EndType.CLOSED_POLYGON) {
                    this.destPolys.add(node.getPolygon());
                }
            }
            return;
        }
        if (this.miterLimit > 2.0d) {
            this.miterLim = 2.0d / (this.miterLimit * this.miterLimit);
        } else {
            this.miterLim = 0.5d;
        }
        if (this.arcTolerance <= 0.0d) {
            y = 0.25d;
        } else if (this.arcTolerance > Math.abs(delta) * DEFAULT_ARC_TOLERANCE) {
            y = Math.abs(delta) * DEFAULT_ARC_TOLERANCE;
        } else {
            y = this.arcTolerance;
        }
        double steps = 3.141592653589793d / Math.acos(1.0d - (y / Math.abs(delta)));
        this.sin = Math.sin(TWO_PI / steps);
        this.cos = Math.cos(TWO_PI / steps);
        this.stepsPerRad = steps / TWO_PI;
        if (delta < 0.0d) {
            this.sin = -this.sin;
        }
        for (int i2 = 0; i2 < this.polyNodes.getChildCount(); i2++) {
            PolyNode node2 = this.polyNodes.getChilds().get(i2);
            this.srcPoly = node2.getPolygon();
            int len = this.srcPoly.size();
            if (len != 0 && (delta > 0.0d || (len >= 3 && node2.getEndType() == IClipper.EndType.CLOSED_POLYGON))) {
                this.destPoly = new Path();
                if (len == 1) {
                    if (node2.getJoinType() == IClipper.JoinType.ROUND) {
                        double X = 1.0d;
                        double Y = 0.0d;
                        for (int j = 1; j <= steps; j++) {
                            this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + (X * delta)), Math.round(this.srcPoly.get(0).getY() + (Y * delta))));
                            double X2 = X;
                            X = (X * this.cos) - (this.sin * Y);
                            Y = (X2 * this.sin) + (Y * this.cos);
                        }
                    } else {
                        double X3 = -1.0d;
                        double Y2 = -1.0d;
                        for (int j2 = 0; j2 < 4; j2++) {
                            this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + (X3 * delta)), Math.round(this.srcPoly.get(0).getY() + (Y2 * delta))));
                            if (X3 < 0.0d) {
                                X3 = 1.0d;
                            } else if (Y2 < 0.0d) {
                                Y2 = 1.0d;
                            } else {
                                X3 = -1.0d;
                            }
                        }
                    }
                    this.destPolys.add(this.destPoly);
                } else {
                    this.normals.clear();
                    for (int j3 = 0; j3 < len - 1; j3++) {
                        this.normals.add(Point.getUnitNormal(this.srcPoly.get(j3), this.srcPoly.get(j3 + 1)));
                    }
                    if (node2.getEndType() == IClipper.EndType.CLOSED_LINE || node2.getEndType() == IClipper.EndType.CLOSED_POLYGON) {
                        this.normals.add(Point.getUnitNormal(this.srcPoly.get(len - 1), this.srcPoly.get(0)));
                    } else {
                        this.normals.add(new Point.DoublePoint(this.normals.get(len - 2)));
                    }
                    if (node2.getEndType() == IClipper.EndType.CLOSED_POLYGON) {
                        int[] k = {len - 1};
                        for (int j4 = 0; j4 < len; j4++) {
                            offsetPoint(j4, k, node2.getJoinType());
                        }
                        this.destPolys.add(this.destPoly);
                    } else if (node2.getEndType() == IClipper.EndType.CLOSED_LINE) {
                        int[] k2 = {len - 1};
                        for (int j5 = 0; j5 < len; j5++) {
                            offsetPoint(j5, k2, node2.getJoinType());
                        }
                        this.destPolys.add(this.destPoly);
                        this.destPoly = new Path();
                        Point.DoublePoint n = this.normals.get(len - 1);
                        for (int j6 = len - 1; j6 > 0; j6--) {
                            this.normals.set(j6, new Point.DoublePoint(-this.normals.get(j6 - 1).getX(), -this.normals.get(j6 - 1).getY()));
                        }
                        this.normals.set(0, new Point.DoublePoint(-n.getX(), -n.getY(), 0.0d));
                        k2[0] = 0;
                        for (int j7 = len - 1; j7 >= 0; j7--) {
                            offsetPoint(j7, k2, node2.getJoinType());
                        }
                        this.destPolys.add(this.destPoly);
                    } else {
                        int[] k3 = new int[1];
                        for (int j8 = 1; j8 < len - 1; j8++) {
                            offsetPoint(j8, k3, node2.getJoinType());
                        }
                        if (node2.getEndType() == IClipper.EndType.OPEN_BUTT) {
                            int j9 = len - 1;
                            Point.LongPoint pt1 = new Point.LongPoint(Math.round(this.srcPoly.get(j9).getX() + (this.normals.get(j9).getX() * delta)), Math.round(this.srcPoly.get(j9).getY() + (this.normals.get(j9).getY() * delta)), 0L);
                            this.destPoly.add(pt1);
                            Point.LongPoint pt12 = new Point.LongPoint(Math.round(this.srcPoly.get(j9).getX() - (this.normals.get(j9).getX() * delta)), Math.round(this.srcPoly.get(j9).getY() - (this.normals.get(j9).getY() * delta)), 0L);
                            this.destPoly.add(pt12);
                        } else {
                            int j10 = len - 1;
                            k3[0] = len - 2;
                            this.inA = 0.0d;
                            this.normals.set(j10, new Point.DoublePoint(-this.normals.get(j10).getX(), -this.normals.get(j10).getY()));
                            if (node2.getEndType() == IClipper.EndType.OPEN_SQUARE) {
                                doSquare(j10, k3[0], true);
                            } else {
                                doRound(j10, k3[0]);
                            }
                        }
                        for (int j11 = len - 1; j11 > 0; j11--) {
                            this.normals.set(j11, new Point.DoublePoint(-this.normals.get(j11 - 1).getX(), -this.normals.get(j11 - 1).getY()));
                        }
                        this.normals.set(0, new Point.DoublePoint(-this.normals.get(1).getX(), -this.normals.get(1).getY()));
                        k3[0] = len - 1;
                        for (int j12 = k3[0] - 1; j12 > 0; j12--) {
                            offsetPoint(j12, k3, node2.getJoinType());
                        }
                        if (node2.getEndType() == IClipper.EndType.OPEN_BUTT) {
                            Point.LongPoint pt13 = new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() - (this.normals.get(0).getX() * delta)), Math.round(this.srcPoly.get(0).getY() - (this.normals.get(0).getY() * delta)));
                            this.destPoly.add(pt13);
                            Point.LongPoint pt14 = new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + (this.normals.get(0).getX() * delta)), Math.round(this.srcPoly.get(0).getY() + (this.normals.get(0).getY() * delta)));
                            this.destPoly.add(pt14);
                        } else {
                            k3[0] = 1;
                            this.inA = 0.0d;
                            if (node2.getEndType() == IClipper.EndType.OPEN_SQUARE) {
                                doSquare(0, 1, true);
                            } else {
                                doRound(0, 1);
                            }
                        }
                        this.destPolys.add(this.destPoly);
                    }
                }
            }
        }
    }

    private void doRound(int j, int k) {
        double a = Math.atan2(this.inA, (this.normals.get(k).getX() * this.normals.get(j).getX()) + (this.normals.get(k).getY() * this.normals.get(j).getY()));
        int steps = Math.max((int) Math.round(this.stepsPerRad * Math.abs(a)), 1);
        double X = this.normals.get(k).getX();
        double Y = this.normals.get(k).getY();
        for (int i = 0; i < steps; i++) {
            this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + (X * this.delta)), Math.round(this.srcPoly.get(j).getY() + (Y * this.delta))));
            double X2 = X;
            X = (X * this.cos) - (this.sin * Y);
            Y = (X2 * this.sin) + (Y * this.cos);
        }
        this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + (this.normals.get(j).getX() * this.delta)), Math.round(this.srcPoly.get(j).getY() + (this.normals.get(j).getY() * this.delta))));
    }

    private void doSquare(int j, int k, boolean addExtra) {
        double nkx = this.normals.get(k).getX();
        double nky = this.normals.get(k).getY();
        double njx = this.normals.get(j).getX();
        double njy = this.normals.get(j).getY();
        double sjx = this.srcPoly.get(j).getX();
        double sjy = this.srcPoly.get(j).getY();
        double dx = Math.tan(Math.atan2(this.inA, (nkx * njx) + (nky * njy)) / 4.0d);
        this.destPoly.add(new Point.LongPoint(Math.round(sjx + (this.delta * (nkx - (addExtra ? nky * dx : 0.0d)))), Math.round(sjy + (this.delta * (nky + (addExtra ? nkx * dx : 0.0d)))), 0L));
        this.destPoly.add(new Point.LongPoint(Math.round(sjx + (this.delta * (njx + (addExtra ? njy * dx : 0.0d)))), Math.round(sjy + (this.delta * (njy - (addExtra ? njx * dx : 0.0d)))), 0L));
    }

    public void execute(Paths solution, double delta) {
        solution.clear();
        fixOrientations();
        doOffset(delta);
        DefaultClipper clpr = new DefaultClipper(1);
        clpr.addPaths(this.destPolys, IClipper.PolyType.SUBJECT, true);
        if (delta > 0.0d) {
            clpr.execute(IClipper.ClipType.UNION, solution, IClipper.PolyFillType.POSITIVE, IClipper.PolyFillType.POSITIVE);
            return;
        }
        LongRect r = this.destPolys.getBounds();
        Path outer = new Path(4);
        outer.add(new Point.LongPoint(r.left - 10, r.bottom + 10, 0L));
        outer.add(new Point.LongPoint(r.right + 10, r.bottom + 10, 0L));
        outer.add(new Point.LongPoint(r.right + 10, r.top - 10, 0L));
        outer.add(new Point.LongPoint(r.left - 10, r.top - 10, 0L));
        clpr.addPath(outer, IClipper.PolyType.SUBJECT, true);
        clpr.execute(IClipper.ClipType.UNION, solution, IClipper.PolyFillType.NEGATIVE, IClipper.PolyFillType.NEGATIVE);
        if (solution.size() > 0) {
            solution.remove(0);
        }
    }

    public void execute(PolyTree solution, double delta) {
        solution.Clear();
        fixOrientations();
        doOffset(delta);
        DefaultClipper clpr = new DefaultClipper(1);
        clpr.addPaths(this.destPolys, IClipper.PolyType.SUBJECT, true);
        if (delta > 0.0d) {
            clpr.execute(IClipper.ClipType.UNION, solution, IClipper.PolyFillType.POSITIVE, IClipper.PolyFillType.POSITIVE);
            return;
        }
        LongRect r = this.destPolys.getBounds();
        Path outer = new Path(4);
        outer.add(new Point.LongPoint(r.left - 10, r.bottom + 10, 0L));
        outer.add(new Point.LongPoint(r.right + 10, r.bottom + 10, 0L));
        outer.add(new Point.LongPoint(r.right + 10, r.top - 10, 0L));
        outer.add(new Point.LongPoint(r.left - 10, r.top - 10, 0L));
        clpr.addPath(outer, IClipper.PolyType.SUBJECT, true);
        clpr.execute(IClipper.ClipType.UNION, solution, IClipper.PolyFillType.NEGATIVE, IClipper.PolyFillType.NEGATIVE);
        if (solution.getChildCount() == 1 && solution.getChilds().get(0).getChildCount() > 0) {
            PolyNode outerNode = solution.getChilds().get(0);
            solution.getChilds().set(0, outerNode.getChilds().get(0));
            solution.getChilds().get(0).setParent(solution);
            for (int i = 1; i < outerNode.getChildCount(); i++) {
                solution.addChild(outerNode.getChilds().get(i));
            }
            return;
        }
        solution.Clear();
    }

    private void fixOrientations() {
        if (this.lowest.getX() >= 0 && !this.polyNodes.childs.get((int) this.lowest.getX()).getPolygon().orientation()) {
            for (int i = 0; i < this.polyNodes.getChildCount(); i++) {
                PolyNode node = this.polyNodes.childs.get(i);
                if (node.getEndType() == IClipper.EndType.CLOSED_POLYGON || (node.getEndType() == IClipper.EndType.CLOSED_LINE && node.getPolygon().orientation())) {
                    Collections.reverse(node.getPolygon());
                }
            }
            return;
        }
        for (int i2 = 0; i2 < this.polyNodes.getChildCount(); i2++) {
            PolyNode node2 = this.polyNodes.childs.get(i2);
            if (node2.getEndType() == IClipper.EndType.CLOSED_LINE && !node2.getPolygon().orientation()) {
                Collections.reverse(node2.getPolygon());
            }
        }
    }

    private void offsetPoint(int j, int[] kV, IClipper.JoinType jointype) {
        int k = kV[0];
        double nkx = this.normals.get(k).getX();
        double nky = this.normals.get(k).getY();
        double njy = this.normals.get(j).getY();
        double njx = this.normals.get(j).getX();
        long sjx = this.srcPoly.get(j).getX();
        long sjy = this.srcPoly.get(j).getY();
        this.inA = (nkx * njy) - (njx * nky);
        if (Math.abs(this.inA * this.delta) < 1.0d) {
            double cosA = (nkx * njx) + (njy * nky);
            if (cosA > 0.0d) {
                this.destPoly.add(new Point.LongPoint(Math.round(sjx + (nkx * this.delta)), Math.round(sjy + (nky * this.delta)), 0L));
                return;
            }
        } else if (this.inA > 1.0d) {
            this.inA = 1.0d;
        } else if (this.inA < -1.0d) {
            this.inA = -1.0d;
        }
        if (this.inA * this.delta < 0.0d) {
            this.destPoly.add(new Point.LongPoint(Math.round(sjx + (nkx * this.delta)), Math.round(sjy + (nky * this.delta))));
            this.destPoly.add(this.srcPoly.get(j));
            this.destPoly.add(new Point.LongPoint(Math.round(sjx + (njx * this.delta)), Math.round(sjy + (njy * this.delta))));
        } else {
            switch (jointype) {
                case MITER:
                    double r = 1.0d + (njx * nkx) + (njy * nky);
                    if (r >= this.miterLim) {
                        doMiter(j, k, r);
                        break;
                    } else {
                        doSquare(j, k, false);
                        break;
                    }
                case BEVEL:
                    doSquare(j, k, false);
                    break;
                case ROUND:
                    doRound(j, k);
                    break;
            }
        }
        kV[0] = j;
    }
}
