package com.itextpdf.io.source;

import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ThreadSafeRandomAccessSource.class */
public class ThreadSafeRandomAccessSource implements IRandomAccessSource {
    private final IRandomAccessSource source;
    private final Object lockObj = new Object();

    public ThreadSafeRandomAccessSource(IRandomAccessSource source) {
        this.source = source;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        int i;
        synchronized (this.lockObj) {
            i = this.source.get(position);
        }
        return i;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        int i;
        synchronized (this.lockObj) {
            i = this.source.get(position, bytes, off, len);
        }
        return i;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        long length;
        synchronized (this.lockObj) {
            length = this.source.length();
        }
        return length;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        synchronized (this.lockObj) {
            this.source.close();
        }
    }
}
