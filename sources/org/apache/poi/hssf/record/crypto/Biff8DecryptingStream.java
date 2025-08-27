package org.apache.poi.hssf.record.crypto;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.apache.poi.hssf.record.BiffHeaderInput;
import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/crypto/Biff8DecryptingStream.class */
public final class Biff8DecryptingStream implements BiffHeaderInput, LittleEndianInput {
    public static final int RC4_REKEYING_INTERVAL = 1024;
    private final EncryptionInfo info;
    private ChunkedCipherInputStream ccis;
    private final byte[] buffer = new byte[8];
    private boolean shouldSkipEncryptionOnCurrentRecord = false;

    public Biff8DecryptingStream(InputStream in, int initialOffset, EncryptionInfo info) throws IOException, RecordFormatException {
        InputStream stream;
        try {
            byte[] initialBuf = new byte[initialOffset];
            if (initialOffset == 0) {
                stream = in;
            } else {
                stream = new PushbackInputStream(in, initialOffset);
                ((PushbackInputStream) stream).unread(initialBuf);
            }
            this.info = info;
            Decryptor dec = this.info.getDecryptor();
            dec.setChunkSize(1024);
            this.ccis = (ChunkedCipherInputStream) dec.getDataStream(stream, Integer.MAX_VALUE, 0);
            if (initialOffset > 0) {
                this.ccis.readFully(initialBuf);
            }
        } catch (Exception e) {
            throw new RecordFormatException(e);
        }
    }

    @Override // org.apache.poi.hssf.record.BiffHeaderInput
    public int available() {
        return this.ccis.available();
    }

    @Override // org.apache.poi.hssf.record.BiffHeaderInput
    public int readRecordSID() throws EOFException {
        readPlain(this.buffer, 0, 2);
        int sid = LittleEndian.getUShort(this.buffer, 0);
        this.shouldSkipEncryptionOnCurrentRecord = isNeverEncryptedRecord(sid);
        return sid;
    }

    @Override // org.apache.poi.hssf.record.BiffHeaderInput
    public int readDataSize() throws EOFException {
        readPlain(this.buffer, 0, 2);
        int dataSize = LittleEndian.getUShort(this.buffer, 0);
        this.ccis.setNextRecordSize(dataSize);
        return dataSize;
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public double readDouble() throws EOFException {
        long valueLongBits = readLong();
        double result = Double.longBitsToDouble(valueLongBits);
        if (Double.isNaN(result)) {
            throw new RuntimeException("Did not expect to read NaN");
        }
        return result;
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readFully(byte[] buf) throws EOFException {
        readFully(buf, 0, buf.length);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readFully(byte[] buf, int off, int len) throws EOFException {
        if (this.shouldSkipEncryptionOnCurrentRecord) {
            readPlain(buf, off, buf.length);
        } else {
            this.ccis.readFully(buf, off, len);
        }
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readUByte() {
        return readByte() & 255;
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public byte readByte() throws EOFException {
        if (this.shouldSkipEncryptionOnCurrentRecord) {
            readPlain(this.buffer, 0, 1);
            return this.buffer[0];
        }
        return this.ccis.readByte();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readUShort() {
        return readShort() & 65535;
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public short readShort() throws EOFException {
        if (this.shouldSkipEncryptionOnCurrentRecord) {
            readPlain(this.buffer, 0, 2);
            return LittleEndian.getShort(this.buffer);
        }
        return this.ccis.readShort();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readInt() throws EOFException {
        if (this.shouldSkipEncryptionOnCurrentRecord) {
            readPlain(this.buffer, 0, 4);
            return LittleEndian.getInt(this.buffer);
        }
        return this.ccis.readInt();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public long readLong() throws EOFException {
        if (this.shouldSkipEncryptionOnCurrentRecord) {
            readPlain(this.buffer, 0, 8);
            return LittleEndian.getLong(this.buffer);
        }
        return this.ccis.readLong();
    }

    public long getPosition() {
        return this.ccis.getPos();
    }

    public static boolean isNeverEncryptedRecord(int sid) {
        switch (sid) {
            case 47:
            case 225:
            case 2057:
                return true;
            default:
                return false;
        }
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readPlain(byte[] b, int off, int len) throws EOFException {
        this.ccis.readPlain(b, off, len);
    }
}
