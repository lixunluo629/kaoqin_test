package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/chart/DatRecord.class */
public final class DatRecord extends StandardRecord implements Cloneable {
    public static final short sid = 4195;
    private static final BitField horizontalBorder = BitFieldFactory.getInstance(1);
    private static final BitField verticalBorder = BitFieldFactory.getInstance(2);
    private static final BitField border = BitFieldFactory.getInstance(4);
    private static final BitField showSeriesKey = BitFieldFactory.getInstance(8);
    private short field_1_options;

    public DatRecord() {
    }

    public DatRecord(RecordInputStream in) {
        this.field_1_options = in.readShort();
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DAT]\n");
        buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append((int) getOptions()).append(" )");
        buffer.append(System.getProperty("line.separator"));
        buffer.append("         .horizontalBorder         = ").append(isHorizontalBorder()).append('\n');
        buffer.append("         .verticalBorder           = ").append(isVerticalBorder()).append('\n');
        buffer.append("         .border                   = ").append(isBorder()).append('\n');
        buffer.append("         .showSeriesKey            = ").append(isShowSeriesKey()).append('\n');
        buffer.append("[/DAT]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_options);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 4195;
    }

    @Override // org.apache.poi.hssf.record.Record
    public DatRecord clone() {
        DatRecord rec = new DatRecord();
        rec.field_1_options = this.field_1_options;
        return rec;
    }

    public short getOptions() {
        return this.field_1_options;
    }

    public void setOptions(short field_1_options) {
        this.field_1_options = field_1_options;
    }

    public void setHorizontalBorder(boolean value) {
        this.field_1_options = horizontalBorder.setShortBoolean(this.field_1_options, value);
    }

    public boolean isHorizontalBorder() {
        return horizontalBorder.isSet(this.field_1_options);
    }

    public void setVerticalBorder(boolean value) {
        this.field_1_options = verticalBorder.setShortBoolean(this.field_1_options, value);
    }

    public boolean isVerticalBorder() {
        return verticalBorder.isSet(this.field_1_options);
    }

    public void setBorder(boolean value) {
        this.field_1_options = border.setShortBoolean(this.field_1_options, value);
    }

    public boolean isBorder() {
        return border.isSet(this.field_1_options);
    }

    public void setShowSeriesKey(boolean value) {
        this.field_1_options = showSeriesKey.setShortBoolean(this.field_1_options, value);
    }

    public boolean isShowSeriesKey() {
        return showSeriesKey.isSet(this.field_1_options);
    }
}
