package com.alibaba.excel.exception;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/exception/ExcelDataConvertException.class */
public class ExcelDataConvertException extends RuntimeException {
    private Integer rowIndex;
    private Integer columnIndex;
    private CellData cellData;
    private ExcelContentProperty excelContentProperty;

    public ExcelDataConvertException(Integer rowIndex, Integer columnIndex, CellData cellData, ExcelContentProperty excelContentProperty, String message) {
        super(message);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.cellData = cellData;
        this.excelContentProperty = excelContentProperty;
    }

    public ExcelDataConvertException(Integer rowIndex, Integer columnIndex, CellData cellData, ExcelContentProperty excelContentProperty, String message, Throwable cause) {
        super(message, cause);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.cellData = cellData;
        this.excelContentProperty = excelContentProperty;
    }

    public Integer getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return this.columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ExcelContentProperty getExcelContentProperty() {
        return this.excelContentProperty;
    }

    public void setExcelContentProperty(ExcelContentProperty excelContentProperty) {
        this.excelContentProperty = excelContentProperty;
    }

    public CellData getCellData() {
        return this.cellData;
    }

    public void setCellData(CellData cellData) {
        this.cellData = cellData;
    }
}
