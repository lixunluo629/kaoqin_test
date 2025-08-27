package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/FtCblsSubRecord.class */
public final class FtCblsSubRecord extends SubRecord implements Cloneable {
    public static final short sid = 12;
    private static final int ENCODED_SIZE = 20;
    private byte[] reserved;

    public FtCblsSubRecord() {
        this.reserved = new byte[20];
    }

    public FtCblsSubRecord(LittleEndianInput in, int size) {
        if (size != 20) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FtCbls ]").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("  size     = ").append(getDataSize()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/FtCbls ]").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(12);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    protected int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return (short) 12;
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    /* renamed from: clone */
    public FtCblsSubRecord mo3334clone() {
        FtCblsSubRecord rec = new FtCblsSubRecord();
        byte[] recdata = new byte[this.reserved.length];
        System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
        rec.reserved = recdata;
        return rec;
    }
}
