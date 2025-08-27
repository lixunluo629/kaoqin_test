package org.apache.poi.poifs.crypt.binaryrc4;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/binaryrc4/BinaryRC4EncryptionVerifier.class */
public class BinaryRC4EncryptionVerifier extends EncryptionVerifier implements EncryptionRecord, Cloneable {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BinaryRC4EncryptionVerifier.class.desiredAssertionStatus();
    }

    protected BinaryRC4EncryptionVerifier() {
        setSpinCount(-1);
        setCipherAlgorithm(CipherAlgorithm.rc4);
        setChainingMode(null);
        setEncryptedKey(null);
        setHashAlgorithm(HashAlgorithm.md5);
    }

    protected BinaryRC4EncryptionVerifier(LittleEndianInput is) {
        byte[] salt = new byte[16];
        is.readFully(salt);
        setSalt(salt);
        byte[] encryptedVerifier = new byte[16];
        is.readFully(encryptedVerifier);
        setEncryptedVerifier(encryptedVerifier);
        byte[] encryptedVerifierHash = new byte[16];
        is.readFully(encryptedVerifierHash);
        setEncryptedVerifierHash(encryptedVerifierHash);
        setSpinCount(-1);
        setCipherAlgorithm(CipherAlgorithm.rc4);
        setChainingMode(null);
        setEncryptedKey(null);
        setHashAlgorithm(HashAlgorithm.md5);
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionVerifier
    protected void setSalt(byte[] salt) {
        if (salt == null || salt.length != 16) {
            throw new EncryptedDocumentException("invalid verifier salt");
        }
        super.setSalt(salt);
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionVerifier
    protected void setEncryptedVerifier(byte[] encryptedVerifier) {
        super.setEncryptedVerifier(encryptedVerifier);
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionVerifier
    protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash) {
        super.setEncryptedVerifierHash(encryptedVerifierHash);
    }

    @Override // org.apache.poi.poifs.crypt.standard.EncryptionRecord
    public void write(LittleEndianByteArrayOutputStream bos) {
        byte[] salt = getSalt();
        if (!$assertionsDisabled && salt.length != 16) {
            throw new AssertionError();
        }
        bos.write(salt);
        byte[] encryptedVerifier = getEncryptedVerifier();
        if (!$assertionsDisabled && encryptedVerifier.length != 16) {
            throw new AssertionError();
        }
        bos.write(encryptedVerifier);
        byte[] encryptedVerifierHash = getEncryptedVerifierHash();
        if (!$assertionsDisabled && encryptedVerifierHash.length != 16) {
            throw new AssertionError();
        }
        bos.write(encryptedVerifierHash);
    }

    @Override // org.apache.poi.poifs.crypt.EncryptionVerifier
    /* renamed from: clone */
    public BinaryRC4EncryptionVerifier mo3520clone() throws CloneNotSupportedException {
        return (BinaryRC4EncryptionVerifier) super.mo3520clone();
    }
}
