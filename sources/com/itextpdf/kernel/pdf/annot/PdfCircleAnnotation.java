package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfCircleAnnotation.class */
public class PdfCircleAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = -4123774794612333746L;

    public PdfCircleAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfCircleAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Circle;
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfCircleAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfCircleAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfCircleAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfCircleAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }

    public PdfArray getRectangleDifferences() {
        return getPdfObject().getAsArray(PdfName.RD);
    }

    public PdfCircleAnnotation setRectangleDifferences(PdfArray rect) {
        return (PdfCircleAnnotation) put(PdfName.RD, rect);
    }

    public PdfDictionary getBorderEffect() {
        return getPdfObject().getAsDictionary(PdfName.BE);
    }

    public PdfCircleAnnotation setBorderEffect(PdfDictionary borderEffect) {
        return (PdfCircleAnnotation) put(PdfName.BE, borderEffect);
    }

    public Color getInteriorColor() {
        return InteriorColorUtil.parseInteriorColor(getPdfObject().getAsArray(PdfName.IC));
    }

    public PdfCircleAnnotation setInteriorColor(PdfArray interiorColor) {
        return (PdfCircleAnnotation) put(PdfName.IC, interiorColor);
    }

    public PdfCircleAnnotation setInteriorColor(float[] interiorColor) {
        return setInteriorColor(new PdfArray(interiorColor));
    }
}
