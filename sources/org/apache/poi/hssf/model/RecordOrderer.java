package org.apache.poi.hssf.model;

import java.util.List;
import org.apache.poi.hssf.record.ArrayRecord;
import org.apache.poi.hssf.record.DVALRecord;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.DrawingSelectionRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.FeatRecord;
import org.apache.poi.hssf.record.GutsRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.TableRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/model/RecordOrderer.class */
final class RecordOrderer {
    private RecordOrderer() {
    }

    public static void addNewSheetRecord(List<RecordBase> sheetRecords, RecordBase newRecord) {
        int index = findSheetInsertPos(sheetRecords, newRecord.getClass());
        sheetRecords.add(index, newRecord);
    }

    private static int findSheetInsertPos(List<RecordBase> records, Class<? extends RecordBase> recClass) {
        if (recClass == DataValidityTable.class) {
            return findDataValidationTableInsertPos(records);
        }
        if (recClass == MergedCellsTable.class) {
            return findInsertPosForNewMergedRecordTable(records);
        }
        if (recClass == ConditionalFormattingTable.class) {
            return findInsertPosForNewCondFormatTable(records);
        }
        if (recClass == GutsRecord.class) {
            return getGutsRecordInsertPos(records);
        }
        if (recClass == PageSettingsBlock.class) {
            return getPageBreakRecordInsertPos(records);
        }
        if (recClass == WorksheetProtectionBlock.class) {
            return getWorksheetProtectionBlockInsertPos(records);
        }
        throw new RuntimeException("Unexpected record class (" + recClass.getName() + ")");
    }

    private static int getWorksheetProtectionBlockInsertPos(List<RecordBase> records) {
        int i = getDimensionsIndex(records);
        while (i > 0) {
            i--;
            Object rb = records.get(i);
            if (!isProtectionSubsequentRecord(rb)) {
                return i + 1;
            }
        }
        throw new IllegalStateException("did not find insert pos for protection block");
    }

    private static boolean isProtectionSubsequentRecord(Object rb) {
        if (rb instanceof ColumnInfoRecordsAggregate) {
            return true;
        }
        if (rb instanceof Record) {
            Record record = (Record) rb;
            switch (record.getSid()) {
                case 85:
                case 144:
                    break;
            }
            return true;
        }
        return false;
    }

    private static int getPageBreakRecordInsertPos(List<RecordBase> records) {
        int dimensionsIndex = getDimensionsIndex(records);
        int i = dimensionsIndex - 1;
        while (i > 0) {
            i--;
            Object rb = records.get(i);
            if (isPageBreakPriorRecord(rb)) {
                return i + 1;
            }
        }
        throw new RuntimeException("Did not find insert point for GUTS");
    }

