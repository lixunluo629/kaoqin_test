package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/MissingCellDummyRecordHandler.class */
public class MissingCellDummyRecordHandler extends AbstractXlsRecordHandler {
    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return record instanceof MissingCellDummyRecord;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        MissingCellDummyRecord mcdr = (MissingCellDummyRecord) record;
        this.row = mcdr.getRow();
        this.column = mcdr.getColumn();
        this.cellData = new CellData(CellDataTypeEnum.EMPTY);
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 1;
    }
}
