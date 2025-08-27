package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/DrawingRecordForBiffViewer.class */
public final class DrawingRecordForBiffViewer extends AbstractEscherHolderRecord {
    public static final short sid = 236;

    public DrawingRecordForBiffViewer() {
    }

    public DrawingRecordForBiffViewer(RecordInputStream in) {
        super(in);
    }

    public DrawingRecordForBiffViewer(DrawingRecord r) {
        super(convertToInputStream(r));
        convertRawBytesToEscherRecords();
    }

    private static RecordInputStream convertToInputStream(DrawingRecord r) throws RecordFormatException {
        byte[] data = r.serialize();
        RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(data));
        rinp.nextRecord();
        return rinp;
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord
    protected String getRecordName() {
        return "MSODRAWING";
    }

    @Override // org.apache.poi.hssf.record.AbstractEscherHolderRecord, org.apache.poi.hssf.record.Record
    public short getSid() {
        return (short) 236;
    }
}
