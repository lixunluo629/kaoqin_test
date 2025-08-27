package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.metadata.CellData;
import java.math.BigDecimal;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/NumberRecordHandler.class */
public class NumberRecordHandler extends AbstractXlsRecordHandler {
    private FormatTrackingHSSFListener formatListener;

    public NumberRecordHandler(FormatTrackingHSSFListener formatListener) {
        this.formatListener = formatListener;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 515 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        NumberRecord numrec = (NumberRecord) record;
        this.row = numrec.getRow();
        this.column = numrec.getColumn();
        this.cellData = new CellData(BigDecimal.valueOf(numrec.getValue()));
        this.cellData.setDataFormat(Integer.valueOf(this.formatListener.getFormatIndex(numrec)));
        this.cellData.setDataFormatString(this.formatListener.getFormatString(numrec));
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
