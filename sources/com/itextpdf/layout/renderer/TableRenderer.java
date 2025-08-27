package com.itextpdf.layout.renderer;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.margincollapse.MarginsCollapseHandler;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.minmaxwidth.MinMaxWidthUtils;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.CaptionSide;
import com.itextpdf.layout.property.FloatPropertyValue;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.tagging.LayoutTaggingHelper;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableRenderer.class */
public class TableRenderer extends AbstractRenderer {
    protected List<CellRenderer[]> rows;
    protected Table.RowRange rowRange;
    protected TableRenderer headerRenderer;
    protected TableRenderer footerRenderer;
    protected DivRenderer captionRenderer;
    protected boolean isOriginalNonSplitRenderer;
    TableBorders bordersHandler;
    private float[] columnWidths;
    private List<Float> heights;
    private float[] countedColumnWidth;
    private float totalWidthForColumns;
    private float topBorderMaxWidth;

    private TableRenderer() {
        this.rows = new ArrayList();
        this.isOriginalNonSplitRenderer = true;
        this.columnWidths = null;
        this.heights = new ArrayList();
        this.countedColumnWidth = null;
    }

    public TableRenderer(Table modelElement, Table.RowRange rowRange) {
        super(modelElement);
        this.rows = new ArrayList();
        this.isOriginalNonSplitRenderer = true;
        this.columnWidths = null;
        this.heights = new ArrayList();
        this.countedColumnWidth = null;
        setRowRange(rowRange);
    }

