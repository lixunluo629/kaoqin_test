package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/InterfaceEndRecord.class */
public final class InterfaceEndRecord extends StandardRecord {
    public static final short sid = 226;
    public static final InterfaceEndRecord instance = new InterfaceEndRecord();

    private InterfaceEndRecord() {
    }

    public static Record create(RecordInputStream in) {
        switch (in.remaining()) {
            case 0:
                return instance;
            case 2:
                return new InterfaceHdrRecord(in);
            default:
                throw new RecordFormatException("Invalid record data size: " + in.remaining());
        }
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        return "[INTERFACEEND/]\n";
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    public void serialize(LittleEndianOutput out) {
    }

    @Override // org.apache.poi.hssf.record.StandardRecord
    protected int getDataSize() {
        return 0;
    }

    @Override // org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 226;
    }
}
