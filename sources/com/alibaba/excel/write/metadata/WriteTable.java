package com.alibaba.excel.write.metadata;

import com.alibaba.excel.metadata.TableStyle;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/WriteTable.class */
public class WriteTable extends WriteBasicParameter {
    private Integer tableNo;

    @Deprecated
    private TableStyle tableStyle;

    public Integer getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public TableStyle getTableStyle() {
        return this.tableStyle;
    }

    public void setTableStyle(TableStyle tableStyle) {
        this.tableStyle = tableStyle;
    }
}
