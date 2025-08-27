package org.apache.poi.xdgf.geom;

import com.graphbuilder.curve.ControlPath;
import com.graphbuilder.curve.GroupIterator;
import com.graphbuilder.curve.NURBSpline;
import com.graphbuilder.curve.ShapeMultiPath;
import com.graphbuilder.curve.ValueVector;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/geom/SplineRenderer.class */
public class SplineRenderer {
    public static ShapeMultiPath createNurbsSpline(ControlPath controlPoints, ValueVector knots, ValueVector weights, int degree) {
        double firstKnot = knots.get(0);
        int count = knots.size();
        double lastKnot = knots.get(count - 1);
        for (int i = 0; i < count; i++) {
            knots.set((knots.get(i) - firstKnot) / lastKnot, i);
        }
        int knotsToAdd = controlPoints.numPoints() + degree + 1;
        for (int i2 = count; i2 < knotsToAdd; i2++) {
            knots.add(1.0d);
        }
        GroupIterator gi = new GroupIterator("0:n-1", controlPoints.numPoints());
        NURBSpline spline = new NURBSpline(controlPoints, gi);
        spline.setDegree(degree);
        spline.setKnotVectorType(2);
        spline.setKnotVector(knots);
        if (weights == null) {
            spline.setUseWeightVector(false);
        } else {
            spline.setWeightVector(weights);
        }
        ShapeMultiPath shape = new ShapeMultiPath();
        shape.setFlatness(0.01d);
        spline.appendTo(shape);
        return shape;
    }
}
