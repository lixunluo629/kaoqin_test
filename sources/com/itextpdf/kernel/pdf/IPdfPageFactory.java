package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.geom.PageSize;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/IPdfPageFactory.class */
public interface IPdfPageFactory {
    PdfPage createPdfPage(PdfDictionary pdfDictionary);

    PdfPage createPdfPage(PdfDocument pdfDocument, PageSize pageSize);
}
