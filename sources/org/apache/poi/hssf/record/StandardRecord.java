package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianOutput;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/StandardRecord.class */
public abstract class StandardRecord extends Record {
    protected abstract int getDataSize();

    protected abstract void serialize(LittleEndianOutput littleEndianOutput);

    @Override // org.apache.poi.hssf.record.RecordBase
    public final int getRecordSize() {
        return 4 + getDataSize();
    }

    @Override // org.apache.poi.hssf.record.RecordBase
    public final int serialize(int offset, byte[] data) {
        int dataSize = getDataSize();
        int recSize = 4 + dataSize;
        LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(data, offset, recSize);
        out.writeShort(getSid());
        out.writeShort(dataSize);
        serialize(out);
        if (out.getWriteIndex() - offset != recSize) {
            throw new IllegalStateException("Error in serialization of (" + getClass().getName() + "): Incorrect number of bytes written - expected " + recSize + " but got " + (out.getWriteIndex() - offset));
        }
        return recSize;
    }
}
