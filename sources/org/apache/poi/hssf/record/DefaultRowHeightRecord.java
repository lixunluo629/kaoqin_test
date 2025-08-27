package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/DefaultRowHeightRecord.class */
public final class DefaultRowHeightRecord extends StandardRecord implements Cloneable {
    public static final short sid = 549;
    private short field_1_option_flags;
    private short field_2_row_height;
    public static final short DEFAULT_ROW_HEIGHT = 255;

    public DefaultRowHeightRecord() {
        this.field_1_option_flags = (short) 0;
        this.field_2_row_height = (short) 255;
    }

    public DefaultRowHeightRecord(RecordInputStream in) {
        this.field_1_option_flags = in.readShort();
        this.field_2_row_height = in.readShort();
    }

    public void setOptionFlags(short flags) {
        this.field_1_option_flags = flags;
    }

    public void setRowHeight(short height) {
        this.field_2_row_height = height;
    }

    public short getOptionFlags() {
        return this.field_1_option_flags;
    }

    public short getRowHeight() {
        return this.field_2_row_height;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DEFAULTROWHEIGHT]\n");
        buffer.append("    .optionflags    = ").append(Integer.toHexString(getOptionFlags())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .rowheight      = ").append(Integer.toHexString(getRowHeight())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/DEFAULTROWHEIGHT]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(getOptionFlags());
        out.writeShort(getRowHeight());
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 4;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 549;
    }

    @Override // org.apache.poi.hssf.record.Record
    public DefaultRowHeightRecord clone() {
        DefaultRowHeightRecord rec = new DefaultRowHeightRecord();
        rec.field_1_option_flags = this.field_1_option_flags;
        rec.field_2_row_height = this.field_2_row_height;
        return rec;
    }
}
