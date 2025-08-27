package com.alibaba.excel.read.builder;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.cache.ReadCache;
import com.alibaba.excel.cache.selector.ReadCacheSelector;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/builder/ExcelReaderBuilder.class */
public class ExcelReaderBuilder {
    private ReadWorkbook readWorkbook = new ReadWorkbook();

    public ExcelReaderBuilder excelType(ExcelTypeEnum excelType) {
        this.readWorkbook.setExcelType(excelType);
        return this;
    }

    public ExcelReaderBuilder file(InputStream inputStream) {
        this.readWorkbook.setInputStream(inputStream);
        return this;
    }

    public ExcelReaderBuilder file(File file) {
        this.readWorkbook.setFile(file);
        return this;
    }

    public ExcelReaderBuilder file(String pathName) {
        return file(new File(pathName));
    }

    public ExcelReaderBuilder mandatoryUseInputStream(Boolean mandatoryUseInputStream) {
        this.readWorkbook.setMandatoryUseInputStream(mandatoryUseInputStream);
        return this;
    }

    public ExcelReaderBuilder autoCloseStream(Boolean autoCloseStream) {
        this.readWorkbook.setAutoCloseStream(autoCloseStream);
        return this;
    }

    public ExcelReaderBuilder ignoreEmptyRow(Boolean ignoreEmptyRow) {
        this.readWorkbook.setIgnoreEmptyRow(ignoreEmptyRow);
        return this;
    }

    public ExcelReaderBuilder customObject(Object customObject) {
        this.readWorkbook.setCustomObject(customObject);
        return this;
    }

    public ExcelReaderBuilder readCache(ReadCache readCache) {
        this.readWorkbook.setReadCache(readCache);
        return this;
    }

    public ExcelReaderBuilder readCacheSelector(ReadCacheSelector readCacheSelector) {
        this.readWorkbook.setReadCacheSelector(readCacheSelector);
        return this;
    }

    public ExcelReaderBuilder headRowNumber(Integer headRowNumber) {
        this.readWorkbook.setHeadRowNumber(headRowNumber);
        return this;
    }

    public ExcelReaderBuilder head(List<List<String>> head) {
        this.readWorkbook.setHead(head);
        return this;
    }

    public ExcelReaderBuilder head(Class clazz) {
        this.readWorkbook.setClazz(clazz);
        return this;
    }

    public ExcelReaderBuilder registerConverter(Converter converter) {
        if (this.readWorkbook.getCustomConverterList() == null) {
            this.readWorkbook.setCustomConverterList(new ArrayList());
        }
        this.readWorkbook.getCustomConverterList().add(converter);
        return this;
    }

    public ExcelReaderBuilder registerReadListener(ReadListener readListener) {
        if (this.readWorkbook.getCustomReadListenerList() == null) {
            this.readWorkbook.setCustomReadListenerList(new ArrayList());
        }
        this.readWorkbook.getCustomReadListenerList().add(readListener);
        return this;
    }

    public ExcelReaderBuilder use1904windowing(Boolean use1904windowing) {
        this.readWorkbook.setUse1904windowing(use1904windowing);
        return this;
    }

    public ExcelReaderBuilder autoTrim(Boolean autoTrim) {
        this.readWorkbook.setAutoTrim(autoTrim);
        return this;
    }

    public ExcelReaderBuilder password(String password) {
        this.readWorkbook.setPassword(password);
        return this;
    }

    public ExcelReaderBuilder xlsxSAXParserFactoryName(String xlsxSAXParserFactoryName) {
        this.readWorkbook.setXlsxSAXParserFactoryName(xlsxSAXParserFactoryName);
        return this;
    }

    public ExcelReaderBuilder useDefaultListener(Boolean useDefaultListener) {
        this.readWorkbook.setUseDefaultListener(useDefaultListener);
        return this;
    }

    public ExcelReader build() {
        return new ExcelReader(this.readWorkbook);
    }

    public ExcelReader doReadAll() {
        ExcelReader excelReader = build();
        excelReader.readAll();
        excelReader.finish();
        return excelReader;
    }

    public ExcelReaderSheetBuilder sheet() {
        return sheet(null, null);
    }

    public ExcelReaderSheetBuilder sheet(Integer sheetNo) {
        return sheet(sheetNo, null);
    }

    public ExcelReaderSheetBuilder sheet(String sheetName) {
        return sheet(null, sheetName);
    }

    public ExcelReaderSheetBuilder sheet(Integer sheetNo, String sheetName) {
        ExcelReaderSheetBuilder excelReaderSheetBuilder = new ExcelReaderSheetBuilder(build());
        if (sheetNo != null) {
            excelReaderSheetBuilder.sheetNo(sheetNo);
        }
        if (sheetName != null) {
            excelReaderSheetBuilder.sheetName(sheetName);
        }
        return excelReaderSheetBuilder;
    }
}
