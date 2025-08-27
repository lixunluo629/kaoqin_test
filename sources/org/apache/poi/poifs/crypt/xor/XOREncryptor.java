package org.apache.poi.poifs.crypt.xor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.BitSet;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/xor/XOREncryptor.class */
public class XOREncryptor extends Encryptor implements Cloneable {
    protected XOREncryptor() {
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    public void confirmPassword(String password) {
        int keyComp = CryptoFunctions.createXorKey1(password);
        int verifierComp = CryptoFunctions.createXorVerifier1(password);
        byte[] xorArray = CryptoFunctions.createXorArray1(password);
        byte[] shortBuf = new byte[2];
        XOREncryptionVerifier ver = (XOREncryptionVerifier) getEncryptionInfo().getVerifier();
        LittleEndian.putUShort(shortBuf, 0, keyComp);
        ver.setEncryptedKey(shortBuf);
        LittleEndian.putUShort(shortBuf, 0, verifierComp);
        ver.setEncryptedVerifier(shortBuf);
        setSecretKey(new SecretKeySpec(xorArray, "XOR"));
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt) {
        confirmPassword(password);
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    public OutputStream getDataStream(DirectoryNode dir) throws GeneralSecurityException, IOException {
        return new XORCipherOutputStream(dir);
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    public XORCipherOutputStream getDataStream(OutputStream stream, int initialOffset) throws GeneralSecurityException, IOException {
        return new XORCipherOutputStream(stream, initialOffset);
    }

    protected int getKeySizeInBytes() {
        return -1;
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    public void setChunkSize(int chunkSize) {
    }

    protected void createEncryptionInfoEntry(DirectoryNode dir) throws IOException {
    }

    @Override // org.apache.poi.poifs.crypt.Encryptor
    /* renamed from: clone */
    public XOREncryptor mo3521clone() throws CloneNotSupportedException {
        return (XOREncryptor) super.mo3521clone();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/xor/XOREncryptor$XORCipherOutputStream.class */
    private class XORCipherOutputStream extends ChunkedCipherOutputStream {
        private int recordStart;
        private int recordEnd;

        public XORCipherOutputStream(OutputStream stream, int initialPos) throws GeneralSecurityException, IOException {
            super(stream, -1);
            this.recordStart = 0;
            this.recordEnd = 0;
        }

        public XORCipherOutputStream(DirectoryNode dir) throws GeneralSecurityException, IOException {
            super(dir, -1);
            this.recordStart = 0;
            this.recordEnd = 0;
        }

        @Override // org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
        protected Cipher initCipherForBlock(Cipher cipher, int block, boolean lastChunk) throws GeneralSecurityException {
            return XORDecryptor.initCipherForBlock(cipher, block, XOREncryptor.this.getEncryptionInfo(), XOREncryptor.this.getSecretKey(), 1);
        }

        @Override // org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
        protected void calculateChecksum(File file, int i) {
        }

        @Override // org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
        protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile) throws GeneralSecurityException, IOException {
            XOREncryptor.this.createEncryptionInfoEntry(dir);
        }

        @Override // org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
        public void setNextRecordSize(int recordSize, boolean isPlain) {
            if (this.recordEnd > 0 && !isPlain) {
                invokeCipher((int) getPos(), true);
            }
            this.recordStart = ((int) getTotalPos()) + 4;
            this.recordEnd = this.recordStart + recordSize;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            setNextRecordSize(0, true);
            super.flush();
        }

        @Override // org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
        protected int invokeCipher(int posInChunk, boolean doFinal) {
            if (posInChunk == 0) {
                return 0;
            }
            int start = Math.max(posInChunk - (this.recordEnd - this.recordStart), 0);
            BitSet plainBytes = getPlainByteFlags();
            byte[] xorArray = XOREncryptor.this.getEncryptionInfo().getEncryptor().getSecretKey().getEncoded();
            byte[] chunk = getChunk();
            byte[] plain = plainBytes.isEmpty() ? null : (byte[]) chunk.clone();
            int xorArrayIndex = this.recordEnd + (start - this.recordStart);
            for (int i = start; i < posInChunk; i++) {
                byte value = chunk[i];
                int i2 = xorArrayIndex;
                xorArrayIndex++;
                chunk[i] = rotateLeft((byte) (value ^ xorArray[i2 & 15]), 5);
            }
            int iNextSetBit = plainBytes.nextSetBit(start);
            while (true) {
                int i3 = iNextSetBit;
                if (i3 < 0 || i3 >= posInChunk) {
                    break;
                }
                chunk[i3] = plain[i3];
                iNextSetBit = plainBytes.nextSetBit(i3 + 1);
            }
            return posInChunk;
        }

        private byte rotateLeft(byte bits, int shift) {
            return (byte) (((bits & 255) << shift) | ((bits & 255) >>> (8 - shift)));
        }
    }
}
