package com.itextpdf.kernel.xmp.impl;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/CountOutputStream.class */
public final class CountOutputStream extends OutputStream {
    private final OutputStream output;
    private int bytesWritten = 0;

    CountOutputStream(OutputStream output) {
        this.output = output;
    }

    @Override // java.io.OutputStream
    public void write(byte[] buf, int off, int len) throws IOException {
        this.output.write(buf, off, len);
        this.bytesWritten += len;
    }

    @Override // java.io.OutputStream
    public void write(byte[] buf) throws IOException {
        this.output.write(buf);
        this.bytesWritten += buf.length;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.output.write(b);
        this.bytesWritten++;
    }

    public int getBytesWritten() {
        return this.bytesWritten;
    }
}
