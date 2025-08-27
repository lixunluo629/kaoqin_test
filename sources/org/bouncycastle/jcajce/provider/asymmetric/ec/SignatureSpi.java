package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.DSAExt;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.ECNRSigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.crypto.signers.PlainDSAEncoding;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSABase;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi.class */
public class SignatureSpi extends DSABase {

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecCVCDSA.class */
    public static class ecCVCDSA extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecCVCDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecCVCDSA224.class */
    public static class ecCVCDSA224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecCVCDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecCVCDSA256.class */
    public static class ecCVCDSA256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecCVCDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecCVCDSA384.class */
    public static class ecCVCDSA384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecCVCDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecCVCDSA512.class */
    public static class ecCVCDSA512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecCVCDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSA.class */
    public static class ecDSA extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSA224.class */
    public static class ecDSA224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSA256.class */
    public static class ecDSA256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSA384.class */
    public static class ecDSA384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSA512.class */
    public static class ecDSA512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSARipeMD160.class */
    public static class ecDSARipeMD160 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v1, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSARipeMD160() {
            super(new RIPEMD160Digest(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSASha3_224.class */
    public static class ecDSASha3_224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSASha3_224() {
            super(DigestFactory.createSHA3_224(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSASha3_256.class */
    public static class ecDSASha3_256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSASha3_256() {
            super(DigestFactory.createSHA3_256(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSASha3_384.class */
    public static class ecDSASha3_384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSASha3_384() {
            super(DigestFactory.createSHA3_384(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSASha3_512.class */
    public static class ecDSASha3_512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSASha3_512() {
            super(DigestFactory.createSHA3_512(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDSAnone.class */
    public static class ecDSAnone extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v1, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDSAnone() {
            super(new NullDigest(), new ECDSASigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSA.class */
    public static class ecDetDSA extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA1())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSA224.class */
    public static class ecDetDSA224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA224())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSA256.class */
    public static class ecDetDSA256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA256())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSA384.class */
    public static class ecDetDSA384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA384())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSA512.class */
    public static class ecDetDSA512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA512())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSASha3_224.class */
    public static class ecDetDSASha3_224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSASha3_224() {
            super(DigestFactory.createSHA3_224(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_224())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSASha3_256.class */
    public static class ecDetDSASha3_256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSASha3_256() {
            super(DigestFactory.createSHA3_256(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_256())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSASha3_384.class */
    public static class ecDetDSASha3_384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSASha3_384() {
            super(DigestFactory.createSHA3_384(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_384())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecDetDSASha3_512.class */
    public static class ecDetDSASha3_512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecDetDSASha3_512() {
            super(DigestFactory.createSHA3_512(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_512())), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecNR.class */
    public static class ecNR extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECNRSigner] */
        public ecNR() {
            super(DigestFactory.createSHA1(), new ECNRSigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecNR224.class */
    public static class ecNR224 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECNRSigner] */
        public ecNR224() {
            super(DigestFactory.createSHA224(), new ECNRSigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecNR256.class */
    public static class ecNR256 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECNRSigner] */
        public ecNR256() {
            super(DigestFactory.createSHA256(), new ECNRSigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecNR384.class */
    public static class ecNR384 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECNRSigner] */
        public ecNR384() {
            super(DigestFactory.createSHA384(), new ECNRSigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecNR512.class */
    public static class ecNR512 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v0, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECNRSigner] */
        public ecNR512() {
            super(DigestFactory.createSHA512(), new ECNRSigner(), StandardDSAEncoding.INSTANCE);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/ec/SignatureSpi$ecPlainDSARP160.class */
    public static class ecPlainDSARP160 extends SignatureSpi {
        /* JADX WARN: Type inference failed for: r2v1, types: [org.bouncycastle.crypto.DSAExt, org.bouncycastle.crypto.signers.ECDSASigner] */
        public ecPlainDSARP160() {
            super(new RIPEMD160Digest(), new ECDSASigner(), PlainDSAEncoding.INSTANCE);
        }
    }

    SignatureSpi(Digest digest, DSAExt dSAExt, DSAEncoding dSAEncoding) {
        super(digest, dSAExt, dSAEncoding);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameterGeneratePublicKeyParameter = ECUtils.generatePublicKeyParameter(publicKey);
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameterGeneratePublicKeyParameter);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameterGeneratePrivateKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        this.digest.reset();
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameterGeneratePrivateKeyParameter, this.appRandom));
        } else {
            this.signer.init(true, asymmetricKeyParameterGeneratePrivateKeyParameter);
        }
    }
}
