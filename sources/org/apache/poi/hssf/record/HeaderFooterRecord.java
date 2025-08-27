package org.apache.poi.hssf.record;

import java.util.Arrays;
import java.util.Locale;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/HeaderFooterRecord.class */
public final class HeaderFooterRecord extends StandardRecord implements Cloneable {
    private static final byte[] BLANK_GUID = new byte[16];
    public static final short sid = 2204;
    private byte[] _rawData;

    public HeaderFooterRecord(byte[] data) {
        this._rawData = data;
    }

    public HeaderFooterRecord(RecordInputStream in) {
        this._rawData = in.readRemainder();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.write(this._rawData);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return this._rawData.length;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 2204;
    }

    public byte[] getGuid() {
        byte[] guid = new byte[16];
        System.arraycopy(this._rawData, 12, guid, 0, guid.length);
        return guid;
    }

    public boolean isCurrentSheet() {
        return Arrays.equals(getGuid(), BLANK_GUID);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append("HEADERFOOTER").append("] (0x");
        sb.append(Integer.toHexString(2204).toUpperCase(Locale.ROOT) + ")\n");
        sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("[/").append("HEADERFOOTER").append("]\n");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public HeaderFooterRecord clone() {
        return (HeaderFooterRecord) cloneViaReserialise();
    }
}
