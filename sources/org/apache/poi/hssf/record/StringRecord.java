package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.cont.ContinuableRecord;
import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/StringRecord.class */
public final class StringRecord extends ContinuableRecord {
    public static final short sid = 519;
    private boolean _is16bitUnicode;
    private String _text;

    public StringRecord() {
    }

    public StringRecord(RecordInputStream in) throws RecordFormatException {
        int field_1_string_length = in.readUShort();
        this._is16bitUnicode = in.readByte() != 0;
        if (this._is16bitUnicode) {
            this._text = in.readUnicodeLEString(field_1_string_length);
        } else {
            this._text = in.readCompressedUnicode(field_1_string_length);
        }
    }

    @Override // org.apache.poi.hssf.record.cont.ContinuableRecord
    protected void serialize(ContinuableRecordOutput out) {
        out.writeShort(this._text.length());
        out.writeStringData(this._text);
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 519;
    }

    public String getString() {
        return this._text;
    }

    public void setString(String string) {
        this._text = string;
        this._is16bitUnicode = StringUtil.hasMultibyte(string);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[STRING]\n");
        buffer.append("    .string            = ").append(this._text).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/STRING]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        StringRecord rec = new StringRecord();
        rec._is16bitUnicode = this._is16bitUnicode;
        rec._text = this._text;
        return rec;
    }
}
