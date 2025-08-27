package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/SstRecordHandler.class */
public class SstRecordHandler extends AbstractXlsRecordHandler {
    private SSTRecord sstRecord;

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 252 == record.getSid() || 253 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        if (record.getSid() == 252) {
            this.sstRecord = (SSTRecord) record;
            return;
        }
        if (record.getSid() == 253) {
            LabelSSTRecord lsrec = (LabelSSTRecord) record;
            this.row = lsrec.getRow();
            this.column = lsrec.getColumn();
            if (this.sstRecord == null) {
                this.cellData = new CellData(CellDataTypeEnum.EMPTY);
            } else {
                this.cellData = new CellData(this.sstRecord.getString(lsrec.getSSTIndex()).toString());
            }
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
