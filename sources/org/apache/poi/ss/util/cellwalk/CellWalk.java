package org.apache.poi.ss.util.cellwalk;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/cellwalk/CellWalk.class */
public class CellWalk {
    private Sheet sheet;
    private CellRangeAddress range;
    private boolean traverseEmptyCells = false;

    public CellWalk(Sheet sheet, CellRangeAddress range) {
        this.sheet = sheet;
        this.range = range;
    }

    public boolean isTraverseEmptyCells() {
        return this.traverseEmptyCells;
    }

    public void setTraverseEmptyCells(boolean traverseEmptyCells) {
        this.traverseEmptyCells = traverseEmptyCells;
    }

    public void traverse(CellHandler handler) {
        int firstRow = this.range.getFirstRow();
        int lastRow = this.range.getLastRow();
        int firstColumn = this.range.getFirstColumn();
        int lastColumn = this.range.getLastColumn();
        int width = (lastColumn - firstColumn) + 1;
        SimpleCellWalkContext ctx = new SimpleCellWalkContext();
        ctx.rowNumber = firstRow;
        while (ctx.rowNumber <= lastRow) {
            Row currentRow = this.sheet.getRow(ctx.rowNumber);
            if (currentRow != null) {
                ctx.colNumber = firstColumn;
                while (ctx.colNumber <= lastColumn) {
                    Cell currentCell = currentRow.getCell(ctx.colNumber);
                    if (currentCell != null && (!isEmpty(currentCell) || this.traverseEmptyCells)) {
                        ctx.ordinalNumber = ((ctx.rowNumber - firstRow) * width) + (ctx.colNumber - firstColumn) + 1;
                        handler.onCell(currentCell, ctx);
                    }
                    ctx.colNumber++;
                }
            }
            ctx.rowNumber++;
        }
    }

    private boolean isEmpty(Cell cell) {
        return cell.getCellTypeEnum() == CellType.BLANK;
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/util/cellwalk/CellWalk$SimpleCellWalkContext.class */
    private static class SimpleCellWalkContext implements CellWalkContext {
        public long ordinalNumber;
        public int rowNumber;
        public int colNumber;

        private SimpleCellWalkContext() {
            this.ordinalNumber = 0L;
            this.rowNumber = 0;
            this.colNumber = 0;
        }

        @Override // org.apache.poi.ss.util.cellwalk.CellWalkContext
        public long getOrdinalNumber() {
            return this.ordinalNumber;
        }

        @Override // org.apache.poi.ss.util.cellwalk.CellWalkContext
        public int getRowNumber() {
            return this.rowNumber;
        }

        @Override // org.apache.poi.ss.util.cellwalk.CellWalkContext
        public int getColumnNumber() {
            return this.colNumber;
        }
    }
}
