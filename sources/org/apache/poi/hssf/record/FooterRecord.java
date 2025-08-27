package org.apache.poi.hssf.record;

import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/FooterRecord.class */
public final class FooterRecord extends HeaderFooterBase implements Cloneable {
    public static final short sid = 21;

    public FooterRecord(String text) {
        super(text);
    }

    public FooterRecord(RecordInputStream in) {
        super(in);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FOOTER]\n");
        buffer.append("    .footer = ").append(getText()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/FOOTER]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 21;
    }

    @Override // org.apache.poi.hssf.record.Record
    public FooterRecord clone() {
        return new FooterRecord(getText());
    }
}
