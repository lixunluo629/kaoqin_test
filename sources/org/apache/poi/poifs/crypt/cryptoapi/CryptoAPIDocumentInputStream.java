package org.apache.poi.poifs.crypt.cryptoapi;

import java.io.ByteArrayInputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.util.Internal;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDocumentInputStream.class */
class CryptoAPIDocumentInputStream extends ByteArrayInputStream {
    private Cipher cipher;
    private final CryptoAPIDecryptor decryptor;
    private byte[] oneByte;

    public void seek(int newpos) {
        if (newpos > this.count) {
            throw new ArrayIndexOutOfBoundsException(newpos);
        }
        this.pos = newpos;
        this.mark = newpos;
    }

    public void setBlock(int block) throws GeneralSecurityException {
        this.cipher = this.decryptor.initCipherForBlock(this.cipher, block);
    }

    @Override // java.io.ByteArrayInputStream, java.io.InputStream
    public synchronized int read() throws ShortBufferException {
        int ch2 = super.read();
        if (ch2 == -1) {
            return -1;
        }
        this.oneByte[0] = (byte) ch2;
        try {
            this.cipher.update(this.oneByte, 0, 1, this.oneByte);
            return this.oneByte[0];
        } catch (ShortBufferException e) {
            throw new EncryptedDocumentException(e);
        }
    }

    @Override // java.io.ByteArrayInputStream, java.io.InputStream
    public synchronized int read(byte[] b, int off, int len) throws ShortBufferException {
        int readLen = super.read(b, off, len);
        if (readLen == -1) {
            return -1;
        }
        try {
            this.cipher.update(b, off, readLen, b, off);
            return readLen;
        } catch (ShortBufferException e) {
            throw new EncryptedDocumentException(e);
        }
    }

    public CryptoAPIDocumentInputStream(CryptoAPIDecryptor decryptor, byte[] buf) throws GeneralSecurityException {
        super(buf);
        this.oneByte = new byte[]{0};
        this.decryptor = decryptor;
        this.cipher = decryptor.initCipherForBlock(null, 0);
    }
}
