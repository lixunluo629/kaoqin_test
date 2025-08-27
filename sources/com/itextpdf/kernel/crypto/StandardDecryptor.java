package com.itextpdf.kernel.crypto;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/StandardDecryptor.class */
public class StandardDecryptor implements IDecryptor {
    protected ARCFOUREncryption arcfour = new ARCFOUREncryption();

    public StandardDecryptor(byte[] key, int off, int len) {
        this.arcfour.prepareARCFOURKey(key, off, len);
    }

    @Override // com.itextpdf.kernel.crypto.IDecryptor
    public byte[] update(byte[] b, int off, int len) {
        byte[] b2 = new byte[len];
        this.arcfour.encryptARCFOUR(b, off, len, b2, 0);
        return b2;
    }

    @Override // com.itextpdf.kernel.crypto.IDecryptor
    public byte[] finish() {
        return null;
    }
}
