package com.itextpdf.kernel.pdf.filters;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filters/IFilterHandler.class */
public interface IFilterHandler {
    byte[] decode(byte[] bArr, PdfName pdfName, PdfObject pdfObject, PdfDictionary pdfDictionary);
}
