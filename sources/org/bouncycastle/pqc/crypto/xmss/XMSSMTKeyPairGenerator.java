package org.bouncycastle.pqc.crypto.xmss;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.xmss.XMSSMTPublicKeyParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/xmss/XMSSMTKeyPairGenerator.class */
public final class XMSSMTKeyPairGenerator {
    private XMSSMTParameters params;
    private XMSSParameters xmssParams;
    private SecureRandom prng;

    public void init(KeyGenerationParameters keyGenerationParameters) {
        XMSSMTKeyGenerationParameters xMSSMTKeyGenerationParameters = (XMSSMTKeyGenerationParameters) keyGenerationParameters;
        this.prng = xMSSMTKeyGenerationParameters.getRandom();
        this.params = xMSSMTKeyGenerationParameters.getParameters();
        this.xmssParams = this.params.getXMSSParameters();
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParametersGeneratePrivateKey = generatePrivateKey(new XMSSMTPrivateKeyParameters.Builder(this.params).build().getBDSState());
        this.xmssParams.getWOTSPlus().importKeys(new byte[this.params.getTreeDigestSize()], xMSSMTPrivateKeyParametersGeneratePrivateKey.getPublicSeed());
        int layers = this.params.getLayers() - 1;
        BDS bds = new BDS(this.xmssParams, xMSSMTPrivateKeyParametersGeneratePrivateKey.getPublicSeed(), xMSSMTPrivateKeyParametersGeneratePrivateKey.getSecretKeySeed(), (OTSHashAddress) new OTSHashAddress.Builder().withLayerAddress(layers).build());
        XMSSNode root = bds.getRoot();
        xMSSMTPrivateKeyParametersGeneratePrivateKey.getBDSState().put(layers, bds);
        XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParametersBuild = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(xMSSMTPrivateKeyParametersGeneratePrivateKey.getSecretKeySeed()).withSecretKeyPRF(xMSSMTPrivateKeyParametersGeneratePrivateKey.getSecretKeyPRF()).withPublicSeed(xMSSMTPrivateKeyParametersGeneratePrivateKey.getPublicSeed()).withRoot(root.getValue()).withBDSState(xMSSMTPrivateKeyParametersGeneratePrivateKey.getBDSState()).build();
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(root.getValue()).withPublicSeed(xMSSMTPrivateKeyParametersBuild.getPublicSeed()).build(), (AsymmetricKeyParameter) xMSSMTPrivateKeyParametersBuild);
    }

    private XMSSMTPrivateKeyParameters generatePrivateKey(BDSStateMap bDSStateMap) {
        int treeDigestSize = this.params.getTreeDigestSize();
        byte[] bArr = new byte[treeDigestSize];
        this.prng.nextBytes(bArr);
        byte[] bArr2 = new byte[treeDigestSize];
        this.prng.nextBytes(bArr2);
        byte[] bArr3 = new byte[treeDigestSize];
        this.prng.nextBytes(bArr3);
        return new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(bArr).withSecretKeyPRF(bArr2).withPublicSeed(bArr3).withBDSState(bDSStateMap).build();
    }
}
