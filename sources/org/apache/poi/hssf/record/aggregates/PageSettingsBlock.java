package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.BottomMarginRecord;
import org.apache.poi.hssf.record.ContinueRecord;
import org.apache.poi.hssf.record.FooterRecord;
import org.apache.poi.hssf.record.HCenterRecord;
import org.apache.poi.hssf.record.HeaderFooterRecord;
import org.apache.poi.hssf.record.HeaderRecord;
import org.apache.poi.hssf.record.HorizontalPageBreakRecord;
import org.apache.poi.hssf.record.LeftMarginRecord;
import org.apache.poi.hssf.record.Margin;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.apache.poi.hssf.record.PrintSetupRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.RightMarginRecord;
import org.apache.poi.hssf.record.TopMarginRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.UserSViewBegin;
import org.apache.poi.hssf.record.VCenterRecord;
import org.apache.poi.hssf.record.VerticalPageBreakRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/aggregates/PageSettingsBlock.class */
public final class PageSettingsBlock extends RecordAggregate {
    private PageBreakRecord _rowBreaksRecord;
    private PageBreakRecord _columnBreaksRecord;
    private HeaderRecord _header;
    private FooterRecord _footer;
    private HCenterRecord _hCenter;
    private VCenterRecord _vCenter;
    private LeftMarginRecord _leftMargin;
    private RightMarginRecord _rightMargin;
    private TopMarginRecord _topMargin;
    private BottomMarginRecord _bottomMargin;
    private final List<PLSAggregate> _plsRecords;
    private PrintSetupRecord _printSetup;
    private Record _bitmap;
    private HeaderFooterRecord _headerFooter;
    private final List<HeaderFooterRecord> _sviewHeaderFooters;
    private Record _printSize;

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/record/aggregates/PageSettingsBlock$PLSAggregate.class */
    private static final class PLSAggregate extends RecordAggregate {
        private static final ContinueRecord[] EMPTY_CONTINUE_RECORD_ARRAY = new ContinueRecord[0];
        private final Record _pls;
        private ContinueRecord[] _plsContinues;

        public PLSAggregate(RecordStream rs) {
            this._pls = rs.getNext();
            if (rs.peekNextSid() == 60) {
                List<ContinueRecord> temp = new ArrayList<>();
                while (rs.peekNextSid() == 60) {
                    temp.add((ContinueRecord) rs.getNext());
                }
                this._plsContinues = new ContinueRecord[temp.size()];
                temp.toArray(this._plsContinues);
                return;
            }
            this._plsContinues = EMPTY_CONTINUE_RECORD_ARRAY;
        }

        @Override // org.apache.poi.hssf.record.aggregates.RecordAggregate
        public void visitContainedRecords(RecordAggregate.RecordVisitor rv) {
            rv.visitRecord(this._pls);
            ContinueRecord[] arr$ = this._plsContinues;
            for (ContinueRecord _plsContinue : arr$) {
                rv.visitRecord(_plsContinue);
            }
        }
    }

    public PageSettingsBlock(RecordStream rs) {
        this._sviewHeaderFooters = new ArrayList();
        this._plsRecords = new ArrayList();
        while (readARecord(rs)) {
        }
    }

    public PageSettingsBlock() {
        this._sviewHeaderFooters = new ArrayList();
        this._plsRecords = new ArrayList();
        this._rowBreaksRecord = new HorizontalPageBreakRecord();
        this._columnBreaksRecord = new VerticalPageBreakRecord();
        this._header = new HeaderRecord("");
        this._footer = new FooterRecord("");
        this._hCenter = createHCenter();
        this._vCenter = createVCenter();
        this._printSetup = createPrintSetup();
    }

    public static boolean isComponentRecord(int sid) {
        switch (sid) {
            case 20:
            case 21:
            case 26:
            case 27:
            case 38:
            case 39:
            case 40:
            case 41:
            case 51:
            case 77:
            case 131:
            case 132:
            case 161:
            case UnknownRecord.BITMAP_00E9 /* 233 */:
            case 2204:
                return true;
            default:
                return false;
        }
    }

