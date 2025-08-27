package org.apache.poi.hssf.usermodel;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Configurator;

/* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFRow.class */
public final class HSSFRow implements Row, Comparable<HSSFRow> {
    public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFRow.ColInitialCapacity", 5);
    private int rowNum;
    private HSSFCell[] cells;
    private final RowRecord row;
    private final HSSFWorkbook book;
    private final HSSFSheet sheet;

    HSSFRow(HSSFWorkbook book, HSSFSheet sheet, int rowNum) {
        this(book, sheet, new RowRecord(rowNum));
    }

    HSSFRow(HSSFWorkbook book, HSSFSheet sheet, RowRecord record) {
        this.book = book;
        this.sheet = sheet;
        this.row = record;
        setRowNum(record.getRowNumber());
        this.cells = new HSSFCell[record.getLastCol() + INITIAL_CAPACITY];
        record.setEmpty();
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCell createCell(int column) {
        return createCell(column, CellType.BLANK);
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCell createCell(int columnIndex, int type) {
        return createCell(columnIndex, CellType.forInt(type));
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCell createCell(int columnIndex, CellType type) {
        short shortCellNum = (short) columnIndex;
        if (columnIndex > 32767) {
            shortCellNum = (short) (65535 - columnIndex);
        }
        HSSFCell cell = new HSSFCell(this.book, this.sheet, getRowNum(), shortCellNum, type);
        addCell(cell);
        this.sheet.getSheet().addValueRecord(getRowNum(), cell.getCellValueRecord());
        return cell;
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void removeCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell must not be null");
        }
        removeCell((HSSFCell) cell, true);
    }

    private void removeCell(HSSFCell cell, boolean alsoRemoveRecords) {
        int column = cell.getColumnIndex();
        if (column < 0) {
            throw new RuntimeException("Negative cell indexes not allowed");
        }
        if (column >= this.cells.length || cell != this.cells[column]) {
            throw new RuntimeException("Specified cell is not from this row");
        }
        if (cell.isPartOfArrayFormulaGroup()) {
            cell.notifyArrayFormulaChanging();
        }
        this.cells[column] = null;
        if (alsoRemoveRecords) {
            CellValueRecordInterface cval = cell.getCellValueRecord();
            this.sheet.getSheet().removeValueRecord(getRowNum(), cval);
        }
        if (cell.getColumnIndex() + 1 == this.row.getLastCol()) {
            this.row.setLastCol(calculateNewLastCellPlusOne(this.row.getLastCol()));
        }
        if (cell.getColumnIndex() == this.row.getFirstCol()) {
            this.row.setFirstCol(calculateNewFirstCell(this.row.getFirstCol()));
        }
    }

    protected void removeAllCells() {
        HSSFCell[] arr$ = this.cells;
        for (HSSFCell cell : arr$) {
            if (cell != null) {
                removeCell(cell, true);
            }
        }
        this.cells = new HSSFCell[INITIAL_CAPACITY];
    }

    HSSFCell createCellFromRecord(CellValueRecordInterface cell) {
        HSSFCell hcell = new HSSFCell(this.book, this.sheet, cell);
        addCell(hcell);
        int colIx = cell.getColumn();
        if (this.row.isEmpty()) {
            this.row.setFirstCol(colIx);
            this.row.setLastCol(colIx + 1);
        } else if (colIx < this.row.getFirstCol()) {
            this.row.setFirstCol(colIx);
        } else if (colIx > this.row.getLastCol()) {
            this.row.setLastCol(colIx + 1);
        }
        return hcell;
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void setRowNum(int rowIndex) {
        int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
        if (rowIndex < 0 || rowIndex > maxrow) {
            throw new IllegalArgumentException("Invalid row number (" + rowIndex + ") outside allowable range (0.." + maxrow + ")");
        }
        this.rowNum = rowIndex;
        if (this.row != null) {
            this.row.setRowNumber(rowIndex);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public int getRowNum() {
        return this.rowNum;
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFSheet getSheet() {
        return this.sheet;
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public int getOutlineLevel() {
        return this.row.getOutlineLevel();
    }

    public void moveCell(HSSFCell cell, short newColumn) {
        if (this.cells.length > newColumn && this.cells[newColumn] != null) {
            throw new IllegalArgumentException("Asked to move cell to column " + ((int) newColumn) + " but there's already a cell there");
        }
        if (!this.cells[cell.getColumnIndex()].equals(cell)) {
            throw new IllegalArgumentException("Asked to move a cell, but it didn't belong to our row");
        }
        removeCell(cell, false);
        cell.updateCellNum(newColumn);
        addCell(cell);
    }

    private void addCell(HSSFCell cell) {
        int column = cell.getColumnIndex();
        if (column >= this.cells.length) {
            HSSFCell[] oldCells = this.cells;
            int newSize = ((oldCells.length * 3) / 2) + 1;
            if (newSize < column + 1) {
                newSize = column + INITIAL_CAPACITY;
            }
            this.cells = new HSSFCell[newSize];
            System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
        }
        this.cells[column] = cell;
        if (this.row.isEmpty() || column < this.row.getFirstCol()) {
            this.row.setFirstCol((short) column);
        }
        if (this.row.isEmpty() || column >= this.row.getLastCol()) {
            this.row.setLastCol((short) (column + 1));
        }
    }

    private HSSFCell retrieveCell(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= this.cells.length) {
            return null;
        }
        return this.cells[cellIndex];
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCell getCell(int cellnum) {
        return getCell(cellnum, this.book.getMissingCellPolicy());
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCell getCell(int cellnum, Row.MissingCellPolicy policy) {
        HSSFCell cell = retrieveCell(cellnum);
        switch (policy) {
            case RETURN_NULL_AND_BLANK:
                return cell;
            case RETURN_BLANK_AS_NULL:
                boolean isBlank = cell != null && cell.getCellTypeEnum() == CellType.BLANK;
                if (isBlank) {
                    return null;
                }
                return cell;
            case CREATE_NULL_AS_BLANK:
                return cell == null ? createCell(cellnum, CellType.BLANK) : cell;
            default:
                throw new IllegalArgumentException("Illegal policy " + policy);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public short getFirstCellNum() {
        if (this.row.isEmpty()) {
            return (short) -1;
        }
        return (short) this.row.getFirstCol();
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public short getLastCellNum() {
        if (this.row.isEmpty()) {
            return (short) -1;
        }
        return (short) this.row.getLastCol();
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public int getPhysicalNumberOfCells() {
        int count = 0;
        HSSFCell[] arr$ = this.cells;
        for (HSSFCell cell : arr$) {
            if (cell != null) {
                count++;
            }
        }
        return count;
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void setHeight(short height) {
        if (height == -1) {
            this.row.setHeight((short) -32513);
            this.row.setBadFontHeight(false);
        } else {
            this.row.setBadFontHeight(true);
            this.row.setHeight(height);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void setZeroHeight(boolean zHeight) {
        this.row.setZeroHeight(zHeight);
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public boolean getZeroHeight() {
        return this.row.getZeroHeight();
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void setHeightInPoints(float height) {
        if (height == -1.0f) {
            this.row.setHeight((short) -32513);
            this.row.setBadFontHeight(false);
        } else {
            this.row.setBadFontHeight(true);
            this.row.setHeight((short) (height * 20.0f));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public short getHeight() {
        short height = this.row.getHeight();
        return (height & 32768) != 0 ? this.sheet.getSheet().getDefaultRowHeight() : (short) (height & Short.MAX_VALUE);
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public float getHeightInPoints() {
        return getHeight() / 20.0f;
    }

    protected RowRecord getRowRecord() {
        return this.row;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000b */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int calculateNewLastCellPlusOne(int r4) {
        /*
            r3 = this;
            r0 = r4
            r1 = 1
            int r0 = r0 - r1
            r5 = r0
            r0 = r3
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFCell r0 = r0.retrieveCell(r1)
            r6 = r0
        La:
            r0 = r6
            if (r0 != 0) goto L20
            r0 = r5
            if (r0 >= 0) goto L14
            r0 = 0
            return r0
        L14:
            r0 = r3
            int r5 = r5 + (-1)
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFCell r0 = r0.retrieveCell(r1)
            r6 = r0
            goto La
        L20:
            r0 = r5
            r1 = 1
            int r0 = r0 + r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.usermodel.HSSFRow.calculateNewLastCellPlusOne(int):int");
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x000b */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int calculateNewFirstCell(int r4) {
        /*
            r3 = this;
            r0 = r4
            r1 = 1
            int r0 = r0 + r1
            r5 = r0
            r0 = r3
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFCell r0 = r0.retrieveCell(r1)
            r6 = r0
        La:
            r0 = r6
            if (r0 != 0) goto L25
            r0 = r5
            r1 = r3
            org.apache.poi.hssf.usermodel.HSSFCell[] r1 = r1.cells
            int r1 = r1.length
            if (r0 > r1) goto L19
            r0 = 0
            return r0
        L19:
            r0 = r3
            int r5 = r5 + 1
            r1 = r5
            org.apache.poi.hssf.usermodel.HSSFCell r0 = r0.retrieveCell(r1)
            r6 = r0
            goto La
        L25:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.usermodel.HSSFRow.calculateNewFirstCell(int):int");
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public boolean isFormatted() {
        return this.row.getFormatted();
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public HSSFCellStyle getRowStyle() {
        if (!isFormatted()) {
            return null;
        }
        short styleIndex = this.row.getXFIndex();
        ExtendedFormatRecord xf = this.book.getWorkbook().getExFormatAt(styleIndex);
        return new HSSFCellStyle(styleIndex, xf, this.book);
    }

    public void setRowStyle(HSSFCellStyle style) {
        this.row.setFormatted(true);
        this.row.setXFIndex(style.getIndex());
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public void setRowStyle(CellStyle style) {
        setRowStyle((HSSFCellStyle) style);
    }

    @Override // org.apache.poi.ss.usermodel.Row
    public Iterator<Cell> cellIterator() {
        return new CellIterator();
    }

    @Override // java.lang.Iterable
    public Iterator<Cell> iterator() {
        return cellIterator();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/hssf/usermodel/HSSFRow$CellIterator.class */
    private class CellIterator implements Iterator<Cell> {
        int thisId = -1;
        int nextId = -1;

        public CellIterator() {
            findNext();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextId < HSSFRow.this.cells.length;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Cell next() {
            if (hasNext()) {
                HSSFCell cell = HSSFRow.this.cells[this.nextId];
                this.thisId = this.nextId;
                findNext();
                return cell;
            }
            throw new NoSuchElementException("At last element");
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.thisId != -1) {
                HSSFRow.this.cells[this.thisId] = null;
                return;
            }
            throw new IllegalStateException("remove() called before next()");
        }

        private void findNext() {
            int i = this.nextId + 1;
            while (i < HSSFRow.this.cells.length && HSSFRow.this.cells[i] == null) {
                i++;
            }
            this.nextId = i;
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(HSSFRow other) {
        if (getSheet() != other.getSheet()) {
            throw new IllegalArgumentException("The compared rows must belong to the same sheet");
        }
        Integer thisRow = Integer.valueOf(getRowNum());
        Integer otherRow = Integer.valueOf(other.getRowNum());
        return thisRow.compareTo(otherRow);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HSSFRow)) {
            return false;
        }
        HSSFRow other = (HSSFRow) obj;
        return getRowNum() == other.getRowNum() && getSheet() == other.getSheet();
    }

    public int hashCode() {
        return this.row.hashCode();
    }
}
