package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfAnnotationAppearance.class */
public class PdfAnnotationAppearance extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 6989855812604521083L;

    public PdfAnnotationAppearance(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfAnnotationAppearance() {
        this(new PdfDictionary());
    }

    public PdfAnnotationAppearance setState(PdfName stateName, PdfFormXObject state) {
        getPdfObject().put(stateName, state.getPdfObject());
        return this;
    }

    public PdfAnnotationAppearance setStateObject(PdfName stateName, PdfStream state) {
        getPdfObject().put(stateName, state);
        return this;
    }

    public PdfStream getStateObject(PdfName stateName) {
        return getPdfObject().getAsStream(stateName);
    }

    public Set<PdfName> getStates() {
        return getPdfObject().keySet();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
