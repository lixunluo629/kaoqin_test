package com.alibaba.excel.write.handler;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/handler/SheetWriteHandler.class */
public interface SheetWriteHandler extends WriteHandler {
    void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder);

    void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder);
}
