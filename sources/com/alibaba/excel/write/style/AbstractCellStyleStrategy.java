package com.alibaba.excel.write.style;

import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/AbstractCellStyleStrategy.class */
public abstract class AbstractCellStyleStrategy implements CellWriteHandler, SheetWriteHandler, NotRepeatExecutor {
    boolean hasInitialized = false;

    protected abstract void initCellStyle(Workbook workbook);

    protected abstract void setHeadCellStyle(Cell cell, Head head, Integer num);

    protected abstract void setContentCellStyle(Cell cell, Head head, Integer num);

    @Override // com.alibaba.excel.event.NotRepeatExecutor
    public String uniqueValue() {
        return "CellStyleStrategy";
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.CellWriteHandler
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead == null || head == null) {
            return;
        }
        if (isHead.booleanValue()) {
            setHeadCellStyle(cell, head, relativeRowIndex);
        } else {
            setContentCellStyle(cell, head, relativeRowIndex);
        }
    }

    @Override // com.alibaba.excel.write.handler.SheetWriteHandler
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override // com.alibaba.excel.write.handler.SheetWriteHandler
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        initCellStyle(writeWorkbookHolder.getWorkbook());
        this.hasInitialized = true;
    }
}
