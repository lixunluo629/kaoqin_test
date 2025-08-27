package org.bouncycastle.jce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.Hashtable;
import org.apache.poi.hssf.record.UnknownRecord;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator.class */
public abstract class KeyPairGenerator extends JDKKeyPairGenerator {

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$EC.class */
    public static class EC extends KeyPairGenerator {
        ECKeyGenerationParameters param;
        ECKeyPairGenerator engine;
        Object ecParams;
        int strength;
        int certainty;
        SecureRandom random;
        boolean initialised;
        String algorithm;
        private static Hashtable ecParameters = new Hashtable();

        public EC() {
            super("EC");
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = UnknownRecord.PHONETICPR_00EF;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = "EC";
        }

        public EC(String str) {
            super(str);
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = UnknownRecord.PHONETICPR_00EF;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = str;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i, SecureRandom secureRandom) {
            this.strength = i;
            this.random = secureRandom;
            this.ecParams = ecParameters.get(new Integer(i));
            if (this.ecParams == null) {
                throw new InvalidParameterException("unknown key size.");
            }
            try {
                initialize((ECGenParameterSpec) this.ecParams, secureRandom);
            } catch (InvalidAlgorithmParameterException e) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec instanceof ECParameterSpec) {
                ECParameterSpec eCParameterSpec = (ECParameterSpec) algorithmParameterSpec;
                this.ecParams = algorithmParameterSpec;
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN()), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
                return;
            }
            if (algorithmParameterSpec instanceof java.security.spec.ECParameterSpec) {
                java.security.spec.ECParameterSpec eCParameterSpec2 = (java.security.spec.ECParameterSpec) algorithmParameterSpec;
                this.ecParams = algorithmParameterSpec;
                ECCurve eCCurveConvertCurve = EC5Util.convertCurve(eCParameterSpec2.getCurve());
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCCurveConvertCurve, EC5Util.convertPoint(eCCurveConvertCurve, eCParameterSpec2.getGenerator(), false), eCParameterSpec2.getOrder(), BigInteger.valueOf(eCParameterSpec2.getCofactor())), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
                return;
            }
            if (!(algorithmParameterSpec instanceof ECGenParameterSpec)) {
                if (algorithmParameterSpec != null || ProviderUtil.getEcImplicitlyCa() == null) {
                    if (algorithmParameterSpec != null || ProviderUtil.getEcImplicitlyCa() != null) {
                        throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
                    }
                    throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                }
                ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
                this.ecParams = algorithmParameterSpec;
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN()), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
                return;
            }
            String name = ((ECGenParameterSpec) algorithmParameterSpec).getName();
            if (this.algorithm.equals("ECGOST3410")) {
                ECDomainParameters byName = ECGOST3410NamedCurves.getByName(name);
                if (byName == null) {
                    throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                }
                this.ecParams = new ECNamedCurveSpec(name, byName.getCurve(), byName.getG(), byName.getN(), byName.getH(), byName.getSeed());
            } else {
                X9ECParameters byName2 = X962NamedCurves.getByName(name);
                if (byName2 == null) {
                    byName2 = SECNamedCurves.getByName(name);
                    if (byName2 == null) {
                        byName2 = NISTNamedCurves.getByName(name);
                    }
                    if (byName2 == null) {
                        byName2 = TeleTrusTNamedCurves.getByName(name);
                    }
                    if (byName2 == null) {
                        try {
                            DERObjectIdentifier dERObjectIdentifier = new DERObjectIdentifier(name);
                            byName2 = X962NamedCurves.getByOID(dERObjectIdentifier);
                            if (byName2 == null) {
                                byName2 = SECNamedCurves.getByOID(dERObjectIdentifier);
                            }
                            if (byName2 == null) {
                                byName2 = NISTNamedCurves.getByOID(dERObjectIdentifier);
                            }
                            if (byName2 == null) {
                                byName2 = TeleTrusTNamedCurves.getByOID(dERObjectIdentifier);
                            }
                            if (byName2 == null) {
                                throw new InvalidAlgorithmParameterException("unknown curve OID: " + name);
                            }
                        } catch (IllegalArgumentException e) {
                            throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                        }
                    }
                }
                this.ecParams = new ECNamedCurveSpec(name, byName2.getCurve(), byName2.getG(), byName2.getN(), byName2.getH(), null);
            }
            java.security.spec.ECParameterSpec eCParameterSpec3 = (java.security.spec.ECParameterSpec) this.ecParams;
            ECCurve eCCurveConvertCurve2 = EC5Util.convertCurve(eCParameterSpec3.getCurve());
            this.param = new ECKeyGenerationParameters(new ECDomainParameters(eCCurveConvertCurve2, EC5Util.convertPoint(eCCurveConvertCurve2, eCParameterSpec3.getGenerator(), false), eCParameterSpec3.getOrder(), BigInteger.valueOf(eCParameterSpec3.getCofactor())), secureRandom);
            this.engine.init(this.param);
            this.initialised = true;
        }

        @Override // org.bouncycastle.jce.provider.JDKKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                throw new IllegalStateException("EC Key Pair Generator not initialised");
            }
            AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateKeyPair = this.engine.generateKeyPair();
            ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPublic();
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters) asymmetricCipherKeyPairGenerateKeyPair.getPrivate();
            if (this.ecParams instanceof ECParameterSpec) {
                ECParameterSpec eCParameterSpec = (ECParameterSpec) this.ecParams;
                JCEECPublicKey jCEECPublicKey = new JCEECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec);
                return new KeyPair(jCEECPublicKey, new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters, jCEECPublicKey, eCParameterSpec));
            }
            if (this.ecParams == null) {
                return new KeyPair(new JCEECPublicKey(this.algorithm, eCPublicKeyParameters), new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters));
            }
            java.security.spec.ECParameterSpec eCParameterSpec2 = (java.security.spec.ECParameterSpec) this.ecParams;
            JCEECPublicKey jCEECPublicKey2 = new JCEECPublicKey(this.algorithm, eCPublicKeyParameters, eCParameterSpec2);
            return new KeyPair(jCEECPublicKey2, new JCEECPrivateKey(this.algorithm, eCPrivateKeyParameters, jCEECPublicKey2, eCParameterSpec2));
        }

        static {
            ecParameters.put(new Integer(192), new ECGenParameterSpec("prime192v1"));
            ecParameters.put(new Integer(UnknownRecord.PHONETICPR_00EF), new ECGenParameterSpec("prime239v1"));
            ecParameters.put(new Integer(256), new ECGenParameterSpec("prime256v1"));
            ecParameters.put(new Integer(224), new ECGenParameterSpec("P-224"));
            ecParameters.put(new Integer(384), new ECGenParameterSpec("P-384"));
            ecParameters.put(new Integer(521), new ECGenParameterSpec("P-521"));
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$ECDH.class */
    public static class ECDH extends EC {
        public ECDH() {
            super("ECDH");
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$ECDHC.class */
    public static class ECDHC extends EC {
        public ECDHC() {
            super("ECDHC");
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$ECDSA.class */
    public static class ECDSA extends EC {
        public ECDSA() {
            super("ECDSA");
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$ECGOST3410.class */
    public static class ECGOST3410 extends EC {
        public ECGOST3410() {
            super("ECGOST3410");
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyPairGenerator$ECMQV.class */
    public static class ECMQV extends EC {
        public ECMQV() {
            super("ECMQV");
        }
    }

    public KeyPairGenerator(String str) {
        super(str);
    }
}
