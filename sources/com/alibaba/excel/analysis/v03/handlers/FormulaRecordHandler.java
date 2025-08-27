package com.alibaba.excel.analysis.v03.handlers;

import com.alibaba.excel.analysis.v03.AbstractXlsRecordHandler;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import java.math.BigDecimal;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v03/handlers/FormulaRecordHandler.class */
public class FormulaRecordHandler extends AbstractXlsRecordHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) FormulaRecordHandler.class);
    private static final String ERROR = "#VALUE!";
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;
    private CellData tempCellData;
    private FormatTrackingHSSFListener formatListener;
    private HSSFWorkbook stubWorkbook;

    public FormulaRecordHandler(HSSFWorkbook stubWorkbook, FormatTrackingHSSFListener formatListener) {
        this.stubWorkbook = stubWorkbook;
        this.formatListener = formatListener;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public boolean support(Record record) {
        return 6 == record.getSid() || 519 == record.getSid();
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void processRecord(Record record) {
        if (record.getSid() == 6) {
            FormulaRecord frec = (FormulaRecord) record;
            this.row = frec.getRow();
            this.column = frec.getColumn();
            CellType cellType = CellType.forInt(frec.getCachedResultType());
            String formulaValue = null;
            try {
                formulaValue = HSSFFormulaParser.toFormulaString(this.stubWorkbook, frec.getParsedExpression());
            } catch (Exception e) {
                LOGGER.warn("Get formula value error.{}", e.getMessage());
            }
            switch (cellType) {
                case STRING:
                    this.outputNextStringRecord = true;
                    this.nextRow = frec.getRow();
                    this.nextColumn = frec.getColumn();
                    this.tempCellData = new CellData(CellDataTypeEnum.STRING);
                    this.tempCellData.setFormula(Boolean.TRUE);
                    this.tempCellData.setFormulaValue(formulaValue);
                    break;
                case NUMERIC:
                    this.cellData = new CellData(BigDecimal.valueOf(frec.getValue()));
                    this.cellData.setFormula(Boolean.TRUE);
                    this.cellData.setFormulaValue(formulaValue);
                    break;
                case ERROR:
                    this.cellData = new CellData(CellDataTypeEnum.ERROR);
                    this.cellData.setStringValue(ERROR);
                    this.cellData.setFormula(Boolean.TRUE);
                    this.cellData.setFormulaValue(formulaValue);
                    break;
                case BOOLEAN:
                    this.cellData = new CellData(Boolean.valueOf(frec.getCachedBooleanValue()));
                    this.cellData.setFormula(Boolean.TRUE);
                    this.cellData.setFormulaValue(formulaValue);
                    break;
                default:
                    this.cellData = new CellData(CellDataTypeEnum.EMPTY);
                    this.cellData.setFormula(Boolean.TRUE);
                    this.cellData.setFormulaValue(formulaValue);
                    break;
            }
        }
        if (record.getSid() == 519 && this.outputNextStringRecord) {
            StringRecord srec = (StringRecord) record;
            this.cellData = this.tempCellData;
            this.cellData.setStringValue(srec.getString());
            this.row = this.nextRow;
            this.column = this.nextColumn;
            this.outputNextStringRecord = false;
            this.tempCellData = null;
        }
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public void init() {
        this.nextRow = 0;
        this.nextColumn = 0;
    }

    @Override // com.alibaba.excel.analysis.v03.XlsRecordHandler
    public int getOrder() {
        return 0;
    }
}
