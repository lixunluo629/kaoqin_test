package org.apache.poi.hssf.usermodel;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.model.DrawingManager2;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.AutoFilterInfoRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.hssf.record.DrawingRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SCLRecord;
import org.apache.poi.hssf.record.WSBoolRecord;
import org.apache.poi.hssf.record.aggregates.DataValidityTable;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.apache.poi.hssf.record.aggregates.RecordAggregate;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.usermodel.helpers.HSSFRowShifter;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.UnionPtg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Shape;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.helpers.RowShifter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.PaneInformation;
import org.apache.poi.ss.util.SSCellRange;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.util.Configurator;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFSheet.class */
public final class HSSFSheet implements Sheet {
    private static final int DEBUG = 1;
    private static final float PX_DEFAULT = 32.0f;
    private static final float PX_MODIFIED = 36.56f;
    private final InternalSheet _sheet;
    private final TreeMap<Integer, HSSFRow> _rows;
    protected final InternalWorkbook _book;
    protected final HSSFWorkbook _workbook;
    private HSSFPatriarch _patriarch;
    private int _firstrow;
    private int _lastrow;
    private static final POILogger log = POILogFactory.getLogger((Class<?>) HSSFSheet.class);
    public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFSheet.RowInitialCapacity", 20);

    protected HSSFSheet(HSSFWorkbook workbook) {
        this._sheet = InternalSheet.createSheet();
        this._rows = new TreeMap<>();
        this._workbook = workbook;
        this._book = workbook.getWorkbook();
    }

    protected HSSFSheet(HSSFWorkbook workbook, InternalSheet sheet) {
        this._sheet = sheet;
        this._rows = new TreeMap<>();
        this._workbook = workbook;
        this._book = workbook.getWorkbook();
        setPropertiesFromSheet(sheet);
    }

    HSSFSheet cloneSheet(HSSFWorkbook workbook) {
        getDrawingPatriarch();
        HSSFSheet sheet = new HSSFSheet(workbook, this._sheet.cloneSheet());
        int pos = sheet._sheet.findFirstRecordLocBySid((short) 236);
        DrawingRecord dr = (DrawingRecord) sheet._sheet.findFirstRecordBySid((short) 236);
        if (null != dr) {
            sheet._sheet.getRecords().remove(dr);
        }
        if (getDrawingPatriarch() != null) {
            HSSFPatriarch patr = HSSFPatriarch.createPatriarch(getDrawingPatriarch(), sheet);
            sheet._sheet.getRecords().add(pos, patr.getBoundAggregate());
            sheet._patriarch = patr;
        }
        return sheet;
    }

