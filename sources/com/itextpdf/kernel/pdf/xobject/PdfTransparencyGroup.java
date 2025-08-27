package com.itextpdf.kernel.pdf.xobject;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/xobject/PdfTransparencyGroup.class */
public class PdfTransparencyGroup extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 753843601750097627L;

    public PdfTransparencyGroup() {
        super(new PdfDictionary());
        getPdfObject().put(PdfName.S, PdfName.Transparency);
    }

    public void setIsolated(boolean isolated) {
        if (isolated) {
            getPdfObject().put(PdfName.I, PdfBoolean.TRUE);
        } else {
            getPdfObject().remove(PdfName.I);
        }
    }

    public void setKnockout(boolean knockout) {
        if (knockout) {
            getPdfObject().put(PdfName.K, PdfBoolean.TRUE);
        } else {
            getPdfObject().remove(PdfName.K);
        }
    }

    public void setColorSpace(PdfName colorSpace) {
        getPdfObject().put(PdfName.CS, colorSpace);
    }

    public void setColorSpace(PdfArray colorSpace) {
        getPdfObject().put(PdfName.CS, colorSpace);
    }

    public PdfTransparencyGroup put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }
}
