package com.alibaba.excel.read.metadata;

import com.alibaba.excel.cache.ReadCache;
import com.alibaba.excel.cache.selector.ReadCacheSelector;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.File;
import java.io.InputStream;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/ReadWorkbook.class */
public class ReadWorkbook extends ReadBasicParameter {
    private ExcelTypeEnum excelType;
    private InputStream inputStream;
    private File file;
    private Boolean mandatoryUseInputStream;
    private Boolean autoCloseStream;
    private Object customObject;
    private ReadCache readCache;
    private Boolean ignoreEmptyRow;
    private ReadCacheSelector readCacheSelector;
    private String password;
    private String xlsxSAXParserFactoryName;
    private Boolean useDefaultListener;

    @Deprecated
    private Boolean convertAllFiled;

    @Deprecated
    private Boolean defaultReturnMap;

    public ExcelTypeEnum getExcelType() {
        return this.excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean getAutoCloseStream() {
        return this.autoCloseStream;
    }

    public void setAutoCloseStream(Boolean autoCloseStream) {
        this.autoCloseStream = autoCloseStream;
    }

    public Object getCustomObject() {
        return this.customObject;
    }

    public void setCustomObject(Object customObject) {
        this.customObject = customObject;
    }

    public Boolean getMandatoryUseInputStream() {
        return this.mandatoryUseInputStream;
    }

    public void setMandatoryUseInputStream(Boolean mandatoryUseInputStream) {
        this.mandatoryUseInputStream = mandatoryUseInputStream;
    }

    public ReadCache getReadCache() {
        return this.readCache;
    }

    public void setReadCache(ReadCache readCache) {
        this.readCache = readCache;
    }

    public Boolean getConvertAllFiled() {
        return this.convertAllFiled;
    }

    public void setConvertAllFiled(Boolean convertAllFiled) {
        this.convertAllFiled = convertAllFiled;
    }

    public Boolean getDefaultReturnMap() {
        return this.defaultReturnMap;
    }

    public void setDefaultReturnMap(Boolean defaultReturnMap) {
        this.defaultReturnMap = defaultReturnMap;
    }

    public Boolean getIgnoreEmptyRow() {
        return this.ignoreEmptyRow;
    }

    public void setIgnoreEmptyRow(Boolean ignoreEmptyRow) {
        this.ignoreEmptyRow = ignoreEmptyRow;
    }

    public ReadCacheSelector getReadCacheSelector() {
        return this.readCacheSelector;
    }

    public void setReadCacheSelector(ReadCacheSelector readCacheSelector) {
        this.readCacheSelector = readCacheSelector;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getXlsxSAXParserFactoryName() {
        return this.xlsxSAXParserFactoryName;
    }

    public void setXlsxSAXParserFactoryName(String xlsxSAXParserFactoryName) {
        this.xlsxSAXParserFactoryName = xlsxSAXParserFactoryName;
    }

    public Boolean getUseDefaultListener() {
        return this.useDefaultListener;
    }

    public void setUseDefaultListener(Boolean useDefaultListener) {
        this.useDefaultListener = useDefaultListener;
    }
}
