package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/NDocumentInputStream.class */
public final class NDocumentInputStream extends DocumentInputStream {
    private int _current_offset;
    private int _current_block_count;
    private int _marked_offset;
    private int _marked_offset_count;
    private final int _document_size;
    private boolean _closed;
    private final NPOIFSDocument _document;
    private Iterator<ByteBuffer> _data;
    private ByteBuffer _buffer;

    public NDocumentInputStream(DocumentEntry document) throws IOException {
        if (!(document instanceof DocumentNode)) {
            throw new IOException("Cannot open internal document storage, " + document + " not a Document Node");
        }
        this._current_offset = 0;
        this._current_block_count = 0;
        this._marked_offset = 0;
        this._marked_offset_count = 0;
        this._document_size = document.getSize();
        this._closed = false;
        if (this._document_size < 0) {
        }
        DocumentNode doc = (DocumentNode) document;
        DocumentProperty property = (DocumentProperty) doc.getProperty();
        this._document = new NPOIFSDocument(property, ((DirectoryNode) doc.getParent()).getNFileSystem());
        this._data = this._document.getBlockIterator();
    }

    public NDocumentInputStream(NPOIFSDocument document) {
        this._current_offset = 0;
        this._current_block_count = 0;
        this._marked_offset = 0;
        this._marked_offset_count = 0;
        this._document_size = document.getSize();
        this._closed = false;
        this._document = document;
        this._data = this._document.getBlockIterator();
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream, org.apache.poi.util.LittleEndianInput
    public int available() {
        return remainingBytes();
    }

    private int remainingBytes() {
        if (this._closed) {
            throw new IllegalStateException("cannot perform requested operation on a closed stream");
        }
        return this._document_size - this._current_offset;
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this._closed = true;
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream
    public void mark(int ignoredReadlimit) {
        this._marked_offset = this._current_offset;
        this._marked_offset_count = Math.max(0, this._current_block_count - 1);
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream
    public int read() throws IOException {
        dieIfClosed();
        if (atEOD()) {
            return -1;
        }
        byte[] b = new byte[1];
        int result = read(b, 0, 1);
        if (result >= 0) {
            if (b[0] < 0) {
                return b[0] + 256;
            }
            return b[0];
        }
        return result;
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        dieIfClosed();
        if (b == null) {
            throw new IllegalArgumentException("buffer must not be null");
        }
        if (off < 0 || len < 0 || b.length < off + len) {
            throw new IndexOutOfBoundsException("can't read past buffer boundaries");
        }
        if (len == 0) {
            return 0;
        }
        if (atEOD()) {
            return -1;
        }
        int limit = Math.min(remainingBytes(), len);
        readFully(b, off, limit);
        return limit;
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream
    public void reset() {
        if (this._marked_offset == 0 && this._marked_offset_count == 0) {
            this._current_block_count = this._marked_offset_count;
            this._current_offset = this._marked_offset;
            this._data = this._document.getBlockIterator();
            this._buffer = null;
            return;
        }
        this._data = this._document.getBlockIterator();
        this._current_offset = 0;
        for (int i = 0; i < this._marked_offset_count; i++) {
            this._buffer = this._data.next();
            this._current_offset += this._buffer.remaining();
        }
        this._current_block_count = this._marked_offset_count;
        if (this._current_offset != this._marked_offset) {
            this._buffer = this._data.next();
            this._current_block_count++;
            int skipBy = this._marked_offset - this._current_offset;
            this._buffer.position(this._buffer.position() + skipBy);
        }
        this._current_offset = this._marked_offset;
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, java.io.InputStream
    public long skip(long n) throws IOException {
        dieIfClosed();
        if (n < 0) {
            return 0L;
        }
        long new_offset = this._current_offset + n;
        if (new_offset < this._current_offset || new_offset > this._document_size) {
            new_offset = this._document_size;
        }
        long rval = new_offset - this._current_offset;
        byte[] skip = new byte[(int) rval];
        readFully(skip);
        return rval;
    }

    private void dieIfClosed() throws IOException {
        if (this._closed) {
            throw new IOException("cannot perform requested operation on a closed stream");
        }
    }

    private boolean atEOD() {
        return this._current_offset == this._document_size;
    }

    private void checkAvaliable(int requestedSize) {
        if (this._closed) {
            throw new IllegalStateException("cannot perform requested operation on a closed stream");
        }
        if (requestedSize > this._document_size - this._current_offset) {
            throw new RuntimeException("Buffer underrun - requested " + requestedSize + " bytes but " + (this._document_size - this._current_offset) + " was available");
        }
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public void readFully(byte[] buf, int off, int len) {
        if (len < 0) {
            throw new RuntimeException("Can't read negative number of bytes");
        }
        checkAvaliable(len);
        int i = 0;
        while (true) {
            int read = i;
            if (read < len) {
                if (this._buffer == null || this._buffer.remaining() == 0) {
                    this._current_block_count++;
                    this._buffer = this._data.next();
                }
                int limit = Math.min(len - read, this._buffer.remaining());
                this._buffer.get(buf, off + read, limit);
                this._current_offset += limit;
                i = read + limit;
            } else {
                return;
            }
        }
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public byte readByte() {
        return (byte) readUByte();
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public long readLong() {
        checkAvaliable(8);
        byte[] data = new byte[8];
        readFully(data, 0, 8);
        return LittleEndian.getLong(data, 0);
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public short readShort() {
        checkAvaliable(2);
        byte[] data = new byte[2];
        readFully(data, 0, 2);
        return LittleEndian.getShort(data);
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public int readInt() {
        checkAvaliable(4);
        byte[] data = new byte[4];
        readFully(data, 0, 4);
        return LittleEndian.getInt(data);
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public int readUShort() {
        checkAvaliable(2);
        byte[] data = new byte[2];
        readFully(data, 0, 2);
        return LittleEndian.getUShort(data);
    }

    @Override // org.apache.poi.poifs.filesystem.DocumentInputStream, org.apache.poi.util.LittleEndianInput
    public int readUByte() {
        checkAvaliable(1);
        byte[] data = new byte[1];
        readFully(data, 0, 1);
        if (data[0] >= 0) {
            return data[0];
        }
        return data[0] + 256;
    }
}
