package com.alibaba.excel.write.handler;

import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/handler/WorkbookWriteHandler.class */
public interface WorkbookWriteHandler extends WriteHandler {
    void beforeWorkbookCreate();

    void afterWorkbookCreate(WriteWorkbookHolder writeWorkbookHolder);

    void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder);
}
