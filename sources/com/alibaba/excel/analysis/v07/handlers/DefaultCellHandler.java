package com.alibaba.excel.analysis.v07.handlers;

import com.alibaba.excel.analysis.v07.XlsxCellHandler;
import com.alibaba.excel.analysis.v07.XlsxRowResultHolder;
import com.alibaba.excel.constant.ExcelXmlConstants;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.util.PositionUtils;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/handlers/DefaultCellHandler.class */
public class DefaultCellHandler implements XlsxCellHandler, XlsxRowResultHolder {
    private final AnalysisContext analysisContext;
    private Deque<String> currentTagDeque = new LinkedList();
    private int curCol = -1;
    private Map<Integer, CellData> curRowContent = new LinkedHashMap();
    private CellData currentCellData;
    private StringBuilder dataStringBuilder;
    private StringBuilder formulaStringBuilder;
    private StylesTable stylesTable;

    public DefaultCellHandler(AnalysisContext analysisContext, StylesTable stylesTable) {
        this.analysisContext = analysisContext;
        this.stylesTable = stylesTable;
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxRowResultHolder
    public void clearResult() {
        this.curRowContent = new LinkedHashMap();
        this.curCol = -1;
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public boolean support(String name) {
        return ExcelXmlConstants.CELL_VALUE_TAG.equals(name) || ExcelXmlConstants.CELL_FORMULA_TAG.equals(name) || "t".equals(name) || ExcelXmlConstants.CELL_TAG.equals(name);
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void startHandle(String name, Attributes attributes) throws NumberFormatException {
        this.currentTagDeque.push(name);
        if (ExcelXmlConstants.CELL_TAG.equals(name)) {
            this.curCol = PositionUtils.getCol(attributes.getValue(ExcelXmlConstants.POSITION), this.curCol);
            CellDataTypeEnum type = CellDataTypeEnum.buildFromCellType(attributes.getValue("t"));
            this.currentCellData = new CellData(type);
            this.dataStringBuilder = new StringBuilder();
            String dateFormatIndex = attributes.getValue(ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
            if (dateFormatIndex != null) {
                int dateFormatIndexInteger = Integer.parseInt(dateFormatIndex);
                XSSFCellStyle xssfCellStyle = this.stylesTable.getStyleAt(dateFormatIndexInteger);
                int dataFormat = xssfCellStyle.getDataFormat();
                String dataFormatString = xssfCellStyle.getDataFormatString();
                this.currentCellData.setDataFormat(Integer.valueOf(dataFormat));
                if (dataFormatString == null) {
                    this.currentCellData.setDataFormatString(BuiltinFormats.getBuiltinFormat(dataFormat));
                } else {
                    this.currentCellData.setDataFormatString(dataFormatString);
                }
            }
        }
        if (ExcelXmlConstants.CELL_FORMULA_TAG.equals(name)) {
            this.currentCellData.setFormula(Boolean.TRUE);
            this.formulaStringBuilder = new StringBuilder();
        }
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxCellHandler
    public void endHandle(String name) {
        this.currentTagDeque.pop();
        if (ExcelXmlConstants.CELL_FORMULA_TAG.equals(name)) {
            this.currentCellData.setFormulaValue(this.formulaStringBuilder.toString());
            return;
        }
        if (ExcelXmlConstants.CELL_VALUE_TAG.equals(name) || "t".equals(name)) {
            CellDataTypeEnum oldType = this.currentCellData.getType();
            switch (oldType) {
                case DIRECT_STRING:
                case STRING:
                case ERROR:
                    this.currentCellData.setStringValue(this.dataStringBuilder.toString());
                    break;
                case BOOLEAN:
                    this.currentCellData.setBooleanValue(BooleanUtils.valueOf(this.dataStringBuilder.toString()));
                    break;
                case NUMBER:
                case EMPTY:
                    this.currentCellData.setType(CellDataTypeEnum.NUMBER);
                    this.currentCellData.setNumberValue(new BigDecimal(this.dataStringBuilder.toString()));
                    break;
                default:
                    throw new IllegalStateException("Cannot set values now");
            }
            if (ExcelXmlConstants.CELL_VALUE_TAG.equals(name)) {
                if (this.currentCellData.getType() == CellDataTypeEnum.STRING) {
                    String stringValue = this.analysisContext.readWorkbookHolder().getReadCache().get(Integer.valueOf(this.currentCellData.getStringValue()));
                    if (stringValue != null && this.analysisContext.currentReadHolder().globalConfiguration().getAutoTrim().booleanValue()) {
                        stringValue = stringValue.trim();
                    }
                    this.currentCellData.setStringValue(stringValue);
                } else if (this.currentCellData.getType() == CellDataTypeEnum.DIRECT_STRING) {
                    this.currentCellData.setType(CellDataTypeEnum.STRING);
                }
            }
            if ("t".equals(name)) {
                XSSFRichTextString richTextString = new XSSFRichTextString(this.currentCellData.getStringValue());
                String stringValue2 = richTextString.toString();
                if (stringValue2 != null && this.analysisContext.currentReadHolder().globalConfiguration().getAutoTrim().booleanValue()) {
                    stringValue2 = stringValue2.trim();
                }
                this.currentCellData.setStringValue(stringValue2);
            }
            this.currentCellData.checkEmpty();
            this.curRowContent.put(Integer.valueOf(this.curCol), this.currentCellData);
        }
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxRowResultHolder
    public void appendCurrentCellValue(char[] ch2, int start, int length) {
        String currentTag = this.currentTagDeque.peek();
        if (currentTag == null) {
            return;
        }
        if (ExcelXmlConstants.CELL_FORMULA_TAG.equals(currentTag)) {
            this.formulaStringBuilder.append(ch2, start, length);
        } else {
            if (!ExcelXmlConstants.CELL_VALUE_TAG.equals(currentTag) && !"t".equals(currentTag)) {
                return;
            }
            this.dataStringBuilder.append(ch2, start, length);
        }
    }

    @Override // com.alibaba.excel.analysis.v07.XlsxRowResultHolder
    public Map<Integer, CellData> getCurRowContent() {
        return this.curRowContent;
    }
}
