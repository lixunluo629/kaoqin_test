package org.apache.poi.poifs.crypt.cryptoapi;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDocumentOutputStream.class */
class CryptoAPIDocumentOutputStream extends ByteArrayOutputStream {
    private final Cipher cipher;
    private final CryptoAPIEncryptor encryptor;
    private final byte[] oneByte = {0};

    public CryptoAPIDocumentOutputStream(CryptoAPIEncryptor encryptor) throws GeneralSecurityException {
        this.encryptor = encryptor;
        this.cipher = encryptor.initCipherForBlock(null, 0);
    }

    public byte[] getBuf() {
        return this.buf;
    }

    public void setSize(int count) {
        this.count = count;
    }

    public void setBlock(int block) throws GeneralSecurityException {
        this.encryptor.initCipherForBlock(this.cipher, block);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(int b) throws ShortBufferException {
        try {
            this.oneByte[0] = (byte) b;
            this.cipher.update(this.oneByte, 0, 1, this.oneByte, 0);
            super.write(this.oneByte);
        } catch (Exception e) {
            throw new EncryptedDocumentException(e);
        }
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws ShortBufferException {
        try {
            this.cipher.update(b, off, len, b, off);
            super.write(b, off, len);
        } catch (Exception e) {
            throw new EncryptedDocumentException(e);
        }
    }
}
