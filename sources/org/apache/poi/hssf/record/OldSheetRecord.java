package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/OldSheetRecord.class */
public final class OldSheetRecord {
    public static final short sid = 133;
    private int field_1_position_of_BOF;
    private int field_2_visibility;
    private int field_3_type;
    private byte[] field_5_sheetname;
    private CodepageRecord codepage;

    public OldSheetRecord(RecordInputStream in) throws RecordFormatException {
        this.field_1_position_of_BOF = in.readInt();
        this.field_2_visibility = in.readUByte();
        this.field_3_type = in.readUByte();
        int field_4_sheetname_length = in.readUByte();
        this.field_5_sheetname = new byte[field_4_sheetname_length];
        in.read(this.field_5_sheetname, 0, field_4_sheetname_length);
    }

    public void setCodePage(CodepageRecord codepage) {
        this.codepage = codepage;
    }

    public short getSid() {
        return (short) 133;
    }

    public int getPositionOfBof() {
        return this.field_1_position_of_BOF;
    }

    public String getSheetname() {
        return OldStringRecord.getString(this.field_5_sheetname, this.codepage);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BOUNDSHEET]\n");
        buffer.append("    .bof        = ").append(HexDump.intToHex(getPositionOfBof())).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .visibility = ").append(HexDump.shortToHex(this.field_2_visibility)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .type       = ").append(HexDump.byteToHex(this.field_3_type)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("    .sheetname  = ").append(getSheetname()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/BOUNDSHEET]\n");
        return buffer.toString();
    }
}
