package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/DoNothingFilter.class */
public class DoNothingFilter implements IFilterHandler {
    private PdfName lastFilterName;

    @Override // com.itextpdf.kernel.pdf.filters.IFilterHandler
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        this.lastFilterName = filterName;
        return b;
    }

    public PdfName getLastFilterName() {
        return this.lastFilterName;
    }
}
