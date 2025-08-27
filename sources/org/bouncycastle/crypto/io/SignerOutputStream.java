package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Signer;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/io/SignerOutputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/io/SignerOutputStream.class */
public class SignerOutputStream extends FilterOutputStream {
    protected Signer signer;

    public SignerOutputStream(OutputStream outputStream, Signer signer) {
        super(outputStream);
        this.signer = signer;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        this.signer.update((byte) i);
        this.out.write(i);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.signer.update(bArr, i, i2);
        this.out.write(bArr, i, i2);
    }

    public Signer getSigner() {
        return this.signer;
    }
}
