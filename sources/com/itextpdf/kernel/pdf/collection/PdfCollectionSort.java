package com.itextpdf.kernel.pdf.collection;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import java.util.Arrays;
import java.util.List;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/collection/PdfCollectionSort.class */
public class PdfCollectionSort extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -3871923275239410475L;

    public PdfCollectionSort(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfCollectionSort(String key) {
        this(new PdfDictionary());
        getPdfObject().put(PdfName.S, new PdfName(key));
    }

    public PdfCollectionSort(String[] keys) {
        this(new PdfDictionary());
        getPdfObject().put(PdfName.S, new PdfArray((List<String>) Arrays.asList(keys), true));
    }

    public PdfCollectionSort setSortOrder(boolean ascending) {
        PdfObject obj = getPdfObject().get(PdfName.S);
        if (obj.isName()) {
            getPdfObject().put(PdfName.A, PdfBoolean.valueOf(ascending));
            return this;
        }
        throw new PdfException(PdfException.YouHaveToDefineABooleanArrayForThisCollectionSortDictionary);
    }

    public PdfCollectionSort setSortOrder(boolean[] ascending) {
        PdfObject obj = getPdfObject().get(PdfName.S);
        if (obj.isArray()) {
            if (((PdfArray) obj).size() != ascending.length) {
                throw new PdfException(PdfException.NumberOfBooleansInTheArrayDoesntCorrespondWithTheNumberOfFields);
            }
            getPdfObject().put(PdfName.A, new PdfArray(ascending));
            return this;
        }
        throw new PdfException(PdfException.YouNeedASingleBooleanForThisCollectionSortDictionary);
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
