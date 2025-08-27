package com.alibaba.excel.metadata;

import org.apache.poi.ss.usermodel.IndexedColors;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/TableStyle.class */
public class TableStyle {
    private IndexedColors tableHeadBackGroundColor;
    private Font tableHeadFont;
    private Font tableContentFont;
    private IndexedColors tableContentBackGroundColor;

    public IndexedColors getTableHeadBackGroundColor() {
        return this.tableHeadBackGroundColor;
    }

    public void setTableHeadBackGroundColor(IndexedColors tableHeadBackGroundColor) {
        this.tableHeadBackGroundColor = tableHeadBackGroundColor;
    }

    public Font getTableHeadFont() {
        return this.tableHeadFont;
    }

    public void setTableHeadFont(Font tableHeadFont) {
        this.tableHeadFont = tableHeadFont;
    }

    public Font getTableContentFont() {
        return this.tableContentFont;
    }

    public void setTableContentFont(Font tableContentFont) {
        this.tableContentFont = tableContentFont;
    }

    public IndexedColors getTableContentBackGroundColor() {
        return this.tableContentBackGroundColor;
    }

    public void setTableContentBackGroundColor(IndexedColors tableContentBackGroundColor) {
        this.tableContentBackGroundColor = tableContentBackGroundColor;
    }
}
