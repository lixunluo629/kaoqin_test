package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed448PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed448PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.crypto.signers.Ed448Signer;
import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi.class */
public class SignatureSpi extends java.security.SignatureSpi {
    private static final byte[] EMPTY_CONTEXT = new byte[0];
    private final String algorithm;
    private Signer signer;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi$Ed25519.class */
    public static final class Ed25519 extends SignatureSpi {
        public Ed25519() {
            super(EdDSAParameterSpec.Ed25519);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi$Ed448.class */
    public static final class Ed448 extends SignatureSpi {
        public Ed448() {
            super(EdDSAParameterSpec.Ed448);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi$EdDSA.class */
    public static final class EdDSA extends SignatureSpi {
        public EdDSA() {
            super(null);
        }
    }

    SignatureSpi(String str) {
        this.algorithm = str;
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof BCEdDSAPublicKey)) {
            throw new InvalidKeyException("cannot identify EdDSA public key");
        }
        AsymmetricKeyParameter asymmetricKeyParameterEngineGetKeyParameters = ((BCEdDSAPublicKey) publicKey).engineGetKeyParameters();
        if (asymmetricKeyParameterEngineGetKeyParameters instanceof Ed448PublicKeyParameters) {
            this.signer = getSigner(EdDSAParameterSpec.Ed448);
        } else {
            this.signer = getSigner(EdDSAParameterSpec.Ed25519);
        }
        this.signer.init(false, asymmetricKeyParameterEngineGetKeyParameters);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof BCEdDSAPrivateKey)) {
            throw new InvalidKeyException("cannot identify EdDSA private key");
        }
        AsymmetricKeyParameter asymmetricKeyParameterEngineGetKeyParameters = ((BCEdDSAPrivateKey) privateKey).engineGetKeyParameters();
        if (asymmetricKeyParameterEngineGetKeyParameters instanceof Ed448PrivateKeyParameters) {
            this.signer = getSigner(EdDSAParameterSpec.Ed448);
        } else {
            this.signer = getSigner(EdDSAParameterSpec.Ed25519);
        }
        this.signer.init(true, asymmetricKeyParameterEngineGetKeyParameters);
    }

    private Signer getSigner(String str) throws InvalidKeyException {
        if (this.algorithm == null || str.equals(this.algorithm)) {
            return str.equals(EdDSAParameterSpec.Ed448) ? new Ed448Signer(EMPTY_CONTEXT) : new Ed25519Signer();
        }
        throw new InvalidKeyException("inappropriate key for " + this.algorithm);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b) throws SignatureException {
        this.signer.update(b);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i, int i2) throws SignatureException {
        this.signer.update(bArr, i, i2);
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        try {
            return this.signer.generateSignature();
        } catch (CryptoException e) {
            throw new SignatureException(e.getMessage());
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        return this.signer.verifySignature(bArr);
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    @Override // java.security.SignatureSpi
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }
}
