package com.alibaba.excel.write.style.row;

import org.apache.poi.ss.usermodel.Row;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/row/SimpleRowHeightStyleStrategy.class */
public class SimpleRowHeightStyleStrategy extends AbstractRowHeightStyleStrategy {
    private Short headRowHeight;
    private Short contentRowHeight;

    public SimpleRowHeightStyleStrategy(Short headRowHeight, Short contentRowHeight) {
        this.headRowHeight = headRowHeight;
        this.contentRowHeight = contentRowHeight;
    }

    @Override // com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy
    protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
        if (this.headRowHeight != null) {
            row.setHeightInPoints(this.headRowHeight.shortValue());
        }
    }

    @Override // com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy
    protected void setContentColumnHeight(Row row, int relativeRowIndex) {
        if (this.contentRowHeight != null) {
            row.setHeightInPoints(this.contentRowHeight.shortValue());
        }
    }
}
