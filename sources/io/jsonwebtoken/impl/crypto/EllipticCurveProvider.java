package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.jce.ECNamedCurveTable;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/EllipticCurveProvider.class */
public abstract class EllipticCurveProvider extends SignatureProvider {
    private static final Map<SignatureAlgorithm, String> EC_CURVE_NAMES = createEcCurveNames();

    private static Map<SignatureAlgorithm, String> createEcCurveNames() {
        Map<SignatureAlgorithm, String> m = new HashMap<>();
        m.put(SignatureAlgorithm.ES256, "secp256r1");
        m.put(SignatureAlgorithm.ES384, "secp384r1");
        m.put(SignatureAlgorithm.ES512, "secp521r1");
        return m;
    }

    protected EllipticCurveProvider(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isEllipticCurve(), "SignatureAlgorithm must be an Elliptic Curve algorithm.");
    }

    public static KeyPair generateKeyPair() {
        return generateKeyPair(SignatureAlgorithm.ES512);
    }

    public static KeyPair generateKeyPair(SignatureAlgorithm alg) {
        return generateKeyPair(alg, SignatureProvider.DEFAULT_SECURE_RANDOM);
    }

    public static KeyPair generateKeyPair(SignatureAlgorithm alg, SecureRandom random) {
        return generateKeyPair("ECDSA", "BC", alg, random);
    }

    public static KeyPair generateKeyPair(String jcaAlgorithmName, String jcaProviderName, SignatureAlgorithm alg, SecureRandom random) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Assert.notNull(alg, "SignatureAlgorithm argument cannot be null.");
        Assert.isTrue(alg.isEllipticCurve(), "SignatureAlgorithm argument must represent an Elliptic Curve algorithm.");
        try {
            KeyPairGenerator g = KeyPairGenerator.getInstance(jcaAlgorithmName, jcaProviderName);
            String paramSpecCurveName = EC_CURVE_NAMES.get(alg);
            g.initialize(ECNamedCurveTable.getParameterSpec(paramSpecCurveName), random);
            return g.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate Elliptic Curve KeyPair: " + e.getMessage(), e);
        }
    }
}
