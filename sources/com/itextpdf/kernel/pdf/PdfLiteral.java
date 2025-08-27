package com.itextpdf.kernel.pdf;

import com.itextpdf.io.font.PdfEncodings;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfLiteral.class */
public class PdfLiteral extends PdfPrimitiveObject {
    private static final long serialVersionUID = -770215611509192403L;
    private long position;

    public PdfLiteral(byte[] content) {
        super(true);
        this.content = content;
    }

    public PdfLiteral(int size) {
        this(new byte[size]);
        Arrays.fill(this.content, (byte) 32);
    }

    public PdfLiteral(String content) {
        this(PdfEncodings.convertToBytes(content, (String) null));
    }

    private PdfLiteral() {
        this((byte[]) null);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 4;
    }

    public String toString() {
        if (this.content != null) {
            return new String(this.content, StandardCharsets.ISO_8859_1);
        }
        return "";
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getBytesCount() {
        return this.content.length;
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject
    protected void generateContent() {
    }

    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass() && Arrays.equals(this.content, ((PdfLiteral) o).content));
    }

    public int hashCode() {
        if (this.content == null) {
            return 0;
        }
        return Arrays.hashCode(this.content);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return new PdfLiteral();
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject, com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfLiteral literal = (PdfLiteral) from;
        this.content = literal.getInternalContent();
    }
}
