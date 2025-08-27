package com.alibaba.excel.analysis.v03;

import com.alibaba.excel.metadata.CellData;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/AbstractXlsRecordHandler.class */
public abstract class AbstractXlsRecordHandler implements XlsRecordHandler {
    protected int row = -1;
    protected int column = -1;
    protected CellData cellData;

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getRow() {
        return this.row;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getColumn() {
        return this.column;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public CellData getCellData() {
        return this.cellData;
    }

    @Override // java.lang.Comparable
    public int compareTo(XlsRecordHandler o) {
        return getOrder() - o.getOrder();
    }
}
