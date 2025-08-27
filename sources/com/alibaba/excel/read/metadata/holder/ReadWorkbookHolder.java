package com.alibaba.excel.read.metadata.holder;

import com.alibaba.excel.cache.ReadCache;
import com.alibaba.excel.cache.selector.EternalReadCacheSelector;
import com.alibaba.excel.cache.selector.ReadCacheSelector;
import com.alibaba.excel.cache.selector.SimpleReadCacheSelector;
import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.support.ExcelTypeEnum;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/read/metadata/holder/ReadWorkbookHolder.class */
public class ReadWorkbookHolder extends AbstractReadHolder {
    private ReadWorkbook readWorkbook;
    private InputStream inputStream;
    private File file;
    private Boolean mandatoryUseInputStream;
    private Boolean autoCloseStream;
    private ExcelTypeEnum excelType;
    private Object customObject;
    private Boolean ignoreEmptyRow;
    private ReadCache readCache;
    private ReadCacheSelector readCacheSelector;
    private File tempFile;
    private String password;
    private String xlsxSAXParserFactoryName;

    @Deprecated
    private Boolean convertAllFiled;

    @Deprecated
    private Boolean defaultReturnMap;
    private Set<Integer> hasReadSheet;
    private OPCPackage opcPackage;
    private POIFSFileSystem poifsFileSystem;
    private Boolean ignoreRecord03;

    public ReadWorkbookHolder(ReadWorkbook readWorkbook) {
        super(readWorkbook, null, readWorkbook.getConvertAllFiled());
        this.readWorkbook = readWorkbook;
        if (readWorkbook.getInputStream() != null) {
            if (readWorkbook.getInputStream().markSupported()) {
                this.inputStream = readWorkbook.getInputStream();
            } else {
                this.inputStream = new BufferedInputStream(readWorkbook.getInputStream());
            }
        }
        this.file = readWorkbook.getFile();
        if (this.file == null && this.inputStream == null) {
            throw new ExcelAnalysisException("File and inputStream must be a non-null.");
        }
        if (readWorkbook.getMandatoryUseInputStream() == null) {
            this.mandatoryUseInputStream = Boolean.FALSE;
        } else {
            this.mandatoryUseInputStream = readWorkbook.getMandatoryUseInputStream();
        }
        if (readWorkbook.getAutoCloseStream() == null) {
            this.autoCloseStream = Boolean.TRUE;
        } else {
            this.autoCloseStream = readWorkbook.getAutoCloseStream();
        }
        this.excelType = ExcelTypeEnum.valueOf(this.file, this.inputStream, readWorkbook.getExcelType());
        if (ExcelTypeEnum.XLS == this.excelType && getGlobalConfiguration().getUse1904windowing() == null) {
            getGlobalConfiguration().setUse1904windowing(Boolean.FALSE);
        }
        this.customObject = readWorkbook.getCustomObject();
        if (readWorkbook.getIgnoreEmptyRow() == null) {
            this.ignoreEmptyRow = Boolean.TRUE;
        } else {
            this.ignoreEmptyRow = readWorkbook.getIgnoreEmptyRow();
        }
        if (readWorkbook.getReadCache() != null) {
            if (readWorkbook.getReadCacheSelector() != null) {
                throw new ExcelAnalysisException("'readCache' and 'readCacheSelector' only one choice.");
            }
            this.readCacheSelector = new EternalReadCacheSelector(readWorkbook.getReadCache());
        } else if (readWorkbook.getReadCacheSelector() == null) {
            this.readCacheSelector = new SimpleReadCacheSelector();
        } else {
            this.readCacheSelector = readWorkbook.getReadCacheSelector();
        }
        if (readWorkbook.getDefaultReturnMap() == null) {
            this.defaultReturnMap = Boolean.TRUE;
        } else {
            this.defaultReturnMap = readWorkbook.getDefaultReturnMap();
        }
        this.xlsxSAXParserFactoryName = readWorkbook.getXlsxSAXParserFactoryName();
        this.hasReadSheet = new HashSet();
        this.ignoreRecord03 = Boolean.FALSE;
        this.password = readWorkbook.getPassword();
    }

    public ReadWorkbook getReadWorkbook() {
        return this.readWorkbook;
    }

    public void setReadWorkbook(ReadWorkbook readWorkbook) {
        this.readWorkbook = readWorkbook;
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

    public ExcelTypeEnum getExcelType() {
        return this.excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public Object getCustomObject() {
        return this.customObject;
    }

    public void setCustomObject(Object customObject) {
        this.customObject = customObject;
    }

    public Boolean getIgnoreEmptyRow() {
        return this.ignoreEmptyRow;
    }

    public void setIgnoreEmptyRow(Boolean ignoreEmptyRow) {
        this.ignoreEmptyRow = ignoreEmptyRow;
    }

    public ReadCache getReadCache() {
        return this.readCache;
    }

    public void setReadCache(ReadCache readCache) {
        this.readCache = readCache;
    }

    public ReadCacheSelector getReadCacheSelector() {
        return this.readCacheSelector;
    }

    public void setReadCacheSelector(ReadCacheSelector readCacheSelector) {
        this.readCacheSelector = readCacheSelector;
    }

    public Boolean getMandatoryUseInputStream() {
        return this.mandatoryUseInputStream;
    }

    public void setMandatoryUseInputStream(Boolean mandatoryUseInputStream) {
        this.mandatoryUseInputStream = mandatoryUseInputStream;
    }

    public File getTempFile() {
        return this.tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public Boolean getConvertAllFiled() {
        return this.convertAllFiled;
    }

    public void setConvertAllFiled(Boolean convertAllFiled) {
        this.convertAllFiled = convertAllFiled;
    }

    public Set<Integer> getHasReadSheet() {
        return this.hasReadSheet;
    }

    public void setHasReadSheet(Set<Integer> hasReadSheet) {
        this.hasReadSheet = hasReadSheet;
    }

    public Boolean getDefaultReturnMap() {
        return this.defaultReturnMap;
    }

    public void setDefaultReturnMap(Boolean defaultReturnMap) {
        this.defaultReturnMap = defaultReturnMap;
    }

    public OPCPackage getOpcPackage() {
        return this.opcPackage;
    }

    public void setOpcPackage(OPCPackage opcPackage) {
        this.opcPackage = opcPackage;
    }

    public POIFSFileSystem getPoifsFileSystem() {
        return this.poifsFileSystem;
    }

    public void setPoifsFileSystem(POIFSFileSystem poifsFileSystem) {
        this.poifsFileSystem = poifsFileSystem;
    }

    public Boolean getIgnoreRecord03() {
        return this.ignoreRecord03;
    }

    public void setIgnoreRecord03(Boolean ignoreRecord03) {
        this.ignoreRecord03 = ignoreRecord03;
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

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.WORKBOOK;
    }
}
