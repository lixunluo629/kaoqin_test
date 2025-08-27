package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.poifs.property.DocumentProperty;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/NDocumentOutputStream.class */
public final class NDocumentOutputStream extends OutputStream {
    private int _document_size;
    private boolean _closed;
    private NPOIFSDocument _document;
    private DocumentProperty _property;
    private ByteArrayOutputStream _buffer = new ByteArrayOutputStream(4096);
    private NPOIFSStream _stream;
    private OutputStream _stream_output;

    public NDocumentOutputStream(DocumentEntry document) throws IOException {
        if (!(document instanceof DocumentNode)) {
            throw new IOException("Cannot open internal document storage, " + document + " not a Document Node");
        }
        this._document_size = 0;
        this._closed = false;
        this._property = (DocumentProperty) ((DocumentNode) document).getProperty();
        this._document = new NPOIFSDocument((DocumentNode) document);
        this._document.free();
    }

    public NDocumentOutputStream(DirectoryEntry parent, String name) throws IOException {
        if (!(parent instanceof DirectoryNode)) {
            throw new IOException("Cannot open internal directory storage, " + parent + " not a Directory Node");
        }
        this._document_size = 0;
        this._closed = false;
        DocumentEntry doc = parent.createDocument(name, new ByteArrayInputStream(new byte[0]));
        this._property = (DocumentProperty) ((DocumentNode) doc).getProperty();
        this._document = new NPOIFSDocument((DocumentNode) doc);
    }

    private void dieIfClosed() throws IOException {
        if (this._closed) {
            throw new IOException("cannot perform requested operation on a closed stream");
        }
    }

    private void checkBufferSize() throws IOException {
        if (this._buffer.size() > 4096) {
            byte[] data = this._buffer.toByteArray();
            this._buffer = null;
            write(data, 0, data.length);
        }
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        dieIfClosed();
        if (this._buffer != null) {
            this._buffer.write(b);
            checkBufferSize();
        } else {
            write(new byte[]{(byte) b});
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        dieIfClosed();
        if (this._buffer != null) {
            this._buffer.write(b);
            checkBufferSize();
        } else {
            write(b, 0, b.length);
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        dieIfClosed();
        if (this._buffer != null) {
            this._buffer.write(b, off, len);
            checkBufferSize();
            return;
        }
        if (this._stream == null) {
            this._stream = new NPOIFSStream(this._document.getFileSystem());
            this._stream_output = this._stream.getOutputStream();
        }
        this._stream_output.write(b, off, len);
        this._document_size += len;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this._buffer != null) {
            this._document.replaceContents(new ByteArrayInputStream(this._buffer.toByteArray()));
        } else {
            this._stream_output.close();
            this._property.updateSize(this._document_size);
            this._property.setStartBlock(this._stream.getStartBlock());
        }
        this._closed = true;
    }
}
