package org.bouncycastle.openssl;

import com.moredian.onpremise.core.utils.RSAUtils;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMUtilities.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMUtilities.class */
final class PEMUtilities {
    private static final Map KEYSIZES = new HashMap();
    private static final Set PKCS5_SCHEME_1 = new HashSet();
    private static final Set PKCS5_SCHEME_2 = new HashSet();

    PEMUtilities() {
    }

    static int getKeySize(String str) {
        if (KEYSIZES.containsKey(str)) {
            return ((Integer) KEYSIZES.get(str)).intValue();
        }
        throw new IllegalStateException("no key size for algorithm: " + str);
    }

    static boolean isPKCS5Scheme1(DERObjectIdentifier dERObjectIdentifier) {
        return PKCS5_SCHEME_1.contains(dERObjectIdentifier);
    }

    static boolean isPKCS5Scheme2(DERObjectIdentifier dERObjectIdentifier) {
        return PKCS5_SCHEME_2.contains(dERObjectIdentifier);
    }

    static boolean isPKCS12(DERObjectIdentifier dERObjectIdentifier) {
        return dERObjectIdentifier.getId().startsWith(PKCSObjectIdentifiers.pkcs_12PbeIds.getId());
    }

    static SecretKey generateSecretKeyForPKCS5Scheme2(String str, char[] cArr, byte[] bArr, int i) {
        PKCS5S2ParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator();
        pKCS5S2ParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(cArr), bArr, i);
        return new SecretKeySpec(((KeyParameter) pKCS5S2ParametersGenerator.generateDerivedParameters(getKeySize(str))).getKey(), str);
    }

    static byte[] crypt(boolean z, String str, byte[] bArr, char[] cArr, String str2, byte[] bArr2) throws IOException {
        Provider provider = null;
        if (str != null) {
            provider = Security.getProvider(str);
            if (provider == null) {
                throw new EncryptionException("cannot find provider: " + str);
            }
        }
        return crypt(z, provider, bArr, cArr, str2, bArr2);
    }

    static byte[] crypt(boolean z, Provider provider, byte[] bArr, char[] cArr, String str, byte[] bArr2) throws IOException {
        String str2;
        int i;
        SecretKey key;
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        String str3 = "CBC";
        String str4 = "PKCS5Padding";
        if (str.endsWith("-CFB")) {
            str3 = "CFB";
            str4 = "NoPadding";
        }
        if (str.endsWith("-ECB") || "DES-EDE".equals(str) || "DES-EDE3".equals(str)) {
            str3 = "ECB";
            ivParameterSpec = null;
        }
        if (str.endsWith("-OFB")) {
            str3 = "OFB";
            str4 = "NoPadding";
        }
        if (str.startsWith("DES-EDE")) {
            str2 = "DESede";
            key = getKey(cArr, str2, 24, bArr2, !str.startsWith("DES-EDE3"));
        } else if (str.startsWith("DES-")) {
            str2 = "DES";
            key = getKey(cArr, str2, 8, bArr2);
        } else if (str.startsWith("BF-")) {
            str2 = "Blowfish";
            key = getKey(cArr, str2, 16, bArr2);
        } else if (str.startsWith("RC2-")) {
            str2 = "RC2";
            int i2 = 128;
            if (str.startsWith("RC2-40-")) {
                i2 = 40;
            } else if (str.startsWith("RC2-64-")) {
                i2 = 64;
            }
            key = getKey(cArr, str2, i2 / 8, bArr2);
            ivParameterSpec = ivParameterSpec == null ? new RC2ParameterSpec(i2) : new RC2ParameterSpec(i2, bArr2);
        } else {
            if (!str.startsWith("AES-")) {
                throw new EncryptionException("unknown encryption with private key");
            }
            str2 = RSAUtils.AES_KEY_ALGORITHM;
            byte[] bArr3 = bArr2;
            if (bArr3.length > 8) {
                bArr3 = new byte[8];
                System.arraycopy(bArr2, 0, bArr3, 0, 8);
            }
            if (str.startsWith("AES-128-")) {
                i = 128;
            } else if (str.startsWith("AES-192-")) {
                i = 192;
            } else {
                if (!str.startsWith("AES-256-")) {
                    throw new EncryptionException("unknown AES encryption with private key");
                }
                i = 256;
            }
            key = getKey(cArr, RSAUtils.AES_KEY_ALGORITHM, i / 8, bArr3);
        }
        try {
            Cipher cipher = Cipher.getInstance(str2 + "/" + str3 + "/" + str4, provider);
            int i3 = z ? 1 : 2;
            if (ivParameterSpec == null) {
                cipher.init(i3, key);
            } else {
                cipher.init(i3, key, ivParameterSpec);
            }
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            throw new EncryptionException("exception using cipher - please check password and data.", e);
        }
    }

    private static SecretKey getKey(char[] cArr, String str, int i, byte[] bArr) {
        return getKey(cArr, str, i, bArr, false);
    }

    private static SecretKey getKey(char[] cArr, String str, int i, byte[] bArr, boolean z) {
        OpenSSLPBEParametersGenerator openSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
        openSSLPBEParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(cArr), bArr);
        byte[] key = ((KeyParameter) openSSLPBEParametersGenerator.generateDerivedParameters(i * 8)).getKey();
        if (z && key.length >= 24) {
            System.arraycopy(key, 0, key, 16, 8);
        }
        return new SecretKeySpec(key, str);
    }

    static {
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC);
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC);
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC);
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC);
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC);
        PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC);
        PKCS5_SCHEME_2.add(PKCSObjectIdentifiers.id_PBES2);
        PKCS5_SCHEME_2.add(PKCSObjectIdentifiers.des_EDE3_CBC);
        PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes128_CBC);
        PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes192_CBC);
        PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes256_CBC);
        KEYSIZES.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), new Integer(192));
        KEYSIZES.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), new Integer(128));
        KEYSIZES.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), new Integer(192));
        KEYSIZES.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), new Integer(256));
    }
}
