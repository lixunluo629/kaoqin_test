package com.alibaba.excel.write.style.column;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/column/AbstractHeadColumnWidthStyleStrategy.class */
public abstract class AbstractHeadColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {
    protected abstract Integer columnWidth(Head head, Integer num);

    @Override // com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Integer width;
        boolean needSetWidth = relativeRowIndex != null && (isHead.booleanValue() || relativeRowIndex.intValue() == 0);
        if (needSetWidth && (width = columnWidth(head, Integer.valueOf(cell.getColumnIndex()))) != null) {
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), Integer.valueOf(width.intValue() * 256).intValue());
        }
    }
}
