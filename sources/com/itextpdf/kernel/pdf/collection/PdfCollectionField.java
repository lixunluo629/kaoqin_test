package com.itextpdf.kernel.pdf.collection;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/collection/PdfCollectionField.class */
public class PdfCollectionField extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 4766153544105870238L;
    public static final int TEXT = 0;
    public static final int DATE = 1;
    public static final int NUMBER = 2;
    public static final int FILENAME = 3;
    public static final int DESC = 4;
    public static final int MODDATE = 5;
    public static final int CREATIONDATE = 6;
    public static final int SIZE = 7;
    protected int subType;

    protected PdfCollectionField(PdfDictionary pdfObject) {
        super(pdfObject);
        String subType = pdfObject.getAsName(PdfName.Subtype).getValue();
        switch (subType) {
            case "D":
                this.subType = 1;
                break;
            case "N":
                this.subType = 2;
                break;
            case "F":
                this.subType = 3;
                break;
            case "Desc":
                this.subType = 4;
                break;
            case "ModDate":
                this.subType = 5;
                break;
            case "CreationDate":
                this.subType = 6;
                break;
            case "Size":
                this.subType = 7;
                break;
            default:
                this.subType = 0;
                break;
        }
    }

    public PdfCollectionField(String name, int subType) {
        super(new PdfDictionary());
        getPdfObject().put(PdfName.N, new PdfString(name));
        this.subType = subType;
        switch (subType) {
            case 1:
                getPdfObject().put(PdfName.Subtype, PdfName.D);
                break;
            case 2:
                getPdfObject().put(PdfName.Subtype, PdfName.N);
                break;
            case 3:
                getPdfObject().put(PdfName.Subtype, PdfName.F);
                break;
            case 4:
                getPdfObject().put(PdfName.Subtype, PdfName.Desc);
                break;
            case 5:
                getPdfObject().put(PdfName.Subtype, PdfName.ModDate);
                break;
            case 6:
                getPdfObject().put(PdfName.Subtype, PdfName.CreationDate);
                break;
            case 7:
                getPdfObject().put(PdfName.Subtype, PdfName.Size);
                break;
            default:
                getPdfObject().put(PdfName.Subtype, PdfName.S);
                break;
        }
    }

    public PdfCollectionField setOrder(int order) {
        getPdfObject().put(PdfName.O, new PdfNumber(order));
        return this;
    }

    public PdfNumber getOrder() {
        return getPdfObject().getAsNumber(PdfName.O);
    }

    public PdfCollectionField setVisibility(boolean visible) {
        getPdfObject().put(PdfName.V, PdfBoolean.valueOf(visible));
        return this;
    }

    public PdfBoolean getVisibility() {
        return getPdfObject().getAsBoolean(PdfName.V);
    }

    public PdfCollectionField setEditable(boolean editable) {
        getPdfObject().put(PdfName.E, PdfBoolean.valueOf(editable));
        return this;
    }

    public PdfBoolean getEditable() {
        return getPdfObject().getAsBoolean(PdfName.E);
    }

    public PdfObject getValue(String value) {
        switch (this.subType) {
            case 0:
                return new PdfString(value);
            case 1:
                return new PdfDate(PdfDate.decode(value)).getPdfObject();
            case 2:
                return new PdfNumber(Double.parseDouble(value.trim()));
            default:
                throw new PdfException(PdfException._1IsNotAnAcceptableValueForTheField2).setMessageParams(value, getPdfObject().getAsName(PdfName.N).getValue());
        }
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
