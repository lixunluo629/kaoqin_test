package com.itextpdf.kernel.crypto.securityhandler;

import java.io.Serializable;
import java.security.cert.Certificate;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/securityhandler/PublicKeyRecipient.class */
public class PublicKeyRecipient implements Serializable {
    private static final long serialVersionUID = -6985649182567287907L;
    private Certificate certificate;
    private int permission;
    protected byte[] cms = null;

    public PublicKeyRecipient(Certificate certificate, int permission) {
        this.certificate = null;
        this.permission = 0;
        this.certificate = certificate;
        this.permission = permission;
    }

    public Certificate getCertificate() {
        return this.certificate;
    }

    public int getPermission() {
        return this.permission;
    }

    protected void setCms(byte[] cms) {
        this.cms = cms;
    }

    protected byte[] getCms() {
        return this.cms;
    }
}
