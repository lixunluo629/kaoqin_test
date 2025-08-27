package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.math.BigInteger;
import java.util.logging.Logger;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Edge.class */
class Edge {
    double deltaX;
    IClipper.PolyType polyTyp;
    Side side;
    int windDelta;
    int windCnt;
    int windCnt2;
    int outIdx;
    Edge next;
    Edge prev;
    Edge nextInLML;
    Edge nextInAEL;
    Edge prevInAEL;
    Edge nextInSEL;
    Edge prevInSEL;
    protected static final int SKIP = -2;
    protected static final int UNASSIGNED = -1;
    protected static final double HORIZONTAL = -3.4E38d;
    private static final Logger LOGGER = Logger.getLogger(Edge.class.getName());
    private final Point.LongPoint delta = new Point.LongPoint();
    private final Point.LongPoint top = new Point.LongPoint();
    private final Point.LongPoint bot = new Point.LongPoint();
    private final Point.LongPoint current = new Point.LongPoint();

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/Edge$Side.class */
    enum Side {
        LEFT,
        RIGHT
    }

    static boolean doesE2InsertBeforeE1(Edge e1, Edge e2) {
        return e2.current.getX() == e1.current.getX() ? e2.top.getY() > e1.top.getY() ? e2.top.getX() < topX(e1, e2.top.getY()) : e1.top.getX() > topX(e2, e1.top.getY()) : e2.current.getX() < e1.current.getX();
    }

    static boolean slopesEqual(Edge e1, Edge e2, boolean useFullRange) {
        if (useFullRange) {
            return BigInteger.valueOf(e1.getDelta().getY()).multiply(BigInteger.valueOf(e2.getDelta().getX())).equals(BigInteger.valueOf(e1.getDelta().getX()).multiply(BigInteger.valueOf(e2.getDelta().getY())));
        }
        return e1.getDelta().getY() * e2.getDelta().getX() == e1.getDelta().getX() * e2.getDelta().getY();
    }

    static void swapPolyIndexes(Edge edge1, Edge edge2) {
        int outIdx = edge1.outIdx;
        edge1.outIdx = edge2.outIdx;
        edge2.outIdx = outIdx;
    }

    static void swapSides(Edge edge1, Edge edge2) {
        Side side = edge1.side;
        edge1.side = edge2.side;
        edge2.side = side;
    }

    static long topX(Edge edge, long currentY) {
        if (currentY == edge.getTop().getY()) {
            return edge.getTop().getX();
        }
        return edge.getBot().getX() + Math.round(edge.deltaX * (currentY - edge.getBot().getY()));
    }

    public Edge findNextLocMin() {
        Edge e = this;
        while (true) {
            if (e.bot.equals(e.prev.bot) && !e.current.equals(e.top)) {
                if (e.deltaX != HORIZONTAL && e.prev.deltaX != HORIZONTAL) {
                    break;
                }
                while (e.prev.deltaX == HORIZONTAL) {
                    e = e.prev;
                }
                Edge e2 = e;
                while (e.deltaX == HORIZONTAL) {
                    e = e.next;
                }
                if (e.top.getY() != e.prev.bot.getY()) {
                    if (e2.prev.bot.getX() < e.bot.getX()) {
                        e = e2;
                    }
                }
            } else {
                e = e.next;
            }
        }
        return e;
    }

    public Point.LongPoint getBot() {
        return this.bot;
    }

    public Point.LongPoint getCurrent() {
        return this.current;
    }

    public Point.LongPoint getDelta() {
        return this.delta;
    }

    public Edge getMaximaPair() {
        Edge result = null;
        if (this.next.top.equals(this.top) && this.next.nextInLML == null) {
            result = this.next;
        } else if (this.prev.top.equals(this.top) && this.prev.nextInLML == null) {
            result = this.prev;
        }
        if (result != null) {
            if (result.outIdx == -2) {
                return null;
            }
            if (result.nextInAEL == result.prevInAEL && !result.isHorizontal()) {
                return null;
            }
        }
        return result;
    }

    public Edge getNextInAEL(IClipper.Direction direction) {
        return direction == IClipper.Direction.LEFT_TO_RIGHT ? this.nextInAEL : this.prevInAEL;
    }

    public Point.LongPoint getTop() {
        return this.top;
    }

