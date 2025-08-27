package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.annot.da.AnnotationDefaultAppearance;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfFreeTextAnnotation.class */
public class PdfFreeTextAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = -7835504102518915220L;
    public static final int LEFT_JUSTIFIED = 0;
    public static final int CENTERED = 1;
    public static final int RIGHT_JUSTIFIED = 2;

    public PdfFreeTextAnnotation(Rectangle rect, PdfString contents) {
        super(rect);
        setContents(contents);
    }

    protected PdfFreeTextAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.FreeText;
    }

    public PdfString getDefaultStyleString() {
        return getPdfObject().getAsString(PdfName.DS);
    }

    public PdfFreeTextAnnotation setDefaultStyleString(PdfString defaultStyleString) {
        return (PdfFreeTextAnnotation) put(PdfName.DS, defaultStyleString);
    }

    public PdfString getDefaultAppearance() {
        return getPdfObject().getAsString(PdfName.DA);
    }

    public PdfFreeTextAnnotation setDefaultAppearance(PdfString appearanceString) {
        return (PdfFreeTextAnnotation) put(PdfName.DA, appearanceString);
    }

    public PdfFreeTextAnnotation setDefaultAppearance(AnnotationDefaultAppearance da) {
        return setDefaultAppearance(da.toPdfString());
    }

    public PdfArray getCalloutLine() {
        return getPdfObject().getAsArray(PdfName.CL);
    }

    public PdfFreeTextAnnotation setCalloutLine(float[] calloutLine) {
        return setCalloutLine(new PdfArray(calloutLine));
    }

    public PdfFreeTextAnnotation setCalloutLine(PdfArray calloutLine) {
        return (PdfFreeTextAnnotation) put(PdfName.CL, calloutLine);
    }

    public PdfName getLineEndingStyle() {
        return getPdfObject().getAsName(PdfName.LE);
    }

    public PdfFreeTextAnnotation setLineEndingStyle(PdfName lineEndingStyle) {
        return (PdfFreeTextAnnotation) put(PdfName.LE, lineEndingStyle);
    }

    public int getJustification() {
        PdfNumber q = getPdfObject().getAsNumber(PdfName.Q);
        if (q == null) {
            return 0;
        }
        return q.intValue();
    }

    public PdfFreeTextAnnotation setJustification(int justification) {
        return (PdfFreeTextAnnotation) put(PdfName.Q, new PdfNumber(justification));
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfFreeTextAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfFreeTextAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfFreeTextAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfFreeTextAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }

    public PdfArray getRectangleDifferences() {
        return getPdfObject().getAsArray(PdfName.RD);
    }

    public PdfFreeTextAnnotation setRectangleDifferences(PdfArray rect) {
        return (PdfFreeTextAnnotation) put(PdfName.RD, rect);
    }

    public PdfDictionary getBorderEffect() {
        return getPdfObject().getAsDictionary(PdfName.BE);
    }

    public PdfFreeTextAnnotation setBorderEffect(PdfDictionary borderEffect) {
        return (PdfFreeTextAnnotation) put(PdfName.BE, borderEffect);
    }
}
