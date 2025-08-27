package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/DConRefRecord.class */
public class DConRefRecord extends StandardRecord {
    public static final short sid = 81;
    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;
    private int charCount;
    private int charType;
    private byte[] path;
    private byte[] _unused;

    public DConRefRecord(byte[] data) {
        if (LittleEndian.getShort(data, 0) != 81) {
            throw new RecordFormatException("incompatible sid.");
        }
        int offset = 0 + 2 + 2;
        this.firstRow = LittleEndian.getUShort(data, offset);
        int offset2 = offset + 2;
        this.lastRow = LittleEndian.getUShort(data, offset2);
        int offset3 = offset2 + 2;
        this.firstCol = LittleEndian.getUByte(data, offset3);
        int offset4 = offset3 + 1;
        this.lastCol = LittleEndian.getUByte(data, offset4);
        int offset5 = offset4 + 1;
        this.charCount = LittleEndian.getUShort(data, offset5);
        int offset6 = offset5 + 2;
        if (this.charCount < 2) {
            throw new RecordFormatException("Character count must be >= 2");
        }
        this.charType = LittleEndian.getUByte(data, offset6);
        int offset7 = offset6 + 1;
        int byteLength = this.charCount * ((this.charType & 1) + 1);
        this.path = LittleEndian.getByteArray(data, offset7, byteLength);
        int offset8 = offset7 + byteLength;
        if (this.path[0] == 2) {
            this._unused = LittleEndian.getByteArray(data, offset8, this.charType + 1);
        }
    }

    public DConRefRecord(RecordInputStream inStream) throws RecordFormatException {
        if (inStream.getSid() != 81) {
            throw new RecordFormatException("Wrong sid: " + ((int) inStream.getSid()));
        }
        this.firstRow = inStream.readUShort();
        this.lastRow = inStream.readUShort();
        this.firstCol = inStream.readUByte();
        this.lastCol = inStream.readUByte();
        this.charCount = inStream.readUShort();
        this.charType = inStream.readUByte() & 1;
        int byteLength = this.charCount * (this.charType + 1);
        this.path = new byte[byteLength];
        inStream.readFully(this.path);
        if (this.path[0] == 2) {
            this._unused = inStream.readRemainder();
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        int sz = 9 + this.path.length;
        if (this.path[0] == 2) {
            sz += this._unused.length;
        }
        return sz;
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.firstRow);
        out.writeShort(this.lastRow);
        out.writeByte(this.firstCol);
        out.writeByte(this.lastCol);
        out.writeShort(this.charCount);
        out.writeByte(this.charType);
        out.write(this.path);
        if (this.path[0] == 2) {
            out.write(this._unused);
        }
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 81;
    }

    public int getFirstColumn() {
        return this.firstCol;
    }

    public int getFirstRow() {
        return this.firstRow;
    }

    public int getLastColumn() {
        return this.lastCol;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[DCONREF]\n");
        b.append("    .ref\n");
        b.append("        .firstrow   = ").append(this.firstRow).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("        .lastrow    = ").append(this.lastRow).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("        .firstcol   = ").append(this.firstCol).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("        .lastcol    = ").append(this.lastCol).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("    .cch            = ").append(this.charCount).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("    .stFile\n");
        b.append("        .h          = ").append(this.charType).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("        .rgb        = ").append(getReadablePath()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        b.append("[/DCONREF]\n");
        return b.toString();
    }

    public byte[] getPath() {
        return Arrays.copyOf(this.path, this.path.length);
    }

    public String getReadablePath() {
        if (this.path != null) {
            int offset = 1;
            while (this.path[offset] < 32 && offset < this.path.length) {
                offset++;
            }
            String out = new String(Arrays.copyOfRange(this.path, offset, this.path.length), StringUtil.UTF8);
            return out.replaceAll("\u0003", "/");
        }
        return null;
    }

    public boolean isExternalRef() {
        if (this.path[0] == 1) {
            return true;
        }
        return false;
    }
}
