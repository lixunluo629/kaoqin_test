package org.apache.poi.poifs.crypt;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndianInputStream;

@Internal
/* loaded from: poi-3.17.jar:org/apache/poi/poifs/crypt/ChunkedCipherInputStream.class */
public abstract class ChunkedCipherInputStream extends LittleEndianInputStream {
    private final int chunkSize;
    private final int chunkBits;
    private final long size;
    private final byte[] chunk;
    private final byte[] plain;
    private final Cipher cipher;
    private int lastIndex;
    private long pos;
    private boolean chunkIsValid;

    protected abstract Cipher initCipherForBlock(Cipher cipher, int i) throws GeneralSecurityException;

    public ChunkedCipherInputStream(InputStream stream, long size, int chunkSize) throws GeneralSecurityException {
        this(stream, size, chunkSize, 0);
    }

    public ChunkedCipherInputStream(InputStream stream, long size, int chunkSize, int initialPos) throws GeneralSecurityException {
        super(stream);
        this.chunkIsValid = false;
        this.size = size;
        this.pos = initialPos;
        this.chunkSize = chunkSize;
        int cs = chunkSize == -1 ? 4096 : chunkSize;
        this.chunk = new byte[cs];
        this.plain = new byte[cs];
        this.chunkBits = Integer.bitCount(this.chunk.length - 1);
        this.lastIndex = (int) (this.pos >> this.chunkBits);
        this.cipher = initCipherForBlock(null, this.lastIndex);
    }

    public final Cipher initCipherForBlock(int block) throws GeneralSecurityException, IOException {
        if (this.chunkSize != -1) {
            throw new GeneralSecurityException("the cipher block can only be set for streaming encryption, e.g. CryptoAPI...");
        }
        this.chunkIsValid = false;
        return initCipherForBlock(this.cipher, block);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        byte[] b = {0};
        if (read(b) == 1) {
            return -1;
        }
        return b[0];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        return read(b, off, len, false);
    }

    private int read(byte[] b, int off, int len, boolean readPlain) throws IOException {
        int total = 0;
        if (available() <= 0) {
            return -1;
        }
        int chunkMask = getChunkMask();
        while (len > 0) {
            if (!this.chunkIsValid) {
                try {
                    nextChunk();
                    this.chunkIsValid = true;
                } catch (GeneralSecurityException e) {
                    throw new EncryptedDocumentException(e.getMessage(), e);
                }
            }
            int count = (int) (this.chunk.length - (this.pos & chunkMask));
            int avail = available();
            if (avail == 0) {
                return total;
            }
            int count2 = Math.min(avail, Math.min(count, len));
            System.arraycopy(readPlain ? this.plain : this.chunk, (int) (this.pos & chunkMask), b, off, count2);
            off += count2;
            len -= count2;
            this.pos += count2;
            if ((this.pos & chunkMask) == 0) {
                this.chunkIsValid = false;
            }
            total += count2;
        }
        return total;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws IOException {
        long start = this.pos;
        long skip = Math.min(remainingBytes(), n);
        if ((((this.pos + skip) ^ start) & (getChunkMask() ^ (-1))) != 0) {
            this.chunkIsValid = false;
        }
        this.pos += skip;
        return skip;
    }

    @Override // org.apache.poi.util.LittleEndianInputStream, java.io.FilterInputStream, java.io.InputStream, org.apache.poi.util.LittleEndianInput
    public int available() {
        return remainingBytes();
    }

    private int remainingBytes() {
        return (int) (this.size - this.pos);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    protected int getChunkMask() {
        return this.chunk.length - 1;
    }

    private void nextChunk() throws GeneralSecurityException, IOException {
        int readBytes;
        if (this.chunkSize != -1) {
            int index = (int) (this.pos >> this.chunkBits);
            initCipherForBlock(this.cipher, index);
            if (this.lastIndex != index) {
                long skipN = (index - this.lastIndex) << this.chunkBits;
                if (super.skip(skipN) < skipN) {
                    throw new EOFException("buffer underrun");
                }
            }
            this.lastIndex = index + 1;
        }
        int todo = (int) Math.min(this.size, this.chunk.length);
        int totalBytes = 0;
        do {
            readBytes = super.read(this.plain, totalBytes, todo - totalBytes);
            totalBytes += Math.max(0, readBytes);
            if (readBytes == -1) {
                break;
            }
        } while (totalBytes < todo);
        if (readBytes == -1 && this.pos + totalBytes < this.size && this.size < 2147483647L) {
            throw new EOFException("buffer underrun");
        }
        System.arraycopy(this.plain, 0, this.chunk, 0, totalBytes);
        invokeCipher(totalBytes, totalBytes == this.chunkSize);
    }

    protected int invokeCipher(int totalBytes, boolean doFinal) throws GeneralSecurityException {
        if (doFinal) {
            return this.cipher.doFinal(this.chunk, 0, totalBytes, this.chunk);
        }
        return this.cipher.update(this.chunk, 0, totalBytes, this.chunk);
    }

    @Override // org.apache.poi.util.LittleEndianInputStream, org.apache.poi.util.LittleEndianInput
    public void readPlain(byte[] b, int off, int len) throws EOFException {
        if (len <= 0) {
            return;
        }
        int total = 0;
        do {
            try {
                int readBytes = read(b, off, len, true);
                total += Math.max(0, readBytes);
                if (readBytes <= -1) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (total < len);
        if (total < len) {
            throw new EOFException("buffer underrun");
        }
    }

    public void setNextRecordSize(int recordSize) {
    }

    protected byte[] getChunk() {
        return this.chunk;
    }

    protected byte[] getPlain() {
        return this.plain;
    }

    public long getPos() {
        return this.pos;
    }
}
