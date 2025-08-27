package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.PdfException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfStream.class */
public class PdfStream extends PdfDictionary {
    private static final long serialVersionUID = -8259929152054328141L;
    protected int compressionLevel;
    protected PdfOutputStream outputStream;
    private InputStream inputStream;
    private long offset;
    private int length;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfStream.class.desiredAssertionStatus();
    }

    public PdfStream(byte[] bytes, int compressionLevel) {
        this.length = -1;
        setState((short) 64);
        this.compressionLevel = compressionLevel;
        if (bytes != null && bytes.length > 0) {
            this.outputStream = new PdfOutputStream(new ByteArrayOutputStream(bytes.length));
            this.outputStream.writeBytes(bytes);
        } else {
            this.outputStream = new PdfOutputStream(new ByteArrayOutputStream());
        }
    }

    public PdfStream(byte[] bytes) {
        this(bytes, Integer.MIN_VALUE);
    }

    public PdfStream(PdfDocument doc, InputStream inputStream, int compressionLevel) {
        this.length = -1;
        if (doc == null) {
            throw new PdfException(PdfException.CannotCreatePdfStreamByInputStreamWithoutPdfDocument);
        }
        makeIndirect(doc);
        if (inputStream == null) {
            throw new IllegalArgumentException("The input stream in PdfStream constructor can not be null.");
        }
        this.inputStream = inputStream;
        this.compressionLevel = compressionLevel;
        put(PdfName.Length, new PdfNumber(-1).makeIndirect(doc));
    }

    public PdfStream(PdfDocument doc, InputStream inputStream) {
        this(doc, inputStream, Integer.MIN_VALUE);
    }

    public PdfStream(int compressionLevel) {
        this((byte[]) null, compressionLevel);
    }

    public PdfStream() {
        this((byte[]) null);
    }

    protected PdfStream(OutputStream outputStream) {
        this.length = -1;
        this.outputStream = new PdfOutputStream(outputStream);
        this.compressionLevel = Integer.MIN_VALUE;
        setState((short) 64);
    }

    PdfStream(long offset, PdfDictionary keys) {
        this.length = -1;
        this.compressionLevel = Integer.MIN_VALUE;
        this.offset = offset;
        putAll(keys);
        PdfNumber length = getAsNumber(PdfName.Length);
        if (length == null) {
            this.length = 0;
        } else {
            this.length = length.intValue();
        }
    }

    public PdfOutputStream getOutputStream() {
        return this.outputStream;
    }

    public int getCompressionLevel() {
        return this.compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    @Override // com.itextpdf.kernel.pdf.PdfDictionary, com.itextpdf.kernel.pdf.PdfObject
    public byte getType() {
        return (byte) 9;
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getBytes() {
        return getBytes(true);
    }

    public byte[] getBytes(boolean decoded) {
        PdfReader reader;
        if (isFlushed()) {
            throw new PdfException(PdfException.CannotOperateWithFlushedPdfStream);
        }
        if (this.inputStream != null) {
            LoggerFactory.getLogger((Class<?>) PdfStream.class).warn("PdfStream was created by InputStream.getBytes() always returns null in this case");
            return null;
        }
        byte[] bytes = null;
        if (this.outputStream != null && this.outputStream.getOutputStream() != null) {
            if (!$assertionsDisabled && !(this.outputStream.getOutputStream() instanceof ByteArrayOutputStream)) {
                throw new AssertionError("Invalid OutputStream: ByteArrayByteArrayOutputStream expected");
            }
            try {
                this.outputStream.getOutputStream().flush();
                bytes = ((ByteArrayOutputStream) this.outputStream.getOutputStream()).toByteArray();
                if (decoded && containsKey(PdfName.Filter)) {
                    bytes = PdfReader.decodeBytes(bytes, this);
                }
            } catch (IOException ioe) {
                throw new PdfException(PdfException.CannotGetPdfStreamBytes, ioe, this);
            }
        } else if (getIndirectReference() != null && (reader = getIndirectReference().getReader()) != null) {
            try {
                bytes = reader.readStreamBytes(this, decoded);
            } catch (IOException ioe2) {
                throw new PdfException(PdfException.CannotGetPdfStreamBytes, ioe2, this);
            }
        }
        return bytes;
    }

    public void setData(byte[] bytes) {
        setData(bytes, false);
    }

    public void setData(byte[] bytes, boolean append) {
        if (isFlushed()) {
            throw new PdfException(PdfException.CannotOperateWithFlushedPdfStream);
        }
        if (this.inputStream != null) {
            throw new PdfException("Cannot set data to PdfStream which was created by InputStream.");
        }
        boolean outputStreamIsUninitialized = this.outputStream == null;
        if (outputStreamIsUninitialized) {
            this.outputStream = new PdfOutputStream(new ByteArrayOutputStream());
        }
        if (append) {
            if ((outputStreamIsUninitialized && getIndirectReference() != null && getIndirectReference().getReader() != null) || (!outputStreamIsUninitialized && containsKey(PdfName.Filter))) {
                try {
                    byte[] oldBytes = getBytes();
                    this.outputStream.assignBytes(oldBytes, oldBytes.length);
                } catch (PdfException ex) {
                    throw new PdfException(PdfException.CannotReadAStreamInOrderToAppendNewBytes, (Throwable) ex);
                }
            }
            if (bytes != null) {
                this.outputStream.writeBytes(bytes);
            }
        } else if (bytes != null) {
            this.outputStream.assignBytes(bytes, bytes.length);
        } else {
            this.outputStream.reset();
        }
        this.offset = 0L;
        remove(PdfName.Filter);
        remove(PdfName.DecodeParms);
    }

    @Override // com.itextpdf.kernel.pdf.PdfDictionary, com.itextpdf.kernel.pdf.PdfObject
    protected PdfObject newInstance() {
        return new PdfStream();
    }

    protected long getOffset() {
        return this.offset;
    }

    protected void updateLength(int length) {
        this.length = length;
    }

    @Override // com.itextpdf.kernel.pdf.PdfDictionary, com.itextpdf.kernel.pdf.PdfObject
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfStream stream = (PdfStream) from;
        if (!$assertionsDisabled && this.inputStream != null) {
            throw new AssertionError("Try to copy the PdfStream that has been just created.");
        }
        byte[] bytes = stream.getBytes(false);
        try {
            this.outputStream.write(bytes);
        } catch (IOException ioe) {
            throw new PdfException(PdfException.CannotCopyObjectContent, ioe, stream);
        }
    }

    protected void initOutputStream(OutputStream stream) {
        if (getOutputStream() == null && this.inputStream == null) {
            this.outputStream = new PdfOutputStream(stream != null ? stream : new ByteArrayOutputStream());
        }
    }

    @Override // com.itextpdf.kernel.pdf.PdfDictionary
    protected void releaseContent() {
        super.releaseContent();
        try {
            if (this.outputStream != null) {
                this.outputStream.close();
                this.outputStream = null;
            }
        } catch (IOException e) {
            throw new PdfException("I/O exception.", (Throwable) e);
        }
    }

    protected InputStream getInputStream() {
        return this.inputStream;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.inputStream == null || (this.inputStream instanceof Serializable)) {
            out.defaultWriteObject();
            return;
        }
        InputStream backup = this.inputStream;
        this.inputStream = null;
        LoggerFactory.getLogger(getClass()).warn(LogMessageConstant.INPUT_STREAM_CONTENT_IS_LOST_ON_PDFSTREAM_SERIALIZATION);
        this.inputStream = backup;
    }
}
