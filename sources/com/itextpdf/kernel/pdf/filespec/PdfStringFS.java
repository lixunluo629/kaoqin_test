package com.itextpdf.kernel.pdf.filespec;

import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filespec/PdfStringFS.class */
public class PdfStringFS extends PdfFileSpec {
    private static final long serialVersionUID = 3440302276954369264L;

    public PdfStringFS(String string) {
        super(new PdfString(string));
    }

    public PdfStringFS(PdfString pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.filespec.PdfFileSpec, com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
