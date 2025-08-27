package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfPopupAnnotation.class */
public class PdfPopupAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = -8892617787951569855L;
    protected PdfAnnotation parent;

    public PdfPopupAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfPopupAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Popup;
    }

    public PdfDictionary getParentObject() {
        return getPdfObject().getAsDictionary(PdfName.Parent);
    }

    public PdfAnnotation getParent() {
        if (this.parent == null) {
            this.parent = makeAnnotation(getParentObject());
        }
        return this.parent;
    }

    public PdfPopupAnnotation setParent(PdfAnnotation parent) {
        this.parent = parent;
        return (PdfPopupAnnotation) put(PdfName.Parent, parent.getPdfObject());
    }

    public boolean getOpen() {
        return PdfBoolean.TRUE.equals(getPdfObject().getAsBoolean(PdfName.Open));
    }

    public PdfPopupAnnotation setOpen(boolean open) {
        return (PdfPopupAnnotation) put(PdfName.Open, PdfBoolean.valueOf(open));
    }
}
