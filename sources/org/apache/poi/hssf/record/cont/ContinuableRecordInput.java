package org.apache.poi.hssf.record.cont;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/cont/ContinuableRecordInput.class */
public class ContinuableRecordInput implements LittleEndianInput {
    private final RecordInputStream _in;

    public ContinuableRecordInput(RecordInputStream in) {
        this._in = in;
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int available() {
        return this._in.available();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public byte readByte() {
        return this._in.readByte();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readUByte() {
        return this._in.readUByte();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public short readShort() {
        return this._in.readShort();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readUShort() {
        int ch1 = readUByte();
        int ch2 = readUByte();
        return (ch2 << 8) + (ch1 << 0);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public int readInt() {
        int ch1 = this._in.readUByte();
        int ch2 = this._in.readUByte();
        int ch3 = this._in.readUByte();
        int ch4 = this._in.readUByte();
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public long readLong() {
        int b0 = this._in.readUByte();
        int b1 = this._in.readUByte();
        int b2 = this._in.readUByte();
        int b3 = this._in.readUByte();
        int b4 = this._in.readUByte();
        int b5 = this._in.readUByte();
        int b6 = this._in.readUByte();
        int b7 = this._in.readUByte();
        return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public double readDouble() {
        return this._in.readDouble();
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readFully(byte[] buf) throws RecordFormatException {
        this._in.readFully(buf);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readFully(byte[] buf, int off, int len) throws RecordFormatException {
        this._in.readFully(buf, off, len);
    }

    @Override // org.apache.poi.util.LittleEndianInput
    public void readPlain(byte[] buf, int off, int len) throws RecordFormatException {
        readFully(buf, off, len);
    }
}
