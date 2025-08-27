package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/TableStylesRecord.class */
public final class TableStylesRecord extends StandardRecord {
    public static final short sid = 2190;
    private int rt;
    private int grbitFrt;
    private byte[] unused = new byte[8];
    private int cts;
    private String rgchDefListStyle;
    private String rgchDefPivotStyle;

    public TableStylesRecord(RecordInputStream in) throws RecordFormatException {
        this.rt = in.readUShort();
        this.grbitFrt = in.readUShort();
        in.readFully(this.unused);
        this.cts = in.readInt();
        int cchDefListStyle = in.readUShort();
        int cchDefPivotStyle = in.readUShort();
        this.rgchDefListStyle = in.readUnicodeLEString(cchDefListStyle);
        this.rgchDefPivotStyle = in.readUnicodeLEString(cchDefPivotStyle);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected void serialize(LittleEndianOutput out) {
        out.writeShort(this.rt);
        out.writeShort(this.grbitFrt);
        out.write(this.unused);
        out.writeInt(this.cts);
        out.writeShort(this.rgchDefListStyle.length());
        out.writeShort(this.rgchDefPivotStyle.length());
        StringUtil.putUnicodeLE(this.rgchDefListStyle, out);
        StringUtil.putUnicodeLE(this.rgchDefPivotStyle, out);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 20 + (2 * this.rgchDefListStyle.length()) + (2 * this.rgchDefPivotStyle.length());
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 2190;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TABLESTYLES]\n");
        buffer.append("    .rt      =").append(HexDump.shortToHex(this.rt)).append('\n');
        buffer.append("    .grbitFrt=").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
        buffer.append("    .unused  =").append(HexDump.toHex(this.unused)).append('\n');
        buffer.append("    .cts=").append(HexDump.intToHex(this.cts)).append('\n');
        buffer.append("    .rgchDefListStyle=").append(this.rgchDefListStyle).append('\n');
        buffer.append("    .rgchDefPivotStyle=").append(this.rgchDefPivotStyle).append('\n');
        buffer.append("[/TABLESTYLES]\n");
        return buffer.toString();
    }
}
