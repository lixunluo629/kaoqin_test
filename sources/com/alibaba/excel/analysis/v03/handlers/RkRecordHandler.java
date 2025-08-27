package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/RkRecordHandler.class */
public class RkRecordHandler extends AbstractXlsRecordHandler {
    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 638 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        RKRecord rkrec = (RKRecord) record;
        this.row = rkrec.getRow();
        this.row = rkrec.getColumn();
        this.cellData = new CellData(CellDataTypeEnum.EMPTY);
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
