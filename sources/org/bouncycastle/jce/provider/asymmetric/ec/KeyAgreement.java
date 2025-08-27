package org.bouncycastle.jce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.agreement.ECMQVBasicAgreement;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.crypto.agreement.kdf.ECDHKEKGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.interfaces.MQVPrivateKey;
import org.bouncycastle.jce.interfaces.MQVPublicKey;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement.class */
public class KeyAgreement extends KeyAgreementSpi {
    private static final X9IntegerConverter converter = new X9IntegerConverter();
    private static final Hashtable algorithms = new Hashtable();
    private String kaAlgorithm;
    private BigInteger result;
    private ECDomainParameters parameters;
    private BasicAgreement agreement;
    private DerivationFunction kdf;

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement$DH.class */
    public static class DH extends KeyAgreement {
        public DH() {
            super("ECDH", new ECDHBasicAgreement(), null);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement$DHC.class */
    public static class DHC extends KeyAgreement {
        public DHC() {
            super("ECDHC", new ECDHCBasicAgreement(), null);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement$DHwithSHA1KDF.class */
    public static class DHwithSHA1KDF extends KeyAgreement {
        public DHwithSHA1KDF() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement$MQV.class */
    public static class MQV extends KeyAgreement {
        public MQV() {
            super("ECMQV", new ECMQVBasicAgreement(), null);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/KeyAgreement$MQVwithSHA1KDF.class */
    public static class MQVwithSHA1KDF extends KeyAgreement {
        public MQVwithSHA1KDF() {
            super("ECMQVwithSHA1KDF", new ECMQVBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        return converter.integerToBytes(bigInteger, converter.getByteLength(this.parameters.getG().getX()));
    }

    protected KeyAgreement(String str, BasicAgreement basicAgreement, DerivationFunction derivationFunction) {
        this.kaAlgorithm = str;
        this.agreement = basicAgreement;
        this.kdf = derivationFunction;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected Key engineDoPhase(Key key, boolean z) throws IllegalStateException, InvalidKeyException {
        CipherParameters cipherParametersGeneratePublicKeyParameter;
        if (this.parameters == null) {
            throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
        }
        if (!z) {
            throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
        }
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (!(key instanceof MQVPublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPublicKey.class) + " for doPhase");
            }
            MQVPublicKey mQVPublicKey = (MQVPublicKey) key;
            cipherParametersGeneratePublicKeyParameter = new MQVPublicParameters((ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPublicKey.getStaticKey()), (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPublicKey.getEphemeralKey()));
        } else {
            if (!(key instanceof ECPublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPublicKey.class) + " for doPhase");
            }
            cipherParametersGeneratePublicKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey) key);
        }
        this.result = this.agreement.calculateAgreement(cipherParametersGeneratePublicKeyParameter);
        return null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.kdf != null) {
            throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
        }
        return bigIntToBytes(this.result);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i) throws IllegalStateException, ShortBufferException {
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        if (bArr.length - i < bArrEngineGenerateSecret.length) {
            throw new ShortBufferException(this.kaAlgorithm + " key agreement: need " + bArrEngineGenerateSecret.length + " bytes");
        }
        System.arraycopy(bArrEngineGenerateSecret, 0, bArr, i, bArrEngineGenerateSecret.length);
        return bArrEngineGenerateSecret.length;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) throws DataLengthException, NoSuchAlgorithmException, IllegalArgumentException {
        byte[] bArrBigIntToBytes = bigIntToBytes(this.result);
        if (this.kdf != null) {
            if (!algorithms.containsKey(str)) {
                throw new NoSuchAlgorithmException("unknown algorithm encountered: " + str);
            }
            int iIntValue = ((Integer) algorithms.get(str)).intValue();
            DHKDFParameters dHKDFParameters = new DHKDFParameters(new DERObjectIdentifier(str), iIntValue, bArrBigIntToBytes);
            byte[] bArr = new byte[iIntValue / 8];
            this.kdf.init(dHKDFParameters);
            this.kdf.generateBytes(bArr, 0, bArr.length);
            bArrBigIntToBytes = bArr;
        }
        return new SecretKeySpec(bArrBigIntToBytes, str);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        initFromKey(key);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        initFromKey(key);
    }

    private void initFromKey(Key key) throws InvalidKeyException {
        if (!(this.agreement instanceof ECMQVBasicAgreement)) {
            if (!(key instanceof ECPrivateKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPrivateKey.class) + " for initialisation");
            }
            ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter((PrivateKey) key);
            this.parameters = eCPrivateKeyParameters.getParameters();
            this.agreement.init(eCPrivateKeyParameters);
            return;
        }
        if (!(key instanceof MQVPrivateKey)) {
            throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPrivateKey.class) + " for initialisation");
        }
        MQVPrivateKey mQVPrivateKey = (MQVPrivateKey) key;
        ECPrivateKeyParameters eCPrivateKeyParameters2 = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(mQVPrivateKey.getStaticPrivateKey());
        ECPrivateKeyParameters eCPrivateKeyParameters3 = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(mQVPrivateKey.getEphemeralPrivateKey());
        ECPublicKeyParameters eCPublicKeyParameters = null;
        if (mQVPrivateKey.getEphemeralPublicKey() != null) {
            eCPublicKeyParameters = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(mQVPrivateKey.getEphemeralPublicKey());
        }
        MQVPrivateParameters mQVPrivateParameters = new MQVPrivateParameters(eCPrivateKeyParameters2, eCPrivateKeyParameters3, eCPublicKeyParameters);
        this.parameters = eCPrivateKeyParameters2.getParameters();
        this.agreement.init(mQVPrivateParameters);
    }

    private static String getSimpleName(Class cls) {
        String name = cls.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }

    static {
        Integer num = new Integer(128);
        Integer num2 = new Integer(192);
        Integer num3 = new Integer(256);
        algorithms.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), num);
        algorithms.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), num2);
        algorithms.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), num3);
        algorithms.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), num);
        algorithms.put(NISTObjectIdentifiers.id_aes192_wrap.getId(), num2);
        algorithms.put(NISTObjectIdentifiers.id_aes256_wrap.getId(), num3);
        algorithms.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), num2);
    }
}
