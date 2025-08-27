package com.alibaba.excel.read.builder;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.util.ArrayList;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/builder/ExcelReaderSheetBuilder.class */
public class ExcelReaderSheetBuilder {
    private ExcelReader excelReader;
    private ReadSheet readSheet = new ReadSheet();

    public ExcelReaderSheetBuilder() {
    }

    public ExcelReaderSheetBuilder(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public ExcelReaderSheetBuilder sheetNo(Integer sheetNo) {
        this.readSheet.setSheetNo(sheetNo);
        return this;
    }

    public ExcelReaderSheetBuilder sheetName(String sheetName) {
        this.readSheet.setSheetName(sheetName);
        return this;
    }

    public ExcelReaderSheetBuilder headRowNumber(Integer headRowNumber) {
        this.readSheet.setHeadRowNumber(headRowNumber);
        return this;
    }

    public ExcelReaderSheetBuilder head(List<List<String>> head) {
        this.readSheet.setHead(head);
        return this;
    }

    public ExcelReaderSheetBuilder head(Class clazz) {
        this.readSheet.setClazz(clazz);
        return this;
    }

    public ExcelReaderSheetBuilder registerConverter(Converter converter) {
        if (this.readSheet.getCustomConverterList() == null) {
            this.readSheet.setCustomConverterList(new ArrayList());
        }
        this.readSheet.getCustomConverterList().add(converter);
        return this;
    }

    public ExcelReaderSheetBuilder registerReadListener(ReadListener readListener) {
        if (this.readSheet.getCustomReadListenerList() == null) {
            this.readSheet.setCustomReadListenerList(new ArrayList());
        }
        this.readSheet.getCustomReadListenerList().add(readListener);
        return this;
    }

    public ExcelReaderSheetBuilder use1904windowing(Boolean use1904windowing) {
        this.readSheet.setUse1904windowing(use1904windowing);
        return this;
    }

    public ExcelReaderSheetBuilder autoTrim(Boolean autoTrim) {
        this.readSheet.setAutoTrim(autoTrim);
        return this;
    }

    public ReadSheet build() {
        return this.readSheet;
    }

    public void doRead() {
        if (this.excelReader == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.read().sheet()' to call this method");
        }
        this.excelReader.read(build());
        this.excelReader.finish();
    }

    public <T> List<T> doReadSync() {
        if (this.excelReader == null) {
            throw new ExcelAnalysisException("Must use 'EasyExcelFactory.read().sheet()' to call this method");
        }
        SyncReadListener syncReadListener = new SyncReadListener();
        registerReadListener(syncReadListener);
        this.excelReader.read(build());
        this.excelReader.finish();
        return (List<T>) syncReadListener.getList();
    }
}
