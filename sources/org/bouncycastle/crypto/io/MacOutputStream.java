package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/io/MacOutputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/io/MacOutputStream.class */
public class MacOutputStream extends FilterOutputStream {
    protected Mac mac;

    public MacOutputStream(OutputStream outputStream, Mac mac) {
        super(outputStream);
        this.mac = mac;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IllegalStateException, IOException {
        this.mac.update((byte) i);
        this.out.write(i);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IOException {
        this.mac.update(bArr, i, i2);
        this.out.write(bArr, i, i2);
    }

    public Mac getMac() {
        return this.mac;
    }
}
