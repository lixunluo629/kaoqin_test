package com.itextpdf.kernel.pdf;

import java.io.Serializable;
import java.security.cert.Certificate;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/WriterProperties.class */
public class WriterProperties implements Serializable {
    private static final long serialVersionUID = -8692165914703604764L;
    protected boolean addXmpMetadata;
    protected PdfVersion pdfVersion;
    protected PdfString initialDocumentId;
    protected PdfString modifiedDocumentId;
    protected boolean smartMode = false;
    protected boolean debugMode = false;
    protected boolean addUAXmpMetadata = false;
    protected int compressionLevel = -1;
    protected Boolean isFullCompression = null;
    protected EncryptionProperties encryptionProperties = new EncryptionProperties();

    public WriterProperties setPdfVersion(PdfVersion version) {
        this.pdfVersion = version;
        return this;
    }

    public WriterProperties useSmartMode() {
        this.smartMode = true;
        return this;
    }

    public WriterProperties addXmpMetadata() {
        this.addXmpMetadata = true;
        return this;
    }

    public WriterProperties setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
        return this;
    }

    public WriterProperties setFullCompressionMode(boolean fullCompressionMode) {
        this.isFullCompression = Boolean.valueOf(fullCompressionMode);
        return this;
    }

    public WriterProperties setStandardEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionAlgorithm) {
        this.encryptionProperties.setStandardEncryption(userPassword, ownerPassword, permissions, encryptionAlgorithm);
        return this;
    }

    public WriterProperties setPublicKeyEncryption(Certificate[] certs, int[] permissions, int encryptionAlgorithm) {
        this.encryptionProperties.setPublicKeyEncryption(certs, permissions, encryptionAlgorithm);
        return this;
    }

    public WriterProperties setInitialDocumentId(PdfString initialDocumentId) {
        this.initialDocumentId = initialDocumentId;
        return this;
    }

    public WriterProperties setModifiedDocumentId(PdfString modifiedDocumentId) {
        this.modifiedDocumentId = modifiedDocumentId;
        return this;
    }

    public WriterProperties useDebugMode() {
        this.debugMode = true;
        return this;
    }

    public WriterProperties addUAXmpMetadata() {
        this.addUAXmpMetadata = true;
        return addXmpMetadata();
    }

    boolean isStandardEncryptionUsed() {
        return this.encryptionProperties.isStandardEncryptionUsed();
    }

    boolean isPublicKeyEncryptionUsed() {
        return this.encryptionProperties.isPublicKeyEncryptionUsed();
    }
}