    private static boolean isPageBreakPriorRecord(Object rb) {
        if (rb instanceof Record) {
            Record record = (Record) rb;
            switch (record.getSid()) {
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 34:
                case 42:
                case 43:
                case 94:
                case 95:
                case 129:
                case 130:
                case 523:
                case 549:
                case 2057:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private static int findInsertPosForNewCondFormatTable(List<RecordBase> records) {
        for (int i = records.size() - 2; i >= 0; i--) {
            Object rb = records.get(i);
            if (rb instanceof MergedCellsTable) {
                return i + 1;
            }
            if (!(rb instanceof DataValidityTable)) {
                Record rec = (Record) rb;
                switch (rec.getSid()) {
                    case 29:
                    case 65:
                    case 153:
                    case 160:
                    case UnknownRecord.PHONETICPR_00EF /* 239 */:
                    case 351:
                    case 574:
                        return i + 1;
                }
            }
        }
        throw new RuntimeException("Did not find Window2 record");
    }

    private static int findInsertPosForNewMergedRecordTable(List<RecordBase> records) {
        for (int i = records.size() - 2; i >= 0; i--) {
            Object rb = records.get(i);
            if (rb instanceof Record) {
                Record rec = (Record) rb;
                switch (rec.getSid()) {
                    case 29:
                    case 65:
                    case 153:
                    case 160:
                    case 574:
                        return i + 1;
                }
            }
        }
        throw new RuntimeException("Did not find Window2 record");
    }

    private static int findDataValidationTableInsertPos(List<RecordBase> records) {
        int i = records.size() - 1;
        if (!(records.get(i) instanceof EOFRecord)) {
            throw new IllegalStateException("Last sheet record should be EOFRecord");
        }
        while (i > 0) {
            i--;
            RecordBase rb = records.get(i);
            if (isDVTPriorRecord(rb)) {
                Record nextRec = (Record) records.get(i + 1);
                if (!isDVTSubsequentRecord(nextRec.getSid())) {
                    throw new IllegalStateException("Unexpected (" + nextRec.getClass().getName() + ") found after (" + rb.getClass().getName() + ")");
                }
                return i + 1;
            }
            Record rec = (Record) rb;
            if (!isDVTSubsequentRecord(rec.getSid())) {
                throw new IllegalStateException("Unexpected (" + rec.getClass().getName() + ") while looking for DV Table insert pos");
            }
        }
        return 0;
    }

    private static boolean isDVTPriorRecord(RecordBase rb) {
        if ((rb instanceof MergedCellsTable) || (rb instanceof ConditionalFormattingTable)) {
            return true;
        }
        short sid = ((Record) rb).getSid();
        switch (sid) {
            case 29:
            case 65:
            case 153:
            case 160:
            case UnknownRecord.PHONETICPR_00EF /* 239 */:
            case 351:
            case HyperlinkRecord.sid /* 440 */:
            case UnknownRecord.CODENAME_1BA /* 442 */:
            case 574:
            case 2048:
                return true;
            default:
                return false;
        }
    }

    private static boolean isDVTSubsequentRecord(short sid) {
        switch (sid) {
            case 10:
            case UnknownRecord.SHEETEXT_0862 /* 2146 */:
            case 2151:
            case FeatRecord.sid /* 2152 */:
            case UnknownRecord.PLV_MAC /* 2248 */:
                return true;
            default:
                return false;
        }
    }

    private static int getDimensionsIndex(List<RecordBase> records) {
        int nRecs = records.size();
        for (int i = 0; i < nRecs; i++) {
            if (records.get(i) instanceof DimensionsRecord) {
                return i;
            }
        }
        throw new RuntimeException("DimensionsRecord not found");
    }

    private static int getGutsRecordInsertPos(List<RecordBase> records) {
        int dimensionsIndex = getDimensionsIndex(records);
        int i = dimensionsIndex - 1;
        while (i > 0) {
            i--;
            RecordBase rb = records.get(i);
            if (isGutsPriorRecord(rb)) {
                return i + 1;
            }
        }
        throw new RuntimeException("Did not find insert point for GUTS");
    }

    private static boolean isGutsPriorRecord(RecordBase rb) {
        if (rb instanceof Record) {
            Record record = (Record) rb;
            switch (record.getSid()) {
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 34:
                case 42:
                case 43:
                case 94:
                case 95:
                case 130:
                case 523:
                case 2057:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static boolean isEndOfRowBlock(int sid) {
        switch (sid) {
            case 10:
                throw new RuntimeException("Found EOFRecord before WindowTwoRecord was encountered");
            case 61:
            case 93:
            case 125:
            case 128:
            case 176:
            case 236:
            case DrawingSelectionRecord.sid /* 237 */:
            case TextObjectRecord.sid /* 438 */:
            case 574:
                return true;
            case DVALRecord.sid /* 434 */:
                return true;
            default:
                return PageSettingsBlock.isComponentRecord(sid);
        }
    }

    public static boolean isRowBlockRecord(int sid) {
        switch (sid) {
            case 6:
            case 253:
            case 513:
            case 515:
            case 516:
            case 517:
            case 520:
            case ArrayRecord.sid /* 545 */:
            case TableRecord.sid /* 566 */:
            case RKRecord.sid /* 638 */:
            case 1212:
                return true;
            default:
                return false;
        }
    }
}
