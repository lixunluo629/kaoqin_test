package com.alibaba.excel.context;

import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.excel.read.metadata.holder.ReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/context/AnalysisContext.class */
public interface AnalysisContext {
    void currentSheet(ReadSheet readSheet);

    ReadWorkbookHolder readWorkbookHolder();

    ReadSheetHolder readSheetHolder();

    void readRowHolder(ReadRowHolder readRowHolder);

    ReadRowHolder readRowHolder();

    ReadHolder currentReadHolder();

    Object getCustom();

    @Deprecated
    Sheet getCurrentSheet();

    @Deprecated
    ExcelTypeEnum getExcelType();

    @Deprecated
    InputStream getInputStream();

    @Deprecated
    Integer getCurrentRowNum();

    @Deprecated
    Integer getTotalCount();

    @Deprecated
    Object getCurrentRowAnalysisResult();

    @Deprecated
    void interrupt();
}
