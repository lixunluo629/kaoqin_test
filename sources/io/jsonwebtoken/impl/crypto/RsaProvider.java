package io.jsonwebtoken.impl.crypto;

import com.moredian.onpremise.core.utils.RSAUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Assert;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/RsaProvider.class */
public abstract class RsaProvider extends SignatureProvider {
    private static final Map<SignatureAlgorithm, PSSParameterSpec> PSS_PARAMETER_SPECS = createPssParameterSpecs();

    private static Map<SignatureAlgorithm, PSSParameterSpec> createPssParameterSpecs() {
        Map<SignatureAlgorithm, PSSParameterSpec> m = new HashMap<>();
        MGF1ParameterSpec ps = MGF1ParameterSpec.SHA256;
        PSSParameterSpec spec = new PSSParameterSpec(ps.getDigestAlgorithm(), "MGF1", ps, 32, 1);
        m.put(SignatureAlgorithm.PS256, spec);
        MGF1ParameterSpec ps2 = MGF1ParameterSpec.SHA384;
        PSSParameterSpec spec2 = new PSSParameterSpec(ps2.getDigestAlgorithm(), "MGF1", ps2, 48, 1);
        m.put(SignatureAlgorithm.PS384, spec2);
        MGF1ParameterSpec ps3 = MGF1ParameterSpec.SHA512;
        PSSParameterSpec spec3 = new PSSParameterSpec(ps3.getDigestAlgorithm(), "MGF1", ps3, 64, 1);
        m.put(SignatureAlgorithm.PS512, spec3);
        return m;
    }

    protected RsaProvider(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isRsa(), "SignatureAlgorithm must be an RSASSA or RSASSA-PSS algorithm.");
    }

    @Override // io.jsonwebtoken.impl.crypto.SignatureProvider
    protected Signature createSignatureInstance() {
        Signature sig = super.createSignatureInstance();
        PSSParameterSpec spec = PSS_PARAMETER_SPECS.get(this.alg);
        if (spec != null) {
            setParameter(sig, spec);
        }
        return sig;
    }

    protected void setParameter(Signature sig, PSSParameterSpec spec) {
        try {
            doSetParameter(sig, spec);
        } catch (InvalidAlgorithmParameterException e) {
            String msg = "Unsupported RSASSA-PSS parameter '" + spec + "': " + e.getMessage();
            throw new SignatureException(msg, e);
        }
    }

    protected void doSetParameter(Signature sig, PSSParameterSpec spec) throws InvalidAlgorithmParameterException {
        sig.setParameter(spec);
    }

    public static KeyPair generateKeyPair() {
        return generateKeyPair(4096);
    }

    public static KeyPair generateKeyPair(int keySizeInBits) {
        return generateKeyPair(keySizeInBits, SignatureProvider.DEFAULT_SECURE_RANDOM);
    }

    public static KeyPair generateKeyPair(int keySizeInBits, SecureRandom random) {
        return generateKeyPair(RSAUtils.RSA_KEY_ALGORITHM, keySizeInBits, random);
    }

    protected static KeyPair generateKeyPair(String jcaAlgorithmName, int keySizeInBits, SecureRandom random) throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(jcaAlgorithmName);
            keyGenerator.initialize(keySizeInBits, random);
            return keyGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to obtain an RSA KeyPairGenerator: " + e.getMessage(), e);
        }
    }
}
