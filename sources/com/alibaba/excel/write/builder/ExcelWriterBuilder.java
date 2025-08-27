package com.alibaba.excel.write.builder;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/builder/ExcelWriterBuilder.class */
public class ExcelWriterBuilder {
    private WriteWorkbook writeWorkbook = new WriteWorkbook();

    public ExcelWriterBuilder relativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.writeWorkbook.setRelativeHeadRowIndex(relativeHeadRowIndex);
        return this;
    }

    public ExcelWriterBuilder head(List<List<String>> head) {
        this.writeWorkbook.setHead(head);
        return this;
    }

    public ExcelWriterBuilder head(Class clazz) {
        this.writeWorkbook.setClazz(clazz);
        return this;
    }

    public ExcelWriterBuilder needHead(Boolean needHead) {
        this.writeWorkbook.setNeedHead(needHead);
        return this;
    }

    public ExcelWriterBuilder autoCloseStream(Boolean autoCloseStream) {
        this.writeWorkbook.setAutoCloseStream(autoCloseStream);
        return this;
    }

    public ExcelWriterBuilder useDefaultStyle(Boolean useDefaultStyle) {
        this.writeWorkbook.setUseDefaultStyle(useDefaultStyle);
        return this;
    }

    public ExcelWriterBuilder automaticMergeHead(Boolean automaticMergeHead) {
        this.writeWorkbook.setAutomaticMergeHead(automaticMergeHead);
        return this;
    }

    public ExcelWriterBuilder password(String password) {
        this.writeWorkbook.setPassword(password);
        return this;
    }

    public ExcelWriterBuilder inMemory(Boolean inMemory) {
        this.writeWorkbook.setInMemory(inMemory);
        return this;
    }

    public ExcelWriterBuilder excludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        this.writeWorkbook.setExcludeColumnIndexes(excludeColumnIndexes);
        return this;
    }

    public ExcelWriterBuilder excludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.writeWorkbook.setExcludeColumnFiledNames(excludeColumnFiledNames);
        return this;
    }

    public ExcelWriterBuilder includeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        this.writeWorkbook.setIncludeColumnIndexes(includeColumnIndexes);
        return this;
    }

    public ExcelWriterBuilder includeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.writeWorkbook.setIncludeColumnFiledNames(includeColumnFiledNames);
        return this;
    }

    public ExcelWriterBuilder writeExcelOnException(Boolean writeExcelOnException) {
        this.writeWorkbook.setWriteExcelOnException(writeExcelOnException);
        return this;
    }

    @Deprecated
    public ExcelWriterBuilder convertAllFiled(Boolean convertAllFiled) {
        this.writeWorkbook.setConvertAllFiled(convertAllFiled);
        return this;
    }

    public ExcelWriterBuilder registerConverter(Converter converter) {
        if (this.writeWorkbook.getCustomConverterList() == null) {
            this.writeWorkbook.setCustomConverterList(new ArrayList());
        }
        this.writeWorkbook.getCustomConverterList().add(converter);
        return this;
    }

    public ExcelWriterBuilder registerWriteHandler(WriteHandler writeHandler) {
        if (this.writeWorkbook.getCustomWriteHandlerList() == null) {
            this.writeWorkbook.setCustomWriteHandlerList(new ArrayList());
        }
        this.writeWorkbook.getCustomWriteHandlerList().add(writeHandler);
        return this;
    }

    public ExcelWriterBuilder excelType(ExcelTypeEnum excelType) {
        this.writeWorkbook.setExcelType(excelType);
        return this;
    }

    public ExcelWriterBuilder file(OutputStream outputStream) {
        this.writeWorkbook.setOutputStream(outputStream);
        return this;
    }

    public ExcelWriterBuilder file(File outputFile) {
        this.writeWorkbook.setFile(outputFile);
        return this;
    }

    public ExcelWriterBuilder file(String outputPathName) {
        return file(new File(outputPathName));
    }

    public ExcelWriterBuilder withTemplate(InputStream templateInputStream) {
        this.writeWorkbook.setTemplateInputStream(templateInputStream);
        return this;
    }

    public ExcelWriterBuilder withTemplate(File templateFile) {
        this.writeWorkbook.setTemplateFile(templateFile);
        return this;
    }

    public ExcelWriterBuilder withTemplate(String pathName) {
        return withTemplate(new File(pathName));
    }

    @Deprecated
    public ExcelWriterBuilder registerWriteHandler(com.alibaba.excel.event.WriteHandler writeHandler) {
        this.writeWorkbook.setWriteHandler(writeHandler);
        return this;
    }

    public ExcelWriter build() {
        return new ExcelWriter(this.writeWorkbook);
    }

    public ExcelWriterSheetBuilder sheet() {
        return sheet(null, null);
    }

    public ExcelWriterSheetBuilder sheet(Integer sheetNo) {
        return sheet(sheetNo, null);
    }

    public ExcelWriterSheetBuilder sheet(String sheetName) {
        return sheet(null, sheetName);
    }

    public ExcelWriterSheetBuilder sheet(Integer sheetNo, String sheetName) {
        ExcelWriter excelWriter = build();
        ExcelWriterSheetBuilder excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
        if (sheetNo != null) {
            excelWriterSheetBuilder.sheetNo(sheetNo);
        }
        if (sheetName != null) {
            excelWriterSheetBuilder.sheetName(sheetName);
        }
        return excelWriterSheetBuilder;
    }
}
