package com.itextpdf.io.source;

import java.io.IOException;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/WindowRandomAccessSource.class */
public class WindowRandomAccessSource implements IRandomAccessSource, Serializable {
    private static final long serialVersionUID = -8539987600466289182L;
    private final IRandomAccessSource source;
    private final long offset;
    private final long length;

    public WindowRandomAccessSource(IRandomAccessSource source, long offset) {
        this(source, offset, source.length() - offset);
    }

    public WindowRandomAccessSource(IRandomAccessSource source, long offset, long length) {
        this.source = source;
        this.offset = offset;
        this.length = length;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        if (position >= this.length) {
            return -1;
        }
        return this.source.get(this.offset + position);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (position >= this.length) {
            return -1;
        }
        long toRead = Math.min(len, this.length - position);
        return this.source.get(this.offset + position, bytes, off, (int) toRead);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.length;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        this.source.close();
    }
}
