package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.signers.DSASigner;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsDSSSigner.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsDSSSigner.class */
class TlsDSSSigner extends TlsDSASigner {
    TlsDSSSigner() {
    }

    @Override // org.bouncycastle.crypto.tls.TlsSigner
    public boolean isValidPublicKey(AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof DSAPublicKeyParameters;
    }

    @Override // org.bouncycastle.crypto.tls.TlsDSASigner
    protected DSA createDSAImpl() {
        return new DSASigner();
    }
}
