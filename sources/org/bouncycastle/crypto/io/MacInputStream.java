package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/io/MacInputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/io/MacInputStream.class */
public class MacInputStream extends FilterInputStream {
    protected Mac mac;

    public MacInputStream(InputStream inputStream, Mac mac) {
        super(inputStream);
        this.mac = mac;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IllegalStateException, IOException {
        int i = this.in.read();
        if (i >= 0) {
            this.mac.update((byte) i);
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IOException {
        int i3 = this.in.read(bArr, i, i2);
        if (i3 >= 0) {
            this.mac.update(bArr, i, i3);
        }
        return i3;
    }

    public Mac getMac() {
        return this.mac;
    }
}
