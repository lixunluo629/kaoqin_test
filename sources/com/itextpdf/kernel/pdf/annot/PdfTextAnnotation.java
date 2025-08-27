package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfTextAnnotation.class */
public class PdfTextAnnotation extends PdfMarkupAnnotation {
    private static final long serialVersionUID = -2061119066076464569L;

    public PdfTextAnnotation(Rectangle rect) {
        super(rect);
    }

    protected PdfTextAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Text;
    }

    public PdfString getState() {
        return getPdfObject().getAsString(PdfName.State);
    }

    public PdfTextAnnotation setState(PdfString state) {
        return (PdfTextAnnotation) put(PdfName.State, state);
    }

    public PdfString getStateModel() {
        return getPdfObject().getAsString(PdfName.StateModel);
    }

    public PdfTextAnnotation setStateModel(PdfString stateModel) {
        return (PdfTextAnnotation) put(PdfName.StateModel, stateModel);
    }

    public boolean getOpen() {
        return PdfBoolean.TRUE.equals(getPdfObject().getAsBoolean(PdfName.Open));
    }

    public PdfTextAnnotation setOpen(boolean open) {
        return (PdfTextAnnotation) put(PdfName.Open, PdfBoolean.valueOf(open));
    }

    public PdfName getIconName() {
        return getPdfObject().getAsName(PdfName.Name);
    }

    public PdfTextAnnotation setIconName(PdfName name) {
        return (PdfTextAnnotation) put(PdfName.Name, name);
    }
}
