package com.alibaba.excel.write.merge;

import com.alibaba.excel.metadata.Head;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/merge/LoopMergeStrategy.class */
public class LoopMergeStrategy extends AbstractMergeStrategy {
    private int eachRow;
    private int columnCount;
    private int columnIndex;

    public LoopMergeStrategy(int eachRow, int columnIndex) {
        this(eachRow, 1, columnIndex);
    }

    public LoopMergeStrategy(int eachRow, int columnCount, int columnIndex) {
        if (eachRow < 1) {
            throw new IllegalArgumentException("EachRows must be greater than 1");
        }
        if (columnCount < 1) {
            throw new IllegalArgumentException("ColumnCount must be greater than 1");
        }
        if (columnCount == 1 && eachRow == 1) {
            throw new IllegalArgumentException("ColumnCount or eachRows must be greater than 1");
        }
        if (columnIndex < 0) {
            throw new IllegalArgumentException("ColumnIndex must be greater than 0");
        }
        this.eachRow = eachRow;
        this.columnCount = columnCount;
        this.columnIndex = columnIndex;
    }

    @Override // com.alibaba.excel.write.merge.AbstractMergeStrategy
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        Integer currentColumnIndex;
        if (relativeRowIndex == null) {
            return;
        }
        if (head != null) {
            currentColumnIndex = head.getColumnIndex();
        } else {
            currentColumnIndex = Integer.valueOf(cell.getColumnIndex());
        }
        if (currentColumnIndex.intValue() == this.columnIndex && relativeRowIndex.intValue() % this.eachRow == 0) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRowIndex(), (cell.getRowIndex() + this.eachRow) - 1, cell.getColumnIndex(), (cell.getColumnIndex() + this.columnCount) - 1);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }
}
