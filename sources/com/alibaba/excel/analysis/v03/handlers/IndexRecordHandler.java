package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.context.AnalysisContext;
import org.apache.poi.hssf.record.IndexRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/IndexRecordHandler.class */
public class IndexRecordHandler extends AbstractXlsRecordHandler {
    private AnalysisContext context;

    public IndexRecordHandler(AnalysisContext context) {
        this.context = context;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return record instanceof IndexRecord;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        if (this.context.readSheetHolder() == null) {
            return;
        }
        this.context.readSheetHolder().setApproximateTotalRowNumber(Integer.valueOf(((IndexRecord) record).getLastRowAdd1()));
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 1;
    }
}
