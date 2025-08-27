package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.cache.ReadCache;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadWorkbookHolder;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.SheetUtils;
import com.alibaba.excel.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/XlsxSaxAnalyser.class */
public class XlsxSaxAnalyser implements ExcelReadExecutor {
    private AnalysisContext analysisContext;
    private List<ReadSheet> sheetList;
    private Map<Integer, InputStream> sheetMap;
    private StylesTable stylesTable;

    public XlsxSaxAnalyser(AnalysisContext analysisContext, InputStream decryptedStream) throws Exception {
        this.analysisContext = analysisContext;
        ReadWorkbookHolder readWorkbookHolder = analysisContext.readWorkbookHolder();
        OPCPackage pkg = readOpcPackage(readWorkbookHolder, decryptedStream);
        readWorkbookHolder.setOpcPackage(pkg);
        ArrayList<PackagePart> packageParts = pkg.getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());
        if (!CollectionUtils.isEmpty(packageParts)) {
            PackagePart sharedStringsTablePackagePart = packageParts.get(0);
            defaultReadCache(readWorkbookHolder, sharedStringsTablePackagePart);
            analysisSharedStringsTable(sharedStringsTablePackagePart.getInputStream(), readWorkbookHolder);
        }
        XSSFReader xssfReader = new XSSFReader(pkg);
        analysisUse1904WindowDate(xssfReader, readWorkbookHolder);
        this.stylesTable = xssfReader.getStylesTable();
        this.sheetList = new ArrayList();
        this.sheetMap = new HashMap();
        XSSFReader.SheetIterator ite = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        if (!ite.hasNext()) {
            throw new ExcelAnalysisException("Can not find any sheet!");
        }
        while (ite.hasNext()) {
            InputStream inputStream = ite.next();
            this.sheetList.add(new ReadSheet(Integer.valueOf(index), ite.getSheetName()));
            this.sheetMap.put(Integer.valueOf(index), inputStream);
            index++;
        }
    }

    private void defaultReadCache(ReadWorkbookHolder readWorkbookHolder, PackagePart sharedStringsTablePackagePart) {
        ReadCache readCache = readWorkbookHolder.getReadCacheSelector().readCache(sharedStringsTablePackagePart);
        readWorkbookHolder.setReadCache(readCache);
        readCache.init(this.analysisContext);
    }

    private void analysisUse1904WindowDate(XSSFReader xssfReader, ReadWorkbookHolder readWorkbookHolder) throws Exception {
        if (readWorkbookHolder.globalConfiguration().getUse1904windowing() != null) {
            return;
        }
        InputStream workbookXml = xssfReader.getWorkbookData();
        WorkbookDocument ctWorkbook = WorkbookDocument.Factory.parse(workbookXml);
        CTWorkbook wb = ctWorkbook.getWorkbook();
        CTWorkbookPr prefix = wb.getWorkbookPr();
        if (prefix != null && prefix.getDate1904()) {
            readWorkbookHolder.getGlobalConfiguration().setUse1904windowing(Boolean.TRUE);
        } else {
            readWorkbookHolder.getGlobalConfiguration().setUse1904windowing(Boolean.FALSE);
        }
    }

    private void analysisSharedStringsTable(InputStream sharedStringsTableInputStream, ReadWorkbookHolder readWorkbookHolder) throws Exception {
        ContentHandler handler = new SharedStringsTableHandler(readWorkbookHolder.getReadCache());
        parseXmlSource(sharedStringsTableInputStream, handler);
        readWorkbookHolder.getReadCache().putFinished();
    }

    private OPCPackage readOpcPackage(ReadWorkbookHolder readWorkbookHolder, InputStream decryptedStream) throws Exception {
        if (decryptedStream == null && readWorkbookHolder.getFile() != null) {
            return OPCPackage.open(readWorkbookHolder.getFile());
        }
        if (readWorkbookHolder.getMandatoryUseInputStream().booleanValue()) {
            if (decryptedStream != null) {
                return OPCPackage.open(decryptedStream);
            }
            return OPCPackage.open(readWorkbookHolder.getInputStream());
        }
        File readTempFile = FileUtils.createCacheTmpFile();
        readWorkbookHolder.setTempFile(readTempFile);
        File tempFile = new File(readTempFile.getPath(), UUID.randomUUID().toString() + ".xlsx");
        if (decryptedStream != null) {
            FileUtils.writeToFile(tempFile, decryptedStream);
        } else {
            FileUtils.writeToFile(tempFile, readWorkbookHolder.getInputStream());
        }
        return OPCPackage.open(tempFile, PackageAccess.READ);
    }

    @Override // com.alibaba.excel.analysis.ExcelReadExecutor
    public List<ReadSheet> sheetList() {
        return this.sheetList;
    }

    private void parseXmlSource(InputStream inputStream, ContentHandler handler) throws IOException {
        SAXParserFactory saxFactory;
        InputSource inputSource = new InputSource(inputStream);
        try {
            try {
                try {
                    String xlsxSAXParserFactoryName = this.analysisContext.readWorkbookHolder().getXlsxSAXParserFactoryName();
                    if (StringUtils.isEmpty(xlsxSAXParserFactoryName)) {
                        saxFactory = SAXParserFactory.newInstance();
                    } else {
                        saxFactory = SAXParserFactory.newInstance(xlsxSAXParserFactoryName, null);
                    }
                    saxFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                    saxFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                    saxFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                    SAXParser saxParser = saxFactory.newSAXParser();
                    XMLReader xmlReader = saxParser.getXMLReader();
                    xmlReader.setContentHandler(handler);
                    xmlReader.parse(inputSource);
                    inputStream.close();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            throw new ExcelAnalysisException("Can not close 'inputStream'!");
                        }
                    }
                } catch (Exception e2) {
                    throw new ExcelAnalysisException(e2);
                }
            } catch (ExcelAnalysisException e3) {
                throw e3;
            }
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    throw new ExcelAnalysisException("Can not close 'inputStream'!");
                }
            }
            throw th;
        }
    }

    @Override // com.alibaba.excel.analysis.ExcelReadExecutor
    public void execute(List<ReadSheet> readSheetList, Boolean readAll) throws IOException {
        Iterator<ReadSheet> it = this.sheetList.iterator();
        while (it.hasNext()) {
            ReadSheet readSheet = SheetUtils.match(it.next(), readSheetList, readAll, this.analysisContext.readWorkbookHolder().getGlobalConfiguration());
            if (readSheet != null) {
                this.analysisContext.currentSheet(readSheet);
                parseXmlSource(this.sheetMap.get(readSheet.getSheetNo()), new XlsxRowHandler(this.analysisContext, this.stylesTable));
                this.analysisContext.readSheetHolder().notifyAfterAllAnalysed(this.analysisContext);
            }
        }
    }
}
