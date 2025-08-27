package com.itextpdf.kernel.crypto;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/IDecryptor.class */
public interface IDecryptor {
    byte[] update(byte[] bArr, int i, int i2);

    byte[] finish();
}
