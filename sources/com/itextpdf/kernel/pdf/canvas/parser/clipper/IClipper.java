package com.itextpdf.kernel.pdf.canvas.parser.clipper;

import com.itextpdf.kernel.pdf.canvas.parser.clipper.Point;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper.class */
public interface IClipper {
    public static final int REVERSE_SOLUTION = 1;
    public static final int STRICTLY_SIMPLE = 2;
    public static final int PRESERVE_COLINEAR = 4;

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$ClipType.class */
    public enum ClipType {
        INTERSECTION,
        UNION,
        DIFFERENCE,
        XOR
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$Direction.class */
    public enum Direction {
        RIGHT_TO_LEFT,
        LEFT_TO_RIGHT
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$EndType.class */
    public enum EndType {
        CLOSED_POLYGON,
        CLOSED_LINE,
        OPEN_BUTT,
        OPEN_SQUARE,
        OPEN_ROUND
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$IZFillCallback.class */
    public interface IZFillCallback {
        void zFill(Point.LongPoint longPoint, Point.LongPoint longPoint2, Point.LongPoint longPoint3, Point.LongPoint longPoint4, Point.LongPoint longPoint5);
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$JoinType.class */
    public enum JoinType {
        BEVEL,
        ROUND,
        MITER
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$PolyFillType.class */
    public enum PolyFillType {
        EVEN_ODD,
        NON_ZERO,
        POSITIVE,
        NEGATIVE
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/parser/clipper/IClipper$PolyType.class */
    public enum PolyType {
        SUBJECT,
        CLIP
    }

    boolean addPath(Path path, PolyType polyType, boolean z);

    boolean addPaths(Paths paths, PolyType polyType, boolean z);

    void clear();

    boolean execute(ClipType clipType, Paths paths);

    boolean execute(ClipType clipType, Paths paths, PolyFillType polyFillType, PolyFillType polyFillType2);

    boolean execute(ClipType clipType, PolyTree polyTree);

    boolean execute(ClipType clipType, PolyTree polyTree, PolyFillType polyFillType, PolyFillType polyFillType2);
}