    public TableRenderer(Table modelElement) {
        this(modelElement, new Table.RowRange(0, modelElement.getNumberOfRows() - 1));
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void addChild(IRenderer renderer) {
        if (renderer instanceof CellRenderer) {
            Cell cell = (Cell) renderer.getModelElement();
            this.rows.get(((cell.getRow() - this.rowRange.getStartRow()) + cell.getRowspan()) - 1)[cell.getCol()] = (CellRenderer) renderer;
        } else {
            Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger.error("Only CellRenderer could be added");
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Rectangle applyBorderBox(Rectangle rect, Border[] borders, boolean reverse) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            super.applyBorderBox(rect, borders, reverse);
        }
        return rect;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Rectangle applyPaddings(Rectangle rect, UnitValue[] paddings, boolean reverse) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            super.applyPaddings(rect, paddings, reverse);
        }
        return rect;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public Rectangle applyPaddings(Rectangle rect, boolean reverse) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            super.applyPaddings(rect, reverse);
        }
        return rect;
    }

    private Rectangle applySpacing(Rectangle rect, float horizontalSpacing, float verticalSpacing, boolean reverse) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            return rect.applyMargins(verticalSpacing / 2.0f, horizontalSpacing / 2.0f, verticalSpacing / 2.0f, horizontalSpacing / 2.0f, reverse);
        }
        return rect;
    }

    private Rectangle applySingleSpacing(Rectangle rect, float spacing, boolean isHorizontal, boolean reverse) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            if (isHorizontal) {
                return rect.applyMargins(0.0f, spacing / 2.0f, 0.0f, spacing / 2.0f, reverse);
            }
            return rect.applyMargins(spacing / 2.0f, 0.0f, spacing / 2.0f, 0.0f, reverse);
        }
        return rect;
    }

    Table getTable() {
        return (Table) getModelElement();
    }

    private void initializeHeaderAndFooter(boolean isFirstOnThePage) {
        Table table = (Table) getModelElement();
        Border[] tableBorder = getBorders();
        Table headerElement = table.getHeader();
        boolean isFirstHeader = this.rowRange.getStartRow() == 0 && this.isOriginalNonSplitRenderer;
        boolean headerShouldBeApplied = (table.isComplete() || !this.rows.isEmpty()) && isFirstOnThePage && !((table.isSkipFirstHeader() && isFirstHeader) || Boolean.TRUE.equals(getOwnProperty(97)));
        if (headerElement != null && headerShouldBeApplied) {
            this.headerRenderer = initFooterOrHeaderRenderer(false, tableBorder);
        }
        Table footerElement = table.getFooter();
        boolean footerShouldBeApplied = ((table.isComplete() && 0 != table.getLastRowBottomBorder().size() && table.isSkipLastFooter()) || Boolean.TRUE.equals(getOwnProperty(96))) ? false : true;
        if (footerElement != null && footerShouldBeApplied) {
            this.footerRenderer = initFooterOrHeaderRenderer(true, tableBorder);
        }
    }

    private void initializeCaptionRenderer(Div caption) {
        if (this.isOriginalNonSplitRenderer && null != caption) {
            this.captionRenderer = (DivRenderer) caption.createRendererSubTree();
            this.captionRenderer.setParent(this.parent);
            LayoutTaggingHelper taggingHelper = (LayoutTaggingHelper) getProperty(108);
            if (taggingHelper != null) {
                taggingHelper.addKidsHint(this, Collections.singletonList(this.captionRenderer));
                LayoutTaggingHelper.addTreeHints(taggingHelper, this.captionRenderer);
            }
        }
    }

    private boolean isOriginalRenderer() {
        return (!this.isOriginalNonSplitRenderer || isFooterRenderer() || isHeaderRenderer()) ? false : true;
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutTaggingHelper taggingHelper;
        TableBorders collapsedTableBorders;
        Float blockMinHeight = retrieveMinHeight();
        Float blockMaxHeight = retrieveMaxHeight();
        LayoutArea area = layoutContext.getArea();
        boolean wasParentsHeightClipped = layoutContext.isClippedHeight();
        boolean wasHeightClipped = false;
        Rectangle layoutBox = area.getBBox().mo825clone();
        Table tableModel = (Table) getModelElement();
        if (!tableModel.isComplete()) {
            setProperty(43, UnitValue.createPointValue(0.0f));
        }
        if (this.rowRange.getStartRow() != 0) {
            setProperty(46, UnitValue.createPointValue(0.0f));
        }
        this.heights.clear();
        this.childRenderers.clear();
        Map<Integer, Integer> rowMoves = new HashMap<>();
        int numberOfColumns = ((Table) getModelElement()).getNumberOfColumns();
        List<Border> lastFlushedRowBottomBorder = tableModel.getLastRowBottomBorder();
        boolean isAndWasComplete = tableModel.isComplete() && 0 == lastFlushedRowBottomBorder.size();
        boolean isFirstOnThePage = 0 == this.rowRange.getStartRow() || isFirstOnRootArea(true);
        if (!isFooterRenderer() && !isHeaderRenderer() && this.isOriginalNonSplitRenderer) {
            boolean isSeparated = BorderCollapsePropertyValue.SEPARATE.equals(getProperty(114));
            if (isSeparated) {
                collapsedTableBorders = new SeparatedTableBorders(this.rows, numberOfColumns, getBorders(), !isAndWasComplete ? this.rowRange.getStartRow() : 0);
            } else {
                collapsedTableBorders = new CollapsedTableBorders(this.rows, numberOfColumns, getBorders(), !isAndWasComplete ? this.rowRange.getStartRow() : 0);
            }
            this.bordersHandler = collapsedTableBorders;
            this.bordersHandler.initializeBorders();
        }
        this.bordersHandler.setRowRange(this.rowRange.getStartRow(), this.rowRange.getFinishRow());
        initializeHeaderAndFooter(isFirstOnThePage);
        this.bordersHandler.updateBordersOnNewPage(this.isOriginalNonSplitRenderer, isFooterRenderer() || isHeaderRenderer(), this, this.headerRenderer, this.footerRenderer);
        if (this.isOriginalNonSplitRenderer) {
            correctRowRange();
        }
        float horizontalBorderSpacing = (!(this.bordersHandler instanceof SeparatedTableBorders) || null == getPropertyAsFloat(115)) ? 0.0f : getPropertyAsFloat(115).floatValue();
        float verticalBorderSpacing = (!(this.bordersHandler instanceof SeparatedTableBorders) || null == getPropertyAsFloat(116)) ? 0.0f : getPropertyAsFloat(116).floatValue();
        if (!isAndWasComplete && !isFirstOnThePage) {
            layoutBox.increaseHeight(verticalBorderSpacing);
        }
        if (isOriginalRenderer()) {
            applyMarginsAndPaddingsAndCalculateColumnWidths(layoutBox);
        }
        float tableWidth = getTableWidth();
        MarginsCollapseHandler marginsCollapseHandler = null;
        boolean marginsCollapsingEnabled = Boolean.TRUE.equals(getPropertyAsBoolean(89));
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler = new MarginsCollapseHandler(this, layoutContext.getMarginsCollapseInfo());
        }
        List<Rectangle> siblingFloatRendererAreas = layoutContext.getFloatRendererAreas();
        float clearHeightCorrection = FloatingHelper.calculateClearHeightCorrection(this, siblingFloatRendererAreas, layoutBox);
        FloatPropertyValue floatPropertyValue = (FloatPropertyValue) getProperty(99);
        if (FloatingHelper.isRendererFloating(this, floatPropertyValue)) {
            layoutBox.decreaseHeight(clearHeightCorrection);
            FloatingHelper.adjustFloatedTableLayoutBox(this, layoutBox, tableWidth, siblingFloatRendererAreas, floatPropertyValue);
        } else {
            clearHeightCorrection = FloatingHelper.adjustLayoutBoxAccordingToFloats(siblingFloatRendererAreas, layoutBox, Float.valueOf(tableWidth), clearHeightCorrection, marginsCollapseHandler);
        }
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler.startMarginsCollapse(layoutBox);
        }
        applyMargins(layoutBox, false);
        applyFixedXOrYPosition(true, layoutBox);
        applyPaddings(layoutBox, false);
        if (null != blockMaxHeight && blockMaxHeight.floatValue() <= layoutBox.getHeight() && !Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
            layoutBox.moveUp(layoutBox.getHeight() - blockMaxHeight.floatValue()).setHeight(blockMaxHeight.floatValue());
            wasHeightClipped = true;
        }
        initializeCaptionRenderer(getTable().getCaption());
        if (this.captionRenderer != null) {
            float minCaptionWidth = this.captionRenderer.getMinMaxWidth().getMinWidth();
            LayoutResult captionLayoutResult = this.captionRenderer.layout(new LayoutContext(new LayoutArea(area.getPageNumber(), new Rectangle(layoutBox.getX(), layoutBox.getY(), Math.max(tableWidth, minCaptionWidth), layoutBox.getHeight())), wasHeightClipped || wasParentsHeightClipped));
            if (1 != captionLayoutResult.getStatus()) {
                return new LayoutResult(3, null, null, this, captionLayoutResult.getCauseOfNothing());
            }
            float captionHeight = captionLayoutResult.getOccupiedArea().getBBox().getHeight();
            if (CaptionSide.BOTTOM.equals(tableModel.getCaption().getProperty(119))) {
                this.captionRenderer.move(0.0f, -(layoutBox.getHeight() - captionHeight));
                layoutBox.decreaseHeight(captionHeight);
                layoutBox.moveUp(captionHeight);
            } else {
                layoutBox.decreaseHeight(captionHeight);
            }
        }
        this.occupiedArea = new LayoutArea(area.getPageNumber(), new Rectangle(layoutBox.getX(), layoutBox.getY() + layoutBox.getHeight(), tableWidth, 0.0f));
        if (this.footerRenderer != null) {
            prepareFooterOrHeaderRendererForLayout(this.footerRenderer, layoutBox.getWidth());
            if (0 != this.rows.size() || !isAndWasComplete) {
                this.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, false);
            } else if (null != this.headerRenderer) {
                this.headerRenderer.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, false);
            }
            LayoutResult result = this.footerRenderer.layout(new LayoutContext(new LayoutArea(area.getPageNumber(), layoutBox), wasHeightClipped || wasParentsHeightClipped));
            if (result.getStatus() != 1) {
                deleteOwnProperty(10);
                return new LayoutResult(3, null, null, this, result.getCauseOfNothing());
            }
            float footerHeight = result.getOccupiedArea().getBBox().getHeight();
            this.footerRenderer.move(0.0f, -(layoutBox.getHeight() - footerHeight));
            layoutBox.moveUp(footerHeight).decreaseHeight(footerHeight);
            layoutBox.moveDown(verticalBorderSpacing).increaseHeight(verticalBorderSpacing);
            if (!tableModel.isEmpty()) {
                float maxFooterTopBorderWidth = this.footerRenderer.bordersHandler.getMaxTopWidth();
                this.footerRenderer.occupiedArea.getBBox().decreaseHeight(maxFooterTopBorderWidth);
                layoutBox.moveDown(maxFooterTopBorderWidth).increaseHeight(maxFooterTopBorderWidth);
            }
            if (Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
                this.footerRenderer.setProperty(26, true);
            }
        }
        if (this.headerRenderer != null) {
            prepareFooterOrHeaderRendererForLayout(this.headerRenderer, layoutBox.getWidth());
            if (0 != this.rows.size()) {
                this.bordersHandler.collapseTableWithHeader(this.headerRenderer.bordersHandler, !tableModel.isEmpty());
            } else if (null != this.footerRenderer) {
                this.footerRenderer.bordersHandler.collapseTableWithHeader(this.headerRenderer.bordersHandler, true);
            }
            this.topBorderMaxWidth = this.bordersHandler.getMaxTopWidth();
            LayoutResult result2 = this.headerRenderer.layout(new LayoutContext(new LayoutArea(area.getPageNumber(), layoutBox), wasHeightClipped || wasParentsHeightClipped));
            if (result2.getStatus() != 1) {
                deleteOwnProperty(13);
                return new LayoutResult(3, null, null, this, result2.getCauseOfNothing());
            }
            float headerHeight = result2.getOccupiedArea().getBBox().getHeight();
            layoutBox.decreaseHeight(headerHeight);
            this.occupiedArea.getBBox().moveDown(headerHeight).increaseHeight(headerHeight);
            this.bordersHandler.fixHeaderOccupiedArea(this.occupiedArea.getBBox(), layoutBox);
            layoutBox.increaseHeight(verticalBorderSpacing);
            this.occupiedArea.getBBox().moveUp(verticalBorderSpacing).decreaseHeight(verticalBorderSpacing);
        }
        applySpacing(layoutBox, horizontalBorderSpacing, verticalBorderSpacing, false);
        applySingleSpacing(this.occupiedArea.getBBox(), horizontalBorderSpacing, true, false);
        this.occupiedArea.getBBox().moveDown(verticalBorderSpacing / 2.0f);
        this.topBorderMaxWidth = this.bordersHandler.getMaxTopWidth();
        this.bordersHandler.applyLeftAndRightTableBorder(layoutBox, false);
        this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, tableModel.isEmpty() || 0 == this.rows.size(), isAndWasComplete, false);
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            float bottomBorderWidth = this.bordersHandler.getMaxBottomWidth();
            layoutBox.moveUp(bottomBorderWidth).decreaseHeight(bottomBorderWidth);
        }
        LayoutResult[] splits = new LayoutResult[numberOfColumns];
        int[] targetOverflowRowIndex = new int[numberOfColumns];
        List<Boolean> rowsHasCellWithSetHeight = new ArrayList<>();
        int row = 0;
        while (row < this.rows.size()) {
            List<Rectangle> childFloatRendererAreas = new ArrayList<>();
            if (row == 1 && Boolean.TRUE.equals(getProperty(26))) {
                if (Boolean.TRUE.equals(getOwnProperty(26))) {
                    deleteOwnProperty(26);
                } else {
                    setProperty(26, false);
                }
            }
            CellRenderer[] currentRow = this.rows.get(row);
            float rowHeight = 0.0f;
            boolean split = false;
            boolean hasContent = true;
            boolean cellWithBigRowspanAdded = false;
            List<CellRenderer> currChildRenderers = new ArrayList<>();
            Deque<CellRendererInfo> cellProcessingQueue = new ArrayDeque<>();
            for (int col = 0; col < currentRow.length; col++) {
                if (currentRow[col] != null) {
                    cellProcessingQueue.addLast(new CellRendererInfo(currentRow[col], col, row));
                }
            }
            boolean rowHasCellWithSetHeight = false;
            IRenderer firstCauseOfNothing = null;
            this.bordersHandler.setFinishRow(this.rowRange.getStartRow() + row);
            Border widestRowBottomBorder = this.bordersHandler.getWidestHorizontalBorder(this.rowRange.getStartRow() + row + 1);
            this.bordersHandler.setFinishRow(this.rowRange.getFinishRow());
            float widestRowBottomBorderWidth = null == widestRowBottomBorder ? 0.0f : widestRowBottomBorder.getWidth();
            while (cellProcessingQueue.size() > 0) {
                CellRendererInfo currentCellInfo = cellProcessingQueue.pop();
                int col2 = currentCellInfo.column;
                CellRenderer cell = currentCellInfo.cellRenderer;
                int colspan = cell.getPropertyAsInteger(16).intValue();
                int rowspan = cell.getPropertyAsInteger(60).intValue();
                if (1 != rowspan) {
                    cellWithBigRowspanAdded = true;
                }
                targetOverflowRowIndex[col2] = currentCellInfo.finishRowInd;
                boolean currentCellHasBigRowspan = row != currentCellInfo.finishRowInd;
                if (cell.hasOwnOrModelProperty(27)) {
                    rowHasCellWithSetHeight = true;
                }
                float cellWidth = 0.0f;
                float colOffset = 0.0f;
                for (int k = col2; k < col2 + colspan; k++) {
                    cellWidth += this.countedColumnWidth[k];
                }
                for (int l = 0; l < col2; l++) {
                    colOffset += this.countedColumnWidth[l];
                }
                float rowspanOffset = 0.0f;
                for (int m = row - 1; m > currentCellInfo.finishRowInd - rowspan && m >= 0; m--) {
                    rowspanOffset += this.heights.get(m).floatValue();
                }
                float cellLayoutBoxHeight = rowspanOffset + ((!currentCellHasBigRowspan || hasContent) ? layoutBox.getHeight() : 0.0f);
                float cellLayoutBoxBottom = layoutBox.getY() + ((!currentCellHasBigRowspan || hasContent) ? 0.0f : layoutBox.getHeight());
                Rectangle cellLayoutBox = new Rectangle(layoutBox.getX() + colOffset, cellLayoutBoxBottom, cellWidth, cellLayoutBoxHeight);
                LayoutArea cellArea = new LayoutArea(layoutContext.getArea().getPageNumber(), cellLayoutBox);
                VerticalAlignment verticalAlignment = (VerticalAlignment) cell.getProperty(75);
                cell.setProperty(75, null);
                UnitValue cellWidthProperty = (UnitValue) cell.getProperty(77);
                if (cellWidthProperty != null && cellWidthProperty.isPercentValue()) {
                    cell.setProperty(77, UnitValue.createPointValue(cellWidth));
                }
                float[] cellIndents = this.bordersHandler.getCellBorderIndents(currentCellInfo.finishRowInd, col2, rowspan, colspan);
                if (!(this.bordersHandler instanceof SeparatedTableBorders)) {
                    this.bordersHandler.applyCellIndents(cellArea.getBBox(), cellIndents[0], cellIndents[1], cellIndents[2] + widestRowBottomBorderWidth, cellIndents[3], false);
                }
                float cellWidth2 = cellArea.getBBox().getWidth();
                LayoutTaggingHelper taggingHelper2 = (LayoutTaggingHelper) getProperty(108);
                if (taggingHelper2 != null) {
                    taggingHelper2.addKidsHint(this, Collections.singletonList(cell));
                    LayoutTaggingHelper.addTreeHints(taggingHelper2, cell);
                }
                LayoutResult cellResult = cell.setParent(this).layout(new LayoutContext(cellArea, null, childFloatRendererAreas, wasHeightClipped || wasParentsHeightClipped));
                cell.setProperty(75, verticalAlignment);
                if (cellResult.getStatus() != 3) {
                    cell.getOccupiedArea().getBBox().setWidth(cellWidth2);
                } else if (null == firstCauseOfNothing) {
                    firstCauseOfNothing = cellResult.getCauseOfNothing();
                }
                if (currentCellHasBigRowspan) {
                    if (cellResult.getStatus() != 1) {
                        splits[col2] = cellResult;
                        if (cellResult.getStatus() != 3) {
                            splits[col2].getOverflowRenderer().setProperty(75, VerticalAlignment.TOP);
                        }
                    }
                    if (cellResult.getStatus() == 2) {
                        currentRow[col2] = (CellRenderer) cellResult.getSplitRenderer();
                    } else {
                        this.rows.get(currentCellInfo.finishRowInd)[col2] = null;
                        currentRow[col2] = cell;
                        rowMoves.put(Integer.valueOf(col2), Integer.valueOf(currentCellInfo.finishRowInd));
                    }
                } else if (cellResult.getStatus() != 1) {
                    if (!split) {
                        boolean skipLastFooter = null != this.footerRenderer && tableModel.isSkipLastFooter() && tableModel.isComplete() && !Boolean.TRUE.equals(getOwnProperty(26));
                        if (skipLastFooter) {
                            LayoutArea potentialArea = new LayoutArea(area.getPageNumber(), layoutBox.mo825clone());
                            applySingleSpacing(potentialArea.getBBox(), horizontalBorderSpacing, true, true);
                            Border widestRowTopBorder = this.bordersHandler.getWidestHorizontalBorder(this.rowRange.getStartRow() + row);
                            if ((this.bordersHandler instanceof CollapsedTableBorders) && null != widestRowTopBorder) {
                                potentialArea.getBBox().increaseHeight(widestRowTopBorder.getWidth() / 2.0f);
                            }
                            if (null == this.headerRenderer) {
                                potentialArea.getBBox().increaseHeight(this.bordersHandler.getMaxTopWidth());
                            }
                            this.bordersHandler.applyLeftAndRightTableBorder(potentialArea.getBBox(), true);
                            float footerHeight2 = this.footerRenderer.getOccupiedArea().getBBox().getHeight();
                            potentialArea.getBBox().moveDown(footerHeight2 - (verticalBorderSpacing / 2.0f)).increaseHeight(footerHeight2);
                            TableRenderer overflowRenderer = createOverflowRenderer(new Table.RowRange(this.rowRange.getStartRow() + row, this.rowRange.getFinishRow()));
                            overflowRenderer.rows = this.rows.subList(row, this.rows.size());
                            overflowRenderer.setProperty(97, true);
                            overflowRenderer.setProperty(96, true);
                            overflowRenderer.setProperty(46, UnitValue.createPointValue(0.0f));
                            overflowRenderer.setProperty(43, UnitValue.createPointValue(0.0f));
                            overflowRenderer.setProperty(44, UnitValue.createPointValue(0.0f));
                            overflowRenderer.setProperty(45, UnitValue.createPointValue(0.0f));
                            if (null != this.headerRenderer) {
                                overflowRenderer.setProperty(13, Border.NO_BORDER);
                            }
                            overflowRenderer.bordersHandler = this.bordersHandler;
                            this.bordersHandler.skipFooter(overflowRenderer.getBorders());
                            if (null != this.headerRenderer) {
                                this.bordersHandler.skipHeader(overflowRenderer.getBorders());
                            }
                            int savedStartRow = overflowRenderer.bordersHandler.startRow;
                            overflowRenderer.bordersHandler.setStartRow(row);
                            prepareFooterOrHeaderRendererForLayout(overflowRenderer, potentialArea.getBBox().getWidth());
                            LayoutResult res = overflowRenderer.layout(new LayoutContext(potentialArea, wasHeightClipped || wasParentsHeightClipped));
                            this.bordersHandler.setStartRow(savedStartRow);
                            if (1 == res.getStatus()) {
                                if (taggingHelper2 != null) {
                                    taggingHelper2.markArtifactHint(this.footerRenderer);
                                }
                                this.footerRenderer = null;
                                layoutBox.increaseHeight(footerHeight2).moveDown(footerHeight2);
                                deleteOwnProperty(10);
                                this.bordersHandler.setFinishRow(this.rowRange.getStartRow() + row);
                                Border widestRowBottomBorder2 = this.bordersHandler.getWidestHorizontalBorder(this.rowRange.getStartRow() + row + 1);
                                this.bordersHandler.setFinishRow(this.rowRange.getFinishRow());
                                widestRowBottomBorderWidth = null == widestRowBottomBorder2 ? 0.0f : widestRowBottomBorder2.getWidth();
                                cellProcessingQueue.clear();
                                currChildRenderers.clear();
                                for (int addCol = 0; addCol < currentRow.length; addCol++) {
                                    if (currentRow[addCol] != null) {
                                        cellProcessingQueue.addLast(new CellRendererInfo(currentRow[addCol], addCol, row));
                                    }
                                }
                            } else {
                                if (null != this.headerRenderer) {
                                    this.bordersHandler.collapseTableWithHeader(this.headerRenderer.bordersHandler, false);
                                }
                                this.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, false);
                                this.bordersHandler.tableBoundingBorders[2] = Border.NO_BORDER;
                            }
                        }
                        for (int addCol2 = 0; addCol2 < currentRow.length; addCol2++) {
                            if (currentRow[addCol2] == null) {
                                int addRow = row + 1;
                                while (true) {
                                    if (addRow >= this.rows.size()) {
                                        break;
                                    }
                                    if (this.rows.get(addRow)[addCol2] == null) {
                                        addRow++;
                                    } else {
                                        CellRenderer addRenderer = this.rows.get(addRow)[addCol2];
                                        if ((row + addRenderer.getPropertyAsInteger(60).intValue()) - 1 >= addRow) {
                                            cellProcessingQueue.addLast(new CellRendererInfo(addRenderer, addCol2, addRow));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    split = true;
                    splits[col2] = cellResult;
                    if (cellResult.getStatus() == 3) {
                        hasContent = false;
                        splits[col2].getOverflowRenderer().setProperty(75, verticalAlignment);
                    }
                }
                currChildRenderers.add(cell);
                if (cellResult.getStatus() != 3) {
                    rowHeight = Math.max(rowHeight, (cellResult.getOccupiedArea().getBBox().getHeight() + this.bordersHandler.getCellVerticalAddition(cellIndents)) - rowspanOffset);
                }
            }
            if (hasContent) {
                this.heights.add(Float.valueOf(rowHeight));
                rowsHasCellWithSetHeight.add(Boolean.valueOf(rowHasCellWithSetHeight));
                this.occupiedArea.getBBox().moveDown(rowHeight);
                this.occupiedArea.getBBox().increaseHeight(rowHeight);
                layoutBox.decreaseHeight(rowHeight);
            }
            if (split || row == this.rows.size() - 1) {
                this.bordersHandler.setFinishRow(this.bordersHandler.getStartRow() + row);
                if (!hasContent && this.bordersHandler.getFinishRow() != this.bordersHandler.getStartRow()) {
                    this.bordersHandler.setFinishRow(this.bordersHandler.getFinishRow() - 1);
                }
                boolean skip = false;
                if (null != this.footerRenderer && tableModel.isComplete() && tableModel.isSkipLastFooter() && !split && !Boolean.TRUE.equals(getOwnProperty(26))) {
                    LayoutTaggingHelper taggingHelper3 = (LayoutTaggingHelper) getProperty(108);
                    if (taggingHelper3 != null) {
                        taggingHelper3.markArtifactHint(this.footerRenderer);
                    }
                    this.footerRenderer = null;
                    if (tableModel.isEmpty()) {
                        deleteOwnProperty(13);
                    }
                    skip = true;
                }
                correctLayoutedCellsOccupiedAreas(splits, row, targetOverflowRowIndex, blockMinHeight, layoutBox, rowsHasCellWithSetHeight, !split, !hasContent && cellWithBigRowspanAdded, skip);
            }
            if ((split || row == this.rows.size() - 1) && null != this.footerRenderer) {
                if (!hasContent && this.childRenderers.size() == 0) {
                    this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, true);
                } else {
                    this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, tableModel.isEmpty(), false, true);
                }
                if (!(this.bordersHandler instanceof SeparatedTableBorders)) {
                    layoutBox.moveDown(this.footerRenderer.occupiedArea.getBBox().getHeight()).increaseHeight(this.footerRenderer.occupiedArea.getBBox().getHeight());
                    this.bordersHandler.applyLeftAndRightTableBorder(layoutBox, true);
                    prepareFooterOrHeaderRendererForLayout(this.footerRenderer, layoutBox.getWidth());
                    this.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, hasContent || 0 != this.childRenderers.size());
                    if (this.bordersHandler instanceof CollapsedTableBorders) {
                        this.footerRenderer.setBorders(CollapsedTableBorders.getCollapsedBorder(this.footerRenderer.getBorders()[2], getBorders()[2]), 2);
                    }
                    this.footerRenderer.layout(new LayoutContext(new LayoutArea(area.getPageNumber(), layoutBox), wasHeightClipped || wasParentsHeightClipped));
                    this.bordersHandler.applyLeftAndRightTableBorder(layoutBox, false);
                    this.footerRenderer.move(0.0f, -(layoutBox.getHeight() - this.footerRenderer.getOccupiedAreaBBox().getHeight()));
                    layoutBox.setY(this.footerRenderer.occupiedArea.getBBox().getTop()).setHeight(this.occupiedArea.getBBox().getBottom() - layoutBox.getBottom());
                }
            }
            if (!split) {
                this.childRenderers.addAll(currChildRenderers);
                currChildRenderers.clear();
            }
            if (split && this.footerRenderer != null && (taggingHelper = (LayoutTaggingHelper) getProperty(108)) != null) {
                taggingHelper.markArtifactHint(this.footerRenderer);
            }
            if (!split) {
                row++;
            } else {
                if (marginsCollapsingEnabled) {
                    marginsCollapseHandler.endMarginsCollapse(layoutBox);
                }
                TableRenderer[] splitResult = split(row, hasContent, cellWithBigRowspanAdded);
                OverflowRowsWrapper overflowRows = new OverflowRowsWrapper(splitResult[1]);
                if (null != this.headerRenderer || null != this.footerRenderer) {
                    if (null != this.headerRenderer || tableModel.isEmpty()) {
                        splitResult[1].deleteOwnProperty(13);
                    }
                    if (null != this.footerRenderer || tableModel.isEmpty()) {
                        splitResult[1].deleteOwnProperty(10);
                    }
                }
                if (split) {
                    int[] rowspans = new int[currentRow.length];
                    boolean[] columnsWithCellToBeEnlarged = new boolean[currentRow.length];
                    for (int col3 = 0; col3 < currentRow.length; col3++) {
                        if (splits[col3] != null) {
                            CellRenderer cellSplit = (CellRenderer) splits[col3].getSplitRenderer();
                            if (null != cellSplit) {
                                rowspans[col3] = ((Cell) cellSplit.getModelElement()).getRowspan();
                            }
                            if (splits[col3].getStatus() != 3 && (hasContent || cellWithBigRowspanAdded)) {
                                this.childRenderers.add(cellSplit);
                            }
                            LayoutArea cellOccupiedArea = currentRow[col3].getOccupiedArea();
                            if (hasContent || cellWithBigRowspanAdded || splits[col3].getStatus() == 3) {
                                CellRenderer cellOverflow = (CellRenderer) splits[col3].getOverflowRenderer();
                                CellRenderer originalCell = currentRow[col3];
                                currentRow[col3] = null;
                                this.rows.get(targetOverflowRowIndex[col3])[col3] = originalCell;
                                overflowRows.setCell(0, col3, null);
                                overflowRows.setCell(targetOverflowRowIndex[col3] - row, col3, (CellRenderer) cellOverflow.setParent(splitResult[1]));
                            } else {
                                overflowRows.setCell(targetOverflowRowIndex[col3] - row, col3, (CellRenderer) currentRow[col3].setParent(splitResult[1]));
                            }
                            overflowRows.getCell(targetOverflowRowIndex[col3] - row, col3).occupiedArea = cellOccupiedArea;
                        } else if (currentRow[col3] != null) {
                            if (hasContent) {
                                rowspans[col3] = ((Cell) currentRow[col3].getModelElement()).getRowspan();
                            }
                            boolean isBigRowspannedCell = 1 != ((Cell) currentRow[col3].getModelElement()).getRowspan();
                            if (hasContent || isBigRowspannedCell) {
                                columnsWithCellToBeEnlarged[col3] = true;
                            }
                        }
                    }
                    int minRowspan = Integer.MAX_VALUE;
                    for (int col4 = 0; col4 < rowspans.length; col4++) {
                        if (0 != rowspans[col4]) {
                            minRowspan = Math.min(minRowspan, rowspans[col4]);
                        }
                    }
                    for (int col5 = 0; col5 < numberOfColumns; col5++) {
                        if (columnsWithCellToBeEnlarged[col5]) {
                            enlargeCell(col5, row, minRowspan, currentRow, overflowRows, targetOverflowRowIndex, splitResult);
                        }
                    }
                }
                applySpacing(layoutBox, horizontalBorderSpacing, verticalBorderSpacing, true);
                applySingleSpacing(this.occupiedArea.getBBox(), horizontalBorderSpacing, true, true);
                if (null != this.footerRenderer) {
                    layoutBox.moveUp(verticalBorderSpacing).decreaseHeight(verticalBorderSpacing);
                }
                if (null != this.headerRenderer || !tableModel.isEmpty()) {
                    layoutBox.decreaseHeight(verticalBorderSpacing);
                }
                if (0 == row && !hasContent && null == this.headerRenderer) {
                    this.occupiedArea.getBBox().moveUp(verticalBorderSpacing / 2.0f);
                } else {
                    applySingleSpacing(this.occupiedArea.getBBox(), verticalBorderSpacing, false, true);
                }
                if (!isAndWasComplete && null != this.footerRenderer && 0 == splitResult[0].rows.size()) {
                    layoutBox.increaseHeight(verticalBorderSpacing);
                }
                if (null == this.footerRenderer) {
                    if (0 != this.childRenderers.size()) {
                        this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, false);
                    } else {
                        this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, true);
                        if (!isAndWasComplete && !isFirstOnThePage) {
                            this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), true, false);
                        }
                    }
                }
                if (Boolean.TRUE.equals(getPropertyAsBoolean(86)) || Boolean.TRUE.equals(getPropertyAsBoolean(87))) {
                    extendLastRow(splitResult[1].rows.get(0), layoutBox);
                }
                adjustFooterAndFixOccupiedArea(layoutBox, 0 != this.heights.size() ? verticalBorderSpacing : 0.0f);
                adjustCaptionAndFixOccupiedArea(layoutBox, 0 != this.heights.size() ? verticalBorderSpacing : 0.0f);
                for (Map.Entry<Integer, Integer> entry : rowMoves.entrySet()) {
                    if (null == splitResult[1].rows.get(entry.getValue().intValue() - splitResult[0].rows.size())[entry.getKey().intValue()]) {
                        CellRenderer originalCellRenderer = this.rows.get(row)[entry.getKey().intValue()];
                        CellRenderer overflowCellRenderer = splitResult[1].rows.get(row - splitResult[0].rows.size())[entry.getKey().intValue()];
                        this.rows.get(entry.getValue().intValue())[entry.getKey().intValue()] = originalCellRenderer;
                        this.rows.get(row)[entry.getKey().intValue()] = null;
                        overflowRows.setCell(entry.getValue().intValue() - splitResult[0].rows.size(), entry.getKey().intValue(), overflowCellRenderer);
                        overflowRows.setCell(row - splitResult[0].rows.size(), entry.getKey().intValue(), null);
                    }
                }
                if (isKeepTogether() && 0 == lastFlushedRowBottomBorder.size() && !Boolean.TRUE.equals(getPropertyAsBoolean(26))) {
                    return new LayoutResult(3, null, null, this, null == firstCauseOfNothing ? this : firstCauseOfNothing);
                }
                int status = ((this.occupiedArea.getBBox().getHeight() - (null == this.footerRenderer ? 0.0f : this.footerRenderer.getOccupiedArea().getBBox().getHeight())) - (null == this.headerRenderer ? 0.0f : this.headerRenderer.getOccupiedArea().getBBox().getHeight() - this.headerRenderer.bordersHandler.getMaxBottomWidth()) == 0.0f && (isAndWasComplete || isFirstOnThePage)) ? 3 : 2;
                if ((status == 3 && Boolean.TRUE.equals(getPropertyAsBoolean(26))) || wasHeightClipped) {
                    if (wasHeightClipped) {
                        Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                        logger.warn(LogMessageConstant.CLIP_ELEMENT);
                        if (status == 3) {
                            this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), true, false);
                            this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), true, false);
                        }
                        if (null != blockMinHeight && blockMinHeight.floatValue() > this.occupiedArea.getBBox().getHeight()) {
                            float blockBottom = Math.max(this.occupiedArea.getBBox().getBottom() - (blockMinHeight.floatValue() - this.occupiedArea.getBBox().getHeight()), layoutBox.getBottom());
                            if (0 == this.heights.size()) {
                                this.heights.add(Float.valueOf(blockMinHeight.floatValue() - (this.occupiedArea.getBBox().getHeight() / 2.0f)));
                            } else {
                                this.heights.set(this.heights.size() - 1, Float.valueOf((this.heights.get(this.heights.size() - 1).floatValue() + blockMinHeight.floatValue()) - this.occupiedArea.getBBox().getHeight()));
                            }
                            this.occupiedArea.getBBox().increaseHeight(this.occupiedArea.getBBox().getBottom() - blockBottom).setY(blockBottom);
                        }
                    }
                    applyFixedXOrYPosition(false, layoutBox);
                    applyPaddings(this.occupiedArea.getBBox(), true);
                    applyMargins(this.occupiedArea.getBBox(), true);
                    LayoutArea editedArea = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, siblingFloatRendererAreas, layoutContext.getArea().getBBox(), clearHeightCorrection, marginsCollapsingEnabled);
                    return new LayoutResult(1, editedArea, splitResult[0], null);
                }
                updateHeightsOnSplit(false, splitResult[0], splitResult[1]);
                applyFixedXOrYPosition(false, layoutBox);
                applyPaddings(this.occupiedArea.getBBox(), true);
                applyMargins(this.occupiedArea.getBBox(), true);
                LayoutArea editedArea2 = null;
                if (status != 3) {
                    editedArea2 = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, siblingFloatRendererAreas, layoutContext.getArea().getBBox(), clearHeightCorrection, marginsCollapsingEnabled);
                }
                return new LayoutResult(status, editedArea2, splitResult[0], splitResult[1], null == firstCauseOfNothing ? this : firstCauseOfNothing);
            }
        }
        if (tableModel.isComplete() && !tableModel.isEmpty()) {
            CellRenderer[] lastRow = this.rows.get(this.rows.size() - 1);
            int lastInRow = lastRow.length - 1;
            while (lastInRow >= 0 && null == lastRow[lastInRow]) {
                lastInRow--;
            }
            if (lastInRow < 0 || lastRow.length != lastInRow + lastRow[lastInRow].getPropertyAsInteger(16).intValue()) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                logger2.warn(LogMessageConstant.LAST_ROW_IS_NOT_COMPLETE);
            }
        }
        if (!(this.bordersHandler instanceof SeparatedTableBorders) && tableModel.isComplete() && ((0 != lastFlushedRowBottomBorder.size() || tableModel.isEmpty()) && null != this.footerRenderer)) {
            layoutBox.moveDown(this.footerRenderer.occupiedArea.getBBox().getHeight()).increaseHeight(this.footerRenderer.occupiedArea.getBBox().getHeight());
            this.bordersHandler.applyLeftAndRightTableBorder(layoutBox, true);
            prepareFooterOrHeaderRendererForLayout(this.footerRenderer, layoutBox.getWidth());
            if (0 != this.rows.size() || !isAndWasComplete) {
                this.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, true);
            } else if (null != this.headerRenderer) {
                this.headerRenderer.bordersHandler.collapseTableWithFooter(this.footerRenderer.bordersHandler, true);
            }
            this.footerRenderer.layout(new LayoutContext(new LayoutArea(area.getPageNumber(), layoutBox), wasHeightClipped || wasParentsHeightClipped));
            this.bordersHandler.applyLeftAndRightTableBorder(layoutBox, false);
            float footerHeight3 = this.footerRenderer.getOccupiedAreaBBox().getHeight();
            this.footerRenderer.move(0.0f, -(layoutBox.getHeight() - footerHeight3));
            layoutBox.moveUp(footerHeight3).decreaseHeight(footerHeight3);
        }
        applySpacing(layoutBox, horizontalBorderSpacing, verticalBorderSpacing, true);
        applySingleSpacing(this.occupiedArea.getBBox(), horizontalBorderSpacing, true, true);
        if (null != this.footerRenderer) {
            layoutBox.moveUp(verticalBorderSpacing).decreaseHeight(verticalBorderSpacing);
        }
        if (null != this.headerRenderer || !tableModel.isEmpty()) {
            layoutBox.decreaseHeight(verticalBorderSpacing);
        }
        if (tableModel.isEmpty() && null == this.headerRenderer) {
            this.occupiedArea.getBBox().moveUp(verticalBorderSpacing / 2.0f);
        } else if (isAndWasComplete || 0 != this.rows.size()) {
            applySingleSpacing(this.occupiedArea.getBBox(), verticalBorderSpacing, false, true);
        }
        float bottomTableBorderWidth = this.bordersHandler.getMaxBottomWidth();
        if (tableModel.isComplete()) {
            if (null == this.footerRenderer) {
                if (0 != this.childRenderers.size()) {
                    this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, false);
                } else if (0 != lastFlushedRowBottomBorder.size()) {
                    this.bordersHandler.applyTopTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), true, false);
                } else {
                    this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), true, false);
                }
            } else if (tableModel.isEmpty() && null != this.headerRenderer) {
                float headerBottomBorderWidth = this.headerRenderer.bordersHandler.getMaxBottomWidth();
                this.headerRenderer.bordersHandler.applyBottomTableBorder(this.headerRenderer.occupiedArea.getBBox(), layoutBox, true, true, true);
                this.occupiedArea.getBBox().moveUp(headerBottomBorderWidth).decreaseHeight(headerBottomBorderWidth);
            }
        } else {
            if (0 != this.heights.size()) {
                this.heights.set(this.heights.size() - 1, Float.valueOf(this.heights.get(this.heights.size() - 1).floatValue() - (bottomTableBorderWidth / 2.0f)));
            }
            if (null == this.footerRenderer) {
                if (0 != this.childRenderers.size()) {
                    this.bordersHandler.applyBottomTableBorder(this.occupiedArea.getBBox(), layoutBox, 0 == this.childRenderers.size(), false, true);
                }
            } else {
                layoutBox.increaseHeight(bottomTableBorderWidth);
            }
        }
        if (0 != this.rows.size()) {
            if (Boolean.TRUE.equals(getPropertyAsBoolean(86))) {
                extendLastRow(this.rows.get(this.rows.size() - 1), layoutBox);
            }
        } else if (null != blockMinHeight && blockMinHeight.floatValue() > this.occupiedArea.getBBox().getHeight()) {
            float blockBottom2 = Math.max(this.occupiedArea.getBBox().getBottom() - (blockMinHeight.floatValue() - this.occupiedArea.getBBox().getHeight()), layoutBox.getBottom());
            if (0 != this.heights.size()) {
                this.heights.set(this.heights.size() - 1, Float.valueOf((this.heights.get(this.heights.size() - 1).floatValue() + this.occupiedArea.getBBox().getBottom()) - blockBottom2));
            } else {
                this.heights.add(Float.valueOf((this.occupiedArea.getBBox().getBottom() - blockBottom2) + (this.occupiedArea.getBBox().getHeight() / 2.0f)));
            }
            this.occupiedArea.getBBox().increaseHeight(this.occupiedArea.getBBox().getBottom() - blockBottom2).setY(blockBottom2);
        }
        applyFixedXOrYPosition(false, layoutBox);
        if (marginsCollapsingEnabled) {
            marginsCollapseHandler.endMarginsCollapse(layoutBox);
        }
        applyPaddings(this.occupiedArea.getBBox(), true);
        applyMargins(this.occupiedArea.getBBox(), true);
        if (!tableModel.isComplete() && null != this.footerRenderer) {
            LayoutTaggingHelper taggingHelper4 = (LayoutTaggingHelper) getProperty(108);
            if (taggingHelper4 != null) {
                taggingHelper4.markArtifactHint(this.footerRenderer);
            }
            this.footerRenderer = null;
            this.bordersHandler.skipFooter(this.bordersHandler.tableBoundingBorders);
        }
        adjustFooterAndFixOccupiedArea(layoutBox, (null == this.headerRenderer && tableModel.isEmpty()) ? 0.0f : verticalBorderSpacing);
        adjustCaptionAndFixOccupiedArea(layoutBox, (null == this.headerRenderer && tableModel.isEmpty()) ? 0.0f : verticalBorderSpacing);
        FloatingHelper.removeFloatsAboveRendererBottom(siblingFloatRendererAreas, this);
        if (!isAndWasComplete && !isFirstOnThePage && (0 != this.rows.size() || (null != this.footerRenderer && tableModel.isComplete()))) {
            this.occupiedArea.getBBox().decreaseHeight(verticalBorderSpacing);
        }
        LayoutArea editedArea3 = FloatingHelper.adjustResultOccupiedAreaForFloatAndClear(this, siblingFloatRendererAreas, layoutContext.getArea().getBBox(), clearHeightCorrection, marginsCollapsingEnabled);
        return new LayoutResult(1, editedArea3, null, null, null);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void draw(DrawContext drawContext) {
        boolean isTagged = drawContext.isTaggingEnabled();
        LayoutTaggingHelper taggingHelper = null;
        if (isTagged) {
            taggingHelper = (LayoutTaggingHelper) getProperty(108);
            if (taggingHelper == null) {
                isTagged = false;
            } else {
                TagTreePointer tagPointer = taggingHelper.useAutoTaggingPointerAndRememberItsPosition(this);
                if (taggingHelper.createTag(this, tagPointer)) {
                    tagPointer.getProperties().addAttributes(0, AccessibleAttributesApplier.getLayoutAttributes(this, tagPointer));
                }
            }
        }
        beginTransformationIfApplied(drawContext.getCanvas());
        applyDestinationsAndAnnotation(drawContext);
        boolean relativePosition = isRelativePosition();
        if (relativePosition) {
            applyRelativePositioningTranslation(false);
        }
        beginElementOpacityApplying(drawContext);
        float captionHeight = null != this.captionRenderer ? this.captionRenderer.getOccupiedArea().getBBox().getHeight() : 0.0f;
        boolean isBottomCaption = CaptionSide.BOTTOM.equals(0.0f != captionHeight ? (CaptionSide) this.captionRenderer.getProperty(119) : null);
        if (0.0f != captionHeight) {
            this.occupiedArea.getBBox().applyMargins(isBottomCaption ? 0.0f : captionHeight, 0.0f, isBottomCaption ? captionHeight : 0.0f, 0.0f, false);
        }
        drawBackground(drawContext);
        if ((this.bordersHandler instanceof SeparatedTableBorders) && !isHeaderRenderer() && !isFooterRenderer()) {
            drawBorder(drawContext);
        }
        drawChildren(drawContext);
        drawPositionedChildren(drawContext);
        if (0.0f != captionHeight) {
            this.occupiedArea.getBBox().applyMargins(isBottomCaption ? 0.0f : captionHeight, 0.0f, isBottomCaption ? captionHeight : 0.0f, 0.0f, true);
        }
        drawCaption(drawContext);
        endElementOpacityApplying(drawContext);
        if (relativePosition) {
            applyRelativePositioningTranslation(true);
        }
        this.flushed = true;
        endTransformationIfApplied(drawContext.getCanvas());
        if (isTagged) {
            if (this.isLastRendererForModelElement && ((Table) getModelElement()).isComplete()) {
                taggingHelper.finishTaggingHint(this);
            }
            taggingHelper.restoreAutoTaggingPointerPosition(this);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawChildren(DrawContext drawContext) {
        if (this.headerRenderer != null) {
            this.headerRenderer.draw(drawContext);
        }
        for (IRenderer child : this.childRenderers) {
            child.draw(drawContext);
        }
        if (this.bordersHandler instanceof CollapsedTableBorders) {
            drawBorders(drawContext);
        }
        if (this.footerRenderer != null) {
            this.footerRenderer.draw(drawContext);
        }
    }

    protected void drawBackgrounds(DrawContext drawContext) {
        boolean shrinkBackgroundArea = (this.bordersHandler instanceof CollapsedTableBorders) && (isHeaderRenderer() || isFooterRenderer());
        if (shrinkBackgroundArea) {
            this.occupiedArea.getBBox().applyMargins(this.bordersHandler.getMaxTopWidth() / 2.0f, this.bordersHandler.getRightBorderMaxWidth() / 2.0f, this.bordersHandler.getMaxBottomWidth() / 2.0f, this.bordersHandler.getLeftBorderMaxWidth() / 2.0f, false);
        }
        super.drawBackground(drawContext);
        if (shrinkBackgroundArea) {
            this.occupiedArea.getBBox().applyMargins(this.bordersHandler.getMaxTopWidth() / 2.0f, this.bordersHandler.getRightBorderMaxWidth() / 2.0f, this.bordersHandler.getMaxBottomWidth() / 2.0f, this.bordersHandler.getLeftBorderMaxWidth() / 2.0f, true);
        }
        if (null != this.headerRenderer) {
            this.headerRenderer.drawBackgrounds(drawContext);
        }
        if (null != this.footerRenderer) {
            this.footerRenderer.drawBackgrounds(drawContext);
        }
    }

    protected void drawCaption(DrawContext drawContext) {
        if (null != this.captionRenderer && !isFooterRenderer() && !isHeaderRenderer()) {
            this.captionRenderer.draw(drawContext);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawBackground(DrawContext drawContext) {
        if (!isFooterRenderer() && !isHeaderRenderer()) {
            drawBackgrounds(drawContext);
        }
    }

    @Override // com.itextpdf.layout.renderer.IRenderer
    public IRenderer getNextRenderer() {
        TableRenderer nextTable = new TableRenderer();
        nextTable.modelElement = this.modelElement;
        return nextTable;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer, com.itextpdf.layout.renderer.IRenderer
    public void move(float dxRight, float dyUp) {
        super.move(dxRight, dyUp);
        if (this.headerRenderer != null) {
            this.headerRenderer.move(dxRight, dyUp);
        }
        if (this.footerRenderer != null) {
            this.footerRenderer.move(dxRight, dyUp);
        }
    }

    protected TableRenderer[] split(int row) {
        return split(row, false);
    }

    protected TableRenderer[] split(int row, boolean hasContent) {
        return split(row, hasContent, false);
    }

    protected TableRenderer[] split(int row, boolean hasContent, boolean cellWithBigRowspanAdded) {
        TableRenderer splitRenderer = createSplitRenderer(new Table.RowRange(this.rowRange.getStartRow(), this.rowRange.getStartRow() + row));
        splitRenderer.rows = this.rows.subList(0, row);
        splitRenderer.bordersHandler = this.bordersHandler;
        splitRenderer.heights = this.heights;
        splitRenderer.columnWidths = this.columnWidths;
        splitRenderer.countedColumnWidth = this.countedColumnWidth;
        splitRenderer.totalWidthForColumns = this.totalWidthForColumns;
        TableRenderer overflowRenderer = createOverflowRenderer(new Table.RowRange(this.rowRange.getStartRow() + row, this.rowRange.getFinishRow()));
        if (0 == row && !hasContent && !cellWithBigRowspanAdded && 0 == this.rowRange.getStartRow()) {
            overflowRenderer.isOriginalNonSplitRenderer = this.isOriginalNonSplitRenderer;
        }
        overflowRenderer.rows = this.rows.subList(row, this.rows.size());
        splitRenderer.occupiedArea = this.occupiedArea;
        overflowRenderer.bordersHandler = this.bordersHandler;
        return new TableRenderer[]{splitRenderer, overflowRenderer};
    }

    protected TableRenderer createSplitRenderer(Table.RowRange rowRange) {
        TableRenderer splitRenderer = (TableRenderer) getNextRenderer();
        splitRenderer.rowRange = rowRange;
        splitRenderer.parent = this.parent;
        splitRenderer.modelElement = this.modelElement;
        splitRenderer.childRenderers = this.childRenderers;
        splitRenderer.addAllProperties(getOwnProperties());
        splitRenderer.headerRenderer = this.headerRenderer;
        splitRenderer.footerRenderer = this.footerRenderer;
        splitRenderer.isLastRendererForModelElement = false;
        splitRenderer.topBorderMaxWidth = this.topBorderMaxWidth;
        splitRenderer.captionRenderer = this.captionRenderer;
        splitRenderer.isOriginalNonSplitRenderer = this.isOriginalNonSplitRenderer;
        return splitRenderer;
    }

    protected TableRenderer createOverflowRenderer(Table.RowRange rowRange) {
        TableRenderer overflowRenderer = (TableRenderer) getNextRenderer();
        overflowRenderer.setRowRange(rowRange);
        overflowRenderer.parent = this.parent;
        overflowRenderer.modelElement = this.modelElement;
        overflowRenderer.addAllProperties(getOwnProperties());
        overflowRenderer.isOriginalNonSplitRenderer = false;
        overflowRenderer.countedColumnWidth = this.countedColumnWidth;
        return overflowRenderer;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected Float retrieveWidth(float parentBoxWidth) {
        Float tableWidth = super.retrieveWidth(parentBoxWidth);
        Table tableModel = (Table) getModelElement();
        if (tableWidth == null || tableWidth.floatValue() == 0.0f) {
            float totalColumnWidthInPercent = 0.0f;
            for (int col = 0; col < tableModel.getNumberOfColumns(); col++) {
                UnitValue columnWidth = tableModel.getColumnWidth(col);
                if (columnWidth.isPercentValue()) {
                    totalColumnWidthInPercent += columnWidth.getValue();
                }
            }
            tableWidth = Float.valueOf(parentBoxWidth);
            if (totalColumnWidthInPercent > 0.0f) {
                tableWidth = Float.valueOf((parentBoxWidth * totalColumnWidthInPercent) / 100.0f);
            }
        }
        return tableWidth;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public MinMaxWidth getMinMaxWidth() {
        if (this.isOriginalNonSplitRenderer) {
            initializeTableLayoutBorders();
        }
        float rightMaxBorder = this.bordersHandler.getRightBorderMaxWidth();
        float leftMaxBorder = this.bordersHandler.getLeftBorderMaxWidth();
        TableWidths tableWidths = new TableWidths(this, MinMaxWidthUtils.getInfWidth(), true, rightMaxBorder, leftMaxBorder);
        float maxColTotalWidth = 0.0f;
        float[] columns = this.isOriginalNonSplitRenderer ? tableWidths.layout() : this.countedColumnWidth;
        for (float column : columns) {
            maxColTotalWidth += column;
        }
        float minWidth = this.isOriginalNonSplitRenderer ? tableWidths.getMinWidth() : maxColTotalWidth;
        UnitValue marginRightUV = getPropertyAsUnitValue(45);
        if (!marginRightUV.isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
        }
        UnitValue marginLefttUV = getPropertyAsUnitValue(44);
        if (!marginLefttUV.isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        float additionalWidth = marginLefttUV.getValue() + marginRightUV.getValue() + (rightMaxBorder / 2.0f) + (leftMaxBorder / 2.0f);
        return new MinMaxWidth(minWidth, maxColTotalWidth, additionalWidth);
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    @Deprecated
    protected Float getLastYLineRecursively() {
        return null;
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    protected boolean allowLastYLineRecursiveExtraction() {
        return false;
    }

    private void initializeTableLayoutBorders() {
        TableBorders collapsedTableBorders;
        boolean isSeparated = BorderCollapsePropertyValue.SEPARATE.equals(getProperty(114));
        if (isSeparated) {
            collapsedTableBorders = new SeparatedTableBorders(this.rows, ((Table) getModelElement()).getNumberOfColumns(), getBorders());
        } else {
            collapsedTableBorders = new CollapsedTableBorders(this.rows, ((Table) getModelElement()).getNumberOfColumns(), getBorders());
        }
        this.bordersHandler = collapsedTableBorders;
        this.bordersHandler.initializeBorders();
        this.bordersHandler.setTableBoundingBorders(getBorders());
        this.bordersHandler.setRowRange(this.rowRange.getStartRow(), this.rowRange.getFinishRow());
        initializeHeaderAndFooter(true);
        this.bordersHandler.updateBordersOnNewPage(this.isOriginalNonSplitRenderer, isFooterRenderer() || isHeaderRenderer(), this, this.headerRenderer, this.footerRenderer);
        correctRowRange();
    }

    private void correctRowRange() {
        if (this.rows.size() < (this.rowRange.getFinishRow() - this.rowRange.getStartRow()) + 1) {
            this.rowRange = new Table.RowRange(this.rowRange.getStartRow(), (this.rowRange.getStartRow() + this.rows.size()) - 1);
        }
    }

    @Override // com.itextpdf.layout.renderer.AbstractRenderer
    public void drawBorder(DrawContext drawContext) {
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            super.drawBorder(drawContext);
        }
    }

    protected void drawBorders(DrawContext drawContext) {
        drawBorders(drawContext, null != this.headerRenderer, null != this.footerRenderer);
    }

    private void drawBorders(DrawContext drawContext, boolean hasHeader, boolean hasFooter) {
        float startY;
        float height = this.occupiedArea.getBBox().getHeight();
        if (null != this.footerRenderer) {
            height -= this.footerRenderer.occupiedArea.getBBox().getHeight();
        }
        if (null != this.headerRenderer) {
            height -= this.headerRenderer.occupiedArea.getBBox().getHeight();
        }
        if (height < 1.0E-4f) {
            return;
        }
        float startX = getOccupiedArea().getBBox().getX() + (this.bordersHandler.getLeftBorderMaxWidth() / 2.0f);
        float startY2 = getOccupiedArea().getBBox().getY() + getOccupiedArea().getBBox().getHeight();
        if (null != this.headerRenderer) {
            startY = (startY2 - this.headerRenderer.occupiedArea.getBBox().getHeight()) + (this.topBorderMaxWidth / 2.0f);
        } else {
            startY = startY2 - (this.topBorderMaxWidth / 2.0f);
        }
        if (hasProperty(46)) {
            UnitValue topMargin = getPropertyAsUnitValue(46);
            if (null != topMargin && !topMargin.isPointValue()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
            }
            startY -= null == topMargin ? 0.0f : topMargin.getValue();
        }
        if (hasProperty(44)) {
            UnitValue leftMargin = getPropertyAsUnitValue(44);
            if (null != leftMargin && !leftMargin.isPointValue()) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
            }
            startX += null == leftMargin ? 0.0f : leftMargin.getValue();
        }
        if (this.childRenderers.size() == 0) {
            Border[] borders = this.bordersHandler.tableBoundingBorders;
            if (null != borders[0]) {
                if (null != borders[2] && 0 == this.heights.size()) {
                    this.heights.add(0, Float.valueOf((borders[0].getWidth() / 2.0f) + (borders[2].getWidth() / 2.0f)));
                }
            } else if (null != borders[2]) {
                startY -= borders[2].getWidth() / 2.0f;
            }
            if (0 == this.heights.size()) {
                this.heights.add(Float.valueOf(0.0f));
            }
        }
        boolean isTagged = drawContext.isTaggingEnabled();
        if (isTagged) {
            drawContext.getCanvas().openTag(new CanvasArtifact());
        }
        boolean isTopTablePart = isTopTablePart();
        boolean isBottomTablePart = isBottomTablePart();
        boolean isComplete = getTable().isComplete();
        boolean isFooterRendererOfLargeTable = isFooterRendererOfLargeTable();
        this.bordersHandler.setRowRange(this.rowRange.getStartRow(), (this.rowRange.getStartRow() + this.heights.size()) - 1);
        if (this.bordersHandler instanceof CollapsedTableBorders) {
            if (hasFooter) {
                ((CollapsedTableBorders) this.bordersHandler).setBottomBorderCollapseWith(this.footerRenderer.bordersHandler.getFirstHorizontalBorder());
            } else if (isBottomTablePart) {
                ((CollapsedTableBorders) this.bordersHandler).setBottomBorderCollapseWith(null);
            }
        }
        float y1 = startY;
        if (isFooterRendererOfLargeTable) {
            this.bordersHandler.drawHorizontalBorder(0, startX, y1, drawContext.getCanvas(), this.countedColumnWidth);
        }
        if (0 != this.heights.size()) {
            y1 -= this.heights.get(0).floatValue();
        }
        for (int i = 1; i < this.heights.size(); i++) {
            this.bordersHandler.drawHorizontalBorder(i, startX, y1, drawContext.getCanvas(), this.countedColumnWidth);
            if (i < this.heights.size()) {
                y1 -= this.heights.get(i).floatValue();
            }
        }
        if (!isBottomTablePart && isComplete) {
            this.bordersHandler.drawHorizontalBorder(this.heights.size(), startX, y1, drawContext.getCanvas(), this.countedColumnWidth);
        }
        float x1 = startX;
        if (this.countedColumnWidth.length > 0) {
            x1 += this.countedColumnWidth[0];
        }
        for (int i2 = 1; i2 < this.bordersHandler.getNumberOfColumns(); i2++) {
            this.bordersHandler.drawVerticalBorder(i2, startY, x1, drawContext.getCanvas(), this.heights);
            if (i2 < this.countedColumnWidth.length) {
                x1 += this.countedColumnWidth[i2];
            }
        }
        if (isTopTablePart) {
            this.bordersHandler.drawHorizontalBorder(0, startX, startY, drawContext.getCanvas(), this.countedColumnWidth);
        }
        if (isBottomTablePart && isComplete) {
            this.bordersHandler.drawHorizontalBorder(this.heights.size(), startX, y1, drawContext.getCanvas(), this.countedColumnWidth);
        }
        this.bordersHandler.drawVerticalBorder(0, startY, startX, drawContext.getCanvas(), this.heights);
        this.bordersHandler.drawVerticalBorder(this.bordersHandler.getNumberOfColumns(), startY, x1, drawContext.getCanvas(), this.heights);
        if (isTagged) {
            drawContext.getCanvas().closeTag();
        }
    }

    private void applyFixedXOrYPosition(boolean isXPosition, Rectangle layoutBox) {
        if (isPositioned() && isFixedLayout()) {
            if (isXPosition) {
                float x = getPropertyAsFloat(34).floatValue();
                layoutBox.setX(x);
            } else {
                float y = getPropertyAsFloat(14).floatValue();
                move(0.0f, y - this.occupiedArea.getBBox().getY());
            }
        }
    }

    private void adjustFooterAndFixOccupiedArea(Rectangle layoutBox, float verticalBorderSpacing) {
        if (this.footerRenderer != null) {
            this.footerRenderer.move(0.0f, layoutBox.getHeight() + verticalBorderSpacing);
            float footerHeight = this.footerRenderer.getOccupiedArea().getBBox().getHeight() - verticalBorderSpacing;
            this.occupiedArea.getBBox().moveDown(footerHeight).increaseHeight(footerHeight);
        }
    }

    private void adjustCaptionAndFixOccupiedArea(Rectangle layoutBox, float verticalBorderSpacing) {
        if (this.captionRenderer != null) {
            float captionHeight = this.captionRenderer.getOccupiedArea().getBBox().getHeight();
            this.occupiedArea.getBBox().moveDown(captionHeight).increaseHeight(captionHeight);
            if (CaptionSide.BOTTOM.equals(this.captionRenderer.getProperty(119))) {
                this.captionRenderer.move(0.0f, layoutBox.getHeight() + verticalBorderSpacing);
            } else {
                this.occupiedArea.getBBox().moveUp(captionHeight);
            }
        }
    }

    private void correctLayoutedCellsOccupiedAreas(LayoutResult[] splits, int row, int[] targetOverflowRowIndex, Float blockMinHeight, Rectangle layoutBox, List<Boolean> rowsHasCellWithSetHeight, boolean isLastRenderer, boolean processBigRowspan, boolean skip) {
        float cellHeightInLastRow;
        int finish = this.bordersHandler.getFinishRow();
        this.bordersHandler.setFinishRow(this.rowRange.getFinishRow());
        Border currentBorder = this.bordersHandler.getWidestHorizontalBorder(finish + 1);
        this.bordersHandler.setFinishRow(finish);
        if (skip) {
            this.bordersHandler.tableBoundingBorders[2] = getBorders()[2];
            this.bordersHandler.skipFooter(this.bordersHandler.tableBoundingBorders);
        }
        float width = (!(this.bordersHandler instanceof CollapsedTableBorders) || null == currentBorder) ? 0.0f : currentBorder.getWidth();
        float currentBottomIndent = width;
        float realBottomIndent = this.bordersHandler instanceof CollapsedTableBorders ? this.bordersHandler.getMaxBottomWidth() : 0.0f;
        if (0 != this.heights.size()) {
            this.heights.set(this.heights.size() - 1, Float.valueOf(this.heights.get(this.heights.size() - 1).floatValue() + ((realBottomIndent - currentBottomIndent) / 2.0f)));
            this.occupiedArea.getBBox().increaseHeight((realBottomIndent - currentBottomIndent) / 2.0f).moveDown((realBottomIndent - currentBottomIndent) / 2.0f);
            layoutBox.decreaseHeight((realBottomIndent - currentBottomIndent) / 2.0f);
            if (processBigRowspan) {
                CellRenderer[] currentRow = this.rows.get(this.heights.size());
                for (int col = 0; col < currentRow.length; col++) {
                    CellRenderer cell = null == splits[col] ? currentRow[col] : (CellRenderer) splits[col].getSplitRenderer();
                    if (cell != null) {
                        float height = 0.0f;
                        int rowspan = cell.getPropertyAsInteger(60).intValue();
                        int colspan = cell.getPropertyAsInteger(16).intValue();
                        float[] indents = this.bordersHandler.getCellBorderIndents(this.bordersHandler instanceof SeparatedTableBorders ? row : targetOverflowRowIndex[col], col, rowspan, colspan);
                        for (int l = (this.heights.size() - 1) - 1; l > targetOverflowRowIndex[col] - rowspan && l >= 0; l--) {
                            height += this.heights.get(l).floatValue();
                        }
                        if (this.bordersHandler instanceof SeparatedTableBorders) {
                            cellHeightInLastRow = cell.getOccupiedArea().getBBox().getHeight() - height;
                        } else {
                            cellHeightInLastRow = ((cell.getOccupiedArea().getBBox().getHeight() + (indents[0] / 2.0f)) + (indents[2] / 2.0f)) - height;
                        }
                        if (this.heights.get(this.heights.size() - 1).floatValue() < cellHeightInLastRow) {
                            if (this.bordersHandler instanceof SeparatedTableBorders) {
                                float differenceToConsider = cellHeightInLastRow - this.heights.get(this.heights.size() - 1).floatValue();
                                this.occupiedArea.getBBox().moveDown(differenceToConsider);
                                this.occupiedArea.getBBox().increaseHeight(differenceToConsider);
                            }
                            this.heights.set(this.heights.size() - 1, Float.valueOf(cellHeightInLastRow));
                        }
                    }
                }
            }
        }
        float additionalCellHeight = 0.0f;
        int numOfRowsWithFloatHeight = 0;
        if (isLastRenderer) {
            float additionalHeight = 0.0f;
            if (null != blockMinHeight && blockMinHeight.floatValue() > this.occupiedArea.getBBox().getHeight() + (realBottomIndent / 2.0f)) {
                additionalHeight = Math.min(layoutBox.getHeight() - (realBottomIndent / 2.0f), (blockMinHeight.floatValue() - this.occupiedArea.getBBox().getHeight()) - (realBottomIndent / 2.0f));
                for (int k = 0; k < rowsHasCellWithSetHeight.size(); k++) {
                    if (Boolean.FALSE.equals(rowsHasCellWithSetHeight.get(k))) {
                        numOfRowsWithFloatHeight++;
                    }
                }
            }
            additionalCellHeight = additionalHeight / (0 == numOfRowsWithFloatHeight ? this.heights.size() : numOfRowsWithFloatHeight);
            for (int k2 = 0; k2 < this.heights.size(); k2++) {
                if (0 == numOfRowsWithFloatHeight || Boolean.FALSE.equals(rowsHasCellWithSetHeight.get(k2))) {
                    this.heights.set(k2, Float.valueOf(this.heights.get(k2).floatValue() + additionalCellHeight));
                }
            }
        }
        float cumulativeShift = 0.0f;
        for (int k3 = 0; k3 < this.heights.size(); k3++) {
            correctRowCellsOccupiedAreas(splits, row, targetOverflowRowIndex, k3, rowsHasCellWithSetHeight, cumulativeShift, additionalCellHeight);
            if (isLastRenderer && (0 == numOfRowsWithFloatHeight || Boolean.FALSE.equals(rowsHasCellWithSetHeight.get(k3)))) {
                cumulativeShift += additionalCellHeight;
            }
        }
        this.occupiedArea.getBBox().moveDown(cumulativeShift).increaseHeight(cumulativeShift);
        layoutBox.decreaseHeight(cumulativeShift);
    }

    private void correctRowCellsOccupiedAreas(LayoutResult[] splits, int row, int[] targetOverflowRowIndex, int currentRowIndex, List<Boolean> rowsHasCellWithSetHeight, float cumulativeShift, float additionalCellHeight) {
        CellRenderer[] currentRow = this.rows.get(currentRowIndex);
        for (int col = 0; col < currentRow.length; col++) {
            CellRenderer cell = (currentRowIndex < row || null == splits[col]) ? currentRow[col] : (CellRenderer) splits[col].getSplitRenderer();
            if (cell != null) {
                float height = 0.0f;
                int colspan = cell.getPropertyAsInteger(16).intValue();
                int rowspan = cell.getPropertyAsInteger(60).intValue();
                float rowspanOffset = 0.0f;
                float[] indents = this.bordersHandler.getCellBorderIndents((currentRowIndex < row || (this.bordersHandler instanceof SeparatedTableBorders)) ? currentRowIndex : targetOverflowRowIndex[col], col, rowspan, colspan);
                int l = (currentRowIndex < row ? currentRowIndex : this.heights.size() - 1) - 1;
                while (true) {
                    if (l <= (currentRowIndex < row ? currentRowIndex : targetOverflowRowIndex[col]) - rowspan || l < 0) {
                        break;
                    }
                    height += this.heights.get(l).floatValue();
                    if (Boolean.FALSE.equals(rowsHasCellWithSetHeight.get(l))) {
                        rowspanOffset += additionalCellHeight;
                    }
                    l--;
                }
                float height2 = height + this.heights.get(currentRowIndex < row ? currentRowIndex : this.heights.size() - 1).floatValue();
                if (!(this.bordersHandler instanceof SeparatedTableBorders)) {
                    height2 -= (indents[0] / 2.0f) + (indents[2] / 2.0f);
                }
                float shift = height2 - cell.getOccupiedArea().getBBox().getHeight();
                Rectangle bBox = cell.getOccupiedArea().getBBox();
                bBox.moveDown(shift);
                try {
                    cell.move(0.0f, -(cumulativeShift - rowspanOffset));
                    bBox.setHeight(height2);
                    cell.applyVerticalAlignment();
                } catch (NullPointerException e) {
                    Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
                    logger.error(MessageFormatUtil.format(LogMessageConstant.OCCUPIED_AREA_HAS_NOT_BEEN_INITIALIZED, "Some of the cell's content might not end up placed correctly."));
                }
            }
        }
    }

    protected void extendLastRow(CellRenderer[] lastRow, Rectangle freeBox) {
        if (null != lastRow && 0 != this.heights.size()) {
            this.heights.set(this.heights.size() - 1, Float.valueOf(this.heights.get(this.heights.size() - 1).floatValue() + freeBox.getHeight()));
            this.occupiedArea.getBBox().moveDown(freeBox.getHeight()).increaseHeight(freeBox.getHeight());
            for (CellRenderer cell : lastRow) {
                if (null != cell) {
                    cell.occupiedArea.getBBox().moveDown(freeBox.getHeight()).increaseHeight(freeBox.getHeight());
                }
            }
            freeBox.moveUp(freeBox.getHeight()).setHeight(0.0f);
        }
    }

    private void setRowRange(Table.RowRange rowRange) {
        this.rowRange = rowRange;
        for (int row = rowRange.getStartRow(); row <= rowRange.getFinishRow(); row++) {
            this.rows.add(new CellRenderer[((Table) this.modelElement).getNumberOfColumns()]);
        }
    }

    private TableRenderer initFooterOrHeaderRenderer(boolean footer, Border[] tableBorders) {
        TableBorders collapsedTableBorders;
        Table table = (Table) getModelElement();
        boolean isSeparated = BorderCollapsePropertyValue.SEPARATE.equals(getProperty(114));
        Table footerOrHeader = footer ? table.getFooter() : table.getHeader();
        int innerBorder = footer ? 0 : 2;
        int outerBorder = footer ? 2 : 0;
        TableRenderer renderer = (TableRenderer) footerOrHeader.createRendererSubTree().setParent(this);
        ensureFooterOrHeaderHasTheSamePropertiesAsParentTableRenderer(renderer);
        boolean firstHeader = !footer && this.rowRange.getStartRow() == 0 && this.isOriginalNonSplitRenderer;
        LayoutTaggingHelper taggingHelper = (LayoutTaggingHelper) getProperty(108);
        if (taggingHelper != null) {
            taggingHelper.addKidsHint(this, Collections.singletonList(renderer));
            LayoutTaggingHelper.addTreeHints(taggingHelper, renderer);
            if (!footer && !firstHeader) {
                taggingHelper.markArtifactHint(renderer);
            }
        }
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            if (table.isEmpty()) {
                if (!footer || null == this.headerRenderer) {
                    renderer.setBorders(tableBorders[innerBorder], innerBorder);
                }
                this.bordersHandler.tableBoundingBorders[innerBorder] = Border.NO_BORDER;
            }
            renderer.setBorders(tableBorders[1], 1);
            renderer.setBorders(tableBorders[3], 3);
            renderer.setBorders(tableBorders[outerBorder], outerBorder);
            this.bordersHandler.tableBoundingBorders[outerBorder] = Border.NO_BORDER;
        } else if (this.bordersHandler instanceof CollapsedTableBorders) {
            Border[] borders = renderer.getBorders();
            if (table.isEmpty()) {
                renderer.setBorders(CollapsedTableBorders.getCollapsedBorder(borders[innerBorder], tableBorders[innerBorder]), innerBorder);
                this.bordersHandler.tableBoundingBorders[innerBorder] = Border.NO_BORDER;
            }
            renderer.setBorders(CollapsedTableBorders.getCollapsedBorder(borders[1], tableBorders[1]), 1);
            renderer.setBorders(CollapsedTableBorders.getCollapsedBorder(borders[3], tableBorders[3]), 3);
            renderer.setBorders(CollapsedTableBorders.getCollapsedBorder(borders[outerBorder], tableBorders[outerBorder]), outerBorder);
            this.bordersHandler.tableBoundingBorders[outerBorder] = Border.NO_BORDER;
        }
        if (isSeparated) {
            collapsedTableBorders = new SeparatedTableBorders(renderer.rows, ((Table) renderer.getModelElement()).getNumberOfColumns(), renderer.getBorders());
        } else {
            collapsedTableBorders = new CollapsedTableBorders(renderer.rows, ((Table) renderer.getModelElement()).getNumberOfColumns(), renderer.getBorders());
        }
        renderer.bordersHandler = collapsedTableBorders;
        renderer.bordersHandler.initializeBorders();
        renderer.bordersHandler.setRowRange(renderer.rowRange.getStartRow(), renderer.rowRange.getFinishRow());
        renderer.bordersHandler.processAllBordersAndEmptyRows();
        renderer.correctRowRange();
        return renderer;
    }

    private void ensureFooterOrHeaderHasTheSamePropertiesAsParentTableRenderer(TableRenderer headerOrFooterRenderer) {
        headerOrFooterRenderer.setProperty(114, getProperty(114));
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            headerOrFooterRenderer.setProperty(115, getPropertyAsFloat(115));
            headerOrFooterRenderer.setProperty(116, getPropertyAsFloat(116));
            headerOrFooterRenderer.setProperty(9, Border.NO_BORDER);
            headerOrFooterRenderer.setProperty(11, Border.NO_BORDER);
            headerOrFooterRenderer.setProperty(13, Border.NO_BORDER);
            headerOrFooterRenderer.setProperty(12, Border.NO_BORDER);
            headerOrFooterRenderer.setProperty(10, Border.NO_BORDER);
        }
    }

    private TableRenderer prepareFooterOrHeaderRendererForLayout(TableRenderer renderer, float layoutBoxWidth) {
        renderer.countedColumnWidth = this.countedColumnWidth;
        renderer.bordersHandler.leftBorderMaxWidth = this.bordersHandler.getLeftBorderMaxWidth();
        renderer.bordersHandler.rightBorderMaxWidth = this.bordersHandler.getRightBorderMaxWidth();
        if (hasProperty(77)) {
            renderer.setProperty(77, UnitValue.createPointValue(layoutBoxWidth));
        }
        return this;
    }

    private boolean isHeaderRenderer() {
        return (this.parent instanceof TableRenderer) && ((TableRenderer) this.parent).headerRenderer == this;
    }

    private boolean isFooterRenderer() {
        return (this.parent instanceof TableRenderer) && ((TableRenderer) this.parent).footerRenderer == this;
    }

    private boolean isFooterRendererOfLargeTable() {
        return isFooterRenderer() && !(getTable().isComplete() && 0 == ((TableRenderer) this.parent).getTable().getLastRowBottomBorder().size());
    }

    private boolean isTopTablePart() {
        return null == this.headerRenderer && (!isFooterRenderer() || (0 == ((TableRenderer) this.parent).rows.size() && null == ((TableRenderer) this.parent).headerRenderer));
    }

    private boolean isBottomTablePart() {
        return null == this.footerRenderer && (!isHeaderRenderer() || (0 == ((TableRenderer) this.parent).rows.size() && null == ((TableRenderer) this.parent).footerRenderer));
    }

    private void calculateColumnWidths(float availableWidth) {
        if (this.countedColumnWidth == null || this.totalWidthForColumns != availableWidth) {
            TableWidths tableWidths = new TableWidths(this, availableWidth, false, this.bordersHandler.rightBorderMaxWidth, this.bordersHandler.leftBorderMaxWidth);
            this.countedColumnWidth = tableWidths.layout();
        }
    }

    private float getTableWidth() {
        float sum;
        float sum2 = 0.0f;
        for (float column : this.countedColumnWidth) {
            sum2 += column;
        }
        if (this.bordersHandler instanceof SeparatedTableBorders) {
            float sum3 = sum2 + this.bordersHandler.getRightBorderMaxWidth() + this.bordersHandler.getLeftBorderMaxWidth();
            Float horizontalSpacing = getPropertyAsFloat(115);
            sum = sum3 + (null == horizontalSpacing ? 0.0f : horizontalSpacing.floatValue());
        } else {
            sum = sum2 + (this.bordersHandler.getRightBorderMaxWidth() / 2.0f) + (this.bordersHandler.getLeftBorderMaxWidth() / 2.0f);
        }
        return sum;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableRenderer$CellRendererInfo.class */
    private static class CellRendererInfo {
        public CellRenderer cellRenderer;
        public int column;
        public int finishRowInd;

        public CellRendererInfo(CellRenderer cellRenderer, int column, int finishRow) {
            this.cellRenderer = cellRenderer;
            this.column = column;
            this.finishRowInd = finishRow;
        }
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/renderer/TableRenderer$OverflowRowsWrapper.class */
    private static class OverflowRowsWrapper {
        private TableRenderer overflowRenderer;
        private HashMap<Integer, Boolean> isRowReplaced = new HashMap<>();
        private boolean isReplaced = false;

        public OverflowRowsWrapper(TableRenderer overflowRenderer) {
            this.overflowRenderer = overflowRenderer;
        }

        public CellRenderer getCell(int row, int col) {
            return this.overflowRenderer.rows.get(row)[col];
        }

        public CellRenderer setCell(int row, int col, CellRenderer newCell) {
            if (!this.isReplaced) {
                this.overflowRenderer.rows = new ArrayList(this.overflowRenderer.rows);
                this.isReplaced = true;
            }
            if (!Boolean.TRUE.equals(this.isRowReplaced.get(Integer.valueOf(row)))) {
                this.overflowRenderer.rows.set(row, (CellRenderer[]) this.overflowRenderer.rows.get(row).clone());
            }
            this.overflowRenderer.rows.get(row)[col] = newCell;
            return newCell;
        }
    }

    private void enlargeCellWithBigRowspan(CellRenderer[] currentRow, OverflowRowsWrapper overflowRows, int row, int col, int minRowspan, TableRenderer[] splitResult, int[] targetOverflowRowIndex) {
        this.childRenderers.add(currentRow[col]);
        int i = row;
        while (i < row + minRowspan && i + 1 < this.rows.size() && splitResult[1].rows.get((i + 1) - row)[col] != null) {
            overflowRows.setCell(i - row, col, splitResult[1].rows.get((i + 1) - row)[col]);
            overflowRows.setCell((i + 1) - row, col, null);
            this.rows.get(i)[col] = this.rows.get(i + 1)[col];
            this.rows.get(i + 1)[col] = null;
            i++;
        }
        if (i != (row + minRowspan) - 1 && null != this.rows.get(i)[col]) {
            CellRenderer overflowCell = (CellRenderer) ((Cell) this.rows.get(i)[col].getModelElement()).getRenderer().setParent(this);
            overflowRows.setCell(i - row, col, null);
            overflowRows.setCell(targetOverflowRowIndex[col] - row, col, overflowCell);
            CellRenderer originalCell = this.rows.get(i)[col];
            this.rows.get(i)[col] = null;
            this.rows.get(targetOverflowRowIndex[col])[col] = originalCell;
            originalCell.isLastRendererForModelElement = false;
            overflowCell.setProperty(109, originalCell.getProperty(109));
        }
    }

    private void enlargeCell(int col, int row, int minRowspan, CellRenderer[] currentRow, OverflowRowsWrapper overflowRows, int[] targetOverflowRowIndex, TableRenderer[] splitResult) {
        LayoutArea cellOccupiedArea = currentRow[col].getOccupiedArea();
        if (1 == minRowspan) {
            CellRenderer overflowCell = (CellRenderer) ((Cell) currentRow[col].getModelElement()).clone(true).getRenderer();
            overflowCell.setParent(this);
            overflowCell.deleteProperty(27);
            overflowCell.deleteProperty(85);
            overflowCell.deleteProperty(84);
            overflowRows.setCell(0, col, null);
            overflowRows.setCell(targetOverflowRowIndex[col] - row, col, overflowCell);
            this.childRenderers.add(currentRow[col]);
            CellRenderer originalCell = currentRow[col];
            currentRow[col] = null;
            this.rows.get(targetOverflowRowIndex[col])[col] = originalCell;
            originalCell.isLastRendererForModelElement = false;
            overflowCell.setProperty(109, originalCell.getProperty(109));
        } else {
            enlargeCellWithBigRowspan(currentRow, overflowRows, row, col, minRowspan, splitResult, targetOverflowRowIndex);
        }
        overflowRows.getCell(targetOverflowRowIndex[col] - row, col).occupiedArea = cellOccupiedArea;
    }

    void applyMarginsAndPaddingsAndCalculateColumnWidths(Rectangle layoutBox) {
        UnitValue[] margins = getMargins();
        if (!margins[1].isPointValue()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 45));
        }
        if (!margins[3].isPointValue()) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger2.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 44));
        }
        UnitValue[] paddings = getPaddings();
        if (!paddings[1].isPointValue()) {
            Logger logger3 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger3.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 49));
        }
        if (!paddings[3].isPointValue()) {
            Logger logger4 = LoggerFactory.getLogger((Class<?>) TableRenderer.class);
            logger4.error(MessageFormatUtil.format(LogMessageConstant.PROPERTY_IN_PERCENTS_NOT_SUPPORTED, 48));
        }
        calculateColumnWidths((((layoutBox.getWidth() - margins[1].getValue()) - margins[3].getValue()) - paddings[1].getValue()) - paddings[3].getValue());
    }
}
