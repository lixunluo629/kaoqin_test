package com.alibaba.excel.analysis.v07.handlers;

import com.alibaba.excel.analysis.v07.XlsxCellHandler;
import com.alibaba.excel.analysis.v07.XlsxRowResultHolder;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.event.EachRowAnalysisFinishEvent;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.PositionUtils;
import org.xml.sax.Attributes;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/handlers/ProcessResultCellHandler.class */
public class ProcessResultCellHandler implements XlsxCellHandler {
    private AnalysisContext analysisContext;
    private XlsxRowResultHolder rowResultHandler;
    private int currentRow = -1;

    public ProcessResultCellHandler(AnalysisContext analysisContext, XlsxRowResultHolder rowResultHandler) {
        this.analysisContext = analysisContext;
        this.rowResultHandler = rowResultHandler;
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public boolean support(String name) {
        return ExcelXmlConstants.ROW_TAG.equals(name);
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void startHandle(String name, Attributes attributes) {
        this.currentRow = PositionUtils.getRowByRowTagt(attributes.getValue(ExcelXmlConstants.POSITION), this.currentRow);
        this.analysisContext.readRowHolder(new ReadRowHolder(Integer.valueOf(this.currentRow), this.analysisContext.readSheetHolder().getGlobalConfiguration()));
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void endHandle(String name) {
        this.analysisContext.readSheetHolder().notifyEndOneRow(new EachRowAnalysisFinishEvent(this.rowResultHandler.getCurRowContent()), this.analysisContext);
        this.rowResultHandler.clearResult();
    }
}
