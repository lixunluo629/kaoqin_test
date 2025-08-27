package com.itextpdf.io.source;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/RASInputStream.class */
public class RASInputStream extends InputStream {
    private final IRandomAccessSource source;
    private long position = 0;

    public RASInputStream(IRandomAccessSource source) {
        this.source = source;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int count = this.source.get(this.position, b, off, len);
        this.position += count;
        return count;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        IRandomAccessSource iRandomAccessSource = this.source;
        long j = this.position;
        this.position = j + 1;
        return iRandomAccessSource.get(j);
    }
}
