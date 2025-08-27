package com.itextpdf.kernel.pdf.annot;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/annot/PdfLinkAnnotation.class */
public class PdfLinkAnnotation extends PdfAnnotation {
    private static final long serialVersionUID = 5795613340575331536L;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) PdfLinkAnnotation.class);
    public static final PdfName None = PdfName.N;
    public static final PdfName Invert = PdfName.I;
    public static final PdfName Outline = PdfName.O;
    public static final PdfName Push = PdfName.P;

    protected PdfLinkAnnotation(PdfDictionary pdfObject) {
        super(pdfObject);
    }

    public PdfLinkAnnotation(Rectangle rect) {
        super(rect);
    }

    @Override // com.itextpdf.kernel.pdf.annot.PdfAnnotation
    public PdfName getSubtype() {
        return PdfName.Link;
    }

    public PdfObject getDestinationObject() {
        return getPdfObject().get(PdfName.Dest);
    }

    public PdfLinkAnnotation setDestination(PdfObject destination) {
        if (getPdfObject().containsKey(PdfName.A)) {
            getPdfObject().remove(PdfName.A);
            logger.warn(LogMessageConstant.DESTINATION_NOT_PERMITTED_WHEN_ACTION_IS_SET);
        }
        if (destination.isArray() && ((PdfArray) destination).get(0).isNumber()) {
            LoggerFactory.getLogger((Class<?>) PdfLinkAnnotation.class).warn(LogMessageConstant.INVALID_DESTINATION_TYPE);
        }
        return (PdfLinkAnnotation) put(PdfName.Dest, destination);
    }

    public PdfLinkAnnotation setDestination(PdfDestination destination) {
        return setDestination(destination.getPdfObject());
    }

    public PdfLinkAnnotation removeDestination() {
        getPdfObject().remove(PdfName.Dest);
        return this;
    }

    public PdfDictionary getAction() {
        return getPdfObject().getAsDictionary(PdfName.A);
    }

    public PdfLinkAnnotation setAction(PdfDictionary action) {
        return (PdfLinkAnnotation) put(PdfName.A, action);
    }

    public PdfLinkAnnotation setAction(PdfAction action) {
        if (getDestinationObject() != null) {
            removeDestination();
            logger.warn(LogMessageConstant.ACTION_WAS_SET_TO_LINK_ANNOTATION_WITH_DESTINATION);
        }
        return (PdfLinkAnnotation) put(PdfName.A, action.getPdfObject());
    }

    public PdfLinkAnnotation removeAction() {
        getPdfObject().remove(PdfName.A);
        return this;
    }

    public PdfName getHighlightMode() {
        return getPdfObject().getAsName(PdfName.H);
    }

    public PdfLinkAnnotation setHighlightMode(PdfName hlMode) {
        return (PdfLinkAnnotation) put(PdfName.H, hlMode);
    }

    public PdfDictionary getUriActionObject() {
        return getPdfObject().getAsDictionary(PdfName.PA);
    }

    public PdfLinkAnnotation setUriAction(PdfDictionary action) {
        return (PdfLinkAnnotation) put(PdfName.PA, action);
    }

    public PdfLinkAnnotation setUriAction(PdfAction action) {
        return (PdfLinkAnnotation) put(PdfName.PA, action.getPdfObject());
    }

    public PdfArray getQuadPoints() {
        return getPdfObject().getAsArray(PdfName.QuadPoints);
    }

    public PdfLinkAnnotation setQuadPoints(PdfArray quadPoints) {
        return (PdfLinkAnnotation) put(PdfName.QuadPoints, quadPoints);
    }

    public PdfDictionary getBorderStyle() {
        return getPdfObject().getAsDictionary(PdfName.BS);
    }

    public PdfLinkAnnotation setBorderStyle(PdfDictionary borderStyle) {
        return (PdfLinkAnnotation) put(PdfName.BS, borderStyle);
    }

    public PdfLinkAnnotation setBorderStyle(PdfName style) {
        return setBorderStyle(BorderStyleUtil.setStyle(getBorderStyle(), style));
    }

    public PdfLinkAnnotation setDashPattern(PdfArray dashPattern) {
        return setBorderStyle(BorderStyleUtil.setDashPattern(getBorderStyle(), dashPattern));
    }
}
