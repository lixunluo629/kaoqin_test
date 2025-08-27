package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.security.IExternalDecryptionProcess;
import java.io.Serializable;
import java.security.Key;
import java.security.cert.Certificate;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/ReaderProperties.class */
public class ReaderProperties implements Serializable {
    private static final long serialVersionUID = 5569118801793215916L;
    protected byte[] password;
    protected Key certificateKey;
    protected Certificate certificate;
    protected String certificateKeyProvider;
    protected IExternalDecryptionProcess externalDecryptionProcess;
    protected MemoryLimitsAwareHandler memoryLimitsAwareHandler;

    public ReaderProperties setPassword(byte[] password) {
        clearEncryptionParams();
        this.password = password;
        return this;
    }

    public ReaderProperties setPublicKeySecurityParams(Certificate certificate, Key certificateKey, String certificateKeyProvider, IExternalDecryptionProcess externalDecryptionProcess) {
        clearEncryptionParams();
        this.certificate = certificate;
        this.certificateKey = certificateKey;
        this.certificateKeyProvider = certificateKeyProvider;
        this.externalDecryptionProcess = externalDecryptionProcess;
        return this;
    }

    public ReaderProperties setPublicKeySecurityParams(Certificate certificate, IExternalDecryptionProcess externalDecryptionProcess) {
        clearEncryptionParams();
        this.certificate = certificate;
        this.externalDecryptionProcess = externalDecryptionProcess;
        return this;
    }

    private void clearEncryptionParams() {
        this.password = null;
        this.certificate = null;
        this.certificateKey = null;
        this.certificateKeyProvider = null;
        this.externalDecryptionProcess = null;
    }

    public ReaderProperties setMemoryLimitsAwareHandler(MemoryLimitsAwareHandler memoryLimitsAwareHandler) {
        this.memoryLimitsAwareHandler = memoryLimitsAwareHandler;
        return this;
    }
}
