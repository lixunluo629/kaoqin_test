package org.apache.poi.hssf.record;

import java.util.Locale;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/UserSViewBegin.class */
public final class UserSViewBegin extends StandardRecord {
    public static final short sid = 426;
    private byte[] _rawData;

    public UserSViewBegin(byte[] data) {
        this._rawData = data;
    }

    public UserSViewBegin(RecordInputStream in) {
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
        return (short) 426;
    }

    public byte[] getGuid() {
        byte[] guid = new byte[16];
        System.arraycopy(this._rawData, 0, guid, 0, guid.length);
        return guid;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append("USERSVIEWBEGIN").append("] (0x");
        sb.append(Integer.toHexString(sid).toUpperCase(Locale.ROOT) + ")\n");
        sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("[/").append("USERSVIEWBEGIN").append("]\n");
        return sb.toString();
    }

    @Override // org.apache.poi.hssf.record.Record
    public Object clone() {
        return cloneViaReserialise();
    }
}