    private boolean readARecord(RecordStream rs) {
        switch (rs.peekNextSid()) {
            case 20:
                checkNotPresent(this._header);
                this._header = (HeaderRecord) rs.getNext();
                return true;
            case 21:
                checkNotPresent(this._footer);
                this._footer = (FooterRecord) rs.getNext();
                return true;
            case 26:
                checkNotPresent(this._columnBreaksRecord);
                this._columnBreaksRecord = (PageBreakRecord) rs.getNext();
                return true;
            case 27:
                checkNotPresent(this._rowBreaksRecord);
                this._rowBreaksRecord = (PageBreakRecord) rs.getNext();
                return true;
            case 38:
                checkNotPresent(this._leftMargin);
                this._leftMargin = (LeftMarginRecord) rs.getNext();
                return true;
            case 39:
                checkNotPresent(this._rightMargin);
                this._rightMargin = (RightMarginRecord) rs.getNext();
                return true;
            case 40:
                checkNotPresent(this._topMargin);
                this._topMargin = (TopMarginRecord) rs.getNext();
                return true;
            case 41:
                checkNotPresent(this._bottomMargin);
                this._bottomMargin = (BottomMarginRecord) rs.getNext();
                return true;
            case 51:
                checkNotPresent(this._printSize);
                this._printSize = rs.getNext();
                return true;
            case 77:
                this._plsRecords.add(new PLSAggregate(rs));
                return true;
            case 131:
                checkNotPresent(this._hCenter);
                this._hCenter = (HCenterRecord) rs.getNext();
                return true;
            case 132:
                checkNotPresent(this._vCenter);
                this._vCenter = (VCenterRecord) rs.getNext();
                return true;
            case 161:
                checkNotPresent(this._printSetup);
                this._printSetup = (PrintSetupRecord) rs.getNext();
                return true;
            case UnknownRecord.BITMAP_00E9 /* 233 */:
                checkNotPresent(this._bitmap);
                this._bitmap = rs.getNext();
                return true;
            case 2204:
                HeaderFooterRecord hf = (HeaderFooterRecord) rs.getNext();
                if (hf.isCurrentSheet()) {
                    this._headerFooter = hf;
                    return true;
                }
                this._sviewHeaderFooters.add(hf);
                return true;
            default:
                return false;
        }
    }

    private void checkNotPresent(Record rec) {
        if (rec != null) {
            throw new RecordFormatException("Duplicate PageSettingsBlock record (sid=0x" + Integer.toHexString(rec.getSid()) + ")");
        }
    }

    private PageBreakRecord getRowBreaksRecord() {
        if (this._rowBreaksRecord == null) {
            this._rowBreaksRecord = new HorizontalPageBreakRecord();
        }
        return this._rowBreaksRecord;
    }

    private PageBreakRecord getColumnBreaksRecord() {
        if (this._columnBreaksRecord == null) {
            this._columnBreaksRecord = new VerticalPageBreakRecord();
        }
        return this._columnBreaksRecord;
    }

    public void setColumnBreak(short column, short fromRow, short toRow) {
        getColumnBreaksRecord().addBreak(column, fromRow, toRow);
    }

    public void removeColumnBreak(int column) {
        getColumnBreaksRecord().removeBreak(column);
    }

    @Override // org.apache.poi.hssf.record.aggregates.RecordAggregate
    public void visitContainedRecords(RecordAggregate.RecordVisitor rv) {
        visitIfPresent(this._rowBreaksRecord, rv);
        visitIfPresent(this._columnBreaksRecord, rv);
        if (this._header == null) {
            rv.visitRecord(new HeaderRecord(""));
        } else {
            rv.visitRecord(this._header);
        }
        if (this._footer == null) {
            rv.visitRecord(new FooterRecord(""));
        } else {
            rv.visitRecord(this._footer);
        }
        visitIfPresent(this._hCenter, rv);
        visitIfPresent(this._vCenter, rv);
        visitIfPresent(this._leftMargin, rv);
        visitIfPresent(this._rightMargin, rv);
        visitIfPresent(this._topMargin, rv);
        visitIfPresent(this._bottomMargin, rv);
        for (PLSAggregate pls : this._plsRecords) {
            pls.visitContainedRecords(rv);
        }
        visitIfPresent(this._printSetup, rv);
        visitIfPresent(this._printSize, rv);
        visitIfPresent(this._headerFooter, rv);
        visitIfPresent(this._bitmap, rv);
    }

