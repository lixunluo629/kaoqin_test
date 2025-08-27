package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/VCenterRecord.class */
public final class VCenterRecord extends StandardRecord {
    public static final short sid = 132;
    private int field_1_vcenter;

    public VCenterRecord() {
    }

    public VCenterRecord(RecordInputStream in) {
        this.field_1_vcenter = in.readShort();
    }

    public void setVCenter(boolean hc) {
        this.field_1_vcenter = hc ? 1 : 0;
    }

    public boolean getVCenter() {
        return this.field_1_vcenter == 1;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[VCENTER]\n");
        buffer.append("    .vcenter        = ").append(getVCenter()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/VCENTER]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_vcenter);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 132;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        VCenterRecord rec = new VCenterRecord();
        rec.field_1_vcenter = this.field_1_vcenter;
        return rec;
    }
}
