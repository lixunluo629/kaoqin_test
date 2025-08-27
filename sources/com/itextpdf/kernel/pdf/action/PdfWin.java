package com.itextpdf.kernel.pdf.action;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/action/PdfWin.class */
public class PdfWin extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -3057526285278565800L;

    public PdfWin(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfWin(PdfString f) {
        this(new PdfDictionary());
        getPdfObject().put(PdfName.F, f);
    }

    public PdfWin(PdfString f, PdfString d, PdfString o, PdfString p) {
        this(new PdfDictionary());
        getPdfObject().put(PdfName.F, f);
        getPdfObject().put(PdfName.D, d);
        getPdfObject().put(PdfName.O, o);
        getPdfObject().put(PdfName.P, p);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
