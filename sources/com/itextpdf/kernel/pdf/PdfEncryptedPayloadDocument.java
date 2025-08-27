package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfEncryptedPayloadDocument.class */
public class PdfEncryptedPayloadDocument extends PdfObjectWrapper<PdfStream> {
    private PdfFileSpec fileSpec;
    private String name;

    public PdfEncryptedPayloadDocument(PdfStream pdfObject, PdfFileSpec fileSpec, String name) {
        super(pdfObject);
        this.fileSpec = fileSpec;
        this.name = name;
    }

    public byte[] getDocumentBytes() {
        return getPdfObject().getBytes();
    }

    public PdfFileSpec getFileSpec() {
        return this.fileSpec;
    }

    public String getName() {
        return this.name;
    }

    public PdfEncryptedPayload getEncryptedPayload() {
        return PdfEncryptedPayload.extractFrom(this.fileSpec);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }
}
