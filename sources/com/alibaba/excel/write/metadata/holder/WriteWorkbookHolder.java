package com.alibaba.excel.write.metadata.holder;

import com.alibaba.excel.enums.HolderEnum;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.IoUtils;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/holder/WriteWorkbookHolder.class */
public class WriteWorkbookHolder extends AbstractWriteHolder {
    private Workbook workbook;
    private Workbook cachedWorkbook;
    private WriteWorkbook writeWorkbook;
    private File file;
    private OutputStream outputStream;
    private InputStream templateInputStream;
    private File templateFile;
    private InputStream tempTemplateInputStream;
    private Boolean autoCloseStream;
    private ExcelTypeEnum excelType;
    private Boolean mandatoryUseInputStream;
    private Map<Integer, WriteSheetHolder> hasBeenInitializedSheetIndexMap;
    private Map<String, WriteSheetHolder> hasBeenInitializedSheetNameMap;
    private String password;
    private Boolean inMemory;
    private Boolean writeExcelOnException;

    public WriteWorkbookHolder(WriteWorkbook writeWorkbook) {
        super(writeWorkbook, null, writeWorkbook.getConvertAllFiled());
        this.writeWorkbook = writeWorkbook;
        this.file = writeWorkbook.getFile();
        if (this.file != null) {
            try {
                this.outputStream = new FileOutputStream(this.file);
            } catch (FileNotFoundException e) {
                throw new ExcelGenerateException("Can not found file.", e);
            }
        } else {
            this.outputStream = writeWorkbook.getOutputStream();
        }
        if (writeWorkbook.getAutoCloseStream() == null) {
            this.autoCloseStream = Boolean.TRUE;
        } else {
            this.autoCloseStream = writeWorkbook.getAutoCloseStream();
        }
        try {
            copyTemplate();
            if (writeWorkbook.getExcelType() == null) {
                boolean isXls = (this.file != null && this.file.getName().endsWith(ExcelTypeEnum.XLS.getValue())) || (writeWorkbook.getTemplateFile() != null && writeWorkbook.getTemplateFile().getName().endsWith(ExcelTypeEnum.XLS.getValue()));
                if (isXls) {
                    this.excelType = ExcelTypeEnum.XLS;
                } else {
                    this.excelType = ExcelTypeEnum.XLSX;
                }
            } else {
                this.excelType = writeWorkbook.getExcelType();
            }
            if (writeWorkbook.getMandatoryUseInputStream() == null) {
                this.mandatoryUseInputStream = Boolean.FALSE;
            } else {
                this.mandatoryUseInputStream = writeWorkbook.getMandatoryUseInputStream();
            }
            this.hasBeenInitializedSheetIndexMap = new HashMap();
            this.hasBeenInitializedSheetNameMap = new HashMap();
            this.password = writeWorkbook.getPassword();
            if (writeWorkbook.getInMemory() == null) {
                this.inMemory = Boolean.FALSE;
            } else {
                this.inMemory = writeWorkbook.getInMemory();
            }
            if (writeWorkbook.getWriteExcelOnException() == null) {
                this.writeExcelOnException = Boolean.FALSE;
            } else {
                this.writeExcelOnException = writeWorkbook.getWriteExcelOnException();
            }
        } catch (IOException e2) {
            throw new ExcelGenerateException("Copy template failure.", e2);
        }
    }

    private void copyTemplate() throws IOException {
        if (this.writeWorkbook.getTemplateFile() == null && this.writeWorkbook.getTemplateInputStream() == null) {
            return;
        }
        byte[] templateFileByte = null;
        if (this.writeWorkbook.getTemplateFile() != null) {
            templateFileByte = FileUtils.readFileToByteArray(this.writeWorkbook.getTemplateFile());
        } else if (this.writeWorkbook.getTemplateInputStream() != null) {
            try {
                templateFileByte = IoUtils.toByteArray(this.writeWorkbook.getTemplateInputStream());
            } finally {
                if (this.autoCloseStream.booleanValue()) {
                    this.writeWorkbook.getTemplateInputStream().close();
                }
            }
        }
        this.tempTemplateInputStream = new ByteArrayInputStream(templateFileByte);
    }

    public Workbook getWorkbook() {
        return this.workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Workbook getCachedWorkbook() {
        return this.cachedWorkbook;
    }

    public void setCachedWorkbook(Workbook cachedWorkbook) {
        this.cachedWorkbook = cachedWorkbook;
    }

    public Map<Integer, WriteSheetHolder> getHasBeenInitializedSheetIndexMap() {
        return this.hasBeenInitializedSheetIndexMap;
    }

    public void setHasBeenInitializedSheetIndexMap(Map<Integer, WriteSheetHolder> hasBeenInitializedSheetIndexMap) {
        this.hasBeenInitializedSheetIndexMap = hasBeenInitializedSheetIndexMap;
    }

    public Map<String, WriteSheetHolder> getHasBeenInitializedSheetNameMap() {
        return this.hasBeenInitializedSheetNameMap;
    }

    public void setHasBeenInitializedSheetNameMap(Map<String, WriteSheetHolder> hasBeenInitializedSheetNameMap) {
        this.hasBeenInitializedSheetNameMap = hasBeenInitializedSheetNameMap;
    }

    public WriteWorkbook getWriteWorkbook() {
        return this.writeWorkbook;
    }

    public void setWriteWorkbook(WriteWorkbook writeWorkbook) {
        this.writeWorkbook = writeWorkbook;
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

    public InputStream getTempTemplateInputStream() {
        return this.tempTemplateInputStream;
    }

    public void setTempTemplateInputStream(InputStream tempTemplateInputStream) {
        this.tempTemplateInputStream = tempTemplateInputStream;
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

    public ExcelTypeEnum getExcelType() {
        return this.excelType;
    }

    public void setExcelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
    }

    public Boolean getMandatoryUseInputStream() {
        return this.mandatoryUseInputStream;
    }

    public void setMandatoryUseInputStream(Boolean mandatoryUseInputStream) {
        this.mandatoryUseInputStream = mandatoryUseInputStream;
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

    @Override // com.alibaba.excel.metadata.Holder
    public HolderEnum holderType() {
        return HolderEnum.WORKBOOK;
    }
}
