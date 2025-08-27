package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/OldCellRecord.class */
public abstract class OldCellRecord {
    private final short sid;
    private final boolean isBiff2;
    private final int field_1_row;
    private final short field_2_column;
    private int field_3_cell_attrs;
    private short field_3_xf_index;

    protected abstract void appendValueText(StringBuilder sb);

    protected abstract String getRecordName();

    protected OldCellRecord(RecordInputStream in, boolean isBiff2) {
        this.sid = in.getSid();
        this.isBiff2 = isBiff2;
        this.field_1_row = in.readUShort();
        this.field_2_column = in.readShort();
        if (isBiff2) {
            this.field_3_cell_attrs = in.readUShort() << 8;
            this.field_3_cell_attrs += in.readUByte();
        } else {
            this.field_3_xf_index = in.readShort();
        }
    }

    public final int getRow() {
        return this.field_1_row;
    }

    public final short getColumn() {
        return this.field_2_column;
    }

    public final short getXFIndex() {
        return this.field_3_xf_index;
    }

    public int getCellAttrs() {
        return this.field_3_cell_attrs;
    }

    public boolean isBiff2() {
        return this.isBiff2;
    }

    public short getSid() {
        return this.sid;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        String recordName = getRecordName();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(recordName).append("]\n");
        sb.append("    .row    = ").append(HexDump.shortToHex(getRow())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .col    = ").append(HexDump.shortToHex(getColumn())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (isBiff2()) {
            sb.append("    .cellattrs = ").append(HexDump.shortToHex(getCellAttrs())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        } else {
            sb.append("    .xfindex   = ").append(HexDump.shortToHex(getXFIndex())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        appendValueText(sb);
        sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("[/").append(recordName).append("]\n");
        return sb.toString();
    }
}
