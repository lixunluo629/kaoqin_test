package com.alibaba.excel.analysis.v03;

import com.alibaba.excel.metadata.CellData;
import org.apache.poi.hssf.record.Record;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/XlsRecordHandler.class */
public interface XlsRecordHandler extends Comparable<XlsRecordHandler> {
    boolean support(Record record);

    void init();

    void processRecord(Record record);

    int getRow();

    int getColumn();

    CellData getCellData();

    int getOrder();
}
