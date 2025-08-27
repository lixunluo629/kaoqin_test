package com.itextpdf.kernel.pdf;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.PdfException;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfObjectStream.class */
class PdfObjectStream extends PdfStream {
    private static final long serialVersionUID = -3513488307665597642L;
    public static final int MAX_OBJ_STREAM_SIZE = 200;
    protected PdfNumber size;
    protected PdfOutputStream indexStream;

    public PdfObjectStream(PdfDocument doc) {
        this(doc, new ByteArrayOutputStream());
        this.indexStream = new PdfOutputStream(new ByteArrayOutputStream());
    }

    PdfObjectStream(PdfObjectStream prev) {
        this(prev.getIndirectReference().getDocument(), prev.getOutputStream().getOutputStream());
        this.indexStream = new PdfOutputStream(prev.indexStream.getOutputStream());
        ((ByteArrayOutputStream) this.outputStream.getOutputStream()).reset();
        ((ByteArrayOutputStream) this.indexStream.getOutputStream()).reset();
        prev.releaseContent(true);
    }

    private PdfObjectStream(PdfDocument doc, OutputStream outputStream) {
        super(outputStream);
        this.size = new PdfNumber(0);
        makeIndirect(doc, doc.getXref().createNewIndirectReference(doc));
        getOutputStream().document = doc;
        put(PdfName.Type, PdfName.ObjStm);
        put(PdfName.N, this.size);
        put(PdfName.First, new PdfNumber(0));
    }

    public void addObject(PdfObject object) throws IOException {
        if (this.size.intValue() == 200) {
            throw new PdfException(PdfException.PdfObjectStreamReachMaxSize);
        }
        PdfOutputStream outputStream = getOutputStream();
        this.indexStream.writeInteger(object.getIndirectReference().getObjNumber()).writeSpace().writeLong(outputStream.getCurrentPos()).writeSpace();
        outputStream.write(object);
        object.getIndirectReference().setObjStreamNumber(getIndirectReference().getObjNumber());
        object.getIndirectReference().setIndex(this.size.intValue());
        outputStream.writeSpace();
        this.size.increment();
        getAsNumber(PdfName.First).setValue(this.indexStream.getCurrentPos());
    }

    public int getSize() {
        return this.size.intValue();
    }

    public PdfOutputStream getIndexStream() {
        return this.indexStream;
    }

    @Override // com.itextpdf.kernel.pdf.PdfStream, com.itextpdf.kernel.pdf.PdfDictionary
    protected void releaseContent() {
        releaseContent(false);
    }

    private void releaseContent(boolean close) {
        if (close) {
            this.outputStream = null;
            this.indexStream = null;
            super.releaseContent();
        }
    }
}
