package com.alibaba.excel.write.executor;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.util.WriteHandlerUtils;
import com.alibaba.excel.write.metadata.holder.WriteHolder;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.beans.BeanMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/executor/ExcelWriteAddExecutor.class */
public class ExcelWriteAddExecutor extends AbstractExcelWriteExecutor {
    public ExcelWriteAddExecutor(WriteContext writeContext) {
        super(writeContext);
    }

    public void add(List data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        WriteSheetHolder writeSheetHolder = this.writeContext.writeSheetHolder();
        int newRowIndex = writeSheetHolder.getNewRowIndexAndStartDoWrite();
        if (writeSheetHolder.isNew() && !writeSheetHolder.getExcelWriteHeadProperty().hasHead()) {
            newRowIndex += this.writeContext.currentWriteHolder().relativeHeadRowIndex();
        }
        List<Field> fieldList = new ArrayList<>();
        for (int relativeRowIndex = 0; relativeRowIndex < data.size(); relativeRowIndex++) {
            int n = relativeRowIndex + newRowIndex;
            addOneRowOfDataToExcel(data.get(relativeRowIndex), n, relativeRowIndex, fieldList);
        }
    }

    private void addOneRowOfDataToExcel(Object oneRowData, int n, int relativeRowIndex, List<Field> fieldList) {
        if (oneRowData == null) {
            return;
        }
        WriteHandlerUtils.beforeRowCreate(this.writeContext, Integer.valueOf(n), Integer.valueOf(relativeRowIndex), Boolean.FALSE);
        Row row = WorkBookUtil.createRow(this.writeContext.writeSheetHolder().getSheet(), n);
        WriteHandlerUtils.afterRowCreate(this.writeContext, row, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
        if (oneRowData instanceof List) {
            addBasicTypeToExcel((List) oneRowData, row, relativeRowIndex);
        } else {
            addJavaObjectToExcel(oneRowData, row, relativeRowIndex, fieldList);
        }
        WriteHandlerUtils.afterRowDispose(this.writeContext, row, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
    }

    private void addBasicTypeToExcel(List<Object> oneRowData, Row row, int relativeRowIndex) {
        if (CollectionUtils.isEmpty(oneRowData)) {
            return;
        }
        Map<Integer, Head> headMap = this.writeContext.currentWriteHolder().excelWriteHeadProperty().getHeadMap();
        int dataIndex = 0;
        int cellIndex = 0;
        for (Map.Entry<Integer, Head> entry : headMap.entrySet()) {
            if (dataIndex >= oneRowData.size()) {
                return;
            }
            cellIndex = entry.getKey().intValue();
            Head head = entry.getValue();
            int i = dataIndex;
            dataIndex++;
            doAddBasicTypeToExcel(oneRowData, head, row, relativeRowIndex, i, cellIndex);
        }
        if (dataIndex >= oneRowData.size()) {
            return;
        }
        if (cellIndex != 0) {
            cellIndex++;
        }
        int size = oneRowData.size() - dataIndex;
        for (int i2 = 0; i2 < size; i2++) {
            int i3 = dataIndex;
            dataIndex++;
            int i4 = cellIndex;
            cellIndex++;
            doAddBasicTypeToExcel(oneRowData, null, row, relativeRowIndex, i3, i4);
        }
    }

    private void doAddBasicTypeToExcel(List<Object> oneRowData, Head head, Row row, int relativeRowIndex, int dataIndex, int cellIndex) {
        if (this.writeContext.currentWriteHolder().ignore(null, Integer.valueOf(cellIndex))) {
            return;
        }
        WriteHandlerUtils.beforeCellCreate(this.writeContext, row, head, Integer.valueOf(cellIndex), Integer.valueOf(relativeRowIndex), Boolean.FALSE);
        Cell cell = WorkBookUtil.createCell(row, cellIndex);
        WriteHandlerUtils.afterCellCreate(this.writeContext, cell, head, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
        Object value = oneRowData.get(dataIndex);
        CellData cellData = converterAndSet(this.writeContext.currentWriteHolder(), value == null ? null : value.getClass(), cell, value, null);
        WriteHandlerUtils.afterCellDispose(this.writeContext, cellData, cell, head, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
    }

    private void addJavaObjectToExcel(Object oneRowData, Row row, int relativeRowIndex, List<Field> fieldList) {
        WriteHolder currentWriteHolder = this.writeContext.currentWriteHolder();
        BeanMap beanMap = BeanMap.create(oneRowData);
        Set<String> beanMapHandledSet = new HashSet<>();
        int cellIndex = 0;
        if (HeadKindEnum.CLASS.equals(this.writeContext.currentWriteHolder().excelWriteHeadProperty().getHeadKind())) {
            Map<Integer, Head> headMap = this.writeContext.currentWriteHolder().excelWriteHeadProperty().getHeadMap();
            Map<Integer, ExcelContentProperty> contentPropertyMap = this.writeContext.currentWriteHolder().excelWriteHeadProperty().getContentPropertyMap();
            for (Map.Entry<Integer, ExcelContentProperty> entry : contentPropertyMap.entrySet()) {
                cellIndex = entry.getKey().intValue();
                ExcelContentProperty excelContentProperty = entry.getValue();
                String name = excelContentProperty.getField().getName();
                if (!this.writeContext.currentWriteHolder().ignore(name, Integer.valueOf(cellIndex)) && beanMap.containsKey(name)) {
                    Head head = headMap.get(Integer.valueOf(cellIndex));
                    WriteHandlerUtils.beforeCellCreate(this.writeContext, row, head, Integer.valueOf(cellIndex), Integer.valueOf(relativeRowIndex), Boolean.FALSE);
                    Cell cell = WorkBookUtil.createCell(row, cellIndex);
                    WriteHandlerUtils.afterCellCreate(this.writeContext, cell, head, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
                    CellData cellData = converterAndSet(currentWriteHolder, excelContentProperty.getField().getType(), cell, beanMap.get(name), excelContentProperty);
                    WriteHandlerUtils.afterCellDispose(this.writeContext, cellData, cell, head, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
                    beanMapHandledSet.add(name);
                }
            }
        }
        if (beanMapHandledSet.size() == beanMap.size()) {
            return;
        }
        if (cellIndex != 0) {
            cellIndex++;
        }
        Map<String, Field> ignoreMap = this.writeContext.currentWriteHolder().excelWriteHeadProperty().getIgnoreMap();
        initFieldList(oneRowData.getClass(), fieldList);
        for (Field field : fieldList) {
            String filedName = field.getName();
            boolean uselessData = !beanMap.containsKey(filedName) || beanMapHandledSet.contains(filedName) || ignoreMap.containsKey(filedName) || this.writeContext.currentWriteHolder().ignore(filedName, Integer.valueOf(cellIndex));
            if (!uselessData) {
                Object value = beanMap.get(filedName);
                WriteHandlerUtils.beforeCellCreate(this.writeContext, row, null, Integer.valueOf(cellIndex), Integer.valueOf(relativeRowIndex), Boolean.FALSE);
                int i = cellIndex;
                cellIndex++;
                Cell cell2 = WorkBookUtil.createCell(row, i);
                WriteHandlerUtils.afterCellCreate(this.writeContext, cell2, null, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
                CellData cellData2 = converterAndSet(currentWriteHolder, value == null ? null : value.getClass(), cell2, value, null);
                WriteHandlerUtils.afterCellDispose(this.writeContext, cellData2, cell2, (Head) null, Integer.valueOf(relativeRowIndex), Boolean.FALSE);
            }
        }
    }

    private void initFieldList(Class clazz, List<Field> fieldList) {
        if (!fieldList.isEmpty()) {
            return;
        }
        ClassUtils.declaredFields(clazz, fieldList, this.writeContext.writeWorkbookHolder().getWriteWorkbook().getConvertAllFiled());
    }
}
