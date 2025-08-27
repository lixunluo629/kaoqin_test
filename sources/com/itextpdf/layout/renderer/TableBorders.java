package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableBorders.class */
abstract class TableBorders {
    protected List<List<Border>> horizontalBorders;
    protected List<List<Border>> verticalBorders;
    protected final int numberOfColumns;
    protected Border[] tableBoundingBorders;
    protected List<CellRenderer[]> rows;
    protected int startRow;
    protected int finishRow;
    protected float leftBorderMaxWidth;
    protected float rightBorderMaxWidth;
    protected int largeTableIndexOffset;

    protected abstract TableBorders drawHorizontalBorder(int i, float f, float f2, PdfCanvas pdfCanvas, float[] fArr);

    protected abstract TableBorders drawVerticalBorder(int i, float f, float f2, PdfCanvas pdfCanvas, List<Float> list);

    protected abstract TableBorders applyTopTableBorder(Rectangle rectangle, Rectangle rectangle2, boolean z, boolean z2, boolean z3);

    protected abstract TableBorders applyTopTableBorder(Rectangle rectangle, Rectangle rectangle2, boolean z);

    protected abstract TableBorders applyBottomTableBorder(Rectangle rectangle, Rectangle rectangle2, boolean z, boolean z2, boolean z3);

    protected abstract TableBorders applyBottomTableBorder(Rectangle rectangle, Rectangle rectangle2, boolean z);

    protected abstract TableBorders applyLeftAndRightTableBorder(Rectangle rectangle, boolean z);

    protected abstract TableBorders skipFooter(Border[] borderArr);

    protected abstract TableBorders skipHeader(Border[] borderArr);

    protected abstract TableBorders collapseTableWithFooter(TableBorders tableBorders, boolean z);

    protected abstract TableBorders collapseTableWithHeader(TableBorders tableBorders, boolean z);

    protected abstract TableBorders fixHeaderOccupiedArea(Rectangle rectangle, Rectangle rectangle2);

    protected abstract TableBorders applyCellIndents(Rectangle rectangle, float f, float f2, float f3, float f4, boolean z);

    public abstract List<Border> getVerticalBorder(int i);

    public abstract List<Border> getHorizontalBorder(int i);

    protected abstract float getCellVerticalAddition(float[] fArr);

    protected abstract void buildBordersArrays(CellRenderer cellRenderer, int i, int i2, int[] iArr);

    protected abstract TableBorders updateBordersOnNewPage(boolean z, boolean z2, TableRenderer tableRenderer, TableRenderer tableRenderer2, TableRenderer tableRenderer3);

