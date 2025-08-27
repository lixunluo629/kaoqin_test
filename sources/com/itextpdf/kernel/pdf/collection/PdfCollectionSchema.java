package com.itextpdf.kernel.pdf.collection;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/collection/PdfCollectionSchema.class */
public class PdfCollectionSchema extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -4388183665435879535L;

    public PdfCollectionSchema(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfCollectionSchema() {
        this(new PdfDictionary());
    }

    public PdfCollectionSchema addField(String name, PdfCollectionField field) {
        getPdfObject().put(new PdfName(name), field.getPdfObject());
        return this;
    }

    public PdfCollectionField getField(String name) {
        return new PdfCollectionField(getPdfObject().getAsDictionary(new PdfName(name)));
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
