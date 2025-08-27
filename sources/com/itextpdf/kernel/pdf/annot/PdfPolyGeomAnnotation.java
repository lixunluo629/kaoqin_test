package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfPolyGeomAnnotation.class */
public abstract class PdfPolyGeomAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = -9038993253308315792L;

    @Deprecated
    public static final PdfName Polygon = PdfName.Polygon;

    @Deprecated
    public static final PdfName PolyLine = PdfName.PolyLine;

    PdfPolyGeomAnnotation(Rectangle rect, float[] vertices) {
        super(rect);
        setVertices(vertices);
    }

    protected PdfPolyGeomAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public static PdfPolyGeomAnnotation createPolygon(Rectangle rect, float[] vertices) {
        return new PdfPolygonAnnotation(rect, vertices);
    }

    public static PdfPolyGeomAnnotation createPolyLine(Rectangle rect, float[] vertices) {
        return new PdfPolylineAnnotation(rect, vertices);
    }

    public PdfArray getVertices() {
        return getPdfObject().getAsArray(PdfName.Vertices);
    }

    public PdfPolyGeomAnnotation setVertices(PdfArray vertices) {
        if (getPdfObject().containsKey(PdfName.Path)) {
            LoggerFactory.getLogger(getClass()).warn(LogMessageConstant.PATH_KEY_IS_PRESENT_VERTICES_WILL_BE_IGNORED);
        }
        return (PdfPolyGeomAnnotation) put(PdfName.Vertices, vertices);
    }

    public PdfPolyGeomAnnotation setVertices(float[] vertices) {
        if (getPdfObject().containsKey(PdfName.Path)) {
            LoggerFactory.getLogger(getClass()).warn(LogMessageConstant.PATH_KEY_IS_PRESENT_VERTICES_WILL_BE_IGNORED);
        }
        return (PdfPolyGeomAnnotation) put(PdfName.Vertices, new PdfArray(vertices));
    }

    public PdfArray getLineEndingStyles() {
        return getPdfObject().getAsArray(PdfName.LE);
    }

    public PdfPolyGeomAnnotation setLineEndingStyles(PdfArray lineEndingStyles) {
        return (PdfPolyGeomAnnotation) put(PdfName.LE, lineEndingStyles);
    }

    public PdfDictionary getMeasure() {
        return getPdfObject().getAsDictionary(PdfName.Measure);
    }

    public PdfPolyGeomAnnotation setMeasure(PdfDictionary measure) {
        return (PdfPolyGeomAnnotation) put(PdfName.Measure, measure);
    }

    public PdfArray getPath() {
        return getPdfObject().getAsArray(PdfName.Path);
    }

    public PdfPolyGeomAnnotation setPath(PdfArray path) {
        if (getPdfObject().containsKey(PdfName.Vertices)) {
            LoggerFactory.getLogger(getClass()).error(LogMessageConstant.IF_PATH_IS_SET_VERTICES_SHALL_NOT_BE_PRESENT);
        }
        return (PdfPolyGeomAnnotation) put(PdfName.Path, path);
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfPolyGeomAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfPolyGeomAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfPolyGeomAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfPolyGeomAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }

    public PdfDictionary getBorderEffect() {
        return getPdfObject().getAsDictionary(PdfName.BE);
    }

    public PdfPolyGeomAnnotation setBorderEffect(PdfDictionary borderEffect) {
        return (PdfPolyGeomAnnotation) put(PdfName.BE, borderEffect);
    }

    public Color getInteriorColor() {
        return InteriorColorUtil.parseInteriorColor(getPdfObject().getAsArray(PdfName.IC));
    }

    public PdfPolyGeomAnnotation setInteriorColor(PdfArray interiorColor) {
        return (PdfPolyGeomAnnotation) put(PdfName.IC, interiorColor);
    }

    public PdfPolyGeomAnnotation setInteriorColor(float[] interiorColor) {
        return setInteriorColor(new PdfArray(interiorColor));
    }
}
