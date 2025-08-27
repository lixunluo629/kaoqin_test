package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/chart/ChartTitleFormatRecord.class */
public class ChartTitleFormatRecord extends StandardRecord {
    public static final short sid = 4176;
    private CTFormat[] _formats;

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/chart/ChartTitleFormatRecord$CTFormat.class */
    private static final class CTFormat {
        public static final int ENCODED_SIZE = 4;
        private int _offset;
        private int _fontIndex;

        public CTFormat(RecordInputStream in) {
            this._offset = in.readShort();
            this._fontIndex = in.readShort();
        }

        public int getOffset() {
            return this._offset;
        }

        public void setOffset(int newOff) {
            this._offset = newOff;
        }

        public int getFontIndex() {
            return this._fontIndex;
        }

        public void serialize(LittleEndianOutput out) {
            out.writeShort(this._offset);
            out.writeShort(this._fontIndex);
        }
    }

    public ChartTitleFormatRecord(RecordInputStream in) throws RecordFormatException {
        int nRecs = in.readUShort();
        this._formats = new CTFormat[nRecs];
        for (int i = 0; i < nRecs; i++) {
            this._formats[i] = new CTFormat(in);
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._formats.length);
        for (int i = 0; i < this._formats.length; i++) {
            this._formats[i].serialize(out);
        }
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2 + (4 * this._formats.length);
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 4176;
    }

    public int getFormatCount() {
        return this._formats.length;
    }

    public void modifyFormatRun(short oldPos, short newLen) {
        int shift = 0;
        for (int i = 0; i < this._formats.length; i++) {
            CTFormat ctf = this._formats[i];
            if (shift != 0) {
                ctf.setOffset(ctf.getOffset() + shift);
            } else if (oldPos == ctf.getOffset() && i < this._formats.length - 1) {
                CTFormat nextCTF = this._formats[i + 1];
                shift = newLen - (nextCTF.getOffset() - ctf.getOffset());
            }
        }
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CHARTTITLEFORMAT]\n");
        buffer.append("    .format_runs       = ").append(this._formats.length).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        for (int i = 0; i < this._formats.length; i++) {
            CTFormat ctf = this._formats[i];
            buffer.append("       .char_offset= ").append(ctf.getOffset());
            buffer.append(",.fontidx= ").append(ctf.getFontIndex());
            buffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        buffer.append("[/CHARTTITLEFORMAT]\n");
        return buffer.toString();
    }
}
