package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/PrintHeadersRecord.class */
public final class PrintHeadersRecord extends StandardRecord {
    public static final short sid = 42;
    private short field_1_print_headers;

    public PrintHeadersRecord() {
    }

    public PrintHeadersRecord(RecordInputStream in) {
        this.field_1_print_headers = in.readShort();
    }

    public void setPrintHeaders(boolean p) {
        if (p) {
            this.field_1_print_headers = (short) 1;
        } else {
            this.field_1_print_headers = (short) 0;
        }
    }

    public boolean getPrintHeaders() {
        return this.field_1_print_headers == 1;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PRINTHEADERS]\n");
        buffer.append("    .printheaders   = ").append(getPrintHeaders()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/PRINTHEADERS]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_print_headers);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 42;
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        PrintHeadersRecord rec = new PrintHeadersRecord();
        rec.field_1_print_headers = this.field_1_print_headers;
        return rec;
    }
}
