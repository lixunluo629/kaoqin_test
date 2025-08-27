package com.alibaba.excel.write.handler;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Row;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/handler/RowWriteHandler.class */
public interface RowWriteHandler extends WriteHandler {
    void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer num, Integer num2, Boolean bool);

    void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer num, Boolean bool);

    void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer num, Boolean bool);
}
