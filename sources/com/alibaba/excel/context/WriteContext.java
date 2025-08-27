package com.alibaba.excel.context;

import com.alibaba.excel.enums.WriteTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.holder.WriteHolder;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/context/WriteContext.class */
public interface WriteContext {
    void currentSheet(WriteSheet writeSheet, WriteTypeEnum writeTypeEnum);

    void currentTable(WriteTable writeTable);

    WriteWorkbookHolder writeWorkbookHolder();

    WriteSheetHolder writeSheetHolder();

    WriteTableHolder writeTableHolder();

    WriteHolder currentWriteHolder();

    void finish(boolean z);

    @Deprecated
    Sheet getCurrentSheet();

    @Deprecated
    boolean needHead();

    @Deprecated
    OutputStream getOutputStream();

    @Deprecated
    Workbook getWorkbook();
}
