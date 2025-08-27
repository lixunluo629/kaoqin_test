package org.apache.poi.hssf.record;

import java.util.Locale;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/UserSViewEnd.class */
public final class UserSViewEnd extends StandardRecord {
    public static final short sid = 427;
    private byte[] _rawData;

    public UserSViewEnd(byte[] data) {
        this._rawData = data;
    }

    public UserSViewEnd(RecordInputStream in) {
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
        return (short) 427;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append("USERSVIEWEND").append("] (0x");
        sb.append(Integer.toHexString(sid).toUpperCase(Locale.ROOT) + ")\n");
        sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("[/").append("USERSVIEWEND").append("]\n");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        return cloneViaReserialise();
    }
}
