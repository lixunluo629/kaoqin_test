package org.apache.poi.hssf.record;

import org.apache.poi.hssf.util.RKUtil;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/RKRecord.class */
public final class RKRecord extends CellRecord {
    public static final short sid = 638;
    public static final short RK_IEEE_NUMBER = 0;
    public static final short RK_IEEE_NUMBER_TIMES_100 = 1;
    public static final short RK_INTEGER = 2;
    public static final short RK_INTEGER_TIMES_100 = 3;
    private int field_4_rk_number;

    private RKRecord() {
    }

    public RKRecord(RecordInputStream in) {
        super(in);
        this.field_4_rk_number = in.readInt();
    }

    public double getRKNumber() {
        return RKUtil.decodeNumber(this.field_4_rk_number);
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected String getRecordName() {
        return "RK";
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected void appendValueText(StringBuilder sb) {
        sb.append("  .value= ").append(getRKNumber());
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected void serializeValue(LittleEndianOutput out) {
        out.writeInt(this.field_4_rk_number);
    }

    @Override // org.apache.poi.hssf.record.CellRecord
    protected int getValueDataSize() {
        return 4;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 638;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        RKRecord rec = new RKRecord();
        copyBaseFields(rec);
        rec.field_4_rk_number = this.field_4_rk_number;
        return rec;
    }
}
