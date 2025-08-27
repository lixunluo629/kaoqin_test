package com.alibaba.excel;

import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.ExcelAnalyserImpl;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.cache.MapCache;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.parameter.AnalysisParam;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/ExcelReader.class */
public class ExcelReader {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ExcelReader.class);
    private ExcelAnalyser excelAnalyser;

    @Deprecated
    public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent, AnalysisEventListener eventListener) {
        this(in, excelTypeEnum, customContent, eventListener, true);
    }

    @Deprecated
    public ExcelReader(InputStream in, Object customContent, AnalysisEventListener eventListener) {
        this(in, customContent, eventListener, true);
    }

    @Deprecated
    public ExcelReader(AnalysisParam param, AnalysisEventListener eventListener) {
        this(param.getIn(), param.getExcelTypeEnum(), param.getCustomContent(), eventListener, true);
    }

    @Deprecated
    public ExcelReader(InputStream in, Object customContent, AnalysisEventListener eventListener, boolean trim) {
        this(in, null, customContent, eventListener, trim);
    }

    @Deprecated
    public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent, AnalysisEventListener eventListener, boolean trim) {
        ReadWorkbook readWorkbook = new ReadWorkbook();
        readWorkbook.setInputStream(in);
        readWorkbook.setExcelType(excelTypeEnum);
        readWorkbook.setCustomObject(customContent);
        if (eventListener != null) {
            List<ReadListener> customReadListenerList = new ArrayList<>();
            customReadListenerList.add(eventListener);
            readWorkbook.setCustomReadListenerList(customReadListenerList);
        }
        readWorkbook.setAutoTrim(Boolean.valueOf(trim));
        readWorkbook.setAutoCloseStream(Boolean.FALSE);
        readWorkbook.setMandatoryUseInputStream(Boolean.TRUE);
        readWorkbook.setReadCache(new MapCache());
        readWorkbook.setConvertAllFiled(Boolean.FALSE);
        readWorkbook.setDefaultReturnMap(Boolean.FALSE);
        this.excelAnalyser = new ExcelAnalyserImpl(readWorkbook);
    }

    public ExcelReader(ReadWorkbook readWorkbook) {
        this.excelAnalyser = new ExcelAnalyserImpl(readWorkbook);
    }

    @Deprecated
    public void read() {
        readAll();
    }

    public void readAll() {
        this.excelAnalyser.analysis(null, Boolean.TRUE);
    }

    public ExcelReader read(ReadSheet... readSheet) {
        return read(Arrays.asList(readSheet));
    }

    public ExcelReader read(List<ReadSheet> readSheetList) {
        this.excelAnalyser.analysis(readSheetList, Boolean.FALSE);
        return this;
    }

    @Deprecated
    public void read(Sheet sheet) {
        ReadSheet readSheet = null;
        if (sheet != null) {
            readSheet = new ReadSheet();
            readSheet.setSheetNo(Integer.valueOf(sheet.getSheetNo() - 1));
            readSheet.setSheetName(sheet.getSheetName());
            readSheet.setClazz(sheet.getClazz());
            readSheet.setHead(sheet.getHead());
            readSheet.setHeadRowNumber(Integer.valueOf(sheet.getHeadLineMun()));
        }
        read(readSheet);
    }

    @Deprecated
    public void read(Sheet sheet, Class clazz) {
        if (sheet != null) {
            sheet.setClazz(clazz);
        }
        read(sheet);
    }

    public AnalysisContext analysisContext() {
        return this.excelAnalyser.analysisContext();
    }

    public ExcelReadExecutor excelExecutor() {
        return this.excelAnalyser.excelExecutor();
    }

    @Deprecated
    public List<Sheet> getSheets() {
        List<ReadSheet> sheetList = excelExecutor().sheetList();
        List<Sheet> sheets = new ArrayList<>();
        if (sheetList == null || sheetList.isEmpty()) {
            return sheets;
        }
        for (ReadSheet readSheet : sheetList) {
            Sheet sheet = new Sheet(readSheet.getSheetNo().intValue() + 1);
            sheet.setSheetName(readSheet.getSheetName());
            sheets.add(sheet);
        }
        return sheets;
    }

    @Deprecated
    public AnalysisContext getAnalysisContext() {
        return analysisContext();
    }

    public void finish() {
        this.excelAnalyser.finish();
    }

    protected void finalize() {
        try {
            finish();
        } catch (Throwable e) {
            LOGGER.warn("Destroy object failed", e);
        }
    }
}
