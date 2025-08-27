package com.alibaba.excel.analysis.v07.handlers;

import com.alibaba.excel.analysis.v07.XlsxCellHandler;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.alibaba.excel.context.AnalysisContext;
import org.xml.sax.Attributes;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/handlers/CountRowCellHandler.class */
public class CountRowCellHandler implements XlsxCellHandler {
    private final AnalysisContext analysisContext;

    public CountRowCellHandler(AnalysisContext analysisContext) {
        this.analysisContext = analysisContext;
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public boolean support(String name) {
        return ExcelXmlConstants.DIMENSION.equals(name);
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void startHandle(String name, Attributes attributes) {
        String d = attributes.getValue("ref");
        String totalStr = d.substring(d.indexOf(":") + 1, d.length());
        String c = totalStr.toUpperCase().replaceAll("[A-Z]", "");
        this.analysisContext.readSheetHolder().setApproximateTotalRowNumber(Integer.valueOf(Integer.parseInt(c)));
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void endHandle(String name) {
    }
}
