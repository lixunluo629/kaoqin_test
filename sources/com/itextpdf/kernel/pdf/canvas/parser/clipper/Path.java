package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Path.class */
public class Path extends ArrayList<Point.LongPoint> {
    private static final long serialVersionUID = -7120161578077546673L;

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Path$Join.class */
    static class Join {
        OutPt outPt1;
        OutPt outPt2;
        private Point.LongPoint offPt;

        Join() {
        }

        public Point.LongPoint getOffPt() {
            return this.offPt;
        }

        public void setOffPt(Point.LongPoint offPt) {
            this.offPt = offPt;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Path$OutPt.class */
    static class OutPt {
        int idx;
        protected Point.LongPoint pt;
        OutPt next;
        OutPt prev;

        OutPt() {
        }

        public static OutRec getLowerMostRec(OutRec outRec1, OutRec outRec2) {
            if (outRec1.bottomPt == null) {
                outRec1.bottomPt = outRec1.pts.getBottomPt();
            }
            if (outRec2.bottomPt == null) {
                outRec2.bottomPt = outRec2.pts.getBottomPt();
            }
            OutPt bPt1 = outRec1.bottomPt;
            OutPt bPt2 = outRec2.bottomPt;
            if (bPt1.getPt().getY() > bPt2.getPt().getY()) {
                return outRec1;
            }
            if (bPt1.getPt().getY() < bPt2.getPt().getY()) {
                return outRec2;
            }
            if (bPt1.getPt().getX() < bPt2.getPt().getX()) {
                return outRec1;
            }
            if (bPt1.getPt().getX() > bPt2.getPt().getX()) {
                return outRec2;
            }
            if (bPt1.next == bPt1) {
                return outRec2;
            }
            if (bPt2.next == bPt2) {
                return outRec1;
            }
            if (isFirstBottomPt(bPt1, bPt2)) {
                return outRec1;
            }
            return outRec2;
        }

        private static boolean isFirstBottomPt(OutPt btmPt1, OutPt btmPt2) {
            OutPt p;
            OutPt p2;
            OutPt p3;
            OutPt p4;
            OutPt outPt = btmPt1.prev;
            while (true) {
                p = outPt;
                if (!p.getPt().equals(btmPt1.getPt()) || p.equals(btmPt1)) {
                    break;
                }
                outPt = p.prev;
            }
            double dx1p = Math.abs(Point.LongPoint.getDeltaX(btmPt1.getPt(), p.getPt()));
            OutPt outPt2 = btmPt1.next;
            while (true) {
                p2 = outPt2;
                if (!p2.getPt().equals(btmPt1.getPt()) || p2.equals(btmPt1)) {
                    break;
                }
                outPt2 = p2.next;
            }
            double dx1n = Math.abs(Point.LongPoint.getDeltaX(btmPt1.getPt(), p2.getPt()));
            OutPt outPt3 = btmPt2.prev;
            while (true) {
                p3 = outPt3;
                if (!p3.getPt().equals(btmPt2.getPt()) || p3.equals(btmPt2)) {
                    break;
                }
                outPt3 = p3.prev;
            }
            double dx2p = Math.abs(Point.LongPoint.getDeltaX(btmPt2.getPt(), p3.getPt()));
            OutPt outPt4 = btmPt2.next;
            while (true) {
                p4 = outPt4;
                if (!p4.getPt().equals(btmPt2.getPt()) || !p4.equals(btmPt2)) {
                    break;
                }
                outPt4 = p4.next;
            }
            double dx2n = Math.abs(Point.LongPoint.getDeltaX(btmPt2.getPt(), p4.getPt()));
            return (dx1p >= dx2p && dx1p >= dx2n) || (dx1n >= dx2p && dx1n >= dx2n);
        }

        public OutPt duplicate(boolean InsertAfter) {
            OutPt result = new OutPt();
            result.setPt(new Point.LongPoint(getPt()));
            result.idx = this.idx;
            if (InsertAfter) {
                result.next = this.next;
                result.prev = this;
                this.next.prev = result;
                this.next = result;
            } else {
                result.prev = this.prev;
                result.next = this;
                this.prev.next = result;
                this.prev = result;
            }
            return result;
        }

        OutPt getBottomPt() {
            OutPt dups = null;
            OutPt p = this.next;
            OutPt pp = this;
            while (p != pp) {
                if (p.getPt().getY() > pp.getPt().getY()) {
                    pp = p;
                    dups = null;
                } else if (p.getPt().getY() == pp.getPt().getY() && p.getPt().getX() <= pp.getPt().getX()) {
                    if (p.getPt().getX() < pp.getPt().getX()) {
                        dups = null;
                        pp = p;
                    } else if (p.next != pp && p.prev != pp) {
                        dups = p;
                    }
                }
                p = p.next;
            }
            if (dups != null) {
                while (dups != p) {
                    if (!isFirstBottomPt(p, dups)) {
                        pp = dups;
                    }
                    OutPt outPt = dups.next;
                    while (true) {
                        dups = outPt;
                        if (!dups.getPt().equals(pp.getPt())) {
                            outPt = dups.next;
                        }
                    }
                }
            }
            return pp;
        }

        public int getPointCount() {
            int result = 0;
            OutPt p = this;
            do {
                result++;
                p = p.next;
                if (p == this) {
                    break;
                }
            } while (p != null);
            return result;
        }

        public Point.LongPoint getPt() {
            return this.pt;
        }

        public void reversePolyPtLinks() {
            OutPt pp1 = this;
            do {
                OutPt pp2 = pp1.next;
                pp1.next = pp1.prev;
                pp1.prev = pp2;
                pp1 = pp2;
            } while (pp1 != this);
        }

        public void setPt(Point.LongPoint pt) {
            this.pt = pt;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Path$Maxima.class */
    protected static class Maxima {
        protected long X;
        protected Maxima Next;
        protected Maxima Prev;

        protected Maxima() {
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Path$OutRec.class */
    static class OutRec {
        int Idx;
        boolean isHole;
        boolean isOpen;
        OutRec firstLeft;
        protected OutPt pts;
        OutPt bottomPt;
        PolyNode polyNode;

        OutRec() {
        }

        public double area() {
            OutPt op = this.pts;
            if (op == null) {
                return 0.0d;
            }
            double a = 0.0d;
            do {
                a += (op.prev.getPt().getX() + op.getPt().getX()) * (op.prev.getPt().getY() - op.getPt().getY());
                op = op.next;
            } while (op != this.pts);
            return a * 0.5d;
        }

        public void fixHoleLinkage() {
            OutRec orfl;
            if (this.firstLeft != null) {
                if (this.isHole != this.firstLeft.isHole && this.firstLeft.pts != null) {
                    return;
                }
                OutRec outRec = this.firstLeft;
                while (true) {
                    orfl = outRec;
                    if (orfl == null || !(orfl.isHole == this.isHole || orfl.pts == null)) {
                        break;
                    } else {
                        outRec = orfl.firstLeft;
                    }
                }
                this.firstLeft = orfl;
            }
        }

        public OutPt getPoints() {
            return this.pts;
        }

        public void setPoints(OutPt pts) {
            this.pts = pts;
        }
    }

    private static OutPt excludeOp(OutPt op) {
        OutPt result = op.prev;
        result.next = op.next;
        op.next.prev = result;
        result.idx = 0;
        return result;
    }

    public Path() {
    }

    public Path(Point.LongPoint[] points) {
        this();
        for (Point.LongPoint point : points) {
            add(point);
        }
    }

    public Path(int cnt) {
        super(cnt);
    }

    public Path(Collection<? extends Point.LongPoint> c) {
        super(c);
    }

    public double area() {
        int cnt = size();
        if (cnt < 3) {
            return 0.0d;
        }
        double a = 0.0d;
        int j = cnt - 1;
        for (int i = 0; i < cnt; i++) {
            a += (get(j).getX() + get(i).getX()) * (get(j).getY() - get(i).getY());
            j = i;
        }
        return (-a) * 0.5d;
    }

    public Path cleanPolygon() {
        return cleanPolygon(1.415d);
    }

    public Path cleanPolygon(double distance) {
        int cnt = size();
        if (cnt == 0) {
            return new Path();
        }
        OutPt[] outPts = new OutPt[cnt];
        for (int i = 0; i < cnt; i++) {
            outPts[i] = new OutPt();
        }
        for (int i2 = 0; i2 < cnt; i2++) {
            outPts[i2].pt = get(i2);
            outPts[i2].next = outPts[(i2 + 1) % cnt];
            outPts[i2].next.prev = outPts[i2];
            outPts[i2].idx = 0;
        }
        double distSqrd = distance * distance;
        OutPt op = outPts[0];
        while (op.idx == 0 && op.next != op.prev) {
            if (Point.arePointsClose(op.pt, op.prev.pt, distSqrd)) {
                op = excludeOp(op);
                cnt--;
            } else if (Point.arePointsClose(op.prev.pt, op.next.pt, distSqrd)) {
                excludeOp(op.next);
                op = excludeOp(op);
                cnt -= 2;
            } else if (Point.slopesNearCollinear(op.prev.pt, op.pt, op.next.pt, distSqrd)) {
                op = excludeOp(op);
                cnt--;
            } else {
                op.idx = 1;
                op = op.next;
            }
        }
        if (cnt < 3) {
            cnt = 0;
        }
        Path result = new Path(cnt);
        for (int i3 = 0; i3 < cnt; i3++) {
            result.add(op.pt);
            op = op.next;
        }
        return result;
    }

    public int isPointInPolygon(Point.LongPoint pt) {
        int result = 0;
        int cnt = size();
        if (cnt < 3) {
            return 0;
        }
        Point.LongPoint ip = get(0);
        int i = 1;
        while (i <= cnt) {
            Point.LongPoint ipNext = i == cnt ? get(0) : get(i);
            if (ipNext.getY() == pt.getY()) {
                if (ipNext.getX() == pt.getX()) {
                    return -1;
                }
                if (ip.getY() == pt.getY()) {
                    if ((ipNext.getX() > pt.getX()) == (ip.getX() < pt.getX())) {
                        return -1;
                    }
                }
            }
            if ((ip.getY() < pt.getY()) != (ipNext.getY() < pt.getY())) {
                if (ip.getX() >= pt.getX()) {
                    if (ipNext.getX() > pt.getX()) {
                        result = 1 - result;
                    } else {
                        double d = ((ip.getX() - pt.getX()) * (ipNext.getY() - pt.getY())) - ((ipNext.getX() - pt.getX()) * (ip.getY() - pt.getY()));
                        if (d == 0.0d) {
                            return -1;
                        }
                        if ((d > 0.0d) == (ipNext.getY() > ip.getY())) {
                            result = 1 - result;
                        }
                    }
                } else if (ipNext.getX() > pt.getX()) {
                    double d2 = ((ip.getX() - pt.getX()) * (ipNext.getY() - pt.getY())) - ((ipNext.getX() - pt.getX()) * (ip.getY() - pt.getY()));
                    if (d2 == 0.0d) {
                        return -1;
                    }
                    if ((d2 > 0.0d) == (ipNext.getY() > ip.getY())) {
                        result = 1 - result;
                    }
                } else {
                    continue;
                }
            }
            ip = ipNext;
            i++;
        }
        return result;
    }

    public boolean orientation() {
        return area() >= 0.0d;
    }

    public void reverse() {
        Collections.reverse(this);
    }

    public Path TranslatePath(Point.LongPoint delta) {
        Path outPath = new Path(size());
        for (int i = 0; i < size(); i++) {
            outPath.add(new Point.LongPoint(get(i).getX() + delta.getX(), get(i).getY() + delta.getY()));
        }
        return outPath;
    }
}
