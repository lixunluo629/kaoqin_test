package org.apache.poi.hssf.record.chart;

import java.util.Arrays;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/chart/SeriesListRecord.class */
public final class SeriesListRecord extends StandardRecord {
    public static final short sid = 4118;
    private short[] field_1_seriesNumbers;

    public SeriesListRecord(short[] seriesNumbers) {
        this.field_1_seriesNumbers = seriesNumbers == null ? null : (short[]) seriesNumbers.clone();
    }

    public SeriesListRecord(RecordInputStream in) throws RecordFormatException {
        int nItems = in.readUShort();
        short[] ss = new short[nItems];
        for (int i = 0; i < nItems; i++) {
            ss[i] = in.readShort();
        }
        this.field_1_seriesNumbers = ss;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SERIESLIST]\n");
        buffer.append("    .seriesNumbers= ").append(" (").append(Arrays.toString(getSeriesNumbers())).append(" )");
        buffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/SERIESLIST]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        int nItems = this.field_1_seriesNumbers.length;
        out.writeShort(nItems);
        for (int i = 0; i < nItems; i++) {
            out.writeShort(this.field_1_seriesNumbers[i]);
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return (this.field_1_seriesNumbers.length * 2) + 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 4118;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        return new SeriesListRecord(this.field_1_seriesNumbers);
    }

    public short[] getSeriesNumbers() {
        return this.field_1_seriesNumbers;
    }
}
