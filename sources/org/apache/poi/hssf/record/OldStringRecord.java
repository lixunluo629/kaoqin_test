package org.apache.poi.hssf.record;

import java.io.UnsupportedEncodingException;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.util.RecordFormatException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/OldStringRecord.class */
public final class OldStringRecord {
    public static final short biff2_sid = 7;
    public static final short biff345_sid = 519;
    private short sid;
    private short field_1_string_len;
    private byte[] field_2_bytes;
    private CodepageRecord codepage;

    public OldStringRecord(RecordInputStream in) throws RecordFormatException {
        this.sid = in.getSid();
        if (in.getSid() == 7) {
            this.field_1_string_len = (short) in.readUByte();
        } else {
            this.field_1_string_len = in.readShort();
        }
        this.field_2_bytes = new byte[this.field_1_string_len];
        in.read(this.field_2_bytes, 0, this.field_1_string_len);
    }

    public boolean isBiff2() {
        return this.sid == 7;
    }

    public short getSid() {
        return this.sid;
    }

    public void setCodePage(CodepageRecord codepage) {
        this.codepage = codepage;
    }

    public String getString() {
        return getString(this.field_2_bytes, this.codepage);
    }

    protected static String getString(byte[] data, CodepageRecord codepage) {
        int cp = 1252;
        if (codepage != null) {
            cp = codepage.getCodepage() & 65535;
        }
        try {
            return CodePageUtil.getStringFromCodePage(data, cp);
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalArgumentException("Unsupported codepage requested", uee);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[OLD STRING]\n");
        buffer.append("    .string            = ").append(getString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/OLD STRING]\n");
        return buffer.toString();
    }
}
