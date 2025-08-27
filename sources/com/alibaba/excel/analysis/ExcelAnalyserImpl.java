package com.alibaba.excel.analysis;

import com.alibaba.excel.analysis.v03.XlsSaxAnalyser;
import com.alibaba.excel.analysis.v07.XlsxSaxAnalyser;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.context.AnalysisContextImpl;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.ReadWorkbook;
import com.alibaba.excel.read.metadata.holder.ReadWorkbookHolder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.StringUtils;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/ExcelAnalyserImpl.class */
public class ExcelAnalyserImpl implements ExcelAnalyser {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ExcelAnalyserImpl.class);
    private AnalysisContext analysisContext;
    private ExcelReadExecutor excelReadExecutor;
    private boolean finished = false;

    public ExcelAnalyserImpl(ReadWorkbook readWorkbook) {
        try {
            this.analysisContext = new AnalysisContextImpl(readWorkbook);
            choiceExcelExecutor();
        } catch (RuntimeException e) {
            finish();
            throw e;
        } catch (Throwable e2) {
            finish();
            throw new ExcelAnalysisException(e2);
        }
    }

    /* JADX WARN: Finally extract failed */
    private void choiceExcelExecutor() throws Exception {
        POIFSFileSystem poifsFileSystem;
        ReadWorkbookHolder readWorkbookHolder = this.analysisContext.readWorkbookHolder();
        ExcelTypeEnum excelType = readWorkbookHolder.getExcelType();
        if (excelType == null) {
            this.excelReadExecutor = new XlsxSaxAnalyser(this.analysisContext, null);
            return;
        }
        switch (excelType) {
            case XLS:
                if (readWorkbookHolder.getFile() != null) {
                    poifsFileSystem = new POIFSFileSystem(readWorkbookHolder.getFile());
                } else {
                    poifsFileSystem = new POIFSFileSystem(readWorkbookHolder.getInputStream());
                }
                if (poifsFileSystem.getRoot().hasEntry(Decryptor.DEFAULT_POIFS_ENTRY)) {
                    InputStream decryptedStream = null;
                    try {
                        decryptedStream = DocumentFactoryHelper.getDecryptedStream(poifsFileSystem.getRoot().getFileSystem(), this.analysisContext.readWorkbookHolder().getPassword());
                        this.excelReadExecutor = new XlsxSaxAnalyser(this.analysisContext, decryptedStream);
                        IOUtils.closeQuietly(decryptedStream);
                        poifsFileSystem.close();
                        return;
                    } catch (Throwable th) {
                        IOUtils.closeQuietly(decryptedStream);
                        poifsFileSystem.close();
                        throw th;
                    }
                }
                if (this.analysisContext.readWorkbookHolder().getPassword() != null) {
                    Biff8EncryptionKey.setCurrentUserPassword(this.analysisContext.readWorkbookHolder().getPassword());
                }
                this.excelReadExecutor = new XlsSaxAnalyser(this.analysisContext, poifsFileSystem);
                return;
            case XLSX:
                this.excelReadExecutor = new XlsxSaxAnalyser(this.analysisContext, null);
                return;
            default:
                return;
        }
    }

    @Override // com.alibaba.excel.analysis.ExcelAnalyser
    public void analysis(List<ReadSheet> readSheetList, Boolean readAll) {
        try {
            if (!readAll.booleanValue() && CollectionUtils.isEmpty(readSheetList)) {
                throw new IllegalArgumentException("Specify at least one read sheet.");
            }
            try {
                this.excelReadExecutor.execute(readSheetList, readAll);
            } catch (ExcelAnalysisStopException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Custom stop!");
                }
            }
            if ((this.excelReadExecutor instanceof XlsSaxAnalyser) && this.analysisContext.readSheetHolder() != null) {
                this.analysisContext.readSheetHolder().notifyAfterAllAnalysed(this.analysisContext);
            }
        } catch (RuntimeException e2) {
            finish();
            throw e2;
        } catch (Throwable e3) {
            finish();
            throw new ExcelAnalysisException(e3);
        }
    }

    @Override // com.alibaba.excel.analysis.ExcelAnalyser
    public void finish() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        if (this.analysisContext == null || this.analysisContext.readWorkbookHolder() == null) {
            return;
        }
        ReadWorkbookHolder readWorkbookHolder = this.analysisContext.readWorkbookHolder();
        Throwable throwable = null;
        try {
            if (readWorkbookHolder.getReadCache() != null) {
                readWorkbookHolder.getReadCache().destroy();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if (readWorkbookHolder.getOpcPackage() != null) {
                readWorkbookHolder.getOpcPackage().revert();
            }
        } catch (Throwable t2) {
            throwable = t2;
        }
        try {
            if (readWorkbookHolder.getPoifsFileSystem() != null) {
                readWorkbookHolder.getPoifsFileSystem().close();
            }
        } catch (Throwable t3) {
            throwable = t3;
        }
        try {
            if (this.analysisContext.readWorkbookHolder().getAutoCloseStream().booleanValue() && readWorkbookHolder.getInputStream() != null) {
                readWorkbookHolder.getInputStream().close();
            }
        } catch (Throwable t4) {
            throwable = t4;
        }
        try {
            if (readWorkbookHolder.getTempFile() != null) {
                FileUtils.delete(readWorkbookHolder.getTempFile());
            }
        } catch (Throwable t5) {
            throwable = t5;
        }
        clearEncrypt03();
        if (throwable != null) {
            throw new ExcelAnalysisException("Can not close IO.", throwable);
        }
    }

    private void clearEncrypt03() {
        if (StringUtils.isEmpty(this.analysisContext.readWorkbookHolder().getPassword()) || !ExcelTypeEnum.XLS.equals(this.analysisContext.readWorkbookHolder().getExcelType())) {
            return;
        }
        Biff8EncryptionKey.setCurrentUserPassword(null);
    }

    @Override // com.alibaba.excel.analysis.ExcelAnalyser
    public ExcelReadExecutor excelExecutor() {
        return this.excelReadExecutor;
    }

    @Override // com.alibaba.excel.analysis.ExcelAnalyser
    public AnalysisContext analysisContext() {
        return this.analysisContext;
    }
}
