package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfMarkupAnnotation.class */
public abstract class PdfMarkupAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 239280278775576458L;
    protected PdfAnnotation inReplyTo;
    protected PdfPopupAnnotation popup;

    protected PdfMarkupAnnotation(Rectangle rect) {
        super(rect);
        this.inReplyTo = null;
        this.popup = null;
    }

    protected PdfMarkupAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
        this.inReplyTo = null;
        this.popup = null;
    }

    public PdfString getText() {
        return getPdfObject().getAsString(PdfName.T);
    }

    public PdfMarkupAnnotation setText(PdfString text) {
        return (PdfMarkupAnnotation) put(PdfName.T, text);
    }

    public PdfNumber getOpacity() {
        return getPdfObject().getAsNumber(PdfName.CA);
    }

    public PdfMarkupAnnotation setOpacity(PdfNumber ca) {
        return (PdfMarkupAnnotation) put(PdfName.CA, ca);
    }

    public PdfObject getRichText() {
        return getPdfObject().get(PdfName.RC);
    }

    public PdfMarkupAnnotation setRichText(PdfObject richText) {
        return (PdfMarkupAnnotation) put(PdfName.RC, richText);
    }

    public PdfString getCreationDate() {
        return getPdfObject().getAsString(PdfName.CreationDate);
    }

    public PdfMarkupAnnotation setCreationDate(PdfString creationDate) {
        return (PdfMarkupAnnotation) put(PdfName.CreationDate, creationDate);
    }

    public PdfDictionary getInReplyToObject() {
        return getPdfObject().getAsDictionary(PdfName.IRT);
    }

    public PdfAnnotation getInReplyTo() {
        if (this.inReplyTo == null) {
            this.inReplyTo = makeAnnotation(getInReplyToObject());
        }
        return this.inReplyTo;
    }

    public PdfMarkupAnnotation setInReplyTo(PdfAnnotation inReplyTo) {
        this.inReplyTo = inReplyTo;
        return (PdfMarkupAnnotation) put(PdfName.IRT, inReplyTo.getPdfObject());
    }

    public PdfMarkupAnnotation setPopup(PdfPopupAnnotation popup) {
        this.popup = popup;
        popup.setParent(this);
        return (PdfMarkupAnnotation) put(PdfName.Popup, popup.getPdfObject());
    }

    public PdfDictionary getPopupObject() {
        return getPdfObject().getAsDictionary(PdfName.Popup);
    }

    public PdfPopupAnnotation getPopup() {
        PdfDictionary popupObject;
        if (this.popup == null && (popupObject = getPopupObject()) != null) {
            PdfAnnotation annotation = makeAnnotation(popupObject);
            if (!(annotation instanceof PdfPopupAnnotation)) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfMarkupAnnotation.class);
                logger.warn(LogMessageConstant.POPUP_ENTRY_IS_NOT_POPUP_ANNOTATION);
                return null;
            }
            this.popup = (PdfPopupAnnotation) annotation;
        }
        return this.popup;
    }

    public PdfString getSubject() {
        return getPdfObject().getAsString(PdfName.Subj);
    }

    public PdfMarkupAnnotation setSubject(PdfString subject) {
        return (PdfMarkupAnnotation) put(PdfName.Subj, subject);
    }

    public PdfName getReplyType() {
        return getPdfObject().getAsName(PdfName.RT);
    }

    public PdfMarkupAnnotation setReplyType(PdfName replyType) {
        return (PdfMarkupAnnotation) put(PdfName.RT, replyType);
    }

    public PdfName getIntent() {
        return getPdfObject().getAsName(PdfName.IT);
    }

    public PdfMarkupAnnotation setIntent(PdfName intent) {
        return (PdfMarkupAnnotation) put(PdfName.IT, intent);
    }

    public PdfDictionary getExternalData() {
        return getPdfObject().getAsDictionary(PdfName.ExData);
    }

    public PdfMarkupAnnotation setExternalData(PdfName exData) {
        return (PdfMarkupAnnotation) put(PdfName.ExData, exData);
    }
}
