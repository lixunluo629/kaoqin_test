package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfFixedPrint.class */
public class PdfFixedPrint extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 4253232541458560135L;

    public PdfFixedPrint() {
        this(new PdfDictionary());
    }

    public PdfFixedPrint(PdfDictionary pdfObject) {
        super(pdfObject);
        pdfObject.put(PdfName.Type, PdfName.FixedPrint);
    }

    public PdfFixedPrint setMatrix(PdfArray matrix) {
        getPdfObject().put(PdfName.Matrix, matrix);
        return this;
    }

    public PdfFixedPrint setMatrix(float[] matrix) {
        getPdfObject().put(PdfName.Matrix, new PdfArray(matrix));
        return this;
    }

    public PdfFixedPrint setHorizontalTranslation(float horizontal) {
        getPdfObject().put(PdfName.H, new PdfNumber(horizontal));
        return this;
    }

    public PdfFixedPrint setVerticalTranslation(float vertical) {
        getPdfObject().put(PdfName.V, new PdfNumber(vertical));
        return this;
    }

    public PdfArray getMatrix() {
        return getPdfObject().getAsArray(PdfName.Matrix);
    }

    public PdfNumber getHorizontalTranslation() {
        return getPdfObject().getAsNumber(PdfName.H);
    }

    public PdfNumber getVerticalTranslation() {
        return getPdfObject().getAsNumber(PdfName.V);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }
}