    protected void preSerialize() {
        if (this._patriarch != null) {
            this._patriarch.preSerialize();
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFWorkbook getWorkbook() {
        return this._workbook;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setPropertiesFromSheet(InternalSheet sheet) {
        RowRecord nextRow = sheet.getNextRow();
        while (true) {
            RowRecord row = nextRow;
            if (row == null) {
                break;
            }
            createRowFromRecord(row);
            nextRow = sheet.getNextRow();
        }
        Iterator<CellValueRecordInterface> iter = sheet.getCellValueIterator();
        long timestart = System.currentTimeMillis();
        if (log.check(1)) {
            log.log(1, "Time at start of cell creating in HSSF sheet = ", Long.valueOf(timestart));
        }
        HSSFRow lastrow = null;
        while (iter.hasNext()) {
            CellValueRecordInterface next = iter.next();
            long cellstart = System.currentTimeMillis();
            HSSFRow hrow = lastrow;
            if (hrow == null || hrow.getRowNum() != next.getRow()) {
                hrow = getRow(next.getRow());
                lastrow = hrow;
                if (hrow == null) {
                    RowRecord rowRec = new RowRecord(next.getRow());
                    sheet.addRow(rowRec);
                    hrow = createRowFromRecord(rowRec);
                }
            }
            if (log.check(1)) {
                if (next instanceof Record) {
                    log.log(1, "record id = " + Integer.toHexString(((Record) next).getSid()));
                } else {
                    log.log(1, "record = " + next);
                }
            }
            hrow.createCellFromRecord(next);
            if (log.check(1)) {
                log.log(1, "record took ", Long.valueOf(System.currentTimeMillis() - cellstart));
            }
        }
        if (log.check(1)) {
            log.log(1, "total sheet cell creation took ", Long.valueOf(System.currentTimeMillis() - timestart));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFRow createRow(int rownum) {
        HSSFRow row = new HSSFRow(this._workbook, this, rownum);
        row.setHeight(getDefaultRowHeight());
        row.getRowRecord().setBadFontHeight(false);
        addRow(row, true);
        return row;
    }

    private HSSFRow createRowFromRecord(RowRecord row) {
        HSSFRow hrow = new HSSFRow(this._workbook, this, row);
        addRow(hrow, false);
        return hrow;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void removeRow(Row row) {
        HSSFRow hrow = (HSSFRow) row;
        if (row.getSheet() != this) {
            throw new IllegalArgumentException("Specified row does not belong to this sheet");
        }
        for (Cell cell : row) {
            HSSFCell xcell = (HSSFCell) cell;
            if (xcell.isPartOfArrayFormulaGroup()) {
                String msg = "Row[rownum=" + row.getRowNum() + "] contains cell(s) included in a multi-cell array formula. You cannot change part of an array.";
                xcell.notifyArrayFormulaChanging(msg);
            }
        }
        if (this._rows.size() > 0) {
            Integer key = Integer.valueOf(row.getRowNum());
            HSSFRow removedRow = this._rows.remove(key);
            if (removedRow != row) {
                throw new IllegalArgumentException("Specified row does not belong to this sheet");
            }
            if (hrow.getRowNum() == getLastRowNum()) {
                this._lastrow = findLastRow(this._lastrow);
            }
            if (hrow.getRowNum() == getFirstRowNum()) {
                this._firstrow = findFirstRow(this._firstrow);
            }
            this._sheet.removeRow(hrow.getRowRecord());
        }
    }

    private int findLastRow(int lastrow) {
        HSSFRow r;
        if (lastrow < 1) {
            return 0;
        }
        int rownum = lastrow - 1;
        HSSFRow row = getRow(rownum);
        while (true) {
            r = row;
            if (r != null || rownum <= 0) {
                break;
            }
            rownum--;
            row = getRow(rownum);
        }
        if (r == null) {
            return 0;
        }
        return rownum;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000b */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int findFirstRow(int r4) {
        /*
            r3 = this;
            r0 = r4
            r1 = 1
            int r0 = r0 + r1
            r5 = r0
            r0 = r3
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFRow r0 = r0.getRow(r1)
            r6 = r0
        La:
            r0 = r6
            if (r0 != 0) goto L22
            r0 = r5
            r1 = r3
            int r1 = r1.getLastRowNum()
            if (r0 > r1) goto L22
            r0 = r3
            int r5 = r5 + 1
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFRow r0 = r0.getRow(r1)
            r6 = r0
            goto La
        L22:
            r0 = r5
            r1 = r3
            int r1 = r1.getLastRowNum()
            if (r0 <= r1) goto L2c
            r0 = 0
            return r0
        L2c:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.usermodel.HSSFSheet.findFirstRow(int):int");
    }

    private void addRow(HSSFRow row, boolean addLow) {
        this._rows.put(Integer.valueOf(row.getRowNum()), row);
        if (addLow) {
            this._sheet.addRow(row.getRowRecord());
        }
        boolean firstRow = this._rows.size() == 1;
        if (row.getRowNum() > getLastRowNum() || firstRow) {
            this._lastrow = row.getRowNum();
        }
        if (row.getRowNum() < getFirstRowNum() || firstRow) {
            this._firstrow = row.getRowNum();
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFRow getRow(int rowIndex) {
        return this._rows.get(Integer.valueOf(rowIndex));
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getPhysicalNumberOfRows() {
        return this._rows.size();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getFirstRowNum() {
        return this._firstrow;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getLastRowNum() {
        return this._lastrow;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public List<HSSFDataValidation> getDataValidations() {
        DataValidityTable dvt = this._sheet.getOrCreateDataValidityTable();
        final List<HSSFDataValidation> hssfValidations = new ArrayList<>();
        RecordAggregate.RecordVisitor visitor = new RecordAggregate.RecordVisitor() { // from class: org.apache.poi.hssf.usermodel.HSSFSheet.1
            private HSSFEvaluationWorkbook book;

            {
                this.book = HSSFEvaluationWorkbook.create(HSSFSheet.this.getWorkbook());
            }

            @Override // org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor
            public void visitRecord(Record r) {
                if (!(r instanceof DVRecord)) {
                    return;
                }
                DVRecord dvRecord = (DVRecord) r;
                CellRangeAddressList regions = dvRecord.getCellRangeAddress().copy();
                DVConstraint constraint = DVConstraint.createDVConstraint(dvRecord, this.book);
                HSSFDataValidation hssfDataValidation = new HSSFDataValidation(regions, constraint);
                hssfDataValidation.setErrorStyle(dvRecord.getErrorStyle());
                hssfDataValidation.setEmptyCellAllowed(dvRecord.getEmptyCellAllowed());
                hssfDataValidation.setSuppressDropDownArrow(dvRecord.getSuppressDropdownArrow());
                hssfDataValidation.createPromptBox(dvRecord.getPromptTitle(), dvRecord.getPromptText());
                hssfDataValidation.setShowPromptBox(dvRecord.getShowPromptOnCellSelected());
                hssfDataValidation.createErrorBox(dvRecord.getErrorTitle(), dvRecord.getErrorText());
                hssfDataValidation.setShowErrorBox(dvRecord.getShowErrorOnInvalidValue());
                hssfValidations.add(hssfDataValidation);
            }
        };
        dvt.visitContainedRecords(visitor);
        return hssfValidations;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void addValidationData(DataValidation dataValidation) {
        if (dataValidation == null) {
            throw new IllegalArgumentException("objValidation must not be null");
        }
        HSSFDataValidation hssfDataValidation = (HSSFDataValidation) dataValidation;
        DataValidityTable dvt = this._sheet.getOrCreateDataValidityTable();
        DVRecord dvRecord = hssfDataValidation.createDVRecord(this);
        dvt.addDataValidation(dvRecord);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setColumnHidden(int columnIndex, boolean hidden) {
        this._sheet.setColumnHidden(columnIndex, hidden);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isColumnHidden(int columnIndex) {
        return this._sheet.isColumnHidden(columnIndex);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setColumnWidth(int columnIndex, int width) {
        this._sheet.setColumnWidth(columnIndex, width);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getColumnWidth(int columnIndex) {
        return this._sheet.getColumnWidth(columnIndex);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public float getColumnWidthInPixels(int column) {
        int cw = getColumnWidth(column);
        int def = getDefaultColumnWidth() * 256;
        float px = cw == def ? PX_DEFAULT : PX_MODIFIED;
        return cw / px;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getDefaultColumnWidth() {
        return this._sheet.getDefaultColumnWidth();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDefaultColumnWidth(int width) {
        this._sheet.setDefaultColumnWidth(width);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public short getDefaultRowHeight() {
        return this._sheet.getDefaultRowHeight();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public float getDefaultRowHeightInPoints() {
        return this._sheet.getDefaultRowHeight() / 20.0f;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDefaultRowHeight(short height) {
        this._sheet.setDefaultRowHeight(height);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDefaultRowHeightInPoints(float height) {
        this._sheet.setDefaultRowHeight((short) (height * 20.0f));
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFCellStyle getColumnStyle(int column) {
        short styleIndex = this._sheet.getXFIndexForColAt((short) column);
        if (styleIndex == 15) {
            return null;
        }
        ExtendedFormatRecord xf = this._book.getExFormatAt(styleIndex);
        return new HSSFCellStyle(styleIndex, xf, this._book);
    }

    public boolean isGridsPrinted() {
        return this._sheet.isGridsPrinted();
    }

    public void setGridsPrinted(boolean value) {
        this._sheet.setGridsPrinted(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int addMergedRegion(CellRangeAddress region) {
        return addMergedRegion(region, true);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int addMergedRegionUnsafe(CellRangeAddress region) {
        return addMergedRegion(region, false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void validateMergedRegions() {
        checkForMergedRegionsIntersectingArrayFormulas();
        checkForIntersectingMergedRegions();
    }

    private int addMergedRegion(CellRangeAddress region, boolean validate) {
        if (region.getNumberOfCells() < 2) {
            throw new IllegalArgumentException("Merged region " + region.formatAsString() + " must contain 2 or more cells");
        }
        region.validate(SpreadsheetVersion.EXCEL97);
        if (validate) {
            validateArrayFormulas(region);
            validateMergedRegions(region);
        }
        return this._sheet.addMergedRegion(region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn());
    }

    private void validateArrayFormulas(CellRangeAddress region) {
        int firstRow = region.getFirstRow();
        int firstColumn = region.getFirstColumn();
        int lastRow = region.getLastRow();
        int lastColumn = region.getLastColumn();
        for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
            HSSFRow row = getRow(rowIn);
            if (row != null) {
                for (int colIn = firstColumn; colIn <= lastColumn; colIn++) {
                    HSSFCell cell = row.getCell(colIn);
                    if (cell != null && cell.isPartOfArrayFormulaGroup()) {
                        CellRangeAddress arrayRange = cell.getArrayFormulaRange();
                        if (arrayRange.getNumberOfCells() > 1 && region.intersects(arrayRange)) {
                            String msg = "The range " + region.formatAsString() + " intersects with a multi-cell array formula. You cannot merge cells of an array.";
                            throw new IllegalStateException(msg);
                        }
                    }
                }
            }
        }
    }

    private void checkForMergedRegionsIntersectingArrayFormulas() {
        for (CellRangeAddress region : getMergedRegions()) {
            validateArrayFormulas(region);
        }
    }

    private void validateMergedRegions(CellRangeAddress candidateRegion) {
        for (CellRangeAddress existingRegion : getMergedRegions()) {
            if (existingRegion.intersects(candidateRegion)) {
                throw new IllegalStateException("Cannot add merged region " + candidateRegion.formatAsString() + " to sheet because it overlaps with an existing merged region (" + existingRegion.formatAsString() + ").");
            }
        }
    }

    private void checkForIntersectingMergedRegions() {
        List<CellRangeAddress> regions = getMergedRegions();
        int size = regions.size();
        for (int i = 0; i < size; i++) {
            CellRangeAddress region = regions.get(i);
            for (CellRangeAddress other : regions.subList(i + 1, regions.size())) {
                if (region.intersects(other)) {
                    String msg = "The range " + region.formatAsString() + " intersects with another merged region " + other.formatAsString() + " in this sheet";
                    throw new IllegalStateException(msg);
                }
            }
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setForceFormulaRecalculation(boolean value) {
        this._sheet.setUncalced(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getForceFormulaRecalculation() {
        return this._sheet.getUncalced();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setVerticallyCenter(boolean value) {
        this._sheet.getPageSettings().getVCenter().setVCenter(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getVerticallyCenter() {
        return this._sheet.getPageSettings().getVCenter().getVCenter();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setHorizontallyCenter(boolean value) {
        this._sheet.getPageSettings().getHCenter().setHCenter(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getHorizontallyCenter() {
        return this._sheet.getPageSettings().getHCenter().getHCenter();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRightToLeft(boolean value) {
        this._sheet.getWindowTwo().setArabic(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isRightToLeft() {
        return this._sheet.getWindowTwo().getArabic();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void removeMergedRegion(int index) {
        this._sheet.removeMergedRegion(index);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void removeMergedRegions(Collection<Integer> indices) {
        Iterator i$ = new TreeSet(indices).descendingSet().iterator();
        while (i$.hasNext()) {
            int i = ((Integer) i$.next()).intValue();
            this._sheet.removeMergedRegion(i);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getNumMergedRegions() {
        return this._sheet.getNumMergedRegions();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellRangeAddress getMergedRegion(int index) {
        return this._sheet.getMergedRegionAt(index);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public List<CellRangeAddress> getMergedRegions() {
        List<CellRangeAddress> addresses = new ArrayList<>();
        int count = this._sheet.getNumMergedRegions();
        for (int i = 0; i < count; i++) {
            addresses.add(this._sheet.getMergedRegionAt(i));
        }
        return addresses;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public Iterator<Row> rowIterator() {
        Iterator<Row> result = this._rows.values().iterator();
        return result;
    }

    @Override // java.lang.Iterable
    public Iterator<Row> iterator() {
        return rowIterator();
    }

    InternalSheet getSheet() {
        return this._sheet;
    }

    public void setAlternativeExpression(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setAlternateExpression(b);
    }

    public void setAlternativeFormula(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setAlternateFormula(b);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setAutobreaks(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setAutobreaks(b);
    }

    public void setDialog(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setDialog(b);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDisplayGuts(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setDisplayGuts(b);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setFitToPage(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setFitToPage(b);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRowSumsBelow(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setRowSumsBelow(b);
        record.setAlternateExpression(b);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRowSumsRight(boolean b) {
        WSBoolRecord record = (WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129);
        record.setRowSumsRight(b);
    }

    public boolean getAlternateExpression() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getAlternateExpression();
    }

    public boolean getAlternateFormula() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getAlternateFormula();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getAutobreaks() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getAutobreaks();
    }

    public boolean getDialog() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getDialog();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getDisplayGuts() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getDisplayGuts();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isDisplayZeros() {
        return this._sheet.getWindowTwo().getDisplayZeros();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDisplayZeros(boolean value) {
        this._sheet.getWindowTwo().setDisplayZeros(value);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getFitToPage() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getFitToPage();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getRowSumsBelow() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getRowSumsBelow();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getRowSumsRight() {
        return ((WSBoolRecord) this._sheet.findFirstRecordBySid((short) 129)).getRowSumsRight();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isPrintGridlines() {
        return getSheet().getPrintGridlines().getPrintGridlines();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setPrintGridlines(boolean show) {
        getSheet().getPrintGridlines().setPrintGridlines(show);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isPrintRowAndColumnHeadings() {
        return getSheet().getPrintHeaders().getPrintHeaders();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setPrintRowAndColumnHeadings(boolean show) {
        getSheet().getPrintHeaders().setPrintHeaders(show);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFPrintSetup getPrintSetup() {
        return new HSSFPrintSetup(this._sheet.getPageSettings().getPrintSetup());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFHeader getHeader() {
        return new HSSFHeader(this._sheet.getPageSettings());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFFooter getFooter() {
        return new HSSFFooter(this._sheet.getPageSettings());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isSelected() {
        return getSheet().getWindowTwo().getSelected();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setSelected(boolean sel) {
        getSheet().getWindowTwo().setSelected(sel);
    }

    public boolean isActive() {
        return getSheet().getWindowTwo().isActive();
    }

    public void setActive(boolean sel) {
        getSheet().getWindowTwo().setActive(sel);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public double getMargin(short margin) {
        switch (margin) {
            case 4:
                return this._sheet.getPageSettings().getPrintSetup().getHeaderMargin();
            case 5:
                return this._sheet.getPageSettings().getPrintSetup().getFooterMargin();
            default:
                return this._sheet.getPageSettings().getMargin(margin);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setMargin(short margin, double size) {
        switch (margin) {
            case 4:
                this._sheet.getPageSettings().getPrintSetup().setHeaderMargin(size);
                break;
            case 5:
                this._sheet.getPageSettings().getPrintSetup().setFooterMargin(size);
                break;
            default:
                this._sheet.getPageSettings().setMargin(margin, size);
                break;
        }
    }

    private WorksheetProtectionBlock getProtectionBlock() {
        return this._sheet.getProtectionBlock();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getProtect() {
        return getProtectionBlock().isSheetProtected();
    }

    public short getPassword() {
        return (short) getProtectionBlock().getPasswordHash();
    }

    public boolean getObjectProtect() {
        return getProtectionBlock().isObjectProtected();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean getScenarioProtect() {
        return getProtectionBlock().isScenarioProtected();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void protectSheet(String password) {
        getProtectionBlock().protectSheet(password, true, true);
    }

    public void setZoom(int numerator, int denominator) {
        if (numerator < 1 || numerator > 65535) {
            throw new IllegalArgumentException("Numerator must be greater than 0 and less than 65536");
        }
        if (denominator < 1 || denominator > 65535) {
            throw new IllegalArgumentException("Denominator must be greater than 0 and less than 65536");
        }
        SCLRecord sclRecord = new SCLRecord();
        sclRecord.setNumerator((short) numerator);
        sclRecord.setDenominator((short) denominator);
        getSheet().setSCLRecord(sclRecord);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setZoom(int scale) {
        setZoom(scale, 100);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public short getTopRow() {
        return this._sheet.getTopRow();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public short getLeftCol() {
        return this._sheet.getLeftCol();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void showInPane(int toprow, int leftcol) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (toprow > maxrow) {
            throw new IllegalArgumentException("Maximum row number is " + maxrow);
        }
        showInPane((short) toprow, (short) leftcol);
    }

    private void showInPane(short toprow, short leftcol) {
        this._sheet.setTopRow(toprow);
        this._sheet.setLeftCol(leftcol);
    }

    protected void shiftMerged(int startRow, int endRow, int n, boolean isRow) {
        RowShifter rowShifter = new HSSFRowShifter(this);
        rowShifter.shiftMergedRegions(startRow, endRow, n);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void shiftRows(int startRow, int endRow, int n) {
        shiftRows(startRow, endRow, n, false, false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight) {
        shiftRows(startRow, endRow, n, copyRowHeight, resetOriginalRowHeight, true);
    }

    private static int clip(int row) {
        return Math.min(Math.max(0, row), SpreadsheetVersion.EXCEL97.getLastRowIndex());
    }

    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight, boolean moveComments) {
        int s;
        int inc;
        HSSFComment comment;
        int r;
        if (endRow < startRow) {
            throw new IllegalArgumentException("startRow must be less than or equal to endRow. To shift rows up, use n<0.");
        }
        if (n < 0) {
            s = startRow;
            inc = 1;
        } else if (n > 0) {
            s = endRow;
            inc = -1;
        } else {
            return;
        }
        RowShifter rowShifter = new HSSFRowShifter(this);
        if (moveComments) {
            HSSFPatriarch patriarch = createDrawingPatriarch();
            for (HSSFShape shape : patriarch.getChildren()) {
                if ((shape instanceof HSSFComment) && startRow <= (r = (comment = (HSSFComment) shape).getRow()) && r <= endRow) {
                    comment.setRow(clip(r + n));
                }
            }
        }
        rowShifter.shiftMergedRegions(startRow, endRow, n);
        this._sheet.getPageSettings().shiftRowBreaks(startRow, endRow, n);
        int firstOverwrittenRow = startRow + n;
        int lastOverwrittenRow = endRow + n;
        for (HSSFHyperlink link : getHyperlinkList()) {
            int firstRow = link.getFirstRow();
            int lastRow = link.getLastRow();
            if (firstOverwrittenRow <= firstRow && firstRow <= lastOverwrittenRow && lastOverwrittenRow <= lastRow && lastRow <= lastOverwrittenRow) {
                removeHyperlink(link);
            }
        }
        int i = s;
        while (true) {
            int rowNum = i;
            if (rowNum < startRow || rowNum > endRow || rowNum < 0 || rowNum >= 65536) {
                break;
            }
            HSSFRow row = getRow(rowNum);
            if (row != null) {
                notifyRowShifting(row);
            }
            HSSFRow row2Replace = getRow(rowNum + n);
            if (row2Replace == null) {
                row2Replace = createRow(rowNum + n);
            }
            row2Replace.removeAllCells();
            if (row != null) {
                if (copyRowHeight) {
                    row2Replace.setHeight(row.getHeight());
                }
                if (resetOriginalRowHeight) {
                    row.setHeight((short) 255);
                }
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    HSSFHyperlink link2 = cell.getHyperlink();
                    row.removeCell(cell);
                    CellValueRecordInterface cellRecord = cell.getCellValueRecord();
                    cellRecord.setRow(rowNum + n);
                    row2Replace.createCellFromRecord(cellRecord);
                    this._sheet.addValueRecord(rowNum + n, cellRecord);
                    if (link2 != null) {
                        link2.setFirstRow(link2.getFirstRow() + n);
                        link2.setLastRow(link2.getLastRow() + n);
                    }
                }
                row.removeAllCells();
            }
            i = rowNum + inc;
        }
        if (n > 0) {
            if (startRow == this._firstrow) {
                this._firstrow = Math.max(startRow + n, 0);
                int i2 = startRow + 1;
                while (true) {
                    if (i2 >= startRow + n) {
                        break;
                    }
                    if (getRow(i2) == null) {
                        i2++;
                    } else {
                        this._firstrow = i2;
                        break;
                    }
                }
            }
            if (endRow + n > this._lastrow) {
                this._lastrow = Math.min(endRow + n, SpreadsheetVersion.EXCEL97.getLastRowIndex());
            }
        } else {
            if (startRow + n < this._firstrow) {
                this._firstrow = Math.max(startRow + n, 0);
            }
            if (endRow == this._lastrow) {
                this._lastrow = Math.min(endRow + n, SpreadsheetVersion.EXCEL97.getLastRowIndex());
                int i3 = endRow - 1;
                while (true) {
                    if (i3 <= endRow + n) {
                        break;
                    }
                    if (getRow(i3) != null) {
                        this._lastrow = i3;
                        break;
                    }
                    i3--;
                }
            }
        }
        int sheetIndex = this._workbook.getSheetIndex(this);
        String sheetName = this._workbook.getSheetName(sheetIndex);
        short externSheetIndex = this._book.checkExternSheet(sheetIndex);
        FormulaShifter shifter = FormulaShifter.createForRowShift(externSheetIndex, sheetName, startRow, endRow, n, SpreadsheetVersion.EXCEL97);
        this._sheet.updateFormulasAfterCellShift(shifter, externSheetIndex);
        int nSheets = this._workbook.getNumberOfSheets();
        for (int i4 = 0; i4 < nSheets; i4++) {
            InternalSheet otherSheet = this._workbook.getSheetAt(i4).getSheet();
            if (otherSheet != this._sheet) {
                short otherExtSheetIx = this._book.checkExternSheet(i4);
                otherSheet.updateFormulasAfterCellShift(shifter, otherExtSheetIx);
            }
        }
        this._workbook.getWorkbook().updateNamesAfterCellShift(shifter);
    }

    protected void insertChartRecords(List<Record> records) {
        int window2Loc = this._sheet.findFirstRecordLocBySid((short) 574);
        this._sheet.getRecords().addAll(window2Loc, records);
    }

    private void notifyRowShifting(HSSFRow row) {
        String msg = "Row[rownum=" + row.getRowNum() + "] contains cell(s) included in a multi-cell array formula. You cannot change part of an array.";
        Iterator i$ = row.iterator();
        while (i$.hasNext()) {
            Cell cell = i$.next();
            HSSFCell hcell = (HSSFCell) cell;
            if (hcell.isPartOfArrayFormulaGroup()) {
                hcell.notifyArrayFormulaChanging(msg);
            }
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        validateColumn(colSplit);
        validateRow(rowSplit);
        if (leftmostColumn < colSplit) {
            throw new IllegalArgumentException("leftmostColumn parameter must not be less than colSplit parameter");
        }
        if (topRow < rowSplit) {
            throw new IllegalArgumentException("topRow parameter must not be less than leftmostColumn parameter");
        }
        getSheet().createFreezePane(colSplit, rowSplit, topRow, leftmostColumn);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void createFreezePane(int colSplit, int rowSplit) {
        createFreezePane(colSplit, rowSplit, colSplit, rowSplit);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane) {
        getSheet().createSplitPane(xSplitPos, ySplitPos, topRow, leftmostColumn, activePane);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public PaneInformation getPaneInformation() {
        return getSheet().getPaneInformation();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDisplayGridlines(boolean show) {
        this._sheet.setDisplayGridlines(show);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isDisplayGridlines() {
        return this._sheet.isDisplayGridlines();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDisplayFormulas(boolean show) {
        this._sheet.setDisplayFormulas(show);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isDisplayFormulas() {
        return this._sheet.isDisplayFormulas();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDisplayRowColHeadings(boolean show) {
        this._sheet.setDisplayRowColHeadings(show);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isDisplayRowColHeadings() {
        return this._sheet.isDisplayRowColHeadings();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRowBreak(int row) {
        validateRow(row);
        this._sheet.getPageSettings().setRowBreak(row, (short) 0, (short) 255);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isRowBroken(int row) {
        return this._sheet.getPageSettings().isRowBroken(row);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void removeRowBreak(int row) {
        this._sheet.getPageSettings().removeRowBreak(row);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int[] getRowBreaks() {
        return this._sheet.getPageSettings().getRowBreaks();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int[] getColumnBreaks() {
        return this._sheet.getPageSettings().getColumnBreaks();
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setColumnBreak(int column) {
        validateColumn((short) column);
        this._sheet.getPageSettings().setColumnBreak((short) column, (short) 0, (short) SpreadsheetVersion.EXCEL97.getLastRowIndex());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public boolean isColumnBroken(int column) {
        return this._sheet.getPageSettings().isColumnBroken(column);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void removeColumnBreak(int column) {
        this._sheet.getPageSettings().removeColumnBreak(column);
    }

    protected void validateRow(int row) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (row > maxrow) {
            throw new IllegalArgumentException("Maximum row number is " + maxrow);
        }
        if (row < 0) {
            throw new IllegalArgumentException("Minumum row number is 0");
        }
    }

    protected void validateColumn(int column) {
        int maxcol = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        if (column > maxcol) {
            throw new IllegalArgumentException("Maximum column number is " + maxcol);
        }
        if (column < 0) {
            throw new IllegalArgumentException("Minimum column number is 0");
        }
    }

    public void dumpDrawingRecords(boolean fat, PrintWriter pw) {
        this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false);
        EscherAggregate r = (EscherAggregate) getSheet().findFirstRecordBySid((short) 9876);
        List<EscherRecord> escherRecords = r.getEscherRecords();
        for (EscherRecord escherRecord : escherRecords) {
            if (fat) {
                pw.println(escherRecord);
            } else {
                escherRecord.display(pw, 0);
            }
        }
        pw.flush();
    }

    public EscherAggregate getDrawingEscherAggregate() {
        this._book.findDrawingGroup();
        if (this._book.getDrawingManager() == null) {
            return null;
        }
        int found = this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false);
        if (found == -1) {
            return null;
        }
        return (EscherAggregate) this._sheet.findFirstRecordBySid((short) 9876);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFPatriarch getDrawingPatriarch() {
        this._patriarch = getPatriarch(false);
        return this._patriarch;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFPatriarch createDrawingPatriarch() {
        this._patriarch = getPatriarch(true);
        return this._patriarch;
    }

    private HSSFPatriarch getPatriarch(boolean createIfMissing) {
        if (this._patriarch != null) {
            return this._patriarch;
        }
        DrawingManager2 dm = this._book.findDrawingGroup();
        if (null == dm) {
            if (!createIfMissing) {
                return null;
            }
            this._book.createDrawingGroup();
            dm = this._book.getDrawingManager();
        }
        EscherAggregate agg = (EscherAggregate) this._sheet.findFirstRecordBySid((short) 9876);
        if (null == agg) {
            int pos = this._sheet.aggregateDrawingRecords(dm, false);
            if (-1 == pos) {
                if (createIfMissing) {
                    HSSFPatriarch patriarch = new HSSFPatriarch(this, (EscherAggregate) this._sheet.getRecords().get(this._sheet.aggregateDrawingRecords(dm, true)));
                    patriarch.afterCreate();
                    return patriarch;
                }
                return null;
            }
            agg = (EscherAggregate) this._sheet.getRecords().get(pos);
        }
        return new HSSFPatriarch(this, agg);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
        this._sheet.setColumnGroupCollapsed(columnNumber, collapsed);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void groupColumn(int fromColumn, int toColumn) {
        this._sheet.groupColumnRange(fromColumn, toColumn, true);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void ungroupColumn(int fromColumn, int toColumn) {
        this._sheet.groupColumnRange(fromColumn, toColumn, false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void groupRow(int fromRow, int toRow) {
        this._sheet.groupRowRange(fromRow, toRow, true);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void ungroupRow(int fromRow, int toRow) {
        this._sheet.groupRowRange(fromRow, toRow, false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRowGroupCollapsed(int rowIndex, boolean collapse) {
        if (collapse) {
            this._sheet.getRowsAggregate().collapseRow(rowIndex);
        } else {
            this._sheet.getRowsAggregate().expandRow(rowIndex);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setDefaultColumnStyle(int column, CellStyle style) {
        this._sheet.setDefaultColumnStyle(column, ((HSSFCellStyle) style).getIndex());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void autoSizeColumn(int column) {
        autoSizeColumn(column, false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void autoSizeColumn(int column, boolean useMergedCells) {
        double width = SheetUtil.getColumnWidth(this, column, useMergedCells);
        if (width != -1.0d) {
            double width2 = width * 256.0d;
            if (width2 > 65280) {
                width2 = 65280;
            }
            setColumnWidth(column, (int) width2);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFComment getCellComment(CellAddress ref) {
        return findCellComment(ref.getRow(), ref.getColumn());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFHyperlink getHyperlink(int row, int column) {
        for (RecordBase rec : this._sheet.getRecords()) {
            if (rec instanceof HyperlinkRecord) {
                HyperlinkRecord link = (HyperlinkRecord) rec;
                if (link.getFirstColumn() == column && link.getFirstRow() == row) {
                    return new HSSFHyperlink(link);
                }
            }
        }
        return null;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFHyperlink getHyperlink(CellAddress addr) {
        return getHyperlink(addr.getRow(), addr.getColumn());
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public List<HSSFHyperlink> getHyperlinkList() {
        List<HSSFHyperlink> hyperlinkList = new ArrayList<>();
        for (RecordBase rec : this._sheet.getRecords()) {
            if (rec instanceof HyperlinkRecord) {
                HyperlinkRecord link = (HyperlinkRecord) rec;
                hyperlinkList.add(new HSSFHyperlink(link));
            }
        }
        return hyperlinkList;
    }

    protected void removeHyperlink(HSSFHyperlink link) {
        removeHyperlink(link.record);
    }

    protected void removeHyperlink(HyperlinkRecord link) {
        Iterator<RecordBase> it = this._sheet.getRecords().iterator();
        while (it.hasNext()) {
            RecordBase rec = it.next();
            if (rec instanceof HyperlinkRecord) {
                HyperlinkRecord recLink = (HyperlinkRecord) rec;
                if (link == recLink) {
                    it.remove();
                    return;
                }
            }
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFSheetConditionalFormatting getSheetConditionalFormatting() {
        return new HSSFSheetConditionalFormatting(this);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public String getSheetName() {
        HSSFWorkbook wb = getWorkbook();
        int idx = wb.getSheetIndex(this);
        return wb.getSheetName(idx);
    }

    private CellRange<HSSFCell> getCellRange(CellRangeAddress range) {
        int firstRow = range.getFirstRow();
        int firstColumn = range.getFirstColumn();
        int lastRow = range.getLastRow();
        int lastColumn = range.getLastColumn();
        int height = (lastRow - firstRow) + 1;
        int width = (lastColumn - firstColumn) + 1;
        List<HSSFCell> temp = new ArrayList<>(height * width);
        for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
            for (int colIn = firstColumn; colIn <= lastColumn; colIn++) {
                HSSFRow row = getRow(rowIn);
                if (row == null) {
                    row = createRow(rowIn);
                }
                HSSFCell cell = row.getCell(colIn);
                if (cell == null) {
                    cell = row.createCell(colIn);
                }
                temp.add(cell);
            }
        }
        return SSCellRange.create(firstRow, firstColumn, height, width, temp, HSSFCell.class);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellRange<HSSFCell> setArrayFormula(String formula, CellRangeAddress range) throws FormulaParseException {
        int sheetIndex = this._workbook.getSheetIndex(this);
        Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._workbook, FormulaType.ARRAY, sheetIndex);
        CellRange<HSSFCell> cells = getCellRange(range);
        for (HSSFCell c : cells) {
            c.setCellArrayFormula(range);
        }
        HSSFCell mainArrayFormulaCell = (HSSFCell) cells.getTopLeftCell();
        FormulaRecordAggregate agg = (FormulaRecordAggregate) mainArrayFormulaCell.getCellValueRecord();
        agg.setArrayFormula(range, ptgs);
        return cells;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellRange<HSSFCell> removeArrayFormula(Cell cell) {
        if (cell.getSheet() != this) {
            throw new IllegalArgumentException("Specified cell does not belong to this sheet.");
        }
        CellValueRecordInterface rec = ((HSSFCell) cell).getCellValueRecord();
        if (!(rec instanceof FormulaRecordAggregate)) {
            String ref = new CellReference(cell).formatAsString();
            throw new IllegalArgumentException("Cell " + ref + " is not part of an array formula.");
        }
        FormulaRecordAggregate fra = (FormulaRecordAggregate) rec;
        CellRangeAddress range = fra.removeArrayFormula(cell.getRowIndex(), cell.getColumnIndex());
        CellRange<HSSFCell> result = getCellRange(range);
        for (Cell c : result) {
            c.setCellType(CellType.BLANK);
        }
        return result;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public DataValidationHelper getDataValidationHelper() {
        return new HSSFDataValidationHelper(this);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public HSSFAutoFilter setAutoFilter(CellRangeAddress range) {
        InternalWorkbook workbook = this._workbook.getWorkbook();
        int sheetIndex = this._workbook.getSheetIndex(this);
        NameRecord name = workbook.getSpecificBuiltinRecord((byte) 13, sheetIndex + 1);
        if (name == null) {
            name = workbook.createBuiltInName((byte) 13, sheetIndex + 1);
        }
        int firstRow = range.getFirstRow();
        if (firstRow == -1) {
            firstRow = 0;
        }
        Area3DPtg ptg = new Area3DPtg(firstRow, range.getLastRow(), range.getFirstColumn(), range.getLastColumn(), false, false, false, false, sheetIndex);
        name.setNameDefinition(new Ptg[]{ptg});
        AutoFilterInfoRecord r = new AutoFilterInfoRecord();
        int numcols = (1 + range.getLastColumn()) - range.getFirstColumn();
        r.setNumEntries((short) numcols);
        int idx = this._sheet.findFirstRecordLocBySid((short) 512);
        this._sheet.getRecords().add(idx, r);
        HSSFPatriarch p = createDrawingPatriarch();
        int firstColumn = range.getFirstColumn();
        int lastColumn = range.getLastColumn();
        for (int col = firstColumn; col <= lastColumn; col++) {
            p.createComboBox(new HSSFClientAnchor(0, 0, 0, 0, (short) col, firstRow, (short) (col + 1), firstRow + 1));
        }
        return new HSSFAutoFilter(this);
    }

    protected HSSFComment findCellComment(int row, int column) {
        HSSFPatriarch patriarch = getDrawingPatriarch();
        if (null == patriarch) {
            patriarch = createDrawingPatriarch();
        }
        return lookForComment(patriarch, row, column);
    }

    private HSSFComment lookForComment(HSSFShapeContainer container, int row, int column) {
        for (Shape object : container.getChildren()) {
            Shape shape = (HSSFShape) object;
            if (shape instanceof HSSFShapeGroup) {
                HSSFShape res = lookForComment((HSSFShapeContainer) shape, row, column);
                if (null != res) {
                    return (HSSFComment) res;
                }
            } else if (shape instanceof HSSFComment) {
                HSSFComment comment = (HSSFComment) shape;
                if (comment.hasPosition() && comment.getColumn() == column && comment.getRow() == row) {
                    return comment;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public Map<CellAddress, HSSFComment> getCellComments() {
        HSSFPatriarch patriarch = getDrawingPatriarch();
        if (null == patriarch) {
            patriarch = createDrawingPatriarch();
        }
        Map<CellAddress, HSSFComment> locations = new TreeMap<>();
        findCellCommentLocations(patriarch, locations);
        return locations;
    }

    private void findCellCommentLocations(HSSFShapeContainer container, Map<CellAddress, HSSFComment> locations) {
        for (Object object : container.getChildren()) {
            HSSFShape shape = (HSSFShape) object;
            if (shape instanceof HSSFShapeGroup) {
                findCellCommentLocations((HSSFShapeGroup) shape, locations);
            } else if (shape instanceof HSSFComment) {
                HSSFComment comment = (HSSFComment) shape;
                if (comment.hasPosition()) {
                    locations.put(new CellAddress(comment.getRow(), comment.getColumn()), comment);
                }
            }
        }
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellRangeAddress getRepeatingRows() {
        return getRepeatingRowsOrColums(true);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellRangeAddress getRepeatingColumns() {
        return getRepeatingRowsOrColums(false);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRepeatingRows(CellRangeAddress rowRangeRef) {
        CellRangeAddress columnRangeRef = getRepeatingColumns();
        setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setRepeatingColumns(CellRangeAddress columnRangeRef) {
        CellRangeAddress rowRangeRef = getRepeatingRows();
        setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
    }

    private void setRepeatingRowsAndColumns(CellRangeAddress rowDef, CellRangeAddress colDef) {
        int sheetIndex = this._workbook.getSheetIndex(this);
        int maxRowIndex = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        int maxColIndex = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        int col1 = -1;
        int col2 = -1;
        int row1 = -1;
        int row2 = -1;
        if (rowDef != null) {
            row1 = rowDef.getFirstRow();
            row2 = rowDef.getLastRow();
            if ((row1 == -1 && row2 != -1) || row1 > row2 || row1 < 0 || row1 > maxRowIndex || row2 < 0 || row2 > maxRowIndex) {
                throw new IllegalArgumentException("Invalid row range specification");
            }
        }
        if (colDef != null) {
            col1 = colDef.getFirstColumn();
            col2 = colDef.getLastColumn();
            if ((col1 == -1 && col2 != -1) || col1 > col2 || col1 < 0 || col1 > maxColIndex || col2 < 0 || col2 > maxColIndex) {
                throw new IllegalArgumentException("Invalid column range specification");
            }
        }
        short externSheetIndex = this._workbook.getWorkbook().checkExternSheet(sheetIndex);
        boolean setBoth = (rowDef == null || colDef == null) ? false : true;
        boolean removeAll = rowDef == null && colDef == null;
        HSSFName name = this._workbook.getBuiltInName((byte) 7, sheetIndex);
        if (removeAll) {
            if (name != null) {
                this._workbook.removeName(name);
                return;
            }
            return;
        }
        if (name == null) {
            name = this._workbook.createBuiltInName((byte) 7, sheetIndex);
        }
        List<Ptg> ptgList = new ArrayList<>();
        if (setBoth) {
            ptgList.add(new MemFuncPtg(23));
        }
        if (colDef != null) {
            Area3DPtg colArea = new Area3DPtg(0, maxRowIndex, col1, col2, false, false, false, false, externSheetIndex);
            ptgList.add(colArea);
        }
        if (rowDef != null) {
            Area3DPtg rowArea = new Area3DPtg(row1, row2, 0, maxColIndex, false, false, false, false, externSheetIndex);
            ptgList.add(rowArea);
        }
        if (setBoth) {
            ptgList.add(UnionPtg.instance);
        }
        Ptg[] ptgs = new Ptg[ptgList.size()];
        ptgList.toArray(ptgs);
        name.setNameDefinition(ptgs);
        HSSFPrintSetup printSetup = getPrintSetup();
        printSetup.setValidSettings(false);
        setActive(true);
    }

    private CellRangeAddress getRepeatingRowsOrColums(boolean rows) {
        Ptg[] nameDefinition;
        NameRecord rec = getBuiltinNameRecord((byte) 7);
        if (rec == null || (nameDefinition = rec.getNameDefinition()) == null) {
            return null;
        }
        int maxRowIndex = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        int maxColIndex = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        for (Ptg ptg : nameDefinition) {
            if (ptg instanceof Area3DPtg) {
                Area3DPtg areaPtg = (Area3DPtg) ptg;
                if (areaPtg.getFirstColumn() == 0 && areaPtg.getLastColumn() == maxColIndex) {
                    if (rows) {
                        return new CellRangeAddress(areaPtg.getFirstRow(), areaPtg.getLastRow(), -1, -1);
                    }
                } else if (areaPtg.getFirstRow() == 0 && areaPtg.getLastRow() == maxRowIndex && !rows) {
                    return new CellRangeAddress(-1, -1, areaPtg.getFirstColumn(), areaPtg.getLastColumn());
                }
            }
        }
        return null;
    }

    private NameRecord getBuiltinNameRecord(byte builtinCode) {
        int sheetIndex = this._workbook.getSheetIndex(this);
        int recIndex = this._workbook.findExistingBuiltinNameRecordIdx(sheetIndex, builtinCode);
        if (recIndex == -1) {
            return null;
        }
        return this._workbook.getNameRecord(recIndex);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public int getColumnOutlineLevel(int columnIndex) {
        return this._sheet.getColumnOutlineLevel(columnIndex);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public CellAddress getActiveCell() {
        int row = this._sheet.getActiveCellRow();
        int col = this._sheet.getActiveCellCol();
        return new CellAddress(row, col);
    }

    @Override // org.apache.poi.ss.usermodel.Sheet
    public void setActiveCell(CellAddress address) {
        int row = address.getRow();
        short col = (short) address.getColumn();
        this._sheet.setActiveCellRow(row);
        this._sheet.setActiveCellCol(col);
    }
}
