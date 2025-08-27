package org.apache.poi.poifs.crypt;

import com.moredian.onpremise.core.utils.RSAUtils;
import org.apache.poi.EncryptedDocumentException;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/CipherProvider.class */
public enum CipherProvider {
    rc4("RC4", 1, "Microsoft Base Cryptographic Provider v1.0"),
    aes(RSAUtils.AES_KEY_ALGORITHM, 24, "Microsoft Enhanced RSA and AES Cryptographic Provider");

    public final String jceId;
    public final int ecmaId;
    public final String cipherProviderName;

    public static CipherProvider fromEcmaId(int ecmaId) {
        CipherProvider[] arr$ = values();
        for (CipherProvider cp : arr$) {
            if (cp.ecmaId == ecmaId) {
                return cp;
            }
        }
        throw new EncryptedDocumentException("cipher provider not found");
    }

    CipherProvider(String jceId, int ecmaId, String cipherProviderName) {
        this.jceId = jceId;
        this.ecmaId = ecmaId;
        this.cipherProviderName = cipherProviderName;
    }
}
