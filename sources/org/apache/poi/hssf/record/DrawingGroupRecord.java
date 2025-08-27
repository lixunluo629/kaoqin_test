package org.apache.poi.hssf.record;

import java.util.List;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.NullEscherSerializationListener;
import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/DrawingGroupRecord.class */
public final class DrawingGroupRecord extends AbstractEscherHolderRecord {
    public static final short sid = 235;
    static final int MAX_RECORD_SIZE = 8228;
    private static final int MAX_DATA_SIZE = 8224;

    public DrawingGroupRecord() {
    }

    public DrawingGroupRecord(RecordInputStream in) {
        super(in);
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord
    protected String getRecordName() {
        return "MSODRAWINGGROUP";
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord, org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 235;
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord, org.apache.poi.hssf.record.RecordBase
    public int serialize(int offset, byte[] data) {
        byte[] rawData = getRawData();
        if (getEscherRecords().size() == 0 && rawData != null) {
            return writeData(offset, data, rawData);
        }
        byte[] buffer = new byte[getRawDataSize()];
        int pos = 0;
        for (EscherRecord r : getEscherRecords()) {
            pos += r.serialize(pos, buffer, new NullEscherSerializationListener());
        }
        return writeData(offset, data, buffer);
    }

    public void processChildRecords() {
        convertRawBytesToEscherRecords();
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord, org.apache.poi.hssf.record.RecordBase
    public int getRecordSize() {
        return grossSizeFromDataSize(getRawDataSize());
    }

    private int getRawDataSize() {
        List<EscherRecord> escherRecords = getEscherRecords();
        byte[] rawData = getRawData();
        if (escherRecords.size() == 0 && rawData != null) {
            return rawData.length;
        }
        int size = 0;
        for (EscherRecord r : escherRecords) {
            size += r.getRecordSize();
        }
        return size;
    }

    static int grossSizeFromDataSize(int dataSize) {
        return dataSize + ((((dataSize - 1) / 8224) + 1) * 4);
    }

    private int writeData(int offset, byte[] data, byte[] rawData) {
        int writtenActualData = 0;
        int writtenRawData = 0;
        while (writtenRawData < rawData.length) {
            int segmentLength = Math.min(rawData.length - writtenRawData, 8224);
            if (writtenRawData / 8224 >= 2) {
                writeContinueHeader(data, offset, segmentLength);
            } else {
                writeHeader(data, offset, segmentLength);
            }
            int offset2 = offset + 4;
            System.arraycopy(rawData, writtenRawData, data, offset2, segmentLength);
            offset = offset2 + segmentLength;
            writtenRawData += segmentLength;
            writtenActualData = writtenActualData + 4 + segmentLength;
        }
        return writtenActualData;
    }

    private void writeHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, 0 + offset, getSid());
        LittleEndian.putShort(data, 2 + offset, (short) sizeExcludingHeader);
    }

    private void writeContinueHeader(byte[] data, int offset, int sizeExcludingHeader) {
        LittleEndian.putShort(data, 0 + offset, (short) 60);
        LittleEndian.putShort(data, 2 + offset, (short) sizeExcludingHeader);
    }
}
