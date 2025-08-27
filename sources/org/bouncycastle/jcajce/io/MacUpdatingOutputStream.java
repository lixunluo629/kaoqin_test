package org.bouncycastle.jcajce.io;

import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Mac;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/io/MacUpdatingOutputStream.class */
class MacUpdatingOutputStream extends OutputStream {
    private Mac mac;

    MacUpdatingOutputStream(Mac mac) {
        this.mac = mac;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IllegalStateException, IOException {
        this.mac.update(bArr, i, i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IllegalStateException, IOException {
        this.mac.update(bArr);
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IllegalStateException, IOException {
        this.mac.update((byte) i);
    }
}
