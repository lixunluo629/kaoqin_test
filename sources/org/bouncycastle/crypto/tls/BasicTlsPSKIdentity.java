package org.bouncycastle.crypto.tls;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/BasicTlsPSKIdentity.class */
public class BasicTlsPSKIdentity implements TlsPSKIdentity {
    protected byte[] identity;
    protected byte[] psk;

    public BasicTlsPSKIdentity(byte[] bArr, byte[] bArr2) {
        this.identity = Arrays.clone(bArr);
        this.psk = Arrays.clone(bArr2);
    }

    public BasicTlsPSKIdentity(String str, byte[] bArr) {
        this.identity = Strings.toUTF8ByteArray(str);
        this.psk = Arrays.clone(bArr);
    }

    @Override // org.bouncycastle.crypto.tls.TlsPSKIdentity
    public void skipIdentityHint() {
    }

    @Override // org.bouncycastle.crypto.tls.TlsPSKIdentity
    public void notifyIdentityHint(byte[] bArr) {
    }

    @Override // org.bouncycastle.crypto.tls.TlsPSKIdentity
    public byte[] getPSKIdentity() {
        return this.identity;
    }

    @Override // org.bouncycastle.crypto.tls.TlsPSKIdentity
    public byte[] getPSK() {
        return Arrays.clone(this.psk);
    }
}
