package com.alibaba.excel.write.builder;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/builder/ExcelWriterSheetBuilder.class */
public class ExcelWriterSheetBuilder {
    private ExcelWriter excelWriter;
    private WriteSheet writeSheet = new WriteSheet();

    public ExcelWriterSheetBuilder() {
    }

    public ExcelWriterSheetBuilder(ExcelWriter excelWriter) {
        this.excelWriter = excelWriter;
    }

    public ExcelWriterSheetBuilder relativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.writeSheet.setRelativeHeadRowIndex(relativeHeadRowIndex);
        return this;
    }

    public ExcelWriterSheetBuilder head(List<List<String>> head) {
        this.writeSheet.setHead(head);
        return this;
    }

    public ExcelWriterSheetBuilder head(Class clazz) {
        this.writeSheet.setClazz(clazz);
        return this;
    }

    public ExcelWriterSheetBuilder needHead(Boolean needHead) {
        this.writeSheet.setNeedHead(needHead);
        return this;
    }

    public ExcelWriterSheetBuilder useDefaultStyle(Boolean useDefaultStyle) {
        this.writeSheet.setUseDefaultStyle(useDefaultStyle);
        return this;
    }

    public ExcelWriterSheetBuilder automaticMergeHead(Boolean automaticMergeHead) {
        this.writeSheet.setAutomaticMergeHead(automaticMergeHead);
        return this;
    }

    public ExcelWriterSheetBuilder registerConverter(Converter converter) {
        if (this.writeSheet.getCustomConverterList() == null) {
            this.writeSheet.setCustomConverterList(new ArrayList());
        }
        this.writeSheet.getCustomConverterList().add(converter);
        return this;
    }

    public ExcelWriterSheetBuilder registerWriteHandler(WriteHandler writeHandler) {
        if (this.writeSheet.getCustomWriteHandlerList() == null) {
            this.writeSheet.setCustomWriteHandlerList(new ArrayList());
        }
        this.writeSheet.getCustomWriteHandlerList().add(writeHandler);
        return this;
    }

    public ExcelWriterSheetBuilder sheetNo(Integer sheetNo) {
        this.writeSheet.setSheetNo(sheetNo);
        return this;
    }

    public ExcelWriterSheetBuilder sheetName(String sheetName) {
        this.writeSheet.setSheetName(sheetName);
        return this;
    }

    public ExcelWriterSheetBuilder excludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        this.writeSheet.setExcludeColumnIndexes(excludeColumnIndexes);
        return this;
    }

    public ExcelWriterSheetBuilder excludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.writeSheet.setExcludeColumnFiledNames(excludeColumnFiledNames);
        return this;
    }

    public ExcelWriterSheetBuilder includeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        this.writeSheet.setIncludeColumnIndexes(includeColumnIndexes);
        return this;
    }

    public ExcelWriterSheetBuilder includeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.writeSheet.setIncludeColumnFiledNames(includeColumnFiledNames);
        return this;
    }

    public WriteSheet build() {
        return this.writeSheet;
    }

    public void doWrite(List data) {
        if (this.excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet()' to call this method");
        }
        this.excelWriter.write(data, build());
        this.excelWriter.finish();
    }

    public void doFill(Object data) {
        doFill(data, null);
    }

    public void doFill(Object data, FillConfig fillConfig) {
        if (this.excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet()' to call this method");
        }
        this.excelWriter.fill(data, fillConfig, build());
        this.excelWriter.finish();
    }

    public ExcelWriterTableBuilder table() {
        return table(null);
    }

    public ExcelWriterTableBuilder table(Integer tableNo) {
        ExcelWriterTableBuilder excelWriterTableBuilder = new ExcelWriterTableBuilder(this.excelWriter, build());
        if (tableNo != null) {
            excelWriterTableBuilder.tableNo(tableNo);
        }
        return excelWriterTableBuilder;
    }
}
