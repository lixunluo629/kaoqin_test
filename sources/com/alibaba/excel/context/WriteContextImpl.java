package com.alibaba.excel.context;

import com.alibaba.excel.enums.WriteTypeEnum;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellRange;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.util.WriteHandlerUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.holder.WriteHolder;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/context/WriteContextImpl.class */
public class WriteContextImpl implements WriteContext {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) WriteContextImpl.class);
    private WriteWorkbookHolder writeWorkbookHolder;
    private WriteSheetHolder writeSheetHolder;
    private WriteTableHolder writeTableHolder;
    private WriteHolder currentWriteHolder;
    private boolean finished = false;

    public WriteContextImpl(WriteWorkbook writeWorkbook) {
        if (writeWorkbook == null) {
            throw new IllegalArgumentException("Workbook argument cannot be null");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Begin to Initialization 'WriteContextImpl'");
        }
        initCurrentWorkbookHolder(writeWorkbook);
        WriteHandlerUtils.beforeWorkbookCreate(this);
        try {
            WorkBookUtil.createWorkBook(this.writeWorkbookHolder);
            WriteHandlerUtils.afterWorkbookCreate(this);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Initialization 'WriteContextImpl' complete");
            }
        } catch (Exception e) {
            throw new ExcelGenerateException("Create workbook failure", e);
        }
    }

    private void initCurrentWorkbookHolder(WriteWorkbook writeWorkbook) {
        this.writeWorkbookHolder = new WriteWorkbookHolder(writeWorkbook);
        this.currentWriteHolder = this.writeWorkbookHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeWorkbookHolder");
        }
    }

    @Override // com.alibaba.excel.context.WriteContext
    public void currentSheet(WriteSheet writeSheet, WriteTypeEnum writeType) {
        if (writeSheet == null) {
            throw new IllegalArgumentException("Sheet argument cannot be null");
        }
        if (selectSheetFromCache(writeSheet)) {
            return;
        }
        initCurrentSheetHolder(writeSheet);
        initSheet(writeType);
    }

    private boolean selectSheetFromCache(WriteSheet writeSheet) {
        this.writeSheetHolder = null;
        if (writeSheet.getSheetNo() != null) {
            this.writeSheetHolder = this.writeWorkbookHolder.getHasBeenInitializedSheetIndexMap().get(writeSheet.getSheetNo());
        }
        if (this.writeSheetHolder == null && !StringUtils.isEmpty(writeSheet.getSheetName())) {
            this.writeSheetHolder = this.writeWorkbookHolder.getHasBeenInitializedSheetNameMap().get(writeSheet.getSheetName());
        }
        if (this.writeSheetHolder == null) {
            return false;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sheet:{} is already existed", writeSheet.getSheetNo());
        }
        this.writeSheetHolder.setNewInitialization(Boolean.FALSE);
        this.writeTableHolder = null;
        this.currentWriteHolder = this.writeSheetHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeSheetHolder");
            return true;
        }
        return true;
    }

    private void initCurrentSheetHolder(WriteSheet writeSheet) {
        this.writeSheetHolder = new WriteSheetHolder(writeSheet, this.writeWorkbookHolder);
        this.writeTableHolder = null;
        this.currentWriteHolder = this.writeSheetHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeSheetHolder");
        }
    }

    private void initSheet(WriteTypeEnum writeType) {
        Sheet currentSheet;
        WriteHandlerUtils.beforeSheetCreate(this);
        try {
            if (this.writeSheetHolder.getSheetNo() != null) {
                currentSheet = this.writeWorkbookHolder.getWorkbook().getSheetAt(this.writeSheetHolder.getSheetNo().intValue());
                this.writeSheetHolder.setCachedSheet(this.writeWorkbookHolder.getCachedWorkbook().getSheetAt(this.writeSheetHolder.getSheetNo().intValue()));
            } else {
                currentSheet = this.writeWorkbookHolder.getWorkbook().getSheet(this.writeSheetHolder.getSheetName());
                this.writeSheetHolder.setCachedSheet(this.writeWorkbookHolder.getCachedWorkbook().getSheet(this.writeSheetHolder.getSheetName()));
            }
        } catch (Exception e) {
            currentSheet = createSheet();
        }
        if (currentSheet == null) {
            currentSheet = createSheet();
        }
        this.writeSheetHolder.setSheet(currentSheet);
        WriteHandlerUtils.afterSheetCreate(this);
        if (WriteTypeEnum.ADD.equals(writeType)) {
            initHead(this.writeSheetHolder.excelWriteHeadProperty());
        }
        this.writeWorkbookHolder.getHasBeenInitializedSheetIndexMap().put(this.writeSheetHolder.getSheetNo(), this.writeSheetHolder);
        this.writeWorkbookHolder.getHasBeenInitializedSheetNameMap().put(this.writeSheetHolder.getSheetName(), this.writeSheetHolder);
    }

    private Sheet createSheet() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Can not find sheet:{} ,now create it", this.writeSheetHolder.getSheetNo());
        }
        if (StringUtils.isEmpty(this.writeSheetHolder.getSheetName())) {
            this.writeSheetHolder.setSheetName(this.writeSheetHolder.getSheetNo().toString());
        }
        Sheet currentSheet = WorkBookUtil.createSheet(this.writeWorkbookHolder.getWorkbook(), this.writeSheetHolder.getSheetName());
        this.writeSheetHolder.setCachedSheet(currentSheet);
        return currentSheet;
    }

    public void initHead(ExcelWriteHeadProperty excelWriteHeadProperty) {
        if (!this.currentWriteHolder.needHead() || !this.currentWriteHolder.excelWriteHeadProperty().hasHead()) {
            return;
        }
        int newRowIndex = this.writeSheetHolder.getNewRowIndexAndStartDoWrite() + this.currentWriteHolder.relativeHeadRowIndex();
        if (this.currentWriteHolder.automaticMergeHead()) {
            addMergedRegionToCurrentSheet(excelWriteHeadProperty, newRowIndex);
        }
        int relativeRowIndex = 0;
        int i = newRowIndex;
        while (i < excelWriteHeadProperty.getHeadRowNumber() + newRowIndex) {
            WriteHandlerUtils.beforeRowCreate(this, Integer.valueOf(newRowIndex), Integer.valueOf(relativeRowIndex), Boolean.TRUE);
            Row row = WorkBookUtil.createRow(this.writeSheetHolder.getSheet(), i);
            WriteHandlerUtils.afterRowCreate(this, row, Integer.valueOf(relativeRowIndex), Boolean.TRUE);
            addOneRowOfHeadDataToExcel(row, excelWriteHeadProperty.getHeadMap(), relativeRowIndex);
            WriteHandlerUtils.afterRowDispose(this, row, Integer.valueOf(relativeRowIndex), Boolean.TRUE);
            i++;
            relativeRowIndex++;
        }
    }

    private void addMergedRegionToCurrentSheet(ExcelWriteHeadProperty excelWriteHeadProperty, int rowIndex) {
        for (CellRange cellRangeModel : excelWriteHeadProperty.headCellRangeList()) {
            this.writeSheetHolder.getSheet().addMergedRegionUnsafe(new CellRangeAddress(cellRangeModel.getFirstRow() + rowIndex, cellRangeModel.getLastRow() + rowIndex, cellRangeModel.getFirstCol(), cellRangeModel.getLastCol()));
        }
    }

    private void addOneRowOfHeadDataToExcel(Row row, Map<Integer, Head> headMap, int relativeRowIndex) {
        for (Map.Entry<Integer, Head> entry : headMap.entrySet()) {
            Head head = entry.getValue();
            int columnIndex = entry.getKey().intValue();
            WriteHandlerUtils.beforeCellCreate(this, row, head, Integer.valueOf(columnIndex), Integer.valueOf(relativeRowIndex), Boolean.TRUE);
            Cell cell = row.createCell(columnIndex);
            WriteHandlerUtils.afterCellCreate(this, cell, head, Integer.valueOf(relativeRowIndex), Boolean.TRUE);
            cell.setCellValue(head.getHeadNameList().get(relativeRowIndex));
            WriteHandlerUtils.afterCellDispose(this, (CellData) null, cell, head, Integer.valueOf(relativeRowIndex), Boolean.TRUE);
        }
    }

    @Override // com.alibaba.excel.context.WriteContext
    public void currentTable(WriteTable writeTable) {
        if (writeTable == null) {
            return;
        }
        if (writeTable.getTableNo() == null || writeTable.getTableNo().intValue() <= 0) {
            writeTable.setTableNo(0);
        }
        if (this.writeSheetHolder.getHasBeenInitializedTable().containsKey(writeTable.getTableNo())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table:{} is already existed", writeTable.getTableNo());
            }
            this.writeTableHolder = this.writeSheetHolder.getHasBeenInitializedTable().get(writeTable.getTableNo());
            this.writeTableHolder.setNewInitialization(Boolean.FALSE);
            this.currentWriteHolder = this.writeTableHolder;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("CurrentConfiguration is writeTableHolder");
                return;
            }
            return;
        }
        initCurrentTableHolder(writeTable);
        initHead(this.writeTableHolder.excelWriteHeadProperty());
    }

    private void initCurrentTableHolder(WriteTable writeTable) {
        this.writeTableHolder = new WriteTableHolder(writeTable, this.writeSheetHolder, this.writeWorkbookHolder);
        this.writeSheetHolder.getHasBeenInitializedTable().put(writeTable.getTableNo(), this.writeTableHolder);
        this.currentWriteHolder = this.writeTableHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeTableHolder");
        }
    }

    @Override // com.alibaba.excel.context.WriteContext
    public WriteWorkbookHolder writeWorkbookHolder() {
        return this.writeWorkbookHolder;
    }

    @Override // com.alibaba.excel.context.WriteContext
    public WriteSheetHolder writeSheetHolder() {
        return this.writeSheetHolder;
    }

    @Override // com.alibaba.excel.context.WriteContext
    public WriteTableHolder writeTableHolder() {
        return this.writeTableHolder;
    }

    @Override // com.alibaba.excel.context.WriteContext
    public WriteHolder currentWriteHolder() {
        return this.currentWriteHolder;
    }

    @Override // com.alibaba.excel.context.WriteContext
    public void finish(boolean onException) {
        if (this.finished) {
            return;
        }
        this.finished = true;
        WriteHandlerUtils.afterWorkbookDispose(this);
        if (this.writeWorkbookHolder == null) {
            return;
        }
        Throwable throwable = null;
        boolean isOutputStreamEncrypt = false;
        boolean writeExcel = !onException;
        if (this.writeWorkbookHolder.getWriteExcelOnException().booleanValue()) {
            writeExcel = Boolean.TRUE.booleanValue();
        }
        if (writeExcel) {
            try {
                isOutputStreamEncrypt = doOutputStreamEncrypt07();
            } catch (Throwable t) {
                throwable = t;
            }
        }
        if (!isOutputStreamEncrypt) {
            if (writeExcel) {
                try {
                    this.writeWorkbookHolder.getWorkbook().write(this.writeWorkbookHolder.getOutputStream());
                } catch (Throwable t2) {
                    throwable = t2;
                }
            }
            this.writeWorkbookHolder.getWorkbook().close();
        }
        try {
            Workbook workbook = this.writeWorkbookHolder.getWorkbook();
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (Throwable t3) {
            throwable = t3;
        }
        try {
            if (this.writeWorkbookHolder.getAutoCloseStream().booleanValue() && this.writeWorkbookHolder.getOutputStream() != null) {
                this.writeWorkbookHolder.getOutputStream().close();
            }
        } catch (Throwable t4) {
            throwable = t4;
        }
        if (writeExcel && !isOutputStreamEncrypt) {
            try {
                doFileEncrypt07();
            } catch (Throwable t5) {
                throwable = t5;
            }
        }
        try {
            if (this.writeWorkbookHolder.getTempTemplateInputStream() != null) {
                this.writeWorkbookHolder.getTempTemplateInputStream().close();
            }
        } catch (Throwable t6) {
            throwable = t6;
        }
        clearEncrypt03();
        if (throwable != null) {
            throw new ExcelGenerateException("Can not close IO.", throwable);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Finished write.");
        }
    }

    @Override // com.alibaba.excel.context.WriteContext
    public Sheet getCurrentSheet() {
        return this.writeSheetHolder.getSheet();
    }

    @Override // com.alibaba.excel.context.WriteContext
    public boolean needHead() {
        return this.writeSheetHolder.needHead();
    }

    @Override // com.alibaba.excel.context.WriteContext
    public OutputStream getOutputStream() {
        return this.writeWorkbookHolder.getOutputStream();
    }

    @Override // com.alibaba.excel.context.WriteContext
    public Workbook getWorkbook() {
        return this.writeWorkbookHolder.getWorkbook();
    }

    private void clearEncrypt03() {
        if (StringUtils.isEmpty(this.writeWorkbookHolder.getPassword()) || !ExcelTypeEnum.XLS.equals(this.writeWorkbookHolder.getExcelType())) {
            return;
        }
        Biff8EncryptionKey.setCurrentUserPassword(null);
    }

    private boolean doOutputStreamEncrypt07() throws Exception {
        if (StringUtils.isEmpty(this.writeWorkbookHolder.getPassword()) || !ExcelTypeEnum.XLSX.equals(this.writeWorkbookHolder.getExcelType()) || this.writeWorkbookHolder.getFile() != null) {
            return false;
        }
        File tempXlsx = FileUtils.createTmpFile(UUID.randomUUID().toString() + ".xlsx");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempXlsx);
        try {
            this.writeWorkbookHolder.getWorkbook().write(tempFileOutputStream);
            try {
                this.writeWorkbookHolder.getWorkbook().close();
                tempFileOutputStream.close();
                POIFSFileSystem fileSystem = null;
                try {
                    fileSystem = openFileSystemAndEncrypt(tempXlsx);
                    fileSystem.writeFilesystem(this.writeWorkbookHolder.getOutputStream());
                    if (fileSystem != null) {
                        fileSystem.close();
                    }
                    if (!tempXlsx.delete()) {
                        throw new ExcelGenerateException("Can not delete temp File!");
                    }
                    return true;
                } catch (Throwable th) {
                    if (fileSystem != null) {
                        fileSystem.close();
                    }
                    if (!tempXlsx.delete()) {
                        throw new ExcelGenerateException("Can not delete temp File!");
                    }
                    throw th;
                }
            } catch (Exception e) {
                if (!tempXlsx.delete()) {
                    throw new ExcelGenerateException("Can not delete temp File!");
                }
                throw e;
            }
        } catch (Throwable th2) {
            try {
                this.writeWorkbookHolder.getWorkbook().close();
                tempFileOutputStream.close();
                throw th2;
            } catch (Exception e2) {
                if (!tempXlsx.delete()) {
                    throw new ExcelGenerateException("Can not delete temp File!");
                }
                throw e2;
            }
        }
    }

    private void doFileEncrypt07() throws Exception {
        if (StringUtils.isEmpty(this.writeWorkbookHolder.getPassword()) || !ExcelTypeEnum.XLSX.equals(this.writeWorkbookHolder.getExcelType()) || this.writeWorkbookHolder.getFile() == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        POIFSFileSystem fileSystem = null;
        try {
            fileSystem = openFileSystemAndEncrypt(this.writeWorkbookHolder.getFile());
            fileOutputStream = new FileOutputStream(this.writeWorkbookHolder.getFile());
            fileSystem.writeFilesystem(fileOutputStream);
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (fileSystem != null) {
                fileSystem.close();
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (fileSystem != null) {
                fileSystem.close();
            }
            throw th;
        }
    }

    private POIFSFileSystem openFileSystemAndEncrypt(File file) throws Exception {
        POIFSFileSystem fileSystem = new POIFSFileSystem();
        Encryptor encryptor = new EncryptionInfo(EncryptionMode.standard).getEncryptor();
        encryptor.confirmPassword(this.writeWorkbookHolder.getPassword());
        OPCPackage opcPackage = null;
        try {
            opcPackage = OPCPackage.open(file, PackageAccess.READ_WRITE);
            OutputStream outputStream = encryptor.getDataStream(fileSystem);
            opcPackage.save(outputStream);
            if (opcPackage != null) {
                opcPackage.close();
            }
            return fileSystem;
        } catch (Throwable th) {
            if (opcPackage != null) {
                opcPackage.close();
            }
            throw th;
        }
    }
}
