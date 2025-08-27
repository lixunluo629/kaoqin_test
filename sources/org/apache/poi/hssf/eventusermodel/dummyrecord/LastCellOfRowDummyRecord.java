package org.apache.poi.hssf.eventusermodel.dummyrecord;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/dummyrecord/LastCellOfRowDummyRecord.class */
public final class LastCellOfRowDummyRecord extends DummyRecordBase {
    private final int row;
    private final int lastColumnNumber;

    @Override // org.apache.poi.hssf.eventusermodel.dummyrecord.DummyRecordBase, org.apache.poi.hssf.record.RecordBase
    public /* bridge */ /* synthetic */ int serialize(int x0, byte[] x1) {
        return super.serialize(x0, x1);
    }

    public LastCellOfRowDummyRecord(int row, int lastColumnNumber) {
        this.row = row;
        this.lastColumnNumber = lastColumnNumber;
    }

    public int getRow() {
        return this.row;
    }

    public int getLastColumnNumber() {
        return this.lastColumnNumber;
    }

    @Override // org.apache.poi.hssf.record.Record
    public String toString() {
        return "End-of-Row for Row=" + this.row + " at Column=" + this.lastColumnNumber;
    }
}
