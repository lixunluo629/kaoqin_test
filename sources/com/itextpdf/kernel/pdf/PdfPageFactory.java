package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.geom.PageSize;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfPageFactory.class */
class PdfPageFactory implements IPdfPageFactory {
    PdfPageFactory() {
    }

    @Override // com.itextpdf.kernel.pdf.IPdfPageFactory
    public PdfPage createPdfPage(PdfDictionary pdfObject) {
        return new PdfPage(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.IPdfPageFactory
    public PdfPage createPdfPage(PdfDocument pdfDocument, PageSize pageSize) {
        return new PdfPage(pdfDocument, pageSize);
    }
}
