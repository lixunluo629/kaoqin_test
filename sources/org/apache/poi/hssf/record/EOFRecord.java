package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/EOFRecord.class */
public final class EOFRecord extends StandardRecord implements Cloneable {
    public static final short sid = 10;
    public static final int ENCODED_SIZE = 4;
    public static final EOFRecord instance = new EOFRecord();

    private EOFRecord() {
    }

    public EOFRecord(RecordInputStream in) {
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[EOF]\n");
        buffer.append("[/EOF]\n");
        return buffer.toString();
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
        return (short) 10;
    }

    @Override // org.apache.poi.hssf.record.Record
    public EOFRecord clone() {
        return instance;
    }
}
