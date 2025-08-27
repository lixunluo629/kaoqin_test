package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfUserProperty.class */
public class PdfUserProperty extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -347021704725128837L;

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfUserProperty$ValueType.class */
    public enum ValueType {
        UNKNOWN,
        TEXT,
        NUMBER,
        BOOLEAN
    }

    public PdfUserProperty(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfUserProperty(String name, String value) {
        super(new PdfDictionary());
        setName(name);
        setValue(value);
    }

    public PdfUserProperty(String name, int value) {
        super(new PdfDictionary());
        setName(name);
        setValue(value);
    }

    public PdfUserProperty(String name, float value) {
        super(new PdfDictionary());
        setName(name);
        setValue(value);
    }

    public PdfUserProperty(String name, boolean value) {
        super(new PdfDictionary());
        setName(name);
        setValue(value);
    }

    public String getName() {
        return getPdfObject().getAsString(PdfName.N).toUnicodeString();
    }

    public PdfUserProperty setName(String name) {
        getPdfObject().put(PdfName.N, new PdfString(name, PdfEncodings.UNICODE_BIG));
        return this;
    }

    public ValueType getValueType() {
        PdfObject valObj = getPdfObject().get(PdfName.V);
        if (valObj == null) {
            return ValueType.UNKNOWN;
        }
        switch (valObj.getType()) {
            case 2:
                return ValueType.BOOLEAN;
            case 8:
                return ValueType.NUMBER;
            case 10:
                return ValueType.TEXT;
            default:
                return ValueType.UNKNOWN;
        }
    }

    public PdfUserProperty setValue(String value) {
        getPdfObject().put(PdfName.V, new PdfString(value, PdfEncodings.UNICODE_BIG));
        return this;
    }

    public PdfUserProperty setValue(int value) {
        getPdfObject().put(PdfName.V, new PdfNumber(value));
        return this;
    }

    public PdfUserProperty setValue(float value) {
        getPdfObject().put(PdfName.V, new PdfNumber(value));
        return this;
    }

    public PdfUserProperty setValue(boolean value) {
        getPdfObject().put(PdfName.V, new PdfBoolean(value));
        return this;
    }

    public String getValueAsText() {
        PdfString str = getPdfObject().getAsString(PdfName.V);
        if (str != null) {
            return str.toUnicodeString();
        }
        return null;
    }

    public Float getValueAsFloat() {
        PdfNumber num = getPdfObject().getAsNumber(PdfName.V);
        return num != null ? Float.valueOf(num.floatValue()) : (Float) null;
    }

    public Boolean getValueAsBool() {
        return getPdfObject().getAsBool(PdfName.V);
    }

    public String getValueFormattedRepresentation() {
        PdfString f = getPdfObject().getAsString(PdfName.F);
        if (f != null) {
            return f.toUnicodeString();
        }
        return null;
    }

    public PdfUserProperty setValueFormattedRepresentation(String formattedRepresentation) {
        getPdfObject().put(PdfName.F, new PdfString(formattedRepresentation, PdfEncodings.UNICODE_BIG));
        return this;
    }

    public Boolean isHidden() {
        return getPdfObject().getAsBool(PdfName.H);
    }

    public PdfUserProperty setHidden(boolean isHidden) {
        getPdfObject().put(PdfName.H, new PdfBoolean(isHidden));
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
