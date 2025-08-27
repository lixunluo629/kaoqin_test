package com.itextpdf.kernel.pdf;

import com.itextpdf.io.util.MessageFormatUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfIndirectReference.class */
public class PdfIndirectReference extends PdfObject implements Comparable<PdfIndirectReference> {
    private static final long serialVersionUID = -8293603068792908601L;
    private static final int LENGTH_OF_INDIRECTS_CHAIN = 31;
    protected final int objNr;
    protected int genNr;
    protected PdfObject refersTo;
    protected int objectStreamNumber;
    protected long offsetOrIndex;
    protected PdfDocument pdfDocument;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfIndirectReference.class.desiredAssertionStatus();
    }

    protected PdfIndirectReference(PdfDocument doc, int objNr) {
        this(doc, objNr, 0);
    }

    protected PdfIndirectReference(PdfDocument doc, int objNr, int genNr) {
        this.refersTo = null;
        this.objectStreamNumber = 0;
        this.offsetOrIndex = 0L;
        this.pdfDocument = null;
        this.pdfDocument = doc;
        this.objNr = objNr;
        this.genNr = genNr;
    }

    protected PdfIndirectReference(PdfDocument doc, int objNr, int genNr, long offset) {
        this.refersTo = null;
        this.objectStreamNumber = 0;
        this.offsetOrIndex = 0L;
        this.pdfDocument = null;
        this.pdfDocument = doc;
        this.objNr = objNr;
        this.genNr = genNr;
        this.offsetOrIndex = offset;
        if (!$assertionsDisabled && offset < 0) {
            throw new AssertionError();
        }
    }

    public int getObjNumber() {
        return this.objNr;
    }

    public int getGenNumber() {
        return this.genNr;
    }

    public PdfObject getRefersTo() {
        return getRefersTo(true);
    }

    public PdfObject getRefersTo(boolean recursively) {
        if (!recursively) {
            if (this.refersTo == null && !checkState((short) 1) && !checkState((short) 8) && !checkState((short) 2) && getReader() != null) {
                this.refersTo = getReader().readObject(this);
            }
            return this.refersTo;
        }
        PdfObject currentRefersTo = getRefersTo(false);
        for (int i = 0; i < 31 && (currentRefersTo instanceof PdfIndirectReference); i++) {
            currentRefersTo = ((PdfIndirectReference) currentRefersTo).getRefersTo(false);
        }
        return currentRefersTo;
    }

    protected void setRefersTo(PdfObject refersTo) {
        this.refersTo = refersTo;
    }

    public int getObjStreamNumber() {
        return this.objectStreamNumber;
    }

    public long getOffset() {
        if (this.objectStreamNumber == 0) {
            return this.offsetOrIndex;
        }
        return -1L;
    }

    public int getIndex() {
        if (this.objectStreamNumber == 0) {
            return -1;
        }
        return (int) this.offsetOrIndex;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PdfIndirectReference that = (PdfIndirectReference) o;
        return this.objNr == that.objNr && this.genNr == that.genNr;
    }

    public int hashCode() {
        int result = this.objNr;
        return (31 * result) + this.genNr;
    }

    @Override // java.lang.Comparable
    public int compareTo(PdfIndirectReference o) {
        if (this.objNr != o.objNr) {
            return this.objNr > o.objNr ? 1 : -1;
        }
        if (this.genNr == o.genNr) {
            return 0;
        }
        return this.genNr > o.genNr ? 1 : -1;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 5;
    }

    public PdfDocument getDocument() {
        return this.pdfDocument;
    }

    public void setFree() {
        getDocument().getXref().freeReference(this);
    }

    public boolean isFree() {
        return checkState((short) 2);
    }

    public String toString() {
        StringBuilder states = new StringBuilder(SymbolConstants.SPACE_SYMBOL);
        if (checkState((short) 2)) {
            states.append("Free; ");
        }
        if (checkState((short) 8)) {
            states.append("Modified; ");
        }
        if (checkState((short) 32)) {
            states.append("MustBeFlushed; ");
        }
        if (checkState((short) 4)) {
            states.append("Reading; ");
        }
        if (checkState((short) 1)) {
            states.append("Flushed; ");
        }
        if (checkState((short) 16)) {
            states.append("OriginalObjectStream; ");
        }
        if (checkState((short) 128)) {
            states.append("ForbidRelease; ");
        }
        if (checkState((short) 256)) {
            states.append("ReadOnly; ");
        }
        return MessageFormatUtil.format("{0} {1} R{2}", Integer.toString(getObjNumber()), Integer.toString(getGenNumber()), states.substring(0, states.length() - 1));
    }

    protected PdfWriter getWriter() {
        if (getDocument() != null) {
            return getDocument().getWriter();
        }
        return null;
    }

    protected PdfReader getReader() {
        if (getDocument() != null) {
            return getDocument().getReader();
        }
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return PdfNull.PDF_NULL;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
    }

    @Override // com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject setState(short state) {
        return super.setState(state);
    }

    void setObjStreamNumber(int objectStreamNumber) {
        this.objectStreamNumber = objectStreamNumber;
    }

    void setIndex(long index) {
        this.offsetOrIndex = index;
    }

    void setOffset(long offset) {
        this.offsetOrIndex = offset;
        this.objectStreamNumber = 0;
    }

    void fixOffset(long offset) {
        if (!isFree()) {
            this.offsetOrIndex = offset;
        }
    }
}
