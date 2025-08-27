package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import org.apache.poi.ddf.EscherProperties;
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.generators.Ed448KeyPairGenerator;
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
import org.bouncycastle.crypto.generators.X448KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed448KeyGenerationParameters;
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.X448KeyGenerationParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;
import org.bouncycastle.jcajce.spec.XDHParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi.class */
public class KeyPairGeneratorSpi extends java.security.KeyPairGeneratorSpi {
    private static final int EdDSA = -1;
    private static final int XDH = -2;
    private static final int Ed448 = 0;
    private static final int Ed25519 = 1;
    private static final int X448 = 2;
    private static final int X25519 = 3;
    private int algorithm;
    private AsymmetricCipherKeyPairGenerator generator;
    private boolean initialised;
    private SecureRandom secureRandom;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$Ed25519.class */
    public static final class Ed25519 extends KeyPairGeneratorSpi {
        public Ed25519() {
            super(1, new Ed25519KeyPairGenerator());
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$Ed448.class */
    public static final class Ed448 extends KeyPairGeneratorSpi {
        public Ed448() {
            super(0, new Ed448KeyPairGenerator());
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$EdDSA.class */
    public static final class EdDSA extends KeyPairGeneratorSpi {
        public EdDSA() {
            super(-1, null);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$X25519.class */
    public static final class X25519 extends KeyPairGeneratorSpi {
        public X25519() {
            super(3, new X25519KeyPairGenerator());
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$X448.class */
    public static final class X448 extends KeyPairGeneratorSpi {
        public X448() {
            super(2, new X448KeyPairGenerator());
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi$XDH.class */
    public static final class XDH extends KeyPairGeneratorSpi {
        public XDH() {
            super(-2, null);
        }
    }

    KeyPairGeneratorSpi(int i, AsymmetricCipherKeyPairGenerator asymmetricCipherKeyPairGenerator) {
        this.algorithm = i;
        this.generator = asymmetricCipherKeyPairGenerator;
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i, SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        switch (i) {
            case 255:
            case 256:
                switch (this.algorithm) {
                    case -2:
                    case 3:
                        setupGenerator(3);
                        return;
                    case -1:
                    case 1:
                        setupGenerator(1);
                        return;
                    case 0:
                    case 2:
                    default:
                        throw new InvalidParameterException("key size not configurable");
                }
            case EscherProperties.LINESTYLE__COLOR /* 448 */:
                switch (this.algorithm) {
                    case -2:
                    case 2:
                        setupGenerator(2);
                        return;
                    case -1:
                    case 0:
                        setupGenerator(0);
                        return;
                    case 1:
                    default:
                        throw new InvalidParameterException("key size not configurable");
                }
            default:
                throw new InvalidParameterException("unknown key size");
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        this.secureRandom = secureRandom;
        if (algorithmParameterSpec instanceof ECGenParameterSpec) {
            initializeGenerator(((ECGenParameterSpec) algorithmParameterSpec).getName());
            return;
        }
        if (algorithmParameterSpec instanceof ECNamedCurveGenParameterSpec) {
            initializeGenerator(((ECNamedCurveGenParameterSpec) algorithmParameterSpec).getName());
            return;
        }
        if (algorithmParameterSpec instanceof EdDSAParameterSpec) {
            initializeGenerator(((EdDSAParameterSpec) algorithmParameterSpec).getCurveName());
            return;
        }
        if (algorithmParameterSpec instanceof XDHParameterSpec) {
            initializeGenerator(((XDHParameterSpec) algorithmParameterSpec).getCurveName());
            return;
        }
        String nameFrom = ECUtil.getNameFrom(algorithmParameterSpec);
        if (nameFrom == null) {
            throw new InvalidAlgorithmParameterException("invalid parameterSpec: " + algorithmParameterSpec);
        }
        initializeGenerator(nameFrom);
    }

    private void algorithmCheck(int i) throws InvalidAlgorithmParameterException {
        if (this.algorithm != i) {
            if (this.algorithm == 1 || this.algorithm == 0) {
                throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
            }
            if (this.algorithm == -1 && i != 1 && i != 0) {
                throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
            }
            if (this.algorithm == 3 || this.algorithm == 2) {
                throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
            }
            if (this.algorithm == -2 && i != 3 && i != 2) {
                throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
            }
            this.algorithm = i;
        }
    }

    private void initializeGenerator(String str) throws InvalidAlgorithmParameterException {
        if (str.equalsIgnoreCase(EdDSAParameterSpec.Ed448) || str.equals(EdECObjectIdentifiers.id_Ed448.getId())) {
            algorithmCheck(0);
            this.generator = new Ed448KeyPairGenerator();
            setupGenerator(0);
            return;
        }
        if (str.equalsIgnoreCase(EdDSAParameterSpec.Ed25519) || str.equals(EdECObjectIdentifiers.id_Ed25519.getId())) {
            algorithmCheck(1);
            this.generator = new Ed25519KeyPairGenerator();
            setupGenerator(1);
        } else if (str.equalsIgnoreCase(XDHParameterSpec.X448) || str.equals(EdECObjectIdentifiers.id_X448.getId())) {
            algorithmCheck(2);
            this.generator = new X448KeyPairGenerator();
            setupGenerator(2);
        } else if (str.equalsIgnoreCase(XDHParameterSpec.X25519) || str.equals(EdECObjectIdentifiers.id_X25519.getId())) {
            algorithmCheck(3);
            this.generator = new X25519KeyPairGenerator();
            setupGenerator(3);
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        if (this.generator == null) {
            throw new IllegalStateException("generator not correctly initialized");
        }
        if (!this.initialised) {
            setupGenerator(this.algorithm);
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.generator.generateKeyPair();
        switch (this.algorithm) {
            case 0:
                return new KeyPair(new BCEdDSAPublicKey(asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new BCEdDSAPrivateKey(asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
            case 1:
                return new KeyPair(new BCEdDSAPublicKey(asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new BCEdDSAPrivateKey(asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
            case 2:
                return new KeyPair(new BCXDHPublicKey(asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new BCXDHPrivateKey(asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
            case 3:
                return new KeyPair(new BCXDHPublicKey(asymmetricCipherKeyPairGenerateKeyPair.getPublic()), new BCXDHPrivateKey(asymmetricCipherKeyPairGenerateKeyPair.getPrivate()));
            default:
                throw new IllegalStateException("generator not correctly initialized");
        }
    }

    private void setupGenerator(int i) {
        this.initialised = true;
        if (this.secureRandom == null) {
            this.secureRandom = new SecureRandom();
        }
        switch (i) {
            case -2:
            case 3:
                this.generator.init(new X25519KeyGenerationParameters(this.secureRandom));
                break;
            case -1:
            case 1:
                this.generator.init(new Ed25519KeyGenerationParameters(this.secureRandom));
                break;
            case 0:
                this.generator.init(new Ed448KeyGenerationParameters(this.secureRandom));
                break;
            case 2:
                this.generator.init(new X448KeyGenerationParameters(this.secureRandom));
                break;
        }
    }
}
