package com.alibaba.excel.metadata;

import com.alibaba.excel.annotation.ExcelIgnore;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;

@Deprecated
/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/metadata/BaseRowModel.class */
public class BaseRowModel {

    @ExcelIgnore
    private Map<Integer, CellStyle> cellStyleMap = new HashMap();

    public void addStyle(Integer row, CellStyle cellStyle) {
        this.cellStyleMap.put(row, cellStyle);
    }

    public CellStyle getStyle(Integer row) {
        return this.cellStyleMap.get(row);
    }

    public Map<Integer, CellStyle> getCellStyleMap() {
        return this.cellStyleMap;
    }

    public void setCellStyleMap(Map<Integer, CellStyle> cellStyleMap) {
        this.cellStyleMap = cellStyleMap;
    }
}
