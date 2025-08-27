package com.alibaba.excel.write.style.column;

import com.alibaba.excel.metadata.Head;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/column/SimpleColumnWidthStyleStrategy.class */
public class SimpleColumnWidthStyleStrategy extends AbstractHeadColumnWidthStyleStrategy {
    private Integer columnWidth;

    public SimpleColumnWidthStyleStrategy(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override // com.alibaba.excel.write.style.column.AbstractHeadColumnWidthStyleStrategy
    protected Integer columnWidth(Head head, Integer columnIndex) {
        return this.columnWidth;
    }
}
