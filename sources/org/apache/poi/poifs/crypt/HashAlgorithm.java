package org.apache.poi.poifs.crypt;

import com.drew.metadata.exif.makernotes.PanasonicMakernoteDirectory;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.poi.EncryptedDocumentException;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/HashAlgorithm.class */
public enum HashAlgorithm {
    none("", 0, "", 0, "", false),
    sha1("SHA-1", PanasonicMakernoteDirectory.TAG_WB_RED_LEVEL, "SHA1", 20, "HmacSHA1", false),
    sha256("SHA-256", 32780, "SHA256", 32, "HmacSHA256", false),
    sha384("SHA-384", 32781, "SHA384", 48, "HmacSHA384", false),
    sha512("SHA-512", 32782, "SHA512", 64, "HmacSHA512", false),
    md5(MessageDigestAlgorithms.MD5, -1, MessageDigestAlgorithms.MD5, 16, "HmacMD5", false),
    md2(MessageDigestAlgorithms.MD2, -1, MessageDigestAlgorithms.MD2, 16, "Hmac-MD2", true),
    md4("MD4", -1, "MD4", 16, "Hmac-MD4", true),
    ripemd128("RipeMD128", -1, "RIPEMD-128", 16, "HMac-RipeMD128", true),
    ripemd160("RipeMD160", -1, "RIPEMD-160", 20, "HMac-RipeMD160", true),
    whirlpool("Whirlpool", -1, "WHIRLPOOL", 64, "HMac-Whirlpool", true),
    sha224(McElieceCCA2KeyGenParameterSpec.SHA224, -1, "SHA224", 28, "HmacSHA224", true);

    public final String jceId;
    public final int ecmaId;
    public final String ecmaString;
    public final int hashSize;
    public final String jceHmacId;
    public final boolean needsBouncyCastle;

    HashAlgorithm(String jceId, int ecmaId, String ecmaString, int hashSize, String jceHmacId, boolean needsBouncyCastle) {
        this.jceId = jceId;
        this.ecmaId = ecmaId;
        this.ecmaString = ecmaString;
        this.hashSize = hashSize;
        this.jceHmacId = jceHmacId;
        this.needsBouncyCastle = needsBouncyCastle;
    }

    public static HashAlgorithm fromEcmaId(int ecmaId) {
        HashAlgorithm[] arr$ = values();
        for (HashAlgorithm ha : arr$) {
            if (ha.ecmaId == ecmaId) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }

    public static HashAlgorithm fromEcmaId(String ecmaString) {
        HashAlgorithm[] arr$ = values();
        for (HashAlgorithm ha : arr$) {
            if (ha.ecmaString.equals(ecmaString)) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }

    public static HashAlgorithm fromString(String string) {
        HashAlgorithm[] arr$ = values();
        for (HashAlgorithm ha : arr$) {
            if (ha.ecmaString.equalsIgnoreCase(string) || ha.jceId.equalsIgnoreCase(string)) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }
}