    public TableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders) {
        this.horizontalBorders = new ArrayList();
        this.verticalBorders = new ArrayList();
        this.tableBoundingBorders = new Border[4];
        this.largeTableIndexOffset = 0;
        this.rows = rows;
        this.numberOfColumns = numberOfColumns;
        setTableBoundingBorders(tableBoundingBorders);
    }

    public TableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders, int largeTableIndexOffset) {
        this(rows, numberOfColumns, tableBoundingBorders);
        this.largeTableIndexOffset = largeTableIndexOffset;
    }

    protected TableBorders processAllBordersAndEmptyRows() {
        int[] rowspansToDeduct = new int[this.numberOfColumns];
        int numOfRowsToRemove = 0;
        if (!this.rows.isEmpty()) {
            int row = this.startRow - this.largeTableIndexOffset;
            while (row <= this.finishRow - this.largeTableIndexOffset) {
                CellRenderer[] currentRow = this.rows.get(row);
                boolean hasCells = false;
                int col = 0;
                while (col < this.numberOfColumns) {
                    if (null != currentRow[col]) {
                        int colspan = currentRow[col].getPropertyAsInteger(16).intValue();
                        if (rowspansToDeduct[col] > 0) {
                            int rowspan = currentRow[col].getPropertyAsInteger(60).intValue() - rowspansToDeduct[col];
                            if (rowspan < 1) {
                                Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                                logger.warn(LogMessageConstant.UNEXPECTED_BEHAVIOUR_DURING_TABLE_ROW_COLLAPSING);
                                rowspan = 1;
                            }
                            currentRow[col].setProperty(60, Integer.valueOf(rowspan));
                            if (0 != numOfRowsToRemove) {
                                removeRows(row - numOfRowsToRemove, numOfRowsToRemove);
                                row -= numOfRowsToRemove;
                                numOfRowsToRemove = 0;
                            }
                        }
                        buildBordersArrays(currentRow[col], row, col, rowspansToDeduct);
                        hasCells = true;
                        for (int i = 0; i < colspan; i++) {
                            rowspansToDeduct[col + i] = 0;
                        }
                        col += colspan - 1;
                    } else if (this.horizontalBorders.get(row).size() <= col) {
                        this.horizontalBorders.get(row).add(null);
                    }
                    col++;
                }
                if (!hasCells) {
                    if (row == this.rows.size() - 1) {
                        removeRows(row - rowspansToDeduct[0], rowspansToDeduct[0]);
                        this.rows.remove(row - rowspansToDeduct[0]);
                        setFinishRow(this.finishRow - 1);
                        Logger logger2 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                        logger2.warn(LogMessageConstant.LAST_ROW_IS_NOT_COMPLETE);
                    } else {
                        for (int i2 = 0; i2 < this.numberOfColumns; i2++) {
                            int i3 = i2;
                            rowspansToDeduct[i3] = rowspansToDeduct[i3] + 1;
                        }
                        numOfRowsToRemove++;
                    }
                }
                row++;
            }
        }
        if (this.finishRow < this.startRow) {
            setFinishRow(this.startRow);
        }
        return this;
    }

    private void removeRows(int startRow, int numOfRows) {
        for (int row = startRow; row < startRow + numOfRows; row++) {
            this.rows.remove(startRow);
            this.horizontalBorders.remove(startRow + 1);
            for (int j = 0; j <= this.numberOfColumns; j++) {
                this.verticalBorders.get(j).remove(startRow + 1);
            }
        }
        setFinishRow(this.finishRow - numOfRows);
    }

    protected TableBorders initializeBorders() {
        while (this.numberOfColumns + 1 > this.verticalBorders.size()) {
            List<Border> tempBorders = new ArrayList<>();
            while (Math.max(this.rows.size(), 1) > tempBorders.size()) {
                tempBorders.add(null);
            }
            this.verticalBorders.add(tempBorders);
        }
        while (Math.max(this.rows.size(), 1) + 1 > this.horizontalBorders.size()) {
            List<Border> tempBorders2 = new ArrayList<>();
            while (this.numberOfColumns > tempBorders2.size()) {
                tempBorders2.add(null);
            }
            this.horizontalBorders.add(tempBorders2);
        }
        return this;
    }

    protected TableBorders setTableBoundingBorders(Border[] borders) {
        this.tableBoundingBorders = new Border[4];
        if (null != borders) {
            for (int i = 0; i < borders.length; i++) {
                this.tableBoundingBorders[i] = borders[i];
            }
        }
        return this;
    }

    protected TableBorders setRowRange(int startRow, int finishRow) {
        this.startRow = startRow;
        this.finishRow = finishRow;
        return this;
    }

    protected TableBorders setStartRow(int row) {
        this.startRow = row;
        return this;
    }

    protected TableBorders setFinishRow(int row) {
        this.finishRow = row;
        return this;
    }

    public float getLeftBorderMaxWidth() {
        return this.leftBorderMaxWidth;
    }

    public float getRightBorderMaxWidth() {
        return this.rightBorderMaxWidth;
    }

    public float getMaxTopWidth() {
        float width = 0.0f;
        Border widestBorder = getWidestHorizontalBorder(this.startRow);
        if (null != widestBorder && widestBorder.getWidth() >= 0.0f) {
            width = widestBorder.getWidth();
        }
        return width;
    }

    public float getMaxBottomWidth() {
        float width = 0.0f;
        Border widestBorder = getWidestHorizontalBorder(this.finishRow + 1);
        if (null != widestBorder && widestBorder.getWidth() >= 0.0f) {
            width = widestBorder.getWidth();
        }
        return width;
    }

    public float getMaxRightWidth() {
        float width = 0.0f;
        Border widestBorder = getWidestVerticalBorder(this.verticalBorders.size() - 1);
        if (null != widestBorder && widestBorder.getWidth() >= 0.0f) {
            width = widestBorder.getWidth();
        }
        return width;
    }

    public float getMaxLeftWidth() {
        float width = 0.0f;
        Border widestBorder = getWidestVerticalBorder(0);
        if (null != widestBorder && widestBorder.getWidth() >= 0.0f) {
            width = widestBorder.getWidth();
        }
        return width;
    }

    public Border getWidestVerticalBorder(int col) {
        return TableBorderUtil.getWidestBorder(getVerticalBorder(col));
    }

    public Border getWidestVerticalBorder(int col, int start, int end) {
        return TableBorderUtil.getWidestBorder(getVerticalBorder(col), start, end);
    }

    public Border getWidestHorizontalBorder(int row) {
        return TableBorderUtil.getWidestBorder(getHorizontalBorder(row));
    }

    public Border getWidestHorizontalBorder(int row, int start, int end) {
        return TableBorderUtil.getWidestBorder(getHorizontalBorder(row), start, end);
    }

    public List<Border> getFirstHorizontalBorder() {
        return getHorizontalBorder(this.startRow);
    }

    public List<Border> getLastHorizontalBorder() {
        return getHorizontalBorder(this.finishRow + 1);
    }

    public List<Border> getFirstVerticalBorder() {
        return getVerticalBorder(0);
    }

    public List<Border> getLastVerticalBorder() {
        return getVerticalBorder(this.verticalBorders.size() - 1);
    }

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public int getFinishRow() {
        return this.finishRow;
    }

    public Border[] getTableBoundingBorders() {
        return this.tableBoundingBorders;
    }

    public float[] getCellBorderIndents(int row, int col, int rowspan, int colspan) {
        float[] indents = new float[4];
        List<Border> borderList = getHorizontalBorder(((this.startRow + row) - rowspan) + 1);
        for (int i = col; i < col + colspan; i++) {
            Border border = borderList.get(i);
            if (null != border && border.getWidth() > indents[0]) {
                indents[0] = border.getWidth();
            }
        }
        List<Border> borderList2 = getVerticalBorder(col + colspan);
        for (int i2 = ((this.startRow + row) - rowspan) + 1; i2 < this.startRow + row + 1; i2++) {
            Border border2 = borderList2.get(i2);
            if (null != border2 && border2.getWidth() > indents[1]) {
                indents[1] = border2.getWidth();
            }
        }
        List<Border> borderList3 = getHorizontalBorder(this.startRow + row + 1);
        for (int i3 = col; i3 < col + colspan; i3++) {
            Border border3 = borderList3.get(i3);
            if (null != border3 && border3.getWidth() > indents[2]) {
                indents[2] = border3.getWidth();
            }
        }
        List<Border> borderList4 = getVerticalBorder(col);
        for (int i4 = ((this.startRow + row) - rowspan) + 1; i4 < this.startRow + row + 1; i4++) {
            Border border4 = borderList4.get(i4);
            if (null != border4 && border4.getWidth() > indents[3]) {
                indents[3] = border4.getWidth();
            }
        }
        return indents;
    }
}
