package org.apache.poi.poifs.crypt.temp;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import org.apache.poi.openxml4j.util.ZipEntrySource;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SheetDataWriter;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/poifs/crypt/temp/SXSSFWorkbookWithCustomZipEntrySource.class */
public class SXSSFWorkbookWithCustomZipEntrySource extends SXSSFWorkbook {
    private static final POILogger LOG = POILogFactory.getLogger((Class<?>) SXSSFWorkbookWithCustomZipEntrySource.class);

    public SXSSFWorkbookWithCustomZipEntrySource() {
        super(20);
        setCompressTempFiles(true);
    }

    @Override // org.apache.poi.xssf.streaming.SXSSFWorkbook, org.apache.poi.ss.usermodel.Workbook
    public void write(OutputStream stream) throws IOException {
        flushSheets();
        EncryptedTempData tempData = new EncryptedTempData();
        try {
            try {
                OutputStream os = tempData.getOutputStream();
                try {
                    getXSSFWorkbook().write(os);
                    IOUtils.closeQuietly(os);
                    ZipEntrySource source = AesZipFileZipEntrySource.createZipEntrySource(tempData.getInputStream());
                    injectData(source, stream);
                    tempData.dispose();
                    IOUtils.closeQuietly(source);
                } catch (Throwable th) {
                    IOUtils.closeQuietly(os);
                    throw th;
                }
            } catch (GeneralSecurityException e) {
                throw new IOException(e);
            }
        } catch (Throwable th2) {
            tempData.dispose();
            IOUtils.closeQuietly(null);
            throw th2;
        }
    }

    @Override // org.apache.poi.xssf.streaming.SXSSFWorkbook
    protected SheetDataWriter createSheetDataWriter() throws IOException {
        LOG.log(3, "isCompressTempFiles: " + isCompressTempFiles());
        LOG.log(3, "SharedStringSource: " + getSharedStringSource());
        return new SheetDataWriterWithDecorator();
    }
}
