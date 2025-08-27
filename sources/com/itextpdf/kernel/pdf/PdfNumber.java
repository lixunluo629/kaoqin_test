package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteUtils;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfNumber.class */
public class PdfNumber extends PdfPrimitiveObject {
    private static final long serialVersionUID = -250799718574024246L;
    private double value;
    private boolean isDouble;
    private boolean changed;

    public PdfNumber(double value) {
        this.changed = false;
        setValue(value);
    }

    public PdfNumber(int value) {
        this.changed = false;
        setValue(value);
    }

    public PdfNumber(byte[] content) {
        super(content);
        this.changed = false;
        this.isDouble = true;
        this.value = Double.NaN;
    }

    private PdfNumber() {
        this.changed = false;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 8;
    }

    public double getValue() {
        if (Double.isNaN(this.value)) {
            generateValue();
        }
        return this.value;
    }

    public double doubleValue() {
        return getValue();
    }

    public float floatValue() {
        return (float) getValue();
    }

    public long longValue() {
        return (long) getValue();
    }

    public int intValue() {
        return (int) getValue();
    }

    public void setValue(int value) {
        this.value = value;
        this.isDouble = false;
        this.content = null;
        this.changed = true;
    }

    public void setValue(double value) {
        this.value = value;
        this.isDouble = true;
        this.content = null;
    }

    public void increment() {
        double d = this.value + 1.0d;
        this.value = d;
        setValue(d);
    }

    public void decrement() {
        double d = this.value - 1.0d;
        this.value = d;
        setValue(d);
    }

    public String toString() {
        if (this.content != null) {
            return new String(this.content, StandardCharsets.ISO_8859_1);
        }
        if (this.isDouble) {
            return new String(ByteUtils.getIsoBytes(getValue()), StandardCharsets.ISO_8859_1);
        }
        return new String(ByteUtils.getIsoBytes(intValue()), StandardCharsets.ISO_8859_1);
    }

    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass() && Double.compare(((PdfNumber) o).value, this.value) == 0);
    }

    public int hashCode() {
        if (this.changed) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfReader.class);
            logger.warn(LogMessageConstant.CALCULATE_HASHCODE_FOR_MODIFIED_PDFNUMBER);
            this.changed = false;
        }
        long hash = Double.doubleToLongBits(this.value);
        return (int) (hash ^ (hash >>> 32));
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return new PdfNumber();
    }

    protected boolean isDoubleNumber() {
        return this.isDouble;
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject
    protected void generateContent() {
        if (this.isDouble) {
            this.content = ByteUtils.getIsoBytes(this.value);
        } else {
            this.content = ByteUtils.getIsoBytes((int) this.value);
        }
    }

    protected void generateValue() {
        try {
            this.value = Double.parseDouble(new String(this.content, StandardCharsets.ISO_8859_1));
        } catch (NumberFormatException e) {
            this.value = Double.NaN;
        }
        this.isDouble = true;
    }

    @Override // com.itextpdf.kernel.pdf.PdfPrimitiveObject, com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfNumber number = (PdfNumber) from;
        this.value = number.value;
        this.isDouble = number.isDouble;
    }
}
