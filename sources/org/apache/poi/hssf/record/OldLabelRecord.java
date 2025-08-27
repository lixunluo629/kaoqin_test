package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/OldLabelRecord.class */
public final class OldLabelRecord extends OldCellRecord {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) OldLabelRecord.class);
    public static final short biff2_sid = 4;
    public static final short biff345_sid = 516;
    private short field_4_string_len;
    private final byte[] field_5_bytes;
    private CodepageRecord codepage;

    public OldLabelRecord(RecordInputStream in) throws RecordFormatException {
        super(in, in.getSid() == 4);
        if (isBiff2()) {
            this.field_4_string_len = (short) in.readUByte();
        } else {
            this.field_4_string_len = in.readShort();
        }
        this.field_5_bytes = new byte[this.field_4_string_len];
        in.read(this.field_5_bytes, 0, this.field_4_string_len);
        if (in.remaining() > 0) {
            logger.log(3, "LabelRecord data remains: " + in.remaining() + " : " + HexDump.toHex(in.readRemainder()));
        }
    }

    public void setCodePage(CodepageRecord codepage) {
        this.codepage = codepage;
    }

    public short getStringLength() {
        return this.field_4_string_len;
    }

    public String getValue() {
        return OldStringRecord.getString(this.field_5_bytes, this.codepage);
    }

    public int serialize(int offset, byte[] data) {
        throw new RecordFormatException("Old Label Records are supported READ ONLY");
    }

    public int getRecordSize() {
        throw new RecordFormatException("Old Label Records are supported READ ONLY");
    }

    @Override // org.apache.poi.hssf.record.OldCellRecord
    protected void appendValueText(StringBuilder sb) {
        sb.append("    .string_len= ").append(HexDump.shortToHex(this.field_4_string_len)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("    .value       = ").append(getValue()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    }

    @Override // org.apache.poi.hssf.record.OldCellRecord
    protected String getRecordName() {
        return "OLD LABEL";
    }
}
