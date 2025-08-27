package com.alibaba.excel.write.style.row;

import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Row;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/row/AbstractRowHeightStyleStrategy.class */
public abstract class AbstractRowHeightStyleStrategy implements RowWriteHandler, NotRepeatExecutor {
    protected abstract void setHeadColumnHeight(Row row, int i);

    protected abstract void setContentColumnHeight(Row row, int i);

    @Override // com.alibaba.excel.event.NotRepeatExecutor
    public String uniqueValue() {
        return "RowHighStyleStrategy";
    }

    @Override // com.alibaba.excel.write.handler.RowWriteHandler
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.RowWriteHandler
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override // com.alibaba.excel.write.handler.RowWriteHandler
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (isHead == null) {
            return;
        }
        if (isHead.booleanValue()) {
            setHeadColumnHeight(row, relativeRowIndex.intValue());
        } else {
            setContentColumnHeight(row, relativeRowIndex.intValue());
        }
    }
}
