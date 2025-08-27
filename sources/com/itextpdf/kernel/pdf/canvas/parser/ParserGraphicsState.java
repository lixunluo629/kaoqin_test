package com.itextpdf.kernel.pdf.canvas.parser;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.BezierCurve;
import com.itextpdf.kernel.geom.IShape;
import com.itextpdf.kernel.geom.Line;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.NoninvertibleTransformException;
import com.itextpdf.kernel.geom.Path;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.geom.Subpath;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.ClipperBridge;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.DefaultClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.PolyTree;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/ParserGraphicsState.class */
public class ParserGraphicsState extends CanvasGraphicsState {
    private static final long serialVersionUID = 5402909016194922120L;
    private Path clippingPath;

    ParserGraphicsState() {
    }

    ParserGraphicsState(ParserGraphicsState source) {
        super(source);
        if (source.clippingPath != null) {
            this.clippingPath = new Path(source.clippingPath);
        }
    }

    @Override // com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState
    public void updateCtm(Matrix newCtm) {
        super.updateCtm(newCtm);
        if (this.clippingPath != null) {
            transformClippingPath(newCtm);
        }
    }

    public void clip(Path path, int fillingRule) {
        if (this.clippingPath == null || this.clippingPath.isEmpty()) {
            return;
        }
        Path pathCopy = new Path(path);
        pathCopy.closeAllSubpaths();
        IClipper clipper = new DefaultClipper();
        ClipperBridge.addPath(clipper, this.clippingPath, IClipper.PolyType.SUBJECT);
        ClipperBridge.addPath(clipper, pathCopy, IClipper.PolyType.CLIP);
        PolyTree resultTree = new PolyTree();
        clipper.execute(IClipper.ClipType.INTERSECTION, resultTree, IClipper.PolyFillType.NON_ZERO, ClipperBridge.getFillType(fillingRule));
        this.clippingPath = ClipperBridge.convertToPath(resultTree);
    }

    public Path getClippingPath() {
        return this.clippingPath;
    }

    public void setClippingPath(Path clippingPath) {
        Path pathCopy = new Path(clippingPath);
        pathCopy.closeAllSubpaths();
        this.clippingPath = pathCopy;
    }

    private void transformClippingPath(Matrix newCtm) {
        Path path = new Path();
        for (Subpath subpath : this.clippingPath.getSubpaths()) {
            Subpath transformedSubpath = transformSubpath(subpath, newCtm);
            path.addSubpath(transformedSubpath);
        }
        this.clippingPath = path;
    }

    private Subpath transformSubpath(Subpath subpath, Matrix newCtm) {
        Subpath newSubpath = new Subpath();
        newSubpath.setClosed(subpath.isClosed());
        for (IShape segment : subpath.getSegments()) {
            IShape transformedSegment = transformSegment(segment, newCtm);
            newSubpath.addSegment(transformedSegment);
        }
        return newSubpath;
    }

    private IShape transformSegment(IShape segment, Matrix newCtm) {
        IShape newSegment;
        List<Point> segBasePts = segment.getBasePoints();
        Point[] transformedPoints = transformPoints(newCtm, (Point[]) segBasePts.toArray(new Point[segBasePts.size()]));
        if (segment instanceof BezierCurve) {
            newSegment = new BezierCurve(Arrays.asList(transformedPoints));
        } else {
            newSegment = new Line(transformedPoints[0], transformedPoints[1]);
        }
        return newSegment;
    }

    private Point[] transformPoints(Matrix transformationMatrix, Point... points) {
        try {
            AffineTransform t = new AffineTransform(transformationMatrix.get(0), transformationMatrix.get(1), transformationMatrix.get(3), transformationMatrix.get(4), transformationMatrix.get(6), transformationMatrix.get(7));
            AffineTransform t2 = t.createInverse();
            Point[] transformed = new Point[points.length];
            t2.transform(points, 0, transformed, 0, points.length);
            return transformed;
        } catch (NoninvertibleTransformException e) {
            throw new PdfException(PdfException.NoninvertibleMatrixCannotBeProcessed, (Throwable) e);
        }
    }
}
