package org.apache.poi.poifs.crypt.temp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.xssf.streaming.SheetDataWriter;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/temp/SheetDataWriterWithDecorator.class */
public class SheetDataWriterWithDecorator extends SheetDataWriter {
    static final CipherAlgorithm cipherAlgorithm = CipherAlgorithm.aes128;
    SecretKeySpec skeySpec;
    byte[] ivBytes;

    void init() {
        if (this.skeySpec == null) {
            SecureRandom sr = new SecureRandom();
            this.ivBytes = new byte[16];
            byte[] keyBytes = new byte[16];
            sr.nextBytes(this.ivBytes);
            sr.nextBytes(keyBytes);
            this.skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
        }
    }

    @Override // org.apache.poi.xssf.streaming.SheetDataWriter
    protected OutputStream decorateOutputStream(FileOutputStream fos) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        init();
        Cipher ciEnc = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 1, "PKCS5Padding");
        return new CipherOutputStream(fos, ciEnc);
    }

    @Override // org.apache.poi.xssf.streaming.SheetDataWriter
    protected InputStream decorateInputStream(FileInputStream fis) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Cipher ciDec = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 2, "PKCS5Padding");
        return new CipherInputStream(fis, ciDec);
    }
}
