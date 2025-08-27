package org.apache.poi.xssf.usermodel;

import org.apache.poi.ss.usermodel.TableStyle;
import org.apache.poi.ss.usermodel.TableStyleInfo;
import org.apache.poi.xssf.model.StylesTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFTableStyleInfo.class */
public class XSSFTableStyleInfo implements TableStyleInfo {
    private final CTTableStyleInfo styleInfo;
    private final StylesTable stylesTable;
    private TableStyle style;
    private boolean columnStripes;
    private boolean rowStripes;
    private boolean firstColumn;
    private boolean lastColumn;

    public XSSFTableStyleInfo(StylesTable stylesTable, CTTableStyleInfo tableStyleInfo) {
        this.columnStripes = tableStyleInfo.getShowColumnStripes();
        this.rowStripes = tableStyleInfo.getShowRowStripes();
        this.firstColumn = tableStyleInfo.getShowFirstColumn();
        this.lastColumn = tableStyleInfo.getShowLastColumn();
        this.style = stylesTable.getTableStyle(tableStyleInfo.getName());
        this.stylesTable = stylesTable;
        this.styleInfo = tableStyleInfo;
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public boolean isShowColumnStripes() {
        return this.columnStripes;
    }

    public void setShowColumnStripes(boolean show) {
        this.columnStripes = show;
        this.styleInfo.setShowColumnStripes(show);
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public boolean isShowRowStripes() {
        return this.rowStripes;
    }

    public void setShowRowStripes(boolean show) {
        this.rowStripes = show;
        this.styleInfo.setShowRowStripes(show);
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public boolean isShowFirstColumn() {
        return this.firstColumn;
    }

    public void setFirstColumn(boolean showFirstColumn) {
        this.firstColumn = showFirstColumn;
        this.styleInfo.setShowFirstColumn(showFirstColumn);
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public boolean isShowLastColumn() {
        return this.lastColumn;
    }

    public void setLastColumn(boolean showLastColumn) {
        this.lastColumn = showLastColumn;
        this.styleInfo.setShowLastColumn(showLastColumn);
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public String getName() {
        return this.style.getName();
    }

    public void setName(String name) {
        this.styleInfo.setName(name);
        this.style = this.stylesTable.getTableStyle(name);
    }

    @Override // org.apache.poi.ss.usermodel.TableStyleInfo
    public TableStyle getStyle() {
        return this.style;
    }
}
