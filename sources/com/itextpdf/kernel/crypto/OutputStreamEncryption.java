package com.itextpdf.kernel.crypto;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/OutputStreamEncryption.class */
public abstract class OutputStreamEncryption extends OutputStream {
    protected OutputStream out;
    private byte[] sb = new byte[1];

    @Override // java.io.OutputStream
    public abstract void write(byte[] bArr, int i, int i2) throws IOException;

    public abstract void finish();

    protected OutputStreamEncryption(OutputStream out) {
        this.out = out;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        finish();
        this.out.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.sb[0] = (byte) b;
        write(this.sb, 0, 1);
    }
}
