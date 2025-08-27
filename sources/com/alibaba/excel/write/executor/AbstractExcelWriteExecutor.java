package com.alibaba.excel.write.executor;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteHolder;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/executor/AbstractExcelWriteExecutor.class */
public abstract class AbstractExcelWriteExecutor implements ExcelWriteExecutor {
    protected WriteContext writeContext;

    public AbstractExcelWriteExecutor(WriteContext writeContext) {
        this.writeContext = writeContext;
    }

    protected CellData converterAndSet(WriteHolder currentWriteHolder, Class clazz, Cell cell, Object value, ExcelContentProperty excelContentProperty) throws FormulaParseException {
        if (value == null) {
            return new CellData(CellDataTypeEnum.EMPTY);
        }
        if ((value instanceof String) && currentWriteHolder.globalConfiguration().getAutoTrim().booleanValue()) {
            value = ((String) value).trim();
        }
        CellData cellData = convert(currentWriteHolder, clazz, cell, value, excelContentProperty);
        if (cellData.getFormula() != null && cellData.getFormula().booleanValue()) {
            cell.setCellFormula(cellData.getFormulaValue());
        }
        if (cellData.getType() == null) {
            cellData.setType(CellDataTypeEnum.EMPTY);
        }
        switch (cellData.getType()) {
            case STRING:
                cell.setCellValue(cellData.getStringValue());
                return cellData;
            case BOOLEAN:
                cell.setCellValue(cellData.getBooleanValue().booleanValue());
                return cellData;
            case NUMBER:
                cell.setCellValue(cellData.getNumberValue().doubleValue());
                return cellData;
            case IMAGE:
                setImageValue(cellData, cell);
                return cellData;
            case EMPTY:
                return cellData;
            default:
                throw new ExcelDataConvertException(Integer.valueOf(cell.getRow().getRowNum()), Integer.valueOf(cell.getColumnIndex()), cellData, excelContentProperty, "Not supported data:" + value + " return type:" + cell.getCellType() + "at row:" + cell.getRow().getRowNum());
        }
    }

    protected CellData convert(WriteHolder currentWriteHolder, Class clazz, Cell cell, Object value, ExcelContentProperty excelContentProperty) {
        if (value == null) {
            return new CellData(CellDataTypeEnum.EMPTY);
        }
        if (value instanceof CellData) {
            CellData cellDataValue = (CellData) value;
            if (cellDataValue.getType() != null) {
                return cellDataValue;
            }
            if (cellDataValue.getData() == null) {
                cellDataValue.setType(CellDataTypeEnum.EMPTY);
                return cellDataValue;
            }
            CellData cellDataReturn = doConvert(currentWriteHolder, cellDataValue.getData().getClass(), cell, cellDataValue.getData(), excelContentProperty);
            if (cellDataValue.getFormula() != null) {
                cellDataReturn.setFormula(cellDataValue.getFormula());
                cellDataReturn.setFormulaValue(cellDataValue.getFormulaValue());
            }
            return cellDataReturn;
        }
        return doConvert(currentWriteHolder, clazz, cell, value, excelContentProperty);
    }

    private CellData doConvert(WriteHolder currentWriteHolder, Class clazz, Cell cell, Object value, ExcelContentProperty excelContentProperty) {
        Converter converter = null;
        if (excelContentProperty != null) {
            converter = excelContentProperty.getConverter();
        }
        if (converter == null) {
            converter = currentWriteHolder.converterMap().get(ConverterKeyBuild.buildKey(clazz));
        }
        if (converter == null) {
            throw new ExcelDataConvertException(Integer.valueOf(cell.getRow().getRowNum()), Integer.valueOf(cell.getColumnIndex()), new CellData(CellDataTypeEnum.EMPTY), excelContentProperty, "Can not find 'Converter' support class " + clazz.getSimpleName() + ".");
        }
        try {
            CellData cellData = converter.convertToExcelData(value, excelContentProperty, currentWriteHolder.globalConfiguration());
            if (cellData == null || cellData.getType() == null) {
                throw new ExcelDataConvertException(Integer.valueOf(cell.getRow().getRowNum()), Integer.valueOf(cell.getColumnIndex()), new CellData(CellDataTypeEnum.EMPTY), excelContentProperty, "Convert data:" + value + " return null,at row:" + cell.getRow().getRowNum());
            }
            return cellData;
        } catch (Exception e) {
            throw new ExcelDataConvertException(Integer.valueOf(cell.getRow().getRowNum()), Integer.valueOf(cell.getColumnIndex()), new CellData(CellDataTypeEnum.EMPTY), excelContentProperty, "Convert data:" + value + " error,at row:" + cell.getRow().getRowNum(), e);
        }
    }

    private void setImageValue(CellData cellData, Cell cell) {
        Sheet sheet = cell.getSheet();
        int index = sheet.getWorkbook().addPicture(cellData.getImageValue(), 6);
        Drawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setDx1(0);
        anchor.setDx2(0);
        anchor.setDy1(0);
        anchor.setDy2(0);
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 1);
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        drawing.createPicture(anchor, index);
    }
}
