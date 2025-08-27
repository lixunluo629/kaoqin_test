package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfWatermarkAnnotation.class */
public class PdfWatermarkAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = -4490286782196827176L;

    public PdfWatermarkAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfWatermarkAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Watermark;
    }

    public PdfWatermarkAnnotation setFixedPrint(PdfFixedPrint fixedPrint) {
        return (PdfWatermarkAnnotation) put(PdfName.FixedPrint, fixedPrint.getPdfObject());
    }

    public PdfDictionary getFixedPrint() {
        return getPdfObject().getAsDictionary(PdfName.FixedPrint);
    }
}
