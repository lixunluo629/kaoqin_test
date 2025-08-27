package com.alibaba.excel.write;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/ExcelBuilder.class */
public interface ExcelBuilder {
    @Deprecated
    void addContent(List list, WriteSheet writeSheet);

    void addContent(List list, WriteSheet writeSheet, WriteTable writeTable);

    void fill(Object obj, FillConfig fillConfig, WriteSheet writeSheet);

    @Deprecated
    void merge(int i, int i2, int i3, int i4);

    WriteContext writeContext();

    void finish(boolean z);

    void addContent(List list, WriteSheet writeSheet, WriteTable writeTable, String str);
}
