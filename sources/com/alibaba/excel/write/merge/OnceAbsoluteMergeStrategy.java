package com.alibaba.excel.write.merge;

import com.alibaba.excel.metadata.Head;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/merge/OnceAbsoluteMergeStrategy.class */
public class OnceAbsoluteMergeStrategy extends AbstractMergeStrategy {
    private int firstRowIndex;
    private int lastRowIndex;
    private int firstColumnIndex;
    private int lastColumnIndex;

    public OnceAbsoluteMergeStrategy(int firstRowIndex, int lastRowIndex, int firstColumnIndex, int lastColumnIndex) {
        if (firstRowIndex < 0 || lastRowIndex < 0 || firstColumnIndex < 0 || lastColumnIndex < 0) {
            throw new IllegalArgumentException("All parameters must be greater than 0");
        }
        this.firstRowIndex = firstRowIndex;
        this.lastRowIndex = lastRowIndex;
        this.firstColumnIndex = firstColumnIndex;
        this.lastColumnIndex = lastColumnIndex;
    }

    @Override // com.alibaba.excel.write.merge.AbstractMergeStrategy
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (cell.getRowIndex() == this.firstRowIndex && cell.getColumnIndex() == this.firstColumnIndex) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(this.firstRowIndex, this.lastRowIndex, this.firstColumnIndex, this.lastColumnIndex);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }
}
