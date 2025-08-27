package com.itextpdf.kernel.pdf.xobject;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageData;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageHelper;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/xobject/PdfFormXObject.class */
public class PdfFormXObject extends PdfXObject {
    private static final long serialVersionUID = 467500482711722178L;
    protected PdfResources resources;

    public PdfFormXObject(Rectangle bBox) {
        super(new PdfStream());
        this.resources = null;
        getPdfObject().put(PdfName.Type, PdfName.XObject);
        getPdfObject().put(PdfName.Subtype, PdfName.Form);
        if (bBox != null) {
            getPdfObject().put(PdfName.BBox, new PdfArray(bBox));
        }
    }

    public PdfFormXObject(PdfStream pdfStream) {
        super(pdfStream);
        this.resources = null;
        if (!getPdfObject().containsKey(PdfName.Subtype)) {
            getPdfObject().put(PdfName.Subtype, PdfName.Form);
        }
    }

    public PdfFormXObject(PdfPage page) {
        this(page.getCropBox());
        getPdfObject().getOutputStream().writeBytes(page.getContentBytes());
        this.resources = new PdfResources((PdfDictionary) page.getResources().getPdfObject().m850clone());
        getPdfObject().put(PdfName.Resources, this.resources.getPdfObject());
    }

    public PdfFormXObject(WmfImageData image, PdfDocument pdfDocument) {
        this(new WmfImageHelper(image).createFormXObject(pdfDocument).getPdfObject());
    }

    public PdfResources getResources() {
        if (this.resources == null) {
            PdfDictionary resourcesDict = getPdfObject().getAsDictionary(PdfName.Resources);
            if (resourcesDict == null) {
                resourcesDict = new PdfDictionary();
                getPdfObject().put(PdfName.Resources, resourcesDict);
            }
            this.resources = new PdfResources(resourcesDict);
        }
        return this.resources;
    }

    public PdfArray getBBox() {
        return getPdfObject().getAsArray(PdfName.BBox);
    }

    public PdfFormXObject setBBox(PdfArray bBox) {
        return put(PdfName.BBox, bBox);
    }

    public PdfFormXObject setGroup(PdfTransparencyGroup transparency) {
        return put(PdfName.Group, transparency.getPdfObject());
    }

    @Override // com.itextpdf.kernel.pdf.xobject.PdfXObject
    public float getWidth() {
        if (getBBox() == null) {
            return 0.0f;
        }
        return getBBox().getAsNumber(2).floatValue() - getBBox().getAsNumber(0).floatValue();
    }

    @Override // com.itextpdf.kernel.pdf.xobject.PdfXObject
    public float getHeight() {
        if (getBBox() == null) {
            return 0.0f;
        }
        return getBBox().getAsNumber(3).floatValue() - getBBox().getAsNumber(1).floatValue();
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        this.resources = null;
        if (getPdfObject().get(PdfName.BBox) == null) {
            throw new PdfException(PdfException.FormXObjectMustHaveBbox);
        }
        super.flush();
    }

    public PdfFormXObject setProcessColorModel(PdfName model) {
        return put(PdfName.PCM, model);
    }

    public PdfName getProcessColorModel() {
        return getPdfObject().getAsName(PdfName.PCM);
    }

    public PdfFormXObject setSeparationColorNames(PdfArray colorNames) {
        return put(PdfName.SeparationColorNames, colorNames);
    }

    public PdfArray getSeparationColorNames() {
        return getPdfObject().getAsArray(PdfName.SeparationColorNames);
    }

    public PdfFormXObject setTrapRegions(PdfArray regions) {
        return put(PdfName.TrapRegions, regions);
    }

    public PdfArray getTrapRegions() {
        return getPdfObject().getAsArray(PdfName.TrapRegions);
    }

    public PdfFormXObject setTrapStyles(PdfString trapStyles) {
        return put(PdfName.TrapStyles, trapStyles);
    }

    public PdfString getTrapStyles() {
        return getPdfObject().getAsString(PdfName.TrapStyles);
    }

    public PdfFormXObject setMarkStyle(PdfString markStyle) {
        return put(PdfName.MarkStyle, markStyle);
    }

    public PdfString getMarkStyle() {
        return getPdfObject().getAsString(PdfName.MarkStyle);
    }

    public PdfFormXObject put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        setModified();
        return this;
    }
}
