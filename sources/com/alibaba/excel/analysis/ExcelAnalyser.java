package com.alibaba.excel.analysis;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/ExcelAnalyser.class */
public interface ExcelAnalyser {
    void analysis(List<ReadSheet> list, Boolean bool);

    void finish();

    ExcelReadExecutor excelExecutor();

    AnalysisContext analysisContext();
}
