package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfLineAnnotation.class */
public class PdfLineAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = -6047928061827404283L;

    public PdfLineAnnotation(Rectangle rect, float[] line) {
        super(rect);
        put(PdfName.L, new PdfArray(line));
    }

    protected PdfLineAnnotation(PdfDictionary pdfDictionary) {
        super(pdfDictionary);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Line;
    }

    public PdfArray getLine() {
        return getPdfObject().getAsArray(PdfName.L);
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfLineAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfLineAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfLineAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfLineAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }

    public PdfArray getLineEndingStyles() {
        return getPdfObject().getAsArray(PdfName.LE);
    }

    public PdfLineAnnotation setLineEndingStyles(PdfArray lineEndingStyles) {
        return (PdfLineAnnotation) put(PdfName.LE, lineEndingStyles);
    }

    public Color getInteriorColor() {
        return InteriorColorUtil.parseInteriorColor(getPdfObject().getAsArray(PdfName.IC));
    }

    public PdfLineAnnotation setInteriorColor(PdfArray interiorColor) {
        return (PdfLineAnnotation) put(PdfName.IC, interiorColor);
    }

    public PdfLineAnnotation setInteriorColor(float[] interiorColor) {
        return setInteriorColor(new PdfArray(interiorColor));
    }

    public float getLeaderLineLength() {
        PdfNumber n = getPdfObject().getAsNumber(PdfName.LL);
        if (n == null) {
            return 0.0f;
        }
        return n.floatValue();
    }

    public PdfLineAnnotation setLeaderLineLength(float leaderLineLength) {
        return (PdfLineAnnotation) put(PdfName.LL, new PdfNumber(leaderLineLength));
    }

    public float getLeaderLineExtension() {
        PdfNumber n = getPdfObject().getAsNumber(PdfName.LLE);
        if (n == null) {
            return 0.0f;
        }
        return n.floatValue();
    }

    public PdfLineAnnotation setLeaderLineExtension(float leaderLineExtension) {
        return (PdfLineAnnotation) put(PdfName.LLE, new PdfNumber(leaderLineExtension));
    }

    public float getLeaderLineOffset() {
        PdfNumber n = getPdfObject().getAsNumber(PdfName.LLO);
        if (n == null) {
            return 0.0f;
        }
        return n.floatValue();
    }

    public PdfLineAnnotation setLeaderLineOffset(float leaderLineOffset) {
        return (PdfLineAnnotation) put(PdfName.LLO, new PdfNumber(leaderLineOffset));
    }

    public boolean getContentsAsCaption() {
        PdfBoolean b = getPdfObject().getAsBoolean(PdfName.Cap);
        return b != null && b.getValue();
    }

    public PdfLineAnnotation setContentsAsCaption(boolean contentsAsCaption) {
        return (PdfLineAnnotation) put(PdfName.Cap, PdfBoolean.valueOf(contentsAsCaption));
    }

    public PdfName getCaptionPosition() {
        return getPdfObject().getAsName(PdfName.CP);
    }

    public PdfLineAnnotation setCaptionPosition(PdfName captionPosition) {
        return (PdfLineAnnotation) put(PdfName.CP, captionPosition);
    }

    public PdfDictionary getMeasure() {
        return getPdfObject().getAsDictionary(PdfName.Measure);
    }

    public PdfLineAnnotation setMeasure(PdfDictionary measure) {
        return (PdfLineAnnotation) put(PdfName.Measure, measure);
    }

    public PdfArray getCaptionOffset() {
        return getPdfObject().getAsArray(PdfName.CO);
    }

    public PdfLineAnnotation setCaptionOffset(PdfArray captionOffset) {
        return (PdfLineAnnotation) put(PdfName.CO, captionOffset);
    }

    public PdfLineAnnotation setCaptionOffset(float[] captionOffset) {
        return setCaptionOffset(new PdfArray(captionOffset));
    }
}
