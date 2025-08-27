package com.alibaba.excel.write.builder;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/builder/ExcelWriterTableBuilder.class */
public class ExcelWriterTableBuilder {
    private ExcelWriter excelWriter;
    private WriteSheet writeSheet;
    private WriteTable writeTable = new WriteTable();

    public ExcelWriterTableBuilder() {
    }

    public ExcelWriterTableBuilder(ExcelWriter excelWriter, WriteSheet writeSheet) {
        this.excelWriter = excelWriter;
        this.writeSheet = writeSheet;
    }

    public ExcelWriterTableBuilder relativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.writeTable.setRelativeHeadRowIndex(relativeHeadRowIndex);
        return this;
    }

    public ExcelWriterTableBuilder head(List<List<String>> head) {
        this.writeTable.setHead(head);
        return this;
    }

    public ExcelWriterTableBuilder head(Class clazz) {
        this.writeTable.setClazz(clazz);
        return this;
    }

    public ExcelWriterTableBuilder needHead(Boolean needHead) {
        this.writeTable.setNeedHead(needHead);
        return this;
    }

    public ExcelWriterTableBuilder useDefaultStyle(Boolean useDefaultStyle) {
        this.writeTable.setUseDefaultStyle(useDefaultStyle);
        return this;
    }

    public ExcelWriterTableBuilder automaticMergeHead(Boolean automaticMergeHead) {
        this.writeTable.setAutomaticMergeHead(automaticMergeHead);
        return this;
    }

    public ExcelWriterTableBuilder registerConverter(Converter converter) {
        if (this.writeTable.getCustomConverterList() == null) {
            this.writeTable.setCustomConverterList(new ArrayList());
        }
        this.writeTable.getCustomConverterList().add(converter);
        return this;
    }

    public ExcelWriterTableBuilder registerWriteHandler(WriteHandler writeHandler) {
        if (this.writeTable.getCustomWriteHandlerList() == null) {
            this.writeTable.setCustomWriteHandlerList(new ArrayList());
        }
        this.writeTable.getCustomWriteHandlerList().add(writeHandler);
        return this;
    }

    public ExcelWriterTableBuilder tableNo(Integer tableNo) {
        this.writeTable.setTableNo(tableNo);
        return this;
    }

    public ExcelWriterTableBuilder excludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        this.writeTable.setExcludeColumnIndexes(excludeColumnIndexes);
        return this;
    }

    public ExcelWriterTableBuilder excludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.writeTable.setExcludeColumnFiledNames(excludeColumnFiledNames);
        return this;
    }

    public ExcelWriterTableBuilder includeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        this.writeTable.setIncludeColumnIndexes(includeColumnIndexes);
        return this;
    }

    public ExcelWriterTableBuilder includeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.writeSheet.setIncludeColumnFiledNames(includeColumnFiledNames);
        return this;
    }

    public WriteTable build() {
        return this.writeTable;
    }

    public void doWrite(List data) {
        if (this.excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet().table()' to call this method");
        }
        this.excelWriter.write(data, this.writeSheet, build());
        this.excelWriter.finish();
    }
}
