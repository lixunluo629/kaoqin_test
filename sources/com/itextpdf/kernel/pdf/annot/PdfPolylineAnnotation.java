package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfPolylineAnnotation.class */
class PdfPolylineAnnotation extends PdfPolyGeomAnnotation {
    PdfPolylineAnnotation(Rectangle rect, float[] vertices) {
        super(rect, vertices);
    }

    PdfPolylineAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.PolyLine;
    }
}
