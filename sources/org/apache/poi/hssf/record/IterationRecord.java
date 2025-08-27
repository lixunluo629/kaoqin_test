package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/IterationRecord.class */
public final class IterationRecord extends StandardRecord implements Cloneable {
    public static final short sid = 17;
    private static final BitField iterationOn = BitFieldFactory.getInstance(1);
    private int _flags;

    public IterationRecord(boolean iterateOn) {
        this._flags = iterationOn.setBoolean(0, iterateOn);
    }

    public IterationRecord(RecordInputStream in) {
        this._flags = in.readShort();
    }

    public void setIteration(boolean iterate) {
        this._flags = iterationOn.setBoolean(this._flags, iterate);
    }

    public boolean getIteration() {
        return iterationOn.isSet(this._flags);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ITERATION]\n");
        buffer.append("    .flags      = ").append(HexDump.shortToHex(this._flags)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/ITERATION]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._flags);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 17;
    }

    @Override // org.apache.poi.hssf.record.Record
    public IterationRecord clone() {
        return new IterationRecord(getIteration());
    }
}
