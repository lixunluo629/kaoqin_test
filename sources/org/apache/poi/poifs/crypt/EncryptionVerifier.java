package org.apache.poi.poifs.crypt;

import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/EncryptionVerifier.class */
public abstract class EncryptionVerifier implements Cloneable {
    private byte[] salt;
    private byte[] encryptedVerifier;
    private byte[] encryptedVerifierHash;
    private byte[] encryptedKey;
    private int spinCount;
    private CipherAlgorithm cipherAlgorithm;
    private ChainingMode chainingMode;
    private HashAlgorithm hashAlgorithm;

    protected EncryptionVerifier() {
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public byte[] getEncryptedVerifier() {
        return this.encryptedVerifier;
    }

    public byte[] getEncryptedVerifierHash() {
        return this.encryptedVerifierHash;
    }

    public int getSpinCount() {
        return this.spinCount;
    }

    @Removal(version = "3.18")
    public int getCipherMode() {
        return this.chainingMode.ecmaId;
    }

    public int getAlgorithm() {
        return this.cipherAlgorithm.ecmaId;
    }

    public byte[] getEncryptedKey() {
        return this.encryptedKey;
    }

    public CipherAlgorithm getCipherAlgorithm() {
        return this.cipherAlgorithm;
    }

    public HashAlgorithm getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public ChainingMode getChainingMode() {
        return this.chainingMode;
    }

    protected void setSalt(byte[] salt) {
        this.salt = salt == null ? null : (byte[]) salt.clone();
    }

    protected void setEncryptedVerifier(byte[] encryptedVerifier) {
        this.encryptedVerifier = encryptedVerifier == null ? null : (byte[]) encryptedVerifier.clone();
    }

    protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash) {
        this.encryptedVerifierHash = encryptedVerifierHash == null ? null : (byte[]) encryptedVerifierHash.clone();
    }

    protected void setEncryptedKey(byte[] encryptedKey) {
        this.encryptedKey = encryptedKey == null ? null : (byte[]) encryptedKey.clone();
    }

    protected void setSpinCount(int spinCount) {
        this.spinCount = spinCount;
    }

    protected void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }

    protected void setChainingMode(ChainingMode chainingMode) {
        this.chainingMode = chainingMode;
    }

    protected void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public EncryptionVerifier mo3520clone() throws CloneNotSupportedException {
        EncryptionVerifier other = (EncryptionVerifier) super.clone();
        other.salt = this.salt == null ? null : (byte[]) this.salt.clone();
        other.encryptedVerifier = this.encryptedVerifier == null ? null : (byte[]) this.encryptedVerifier.clone();
        other.encryptedVerifierHash = this.encryptedVerifierHash == null ? null : (byte[]) this.encryptedVerifierHash.clone();
        other.encryptedKey = this.encryptedKey == null ? null : (byte[]) this.encryptedKey.clone();
        return other;
    }
}
