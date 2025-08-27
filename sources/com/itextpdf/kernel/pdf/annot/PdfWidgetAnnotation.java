package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.action.PdfAction;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfWidgetAnnotation.class */
public class PdfWidgetAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 9013938639824707088L;
    public static final int HIDDEN = 1;
    public static final int VISIBLE_BUT_DOES_NOT_PRINT = 2;
    public static final int HIDDEN_BUT_PRINTABLE = 3;
    public static final int VISIBLE = 4;
    private HashSet<PdfName> widgetEntries;

    public PdfWidgetAnnotation(Rectangle rect) {
        super(rect);
        this.widgetEntries = new HashSet<>();
        this.widgetEntries.add(PdfName.Subtype);
        this.widgetEntries.add(PdfName.Type);
        this.widgetEntries.add(PdfName.Rect);
        this.widgetEntries.add(PdfName.Contents);
        this.widgetEntries.add(PdfName.P);
        this.widgetEntries.add(PdfName.NM);
        this.widgetEntries.add(PdfName.M);
        this.widgetEntries.add(PdfName.F);
        this.widgetEntries.add(PdfName.AP);
        this.widgetEntries.add(PdfName.AS);
        this.widgetEntries.add(PdfName.Border);
        this.widgetEntries.add(PdfName.C);
        this.widgetEntries.add(PdfName.StructParent);
        this.widgetEntries.add(PdfName.OC);
        this.widgetEntries.add(PdfName.H);
        this.widgetEntries.add(PdfName.MK);
        this.widgetEntries.add(PdfName.A);
        this.widgetEntries.add(PdfName.AA);
        this.widgetEntries.add(PdfName.BS);
    }

    protected PdfWidgetAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
        this.widgetEntries = new HashSet<>();
        this.widgetEntries.add(PdfName.Subtype);
        this.widgetEntries.add(PdfName.Type);
        this.widgetEntries.add(PdfName.Rect);
        this.widgetEntries.add(PdfName.Contents);
        this.widgetEntries.add(PdfName.P);
        this.widgetEntries.add(PdfName.NM);
        this.widgetEntries.add(PdfName.M);
        this.widgetEntries.add(PdfName.F);
        this.widgetEntries.add(PdfName.AP);
        this.widgetEntries.add(PdfName.AS);
        this.widgetEntries.add(PdfName.Border);
        this.widgetEntries.add(PdfName.C);
        this.widgetEntries.add(PdfName.StructParent);
        this.widgetEntries.add(PdfName.OC);
        this.widgetEntries.add(PdfName.H);
        this.widgetEntries.add(PdfName.MK);
        this.widgetEntries.add(PdfName.A);
        this.widgetEntries.add(PdfName.AA);
        this.widgetEntries.add(PdfName.BS);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Widget;
    }

    public PdfWidgetAnnotation setParent(PdfObject parent) {
        return (PdfWidgetAnnotation) put(PdfName.Parent, parent);
    }

    public PdfWidgetAnnotation setHighlightMode(PdfName mode) {
        return (PdfWidgetAnnotation) put(PdfName.H, mode);
    }

    public PdfName getHighlightMode() {
        return getPdfObject().getAsName(PdfName.H);
    }

    public void releaseFormFieldFromWidgetAnnotation() {
        PdfDictionary annotDict = getPdfObject();
        Iterator<PdfName> it = this.widgetEntries.iterator();
        while (it.hasNext()) {
            PdfName entry = it.next();
            annotDict.remove(entry);
        }
        PdfDictionary parent = annotDict.getAsDictionary(PdfName.Parent);
        if (parent != null && annotDict.size() == 1) {
            PdfArray kids = parent.getAsArray(PdfName.Kids);
            kids.remove(annotDict);
            if (kids.size() == 0) {
                parent.remove(PdfName.Kids);
            }
        }
    }

    public PdfWidgetAnnotation setVisibility(int visibility) {
        switch (visibility) {
            case 1:
                getPdfObject().put(PdfName.F, new PdfNumber(6));
                break;
            case 2:
                break;
            case 3:
                getPdfObject().put(PdfName.F, new PdfNumber(36));
                break;
            case 4:
            default:
                getPdfObject().put(PdfName.F, new PdfNumber(4));
                break;
        }
        return this;
    }

    public PdfDictionary getAction() {
        return getPdfObject().getAsDictionary(PdfName.A);
    }

    public PdfWidgetAnnotation setAction(PdfAction action) {
        return (PdfWidgetAnnotation) put(PdfName.A, action.getPdfObject());
    }

    public PdfDictionary getAdditionalAction() {
        return getPdfObject().getAsDictionary(PdfName.AA);
    }

    public PdfWidgetAnnotation setAdditionalAction(PdfName key, PdfAction action) {
        PdfAction.setAdditionalAction(this, key, action);
        return this;
    }

    public PdfDictionary getAppearanceCharacteristics() {
        return getPdfObject().getAsDictionary(PdfName.MK);
    }

    public PdfWidgetAnnotation setAppearanceCharacteristics(PdfDictionary characteristics) {
        return (PdfWidgetAnnotation) put(PdfName.MK, characteristics);
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfWidgetAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfWidgetAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfWidgetAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfWidgetAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }
}
