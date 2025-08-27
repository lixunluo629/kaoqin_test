package com.itextpdf.kernel.pdf;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.Certificate;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/EncryptionProperties.class */
public class EncryptionProperties implements Serializable {
    private static final long serialVersionUID = 3926570647944137843L;
    protected int encryptionAlgorithm;
    protected byte[] userPassword;
    protected byte[] ownerPassword;
    protected int standardEncryptPermissions;
    protected Certificate[] publicCertificates;
    protected int[] publicKeyEncryptPermissions;

    public EncryptionProperties setStandardEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionAlgorithm) {
        clearEncryption();
        this.userPassword = userPassword;
        if (ownerPassword != null) {
            this.ownerPassword = ownerPassword;
        } else {
            this.ownerPassword = new byte[16];
            randomBytes(this.ownerPassword);
        }
        this.standardEncryptPermissions = permissions;
        this.encryptionAlgorithm = encryptionAlgorithm;
        return this;
    }

    public EncryptionProperties setPublicKeyEncryption(Certificate[] certs, int[] permissions, int encryptionAlgorithm) {
        clearEncryption();
        this.publicCertificates = certs;
        this.publicKeyEncryptPermissions = permissions;
        this.encryptionAlgorithm = encryptionAlgorithm;
        return this;
    }

    boolean isStandardEncryptionUsed() {
        return this.ownerPassword != null;
    }

    boolean isPublicKeyEncryptionUsed() {
        return this.publicCertificates != null;
    }

    private void clearEncryption() {
        this.publicCertificates = null;
        this.publicKeyEncryptPermissions = null;
        this.userPassword = null;
        this.ownerPassword = null;
    }

    private static void randomBytes(byte[] bytes) {
        new SecureRandom().nextBytes(bytes);
    }
}