    private static void visitIfPresent(Record r, RecordAggregate.RecordVisitor rv) {
        if (r != null) {
            rv.visitRecord(r);
        }
    }

    private static void visitIfPresent(PageBreakRecord r, RecordAggregate.RecordVisitor rv) {
        if (r == null || r.isEmpty()) {
            return;
        }
        rv.visitRecord(r);
    }

    private static HCenterRecord createHCenter() {
        HCenterRecord retval = new HCenterRecord();
        retval.setHCenter(false);
        return retval;
    }

    private static VCenterRecord createVCenter() {
        VCenterRecord retval = new VCenterRecord();
        retval.setVCenter(false);
        return retval;
    }

    private static PrintSetupRecord createPrintSetup() {
        PrintSetupRecord retval = new PrintSetupRecord();
        retval.setPaperSize((short) 1);
        retval.setScale((short) 100);
        retval.setPageStart((short) 1);
        retval.setFitWidth((short) 1);
        retval.setFitHeight((short) 1);
        retval.setOptions((short) 2);
        retval.setHResolution((short) 300);
        retval.setVResolution((short) 300);
        retval.setHeaderMargin(0.5d);
        retval.setFooterMargin(0.5d);
        retval.setCopies((short) 1);
        return retval;
    }

    public HeaderRecord getHeader() {
        return this._header;
    }

    public void setHeader(HeaderRecord newHeader) {
        this._header = newHeader;
    }

    public FooterRecord getFooter() {
        return this._footer;
    }

    public void setFooter(FooterRecord newFooter) {
        this._footer = newFooter;
    }

    public PrintSetupRecord getPrintSetup() {
        return this._printSetup;
    }

    public void setPrintSetup(PrintSetupRecord newPrintSetup) {
        this._printSetup = newPrintSetup;
    }

    private Margin getMarginRec(int marginIndex) {
        switch (marginIndex) {
            case 0:
                return this._leftMargin;
            case 1:
                return this._rightMargin;
            case 2:
                return this._topMargin;
            case 3:
                return this._bottomMargin;
            default:
                throw new IllegalArgumentException("Unknown margin constant:  " + marginIndex);
        }
    }

    public double getMargin(short margin) {
        Margin m = getMarginRec(margin);
        if (m != null) {
            return m.getMargin();
        }
        switch (margin) {
            case 0:
                return 0.75d;
            case 1:
                return 0.75d;
            case 2:
                return 1.0d;
            case 3:
                return 1.0d;
            default:
                throw new IllegalArgumentException("Unknown margin constant:  " + ((int) margin));
        }
    }

    public void setMargin(short margin, double size) {
        Margin m = getMarginRec(margin);
        if (m == null) {
            switch (margin) {
                case 0:
                    this._leftMargin = new LeftMarginRecord();
                    m = this._leftMargin;
                    break;
                case 1:
                    this._rightMargin = new RightMarginRecord();
                    m = this._rightMargin;
                    break;
                case 2:
                    this._topMargin = new TopMarginRecord();
                    m = this._topMargin;
                    break;
                case 3:
                    this._bottomMargin = new BottomMarginRecord();
                    m = this._bottomMargin;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown margin constant:  " + ((int) margin));
            }
        }
        m.setMargin(size);
    }

