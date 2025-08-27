package org.apache.poi.hssf.eventusermodel;

import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.MulBlankRecord;
import org.apache.poi.hssf.record.MulRKRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.StringRecord;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/eventusermodel/MissingRecordAwareHSSFListener.class */
public final class MissingRecordAwareHSSFListener implements HSSFListener {
    private HSSFListener childListener;
    private int lastRowRow;
    private int lastCellRow;
    private int lastCellColumn;

    public MissingRecordAwareHSSFListener(HSSFListener listener) {
        resetCounts();
        this.childListener = listener;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [org.apache.poi.hssf.record.BlankRecord[]] */
    /* JADX WARN: Type inference failed for: r7v0, types: [org.apache.poi.hssf.record.Record] */
    @Override // org.apache.poi.hssf.eventusermodel.HSSFListener
    public void processRecord(Record record) {
        int thisRow;
        int thisColumn;
        NumberRecord[] numberRecordArrConvertRKRecords = null;
        if (record instanceof CellValueRecordInterface) {
            CellValueRecordInterface valueRec = (CellValueRecordInterface) record;
            thisRow = valueRec.getRow();
            thisColumn = valueRec.getColumn();
        } else {
            if (record instanceof StringRecord) {
                this.childListener.processRecord(record);
                return;
            }
            thisRow = -1;
            thisColumn = -1;
            switch (record.getSid()) {
                case 28:
                    NoteRecord nrec = (NoteRecord) record;
                    thisRow = nrec.getRow();
                    thisColumn = nrec.getColumn();
                    break;
                case 189:
                    MulRKRecord mrk = (MulRKRecord) record;
                    numberRecordArrConvertRKRecords = RecordFactory.convertRKRecords(mrk);
                    break;
                case 190:
                    MulBlankRecord mbr = (MulBlankRecord) record;
                    numberRecordArrConvertRKRecords = RecordFactory.convertBlankRecords(mbr);
                    break;
                case 520:
                    RowRecord rowrec = (RowRecord) record;
                    if (this.lastRowRow + 1 < rowrec.getRowNumber()) {
                        for (int i = this.lastRowRow + 1; i < rowrec.getRowNumber(); i++) {
                            MissingRowDummyRecord dr = new MissingRowDummyRecord(i);
                            this.childListener.processRecord(dr);
                        }
                    }
                    this.lastRowRow = rowrec.getRowNumber();
                    this.lastCellColumn = -1;
                    break;
                case 1212:
                    this.childListener.processRecord(record);
                    return;
                case 2057:
                    BOFRecord bof = (BOFRecord) record;
                    if (bof.getType() == 5 || bof.getType() == 16) {
                        resetCounts();
                        break;
                    }
                    break;
            }
        }
        if (numberRecordArrConvertRKRecords != null && numberRecordArrConvertRKRecords.length > 0) {
            thisRow = numberRecordArrConvertRKRecords[0].getRow();
            thisColumn = numberRecordArrConvertRKRecords[0].getColumn();
        }
        if (thisRow != this.lastCellRow && thisRow > 0) {
            if (this.lastCellRow == -1) {
                this.lastCellRow = 0;
            }
            for (int i2 = this.lastCellRow; i2 < thisRow; i2++) {
                int cols = -1;
                if (i2 == this.lastCellRow) {
                    cols = this.lastCellColumn;
                }
                this.childListener.processRecord(new LastCellOfRowDummyRecord(i2, cols));
            }
        }
        if (this.lastCellRow != -1 && this.lastCellColumn != -1 && thisRow == -1) {
            this.childListener.processRecord(new LastCellOfRowDummyRecord(this.lastCellRow, this.lastCellColumn));
            this.lastCellRow = -1;
            this.lastCellColumn = -1;
        }
        if (thisRow != this.lastCellRow) {
            this.lastCellColumn = -1;
        }
        if (this.lastCellColumn != thisColumn - 1) {
            for (int i3 = this.lastCellColumn + 1; i3 < thisColumn; i3++) {
                this.childListener.processRecord(new MissingCellDummyRecord(thisRow, i3));
            }
        }
        if (numberRecordArrConvertRKRecords != null && numberRecordArrConvertRKRecords.length > 0) {
            thisColumn = numberRecordArrConvertRKRecords[numberRecordArrConvertRKRecords.length - 1].getColumn();
        }
        if (thisColumn != -1) {
            this.lastCellColumn = thisColumn;
            this.lastCellRow = thisRow;
        }
        if (numberRecordArrConvertRKRecords != null && numberRecordArrConvertRKRecords.length > 0) {
            for (NumberRecord numberRecord : numberRecordArrConvertRKRecords) {
                this.childListener.processRecord(numberRecord);
            }
            return;
        }
        this.childListener.processRecord(record);
    }

    private void resetCounts() {
        this.lastRowRow = -1;
        this.lastCellRow = -1;
        this.lastCellColumn = -1;
    }
}
