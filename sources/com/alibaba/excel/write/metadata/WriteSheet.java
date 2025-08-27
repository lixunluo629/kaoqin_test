package com.alibaba.excel.write.metadata;

import com.alibaba.excel.metadata.TableStyle;
import java.util.HashMap;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/WriteSheet.class */
public class WriteSheet extends WriteBasicParameter {
    private Integer sheetNo;
    private String sheetName;

    @Deprecated
    private Map<Integer, Integer> columnWidthMap = new HashMap();

    @Deprecated
    private TableStyle tableStyle;

    public Integer getSheetNo() {
        return this.sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Map<Integer, Integer> getColumnWidthMap() {
        return this.columnWidthMap;
    }

    public void setColumnWidthMap(Map<Integer, Integer> columnWidthMap) {
        this.columnWidthMap = columnWidthMap;
    }

    public TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public void setTableStyle(TableStyle tableStyle) {
        this.tableStyle = tableStyle;
    }

    public String toString() {
        return "WriteSheet{sheetNo=" + this.sheetNo + ", sheetName='" + this.sheetName + "'} " + super.toString();
    }
}
