package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfPrimitiveObject.class */
public abstract class PdfPrimitiveObject extends PdfObject {
    private static final long serialVersionUID = -1788064882121987538L;
    protected byte[] content;
    protected boolean directOnly;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void generateContent();

    static {
        $assertionsDisabled = !PdfPrimitiveObject.class.desiredAssertionStatus();
    }

    protected PdfPrimitiveObject() {
        this.content = null;
    }

    protected PdfPrimitiveObject(boolean directOnly) {
        this.content = null;
        this.directOnly = directOnly;
    }

    protected PdfPrimitiveObject(byte[] content) {
        this();
        if (!$assertionsDisabled && content == null) {
            throw new AssertionError();
        }
        this.content = content;
    }

    protected final byte[] getInternalContent() {
        if (this.content == null) {
            generateContent();
        }
        return this.content;
    }

    protected boolean hasContent() {
        return this.content != null;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public PdfObject makeIndirect(PdfDocument document, PdfIndirectReference reference) {
        if (!this.directOnly) {
            return super.makeIndirect(document, reference);
        }
        Logger logger = LoggerFactory.getLogger((Class<?>) PdfObject.class);
        logger.warn(LogMessageConstant.DIRECTONLY_OBJECT_CANNOT_BE_INDIRECT);
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public PdfObject setIndirectReference(PdfIndirectReference indirectReference) {
        if (!this.directOnly) {
            super.setIndirectReference(indirectReference);
        } else {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfObject.class);
            logger.warn(LogMessageConstant.DIRECTONLY_OBJECT_CANNOT_BE_INDIRECT);
        }
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfPrimitiveObject object = (PdfPrimitiveObject) from;
        if (object.content != null) {
            this.content = Arrays.copyOf(object.content, object.content.length);
        }
    }

    protected int compareContent(PdfPrimitiveObject o) {
        for (int i = 0; i < Math.min(this.content.length, o.content.length); i++) {
            if (this.content[i] > o.content[i]) {
                return 1;
            }
            if (this.content[i] < o.content[i]) {
                return -1;
            }
        }
        return Integer.compare(this.content.length, o.content.length);
    }
}
