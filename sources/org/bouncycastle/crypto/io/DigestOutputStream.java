package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Digest;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/io/DigestOutputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/io/DigestOutputStream.class */
public class DigestOutputStream extends FilterOutputStream {
    protected Digest digest;

    public DigestOutputStream(OutputStream outputStream, Digest digest) {
        super(outputStream);
        this.digest = digest;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        this.digest.update((byte) i);
        this.out.write(i);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.digest.update(bArr, i, i2);
        this.out.write(bArr, i, i2);
    }

    public Digest getDigest() {
        return this.digest;
    }
}
