package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.action.PdfAction;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfScreenAnnotation.class */
public class PdfScreenAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 1334399136151450493L;

    public PdfScreenAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfScreenAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Screen;
    }

    public PdfDictionary getAction() {
        return getPdfObject().getAsDictionary(PdfName.A);
    }

    public PdfScreenAnnotation setAction(PdfAction action) {
        return (PdfScreenAnnotation) put(PdfName.A, action.getPdfObject());
    }

    public PdfDictionary getAdditionalAction() {
        return getPdfObject().getAsDictionary(PdfName.AA);
    }

    public PdfScreenAnnotation setAdditionalAction(PdfName key, PdfAction action) {
        PdfAction.setAdditionalAction(this, key, action);
        return this;
    }

    public PdfDictionary getAppearanceCharacteristics() {
        return getPdfObject().getAsDictionary(PdfName.MK);
    }

    public PdfScreenAnnotation setAppearanceCharacteristics(PdfDictionary characteristics) {
        return (PdfScreenAnnotation) put(PdfName.MK, characteristics);
    }
}
