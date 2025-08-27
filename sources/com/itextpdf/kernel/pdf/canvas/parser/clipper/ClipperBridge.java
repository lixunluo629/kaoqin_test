package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.geom.Subpath;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.IClipper;
import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/ClipperBridge.class */
public class ClipperBridge {
    public static double floatMultiplier = Math.pow(10.0d, 14.0d);

    public static com.itextpdf.kernel.geom.Path convertToPath(PolyTree result) {
        com.itextpdf.kernel.geom.Path path = new com.itextpdf.kernel.geom.Path();
        PolyNode first = result.getFirst();
        while (true) {
            PolyNode node = first;
            if (node != null) {
                addContour(path, node.getContour(), !node.isOpen());
                first = node.getNext();
            } else {
                return path;
            }
        }
    }

    public static void addPath(IClipper clipper, com.itextpdf.kernel.geom.Path path, IClipper.PolyType polyType) {
        for (Subpath subpath : path.getSubpaths()) {
            if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                List<com.itextpdf.kernel.geom.Point> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                clipper.addPath(new Path(convertToLongPoints(linearApproxPoints)), polyType, subpath.isClosed());
            }
        }
    }

    public static List<Subpath> addPath(ClipperOffset offset, com.itextpdf.kernel.geom.Path path, IClipper.JoinType joinType, IClipper.EndType endType) {
        IClipper.EndType endType2;
        List<Subpath> degenerateSubpaths = new ArrayList<>();
        for (Subpath subpath : path.getSubpaths()) {
            if (subpath.isDegenerate()) {
                degenerateSubpaths.add(subpath);
            } else if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                if (subpath.isClosed()) {
                    endType2 = IClipper.EndType.CLOSED_LINE;
                } else {
                    endType2 = endType;
                }
                IClipper.EndType et = endType2;
                List<com.itextpdf.kernel.geom.Point> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                offset.addPath(new Path(convertToLongPoints(linearApproxPoints)), joinType, et);
            }
        }
        return degenerateSubpaths;
    }

    public static List<com.itextpdf.kernel.geom.Point> convertToFloatPoints(List<Point.LongPoint> points) {
        List<com.itextpdf.kernel.geom.Point> convertedPoints = new ArrayList<>(points.size());
        for (Point.LongPoint point : points) {
            convertedPoints.add(new com.itextpdf.kernel.geom.Point(point.getX() / floatMultiplier, point.getY() / floatMultiplier));
        }
        return convertedPoints;
    }

    public static List<Point.LongPoint> convertToLongPoints(List<com.itextpdf.kernel.geom.Point> points) {
        List<Point.LongPoint> convertedPoints = new ArrayList<>(points.size());
        for (com.itextpdf.kernel.geom.Point point : points) {
            convertedPoints.add(new Point.LongPoint(floatMultiplier * point.getX(), floatMultiplier * point.getY()));
        }
        return convertedPoints;
    }

    public static IClipper.JoinType getJoinType(int lineJoinStyle) {
        switch (lineJoinStyle) {
            case 0:
                return IClipper.JoinType.MITER;
            case 2:
                return IClipper.JoinType.BEVEL;
            default:
                return IClipper.JoinType.ROUND;
        }
    }

    public static IClipper.EndType getEndType(int lineCapStyle) {
        switch (lineCapStyle) {
            case 0:
                return IClipper.EndType.OPEN_BUTT;
            case 2:
                return IClipper.EndType.OPEN_SQUARE;
            default:
                return IClipper.EndType.OPEN_ROUND;
        }
    }

    public static IClipper.PolyFillType getFillType(int fillingRule) {
        IClipper.PolyFillType fillType = IClipper.PolyFillType.NON_ZERO;
        if (fillingRule == 2) {
            fillType = IClipper.PolyFillType.EVEN_ODD;
        }
        return fillType;
    }

    public static boolean addPolygonToClipper(IClipper clipper, com.itextpdf.kernel.geom.Point[] polyVertices, IClipper.PolyType polyType) {
        return clipper.addPath(new Path(convertToLongPoints(new ArrayList(Arrays.asList(polyVertices)))), polyType, true);
    }

    public static boolean addPolylineSubjectToClipper(IClipper clipper, com.itextpdf.kernel.geom.Point[] lineVertices) {
        return clipper.addPath(new Path(convertToLongPoints(new ArrayList(Arrays.asList(lineVertices)))), IClipper.PolyType.SUBJECT, false);
    }

    static void addContour(com.itextpdf.kernel.geom.Path path, List<Point.LongPoint> contour, boolean close) {
        List<com.itextpdf.kernel.geom.Point> floatContour = convertToFloatPoints(contour);
        com.itextpdf.kernel.geom.Point point = floatContour.get(0);
        path.moveTo((float) point.getX(), (float) point.getY());
        for (int i = 1; i < floatContour.size(); i++) {
            com.itextpdf.kernel.geom.Point point2 = floatContour.get(i);
            path.lineTo((float) point2.getX(), (float) point2.getY());
        }
        if (close) {
            path.closeSubpath();
        }
    }

    @Deprecated
    public static void addRectToClipper(IClipper clipper, com.itextpdf.kernel.geom.Point[] rectVertices, IClipper.PolyType polyType) {
        clipper.addPath(new Path(convertToLongPoints(new ArrayList(Arrays.asList(rectVertices)))), polyType, true);
    }
}
