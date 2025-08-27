package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/SeparatedTableBorders.class */
class SeparatedTableBorders extends TableBorders {
    public SeparatedTableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders) {
        super(rows, numberOfColumns, tableBoundingBorders);
    }

    public SeparatedTableBorders(List<CellRenderer[]> rows, int numberOfColumns, Border[] tableBoundingBorders, int largeTableIndexOffset) {
        super(rows, numberOfColumns, tableBoundingBorders, largeTableIndexOffset);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders drawHorizontalBorder(int i, float startX, float y1, PdfCanvas canvas, float[] countedColumnWidth) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders drawVerticalBorder(int i, float startY, float x1, PdfCanvas canvas, List<Float> heights) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyTopTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean isEmpty, boolean force, boolean reverse) {
        return applyTopTableBorder(occupiedBox, layoutBox, reverse);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyTopTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean reverse) {
        float topIndent = (reverse ? -1 : 1) * getMaxTopWidth();
        layoutBox.decreaseHeight(topIndent);
        occupiedBox.moveDown(topIndent).increaseHeight(topIndent);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyBottomTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean isEmpty, boolean force, boolean reverse) {
        return applyBottomTableBorder(occupiedBox, layoutBox, reverse);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyBottomTableBorder(Rectangle occupiedBox, Rectangle layoutBox, boolean reverse) {
        float bottomTableBorderWidth = (reverse ? -1 : 1) * getMaxBottomWidth();
        layoutBox.decreaseHeight(bottomTableBorderWidth);
        occupiedBox.moveDown(bottomTableBorderWidth).increaseHeight(bottomTableBorderWidth);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyLeftAndRightTableBorder(Rectangle layoutBox, boolean reverse) {
        if (null != layoutBox) {
            layoutBox.applyMargins(0.0f, this.rightBorderMaxWidth, 0.0f, this.leftBorderMaxWidth, reverse);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders skipFooter(Border[] borders) {
        setTableBoundingBorders(borders);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders skipHeader(Border[] borders) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders collapseTableWithFooter(TableBorders footerBordersHandler, boolean hasContent) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders collapseTableWithHeader(TableBorders headerBordersHandler, boolean updateBordersHandler) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders fixHeaderOccupiedArea(Rectangle occupiedBox, Rectangle layoutBox) {
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders applyCellIndents(Rectangle box, float topIndent, float rightIndent, float bottomIndent, float leftIndent, boolean reverse) {
        box.applyMargins(topIndent, rightIndent, bottomIndent, leftIndent, false);
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getVerticalBorder(int index) {
        return this.verticalBorders.get(index);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getHorizontalBorder(int index) {
        return this.horizontalBorders.get(index - this.largeTableIndexOffset);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected float getCellVerticalAddition(float[] indents) {
        return 0.0f;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders updateBordersOnNewPage(boolean isOriginalNonSplitRenderer, boolean isFooterOrHeader, TableRenderer currentRenderer, TableRenderer headerRenderer, TableRenderer footerRenderer) {
        if (!isFooterOrHeader && isOriginalNonSplitRenderer && null != this.rows) {
            processAllBordersAndEmptyRows();
            this.rightBorderMaxWidth = getMaxRightWidth();
            this.leftBorderMaxWidth = getMaxLeftWidth();
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
    public float[] getCellBorderIndents(int row, int col, int rowspan, int colspan) {
        float[] indents = new float[4];
        Border[] borders = this.rows.get((row + this.startRow) - this.largeTableIndexOffset)[col].getBorders();
        for (int i = 0; i < 4; i++) {
            if (null != borders[i]) {
                indents[i] = borders[i].getWidth();
            }
        }
        return indents;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected void buildBordersArrays(CellRenderer cell, int row, int col, int[] rowspansToDeduct) {
        int colspan = cell.getPropertyAsInteger(16).intValue();
        int rowspan = cell.getPropertyAsInteger(60).intValue();
        int colN = ((Cell) cell.getModelElement()).getCol();
        Border[] cellBorders = cell.getBorders();
        if ((row + 1) - rowspan < 0) {
            rowspan = row + 1;
        }
        for (int i = 0; i < colspan; i++) {
            checkAndReplaceBorderInArray(this.horizontalBorders, 2 * ((row + 1) - rowspan), colN + i, cellBorders[0], false);
        }
        for (int i2 = 0; i2 < colspan; i2++) {
            checkAndReplaceBorderInArray(this.horizontalBorders, (2 * row) + 1, colN + i2, cellBorders[2], true);
        }
        for (int j = (row - rowspan) + 1; j <= row; j++) {
            checkAndReplaceBorderInArray(this.verticalBorders, 2 * colN, j, cellBorders[3], false);
        }
        for (int i3 = (row - rowspan) + 1; i3 <= row; i3++) {
            checkAndReplaceBorderInArray(this.verticalBorders, (2 * (colN + colspan)) - 1, i3, cellBorders[1], true);
        }
    }

    protected boolean checkAndReplaceBorderInArray(List<List<Border>> borderArray, int i, int j, Border borderToAdd, boolean hasPriority) {
        List<Border> borders = borderArray.get(i);
        Border neighbour = borders.get(j);
        if (neighbour == null) {
            borders.set(j, borderToAdd);
            return true;
        }
        Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
        logger.warn(LogMessageConstant.UNEXPECTED_BEHAVIOUR_DURING_TABLE_ROW_COLLAPSING);
        return true;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    protected TableBorders initializeBorders() {
        while (2 * Math.max(this.numberOfColumns, 1) > this.verticalBorders.size()) {
            List<Border> tempBorders = new ArrayList<>();
            while (2 * Math.max(this.rows.size(), 1) > tempBorders.size()) {
                tempBorders.add(null);
            }
            this.verticalBorders.add(tempBorders);
        }
        while (2 * Math.max(this.rows.size(), 1) > this.horizontalBorders.size()) {
            List<Border> tempBorders2 = new ArrayList<>();
            while (this.numberOfColumns > tempBorders2.size()) {
                tempBorders2.add(null);
            }
            this.horizontalBorders.add(tempBorders2);
        }
        return this;
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getFirstHorizontalBorder() {
        return getHorizontalBorder(2 * this.startRow);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public List<Border> getLastHorizontalBorder() {
        return getHorizontalBorder((2 * this.finishRow) + 1);
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public float getMaxTopWidth() {
        if (null == this.tableBoundingBorders[0]) {
            return 0.0f;
        }
        return this.tableBoundingBorders[0].getWidth();
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public float getMaxBottomWidth() {
        if (null == this.tableBoundingBorders[2]) {
            return 0.0f;
        }
        return this.tableBoundingBorders[2].getWidth();
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public float getMaxRightWidth() {
        if (null == this.tableBoundingBorders[1]) {
            return 0.0f;
        }
        return this.tableBoundingBorders[1].getWidth();
    }

    @Override // com.itextpdf.layout.renderer.TableBorders
    public float getMaxLeftWidth() {
        if (null == this.tableBoundingBorders[3]) {
            return 0.0f;
        }
        return this.tableBoundingBorders[3].getWidth();
    }
}
