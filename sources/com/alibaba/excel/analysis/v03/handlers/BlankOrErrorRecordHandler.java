package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/BlankOrErrorRecordHandler.class */
public class BlankOrErrorRecordHandler extends AbstractXlsRecordHandler {
    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 513 == record.getSid() || 517 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        if (record.getSid() == 513) {
            BlankRecord br = (BlankRecord) record;
            this.row = br.getRow();
            this.column = br.getColumn();
            this.cellData = new CellData(CellDataTypeEnum.EMPTY);
            return;
        }
        if (record.getSid() == 517) {
            BoolErrRecord ber = (BoolErrRecord) record;
            this.row = ber.getRow();
            this.column = ber.getColumn();
            this.cellData = new CellData(Boolean.valueOf(ber.getBooleanValue()));
        }
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
