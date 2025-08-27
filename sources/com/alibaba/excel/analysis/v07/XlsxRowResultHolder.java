package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.metadata.CellData;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/XlsxRowResultHolder.class */
public interface XlsxRowResultHolder {
    void clearResult();

    void appendCurrentCellValue(char[] cArr, int i, int i2);

    Map<Integer, CellData> getCurRowContent();
}
