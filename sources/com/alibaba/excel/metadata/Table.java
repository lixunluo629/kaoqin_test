package com.alibaba.excel.metadata;

import java.util.List;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/Table.class */
public class Table {
    private Class<? extends BaseRowModel> clazz;
    private List<List<String>> head;
    private int tableNo;
    private TableStyle tableStyle;

    public Table(Integer tableNo) {
        this.tableNo = tableNo.intValue();
    }

    public TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public void setTableStyle(TableStyle tableStyle) {
        this.tableStyle = tableStyle;
    }

    public Class<? extends BaseRowModel> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<? extends BaseRowModel> clazz) {
        this.clazz = clazz;
    }

    public List<List<String>> getHead() {
        return this.head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public int getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }
}
