package com.itextpdf.io.source;

import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/IndependentRandomAccessSource.class */
public class IndependentRandomAccessSource implements IRandomAccessSource {
    private final IRandomAccessSource source;

    public IndependentRandomAccessSource(IRandomAccessSource source) {
        this.source = source;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        return this.source.get(position);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        return this.source.get(position, bytes, off, len);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.source.length();
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
    }
}
