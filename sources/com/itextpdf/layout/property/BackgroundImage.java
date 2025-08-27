package com.itextpdf.layout.property;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/property/BackgroundImage.class */
public class BackgroundImage {
    protected PdfXObject image;
    protected boolean repeatX;
    protected boolean repeatY;

    private BackgroundImage(PdfXObject image, boolean repeatX, boolean repeatY) {
        this.image = image;
        this.repeatX = repeatX;
        this.repeatY = repeatY;
    }

    public BackgroundImage(PdfImageXObject image) {
        this(image, true, true);
    }

    public BackgroundImage(PdfFormXObject image) {
        this(image, true, true);
    }

    public BackgroundImage(PdfImageXObject image, boolean repeatX, boolean repeatY) {
        this((PdfXObject) image, repeatX, repeatY);
    }

    public BackgroundImage(PdfFormXObject image, boolean repeatX, boolean repeatY) {
        this((PdfXObject) image, repeatX, repeatY);
    }

    public PdfImageXObject getImage() {
        if (this.image instanceof PdfImageXObject) {
            return (PdfImageXObject) this.image;
        }
        return null;
    }

    public PdfFormXObject getForm() {
        if (this.image instanceof PdfFormXObject) {
            return (PdfFormXObject) this.image;
        }
        return null;
    }

    public boolean isBackgroundSpecified() {
        return (this.image instanceof PdfFormXObject) || (this.image instanceof PdfImageXObject);
    }

    public boolean isRepeatX() {
        return this.repeatX;
    }

    public boolean isRepeatY() {
        return this.repeatY;
    }
}
