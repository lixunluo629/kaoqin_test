package com.itextpdf.kernel.crypto;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/AesDecryptor.class */
public class AesDecryptor implements IDecryptor {
    private AESCipher cipher;
    private byte[] key;
    private boolean initiated;
    private byte[] iv = new byte[16];
    private int ivptr;

    public AesDecryptor(byte[] key, int off, int len) {
        this.key = new byte[len];
        System.arraycopy(key, off, this.key, 0, len);
    }

    @Override // com.itextpdf.kernel.crypto.IDecryptor
    public byte[] update(byte[] b, int off, int len) {
        if (this.initiated) {
            return this.cipher.update(b, off, len);
        }
        int left = Math.min(this.iv.length - this.ivptr, len);
        System.arraycopy(b, off, this.iv, this.ivptr, left);
        int off2 = off + left;
        int len2 = len - left;
        this.ivptr += left;
        if (this.ivptr == this.iv.length) {
            this.cipher = new AESCipher(false, this.key, this.iv);
            this.initiated = true;
            if (len2 > 0) {
                return this.cipher.update(b, off2, len2);
            }
            return null;
        }
        return null;
    }

    @Override // com.itextpdf.kernel.crypto.IDecryptor
    public byte[] finish() {
        if (this.cipher != null) {
            return this.cipher.doFinal();
        }
        return null;
    }
}
