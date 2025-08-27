package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

@Deprecated
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfRichMediaAnnotation.class */
public class PdfRichMediaAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 5368329326723025646L;

    public PdfRichMediaAnnotation(Rectangle rect) {
        super(rect);
    }

    public PdfRichMediaAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.RichMedia;
    }
}
