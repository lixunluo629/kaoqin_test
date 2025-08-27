package com.adobe.xmp.impl;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/CountOutputStream.class */
public final class CountOutputStream extends OutputStream {
    private final OutputStream out;
    private int bytesWritten = 0;

    CountOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        this.bytesWritten += i2;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        this.out.write(bArr);
        this.bytesWritten += bArr.length;
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.out.write(i);
        this.bytesWritten++;
    }

    public int getBytesWritten() {
        return this.bytesWritten;
    }
}
