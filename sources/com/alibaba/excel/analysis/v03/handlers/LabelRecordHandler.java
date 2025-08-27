package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/LabelRecordHandler.class */
public class LabelRecordHandler extends AbstractXlsRecordHandler {
    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 516 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        LabelRecord lrec = (LabelRecord) record;
        this.row = lrec.getRow();
        this.column = lrec.getColumn();
        this.cellData = new CellData(lrec.getValue());
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
