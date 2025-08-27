package org.bouncycastle.mime.encoding;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Base64;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/encoding/Base64OutputStream.class */
public class Base64OutputStream extends FilterOutputStream {
    byte[] buffer;
    int bufOff;

    public Base64OutputStream(OutputStream outputStream) {
        super(outputStream);
        this.buffer = new byte[54];
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        doWrite((byte) i);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        for (int i3 = 0; i3 != i2; i3++) {
            doWrite(bArr[i + i3]);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.bufOff > 0) {
            Base64.encode(this.buffer, 0, this.bufOff, this.out);
        }
        this.out.close();
    }

    private void doWrite(byte b) throws IOException {
        byte[] bArr = this.buffer;
        int i = this.bufOff;
        this.bufOff = i + 1;
        bArr[i] = b;
        if (this.bufOff == this.buffer.length) {
            Base64.encode(this.buffer, 0, this.buffer.length, this.out);
            this.out.write(13);
            this.out.write(10);
            this.bufOff = 0;
        }
    }
}
