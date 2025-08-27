package org.bouncycastle.pqc.crypto.qteslarnd1;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/QTESLAKeyPairGenerator.class */
public final class QTESLAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private int securityCategory;
    private SecureRandom secureRandom;

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public void init(KeyGenerationParameters keyGenerationParameters) {
        QTESLAKeyGenerationParameters qTESLAKeyGenerationParameters = (QTESLAKeyGenerationParameters) keyGenerationParameters;
        this.secureRandom = qTESLAKeyGenerationParameters.getRandom();
        this.securityCategory = qTESLAKeyGenerationParameters.getSecurityCategory();
    }

    @Override // org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public AsymmetricCipherKeyPair generateKeyPair() {
        byte[] bArrAllocatePrivate = allocatePrivate(this.securityCategory);
        byte[] bArrAllocatePublic = allocatePublic(this.securityCategory);
        switch (this.securityCategory) {
            case 0:
                QTESLA.generateKeyPairI(bArrAllocatePublic, bArrAllocatePrivate, this.secureRandom);
                break;
            case 1:
                QTESLA.generateKeyPairIIISize(bArrAllocatePublic, bArrAllocatePrivate, this.secureRandom);
                break;
            case 2:
                QTESLA.generateKeyPairIIISpeed(bArrAllocatePublic, bArrAllocatePrivate, this.secureRandom);
                break;
            case 3:
                QTESLA.generateKeyPairIP(bArrAllocatePublic, bArrAllocatePrivate, this.secureRandom);
                break;
            case 4:
                QTESLA.generateKeyPairIIIP(bArrAllocatePublic, bArrAllocatePrivate, this.secureRandom);
                break;
            default:
                throw new IllegalArgumentException("unknown security category: " + this.securityCategory);
        }
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new QTESLAPublicKeyParameters(this.securityCategory, bArrAllocatePublic), (AsymmetricKeyParameter) new QTESLAPrivateKeyParameters(this.securityCategory, bArrAllocatePrivate));
    }

    private byte[] allocatePrivate(int i) {
        return new byte[QTESLASecurityCategory.getPrivateSize(i)];
    }

    private byte[] allocatePublic(int i) {
        return new byte[QTESLASecurityCategory.getPublicSize(i)];
    }
}
