package com.alibaba.excel.context;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.excel.read.metadata.holder.ReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/context/AnalysisContextImpl.class */
public class AnalysisContextImpl implements AnalysisContext {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) AnalysisContextImpl.class);
    private ReadWorkbookHolder readWorkbookHolder;
    private ReadSheetHolder readSheetHolder;
    private ReadRowHolder readRowHolder;
    private ReadHolder currentReadHolder;

    public AnalysisContextImpl(ReadWorkbook readWorkbook) {
        if (readWorkbook == null) {
            throw new IllegalArgumentException("Workbook argument cannot be null");
        }
        this.readWorkbookHolder = new ReadWorkbookHolder(readWorkbook);
        this.currentReadHolder = this.readWorkbookHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialization 'AnalysisContextImpl' complete");
        }
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public void currentSheet(ReadSheet readSheet) {
        this.readSheetHolder = new ReadSheetHolder(readSheet, this.readWorkbookHolder);
        this.currentReadHolder = this.readSheetHolder;
        if (this.readWorkbookHolder.getHasReadSheet().contains(this.readSheetHolder.getSheetNo())) {
            throw new ExcelAnalysisException("Cannot read sheet repeatedly.");
        }
        this.readWorkbookHolder.getHasReadSheet().add(this.readSheetHolder.getSheetNo());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Began to read：{}", this.readSheetHolder);
        }
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public ReadWorkbookHolder readWorkbookHolder() {
        return this.readWorkbookHolder;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public ReadSheetHolder readSheetHolder() {
        return this.readSheetHolder;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public ReadRowHolder readRowHolder() {
        return this.readRowHolder;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public void readRowHolder(ReadRowHolder readRowHolder) {
        this.readRowHolder = readRowHolder;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public ReadHolder currentReadHolder() {
        return this.currentReadHolder;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public Object getCustom() {
        return this.readWorkbookHolder.getCustomObject();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public Sheet getCurrentSheet() {
        Sheet sheet = new Sheet(this.readSheetHolder.getSheetNo().intValue() + 1);
        sheet.setSheetName(this.readSheetHolder.getSheetName());
        sheet.setHead(this.readSheetHolder.getHead());
        sheet.setClazz(this.readSheetHolder.getClazz());
        sheet.setHeadLineMun(this.readSheetHolder.getHeadRowNumber().intValue());
        return sheet;
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public ExcelTypeEnum getExcelType() {
        return this.readWorkbookHolder.getExcelType();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public InputStream getInputStream() {
        return this.readWorkbookHolder.getInputStream();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public Integer getCurrentRowNum() {
        return this.readRowHolder.getRowIndex();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public Integer getTotalCount() {
        return this.readSheetHolder.getTotal();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public Object getCurrentRowAnalysisResult() {
        return this.readRowHolder.getCurrentRowAnalysisResult();
    }

    @Override // com.alibaba.excel.context.AnalysisContext
    public void interrupt() {
        throw new ExcelAnalysisException("interrupt error");
    }
}
