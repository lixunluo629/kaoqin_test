package org.apache.poi.ss.usermodel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Sheet.class */
public interface Sheet extends Iterable<Row> {
    public static final short LeftMargin = 0;
    public static final short RightMargin = 1;
    public static final short TopMargin = 2;
    public static final short BottomMargin = 3;
    public static final short HeaderMargin = 4;
    public static final short FooterMargin = 5;
    public static final byte PANE_LOWER_RIGHT = 0;
    public static final byte PANE_UPPER_RIGHT = 1;
    public static final byte PANE_LOWER_LEFT = 2;
    public static final byte PANE_UPPER_LEFT = 3;

    Row createRow(int i);

    void removeRow(Row row);

    Row getRow(int i);

    int getPhysicalNumberOfRows();

    int getFirstRowNum();

    int getLastRowNum();

    void setColumnHidden(int i, boolean z);

    boolean isColumnHidden(int i);

    void setRightToLeft(boolean z);

    boolean isRightToLeft();

    void setColumnWidth(int i, int i2);

    int getColumnWidth(int i);

    float getColumnWidthInPixels(int i);

    void setDefaultColumnWidth(int i);

    int getDefaultColumnWidth();

    short getDefaultRowHeight();

    float getDefaultRowHeightInPoints();

    void setDefaultRowHeight(short s);

    void setDefaultRowHeightInPoints(float f);

    CellStyle getColumnStyle(int i);

    int addMergedRegion(CellRangeAddress cellRangeAddress);

    int addMergedRegionUnsafe(CellRangeAddress cellRangeAddress);

    void validateMergedRegions();

    void setVerticallyCenter(boolean z);

    void setHorizontallyCenter(boolean z);

    boolean getHorizontallyCenter();

    boolean getVerticallyCenter();

    void removeMergedRegion(int i);

    void removeMergedRegions(Collection<Integer> collection);

    int getNumMergedRegions();

    CellRangeAddress getMergedRegion(int i);

    List<CellRangeAddress> getMergedRegions();

    Iterator<Row> rowIterator();

    void setForceFormulaRecalculation(boolean z);

    boolean getForceFormulaRecalculation();

    void setAutobreaks(boolean z);

    void setDisplayGuts(boolean z);

    void setDisplayZeros(boolean z);

    boolean isDisplayZeros();

    void setFitToPage(boolean z);

    void setRowSumsBelow(boolean z);

    void setRowSumsRight(boolean z);

    boolean getAutobreaks();

    boolean getDisplayGuts();

    boolean getFitToPage();

    boolean getRowSumsBelow();

    boolean getRowSumsRight();

    boolean isPrintGridlines();

    void setPrintGridlines(boolean z);

    boolean isPrintRowAndColumnHeadings();

    void setPrintRowAndColumnHeadings(boolean z);

    PrintSetup getPrintSetup();

    Header getHeader();

    Footer getFooter();

    void setSelected(boolean z);

    double getMargin(short s);

    void setMargin(short s, double d);

    boolean getProtect();

    void protectSheet(String str);

    boolean getScenarioProtect();

    void setZoom(int i);

    short getTopRow();

    short getLeftCol();

    void showInPane(int i, int i2);

    void shiftRows(int i, int i2, int i3);

    void shiftRows(int i, int i2, int i3, boolean z, boolean z2);

    void createFreezePane(int i, int i2, int i3, int i4);

    void createFreezePane(int i, int i2);

    void createSplitPane(int i, int i2, int i3, int i4, int i5);

    PaneInformation getPaneInformation();

    void setDisplayGridlines(boolean z);

    boolean isDisplayGridlines();

    void setDisplayFormulas(boolean z);

    boolean isDisplayFormulas();

    void setDisplayRowColHeadings(boolean z);

    boolean isDisplayRowColHeadings();

    void setRowBreak(int i);

    boolean isRowBroken(int i);

    void removeRowBreak(int i);

    int[] getRowBreaks();

    int[] getColumnBreaks();

    void setColumnBreak(int i);

    boolean isColumnBroken(int i);

    void removeColumnBreak(int i);

    void setColumnGroupCollapsed(int i, boolean z);

    void groupColumn(int i, int i2);

    void ungroupColumn(int i, int i2);

    void groupRow(int i, int i2);

    void ungroupRow(int i, int i2);

    void setRowGroupCollapsed(int i, boolean z);

    void setDefaultColumnStyle(int i, CellStyle cellStyle);

    void autoSizeColumn(int i);

    void autoSizeColumn(int i, boolean z);

    Comment getCellComment(CellAddress cellAddress);

    Map<CellAddress, ? extends Comment> getCellComments();

    Drawing<?> getDrawingPatriarch();

    Drawing<?> createDrawingPatriarch();

    Workbook getWorkbook();

    String getSheetName();

    boolean isSelected();

    CellRange<? extends Cell> setArrayFormula(String str, CellRangeAddress cellRangeAddress);

    CellRange<? extends Cell> removeArrayFormula(Cell cell);

    DataValidationHelper getDataValidationHelper();

    List<? extends DataValidation> getDataValidations();

    void addValidationData(DataValidation dataValidation);

    AutoFilter setAutoFilter(CellRangeAddress cellRangeAddress);

    SheetConditionalFormatting getSheetConditionalFormatting();

    CellRangeAddress getRepeatingRows();

    CellRangeAddress getRepeatingColumns();

    void setRepeatingRows(CellRangeAddress cellRangeAddress);

    void setRepeatingColumns(CellRangeAddress cellRangeAddress);

    int getColumnOutlineLevel(int i);

    Hyperlink getHyperlink(int i, int i2);

    Hyperlink getHyperlink(CellAddress cellAddress);

    List<? extends Hyperlink> getHyperlinkList();

    CellAddress getActiveCell();

    void setActiveCell(CellAddress cellAddress);
}
