package org.apache.poi.hssf.record.pivottable;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/pivottable/ViewSourceRecord.class */
public final class ViewSourceRecord extends StandardRecord {
    public static final short sid = 227;
    private int vs;

    public ViewSourceRecord(RecordInputStream in) {
        this.vs = in.readShort();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.vs);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 227;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SXVS]\n");
        buffer.append("    .vs      =").append(HexDump.shortToHex(this.vs)).append('\n');
        buffer.append("[/SXVS]\n");
        return buffer.toString();
    }
}
