package com.itextpdf.kernel.pdf.canvas;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.tagging.PdfMcr;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/canvas/CanvasTag.class */
public class CanvasTag {
    protected PdfName role;
    protected PdfDictionary properties;

    public CanvasTag(PdfName role) {
        this.role = role;
    }

    public CanvasTag(PdfName role, int mcid) {
        this.role = role;
        addProperty(PdfName.MCID, new PdfNumber(mcid));
    }

    public CanvasTag(PdfMcr mcr) {
        this(mcr.getRole(), mcr.getMcid());
    }

    public PdfName getRole() {
        return this.role;
    }

    public int getMcid() {
        int mcid = -1;
        if (this.properties != null) {
            mcid = this.properties.getAsInt(PdfName.MCID).intValue();
        }
        if (mcid == -1) {
            throw new IllegalStateException("CanvasTag has no MCID");
        }
        return mcid;
    }

    public boolean hasMcid() {
        return this.properties != null && this.properties.containsKey(PdfName.MCID);
    }

    public CanvasTag setProperties(PdfDictionary properties) {
        this.properties = properties;
        return this;
    }

    public CanvasTag addProperty(PdfName name, PdfObject value) {
        ensurePropertiesInit();
        this.properties.put(name, value);
        return this;
    }

    public CanvasTag removeProperty(PdfName name) {
        if (this.properties != null) {
            this.properties.remove(name);
        }
        return this;
    }

    public PdfObject getProperty(PdfName name) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(name);
    }

    public PdfDictionary getProperties() {
        return this.properties;
    }

    public String getActualText() {
        return getPropertyAsString(PdfName.ActualText);
    }

    public String getExpansionText() {
        return getPropertyAsString(PdfName.E);
    }

    private String getPropertyAsString(PdfName name) {
        PdfString text = this.properties.getAsString(name);
        String result = null;
        if (text != null) {
            result = text.toUnicodeString();
        }
        return result;
    }

    private void ensurePropertiesInit() {
        if (this.properties == null) {
            this.properties = new PdfDictionary();
        }
    }
}
