package com.alibaba.excel.write.style.column;

import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/column/AbstractColumnWidthStyleStrategy.class */
public abstract class AbstractColumnWidthStyleStrategy implements CellWriteHandler, NotRepeatExecutor {
    protected abstract void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> list, Cell cell, Head head, Integer num, Boolean bool);

    @Override // com.alibaba.excel.event.NotRepeatExecutor
    public String uniqueValue() {
        return "ColumnWidthStyleStrategy";
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        setColumnWidth(writeSheetHolder, cellDataList, cell, head, relativeRowIndex, isHead);
    }
}
