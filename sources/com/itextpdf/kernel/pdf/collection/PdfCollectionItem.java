package com.itextpdf.kernel.pdf.collection;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/collection/PdfCollectionItem.class */
public class PdfCollectionItem extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -6471103872805179766L;
    private PdfCollectionSchema schema;

    public PdfCollectionItem(PdfCollectionSchema schema) {
        super(new PdfDictionary());
        this.schema = schema;
    }

    public PdfCollectionItem addItem(String key, String value) {
        PdfCollectionField field = this.schema.getField(key);
        getPdfObject().put(new PdfName(key), field.getValue(value));
        return this;
    }

    public void addItem(String key, PdfDate d) {
        PdfCollectionField field = this.schema.getField(key);
        if (field.subType == 1) {
            getPdfObject().put(new PdfName(key), d.getPdfObject());
        }
    }

    public void addItem(String key, PdfNumber n) {
        PdfCollectionField field = this.schema.getField(key);
        if (field.subType == 2) {
            getPdfObject().put(new PdfName(key), n);
        }
    }

    public PdfCollectionItem setPrefix(String key, String prefix) {
        PdfName fieldName = new PdfName(key);
        PdfObject obj = getPdfObject().get(fieldName);
        if (obj == null) {
            throw new PdfException(PdfException.YouMustSetAValueBeforeAddingAPrefix);
        }
        PdfDictionary subItem = new PdfDictionary();
        subItem.put(PdfName.D, obj);
        subItem.put(PdfName.P, new PdfString(prefix));
        getPdfObject().put(fieldName, subItem);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