    public boolean isContributing(IClipper.PolyFillType clipFillType, IClipper.PolyFillType subjFillType, IClipper.ClipType clipType) {
        IClipper.PolyFillType pft;
        IClipper.PolyFillType pft2;
        LOGGER.entering(Edge.class.getName(), "isContributing");
        if (this.polyTyp == IClipper.PolyType.SUBJECT) {
            pft = subjFillType;
            pft2 = clipFillType;
        } else {
            pft = clipFillType;
            pft2 = subjFillType;
        }
        switch (pft) {
            case EVEN_ODD:
                if (this.windDelta == 0 && this.windCnt != 1) {
                    return false;
                }
                break;
            case NON_ZERO:
                if (Math.abs(this.windCnt) != 1) {
                    return false;
                }
                break;
            case POSITIVE:
                if (this.windCnt != 1) {
                    return false;
                }
                break;
            default:
                if (this.windCnt != -1) {
                    return false;
                }
                break;
        }
        switch (clipType) {
            case INTERSECTION:
                switch (pft2) {
                    case EVEN_ODD:
                    case NON_ZERO:
                        return this.windCnt2 != 0;
                    case POSITIVE:
                        return this.windCnt2 > 0;
                    default:
                        return this.windCnt2 < 0;
                }
            case UNION:
                switch (pft2) {
                    case EVEN_ODD:
                    case NON_ZERO:
                        return this.windCnt2 == 0;
                    case POSITIVE:
                        return this.windCnt2 <= 0;
                    default:
                        return this.windCnt2 >= 0;
                }
            case DIFFERENCE:
                if (this.polyTyp == IClipper.PolyType.SUBJECT) {
                    switch (pft2) {
                        case EVEN_ODD:
                        case NON_ZERO:
                            return this.windCnt2 == 0;
                        case POSITIVE:
                            return this.windCnt2 <= 0;
                        default:
                            return this.windCnt2 >= 0;
                    }
                }
                switch (pft2) {
                    case EVEN_ODD:
                    case NON_ZERO:
                        return this.windCnt2 != 0;
                    case POSITIVE:
                        return this.windCnt2 > 0;
                    default:
                        return this.windCnt2 < 0;
                }
            case XOR:
                if (this.windDelta != 0) {
                    return true;
                }
                switch (pft2) {
                    case EVEN_ODD:
                    case NON_ZERO:
                        return this.windCnt2 == 0;
                    case POSITIVE:
                        return this.windCnt2 <= 0;
                    default:
                        return this.windCnt2 >= 0;
                }
            default:
                return true;
        }
    }

    public boolean isEvenOddAltFillType(IClipper.PolyFillType clipFillType, IClipper.PolyFillType subjFillType) {
        return this.polyTyp == IClipper.PolyType.SUBJECT ? clipFillType == IClipper.PolyFillType.EVEN_ODD : subjFillType == IClipper.PolyFillType.EVEN_ODD;
    }

    public boolean isEvenOddFillType(IClipper.PolyFillType clipFillType, IClipper.PolyFillType subjFillType) {
        return this.polyTyp == IClipper.PolyType.SUBJECT ? subjFillType == IClipper.PolyFillType.EVEN_ODD : clipFillType == IClipper.PolyFillType.EVEN_ODD;
    }

    public boolean isHorizontal() {
        return this.delta.getY() == 0;
    }

    public boolean isIntermediate(double y) {
        return ((double) this.top.getY()) == y && this.nextInLML != null;
    }

    public boolean isMaxima(double Y) {
        return ((double) this.top.getY()) == Y && this.nextInLML == null;
    }

    public void reverseHorizontal() {
        long temp = this.top.getX();
        this.top.setX(Long.valueOf(this.bot.getX()));
        this.bot.setX(Long.valueOf(temp));
        long temp2 = this.top.getZ();
        this.top.setZ(Long.valueOf(this.bot.getZ()));
        this.bot.setZ(Long.valueOf(temp2));
    }

    public void setBot(Point.LongPoint bot) {
        this.bot.set(bot);
    }

    public void setCurrent(Point.LongPoint current) {
        this.current.set(current);
    }

    public void setTop(Point.LongPoint top) {
        this.top.set(top);
    }

    public String toString() {
        return "TEdge [Bot=" + this.bot + ", Curr=" + this.current + ", Top=" + this.top + ", Delta=" + this.delta + ", Dx=" + this.deltaX + ", PolyTyp=" + this.polyTyp + ", Side=" + this.side + ", WindDelta=" + this.windDelta + ", WindCnt=" + this.windCnt + ", WindCnt2=" + this.windCnt2 + ", OutIdx=" + this.outIdx + ", Next=" + this.next + ", Prev=" + this.prev + ", NextInLML=" + this.nextInLML + ", NextInAEL=" + this.nextInAEL + ", PrevInAEL=" + this.prevInAEL + ", NextInSEL=" + this.nextInSEL + ", PrevInSEL=" + this.prevInSEL + "]";
    }

    public void updateDeltaX() {
        this.delta.setX(Long.valueOf(this.top.getX() - this.bot.getX()));
        this.delta.setY(Long.valueOf(this.top.getY() - this.bot.getY()));
        if (this.delta.getY() == 0) {
            this.deltaX = HORIZONTAL;
        } else {
            this.deltaX = this.delta.getX() / this.delta.getY();
        }
    }
}
