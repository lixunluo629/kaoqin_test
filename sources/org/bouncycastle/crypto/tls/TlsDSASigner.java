package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSADigestSigner;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsDSASigner.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsDSASigner.class */
abstract class TlsDSASigner implements TlsSigner {
    TlsDSASigner() {
    }

    @Override // org.bouncycastle.crypto.tls.TlsSigner
    public byte[] calculateRawSignature(SecureRandom secureRandom, AsymmetricKeyParameter asymmetricKeyParameter, byte[] bArr) throws CryptoException {
        DSADigestSigner dSADigestSigner = new DSADigestSigner(createDSAImpl(), new NullDigest());
        dSADigestSigner.init(true, new ParametersWithRandom(asymmetricKeyParameter, secureRandom));
        dSADigestSigner.update(bArr, 16, 20);
        return dSADigestSigner.generateSignature();
    }

    @Override // org.bouncycastle.crypto.tls.TlsSigner
    public Signer createVerifyer(AsymmetricKeyParameter asymmetricKeyParameter) {
        DSADigestSigner dSADigestSigner = new DSADigestSigner(createDSAImpl(), new SHA1Digest());
        dSADigestSigner.init(false, asymmetricKeyParameter);
        return dSADigestSigner;
    }

    protected abstract DSA createDSAImpl();
}
