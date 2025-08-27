package org.apache.poi.hssf.record;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/ProtectionRev4Record.class */
public final class ProtectionRev4Record extends StandardRecord {
    public static final short sid = 431;
    private static final BitField protectedFlag = BitFieldFactory.getInstance(1);
    private int _options;

    private ProtectionRev4Record(int options) {
        this._options = options;
    }

    public ProtectionRev4Record(boolean protect) {
        this(0);
        setProtect(protect);
    }

    public ProtectionRev4Record(RecordInputStream in) {
        this(in.readUShort());
    }

    public void setProtect(boolean protect) {
        this._options = protectedFlag.setBoolean(this._options, protect);
    }

    public boolean getProtect() {
        return protectedFlag.isSet(this._options);
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[PROT4REV]\n");
        buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        buffer.append("[/PROT4REV]\n");
        return buffer.toString();
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._options);
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 2;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 431;
    }
}
