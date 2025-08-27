package org.bouncycastle.jce.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.PBE;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher.class */
public class JCEStreamCipher extends WrapCipherSpi implements PBE {
    private StreamCipher cipher;
    private ParametersWithIV ivParam;
    private int ivLength;
    private Class[] availableSpecs = {RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class};
    private PBEParameterSpec pbeSpec = null;
    private String pbeAlgorithm = null;

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Blowfish_CFB8.class */
    public static class Blowfish_CFB8 extends JCEStreamCipher {
        public Blowfish_CFB8() {
            super(new CFBBlockCipher(new BlowfishEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Blowfish_OFB8.class */
    public static class Blowfish_OFB8 extends JCEStreamCipher {
        public Blowfish_OFB8() {
            super(new OFBBlockCipher(new BlowfishEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$DES_CFB8.class */
    public static class DES_CFB8 extends JCEStreamCipher {
        public DES_CFB8() {
            super(new CFBBlockCipher(new DESEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$DES_OFB8.class */
    public static class DES_OFB8 extends JCEStreamCipher {
        public DES_OFB8() {
            super(new OFBBlockCipher(new DESEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$DESede_CFB8.class */
    public static class DESede_CFB8 extends JCEStreamCipher {
        public DESede_CFB8() {
            super(new CFBBlockCipher(new DESedeEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$DESede_OFB8.class */
    public static class DESede_OFB8 extends JCEStreamCipher {
        public DESede_OFB8() {
            super(new OFBBlockCipher(new DESedeEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$PBEWithSHAAnd128BitRC4.class */
    public static class PBEWithSHAAnd128BitRC4 extends JCEStreamCipher {
        public PBEWithSHAAnd128BitRC4() {
            super(new RC4Engine(), 0);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$PBEWithSHAAnd40BitRC4.class */
    public static class PBEWithSHAAnd40BitRC4 extends JCEStreamCipher {
        public PBEWithSHAAnd40BitRC4() {
            super(new RC4Engine(), 0);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Skipjack_CFB8.class */
    public static class Skipjack_CFB8 extends JCEStreamCipher {
        public Skipjack_CFB8() {
            super(new CFBBlockCipher(new SkipjackEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Skipjack_OFB8.class */
    public static class Skipjack_OFB8 extends JCEStreamCipher {
        public Skipjack_OFB8() {
            super(new OFBBlockCipher(new SkipjackEngine(), 8), 64);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Twofish_CFB8.class */
    public static class Twofish_CFB8 extends JCEStreamCipher {
        public Twofish_CFB8() {
            super(new CFBBlockCipher(new TwofishEngine(), 8), 128);
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/JCEStreamCipher$Twofish_OFB8.class */
    public static class Twofish_OFB8 extends JCEStreamCipher {
        public Twofish_OFB8() {
            super(new OFBBlockCipher(new TwofishEngine(), 8), 128);
        }
    }

    protected JCEStreamCipher(StreamCipher streamCipher, int i) {
        this.ivLength = 0;
        this.cipher = streamCipher;
        this.ivLength = i;
    }

    protected JCEStreamCipher(BlockCipher blockCipher, int i) {
        this.ivLength = 0;
        this.ivLength = i;
        this.cipher = new StreamBlockCipher(blockCipher);
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        if (this.ivParam != null) {
            return this.ivParam.getIV();
        }
        return null;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) {
        return key.getEncoded().length * 8;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i) {
        return i;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchProviderException {
        if (this.engineParams != null || this.pbeSpec == null) {
            return this.engineParams;
        }
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(this.pbeAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            algorithmParameters.init(this.pbeSpec);
            return algorithmParameters;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected void engineSetMode(String str) {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new IllegalArgumentException("can't support mode " + str);
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (!str.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException("Padding " + str + " unknown.");
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, IllegalArgumentException, InvalidAlgorithmParameterException {
        CipherParameters parametersWithIV;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
        }
        if (key instanceof JCEPBEKey) {
            JCEPBEKey jCEPBEKey = (JCEPBEKey) key;
            if (jCEPBEKey.getOID() != null) {
                this.pbeAlgorithm = jCEPBEKey.getOID().getId();
            } else {
                this.pbeAlgorithm = jCEPBEKey.getAlgorithm();
            }
            if (jCEPBEKey.getParam() != null) {
                parametersWithIV = jCEPBEKey.getParam();
                this.pbeSpec = new PBEParameterSpec(jCEPBEKey.getSalt(), jCEPBEKey.getIterationCount());
            } else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                parametersWithIV = PBE.Util.makePBEParameters(jCEPBEKey, algorithmParameterSpec, this.cipher.getAlgorithmName());
                this.pbeSpec = (PBEParameterSpec) algorithmParameterSpec;
            }
            if (jCEPBEKey.getIvSize() != 0) {
                this.ivParam = (ParametersWithIV) parametersWithIV;
            }
        } else if (algorithmParameterSpec == null) {
            parametersWithIV = new KeyParameter(key.getEncoded());
        } else {
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
                throw new IllegalArgumentException("unknown parameter type.");
            }
            parametersWithIV = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec) algorithmParameterSpec).getIV());
            this.ivParam = (ParametersWithIV) parametersWithIV;
        }
        if (this.ivLength != 0 && !(parametersWithIV instanceof ParametersWithIV)) {
            SecureRandom secureRandom2 = secureRandom;
            if (secureRandom2 == null) {
                secureRandom2 = new SecureRandom();
            }
            if (i != 1 && i != 3) {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
            byte[] bArr = new byte[this.ivLength];
            secureRandom2.nextBytes(bArr);
            parametersWithIV = new ParametersWithIV(parametersWithIV, bArr);
            this.ivParam = (ParametersWithIV) parametersWithIV;
        }
        switch (i) {
            case 1:
            case 3:
                this.cipher.init(true, parametersWithIV);
                return;
            case 2:
            case 4:
                this.cipher.init(false, parametersWithIV);
                return;
            default:
                System.out.println("eeek!");
                return;
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidParameterSpecException, InvalidKeyException, IllegalArgumentException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            for (int i2 = 0; i2 != this.availableSpecs.length; i2++) {
                try {
                    parameterSpec = algorithmParameters.getParameterSpec(this.availableSpecs[i2]);
                    break;
                } catch (Exception e) {
                }
            }
            if (parameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        }
        engineInit(i, key, parameterSpec, secureRandom);
        this.engineParams = algorithmParameters;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException, IllegalArgumentException {
        try {
            engineInit(i, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidKeyException(e.getMessage());
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i, int i2) throws DataLengthException {
        byte[] bArr2 = new byte[i2];
        this.cipher.processBytes(bArr, i, i2, bArr2, 0);
        return bArr2;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException {
        try {
            this.cipher.processBytes(bArr, i, i2, bArr2, i3);
            return i2;
        } catch (DataLengthException e) {
            throw new ShortBufferException(e.getMessage());
        }
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i, int i2) throws DataLengthException {
        if (i2 == 0) {
            this.cipher.reset();
            return new byte[0];
        }
        byte[] bArrEngineUpdate = engineUpdate(bArr, i, i2);
        this.cipher.reset();
        return bArrEngineUpdate;
    }

    @Override // org.bouncycastle.jce.provider.WrapCipherSpi, javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException {
        if (i2 != 0) {
            this.cipher.processBytes(bArr, i, i2, bArr2, i3);
        }
        this.cipher.reset();
        return i2;
    }
}
