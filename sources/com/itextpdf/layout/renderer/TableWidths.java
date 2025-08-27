package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.ArrayUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.UnitValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableWidths.class */
final class TableWidths {
    private final TableRenderer tableRenderer;
    private final int numberOfColumns;
    private final float rightBorderMaxWidth;
    private final float leftBorderMaxWidth;
    private final ColumnWidthData[] widths;
    private final float horizontalBorderSpacing;
    private List<CellInfo> cells;
    private float tableWidth;
    private boolean fixedTableWidth;
    private boolean fixedTableLayout = false;
    private float layoutMinWidth;
    private float tableMinWidth;
    private float tableMaxWidth;
    private static final UnitValue ZeroWidth;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TableWidths.class.desiredAssertionStatus();
        ZeroWidth = UnitValue.createPointValue(0.0f);
    }

    TableWidths(TableRenderer tableRenderer, float availableWidth, boolean calculateTableMaxWidth, float rightBorderMaxWidth, float leftBorderMaxWidth) {
        this.tableRenderer = tableRenderer;
        this.numberOfColumns = ((Table) tableRenderer.getModelElement()).getNumberOfColumns();
        this.widths = new ColumnWidthData[this.numberOfColumns];
        this.rightBorderMaxWidth = rightBorderMaxWidth;
        this.leftBorderMaxWidth = leftBorderMaxWidth;
        if (tableRenderer.bordersHandler instanceof SeparatedTableBorders) {
            Float horizontalSpacing = tableRenderer.getPropertyAsFloat(115);
            this.horizontalBorderSpacing = null == horizontalSpacing ? 0.0f : horizontalSpacing.floatValue();
        } else {
            this.horizontalBorderSpacing = 0.0f;
        }
        calculateTableWidth(availableWidth, calculateTableMaxWidth);
    }

    boolean hasFixedLayout() {
        return this.fixedTableLayout;
    }

    float[] layout() {
        if (hasFixedLayout()) {
            return fixedLayout();
        }
        return autoLayout();
    }

    float getMinWidth() {
        return this.layoutMinWidth;
    }

    float[] autoLayout() {
        if (!$assertionsDisabled && !this.tableRenderer.getTable().isComplete()) {
            throw new AssertionError();
        }
        fillAndSortCells();
        calculateMinMaxWidths();
        float minSum = 0.0f;
        for (ColumnWidthData width : this.widths) {
            minSum += width.min;
        }
        for (CellInfo cell : this.cells) {
            processCell(cell);
        }
        processColumns();
        recalculate(minSum);
        return extractWidths();
    }

    List<CellInfo> autoLayoutCustom() {
        if (!$assertionsDisabled && !this.tableRenderer.getTable().isComplete()) {
            throw new AssertionError();
        }
        fillAndSortCells();
        calculateMinMaxWidths();
        return this.cells;
    }

    void processCell(CellInfo cell) {
        UnitValue cellWidth = getCellWidth(cell.getCell(), false);
        if (cellWidth != null) {
            if (!$assertionsDisabled && cellWidth.getValue() <= 0.0f) {
                throw new AssertionError();
            }
            if (cellWidth.isPercentValue()) {
                if (cell.getColspan() == 1) {
                    this.widths[cell.getCol()].setPercents(cellWidth.getValue());
                    return;
                }
                int pointColumns = 0;
                float percentSum = 0.0f;
                for (int i = cell.getCol(); i < cell.getCol() + cell.getColspan(); i++) {
                    if (!this.widths[i].isPercent) {
                        pointColumns++;
                    } else {
                        percentSum += this.widths[i].width;
                    }
                }
                float percentAddition = cellWidth.getValue() - percentSum;
                if (percentAddition > 0.0f) {
                    if (pointColumns == 0) {
                        for (int i2 = cell.getCol(); i2 < cell.getCol() + cell.getColspan(); i2++) {
                            this.widths[i2].addPercents(percentAddition / cell.getColspan());
                        }
                        return;
                    }
                    for (int i3 = cell.getCol(); i3 < cell.getCol() + cell.getColspan(); i3++) {
                        if (!this.widths[i3].isPercent) {
                            this.widths[i3].setPercents(percentAddition / pointColumns);
                        }
                    }
                    return;
                }
                return;
            }
            if (cell.getColspan() == 1) {
                if (this.widths[cell.getCol()].isPercent) {
                    return;
                }
                if (this.widths[cell.getCol()].min <= cellWidth.getValue()) {
                    this.widths[cell.getCol()].setPoints(cellWidth.getValue()).setFixed(true);
                    return;
                } else {
                    this.widths[cell.getCol()].setPoints(this.widths[cell.getCol()].min);
                    return;
                }
            }
            processCellsRemainWidth(cell, cellWidth);
            return;
        }
        if (this.widths[cell.getCol()].isFlexible()) {
            int flexibleCols = 0;
            float remainWidth = 0.0f;
            for (int i4 = cell.getCol(); i4 < cell.getCol() + cell.getColspan(); i4++) {
                if (this.widths[i4].isFlexible()) {
                    remainWidth += this.widths[i4].max - this.widths[i4].width;
                    flexibleCols++;
                }
            }
            if (remainWidth > 0.0f) {
                for (int i5 = cell.getCol(); i5 < cell.getCol() + cell.getColspan(); i5++) {
                    if (this.widths[i5].isFlexible()) {
                        this.widths[i5].addPoints(remainWidth / flexibleCols);
                    }
                }
            }
        }
    }

    void processColumns() {
        for (int i = 0; i < this.numberOfColumns; i++) {
            UnitValue colWidth = getTable().getColumnWidth(i);
            if (colWidth != null && colWidth.getValue() > 0.0f) {
                if (colWidth.isPercentValue()) {
                    if (!this.widths[i].isPercent) {
                        if (this.widths[i].isFixed && this.widths[i].width > this.widths[i].min) {
                            this.widths[i].max = this.widths[i].width;
                        }
                        this.widths[i].setPercents(colWidth.getValue());
                    }
                } else if (!this.widths[i].isPercent && colWidth.getValue() >= this.widths[i].min) {
                    if (this.widths[i].isFixed) {
                        this.widths[i].setPoints(colWidth.getValue());
                    } else {
                        this.widths[i].resetPoints(colWidth.getValue()).setFixed(true);
                    }
                }
            }
        }
    }

    void recalculate(float minSum) {
        if (this.tableWidth - minSum < 0.0f) {
            for (int i = 0; i < this.numberOfColumns; i++) {
                this.widths[i].finalWidth = this.widths[i].min;
            }
            return;
        }
        float sumOfPercents = 0.0f;
        float minTableWidth = 0.0f;
        float totalNonPercent = 0.0f;
        for (int i2 = 0; i2 < this.widths.length; i2++) {
            if (!this.widths[i2].isPercent) {
                minTableWidth += this.widths[i2].min;
                totalNonPercent += this.widths[i2].width;
            } else if (sumOfPercents < 100.0f && sumOfPercents + this.widths[i2].width > 100.0f) {
                this.widths[i2].width = 100.0f - sumOfPercents;
                sumOfPercents += this.widths[i2].width;
                warn100percent();
            } else if (sumOfPercents >= 100.0f) {
                this.widths[i2].resetPoints(this.widths[i2].min);
                minTableWidth += this.widths[i2].min;
                warn100percent();
            } else {
                sumOfPercents += this.widths[i2].width;
            }
        }
        if (!$assertionsDisabled && sumOfPercents > 100.0f) {
            throw new AssertionError();
        }
        boolean toBalance = true;
        if (!this.fixedTableWidth) {
            float tableWidthBasedOnPercents = sumOfPercents < 100.0f ? (totalNonPercent * 100.0f) / (100.0f - sumOfPercents) : 0.0f;
            for (int i3 = 0; i3 < this.numberOfColumns; i3++) {
                if (this.widths[i3].isPercent && this.widths[i3].width > 0.0f) {
                    tableWidthBasedOnPercents = Math.max((this.widths[i3].max * 100.0f) / this.widths[i3].width, tableWidthBasedOnPercents);
                }
            }
            if (tableWidthBasedOnPercents <= this.tableWidth) {
                if (tableWidthBasedOnPercents >= minTableWidth) {
                    this.tableWidth = tableWidthBasedOnPercents;
                    toBalance = false;
                } else {
                    this.tableWidth = minTableWidth;
                }
            }
        }
        if (sumOfPercents > 0.0f && sumOfPercents < 100.0f && totalNonPercent == 0.0f) {
            for (int i4 = 0; i4 < this.widths.length; i4++) {
                this.widths[i4].width = (100.0f * this.widths[i4].width) / sumOfPercents;
            }
            sumOfPercents = 100.0f;
        }
        if (!toBalance) {
            for (int i5 = 0; i5 < this.numberOfColumns; i5++) {
                this.widths[i5].finalWidth = this.widths[i5].isPercent ? (this.tableWidth * this.widths[i5].width) / 100.0f : this.widths[i5].width;
            }
            return;
        }
        if (sumOfPercents >= 100.0f) {
            float sumOfPercents2 = 100.0f;
            boolean recalculatePercents = false;
            float remainWidth = this.tableWidth - minTableWidth;
            for (int i6 = 0; i6 < this.numberOfColumns; i6++) {
                if (!this.widths[i6].isPercent) {
                    this.widths[i6].finalWidth = this.widths[i6].min;
                } else if ((remainWidth * this.widths[i6].width) / 100.0f >= this.widths[i6].min) {
                    this.widths[i6].finalWidth = (remainWidth * this.widths[i6].width) / 100.0f;
                } else {
                    this.widths[i6].finalWidth = this.widths[i6].min;
                    this.widths[i6].isPercent = false;
                    remainWidth -= this.widths[i6].min;
                    sumOfPercents2 -= this.widths[i6].width;
                    recalculatePercents = true;
                }
            }
            if (recalculatePercents) {
                for (int i7 = 0; i7 < this.numberOfColumns; i7++) {
                    if (this.widths[i7].isPercent) {
                        this.widths[i7].finalWidth = (remainWidth * this.widths[i7].width) / sumOfPercents2;
                    }
                }
                return;
            }
            return;
        }
        float totalPercent = 0.0f;
        float minTotalNonPercent = 0.0f;
        float fixedAddition = 0.0f;
        float flexibleAddition = 0.0f;
        boolean hasFlexibleCell = false;
        for (int i8 = 0; i8 < this.numberOfColumns; i8++) {
            if (!this.widths[i8].isPercent) {
                this.widths[i8].finalWidth = this.widths[i8].min;
                minTotalNonPercent += this.widths[i8].min;
                float addition = this.widths[i8].width - this.widths[i8].min;
                if (this.widths[i8].isFixed) {
                    fixedAddition += addition;
                } else {
                    flexibleAddition += addition;
                    hasFlexibleCell = true;
                }
            } else if ((this.tableWidth * this.widths[i8].width) / 100.0f >= this.widths[i8].min) {
                this.widths[i8].finalWidth = (this.tableWidth * this.widths[i8].width) / 100.0f;
                totalPercent += this.widths[i8].finalWidth;
            } else {
                sumOfPercents -= this.widths[i8].width;
                this.widths[i8].resetPoints(this.widths[i8].min);
                this.widths[i8].finalWidth = this.widths[i8].min;
                minTotalNonPercent += this.widths[i8].min;
            }
        }
        if (totalPercent + minTotalNonPercent > this.tableWidth) {
            float extraWidth = this.tableWidth - minTotalNonPercent;
            if (sumOfPercents > 0.0f) {
                for (int i9 = 0; i9 < this.numberOfColumns; i9++) {
                    if (this.widths[i9].isPercent) {
                        this.widths[i9].finalWidth = (extraWidth * this.widths[i9].width) / sumOfPercents;
                    }
                }
                return;
            }
            return;
        }
        float extraWidth2 = (this.tableWidth - totalPercent) - minTotalNonPercent;
        if (fixedAddition > 0.0f && (extraWidth2 < fixedAddition || !hasFlexibleCell)) {
            for (int i10 = 0; i10 < this.numberOfColumns; i10++) {
                if (this.widths[i10].isFixed) {
                    this.widths[i10].finalWidth += ((this.widths[i10].width - this.widths[i10].min) * extraWidth2) / fixedAddition;
                }
            }
            return;
        }
        float extraWidth3 = extraWidth2 - fixedAddition;
        if (extraWidth3 < flexibleAddition) {
            for (int i11 = 0; i11 < this.numberOfColumns; i11++) {
                if (this.widths[i11].isFixed) {
                    this.widths[i11].finalWidth = this.widths[i11].width;
                } else if (!this.widths[i11].isPercent) {
                    this.widths[i11].finalWidth += ((this.widths[i11].width - this.widths[i11].min) * extraWidth3) / flexibleAddition;
                }
            }
            return;
        }
        float totalFixed = 0.0f;
        float totalFlexible = 0.0f;
        float flexibleCount = 0.0f;
        for (int i12 = 0; i12 < this.numberOfColumns; i12++) {
            if (this.widths[i12].isFixed) {
                this.widths[i12].finalWidth = this.widths[i12].width;
                totalFixed += this.widths[i12].width;
            } else if (!this.widths[i12].isPercent) {
                totalFlexible += this.widths[i12].width;
                flexibleCount += 1.0f;
            }
        }
        if (!$assertionsDisabled && totalFlexible <= 0.0f && flexibleCount <= 0.0f) {
            throw new AssertionError();
        }
        float extraWidth4 = (this.tableWidth - totalPercent) - totalFixed;
        for (int i13 = 0; i13 < this.numberOfColumns; i13++) {
            if (!this.widths[i13].isPercent && !this.widths[i13].isFixed) {
                this.widths[i13].finalWidth = totalFlexible > 0.0f ? (this.widths[i13].width * extraWidth4) / totalFlexible : extraWidth4 / flexibleCount;
            }
        }
    }

    void processCellsRemainWidth(CellInfo cell, UnitValue cellWidth) {
        int flexibleCols = 0;
        float remainWidth = cellWidth.getValue();
        int i = cell.getCol();
        while (true) {
            if (i >= cell.getCol() + cell.getColspan()) {
                break;
            }
            if (!this.widths[i].isPercent) {
                remainWidth -= this.widths[i].width;
                if (!this.widths[i].isFixed) {
                    flexibleCols++;
                }
                i++;
            } else {
                remainWidth = 0.0f;
                break;
            }
        }
        if (remainWidth > 0.0f) {
            int[] flexibleColIndexes = ArrayUtil.fillWithValue(new int[cell.getColspan()], -1);
            if (flexibleCols > 0) {
                for (int i2 = cell.getCol(); i2 < cell.getCol() + cell.getColspan(); i2++) {
                    if (this.widths[i2].isFlexible()) {
                        if (this.widths[i2].min > this.widths[i2].width + (remainWidth / flexibleCols)) {
                            this.widths[i2].resetPoints(this.widths[i2].min);
                            remainWidth -= this.widths[i2].min - this.widths[i2].width;
                            flexibleCols--;
                            if (flexibleCols == 0 || remainWidth <= 0.0f) {
                                break;
                            }
                        } else {
                            flexibleColIndexes[i2 - cell.getCol()] = i2;
                        }
                    }
                }
                if (flexibleCols > 0 && remainWidth > 0.0f) {
                    for (int i3 = 0; i3 < flexibleColIndexes.length; i3++) {
                        if (flexibleColIndexes[i3] >= 0) {
                            this.widths[flexibleColIndexes[i3]].addPoints(remainWidth / flexibleCols).setFixed(true);
                        }
                    }
                    return;
                }
                return;
            }
            for (int i4 = cell.getCol(); i4 < cell.getCol() + cell.getColspan(); i4++) {
                this.widths[i4].addPoints(remainWidth / cell.getColspan());
            }
        }
    }

    float[] fixedLayout() {
        CellRenderer[] firtsRow;
        UnitValue cellWidth;
        float width;
        float[] columnWidths = new float[this.numberOfColumns];
        for (int i = 0; i < this.numberOfColumns; i++) {
            UnitValue colWidth = getTable().getColumnWidth(i);
            if (colWidth == null || colWidth.getValue() < 0.0f) {
                columnWidths[i] = -1.0f;
            } else if (colWidth.isPercentValue()) {
                columnWidths[i] = (colWidth.getValue() * this.tableWidth) / 100.0f;
            } else {
                columnWidths[i] = colWidth.getValue();
            }
        }
        int processedColumns = 0;
        float remainWidth = this.tableWidth;
        if (this.tableRenderer.headerRenderer != null && this.tableRenderer.headerRenderer.rows.size() > 0) {
            firtsRow = this.tableRenderer.headerRenderer.rows.get(0);
        } else if (this.tableRenderer.rows.size() > 0 && getTable().isComplete() && 0 == getTable().getLastRowBottomBorder().size()) {
            firtsRow = this.tableRenderer.rows.get(0);
        } else {
            firtsRow = null;
        }
        float[] columnWidthIfPercent = new float[columnWidths.length];
        for (int i2 = 0; i2 < columnWidthIfPercent.length; i2++) {
            columnWidthIfPercent[i2] = -1.0f;
        }
        float sumOfPercents = 0.0f;
        if (firtsRow != null && getTable().isComplete() && getTable().getLastRowBottomBorder().isEmpty()) {
            for (int i3 = 0; i3 < this.numberOfColumns; i3++) {
                if (columnWidths[i3] == -1.0f) {
                    CellRenderer cell = firtsRow[i3];
                    if (cell != null && (cellWidth = getCellWidth(cell, true)) != null) {
                        if (!$assertionsDisabled && cellWidth.getValue() < 0.0f) {
                            throw new AssertionError();
                        }
                        if (cellWidth.isPercentValue()) {
                            width = (this.tableWidth * cellWidth.getValue()) / 100.0f;
                            columnWidthIfPercent[i3] = cellWidth.getValue();
                            sumOfPercents += columnWidthIfPercent[i3];
                        } else {
                            width = cellWidth.getValue();
                        }
                        int colspan = ((Cell) cell.getModelElement()).getColspan();
                        for (int j = 0; j < colspan; j++) {
                            columnWidths[i3 + j] = width / colspan;
                        }
                        remainWidth -= columnWidths[i3];
                        processedColumns++;
                    }
                } else {
                    remainWidth -= columnWidths[i3];
                    processedColumns++;
                }
            }
        } else {
            for (int i4 = 0; i4 < this.numberOfColumns; i4++) {
                if (columnWidths[i4] != -1.0f) {
                    processedColumns++;
                    remainWidth -= columnWidths[i4];
                }
            }
        }
        if (sumOfPercents > 100.0f) {
            warn100percent();
        }
        if (remainWidth > 0.0f) {
            if (this.numberOfColumns == processedColumns) {
                for (int i5 = 0; i5 < this.numberOfColumns; i5++) {
                    columnWidths[i5] = (this.tableWidth * columnWidths[i5]) / (this.tableWidth - remainWidth);
                }
            }
        } else if (remainWidth < 0.0f) {
            for (int i6 = 0; i6 < this.numberOfColumns; i6++) {
                int i7 = i6;
                columnWidths[i7] = columnWidths[i7] + (-1.0f != columnWidthIfPercent[i6] ? (remainWidth * columnWidthIfPercent[i6]) / sumOfPercents : 0.0f);
            }
        }
        for (int i8 = 0; i8 < this.numberOfColumns; i8++) {
            if (columnWidths[i8] == -1.0f) {
                columnWidths[i8] = Math.max(0.0f, remainWidth / (this.numberOfColumns - processedColumns));
            }
        }
        if (this.tableRenderer.bordersHandler instanceof SeparatedTableBorders) {
            for (int i9 = 0; i9 < this.numberOfColumns; i9++) {
                int i10 = i9;
                columnWidths[i10] = columnWidths[i10] + this.horizontalBorderSpacing;
            }
        }
        return columnWidths;
    }

    private void calculateTableWidth(float availableWidth, boolean calculateTableMaxWidth) {
        this.fixedTableLayout = "fixed".equals(((String) this.tableRenderer.getProperty(93, "auto")).toLowerCase());
        UnitValue width = (UnitValue) this.tableRenderer.getProperty(77);
        if (this.fixedTableLayout && width != null && width.getValue() >= 0.0f) {
            if (0 != getTable().getLastRowBottomBorder().size()) {
                width = getTable().getWidth();
            } else if (!getTable().isComplete() && null != getTable().getWidth() && getTable().getWidth().isPercentValue()) {
                getTable().setWidth(this.tableRenderer.retrieveUnitValue(availableWidth, 77).floatValue());
            }
            this.fixedTableWidth = true;
            this.tableWidth = retrieveTableWidth(width, availableWidth).floatValue();
            this.layoutMinWidth = width.isPercentValue() ? 0.0f : this.tableWidth;
        } else {
            this.fixedTableLayout = false;
            this.layoutMinWidth = -1.0f;
            if (!calculateTableMaxWidth && width != null && width.getValue() >= 0.0f) {
                this.fixedTableWidth = true;
                this.tableWidth = retrieveTableWidth(width, availableWidth).floatValue();
            } else {
                this.fixedTableWidth = false;
                this.tableWidth = retrieveTableWidth(availableWidth);
            }
        }
        Float min = retrieveTableWidth((UnitValue) this.tableRenderer.getProperty(80), availableWidth);
        Float max = retrieveTableWidth((UnitValue) this.tableRenderer.getProperty(79), availableWidth);
        this.tableMinWidth = min != null ? min.floatValue() : this.layoutMinWidth;
        this.tableMaxWidth = max != null ? max.floatValue() : this.tableWidth;
        if (this.tableMinWidth > this.tableMaxWidth) {
            this.tableMaxWidth = this.tableMinWidth;
        }
        if (this.tableMinWidth > this.tableWidth) {
            this.tableWidth = this.tableMinWidth;
        }
        if (this.tableMaxWidth < this.tableWidth) {
            this.tableWidth = this.tableMaxWidth;
        }
    }

    private Float retrieveTableWidth(UnitValue width, float availableWidth) {
        float value;
        if (width == null) {
            return null;
        }
        if (width.isPercentValue()) {
            value = (width.getValue() * availableWidth) / 100.0f;
        } else {
            value = width.getValue();
        }
        return Float.valueOf(retrieveTableWidth(value));
    }

    private float retrieveTableWidth(float width) {
        float width2;
        if (BorderCollapsePropertyValue.SEPARATE.equals(this.tableRenderer.getProperty(114))) {
            width2 = (width - (this.rightBorderMaxWidth + this.leftBorderMaxWidth)) - ((this.numberOfColumns + 1) * this.horizontalBorderSpacing);
        } else {
            width2 = width - ((this.rightBorderMaxWidth + this.leftBorderMaxWidth) / 2.0f);
        }
        return Math.max(width2, 0.0f);
    }

    private Table getTable() {
        return (Table) this.tableRenderer.getModelElement();
    }

    private void calculateMinMaxWidths() {
        float[] minWidths = new float[this.numberOfColumns];
        float[] maxWidths = new float[this.numberOfColumns];
        for (CellInfo cell : this.cells) {
            cell.setParent(this.tableRenderer);
            MinMaxWidth minMax = cell.getCell().getMinMaxWidth();
            float[] indents = getCellBorderIndents(cell);
            if (BorderCollapsePropertyValue.SEPARATE.equals(this.tableRenderer.getProperty(114))) {
                minMax.setAdditionalWidth(minMax.getAdditionalWidth() - this.horizontalBorderSpacing);
            } else {
                minMax.setAdditionalWidth(minMax.getAdditionalWidth() + (indents[1] / 2.0f) + (indents[3] / 2.0f));
            }
            if (cell.getColspan() == 1) {
                minWidths[cell.getCol()] = Math.max(minMax.getMinWidth(), minWidths[cell.getCol()]);
                maxWidths[cell.getCol()] = Math.max(minMax.getMaxWidth(), maxWidths[cell.getCol()]);
            } else {
                float remainMin = minMax.getMinWidth();
                float remainMax = minMax.getMaxWidth();
                for (int i = cell.getCol(); i < cell.getCol() + cell.getColspan(); i++) {
                    remainMin -= minWidths[i];
                    remainMax -= maxWidths[i];
                }
                if (remainMin > 0.0f) {
                    for (int i2 = cell.getCol(); i2 < cell.getCol() + cell.getColspan(); i2++) {
                        int i3 = i2;
                        minWidths[i3] = minWidths[i3] + (remainMin / cell.getColspan());
                    }
                }
                if (remainMax > 0.0f) {
                    for (int i4 = cell.getCol(); i4 < cell.getCol() + cell.getColspan(); i4++) {
                        int i5 = i4;
                        maxWidths[i5] = maxWidths[i5] + (remainMax / cell.getColspan());
                    }
                }
            }
        }
        for (int i6 = 0; i6 < this.widths.length; i6++) {
            this.widths[i6] = new ColumnWidthData(minWidths[i6], maxWidths[i6]);
        }
    }

    private float[] getCellBorderIndents(CellInfo cell) {
        TableRenderer renderer;
        if (cell.region == 1) {
            renderer = this.tableRenderer.headerRenderer;
        } else if (cell.region == 3) {
            renderer = this.tableRenderer.footerRenderer;
        } else {
            renderer = this.tableRenderer;
        }
        return renderer.bordersHandler.getCellBorderIndents(cell.getRow(), cell.getCol(), cell.getRowspan(), cell.getColspan());
    }

    private void fillAndSortCells() {
        this.cells = new ArrayList();
        if (this.tableRenderer.headerRenderer != null) {
            fillRendererCells(this.tableRenderer.headerRenderer, (byte) 1);
        }
        fillRendererCells(this.tableRenderer, (byte) 2);
        if (this.tableRenderer.footerRenderer != null) {
            fillRendererCells(this.tableRenderer.footerRenderer, (byte) 3);
        }
        Collections.sort(this.cells);
    }

    private void fillRendererCells(TableRenderer renderer, byte region) {
        for (int row = 0; row < renderer.rows.size(); row++) {
            for (int col = 0; col < this.numberOfColumns; col++) {
                CellRenderer cell = renderer.rows.get(row)[col];
                if (cell != null) {
                    this.cells.add(new CellInfo(cell, row, col, region));
                }
            }
        }
    }

    private void warn100percent() {
        Logger logger = LoggerFactory.getLogger((Class<?>) TableWidths.class);
        logger.warn(LogMessageConstant.SUM_OF_TABLE_COLUMNS_IS_GREATER_THAN_100);
    }

    private float[] extractWidths() {
        float actualWidth = 0.0f;
        this.layoutMinWidth = 0.0f;
        float[] columnWidths = new float[this.widths.length];
        for (int i = 0; i < this.widths.length; i++) {
            if (!$assertionsDisabled && this.widths[i].finalWidth < 0.0f) {
                throw new AssertionError();
            }
            columnWidths[i] = this.widths[i].finalWidth + this.horizontalBorderSpacing;
            actualWidth += this.widths[i].finalWidth;
            this.layoutMinWidth += this.widths[i].min + this.horizontalBorderSpacing;
        }
        if (actualWidth > this.tableWidth + (MinMaxWidthUtils.getEps() * this.widths.length)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TableWidths.class);
            logger.warn(LogMessageConstant.TABLE_WIDTH_IS_MORE_THAN_EXPECTED_DUE_TO_MIN_WIDTH);
        }
        return columnWidths;
    }

    public String toString() {
        return "width=" + this.tableWidth + (this.fixedTableWidth ? "!!" : "");
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableWidths$ColumnWidthData.class */
    private static class ColumnWidthData {
        final float min;
        float max;
        float width = 0.0f;
        float finalWidth = -1.0f;
        boolean isPercent = false;
        boolean isFixed = false;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !TableWidths.class.desiredAssertionStatus();
        }

        ColumnWidthData(float min, float max) {
            if (!$assertionsDisabled && min < 0.0f) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && max < 0.0f) {
                throw new AssertionError();
            }
            this.min = min > 0.0f ? min + MinMaxWidthUtils.getEps() : 0.0f;
            this.max = max > 0.0f ? Math.min(max + MinMaxWidthUtils.getEps(), 32760.0f) : 0.0f;
        }

        ColumnWidthData setPoints(float width) {
            if (!$assertionsDisabled && this.isPercent) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.min > width) {
                throw new AssertionError();
            }
            this.width = Math.max(this.width, width);
            return this;
        }

        ColumnWidthData resetPoints(float width) {
            if (!$assertionsDisabled && this.min > width) {
                throw new AssertionError();
            }
            this.width = width;
            this.isPercent = false;
            return this;
        }

        ColumnWidthData addPoints(float width) {
            if (!$assertionsDisabled && this.isPercent) {
                throw new AssertionError();
            }
            this.width += width;
            return this;
        }

        ColumnWidthData setPercents(float percent) {
            if (this.isPercent) {
                this.width = Math.max(this.width, percent);
            } else {
                this.isPercent = true;
                this.width = percent;
            }
            this.isFixed = false;
            return this;
        }

        ColumnWidthData addPercents(float width) {
            if (!$assertionsDisabled && !this.isPercent) {
                throw new AssertionError();
            }
            this.width += width;
            return this;
        }

        ColumnWidthData setFixed(boolean fixed) {
            this.isFixed = fixed;
            return this;
        }

        boolean isFlexible() {
            return (this.isFixed || this.isPercent) ? false : true;
        }

        public String toString() {
            return "w=" + this.width + (this.isPercent ? QuickTargetSourceCreator.PREFIX_THREAD_LOCAL : "pt") + (this.isFixed ? " !!" : "") + ", min=" + this.min + ", max=" + this.max + ", finalWidth=" + this.finalWidth;
        }
    }

    private UnitValue getCellWidth(CellRenderer cell, boolean zeroIsValid) {
        float width;
        float width2;
        UnitValue widthValue = (UnitValue) cell.getProperty(77);
        if (widthValue == null || widthValue.getValue() < 0.0f) {
            return null;
        }
        if (widthValue.getValue() == 0.0f) {
            if (zeroIsValid) {
                return ZeroWidth;
            }
            return null;
        }
        if (widthValue.isPercentValue()) {
            return widthValue;
        }
        UnitValue widthValue2 = resolveMinMaxCollision(cell, widthValue);
        if (!AbstractRenderer.isBorderBoxSizing(cell)) {
            Border[] borders = cell.getBorders();
            if (borders[1] != null) {
                float value = widthValue2.getValue();
                if (this.tableRenderer.bordersHandler instanceof SeparatedTableBorders) {
                    width2 = borders[1].getWidth();
                } else {
                    width2 = borders[1].getWidth() / 2.0f;
                }
                widthValue2.setValue(value + width2);
            }
            if (borders[3] != null) {
                float value2 = widthValue2.getValue();
                if (this.tableRenderer.bordersHandler instanceof SeparatedTableBorders) {
                    width = borders[3].getWidth();
                } else {
                    width = borders[3].getWidth() / 2.0f;
                }
                widthValue2.setValue(value2 + width);
            }
            UnitValue[] paddings = cell.getPaddings();
            if (!paddings[1].isPointValue()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) TableWidths.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 48));
            }
            if (!paddings[3].isPointValue()) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) TableWidths.class);
                logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 49));
            }
            widthValue2.setValue(widthValue2.getValue() + paddings[1].getValue() + paddings[3].getValue());
        }
        return widthValue2;
    }

    private UnitValue resolveMinMaxCollision(CellRenderer cell, UnitValue widthValue) {
        if (!$assertionsDisabled && !widthValue.isPointValue()) {
            throw new AssertionError();
        }
        UnitValue minWidthValue = (UnitValue) cell.getProperty(80);
        if (minWidthValue != null && minWidthValue.isPointValue() && minWidthValue.getValue() > widthValue.getValue()) {
            return minWidthValue;
        }
        UnitValue maxWidthValue = (UnitValue) cell.getProperty(79);
        if (maxWidthValue != null && maxWidthValue.isPointValue() && maxWidthValue.getValue() < widthValue.getValue()) {
            return maxWidthValue;
        }
        return widthValue;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableWidths$CellInfo.class */
    static class CellInfo implements Comparable<CellInfo> {
        static final byte HEADER = 1;
        static final byte BODY = 2;
        static final byte FOOTER = 3;
        private final CellRenderer cell;
        private final int row;
        private final int col;
        final byte region;

        CellInfo(CellRenderer cell, int row, int col, byte region) {
            this.cell = cell;
            this.region = region;
            this.row = row;
            this.col = col;
        }

        CellRenderer getCell() {
            return this.cell;
        }

        int getCol() {
            return this.col;
        }

        int getColspan() {
            return this.cell.getPropertyAsInteger(16).intValue();
        }

        int getRow() {
            return this.row;
        }

        int getRowspan() {
            return this.cell.getPropertyAsInteger(60).intValue();
        }

        @Override // java.lang.Comparable
        public int compareTo(CellInfo o) {
            if ((getColspan() == 1) ^ (o.getColspan() == 1)) {
                return getColspan() - o.getColspan();
            }
            if (this.region == o.region && getRow() == o.getRow()) {
                return ((getCol() + getColspan()) - o.getCol()) - o.getColspan();
            }
            return this.region == o.region ? getRow() - o.getRow() : this.region - o.region;
        }

        public String toString() {
            String str = MessageFormatUtil.format("row={0}, col={1}, rowspan={2}, colspan={3}, ", Integer.valueOf(getRow()), Integer.valueOf(getCol()), Integer.valueOf(getRowspan()), Integer.valueOf(getColspan()));
            if (this.region == 1) {
                str = str + "header";
            } else if (this.region == 2) {
                str = str + "body";
            } else if (this.region == 3) {
                str = str + "footer";
            }
            return str;
        }

        public void setParent(TableRenderer tableRenderer) {
            if (this.region == 1) {
                this.cell.setParent(tableRenderer.headerRenderer);
            } else if (this.region == 3) {
                this.cell.setParent(tableRenderer.footerRenderer);
            } else {
                this.cell.setParent(tableRenderer);
            }
        }
    }
}
