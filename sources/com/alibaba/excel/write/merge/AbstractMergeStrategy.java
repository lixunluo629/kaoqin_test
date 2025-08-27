package com.alibaba.excel.write.merge;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/merge/AbstractMergeStrategy.class */
public abstract class AbstractMergeStrategy implements CellWriteHandler {
    protected abstract void merge(Sheet sheet, Cell cell, Head head, Integer num);

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead.booleanValue()) {
            return;
        }
        merge(writeSheetHolder.getSheet(), cell, head, relativeRowIndex);
    }
}
