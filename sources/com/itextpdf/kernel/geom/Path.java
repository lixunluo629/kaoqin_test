package com.itextpdf.kernel.geom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/Path.class */
public class Path implements Serializable {
    private static final String START_PATH_ERR_MSG = "Path shall start with \"re\" or \"m\" operator";
    private static final long serialVersionUID = 1658560770858987684L;
    private List<Subpath> subpaths = new ArrayList();
    private Point currentPoint;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Path.class.desiredAssertionStatus();
    }

    public Path() {
    }

    public Path(List<? extends Subpath> subpaths) {
        addSubpaths(subpaths);
    }

    public Path(Path path) {
        addSubpaths(path.getSubpaths());
    }

    public List<Subpath> getSubpaths() {
        return this.subpaths;
    }

    public void addSubpath(Subpath subpath) {
        this.subpaths.add(subpath);
        this.currentPoint = subpath.getLastPoint();
    }

    public void addSubpaths(List<? extends Subpath> subpaths) {
        if (subpaths.size() > 0) {
            for (Subpath subpath : subpaths) {
                this.subpaths.add(new Subpath(subpath));
            }
            this.currentPoint = this.subpaths.get(subpaths.size() - 1).getLastPoint();
        }
    }

    public Point getCurrentPoint() {
        return this.currentPoint;
    }

    public void moveTo(float x, float y) {
        this.currentPoint = new Point(x, y);
        Subpath lastSubpath = this.subpaths.size() > 0 ? this.subpaths.get(this.subpaths.size() - 1) : null;
        if (lastSubpath != null && lastSubpath.isSinglePointOpen()) {
            lastSubpath.setStartPoint(this.currentPoint);
        } else {
            this.subpaths.add(new Subpath(this.currentPoint));
        }
    }

    public void lineTo(float x, float y) {
        if (this.currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }
        Point targetPoint = new Point(x, y);
        getLastSubpath().addSegment(new Line(this.currentPoint, targetPoint));
        this.currentPoint = targetPoint;
    }

    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (this.currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }
        Point secondPoint = new Point(x1, y1);
        Point thirdPoint = new Point(x2, y2);
        Point fourthPoint = new Point(x3, y3);
        List<Point> controlPoints = new ArrayList<>(Arrays.asList(this.currentPoint, secondPoint, thirdPoint, fourthPoint));
        getLastSubpath().addSegment(new BezierCurve(controlPoints));
        this.currentPoint = fourthPoint;
    }

    public void curveTo(float x2, float y2, float x3, float y3) {
        if (this.currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }
        curveTo((float) this.currentPoint.getX(), (float) this.currentPoint.getY(), x2, y2, x3, y3);
    }

    public void curveFromTo(float x1, float y1, float x3, float y3) {
        if (this.currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }
        curveTo(x1, y1, x3, y3, x3, y3);
    }

    public void rectangle(Rectangle rect) {
        rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void rectangle(float x, float y, float w, float h) {
        moveTo(x, y);
        lineTo(x + w, y);
        lineTo(x + w, y + h);
        lineTo(x, y + h);
        closeSubpath();
    }

    public void closeSubpath() {
        if (!isEmpty()) {
            Subpath lastSubpath = getLastSubpath();
            lastSubpath.setClosed(true);
            Point startPoint = lastSubpath.getStartPoint();
            moveTo((float) startPoint.getX(), (float) startPoint.getY());
        }
    }

    public void closeAllSubpaths() {
        for (Subpath subpath : this.subpaths) {
            subpath.setClosed(true);
        }
    }

    public List<Integer> replaceCloseWithLine() {
        List<Integer> modifiedSubpathsIndices = new ArrayList<>();
        int i = 0;
        for (Subpath subpath : this.subpaths) {
            if (subpath.isClosed()) {
                subpath.setClosed(false);
                subpath.addSegment(new Line(subpath.getLastPoint(), subpath.getStartPoint()));
                modifiedSubpathsIndices.add(Integer.valueOf(i));
            }
            i++;
        }
        return modifiedSubpathsIndices;
    }

    public boolean isEmpty() {
        return this.subpaths.size() == 0;
    }

    private Subpath getLastSubpath() {
        if ($assertionsDisabled || this.subpaths.size() > 0) {
            return this.subpaths.get(this.subpaths.size() - 1);
        }
        throw new AssertionError();
    }
}
