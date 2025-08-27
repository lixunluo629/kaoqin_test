package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/NoteStructureSubRecord.class */
public final class NoteStructureSubRecord extends SubRecord implements Cloneable {
    public static final short sid = 13;
    private static final int ENCODED_SIZE = 22;
    private byte[] reserved;

    public NoteStructureSubRecord() {
        this.reserved = new byte[22];
    }

    public NoteStructureSubRecord(LittleEndianInput in, int size) {
        if (size != 22) {
            throw new RecordFormatException("Unexpected size (" + size + ")");
        }
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ftNts ]").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("  size     = ").append(getDataSize()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("  reserved = ").append(HexDump.toHex(this.reserved)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/ftNts ]").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(13);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    protected int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return (short) 13;
    }

    @Override // org.apache.poi.hssf.record.SubRecord
    /* renamed from: clone */
    public NoteStructureSubRecord mo3334clone() {
        NoteStructureSubRecord rec = new NoteStructureSubRecord();
        byte[] recdata = new byte[this.reserved.length];
        System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
        rec.reserved = recdata;
        return rec;
    }
}
