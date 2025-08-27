package org.apache.poi.hssf.eventusermodel.dummyrecord;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/dummyrecord/MissingRowDummyRecord.class */
public final class MissingRowDummyRecord extends DummyRecordBase {
    private int rowNumber;

    @Override // org.apache.poi.hssf.eventusermodel.dummyrecord.DummyRecordBase, org.apache.poi.hssf.record.RecordBase
    public /* bridge */ /* synthetic */ int serialize(int x0, byte[] x1) {
        return super.serialize(x0, x1);
    }

    public MissingRowDummyRecord(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }
}
