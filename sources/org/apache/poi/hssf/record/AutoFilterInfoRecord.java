package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/AutoFilterInfoRecord.class */
public final class AutoFilterInfoRecord extends StandardRecord implements Cloneable {
    public static final short sid = 157;
    private short _cEntries;

    public AutoFilterInfoRecord() {
    }

    public AutoFilterInfoRecord(RecordInputStream in) {
        this._cEntries = in.readShort();
    }

    public void setNumEntries(short num) {
        this._cEntries = num;
    }

    public short getNumEntries() {
        return this._cEntries;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[AUTOFILTERINFO]\n");
        buffer.append("    .numEntries          = ").append((int) this._cEntries).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/AUTOFILTERINFO]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._cEntries);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 157;
    }

    @Override // org.apache.poi.hssf.record.Record
    public AutoFilterInfoRecord clone() {
        return (AutoFilterInfoRecord) cloneViaReserialise();
    }
}
