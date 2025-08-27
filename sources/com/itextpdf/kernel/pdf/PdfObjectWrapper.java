package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfObject;
import java.io.Serializable;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfObjectWrapper.class */
public abstract class PdfObjectWrapper<T extends PdfObject> implements Serializable {
    private static final long serialVersionUID = 3516473712028588356L;
    private T pdfObject;

    protected abstract boolean isWrappedObjectMustBeIndirect();

    protected PdfObjectWrapper(T pdfObject) {
        this.pdfObject = null;
        this.pdfObject = pdfObject;
        if (isWrappedObjectMustBeIndirect()) {
            markObjectAsIndirect(this.pdfObject);
        }
    }

    public T getPdfObject() {
        return this.pdfObject;
    }

    public PdfObjectWrapper<T> makeIndirect(PdfDocument document, PdfIndirectReference reference) {
        getPdfObject().makeIndirect(document, reference);
        return this;
    }

    public PdfObjectWrapper<T> makeIndirect(PdfDocument document) {
        return makeIndirect(document, null);
    }

    public PdfObjectWrapper<T> setModified() {
        this.pdfObject.setModified();
        return this;
    }

    public void flush() {
        this.pdfObject.flush();
    }

    public boolean isFlushed() {
        return this.pdfObject.isFlushed();
    }

    protected void setPdfObject(T pdfObject) {
        this.pdfObject = pdfObject;
    }

    protected void setForbidRelease() {
        if (this.pdfObject != null) {
            this.pdfObject.setState((short) 128);
        }
    }

    protected void unsetForbidRelease() {
        if (this.pdfObject != null) {
            this.pdfObject.clearState((short) 128);
        }
    }

    protected void ensureUnderlyingObjectHasIndirectReference() {
        if (getPdfObject().getIndirectReference() == null) {
            throw new PdfException(PdfException.ToFlushThisWrapperUnderlyingObjectMustBeAddedToDocument);
        }
    }

    protected static void markObjectAsIndirect(PdfObject pdfObject) {
        if (pdfObject.getIndirectReference() == null) {
            pdfObject.setState((short) 64);
        }
    }

    protected static void ensureObjectIsAddedToDocument(PdfObject object) {
        if (object.getIndirectReference() == null) {
            throw new PdfException(PdfException.ObjectMustBeIndirectToWorkWithThisWrapper);
        }
    }
}
