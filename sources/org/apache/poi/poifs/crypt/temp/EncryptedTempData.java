package org.apache.poi.poifs.crypt.temp;

import io.netty.handler.codec.http.multipart.DiskFileUpload;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.TempFile;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/temp/EncryptedTempData.class */
public class EncryptedTempData {
    private static POILogger LOG = POILogFactory.getLogger((Class<?>) EncryptedTempData.class);
    private static final CipherAlgorithm cipherAlgorithm = CipherAlgorithm.aes128;
    private final SecretKeySpec skeySpec;
    private final byte[] ivBytes;
    private final File tempFile;

    public EncryptedTempData() throws IOException {
        SecureRandom sr = new SecureRandom();
        this.ivBytes = new byte[16];
        byte[] keyBytes = new byte[16];
        sr.nextBytes(this.ivBytes);
        sr.nextBytes(keyBytes);
        this.skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
        this.tempFile = TempFile.createTempFile("poi-temp-data", DiskFileUpload.postfix);
    }

    public OutputStream getOutputStream() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Cipher ciEnc = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 1, null);
        return new CipherOutputStream(new FileOutputStream(this.tempFile), ciEnc);
    }

    public InputStream getInputStream() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Cipher ciDec = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 2, null);
        return new CipherInputStream(new FileInputStream(this.tempFile), ciDec);
    }

    public void dispose() {
        if (!this.tempFile.delete()) {
            LOG.log(5, this.tempFile.getAbsolutePath() + " can't be removed (or was already removed.");
        }
    }
}
