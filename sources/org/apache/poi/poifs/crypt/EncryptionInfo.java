package org.apache.poi.poifs.crypt;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/EncryptionInfo.class */
public class EncryptionInfo implements Cloneable {
    private final EncryptionMode encryptionMode;
    private final int versionMajor;
    private final int versionMinor;
    private final int encryptionFlags;
    private EncryptionHeader header;
    private EncryptionVerifier verifier;
    private Decryptor decryptor;
    private Encryptor encryptor;
    public static final BitField flagCryptoAPI = BitFieldFactory.getInstance(4);
    public static final BitField flagDocProps = BitFieldFactory.getInstance(8);
    public static final BitField flagExternal = BitFieldFactory.getInstance(16);
    public static final BitField flagAES = BitFieldFactory.getInstance(32);

    public EncryptionInfo(POIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(OPOIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(NPOIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(DirectoryNode dir) throws IOException {
        this(dir.createDocumentInputStream("EncryptionInfo"), null);
    }

    public EncryptionInfo(LittleEndianInput dis, EncryptionMode preferredEncryptionMode) throws IOException {
        if (preferredEncryptionMode == EncryptionMode.xor) {
            this.versionMajor = EncryptionMode.xor.versionMajor;
            this.versionMinor = EncryptionMode.xor.versionMinor;
        } else {
            this.versionMajor = dis.readUShort();
            this.versionMinor = dis.readUShort();
        }
        if (this.versionMajor == EncryptionMode.xor.versionMajor && this.versionMinor == EncryptionMode.xor.versionMinor) {
            this.encryptionMode = EncryptionMode.xor;
            this.encryptionFlags = -1;
        } else if (this.versionMajor == EncryptionMode.binaryRC4.versionMajor && this.versionMinor == EncryptionMode.binaryRC4.versionMinor) {
            this.encryptionMode = EncryptionMode.binaryRC4;
            this.encryptionFlags = -1;
        } else if (2 <= this.versionMajor && this.versionMajor <= 4 && this.versionMinor == 2) {
            this.encryptionFlags = dis.readInt();
            this.encryptionMode = (preferredEncryptionMode == EncryptionMode.cryptoAPI || !flagAES.isSet(this.encryptionFlags)) ? EncryptionMode.cryptoAPI : EncryptionMode.standard;
        } else if (this.versionMajor == EncryptionMode.agile.versionMajor && this.versionMinor == EncryptionMode.agile.versionMinor) {
            this.encryptionMode = EncryptionMode.agile;
            this.encryptionFlags = dis.readInt();
        } else {
            this.encryptionFlags = dis.readInt();
            throw new EncryptedDocumentException("Unknown encryption: version major: " + this.versionMajor + " / version minor: " + this.versionMinor + " / fCrypto: " + flagCryptoAPI.isSet(this.encryptionFlags) + " / fExternal: " + flagExternal.isSet(this.encryptionFlags) + " / fDocProps: " + flagDocProps.isSet(this.encryptionFlags) + " / fAES: " + flagAES.isSet(this.encryptionFlags));
        }
        try {
            EncryptionInfoBuilder eib = getBuilder(this.encryptionMode);
            eib.initialize(this, dis);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public EncryptionInfo(EncryptionMode encryptionMode) {
        this(encryptionMode, null, null, -1, -1, null);
    }

    public EncryptionInfo(EncryptionMode encryptionMode, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        this.encryptionMode = encryptionMode;
        this.versionMajor = encryptionMode.versionMajor;
        this.versionMinor = encryptionMode.versionMinor;
        this.encryptionFlags = encryptionMode.encryptionFlags;
        try {
            EncryptionInfoBuilder eib = getBuilder(encryptionMode);
            eib.initialize(this, cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
        } catch (Exception e) {
            throw new EncryptedDocumentException(e);
        }
    }

    protected static EncryptionInfoBuilder getBuilder(EncryptionMode encryptionMode) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        EncryptionInfoBuilder eib = (EncryptionInfoBuilder) cl.loadClass(encryptionMode.builder).newInstance();
        return eib;
    }

    public int getVersionMajor() {
        return this.versionMajor;
    }

    public int getVersionMinor() {
        return this.versionMinor;
    }

    public int getEncryptionFlags() {
        return this.encryptionFlags;
    }

    public EncryptionHeader getHeader() {
        return this.header;
    }

    public EncryptionVerifier getVerifier() {
        return this.verifier;
    }

    public Decryptor getDecryptor() {
        return this.decryptor;
    }

    public Encryptor getEncryptor() {
        return this.encryptor;
    }

    public void setHeader(EncryptionHeader header) {
        this.header = header;
    }

    public void setVerifier(EncryptionVerifier verifier) {
        this.verifier = verifier;
    }

    public void setDecryptor(Decryptor decryptor) {
        this.decryptor = decryptor;
    }

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public EncryptionMode getEncryptionMode() {
        return this.encryptionMode;
    }

    public boolean isDocPropsEncrypted() {
        return !flagDocProps.isSet(getEncryptionFlags());
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public EncryptionInfo m3518clone() throws CloneNotSupportedException {
        EncryptionInfo other = (EncryptionInfo) super.clone();
        other.header = this.header.mo3516clone();
        other.verifier = this.verifier.mo3520clone();
        other.decryptor = this.decryptor.mo3514clone();
        other.decryptor.setEncryptionInfo(other);
        other.encryptor = this.encryptor.mo3521clone();
        other.encryptor.setEncryptionInfo(other);
        return other;
    }
}
