package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/HideObjRecord.class */
public final class HideObjRecord extends StandardRecord {
    public static final short sid = 141;
    public static final short HIDE_ALL = 2;
    public static final short SHOW_PLACEHOLDERS = 1;
    public static final short SHOW_ALL = 0;
    private short field_1_hide_obj;

    public HideObjRecord() {
    }

    public HideObjRecord(RecordInputStream in) {
        this.field_1_hide_obj = in.readShort();
    }

    public void setHideObj(short hide) {
        this.field_1_hide_obj = hide;
    }

    public short getHideObj() {
        return this.field_1_hide_obj;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[HIDEOBJ]\n");
        buffer.append("    .hideobj         = ").append(Integer.toHexString(getHideObj())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/HIDEOBJ]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getHideObj());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 141;
    }
}
