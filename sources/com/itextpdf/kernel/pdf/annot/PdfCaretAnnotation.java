package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfCaretAnnotation.class */
public class PdfCaretAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = 1542932123958535397L;

    public PdfCaretAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfCaretAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Caret;
    }

    public PdfCaretAnnotation setSymbol(PdfString symbol) {
        return (PdfCaretAnnotation) put(PdfName.Sy, symbol);
    }

    public PdfString getSymbol() {
        return getPdfObject().getAsString(PdfName.Sy);
    }

    public PdfArray getRectangleDifferences() {
        return getPdfObject().getAsArray(PdfName.RD);
    }

    public PdfCaretAnnotation setRectangleDifferences(PdfArray rect) {
        return (PdfCaretAnnotation) put(PdfName.RD, rect);
    }
}
