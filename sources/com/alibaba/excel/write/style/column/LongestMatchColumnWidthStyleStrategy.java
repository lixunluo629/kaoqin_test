package com.alibaba.excel.write.style.column;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/style/column/LongestMatchColumnWidthStyleStrategy.class */
public class LongestMatchColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {
    private static final int MAX_COLUMN_WIDTH = 255;
    private static final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap(8);

    @Override // com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead.booleanValue() || !CollectionUtils.isEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = CACHE.get(writeSheetHolder.getSheetNo());
        if (maxColumnWidthMap == null) {
            maxColumnWidthMap = new HashMap(16);
            CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
        }
        Integer columnWidth = dataLength(cellDataList, cell, isHead);
        if (columnWidth.intValue() < 0) {
            return;
        }
        if (columnWidth.intValue() > 255) {
            columnWidth = 255;
        }
        Integer maxColumnWidth = maxColumnWidthMap.get(Integer.valueOf(cell.getColumnIndex()));
        if (maxColumnWidth == null || columnWidth.intValue() > maxColumnWidth.intValue()) {
            maxColumnWidthMap.put(Integer.valueOf(cell.getColumnIndex()), columnWidth);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth.intValue() * 256);
        }
    }

    private Integer dataLength(List<CellData> cellDataList, Cell cell, Boolean isHead) {
        if (isHead.booleanValue()) {
            return Integer.valueOf(cell.getStringCellValue().getBytes().length);
        }
        CellData cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        if (type == null) {
            return -1;
        }
        switch (type) {
            case STRING:
                return Integer.valueOf(cellData.getStringValue().getBytes().length);
            case BOOLEAN:
                return Integer.valueOf(cellData.getBooleanValue().toString().getBytes().length);
            case NUMBER:
                return Integer.valueOf(cellData.getNumberValue().toString().getBytes().length);
            default:
                return -1;
        }
    }
}
