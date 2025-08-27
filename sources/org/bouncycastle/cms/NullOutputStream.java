package org.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/NullOutputStream.class */
class NullOutputStream extends OutputStream {
    NullOutputStream() {
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
    }
}
