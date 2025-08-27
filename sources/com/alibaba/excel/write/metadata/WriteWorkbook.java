package com.alibaba.excel.write.metadata;

import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/WriteWorkbook.class */
public class WriteWorkbook extends WriteBasicParameter {
    private ExcelTypeEnum excelType;
    private File file;
    private OutputStream outputStream;
    private InputStream templateInputStream;
    private File templateFile;
    private Boolean autoCloseStream;
    private Boolean mandatoryUseInputStream;
    private String password;
    private Boolean inMemory;
    private Boolean writeExcelOnException;

    @Deprecated
    private Boolean convertAllFiled;

    @Deprecated
    private WriteHandler writeHandler;

    public ExcelTypeEnum getExcelType() {
        return this.excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public InputStream getTemplateInputStream() {
        return this.templateInputStream;
    }

    public void setTemplateInputStream(InputStream templateInputStream) {
        this.templateInputStream = templateInputStream;
    }

    public File getTemplateFile() {
        return this.templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public Boolean getAutoCloseStream() {
        return this.autoCloseStream;
    }

    public void setAutoCloseStream(Boolean autoCloseStream) {
        this.autoCloseStream = autoCloseStream;
    }

    public Boolean getMandatoryUseInputStream() {
        return this.mandatoryUseInputStream;
    }

    public void setMandatoryUseInputStream(Boolean mandatoryUseInputStream) {
        this.mandatoryUseInputStream = mandatoryUseInputStream;
    }

    public Boolean getConvertAllFiled() {
        return this.convertAllFiled;
    }

    public void setConvertAllFiled(Boolean convertAllFiled) {
        this.convertAllFiled = convertAllFiled;
    }

    public WriteHandler getWriteHandler() {
        return this.writeHandler;
    }

    public void setWriteHandler(WriteHandler writeHandler) {
        this.writeHandler = writeHandler;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getInMemory() {
        return this.inMemory;
    }

    public void setInMemory(Boolean inMemory) {
        this.inMemory = inMemory;
    }

    public Boolean getWriteExcelOnException() {
        return this.writeExcelOnException;
    }

    public void setWriteExcelOnException(Boolean writeExcelOnException) {
        this.writeExcelOnException = writeExcelOnException;
    }
}
