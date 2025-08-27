package org.apache.poi.poifs.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/Decryptor.class */
public abstract class Decryptor implements Cloneable {
    public static final String DEFAULT_PASSWORD = "VelvetSweatshop";
    public static final String DEFAULT_POIFS_ENTRY = "EncryptedPackage";
    protected EncryptionInfo encryptionInfo;
    private SecretKey secretKey;
    private byte[] verifier;
    private byte[] integrityHmacKey;
    private byte[] integrityHmacValue;

    public abstract InputStream getDataStream(DirectoryNode directoryNode) throws GeneralSecurityException, IOException;

    public abstract boolean verifyPassword(String str) throws GeneralSecurityException;

    public abstract long getLength();

    protected Decryptor() {
    }

    public InputStream getDataStream(InputStream stream, int size, int initialPos) throws GeneralSecurityException, IOException {
        throw new EncryptedDocumentException("this decryptor doesn't support reading from a stream");
    }

    public void setChunkSize(int chunkSize) {
        throw new EncryptedDocumentException("this decryptor doesn't support changing the chunk size");
    }

    public Cipher initCipherForBlock(Cipher cipher, int block) throws GeneralSecurityException {
        throw new EncryptedDocumentException("this decryptor doesn't support initCipherForBlock");
    }

    public static Decryptor getInstance(EncryptionInfo info) {
        Decryptor d = info.getDecryptor();
        if (d == null) {
            throw new EncryptedDocumentException("Unsupported version");
        }
        return d;
    }

    public InputStream getDataStream(NPOIFSFileSystem fs) throws GeneralSecurityException, IOException {
        return getDataStream(fs.getRoot());
    }

    public InputStream getDataStream(OPOIFSFileSystem fs) throws GeneralSecurityException, IOException {
        return getDataStream(fs.getRoot());
    }

    public InputStream getDataStream(POIFSFileSystem fs) throws GeneralSecurityException, IOException {
        return getDataStream(fs.getRoot());
    }

    public byte[] getVerifier() {
        return this.verifier;
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public byte[] getIntegrityHmacKey() {
        return this.integrityHmacKey;
    }

    public byte[] getIntegrityHmacValue() {
        return this.integrityHmacValue;
    }

    protected void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    protected void setVerifier(byte[] verifier) {
        this.verifier = verifier == null ? null : (byte[]) verifier.clone();
    }

    protected void setIntegrityHmacKey(byte[] integrityHmacKey) {
        this.integrityHmacKey = integrityHmacKey == null ? null : (byte[]) integrityHmacKey.clone();
    }

    protected void setIntegrityHmacValue(byte[] integrityHmacValue) {
        this.integrityHmacValue = integrityHmacValue == null ? null : (byte[]) integrityHmacValue.clone();
    }

    protected int getBlockSizeInBytes() {
        return this.encryptionInfo.getHeader().getBlockSize();
    }

    protected int getKeySizeInBytes() {
        return this.encryptionInfo.getHeader().getKeySize() / 8;
    }

    public EncryptionInfo getEncryptionInfo() {
        return this.encryptionInfo;
    }

    public void setEncryptionInfo(EncryptionInfo encryptionInfo) {
        this.encryptionInfo = encryptionInfo;
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Decryptor mo3514clone() throws CloneNotSupportedException {
        Decryptor other = (Decryptor) super.clone();
        other.integrityHmacKey = (byte[]) this.integrityHmacKey.clone();
        other.integrityHmacValue = (byte[]) this.integrityHmacValue.clone();
        other.verifier = (byte[]) this.verifier.clone();
        other.secretKey = new SecretKeySpec(this.secretKey.getEncoded(), this.secretKey.getAlgorithm());
        return other;
    }
}
