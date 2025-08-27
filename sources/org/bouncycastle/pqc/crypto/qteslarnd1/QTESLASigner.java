package org.bouncycastle.pqc.crypto.qteslarnd1;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/QTESLASigner.class */
public class QTESLASigner implements MessageSigner {
    private QTESLAPublicKeyParameters publicKey;
    private QTESLAPrivateKeyParameters privateKey;
    private SecureRandom secureRandom;

    @Override // org.bouncycastle.pqc.crypto.MessageSigner
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!z) {
            this.privateKey = null;
            this.publicKey = (QTESLAPublicKeyParameters) cipherParameters;
            QTESLASecurityCategory.validate(this.publicKey.getSecurityCategory());
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            this.secureRandom = ((ParametersWithRandom) cipherParameters).getRandom();
            this.privateKey = (QTESLAPrivateKeyParameters) ((ParametersWithRandom) cipherParameters).getParameters();
        } else {
            this.secureRandom = CryptoServicesRegistrar.getSecureRandom();
            this.privateKey = (QTESLAPrivateKeyParameters) cipherParameters;
        }
        this.publicKey = null;
        QTESLASecurityCategory.validate(this.privateKey.getSecurityCategory());
    }

    @Override // org.bouncycastle.pqc.crypto.MessageSigner
    public byte[] generateSignature(byte[] bArr) {
        byte[] bArr2 = new byte[QTESLASecurityCategory.getSignatureSize(this.privateKey.getSecurityCategory())];
        switch (this.privateKey.getSecurityCategory()) {
            case 0:
                QTESLA.signingI(bArr2, bArr, 0, bArr.length, this.privateKey.getSecret(), this.secureRandom);
                break;
            case 1:
                QTESLA.signingIIISize(bArr2, bArr, 0, bArr.length, this.privateKey.getSecret(), this.secureRandom);
                break;
            case 2:
                QTESLA.signingIIISpeed(bArr2, bArr, 0, bArr.length, this.privateKey.getSecret(), this.secureRandom);
                break;
            case 3:
                QTESLA.signingIP(bArr2, bArr, 0, bArr.length, this.privateKey.getSecret(), this.secureRandom);
                break;
            case 4:
                QTESLA.signingIIIP(bArr2, bArr, 0, bArr.length, this.privateKey.getSecret(), this.secureRandom);
                break;
            default:
                throw new IllegalArgumentException("unknown security category: " + this.privateKey.getSecurityCategory());
        }
        return bArr2;
    }

    @Override // org.bouncycastle.pqc.crypto.MessageSigner
    public boolean verifySignature(byte[] bArr, byte[] bArr2) {
        int iVerifyingPIII;
        switch (this.publicKey.getSecurityCategory()) {
            case 0:
                iVerifyingPIII = QTESLA.verifyingI(bArr, bArr2, 0, bArr2.length, this.publicKey.getPublicData());
                break;
            case 1:
                iVerifyingPIII = QTESLA.verifyingIIISize(bArr, bArr2, 0, bArr2.length, this.publicKey.getPublicData());
                break;
            case 2:
                iVerifyingPIII = QTESLA.verifyingIIISpeed(bArr, bArr2, 0, bArr2.length, this.publicKey.getPublicData());
                break;
            case 3:
                iVerifyingPIII = QTESLA.verifyingPI(bArr, bArr2, 0, bArr2.length, this.publicKey.getPublicData());
                break;
            case 4:
                iVerifyingPIII = QTESLA.verifyingPIII(bArr, bArr2, 0, bArr2.length, this.publicKey.getPublicData());
                break;
            default:
                throw new IllegalArgumentException("unknown security category: " + this.publicKey.getSecurityCategory());
        }
        return 0 == iVerifyingPIII;
    }
}
