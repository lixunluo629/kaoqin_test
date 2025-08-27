package com.itextpdf.kernel.pdf.tagutils;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagutils/TagReference.class */
public class TagReference {
    protected TagTreePointer tagPointer;
    protected int insertIndex;
    protected PdfStructElem referencedTag;
    protected PdfName role;
    protected PdfDictionary properties;

    protected TagReference(PdfStructElem referencedTag, TagTreePointer tagPointer, int insertIndex) {
        this.role = referencedTag.getRole();
        this.referencedTag = referencedTag;
        this.tagPointer = tagPointer;
        this.insertIndex = insertIndex;
    }

    public PdfName getRole() {
        return this.role;
    }

    public int createNextMcid() {
        return this.tagPointer.createNextMcidForStructElem(this.referencedTag, this.insertIndex);
    }

    public TagReference addProperty(PdfName name, PdfObject value) {
        if (this.properties == null) {
            this.properties = new PdfDictionary();
        }
        this.properties.put(name, value);
        return this;
    }

    public TagReference removeProperty(PdfName name) {
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
}