    private static void shiftBreaks(PageBreakRecord breaks, int start, int stop, int count) {
        Iterator<PageBreakRecord.Break> iterator = breaks.getBreaksIterator();
        List<PageBreakRecord.Break> shiftedBreak = new ArrayList<>();
        while (iterator.hasNext()) {
            PageBreakRecord.Break breakItem = iterator.next();
            int breakLocation = breakItem.main;
            boolean inStart = breakLocation >= start;
            boolean inEnd = breakLocation <= stop;
            if (inStart && inEnd) {
                shiftedBreak.add(breakItem);
            }
        }
        for (PageBreakRecord.Break breakItem2 : shiftedBreak) {
            breaks.removeBreak(breakItem2.main);
            breaks.addBreak((short) (breakItem2.main + count), breakItem2.subFrom, breakItem2.subTo);
        }
    }

    public void setRowBreak(int row, short fromCol, short toCol) {
        getRowBreaksRecord().addBreak((short) row, fromCol, toCol);
    }

    public void removeRowBreak(int row) {
        if (getRowBreaksRecord().getBreaks().length < 1) {
            throw new IllegalArgumentException("Sheet does not define any row breaks");
        }
        getRowBreaksRecord().removeBreak((short) row);
    }

    public boolean isRowBroken(int row) {
        return getRowBreaksRecord().getBreak(row) != null;
    }

    public boolean isColumnBroken(int column) {
        return getColumnBreaksRecord().getBreak(column) != null;
    }

    public void shiftRowBreaks(int startingRow, int endingRow, int count) {
        shiftBreaks(getRowBreaksRecord(), startingRow, endingRow, count);
    }

    public void shiftColumnBreaks(short startingCol, short endingCol, short count) {
        shiftBreaks(getColumnBreaksRecord(), startingCol, endingCol, count);
    }

    public int[] getRowBreaks() {
        return getRowBreaksRecord().getBreaks();
    }

    public int getNumRowBreaks() {
        return getRowBreaksRecord().getNumBreaks();
    }

    public int[] getColumnBreaks() {
        return getColumnBreaksRecord().getBreaks();
    }

    public int getNumColumnBreaks() {
        return getColumnBreaksRecord().getNumBreaks();
    }

    public VCenterRecord getVCenter() {
        return this._vCenter;
    }

    public HCenterRecord getHCenter() {
        return this._hCenter;
    }

    public void addLateHeaderFooter(HeaderFooterRecord rec) {
        if (this._headerFooter != null) {
            throw new IllegalStateException("This page settings block already has a header/footer record");
        }
        if (rec.getSid() != 2204) {
            throw new RecordFormatException("Unexpected header-footer record sid: 0x" + Integer.toHexString(rec.getSid()));
        }
        this._headerFooter = rec;
    }

    public void addLateRecords(RecordStream rs) {
        while (readARecord(rs)) {
        }
    }

    public void positionRecords(List<RecordBase> sheetRecords) {
        List<HeaderFooterRecord> hfRecordsToIterate = new ArrayList<>(this._sviewHeaderFooters);
        final Map<String, HeaderFooterRecord> hfGuidMap = new HashMap<>();
        for (HeaderFooterRecord hf : hfRecordsToIterate) {
            hfGuidMap.put(HexDump.toHex(hf.getGuid()), hf);
        }
        for (RecordBase rb : sheetRecords) {
            if (rb instanceof CustomViewSettingsRecordAggregate) {
                final CustomViewSettingsRecordAggregate cv = (CustomViewSettingsRecordAggregate) rb;
                cv.visitContainedRecords(new RecordAggregate.RecordVisitor() { // from class: org.apache.poi.hssf.record.aggregates.PageSettingsBlock.1
                    @Override // org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor
                    public void visitRecord(Record r) {
                        if (r.getSid() == 426) {
                            String guid = HexDump.toHex(((UserSViewBegin) r).getGuid());
                            HeaderFooterRecord hf2 = (HeaderFooterRecord) hfGuidMap.get(guid);
                            if (hf2 != null) {
                                cv.append(hf2);
                                PageSettingsBlock.this._sviewHeaderFooters.remove(hf2);
                            }
                        }
                    }
                });
            }
        }
    }
}
