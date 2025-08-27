package org.bouncycastle.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/PBE.class */
public interface PBE {
    public static final int MD5 = 0;
    public static final int SHA1 = 1;
    public static final int RIPEMD160 = 2;
    public static final int TIGER = 3;
    public static final int SHA256 = 4;
    public static final int MD2 = 5;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S2 = 1;
    public static final int PKCS12 = 2;
    public static final int OPENSSL = 3;

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/PBE$Util.class */
    public static class Util {
        private static PBEParametersGenerator makePBEGenerator(int i, int i2) {
            PBEParametersGenerator openSSLPBEParametersGenerator;
            if (i == 0) {
                switch (i2) {
                    case 0:
                        openSSLPBEParametersGenerator = new PKCS5S1ParametersGenerator(new MD5Digest());
                        break;
                    case 1:
                        openSSLPBEParametersGenerator = new PKCS5S1ParametersGenerator(new SHA1Digest());
                        break;
                    case 5:
                        openSSLPBEParametersGenerator = new PKCS5S1ParametersGenerator(new MD2Digest());
                        break;
                    default:
                        throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
                }
            } else if (i == 1) {
                openSSLPBEParametersGenerator = new PKCS5S2ParametersGenerator();
            } else if (i == 2) {
                switch (i2) {
                    case 0:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new MD5Digest());
                        break;
                    case 1:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
                        break;
                    case 2:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new RIPEMD160Digest());
                        break;
                    case 3:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new TigerDigest());
                        break;
                    case 4:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new SHA256Digest());
                        break;
                    case 5:
                        openSSLPBEParametersGenerator = new PKCS12ParametersGenerator(new MD2Digest());
                        break;
                    default:
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                }
            } else {
                openSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
            }
            return openSSLPBEParametersGenerator;
        }

        static CipherParameters makePBEParameters(JCEPBEKey jCEPBEKey, AlgorithmParameterSpec algorithmParameterSpec, String str) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGeneratorMakePBEGenerator = makePBEGenerator(jCEPBEKey.getType(), jCEPBEKey.getDigest());
            byte[] encoded = jCEPBEKey.getEncoded();
            if (jCEPBEKey.shouldTryWrongPKCS12()) {
                encoded = new byte[2];
            }
            pBEParametersGeneratorMakePBEGenerator.init(encoded, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParametersGenerateDerivedParameters = jCEPBEKey.getIvSize() != 0 ? pBEParametersGeneratorMakePBEGenerator.generateDerivedParameters(jCEPBEKey.getKeySize(), jCEPBEKey.getIvSize()) : pBEParametersGeneratorMakePBEGenerator.generateDerivedParameters(jCEPBEKey.getKeySize());
            if (str.startsWith("DES")) {
                if (cipherParametersGenerateDerivedParameters instanceof ParametersWithIV) {
                    DESParameters.setOddParity(((KeyParameter) ((ParametersWithIV) cipherParametersGenerateDerivedParameters).getParameters()).getKey());
                } else {
                    DESParameters.setOddParity(((KeyParameter) cipherParametersGenerateDerivedParameters).getKey());
                }
            }
            for (int i = 0; i != encoded.length; i++) {
                encoded[i] = 0;
            }
            return cipherParametersGenerateDerivedParameters;
        }

        static CipherParameters makePBEMacParameters(JCEPBEKey jCEPBEKey, AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGeneratorMakePBEGenerator = makePBEGenerator(jCEPBEKey.getType(), jCEPBEKey.getDigest());
            byte[] encoded = jCEPBEKey.getEncoded();
            if (jCEPBEKey.shouldTryWrongPKCS12()) {
                encoded = new byte[2];
            }
            pBEParametersGeneratorMakePBEGenerator.init(encoded, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParametersGenerateDerivedMacParameters = pBEParametersGeneratorMakePBEGenerator.generateDerivedMacParameters(jCEPBEKey.getKeySize());
            for (int i = 0; i != encoded.length; i++) {
                encoded[i] = 0;
            }
            return cipherParametersGenerateDerivedMacParameters;
        }

        static CipherParameters makePBEParameters(PBEKeySpec pBEKeySpec, int i, int i2, int i3, int i4) {
            PBEParametersGenerator pBEParametersGeneratorMakePBEGenerator = makePBEGenerator(i, i2);
            byte[] bArrPKCS12PasswordToBytes = i == 2 ? PBEParametersGenerator.PKCS12PasswordToBytes(pBEKeySpec.getPassword()) : PBEParametersGenerator.PKCS5PasswordToBytes(pBEKeySpec.getPassword());
            pBEParametersGeneratorMakePBEGenerator.init(bArrPKCS12PasswordToBytes, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters cipherParametersGenerateDerivedParameters = i4 != 0 ? pBEParametersGeneratorMakePBEGenerator.generateDerivedParameters(i3, i4) : pBEParametersGeneratorMakePBEGenerator.generateDerivedParameters(i3);
            for (int i5 = 0; i5 != bArrPKCS12PasswordToBytes.length; i5++) {
                bArrPKCS12PasswordToBytes[i5] = 0;
            }
            return cipherParametersGenerateDerivedParameters;
        }

        static CipherParameters makePBEMacParameters(PBEKeySpec pBEKeySpec, int i, int i2, int i3) {
            PBEParametersGenerator pBEParametersGeneratorMakePBEGenerator = makePBEGenerator(i, i2);
            byte[] bArrPKCS12PasswordToBytes = i == 2 ? PBEParametersGenerator.PKCS12PasswordToBytes(pBEKeySpec.getPassword()) : PBEParametersGenerator.PKCS5PasswordToBytes(pBEKeySpec.getPassword());
            pBEParametersGeneratorMakePBEGenerator.init(bArrPKCS12PasswordToBytes, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters cipherParametersGenerateDerivedMacParameters = pBEParametersGeneratorMakePBEGenerator.generateDerivedMacParameters(i3);
            for (int i4 = 0; i4 != bArrPKCS12PasswordToBytes.length; i4++) {
                bArrPKCS12PasswordToBytes[i4] = 0;
            }
            return cipherParametersGenerateDerivedMacParameters;
        }
    }
}
