package org.apache.poi.hssf.eventusermodel.dummyrecord;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/dummyrecord/MissingCellDummyRecord.class */
public final class MissingCellDummyRecord extends DummyRecordBase {
    private int row;
    private int column;

    @Override // org.apache.poi.hssf.eventusermodel.dummyrecord.DummyRecordBase, org.apache.poi.hssf.record.RecordBase
    public /* bridge */ /* synthetic */ int serialize(int x0, byte[] x1) {
        return super.serialize(x0, x1);
    }

    public MissingCellDummyRecord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
