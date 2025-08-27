package com.itextpdf.layout.renderer;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/CollapsedTableBorders.class */
class CollapsedTableBorders extends TableBorders {
    private List<Border> topBorderCollapseWith;
    private List<Border> bottomBorderCollapseWith;

    public CollapsedTableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders) {
        super(rows, numberOfColumns, tableBoundingBorders);
        this.topBorderCollapseWith = new ArrayList();
        this.bottomBorderCollapseWith = new ArrayList();
    }

    public CollapsedTableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders, int largeTableIndexOffset) {
        super(rows, numberOfColumns, tableBoundingBorders, largeTableIndexOffset);
        this.topBorderCollapseWith = new ArrayList();
        this.bottomBorderCollapseWith = new ArrayList();
    }

    public List<Border> getTopBorderCollapseWith() {
        return this.topBorderCollapseWith;
    }

    public List<Border> getBottomBorderCollapseWith() {
        return this.bottomBorderCollapseWith;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
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
        for (int i2 = (((this.startRow - this.largeTableIndexOffset) + row) - rowspan) + 1; i2 < (this.startRow - this.largeTableIndexOffset) + row + 1; i2++) {
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
        for (int i4 = (((this.startRow - this.largeTableIndexOffset) + row) - rowspan) + 1; i4 < (this.startRow - this.largeTableIndexOffset) + row + 1; i4++) {
            Border border4 = borderList4.get(i4);
            if (null != border4 && border4.getWidth() > indents[3]) {
                indents[3] = border4.getWidth();
            }
        }
        return indents;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getVerticalBorder(int index) {
        if (index == 0) {
            List<Border> borderList = TableBorderUtil.createAndFillBorderList(null, this.tableBoundingBorders[3], this.verticalBorders.get(0).size());
            return getCollapsedList(this.verticalBorders.get(0), borderList);
        }
        if (index == this.numberOfColumns) {
            List<Border> borderList2 = TableBorderUtil.createAndFillBorderList(null, this.tableBoundingBorders[1], this.verticalBorders.get(0).size());
            return getCollapsedList(this.verticalBorders.get(this.verticalBorders.size() - 1), borderList2);
        }
        return this.verticalBorders.get(index);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getHorizontalBorder(int index) {
        if (index == this.startRow) {
            List<Border> firstBorderOnCurrentPage = TableBorderUtil.createAndFillBorderList(this.topBorderCollapseWith, this.tableBoundingBorders[0], this.numberOfColumns);
            if (index == this.largeTableIndexOffset) {
                return getCollapsedList(this.horizontalBorders.get(index - this.largeTableIndexOffset), firstBorderOnCurrentPage);
            }
            if (0 != this.rows.size()) {
                int col = 0;
                int row = index;
                while (col < this.numberOfColumns) {
                    if (null != this.rows.get(row - this.largeTableIndexOffset)[col] && (row - index) + 1 <= ((Cell) this.rows.get(row - this.largeTableIndexOffset)[col].getModelElement()).getRowspan()) {
                        CellRenderer cell = this.rows.get(row - this.largeTableIndexOffset)[col];
                        Border cellModelTopBorder = TableBorderUtil.getCellSideBorder((Cell) cell.getModelElement(), 13);
                        int colspan = cell.getPropertyAsInteger(16).intValue();
                        if (null == firstBorderOnCurrentPage.get(col) || (null != cellModelTopBorder && cellModelTopBorder.getWidth() > firstBorderOnCurrentPage.get(col).getWidth())) {
                            for (int i = col; i < col + colspan; i++) {
                                firstBorderOnCurrentPage.set(i, cellModelTopBorder);
                            }
                        }
                        col += colspan;
                        row = index;
                    } else {
                        row++;
                        if (row == this.rows.size()) {
                            break;
                        }
                    }
                }
            }
            return firstBorderOnCurrentPage;
        }
        if (index == this.finishRow + 1) {
            List<Border> lastBorderOnCurrentPage = TableBorderUtil.createAndFillBorderList(this.bottomBorderCollapseWith, this.tableBoundingBorders[2], this.numberOfColumns);
            if (index - this.largeTableIndexOffset == this.horizontalBorders.size() - 1) {
                return getCollapsedList(this.horizontalBorders.get(index - this.largeTableIndexOffset), lastBorderOnCurrentPage);
            }
            if (0 != this.rows.size()) {
                int col2 = 0;
                int row2 = index - 1;
                while (col2 < this.numberOfColumns) {
                    if (null != this.rows.get(row2 - this.largeTableIndexOffset)[col2]) {
                        CellRenderer cell2 = this.rows.get(row2 - this.largeTableIndexOffset)[col2];
                        Border cellModelBottomBorder = TableBorderUtil.getCellSideBorder((Cell) cell2.getModelElement(), 10);
                        int colspan2 = cell2.getPropertyAsInteger(16).intValue();
                        if (null == lastBorderOnCurrentPage.get(col2) || (null != cellModelBottomBorder && cellModelBottomBorder.getWidth() > lastBorderOnCurrentPage.get(col2).getWidth())) {
                            for (int i2 = col2; i2 < col2 + colspan2; i2++) {
                                lastBorderOnCurrentPage.set(i2, cellModelBottomBorder);
                            }
                        }
                        col2 += colspan2;
                        row2 = index - 1;
                    } else {
                        row2++;
                        if (row2 == this.rows.size()) {
                            break;
                        }
                    }
                }
            }
            return lastBorderOnCurrentPage;
        }
        return this.horizontalBorders.get(index - this.largeTableIndexOffset);
    }

    public CollapsedTableBorders setTopBorderCollapseWith(List<Border> topBorderCollapseWith) {
        this.topBorderCollapseWith = new ArrayList();
        if (null != topBorderCollapseWith) {
            this.topBorderCollapseWith.addAll(topBorderCollapseWith);
        }
        return this;
    }

    public CollapsedTableBorders setBottomBorderCollapseWith(List<Border> bottomBorderCollapseWith) {
        this.bottomBorderCollapseWith = new ArrayList();
        if (null != bottomBorderCollapseWith) {
            this.bottomBorderCollapseWith.addAll(bottomBorderCollapseWith);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected void buildBordersArrays(CellRenderer cell, int row, int col, int[] rowspansToDeduct) {
        int nextCellRow;
        if (row > this.horizontalBorders.size()) {
            row--;
        }
        int currCellColspan = cell.getPropertyAsInteger(16).intValue();
        if (col != 0 && null == this.rows.get(row)[col - 1]) {
            int j = col;
            while (true) {
                j--;
                nextCellRow = row;
                while (this.rows.size() != nextCellRow && null == this.rows.get(nextCellRow)[j]) {
                    nextCellRow++;
                }
                if (j <= 0 || this.rows.size() == nextCellRow || (j + this.rows.get(nextCellRow)[j].getPropertyAsInteger(16).intValue() == col && (nextCellRow - this.rows.get(nextCellRow)[j].getPropertyAsInteger(60).intValue()) + 1 + rowspansToDeduct[j] == row)) {
                    break;
                }
            }
            if (j >= 0 && nextCellRow != this.rows.size() && nextCellRow > row) {
                CellRenderer nextCell = this.rows.get(nextCellRow)[j];
                nextCell.setProperty(60, Integer.valueOf(nextCell.getPropertyAsInteger(60).intValue() - rowspansToDeduct[j]));
                int nextCellColspan = nextCell.getPropertyAsInteger(16).intValue();
                for (int i = j; i < j + nextCellColspan; i++) {
                    rowspansToDeduct[i] = 0;
                }
                buildBordersArrays(nextCell, nextCellRow, true);
            }
        }
        int iIntValue = 0;
        while (true) {
            int j2 = iIntValue;
            if (j2 >= currCellColspan) {
                break;
            }
            int nextCellRow2 = row + 1;
            while (nextCellRow2 < this.rows.size() && null == this.rows.get(nextCellRow2)[col + j2]) {
                nextCellRow2++;
            }
            if (nextCellRow2 == this.rows.size()) {
                break;
            }
            CellRenderer nextCell2 = this.rows.get(nextCellRow2)[col + j2];
            if (row == nextCellRow2 - nextCell2.getPropertyAsInteger(60).intValue()) {
                buildBordersArrays(nextCell2, nextCellRow2, true);
            }
            iIntValue = j2 + nextCell2.getPropertyAsInteger(16).intValue();
        }
        if (col + currCellColspan < this.rows.get(row).length) {
            int nextCellRow3 = row;
            while (nextCellRow3 < this.rows.size() && null == this.rows.get(nextCellRow3)[col + currCellColspan]) {
                nextCellRow3++;
            }
            if (nextCellRow3 != this.rows.size()) {
                CellRenderer nextCell3 = this.rows.get(nextCellRow3)[col + currCellColspan];
                nextCell3.setProperty(60, Integer.valueOf(nextCell3.getPropertyAsInteger(60).intValue() - rowspansToDeduct[col + currCellColspan]));
                int nextCellColspan2 = nextCell3.getPropertyAsInteger(16).intValue();
                for (int i2 = col + currCellColspan; i2 < col + currCellColspan + nextCellColspan2; i2++) {
                    rowspansToDeduct[i2] = 0;
                }
                buildBordersArrays(nextCell3, nextCellRow3, true);
            }
        }
        buildBordersArrays(cell, row, false);
    }

    protected void buildBordersArrays(CellRenderer cell, int row, boolean isNeighbourCell) {
        int colspan = cell.getPropertyAsInteger(16).intValue();
        int rowspan = cell.getPropertyAsInteger(60).intValue();
        int colN = ((Cell) cell.getModelElement()).getCol();
        Border[] cellBorders = cell.getBorders();
        if ((row + 1) - rowspan < 0) {
            rowspan = row + 1;
        }
        for (int i = 0; i < colspan; i++) {
            checkAndReplaceBorderInArray(this.horizontalBorders, (row + 1) - rowspan, colN + i, cellBorders[0], false);
        }
        for (int i2 = 0; i2 < colspan; i2++) {
            checkAndReplaceBorderInArray(this.horizontalBorders, row + 1, colN + i2, cellBorders[2], true);
        }
        for (int j = (row - rowspan) + 1; j <= row; j++) {
            checkAndReplaceBorderInArray(this.verticalBorders, colN, j, cellBorders[3], false);
        }
        for (int i3 = (row - rowspan) + 1; i3 <= row; i3++) {
            checkAndReplaceBorderInArray(this.verticalBorders, colN + colspan, i3, cellBorders[1], true);
        }
    }

    protected boolean checkAndReplaceBorderInArray(List<List<Border>> borderArray, int i, int j, Border borderToAdd, boolean hasPriority) {
        List<Border> borders = borderArray.get(i);
        Border neighbour = borders.get(j);
        if (neighbour == null) {
            borders.set(j, borderToAdd);
            return true;
        }
        if (neighbour != borderToAdd && borderToAdd != null && neighbour.getWidth() <= borderToAdd.getWidth()) {
            if (!hasPriority && neighbour.getWidth() == borderToAdd.getWidth()) {
                return false;
            }
            borders.set(j, borderToAdd);
            return true;
        }
        return false;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders drawHorizontalBorder(int i, float startX, float y1, PdfCanvas canvas, float[] countedColumnWidth) {
        Border firstBorder;
        List<Border> borders = getHorizontalBorder(this.startRow + i);
        float x1 = startX;
        float x2 = x1 + countedColumnWidth[0];
        if (i == 0) {
            Border firstBorder2 = getFirstVerticalBorder().get(this.startRow - this.largeTableIndexOffset);
            if (firstBorder2 != null) {
                x1 -= firstBorder2.getWidth() / 2.0f;
            }
        } else if (i == (this.finishRow - this.startRow) + 1 && (firstBorder = getFirstVerticalBorder().get(((((this.startRow - this.largeTableIndexOffset) + this.finishRow) - this.startRow) + 1) - 1)) != null) {
            x1 -= firstBorder.getWidth() / 2.0f;
        }
        int j = 1;
        while (j < borders.size()) {
            Border prevBorder = borders.get(j - 1);
            Border curBorder = borders.get(j);
            if (prevBorder != null) {
                if (!prevBorder.equals(curBorder)) {
                    prevBorder.drawCellBorder(canvas, x1, y1, x2, y1, Border.Side.NONE);
                    x1 = x2;
                }
            } else {
                x1 += countedColumnWidth[j - 1];
                x2 = x1;
            }
            if (curBorder != null) {
                x2 += countedColumnWidth[j];
            }
            j++;
        }
        Border lastBorder = borders.size() > j - 1 ? borders.get(j - 1) : null;
        if (lastBorder != null) {
            if (i == 0) {
                if (getVerticalBorder(j).get((this.startRow - this.largeTableIndexOffset) + i) != null) {
                    x2 += getVerticalBorder(j).get((this.startRow - this.largeTableIndexOffset) + i).getWidth() / 2.0f;
                }
            } else if (i == (this.finishRow - this.startRow) + 1 && getVerticalBorder(j).size() > ((this.startRow - this.largeTableIndexOffset) + i) - 1 && getVerticalBorder(j).get(((this.startRow - this.largeTableIndexOffset) + i) - 1) != null) {
                x2 += getVerticalBorder(j).get(((this.startRow - this.largeTableIndexOffset) + i) - 1).getWidth() / 2.0f;
            }
            lastBorder.drawCellBorder(canvas, x1, y1, x2, y1, Border.Side.NONE);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders drawVerticalBorder(int i, float startY, float x1, PdfCanvas canvas, List<Float> heights) {
        List<Border> borders = getVerticalBorder(i);
        float y1 = startY;
        float y2 = y1;
        if (!heights.isEmpty()) {
            y2 = y1 - heights.get(0).floatValue();
        }
        int j = 1;
        while (j < heights.size()) {
            Border prevBorder = borders.get(((this.startRow - this.largeTableIndexOffset) + j) - 1);
            Border curBorder = borders.get((this.startRow - this.largeTableIndexOffset) + j);
            if (prevBorder != null) {
                if (!prevBorder.equals(curBorder)) {
                    prevBorder.drawCellBorder(canvas, x1, y1, x1, y2, Border.Side.NONE);
                    y1 = y2;
                }
            } else {
                y1 -= heights.get(j - 1).floatValue();
                y2 = y1;
            }
            if (curBorder != null) {
                y2 -= heights.get(j).floatValue();
            }
            j++;
        }
        if (borders.size() == 0) {
            return this;
        }
        Border lastBorder = borders.get(((this.startRow - this.largeTableIndexOffset) + j) - 1);
        if (lastBorder != null) {
            lastBorder.drawCellBorder(canvas, x1, y1, x1, y2, Border.Side.NONE);
        }
        return this;
    }

    public static Border getCollapsedBorder(Border cellBorder, Border tableBorder) {
        if (null != tableBorder && (null == cellBorder || cellBorder.getWidth() < tableBorder.getWidth())) {
            return tableBorder;
        }
        if (null != cellBorder) {
            return cellBorder;
        }
        return Border.NO_BORDER;
    }

    public static List<Border> getCollapsedList(List<Border> innerList, List<Border> outerList) {
        int size = Math.min(null == innerList ? 0 : innerList.size(), null == outerList ? 0 : outerList.size());
        List<Border> collapsedList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            collapsedList.add(getCollapsedBorder(innerList.get(i), outerList.get(i)));
        }
        return collapsedList;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyLeftAndRightTableBorder(Rectangle layoutBox, boolean reverse) {
        if (null != layoutBox) {
            layoutBox.applyMargins(0.0f, this.rightBorderMaxWidth / 2.0f, 0.0f, this.leftBorderMaxWidth / 2.0f, reverse);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyTopTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean isEmpty, boolean force, boolean reverse) {
        if (!isEmpty) {
            return applyTopTableBorder(occupiedBox, layoutBox, reverse);
        }
        if (force) {
            applyTopTableBorder(occupiedBox, layoutBox, reverse);
            return applyTopTableBorder(occupiedBox, layoutBox, reverse);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyBottomTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean isEmpty, boolean force, boolean reverse) {
        if (!isEmpty) {
            return applyBottomTableBorder(occupiedBox, layoutBox, reverse);
        }
        if (force) {
            applyBottomTableBorder(occupiedBox, layoutBox, reverse);
            return applyBottomTableBorder(occupiedBox, layoutBox, reverse);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyTopTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean reverse) {
        float topIndent = (reverse ? -1 : 1) * getMaxTopWidth();
        layoutBox.decreaseHeight(topIndent / 2.0f);
        occupiedBox.moveDown(topIndent / 2.0f).increaseHeight(topIndent / 2.0f);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyBottomTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean reverse) {
        float bottomTableBorderWidth = (reverse ? -1 : 1) * getMaxBottomWidth();
        layoutBox.decreaseHeight(bottomTableBorderWidth / 2.0f);
        occupiedBox.moveDown(bottomTableBorderWidth / 2.0f).increaseHeight(bottomTableBorderWidth / 2.0f);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyCellIndents(Rectangle box, float topIndent, float rightIndent, float bottomIndent, float leftIndent, boolean reverse) {
        box.applyMargins(topIndent / 2.0f, rightIndent / 2.0f, bottomIndent / 2.0f, leftIndent / 2.0f, false);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected float getCellVerticalAddition(float[] indents) {
        return (indents[0] / 2.0f) + (indents[2] / 2.0f);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders updateBordersOnNewPage(boolean isOriginalNonSplitRenderer, boolean isFooterOrHeader, TableRenderer currentRenderer, TableRenderer headerRenderer, TableRenderer footerRenderer) {
        if (!isFooterOrHeader) {
            if (isOriginalNonSplitRenderer) {
                if (null != this.rows) {
                    processAllBordersAndEmptyRows();
                    this.rightBorderMaxWidth = getMaxRightWidth();
                    this.leftBorderMaxWidth = getMaxLeftWidth();
                }
                setTopBorderCollapseWith(((Table) currentRenderer.getModelElement()).getLastRowBottomBorder());
            } else {
                setTopBorderCollapseWith(null);
                setBottomBorderCollapseWith(null);
            }
        }
        if (null != footerRenderer) {
            float rightFooterBorderWidth = footerRenderer.bordersHandler.getMaxRightWidth();
            float leftFooterBorderWidth = footerRenderer.bordersHandler.getMaxLeftWidth();
            this.leftBorderMaxWidth = Math.max(this.leftBorderMaxWidth, leftFooterBorderWidth);
            this.rightBorderMaxWidth = Math.max(this.rightBorderMaxWidth, rightFooterBorderWidth);
        }
        if (null != headerRenderer) {
            float rightHeaderBorderWidth = headerRenderer.bordersHandler.getMaxRightWidth();
            float leftHeaderBorderWidth = headerRenderer.bordersHandler.getMaxLeftWidth();
            this.leftBorderMaxWidth = Math.max(this.leftBorderMaxWidth, leftHeaderBorderWidth);
            this.rightBorderMaxWidth = Math.max(this.rightBorderMaxWidth, rightHeaderBorderWidth);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders skipFooter(Border[] borders) {
        setTableBoundingBorders(borders);
        setBottomBorderCollapseWith(null);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders skipHeader(Border[] borders) {
        setTableBoundingBorders(borders);
        setTopBorderCollapseWith(null);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders collapseTableWithFooter(TableBorders footerBordersHandler, boolean hasContent) {
        ((CollapsedTableBorders) footerBordersHandler).setTopBorderCollapseWith(hasContent ? getLastHorizontalBorder() : getTopBorderCollapseWith());
        setBottomBorderCollapseWith(footerBordersHandler.getHorizontalBorder(0));
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders collapseTableWithHeader(TableBorders headerBordersHandler, boolean updateBordersHandler) {
        ((CollapsedTableBorders) headerBordersHandler).setBottomBorderCollapseWith(getHorizontalBorder(this.startRow));
        if (updateBordersHandler) {
            setTopBorderCollapseWith(headerBordersHandler.getLastHorizontalBorder());
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders fixHeaderOccupiedArea(Rectangle occupiedBox, Rectangle layoutBox) {
        float topBorderMaxWidth = getMaxTopWidth();
        layoutBox.increaseHeight(topBorderMaxWidth);
        occupiedBox.moveUp(topBorderMaxWidth).decreaseHeight(topBorderMaxWidth);
        return this;
    }
}
