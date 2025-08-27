package org.apache.poi.xdgf.geom;

import com.graphbuilder.curve.ControlPath;
import com.graphbuilder.curve.ShapeMultiPath;
import com.graphbuilder.curve.ValueVector;
import com.graphbuilder.geom.PointFactory;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.section.geometry.SplineKnot;
import org.apache.poi.xdgf.usermodel.section.geometry.SplineStart;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/geom/SplineCollector.class */
public class SplineCollector {
    SplineStart _start;
    ArrayList<SplineKnot> _knots = new ArrayList<>();

    public SplineCollector(SplineStart start) {
        this._start = start;
    }

    public void addKnot(SplineKnot knot) {
        if (!knot.getDel()) {
            this._knots.add(knot);
        }
    }

    public void addToPath(Path2D.Double path, XDGFShape parent) {
        Point2D last = path.getCurrentPoint();
        ControlPath controlPath = new ControlPath();
        ValueVector knots = new ValueVector(this._knots.size() + 3);
        double firstKnot = this._start.getB().doubleValue();
        double lastKnot = this._start.getC().doubleValue();
        int degree = this._start.getD().intValue();
        knots.add(firstKnot);
        knots.add(this._start.getA().doubleValue());
        controlPath.addPoint(PointFactory.create(last.getX(), last.getY()));
        controlPath.addPoint(PointFactory.create(this._start.getX().doubleValue(), this._start.getY().doubleValue()));
        Iterator i$ = this._knots.iterator();
        while (i$.hasNext()) {
            SplineKnot knot = i$.next();
            knots.add(knot.getA().doubleValue());
            controlPath.addPoint(PointFactory.create(knot.getX().doubleValue(), knot.getY().doubleValue()));
        }
        knots.add(lastKnot);
        ShapeMultiPath shape = SplineRenderer.createNurbsSpline(controlPath, knots, null, degree);
        path.append(shape, true);
    }
}
