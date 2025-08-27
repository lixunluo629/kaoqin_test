package org.apache.poi.poifs.crypt.cryptoapi;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.BoundedInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianInputStream;
import org.apache.poi.util.StringUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDecryptor.class */
public class CryptoAPIDecryptor extends Decryptor implements Cloneable {
    private long length = -1;
    private int chunkSize = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CryptoAPIDecryptor.class.desiredAssertionStatus();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDecryptor$StreamDescriptorEntry.class */
    static class StreamDescriptorEntry {
        static BitField flagStream = BitFieldFactory.getInstance(1);
        int streamOffset;
        int streamSize;
        int block;
        int flags;
        int reserved2;
        String streamName;

        StreamDescriptorEntry() {
        }
    }

    protected CryptoAPIDecryptor() {
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public boolean verifyPassword(String password) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        EncryptionVerifier ver = getEncryptionInfo().getVerifier();
        SecretKey skey = generateSecretKey(password, ver);
        try {
            Cipher cipher = initCipherForBlock(null, 0, getEncryptionInfo(), skey, 2);
            byte[] encryptedVerifier = ver.getEncryptedVerifier();
            byte[] verifier = new byte[encryptedVerifier.length];
            cipher.update(encryptedVerifier, 0, encryptedVerifier.length, verifier);
            setVerifier(verifier);
            byte[] encryptedVerifierHash = ver.getEncryptedVerifierHash();
            byte[] verifierHash = cipher.doFinal(encryptedVerifierHash);
            HashAlgorithm hashAlgo = ver.getHashAlgorithm();
            MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
            byte[] calcVerifierHash = hashAlg.digest(verifier);
            if (Arrays.equals(calcVerifierHash, verifierHash)) {
                setSecretKey(skey);
                return true;
            }
            return false;
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException(e);
        }
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public Cipher initCipherForBlock(Cipher cipher, int block) throws GeneralSecurityException {
        EncryptionInfo ei = getEncryptionInfo();
        SecretKey sk = getSecretKey();
        return initCipherForBlock(cipher, block, ei, sk, 2);
    }

    protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfo encryptionInfo, SecretKey skey, int encryptMode) throws GeneralSecurityException {
        EncryptionVerifier ver = encryptionInfo.getVerifier();
        HashAlgorithm hashAlgo = ver.getHashAlgorithm();
        byte[] blockKey = new byte[4];
        LittleEndian.putUInt(blockKey, 0, block);
        MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
        hashAlg.update(skey.getEncoded());
        byte[] encKey = hashAlg.digest(blockKey);
        EncryptionHeader header = encryptionInfo.getHeader();
        int keyBits = header.getKeySize();
        byte[] encKey2 = CryptoFunctions.getBlock0(encKey, keyBits / 8);
        if (keyBits == 40) {
            encKey2 = CryptoFunctions.getBlock0(encKey2, 16);
        }
        SecretKey key = new SecretKeySpec(encKey2, skey.getAlgorithm());
        if (cipher == null) {
            cipher = CryptoFunctions.getCipher(key, header.getCipherAlgorithm(), null, null, encryptMode);
        } else {
            cipher.init(encryptMode, key);
        }
        return cipher;
    }

    protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver) {
        if (password.length() > 255) {
            password = password.substring(0, 255);
        }
        HashAlgorithm hashAlgo = ver.getHashAlgorithm();
        MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
        hashAlg.update(ver.getSalt());
        byte[] hash = hashAlg.digest(StringUtil.getToUnicodeLE(password));
        SecretKey skey = new SecretKeySpec(hash, ver.getCipherAlgorithm().jceId);
        return skey;
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public ChunkedCipherInputStream getDataStream(DirectoryNode dir) throws GeneralSecurityException, IOException {
        throw new IOException("not supported");
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public ChunkedCipherInputStream getDataStream(InputStream stream, int size, int initialPos) throws GeneralSecurityException, IOException {
        return new CryptoAPICipherInputStream(stream, size, initialPos);
    }

    public POIFSFileSystem getSummaryEntries(DirectoryNode root, String encryptedStream) throws GeneralSecurityException, IOException {
        DocumentNode es = (DocumentNode) root.getEntry(encryptedStream);
        DocumentInputStream dis = root.createDocumentInputStream(es);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(dis, bos);
        dis.close();
        CryptoAPIDocumentInputStream sbis = new CryptoAPIDocumentInputStream(this, bos.toByteArray());
        LittleEndianInputStream leis = new LittleEndianInputStream(sbis);
        try {
            try {
                int streamDescriptorArrayOffset = (int) leis.readUInt();
                leis.readUInt();
                long skipN = streamDescriptorArrayOffset - 8;
                if (sbis.skip(skipN) < skipN) {
                    throw new EOFException("buffer underrun");
                }
                sbis.setBlock(0);
                int encryptedStreamDescriptorCount = (int) leis.readUInt();
                StreamDescriptorEntry[] entries = new StreamDescriptorEntry[encryptedStreamDescriptorCount];
                for (int i = 0; i < encryptedStreamDescriptorCount; i++) {
                    StreamDescriptorEntry entry = new StreamDescriptorEntry();
                    entries[i] = entry;
                    entry.streamOffset = (int) leis.readUInt();
                    entry.streamSize = (int) leis.readUInt();
                    entry.block = leis.readUShort();
                    int nameSize = leis.readUByte();
                    entry.flags = leis.readUByte();
                    entry.reserved2 = leis.readInt();
                    entry.streamName = StringUtil.readUnicodeLE(leis, nameSize);
                    leis.readShort();
                    if (!$assertionsDisabled && entry.streamName.length() != nameSize) {
                        throw new AssertionError();
                    }
                }
                POIFSFileSystem fsOut = new POIFSFileSystem();
                for (StreamDescriptorEntry entry2 : entries) {
                    sbis.seek(entry2.streamOffset);
                    sbis.setBlock(entry2.block);
                    InputStream is = new BoundedInputStream(sbis, entry2.streamSize);
                    fsOut.createDocument(is, entry2.streamName);
                    is.close();
                }
                return fsOut;
            } catch (Exception e) {
                IOUtils.closeQuietly(null);
                if (e instanceof GeneralSecurityException) {
                    throw ((GeneralSecurityException) e);
                }
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new IOException("summary entries can't be read", e);
            }
        } finally {
            IOUtils.closeQuietly(leis);
            IOUtils.closeQuietly(sbis);
        }
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public long getLength() {
        if (this.length == -1) {
            throw new IllegalStateException("Decryptor.getDataStream() was not called");
        }
        return this.length;
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override // org.apache.poi.poifs.crypt.Decryptor
    /* renamed from: clone */
    public CryptoAPIDecryptor mo3514clone() throws CloneNotSupportedException {
        return (CryptoAPIDecryptor) super.mo3514clone();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDecryptor$CryptoAPICipherInputStream.class */
    private class CryptoAPICipherInputStream extends ChunkedCipherInputStream {
        @Override // org.apache.poi.poifs.crypt.ChunkedCipherInputStream
        protected Cipher initCipherForBlock(Cipher existing, int block) throws GeneralSecurityException {
            return CryptoAPIDecryptor.this.initCipherForBlock(existing, block);
        }

        public CryptoAPICipherInputStream(InputStream stream, long size, int initialPos) throws GeneralSecurityException {
            super(stream, size, CryptoAPIDecryptor.this.chunkSize, initialPos);
        }
    }
}
