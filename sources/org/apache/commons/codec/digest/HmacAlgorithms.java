package org.apache.commons.codec.digest;

/* loaded from: commons-codec-1.10.jar:org/apache/commons/codec/digest/HmacAlgorithms.class */
public enum HmacAlgorithms {
    HMAC_MD5("HmacMD5"),
    HMAC_SHA_1("HmacSHA1"),
    HMAC_SHA_256("HmacSHA256"),
    HMAC_SHA_384("HmacSHA384"),
    HMAC_SHA_512("HmacSHA512");

    private final String algorithm;

    HmacAlgorithms(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.algorithm;
    }
}
