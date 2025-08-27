package com.alibaba.excel;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.parameter.GenerateParam;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.ExcelBuilder;
import com.alibaba.excel.write.ExcelBuilderImpl;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/ExcelWriter.class */
public class ExcelWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ExcelWriter.class);
    private ExcelBuilder excelBuilder;

    public ExcelWriter(WriteWorkbook writeWorkbook) {
        this.excelBuilder = new ExcelBuilderImpl(writeWorkbook);
    }

    @Deprecated
    public ExcelWriter(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        this(outputStream, typeEnum, true);
    }

    @Deprecated
    public ExcelWriter(OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        this(null, outputStream, typeEnum, Boolean.valueOf(needHead), null);
    }

    @Deprecated
    public ExcelWriter(InputStream templateInputStream, OutputStream outputStream, ExcelTypeEnum typeEnum, Boolean needHead) {
        this(templateInputStream, outputStream, typeEnum, needHead, null);
    }

    @Deprecated
    public ExcelWriter(InputStream templateInputStream, OutputStream outputStream, ExcelTypeEnum typeEnum, Boolean needHead, WriteHandler writeHandler) {
        List<WriteHandler> customWriteHandlerList = new ArrayList<>();
        customWriteHandlerList.add(writeHandler);
        WriteWorkbook writeWorkbook = new WriteWorkbook();
        writeWorkbook.setTemplateInputStream(templateInputStream);
        writeWorkbook.setOutputStream(outputStream);
        writeWorkbook.setExcelType(typeEnum);
        writeWorkbook.setNeedHead(needHead);
        writeWorkbook.setCustomWriteHandlerList(customWriteHandlerList);
        this.excelBuilder = new ExcelBuilderImpl(writeWorkbook);
    }

    @Deprecated
    public ExcelWriter(GenerateParam generateParam) {
        this(generateParam.getOutputStream(), generateParam.getType(), true);
    }

    public ExcelWriter write(List data, WriteSheet writeSheet) {
        return write(data, writeSheet, (WriteTable) null);
    }

    public ExcelWriter write(List data, WriteSheet writeSheet, WriteTable writeTable) {
        this.excelBuilder.addContent(data, writeSheet, writeTable);
        return this;
    }

    public ExcelWriter fill(Object data, WriteSheet writeSheet) {
        return fill(data, null, writeSheet);
    }

    public ExcelWriter fill(Object data, FillConfig fillConfig, WriteSheet writeSheet) {
        this.excelBuilder.fill(data, fillConfig, writeSheet);
        return this;
    }

    @Deprecated
    public ExcelWriter write(List data, Sheet sheet) {
        return write(data, sheet, (Table) null);
    }

    @Deprecated
    public ExcelWriter write(List data, Sheet sheet, Table table) {
        WriteSheet writeSheet = null;
        if (sheet != null) {
            writeSheet = new WriteSheet();
            writeSheet.setSheetNo(Integer.valueOf(sheet.getSheetNo() - 1));
            writeSheet.setSheetName(sheet.getSheetName());
            writeSheet.setClazz(sheet.getClazz());
            writeSheet.setHead(sheet.getHead());
            writeSheet.setTableStyle(sheet.getTableStyle());
            writeSheet.setRelativeHeadRowIndex(Integer.valueOf(sheet.getStartRow()));
            writeSheet.setColumnWidthMap(sheet.getColumnWidthMap());
        }
        WriteTable writeTable = null;
        if (table != null) {
            writeTable = new WriteTable();
            writeTable.setTableNo(Integer.valueOf(table.getTableNo()));
            writeTable.setClazz(table.getClazz());
            writeTable.setHead(table.getHead());
            writeTable.setTableStyle(table.getTableStyle());
        }
        return write(data, writeSheet, writeTable);
    }

    @Deprecated
    public ExcelWriter write0(List data, Sheet sheet) {
        return write(data, sheet, (Table) null);
    }

    @Deprecated
    public ExcelWriter write0(List data, Sheet sheet, Table table) {
        return write(data, sheet, table);
    }

    @Deprecated
    public ExcelWriter write1(List data, Sheet sheet) {
        return write(data, sheet, (Table) null);
    }

    @Deprecated
    public ExcelWriter write1(List data, Sheet sheet, Table table) {
        return write(data, sheet, table);
    }

    @Deprecated
    public ExcelWriter merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        this.excelBuilder.merge(firstRow, lastRow, firstCol, lastCol);
        return this;
    }

    public void finish() {
        this.excelBuilder.finish(false);
    }

    protected void finalize() {
        try {
            finish();
        } catch (Throwable e) {
            LOGGER.warn("Destroy object failed", e);
        }
    }

    public WriteContext writeContext() {
        return this.excelBuilder.writeContext();
    }
}
