package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.Edge;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Path;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/ClipperBase.class */
public abstract class ClipperBase implements IClipper {
    private static final long LOW_RANGE = 1073741823;
    private static final long HI_RANGE = 4611686018427387903L;
    protected boolean useFullRange;
    protected final boolean preserveCollinear;
    private static final Logger LOGGER = Logger.getLogger(IClipper.class.getName());
    protected LocalMinima minimaList = null;
    protected LocalMinima currentLM = null;
    protected boolean hasOpenPaths = false;
    private final List<List<Edge>> edges = new ArrayList();

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/ClipperBase$LocalMinima.class */
    protected class LocalMinima {
        long y;
        Edge leftBound;
        Edge rightBound;
        LocalMinima next;

        protected LocalMinima() {
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/ClipperBase$Scanbeam.class */
    protected class Scanbeam {
        long y;
        Scanbeam next;

        protected Scanbeam() {
        }
    }

    private static void initEdge(Edge e, Edge eNext, Edge ePrev, Point.LongPoint pt) {
        e.next = eNext;
        e.prev = ePrev;
        e.setCurrent(new Point.LongPoint(pt));
        e.outIdx = -1;
    }

    private static void initEdge2(Edge e, IClipper.PolyType polyType) {
        if (e.getCurrent().getY() >= e.next.getCurrent().getY()) {
            e.setBot(new Point.LongPoint(e.getCurrent()));
            e.setTop(new Point.LongPoint(e.next.getCurrent()));
        } else {
            e.setTop(new Point.LongPoint(e.getCurrent()));
            e.setBot(new Point.LongPoint(e.next.getCurrent()));
        }
        e.updateDeltaX();
        e.polyTyp = polyType;
    }

    private static boolean rangeTest(Point.LongPoint Pt, boolean useFullRange) {
        if (useFullRange) {
            if (Pt.getX() > HI_RANGE || Pt.getY() > HI_RANGE || (-Pt.getX()) > HI_RANGE || (-Pt.getY()) > HI_RANGE) {
                throw new IllegalStateException("Coordinate outside allowed range");
            }
        } else if (Pt.getX() > LOW_RANGE || Pt.getY() > LOW_RANGE || (-Pt.getX()) > LOW_RANGE || (-Pt.getY()) > LOW_RANGE) {
            return rangeTest(Pt, true);
        }
        return useFullRange;
    }

    private static Edge removeEdge(Edge e) {
        e.prev.next = e.next;
        e.next.prev = e.prev;
        Edge result = e.next;
        e.prev = null;
        return result;
    }

    protected ClipperBase(boolean preserveCollinear) {
        this.preserveCollinear = preserveCollinear;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper
    public boolean addPath(Path pg, IClipper.PolyType polyType, boolean Closed) {
        boolean leftBoundIsForward;
        if (!Closed && polyType == IClipper.PolyType.CLIP) {
            throw new IllegalStateException("AddPath: Open paths must be subject.");
        }
        int highI = pg.size() - 1;
        if (Closed) {
            while (highI > 0 && pg.get(highI).equals(pg.get(0))) {
                highI--;
            }
        }
        while (highI > 0 && pg.get(highI).equals(pg.get(highI - 1))) {
            highI--;
        }
        if (Closed && highI < 2) {
            return false;
        }
        if (!Closed && highI < 1) {
            return false;
        }
        List<Edge> edges = new ArrayList<>(highI + 1);
        for (int i = 0; i <= highI; i++) {
            edges.add(new Edge());
        }
        boolean IsFlat = true;
        edges.get(1).setCurrent(new Point.LongPoint(pg.get(1)));
        this.useFullRange = rangeTest(pg.get(0), this.useFullRange);
        this.useFullRange = rangeTest(pg.get(highI), this.useFullRange);
        initEdge(edges.get(0), edges.get(1), edges.get(highI), pg.get(0));
        initEdge(edges.get(highI), edges.get(0), edges.get(highI - 1), pg.get(highI));
        for (int i2 = highI - 1; i2 >= 1; i2--) {
            this.useFullRange = rangeTest(pg.get(i2), this.useFullRange);
            initEdge(edges.get(i2), edges.get(i2 + 1), edges.get(i2 - 1), pg.get(i2));
        }
        Edge eStart = edges.get(0);
        Edge e = eStart;
        Edge eLoopStop = eStart;
        while (true) {
            if (e.getCurrent().equals(e.next.getCurrent()) && (Closed || !e.next.equals(eStart))) {
                if (e == e.next) {
                    break;
                }
                if (e == eStart) {
                    eStart = e.next;
                }
                e = removeEdge(e);
                eLoopStop = e;
            } else {
                if (e.prev == e.next) {
                    break;
                }
                if (Closed && Point.slopesEqual(e.prev.getCurrent(), e.getCurrent(), e.next.getCurrent(), this.useFullRange) && (!isPreserveCollinear() || !Point.isPt2BetweenPt1AndPt3(e.prev.getCurrent(), e.getCurrent(), e.next.getCurrent()))) {
                    if (e == eStart) {
                        eStart = e.next;
                    }
                    e = removeEdge(e).prev;
                    eLoopStop = e;
                } else {
                    e = e.next;
                    if (e == eLoopStop || (!Closed && e.next == eStart)) {
                        break;
                    }
                }
            }
        }
        if (!Closed && e == e.next) {
            return false;
        }
        if (Closed && e.prev == e.next) {
            return false;
        }
        if (!Closed) {
            this.hasOpenPaths = true;
            eStart.prev.outIdx = -2;
        }
        Edge e2 = eStart;
        do {
            initEdge2(e2, polyType);
            e2 = e2.next;
            if (IsFlat && e2.getCurrent().getY() != eStart.getCurrent().getY()) {
                IsFlat = false;
            }
        } while (e2 != eStart);
        if (IsFlat) {
            if (Closed) {
                return false;
            }
            e2.prev.outIdx = -2;
            LocalMinima locMin = new LocalMinima();
            locMin.next = null;
            locMin.y = e2.getBot().getY();
            locMin.leftBound = null;
            locMin.rightBound = e2;
            locMin.rightBound.side = Edge.Side.RIGHT;
            locMin.rightBound.windDelta = 0;
            while (true) {
                if (e2.getBot().getX() != e2.prev.getTop().getX()) {
                    e2.reverseHorizontal();
                }
                if (e2.next.outIdx != -2) {
                    e2.nextInLML = e2.next;
                    e2 = e2.next;
                } else {
                    insertLocalMinima(locMin);
                    this.edges.add(edges);
                    return true;
                }
            }
        } else {
            this.edges.add(edges);
            Edge EMin = null;
            if (e2.prev.getBot().equals(e2.prev.getTop())) {
                e2 = e2.next;
            }
            while (true) {
                Edge e3 = e2.findNextLocMin();
                if (e3 != EMin) {
                    if (EMin == null) {
                        EMin = e3;
                    }
                    LocalMinima locMin2 = new LocalMinima();
                    locMin2.next = null;
                    locMin2.y = e3.getBot().getY();
                    if (e3.deltaX < e3.prev.deltaX) {
                        locMin2.leftBound = e3.prev;
                        locMin2.rightBound = e3;
                        leftBoundIsForward = false;
                    } else {
                        locMin2.leftBound = e3;
                        locMin2.rightBound = e3.prev;
                        leftBoundIsForward = true;
                    }
                    locMin2.leftBound.side = Edge.Side.LEFT;
                    locMin2.rightBound.side = Edge.Side.RIGHT;
                    if (!Closed) {
                        locMin2.leftBound.windDelta = 0;
                    } else if (locMin2.leftBound.next == locMin2.rightBound) {
                        locMin2.leftBound.windDelta = -1;
                    } else {
                        locMin2.leftBound.windDelta = 1;
                    }
                    locMin2.rightBound.windDelta = -locMin2.leftBound.windDelta;
                    e2 = processBound(locMin2.leftBound, leftBoundIsForward);
                    if (e2.outIdx == -2) {
                        e2 = processBound(e2, leftBoundIsForward);
                    }
                    Edge E2 = processBound(locMin2.rightBound, !leftBoundIsForward);
                    if (E2.outIdx == -2) {
                        E2 = processBound(E2, !leftBoundIsForward);
                    }
                    if (locMin2.leftBound.outIdx == -2) {
                        locMin2.leftBound = null;
                    } else if (locMin2.rightBound.outIdx == -2) {
                        locMin2.rightBound = null;
                    }
                    insertLocalMinima(locMin2);
                    if (!leftBoundIsForward) {
                        e2 = E2;
                    }
                } else {
                    return true;
                }
            }
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper
    public boolean addPaths(Paths ppg, IClipper.PolyType polyType, boolean closed) {
        boolean result = false;
        for (int i = 0; i < ppg.size(); i++) {
            if (addPath(ppg.get(i), polyType, closed)) {
                result = true;
            }
        }
        return result;
    }

    @Override // com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper
    public void clear() {
        disposeLocalMinimaList();
        this.edges.clear();
        this.useFullRange = false;
        this.hasOpenPaths = false;
    }

    private void disposeLocalMinimaList() {
        while (this.minimaList != null) {
            LocalMinima tmpLm = this.minimaList.next;
            this.minimaList = null;
            this.minimaList = tmpLm;
        }
        this.currentLM = null;
    }

    private void insertLocalMinima(LocalMinima newLm) {
        LocalMinima tmpLm;
        if (this.minimaList == null) {
            this.minimaList = newLm;
            return;
        }
        if (newLm.y >= this.minimaList.y) {
            newLm.next = this.minimaList;
            this.minimaList = newLm;
            return;
        }
        LocalMinima localMinima = this.minimaList;
        while (true) {
            tmpLm = localMinima;
            if (tmpLm.next == null || newLm.y >= tmpLm.next.y) {
                break;
            } else {
                localMinima = tmpLm.next;
            }
        }
        newLm.next = tmpLm.next;
        tmpLm.next = newLm;
    }

    public boolean isPreserveCollinear() {
        return this.preserveCollinear;
    }

    protected void popLocalMinima() {
        LOGGER.entering(ClipperBase.class.getName(), "popLocalMinima");
        if (this.currentLM == null) {
            return;
        }
        this.currentLM = this.currentLM.next;
    }

    private Edge processBound(Edge e, boolean LeftBoundIsForward) {
        Edge result;
        Edge Horz;
        Edge Horz2;
        Edge EStart;
        Edge e2;
        Edge result2;
        Edge result3 = e;
        if (result3.outIdx == -2) {
            Edge e3 = result3;
            if (LeftBoundIsForward) {
                while (e3.getTop().getY() == e3.next.getBot().getY()) {
                    e3 = e3.next;
                }
                while (e3 != result3 && e3.deltaX == -3.4E38d) {
                    e3 = e3.prev;
                }
            } else {
                while (e3.getTop().getY() == e3.prev.getBot().getY()) {
                    e3 = e3.prev;
                }
                while (e3 != result3 && e3.deltaX == -3.4E38d) {
                    e3 = e3.next;
                }
            }
            if (e3 == result3) {
                if (LeftBoundIsForward) {
                    result2 = e3.next;
                } else {
                    result2 = e3.prev;
                }
            } else {
                if (LeftBoundIsForward) {
                    e2 = result3.next;
                } else {
                    e2 = result3.prev;
                }
                LocalMinima locMin = new LocalMinima();
                locMin.next = null;
                locMin.y = e2.getBot().getY();
                locMin.leftBound = null;
                locMin.rightBound = e2;
                e2.windDelta = 0;
                result2 = processBound(e2, LeftBoundIsForward);
                insertLocalMinima(locMin);
            }
            return result2;
        }
        if (e.deltaX == -3.4E38d) {
            if (LeftBoundIsForward) {
                EStart = e.prev;
            } else {
                EStart = e.next;
            }
            if (EStart.deltaX == -3.4E38d) {
                if (EStart.getBot().getX() != e.getBot().getX() && EStart.getTop().getX() != e.getBot().getX()) {
                    e.reverseHorizontal();
                }
            } else if (EStart.getBot().getX() != e.getBot().getX()) {
                e.reverseHorizontal();
            }
        }
        if (LeftBoundIsForward) {
            while (result3.getTop().getY() == result3.next.getBot().getY() && result3.next.outIdx != -2) {
                result3 = result3.next;
            }
            if (result3.deltaX == -3.4E38d && result3.next.outIdx != -2) {
                Edge edge = result3;
                while (true) {
                    Horz2 = edge;
                    if (Horz2.prev.deltaX != -3.4E38d) {
                        break;
                    }
                    edge = Horz2.prev;
                }
                if (Horz2.prev.getTop().getX() > result3.next.getTop().getX()) {
                    result3 = Horz2.prev;
                }
            }
            while (e != result3) {
                e.nextInLML = e.next;
                if (e.deltaX == -3.4E38d && e != e && e.getBot().getX() != e.prev.getTop().getX()) {
                    e.reverseHorizontal();
                }
                e = e.next;
            }
            if (e.deltaX == -3.4E38d && e != e && e.getBot().getX() != e.prev.getTop().getX()) {
                e.reverseHorizontal();
            }
            result = result3.next;
        } else {
            while (result3.getTop().getY() == result3.prev.getBot().getY() && result3.prev.outIdx != -2) {
                result3 = result3.prev;
            }
            if (result3.deltaX == -3.4E38d && result3.prev.outIdx != -2) {
                Edge edge2 = result3;
                while (true) {
                    Horz = edge2;
                    if (Horz.next.deltaX != -3.4E38d) {
                        break;
                    }
                    edge2 = Horz.next;
                }
                if (Horz.next.getTop().getX() == result3.prev.getTop().getX() || Horz.next.getTop().getX() > result3.prev.getTop().getX()) {
                    result3 = Horz.next;
                }
            }
            while (e != result3) {
                e.nextInLML = e.prev;
                if (e.deltaX == -3.4E38d && e != e && e.getBot().getX() != e.next.getTop().getX()) {
                    e.reverseHorizontal();
                }
                e = e.prev;
            }
            if (e.deltaX == -3.4E38d && e != e && e.getBot().getX() != e.next.getTop().getX()) {
                e.reverseHorizontal();
            }
            result = result3.prev;
        }
        return result;
    }

    protected static Path.OutRec parseFirstLeft(Path.OutRec FirstLeft) {
        while (FirstLeft != null && FirstLeft.getPoints() == null) {
            FirstLeft = FirstLeft.firstLeft;
        }
        return FirstLeft;
    }

    protected void reset() {
        this.currentLM = this.minimaList;
        if (this.currentLM == null) {
            return;
        }
        LocalMinima localMinima = this.minimaList;
        while (true) {
            LocalMinima lm = localMinima;
            if (lm != null) {
                Edge e = lm.leftBound;
                if (e != null) {
                    e.setCurrent(new Point.LongPoint(e.getBot()));
                    e.side = Edge.Side.LEFT;
                    e.outIdx = -1;
                }
                Edge e2 = lm.rightBound;
                if (e2 != null) {
                    e2.setCurrent(new Point.LongPoint(e2.getBot()));
                    e2.side = Edge.Side.RIGHT;
                    e2.outIdx = -1;
                }
                localMinima = lm.next;
            } else {
                return;
            }
        }
    }
}
