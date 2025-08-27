package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/DSTU4145KeyPairGenerator.class */
public class DSTU4145KeyPairGenerator extends ECKeyPairGenerator {
    @Override // org.bouncycastle.crypto.generators.ECKeyPairGenerator, org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
    public AsymmetricCipherKeyPair generateKeyPair() {
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters) super.generateKeyPair().getPublic();
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new ECPublicKeyParameters(eCPublicKeyParameters.getQ().negate(), eCPublicKeyParameters.getParameters()), r0.getPrivate());
    }
}
