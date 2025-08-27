package com.itextpdf.kernel.pdf;

import com.itextpdf.io.source.ByteUtils;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfNull.class */
public class PdfNull extends PdfPrimitiveObject {
    private static final long serialVersionUID = 7789114018630038033L;
    public static final PdfNull PDF_NULL = new PdfNull(true);
    private static final byte[] NullContent = ByteUtils.getIsoBytes("null");

    public PdfNull() {
    }

    private PdfNull(boolean directOnly) {
        super(directOnly);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 7;
    }

    public String toString() {
        return "null";
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject
    protected void generateContent() {
        this.content = NullContent;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return new PdfNull();
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject, com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
    }

    public boolean equals(Object obj) {
        return this == obj || (obj != null && getClass() == obj.getClass());
    }

    public int hashCode() {
        return 0;
    }
}
