package com.alibaba.excel.analysis;

import com.alibaba.excel.read.metadata.ReadSheet;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/ExcelReadExecutor.class */
public interface ExcelReadExecutor {
    List<ReadSheet> sheetList();

    void execute(List<ReadSheet> list, Boolean bool);
}
