package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.annot.da.AnnotationDefaultAppearance;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfRedactAnnotation.class */
public class PdfRedactAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = 8488431772407790511L;

    public PdfRedactAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfRedactAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Redact;
    }

    public PdfString getDefaultAppearance() {
        return getPdfObject().getAsString(PdfName.DA);
    }

    public PdfRedactAnnotation setDefaultAppearance(PdfString appearanceString) {
        return (PdfRedactAnnotation) put(PdfName.DA, appearanceString);
    }

    public PdfRedactAnnotation setDefaultAppearance(AnnotationDefaultAppearance da) {
        return setDefaultAppearance(da.toPdfString());
    }

    public PdfRedactAnnotation setOverlayText(PdfString text) {
        return (PdfRedactAnnotation) put(PdfName.OverlayText, text);
    }

    public PdfString getOverlayText() {
        return getPdfObject().getAsString(PdfName.OverlayText);
    }

    public PdfRedactAnnotation setRedactRolloverAppearance(PdfStream stream) {
        return (PdfRedactAnnotation) put(PdfName.RO, stream);
    }

    public PdfStream getRedactRolloverAppearance() {
        return getPdfObject().getAsStream(PdfName.RO);
    }

    public PdfRedactAnnotation setRepeat(PdfBoolean repeat) {
        return (PdfRedactAnnotation) put(PdfName.Repeat, repeat);
    }

    public PdfBoolean getRepeat() {
        return getPdfObject().getAsBoolean(PdfName.Repeat);
    }

    public PdfArray getQuadPoints() {
        return getPdfObject().getAsArray(PdfName.QuadPoints);
    }

    public PdfRedactAnnotation setQuadPoints(PdfArray quadPoints) {
        return (PdfRedactAnnotation) put(PdfName.QuadPoints, quadPoints);
    }

    public Color getInteriorColor() {
        return InteriorColorUtil.parseInteriorColor(getPdfObject().getAsArray(PdfName.IC));
    }

    public PdfRedactAnnotation setInteriorColor(PdfArray interiorColor) {
        return (PdfRedactAnnotation) put(PdfName.IC, interiorColor);
    }

    public PdfRedactAnnotation setInteriorColor(float[] interiorColor) {
        return setInteriorColor(new PdfArray(interiorColor));
    }

    public int getJustification() {
        PdfNumber q = getPdfObject().getAsNumber(PdfName.Q);
        if (q == null) {
            return 0;
        }
        return q.intValue();
    }

    public PdfRedactAnnotation setJustification(int justification) {
        return (PdfRedactAnnotation) put(PdfName.Q, new PdfNumber(justification));
    }
}
