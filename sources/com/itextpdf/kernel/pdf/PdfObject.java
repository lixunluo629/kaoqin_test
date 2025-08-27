package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.BadPasswordException;
import java.io.IOException;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfObject.class */
public abstract class PdfObject implements Serializable {
    private static final long serialVersionUID = -3852543867469424720L;
    public static final byte ARRAY = 1;
    public static final byte BOOLEAN = 2;
    public static final byte DICTIONARY = 3;
    public static final byte LITERAL = 4;
    public static final byte INDIRECT_REFERENCE = 5;
    public static final byte NAME = 6;
    public static final byte NULL = 7;
    public static final byte NUMBER = 8;
    public static final byte STREAM = 9;
    public static final byte STRING = 10;
    protected static final short FLUSHED = 1;
    protected static final short FREE = 2;
    protected static final short READING = 4;
    protected static final short MODIFIED = 8;
    protected static final short ORIGINAL_OBJECT_STREAM = 16;
    protected static final short MUST_BE_FLUSHED = 32;
    protected static final short MUST_BE_INDIRECT = 64;
    protected static final short FORBID_RELEASE = 128;
    protected static final short READ_ONLY = 256;
    protected static final short UNENCRYPTED = 512;
    protected PdfIndirectReference indirectReference = null;
    private short state;

    public abstract byte getType();

    protected abstract PdfObject newInstance();

    public final void flush() {
        flush(true);
    }

    public final void flush(boolean canBeInObjStm) {
        if (isFlushed() || getIndirectReference() == null || getIndirectReference().isFree()) {
            return;
        }
        try {
            PdfDocument document = getIndirectReference().getDocument();
            if (document != null) {
                if (document.isAppendMode() && !isModified()) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) PdfObject.class);
                    logger.info(LogMessageConstant.PDF_OBJECT_FLUSHING_NOT_PERFORMED);
                } else {
                    document.checkIsoConformance(this, IsoKey.PDF_OBJECT);
                    document.flushObject(this, canBeInObjStm && getType() != 9 && getType() != 5 && getIndirectReference().getGenNumber() == 0);
                }
            }
        } catch (IOException e) {
            throw new PdfException(PdfException.CannotFlushObject, e, this);
        }
    }

    public PdfIndirectReference getIndirectReference() {
        return this.indirectReference;
    }

    public boolean isIndirect() {
        return this.indirectReference != null || checkState((short) 64);
    }

    public PdfObject makeIndirect(PdfDocument document, PdfIndirectReference reference) {
        if (document == null || this.indirectReference != null) {
            return this;
        }
        if (document.getWriter() == null) {
            throw new PdfException(PdfException.ThereIsNoAssociatePdfWriterForMakingIndirects);
        }
        if (reference == null) {
            this.indirectReference = document.createNextIndirectReference();
            this.indirectReference.setRefersTo(this);
        } else {
            reference.setState((short) 8);
            this.indirectReference = reference;
            this.indirectReference.setRefersTo(this);
        }
        setState((short) 128);
        clearState((short) 64);
        return this;
    }

    public PdfObject makeIndirect(PdfDocument document) {
        return makeIndirect(document, null);
    }

    public boolean isFlushed() {
        PdfIndirectReference indirectReference = getIndirectReference();
        return indirectReference != null && indirectReference.checkState((short) 1);
    }

    public boolean isModified() {
        PdfIndirectReference indirectReference = getIndirectReference();
        return indirectReference != null && indirectReference.checkState((short) 8);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public PdfObject m850clone() {
        PdfObject newObject = newInstance();
        if (this.indirectReference != null || checkState((short) 64)) {
            newObject.setState((short) 64);
        }
        newObject.copyContent(this, null);
        return newObject;
    }

    public PdfObject copyTo(PdfDocument document) {
        return copyTo(document, true);
    }

    public PdfObject copyTo(PdfDocument document, boolean allowDuplicating) {
        if (document == null) {
            throw new PdfException(PdfException.DocumentForCopyToCannotBeNull);
        }
        if (this.indirectReference != null) {
            if (this.indirectReference.getWriter() != null || checkState((short) 64)) {
                throw new PdfException(PdfException.CannotCopyIndirectObjectFromTheDocumentThatIsBeingWritten);
            }
            if (!this.indirectReference.getReader().isOpenedWithFullPermission()) {
                throw new BadPasswordException(BadPasswordException.PdfReaderNotOpenedWithOwnerPassword);
            }
        }
        return processCopying(document, allowDuplicating);
    }

    public PdfObject setModified() {
        if (this.indirectReference != null) {
            this.indirectReference.setState((short) 8);
            setState((short) 128);
        }
        return this;
    }

    public boolean isReleaseForbidden() {
        return checkState((short) 128);
    }

    public void release() {
        if (isReleaseForbidden()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfObject.class);
            logger.warn(LogMessageConstant.FORBID_RELEASE_IS_SET);
        } else if (this.indirectReference != null && this.indirectReference.getReader() != null && !this.indirectReference.checkState((short) 1)) {
            this.indirectReference.refersTo = null;
            this.indirectReference = null;
            setState((short) 256);
        }
    }

    public boolean isNull() {
        return getType() == 7;
    }

    public boolean isBoolean() {
        return getType() == 2;
    }

    public boolean isNumber() {
        return getType() == 8;
    }

    public boolean isString() {
        return getType() == 10;
    }

    public boolean isName() {
        return getType() == 6;
    }

    public boolean isArray() {
        return getType() == 1;
    }

    public boolean isDictionary() {
        return getType() == 3;
    }

    public boolean isStream() {
        return getType() == 9;
    }

    public boolean isIndirectReference() {
        return getType() == 5;
    }

    public boolean isLiteral() {
        return getType() == 4;
    }

    protected PdfObject setIndirectReference(PdfIndirectReference indirectReference) {
        this.indirectReference = indirectReference;
        return this;
    }

    protected boolean checkState(short state) {
        return (this.state & state) == state;
    }

    protected PdfObject setState(short state) {
        this.state = (short) (this.state | state);
        return this;
    }

    protected PdfObject clearState(short state) {
        this.state = (short) (this.state & ((short) (state ^ (-1))));
        return this;
    }

    protected void copyContent(PdfObject from, PdfDocument document) {
        if (isFlushed()) {
            throw new PdfException(PdfException.CannotCopyFlushedObject, this);
        }
    }

    PdfObject processCopying(PdfDocument documentTo, boolean allowDuplicating) {
        if (documentTo != null) {
            PdfWriter writer = documentTo.getWriter();
            if (writer == null) {
                throw new PdfException(PdfException.CannotCopyToDocumentOpenedInReadingMode);
            }
            return writer.copyObject(this, documentTo, allowDuplicating);
        }
        PdfObject obj = this;
        if (obj.isIndirectReference()) {
            PdfObject refTo = ((PdfIndirectReference) obj).getRefersTo();
            obj = refTo != null ? refTo : obj;
        }
        if (obj.isIndirect() && !allowDuplicating) {
            return obj;
        }
        return obj.m850clone();
    }

    static boolean equalContent(PdfObject obj1, PdfObject obj2) {
        PdfObject direct1 = (obj1 == null || !obj1.isIndirectReference()) ? obj1 : ((PdfIndirectReference) obj1).getRefersTo(true);
        PdfObject direct2 = (obj2 == null || !obj2.isIndirectReference()) ? obj2 : ((PdfIndirectReference) obj2).getRefersTo(true);
        return direct1 != null && direct1.equals(direct2);
    }
}
