package com.alibaba.excel.write;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.context.WriteContextImpl;
import com.alibaba.excel.enums.WriteTypeEnum;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.write.executor.ExcelWriteAddExecutor;
import com.alibaba.excel.write.executor.ExcelWriteFillExecutor;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import java.util.List;
import org.apache.poi.ss.util.CellRangeAddress;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/ExcelBuilderImpl.class */
public class ExcelBuilderImpl implements ExcelBuilder {
    private WriteContext context;
    private ExcelWriteFillExecutor excelWriteFillExecutor;
    private ExcelWriteAddExecutor excelWriteAddExecutor;

    static {
        FileUtils.createPoiFilesDirectory();
    }

    public ExcelBuilderImpl(WriteWorkbook writeWorkbook) {
        try {
            this.context = new WriteContextImpl(writeWorkbook);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e2) {
            finishOnException();
            throw new ExcelGenerateException(e2);
        }
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void addContent(List data, WriteSheet writeSheet) {
        addContent(data, writeSheet, null);
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void addContent(List data, WriteSheet writeSheet, WriteTable writeTable) {
        if (data == null) {
            return;
        }
        try {
            this.context.currentSheet(writeSheet, WriteTypeEnum.ADD);
            this.context.currentTable(writeTable);
            if (this.excelWriteAddExecutor == null) {
                this.excelWriteAddExecutor = new ExcelWriteAddExecutor(this.context);
            }
            this.excelWriteAddExecutor.add(data);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e2) {
            finishOnException();
            throw new ExcelGenerateException(e2);
        }
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void fill(Object data, FillConfig fillConfig, WriteSheet writeSheet) {
        if (data == null) {
            return;
        }
        try {
            if (this.context.writeWorkbookHolder().getTempTemplateInputStream() == null) {
                throw new ExcelGenerateException("Calling the 'fill' method must use a template.");
            }
            this.context.currentSheet(writeSheet, WriteTypeEnum.FILL);
            if (this.excelWriteFillExecutor == null) {
                this.excelWriteFillExecutor = new ExcelWriteFillExecutor(this.context);
            }
            this.excelWriteFillExecutor.fill(data, fillConfig);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e2) {
            finishOnException();
            throw new ExcelGenerateException(e2);
        }
    }

    private void finishOnException() {
        finish(true);
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void finish(boolean onException) {
        if (this.context != null) {
            this.context.finish(onException);
        }
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void addContent(List data, WriteSheet writeSheet, WriteTable writeTable, String password) {
        if (data == null) {
            return;
        }
        try {
            this.context.currentSheet(writeSheet, WriteTypeEnum.ADD);
            this.context.currentTable(writeTable);
            if (this.excelWriteAddExecutor == null) {
                this.excelWriteAddExecutor = new ExcelWriteAddExecutor(this.context);
            }
            this.excelWriteAddExecutor.add(data);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e2) {
            finishOnException();
            throw new ExcelGenerateException(e2);
        }
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress cra = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        this.context.writeSheetHolder().getSheet().addMergedRegion(cra);
    }

    @Override // com.alibaba.excel.write.ExcelBuilder
    public WriteContext writeContext() {
        return this.context;
    }
}
