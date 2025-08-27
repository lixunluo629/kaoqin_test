package com.alibaba.excel.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/Sheet.class */
public class Sheet {
    private int headLineMun;
    private int sheetNo;
    private String sheetName;
    private Class<? extends BaseRowModel> clazz;
    private List<List<String>> head;
    private TableStyle tableStyle;
    private Map<Integer, Integer> columnWidthMap = new HashMap();
    private Boolean autoWidth = Boolean.FALSE;
    private int startRow = 0;

    public Sheet(int sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Sheet(int sheetNo, int headLineMun) {
        this.sheetNo = sheetNo;
        this.headLineMun = headLineMun;
    }

    public Sheet(int sheetNo, int headLineMun, Class<? extends BaseRowModel> clazz) {
        this.sheetNo = sheetNo;
        this.headLineMun = headLineMun;
        this.clazz = clazz;
    }

    public Sheet(int sheetNo, int headLineMun, Class<? extends BaseRowModel> clazz, String sheetName, List<List<String>> head) {
        this.sheetNo = sheetNo;
        this.clazz = clazz;
        this.headLineMun = headLineMun;
        this.sheetName = sheetName;
        this.head = head;
    }

    public List<List<String>> getHead() {
        return this.head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public Class<? extends BaseRowModel> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<? extends BaseRowModel> clazz) {
        this.clazz = clazz;
        if (this.headLineMun == 0) {
            this.headLineMun = 1;
        }
    }

    public int getHeadLineMun() {
        return this.headLineMun;
    }

    public void setHeadLineMun(int headLineMun) {
        this.headLineMun = headLineMun;
    }

    public int getSheetNo() {
        return this.sheetNo;
    }

    public void setSheetNo(int sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public void setTableStyle(TableStyle tableStyle) {
        this.tableStyle = tableStyle;
    }

    public Map<Integer, Integer> getColumnWidthMap() {
        return this.columnWidthMap;
    }

    public void setColumnWidthMap(Map<Integer, Integer> columnWidthMap) {
        this.columnWidthMap = columnWidthMap;
    }

    public String toString() {
        return "Sheet{headLineMun=" + this.headLineMun + ", sheetNo=" + this.sheetNo + ", sheetName='" + this.sheetName + "', clazz=" + this.clazz + ", head=" + this.head + ", tableStyle=" + this.tableStyle + ", columnWidthMap=" + this.columnWidthMap + '}';
    }

    public Boolean getAutoWidth() {
        return this.autoWidth;
    }

    public void setAutoWidth(Boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}
