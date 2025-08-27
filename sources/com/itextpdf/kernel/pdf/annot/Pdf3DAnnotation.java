package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/Pdf3DAnnotation.class */
public class Pdf3DAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 3823509772499230844L;

    public Pdf3DAnnotation(Rectangle rect, PdfObject artwork) {
        super(rect);
        put(PdfName._3DD, artwork);
    }

    public Pdf3DAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName._3D;
    }

    public Pdf3DAnnotation setDefaultInitialView(PdfObject initialView) {
        return (Pdf3DAnnotation) put(PdfName._3DV, initialView);
    }

    public PdfObject getDefaultInitialView() {
        return getPdfObject().get(PdfName._3DV);
    }

    public Pdf3DAnnotation setActivationDictionary(PdfDictionary activationDictionary) {
        return (Pdf3DAnnotation) put(PdfName._3DA, activationDictionary);
    }

    public PdfDictionary getActivationDictionary() {
        return getPdfObject().getAsDictionary(PdfName._3DA);
    }

    public Pdf3DAnnotation setInteractive(boolean interactive) {
        return (Pdf3DAnnotation) put(PdfName._3DI, PdfBoolean.valueOf(interactive));
    }

    public PdfBoolean isInteractive() {
        return getPdfObject().getAsBoolean(PdfName._3DI);
    }

    public Pdf3DAnnotation setViewBox(Rectangle viewBox) {
        return (Pdf3DAnnotation) put(PdfName._3DB, new PdfArray(viewBox));
    }

    public Rectangle getViewBox() {
        return getPdfObject().getAsRectangle(PdfName._3DB);
    }
}
