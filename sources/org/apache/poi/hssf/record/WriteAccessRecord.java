package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.util.StringUtil;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/WriteAccessRecord.class */
public final class WriteAccessRecord extends StandardRecord {
    public static final short sid = 92;
    private static final byte PAD_CHAR = 32;
    private static final int DATA_SIZE = 112;
    private String field_1_username;
    private static final byte[] PADDING = new byte[112];

    static {
        Arrays.fill(PADDING, (byte) 32);
    }

    public WriteAccessRecord() {
        setUsername("");
    }

    public WriteAccessRecord(RecordInputStream in) throws RecordFormatException {
        String rawText;
        if (in.remaining() > 112) {
            throw new RecordFormatException("Expected data size (112) but got (" + in.remaining() + ")");
        }
        int nChars = in.readUShort();
        int is16BitFlag = in.readUByte();
        if (nChars > 112 || (is16BitFlag & 254) != 0) {
            byte[] data = new byte[3 + in.remaining()];
            LittleEndian.putUShort(data, 0, nChars);
            LittleEndian.putByte(data, 2, is16BitFlag);
            in.readFully(data, 3, data.length - 3);
            String rawValue = new String(data, StringUtil.UTF8);
            setUsername(rawValue.trim());
            return;
        }
        if ((is16BitFlag & 1) == 0) {
            rawText = StringUtil.readCompressedUnicode(in, nChars);
        } else {
            rawText = StringUtil.readUnicodeLE(in, nChars);
        }
        this.field_1_username = rawText.trim();
        for (int padSize = in.remaining(); padSize > 0; padSize--) {
            in.readUByte();
        }
    }

    public void setUsername(String username) {
        boolean is16bit = StringUtil.hasMultibyte(username);
        int encodedByteCount = 3 + (username.length() * (is16bit ? 2 : 1));
        int paddingSize = 112 - encodedByteCount;
        if (paddingSize < 0) {
            throw new IllegalArgumentException("Name is too long: " + username);
        }
        this.field_1_username = username;
    }

    public String getUsername() {
        return this.field_1_username;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WRITEACCESS]\n");
        buffer.append("    .name = ").append(this.field_1_username).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/WRITEACCESS]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        String username = getUsername();
        boolean is16bit = StringUtil.hasMultibyte(username);
        out.writeShort(username.length());
        out.writeByte(is16bit ? 1 : 0);
        if (is16bit) {
            StringUtil.putUnicodeLE(username, out);
        } else {
            StringUtil.putCompressedUnicode(username, out);
        }
        int encodedByteCount = 3 + (username.length() * (is16bit ? 2 : 1));
        int paddingSize = 112 - encodedByteCount;
        out.write(PADDING, 0, paddingSize);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 112;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 92;
    }
}
