package com.itextpdf.kernel.pdf;

import com.itextpdf.io.source.ByteUtils;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfBoolean.class */
public class PdfBoolean extends PdfPrimitiveObject {
    private static final long serialVersionUID = -1363839858135046832L;
    public static final PdfBoolean TRUE = new PdfBoolean(true, true);
    public static final PdfBoolean FALSE = new PdfBoolean(false, true);
    private static final byte[] True = ByteUtils.getIsoBytes("true");
    private static final byte[] False = ByteUtils.getIsoBytes("false");
    private boolean value;

    public PdfBoolean(boolean value) {
        this(value, false);
    }

    private PdfBoolean(boolean value, boolean directOnly) {
        super(directOnly);
        this.value = value;
    }

    private PdfBoolean() {
    }

    public boolean getValue() {
        return this.value;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 2;
    }

    public String toString() {
        return this.value ? "true" : "false";
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject
    protected void generateContent() {
        this.content = this.value ? True : False;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return new PdfBoolean();
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject, com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfBoolean bool = (PdfBoolean) from;
        this.value = bool.value;
    }

    public boolean equals(Object obj) {
        return this == obj || (obj != null && getClass() == obj.getClass() && this.value == ((PdfBoolean) obj).value);
    }

    public int hashCode() {
        return this.value ? 1 : 0;
    }

    public static PdfBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }
}
