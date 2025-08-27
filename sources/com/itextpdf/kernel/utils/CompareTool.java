package com.itextpdf.kernel.utils;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.io.util.SystemUtil;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfIndirectReference;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.XMPUtils;
import com.itextpdf.kernel.xmp.options.ParseOptions;
import com.itextpdf.kernel.xmp.options.SerializeOptions;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.servlet.tags.BindErrorsTag;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool.class */
public class CompareTool {
    private static final String cannotOpenOutputDirectory = "Cannot open output directory for <filename>.";
    private static final String gsFailed = "GhostScript failed for <filename>.";
    private static final String unexpectedNumberOfPages = "Unexpected number of pages for <filename>.";
    private static final String differentPages = "File file:///<filename> differs on page <pagenumber>.";
    private static final String undefinedGsPath = "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs9.14/bin/gswin32c.exe\")";
    private static final String ignoredAreasPrefix = "ignored_areas_";
    private static final String gsParams = " -dSAFER -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile='<outputfile>' '<inputfile>'";
    private static final String compareParams = " '<image1>' '<image2>' '<difference>'";
    private static final String versionRegexp = "(iText®( pdfX(FA|fa)| DITO)?|iTextSharp™) (\\d+\\.)+\\d+(-SNAPSHOT)?";
    private static final String versionReplacement = "iText® <version>";
    private static final String copyrightRegexp = "©\\d+-\\d+ iText Group NV";
    private static final String copyrightReplacement = "©<copyright years> iText Group NV";
    private static final String NEW_LINES = "\\r|\\n";
    private String cmpPdf;
    private String cmpPdfName;
    private String cmpImage;
    private String outPdf;
    private String outPdfName;
    private String outImage;
    private ReaderProperties outProps;
    private ReaderProperties cmpProps;
    private List<PdfIndirectReference> outPagesRef;
    private List<PdfIndirectReference> cmpPagesRef;
    private IMetaInfo metaInfo;
    static final /* synthetic */ boolean $assertionsDisabled;
    private int compareByContentErrorsLimit = 1000;
    private boolean generateCompareByContentXmlReport = false;
    private boolean encryptionCompareEnabled = false;
    private boolean useCachedPagesForComparison = true;
    private String gsExec = SystemUtil.getPropertyOrEnvironmentVariable("gsExec");
    private String compareExec = SystemUtil.getPropertyOrEnvironmentVariable("compareExec");

    static {
        $assertionsDisabled = !CompareTool.class.desiredAssertionStatus();
    }

    public CompareResult compareByCatalog(PdfDocument outDocument, PdfDocument cmpDocument) throws IOException {
        CompareResult compareResult = new CompareResult(this.compareByContentErrorsLimit);
        ObjectPath catalogPath = new ObjectPath(cmpDocument.getCatalog().getPdfObject().getIndirectReference(), outDocument.getCatalog().getPdfObject().getIndirectReference());
        Set<PdfName> ignoredCatalogEntries = new LinkedHashSet<>(Arrays.asList(PdfName.Metadata));
        compareDictionariesExtended(outDocument.getCatalog().getPdfObject(), cmpDocument.getCatalog().getPdfObject(), catalogPath, compareResult, ignoredCatalogEntries);
        if (this.cmpPagesRef == null || this.outPagesRef == null) {
            return compareResult;
        }
        if (this.outPagesRef.size() != this.cmpPagesRef.size() && !compareResult.isMessageLimitReached()) {
            compareResult.addError(catalogPath, "Documents have different numbers of pages.");
        }
        for (int i = 0; i < Math.min(this.cmpPagesRef.size(), this.outPagesRef.size()) && !compareResult.isMessageLimitReached(); i++) {
            ObjectPath currentPath = new ObjectPath(this.cmpPagesRef.get(i), this.outPagesRef.get(i));
            PdfDictionary outPageDict = (PdfDictionary) this.outPagesRef.get(i).getRefersTo();
            PdfDictionary cmpPageDict = (PdfDictionary) this.cmpPagesRef.get(i).getRefersTo();
            compareDictionariesExtended(outPageDict, cmpPageDict, currentPath, compareResult);
        }
        return compareResult;
    }

    public CompareTool disableCachedPagesComparison() {
        this.useCachedPagesForComparison = false;
        return this;
    }

    public CompareTool setCompareByContentErrorsLimit(int compareByContentMaxErrorCount) {
        this.compareByContentErrorsLimit = compareByContentMaxErrorCount;
        return this;
    }

    public CompareTool setGenerateCompareByContentXmlReport(boolean generateCompareByContentXmlReport) {
        this.generateCompareByContentXmlReport = generateCompareByContentXmlReport;
        return this;
    }

    public void setEventCountingMetaInfo(IMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public CompareTool enableEncryptionCompare() {
        this.encryptionCompareEnabled = true;
        return this;
    }

    public ReaderProperties getOutReaderProperties() {
        if (this.outProps == null) {
            this.outProps = new ReaderProperties();
        }
        return this.outProps;
    }

    public ReaderProperties getCmpReaderProperties() {
        if (this.cmpProps == null) {
            this.cmpProps = new ReaderProperties();
        }
        return this.cmpProps;
    }

    public String compareVisually(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws InterruptedException, IOException {
        return compareVisually(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
    }

    public String compareVisually(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws InterruptedException, IOException {
        init(outPdf, cmpPdf);
        System.out.println("Out pdf: file:///" + UrlUtil.toNormalizedURI(outPdf).getPath());
        System.out.println("Cmp pdf: file:///" + UrlUtil.toNormalizedURI(cmpPdf).getPath() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return compareVisually(outPath, differenceImagePrefix, ignoredAreas);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath) throws InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, null, null, null, null);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, null, null, null);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, byte[] outPass, byte[] cmpPass) throws InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, null, outPass, cmpPass);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, ignoredAreas, null, null);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas, byte[] outPass, byte[] cmpPass) throws InterruptedException, IOException {
        init(outPdf, cmpPdf);
        System.out.println("Out pdf: file:///" + UrlUtil.toNormalizedURI(outPdf).getPath());
        System.out.println("Cmp pdf: file:///" + UrlUtil.toNormalizedURI(cmpPdf).getPath() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        setPassword(outPass, cmpPass);
        return compareByContent(outPath, differenceImagePrefix, ignoredAreas);
    }

    public boolean compareDictionaries(PdfDictionary outDict, PdfDictionary cmpDict) throws IOException {
        return compareDictionariesExtended(outDict, cmpDict, null, null);
    }

    public CompareResult compareDictionariesStructure(PdfDictionary outDict, PdfDictionary cmpDict) {
        return compareDictionariesStructure(outDict, cmpDict, null);
    }

    public CompareResult compareDictionariesStructure(PdfDictionary outDict, PdfDictionary cmpDict, Set<PdfName> excludedKeys) {
        if (outDict.getIndirectReference() == null || cmpDict.getIndirectReference() == null) {
            throw new IllegalArgumentException("The 'outDict' and 'cmpDict' objects shall have indirect references.");
        }
        CompareResult compareResult = new CompareResult(this.compareByContentErrorsLimit);
        ObjectPath currentPath = new ObjectPath(cmpDict.getIndirectReference(), outDict.getIndirectReference());
        if (!compareDictionariesExtended(outDict, cmpDict, currentPath, compareResult, excludedKeys)) {
            if (!$assertionsDisabled && compareResult.isOk()) {
                throw new AssertionError();
            }
            System.out.println(compareResult.getReport());
            return compareResult;
        }
        if ($assertionsDisabled || compareResult.isOk()) {
            return null;
        }
        throw new AssertionError();
    }

    public boolean compareStreams(PdfStream outStream, PdfStream cmpStream) throws IOException {
        return compareStreamsExtended(outStream, cmpStream, null, null);
    }

    public boolean compareArrays(PdfArray outArray, PdfArray cmpArray) throws IOException {
        return compareArraysExtended(outArray, cmpArray, null, null);
    }

    public boolean compareNames(PdfName outName, PdfName cmpName) {
        return cmpName.equals(outName);
    }

    public boolean compareNumbers(PdfNumber outNumber, PdfNumber cmpNumber) {
        return cmpNumber.getValue() == outNumber.getValue();
    }

    public boolean compareStrings(PdfString outString, PdfString cmpString) {
        return cmpString.getValue().equals(outString.getValue());
    }

    public boolean compareBooleans(PdfBoolean outBoolean, PdfBoolean cmpBoolean) {
        return cmpBoolean.getValue() == outBoolean.getValue();
    }

    public String compareXmp(String outPdf, String cmpPdf) {
        return compareXmp(outPdf, cmpPdf, false);
    }

    public String compareXmp(String outPdf, String cmpPdf, boolean ignoreDateAndProducerProperties) {
        init(outPdf, cmpPdf);
        PdfDocument cmpDocument = null;
        PdfDocument outDocument = null;
        try {
            cmpDocument = new PdfDocument(new PdfReader(this.cmpPdf), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
            outDocument = new PdfDocument(new PdfReader(this.outPdf), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
            byte[] cmpBytes = cmpDocument.getXmpMetadata();
            byte[] outBytes = outDocument.getXmpMetadata();
            if (ignoreDateAndProducerProperties) {
                XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(cmpBytes, new ParseOptions().setOmitNormalization(true));
                XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", PdfConst.CreateDate, true, true);
                XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, true, true);
                XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", PdfConst.MetadataDate, true, true);
                XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/pdf/1.3/", PdfConst.Producer, true, true);
                cmpBytes = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(8192));
                XMPMeta xmpMeta2 = XMPMetaFactory.parseFromBuffer(outBytes, new ParseOptions().setOmitNormalization(true));
                XMPUtils.removeProperties(xmpMeta2, "http://ns.adobe.com/xap/1.0/", PdfConst.CreateDate, true, true);
                XMPUtils.removeProperties(xmpMeta2, "http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, true, true);
                XMPUtils.removeProperties(xmpMeta2, "http://ns.adobe.com/xap/1.0/", PdfConst.MetadataDate, true, true);
                XMPUtils.removeProperties(xmpMeta2, "http://ns.adobe.com/pdf/1.3/", PdfConst.Producer, true, true);
                outBytes = XMPMetaFactory.serializeToBuffer(xmpMeta2, new SerializeOptions(8192));
            }
            if (!compareXmls(cmpBytes, outBytes)) {
                if (cmpDocument != null) {
                    cmpDocument.close();
                }
                if (outDocument != null) {
                    outDocument.close();
                }
                return "The XMP packages different!";
            }
            if (cmpDocument != null) {
                cmpDocument.close();
            }
            if (outDocument == null) {
                return null;
            }
            outDocument.close();
            return null;
        } catch (Exception e) {
            if (cmpDocument != null) {
                cmpDocument.close();
            }
            if (outDocument != null) {
                outDocument.close();
            }
            return "XMP parsing failure!";
        } catch (Throwable th) {
            if (cmpDocument != null) {
                cmpDocument.close();
            }
            if (outDocument != null) {
                outDocument.close();
            }
            throw th;
        }
    }

    public boolean compareXmls(byte[] xml1, byte[] xml2) throws ParserConfigurationException, SAXException, IOException {
        return XmlUtils.compareXmls(new ByteArrayInputStream(xml1), new ByteArrayInputStream(xml2));
    }

    public boolean compareXmls(String outXmlFile, String cmpXmlFile) throws ParserConfigurationException, SAXException, IOException {
        System.out.println("Out xml: file:///" + UrlUtil.toNormalizedURI(outXmlFile).getPath());
        System.out.println("Cmp xml: file:///" + UrlUtil.toNormalizedURI(cmpXmlFile).getPath() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        return XmlUtils.compareXmls(new FileInputStream(outXmlFile), new FileInputStream(cmpXmlFile));
    }

    public String compareDocumentInfo(String outPdf, String cmpPdf, byte[] outPass, byte[] cmpPass) throws IOException {
        System.out.print("[itext] INFO  Comparing document info.......");
        String message = null;
        setPassword(outPass, cmpPass);
        PdfDocument outDocument = new PdfDocument(new PdfReader(outPdf, getOutReaderProperties()), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        PdfDocument cmpDocument = new PdfDocument(new PdfReader(cmpPdf, getCmpReaderProperties()), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        String[] cmpInfo = convertInfo(cmpDocument.getDocumentInfo());
        String[] outInfo = convertInfo(outDocument.getDocumentInfo());
        int i = 0;
        while (true) {
            if (i >= cmpInfo.length) {
                break;
            }
            if (cmpInfo[i].equals(outInfo[i])) {
                i++;
            } else {
                message = MessageFormatUtil.format("Document info fail. Expected: \"{0}\", actual: \"{1}\"", cmpInfo[i], outInfo[i]);
                break;
            }
        }
        outDocument.close();
        cmpDocument.close();
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    public String compareDocumentInfo(String outPdf, String cmpPdf) throws IOException {
        return compareDocumentInfo(outPdf, cmpPdf, null, null);
    }

    public String compareLinkAnnotations(String outPdf, String cmpPdf) throws IOException {
        System.out.print("[itext] INFO  Comparing link annotations....");
        String message = null;
        PdfDocument outDocument = new PdfDocument(new PdfReader(outPdf), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        PdfDocument cmpDocument = new PdfDocument(new PdfReader(cmpPdf), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        int i = 0;
        while (true) {
            if (i >= outDocument.getNumberOfPages() || i >= cmpDocument.getNumberOfPages()) {
                break;
            }
            List<PdfLinkAnnotation> outLinks = getLinkAnnotations(i + 1, outDocument);
            List<PdfLinkAnnotation> cmpLinks = getLinkAnnotations(i + 1, cmpDocument);
            if (cmpLinks.size() != outLinks.size()) {
                message = MessageFormatUtil.format("Different number of links on page {0}.", Integer.valueOf(i + 1));
                break;
            }
            int j = 0;
            while (true) {
                if (j >= cmpLinks.size()) {
                    break;
                }
                if (compareLinkAnnotations(cmpLinks.get(j), outLinks.get(j), cmpDocument, outDocument)) {
                    j++;
                } else {
                    message = MessageFormatUtil.format("Different links on page {0}.\n{1}\n{2}", Integer.valueOf(i + 1), cmpLinks.get(j).toString(), outLinks.get(j).toString());
                    break;
                }
            }
            i++;
        }
        outDocument.close();
        cmpDocument.close();
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    public String compareTagStructures(String outPdf, String cmpPdf) throws ParserConfigurationException, SAXException, IOException {
        System.out.print("[itext] INFO  Comparing tag structures......");
        String outXmlPath = outPdf.replace(".pdf", ".xml");
        String cmpXmlPath = outPdf.replace(".pdf", ".cmp.xml");
        String message = null;
        PdfReader readerOut = new PdfReader(outPdf);
        PdfDocument docOut = new PdfDocument(readerOut, new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        FileOutputStream xmlOut = new FileOutputStream(outXmlPath);
        new TaggedPdfReaderTool(docOut).setRootTag(OgnlContext.ROOT_CONTEXT_KEY).convertToXml(xmlOut);
        docOut.close();
        xmlOut.close();
        PdfReader readerCmp = new PdfReader(cmpPdf);
        PdfDocument docCmp = new PdfDocument(readerCmp, new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
        FileOutputStream xmlCmp = new FileOutputStream(cmpXmlPath);
        new TaggedPdfReaderTool(docCmp).setRootTag(OgnlContext.ROOT_CONTEXT_KEY).convertToXml(xmlCmp);
        docCmp.close();
        xmlCmp.close();
        if (!compareXmls(outXmlPath, cmpXmlPath)) {
            message = "The tag structures are different.";
        }
        if (message == null) {
            System.out.println("OK");
        } else {
            System.out.println("Fail");
        }
        System.out.flush();
        return message;
    }

    String[] convertInfo(PdfDocumentInfo info) {
        String[] convertedInfo = {"", "", "", "", ""};
        String infoValue = info.getTitle();
        if (infoValue != null) {
            convertedInfo[0] = infoValue;
        }
        String infoValue2 = info.getAuthor();
        if (infoValue2 != null) {
            convertedInfo[1] = infoValue2;
        }
        String infoValue3 = info.getSubject();
        if (infoValue3 != null) {
            convertedInfo[2] = infoValue3;
        }
        String infoValue4 = info.getKeywords();
        if (infoValue4 != null) {
            convertedInfo[3] = infoValue4;
        }
        String infoValue5 = info.getProducer();
        if (infoValue5 != null) {
            convertedInfo[4] = convertProducerLine(infoValue5);
        }
        return convertedInfo;
    }

    String convertProducerLine(String producer) {
        return producer.replaceAll(versionRegexp, versionReplacement).replaceAll(copyrightRegexp, copyrightReplacement);
    }

    private void init(String outPdf, String cmpPdf) {
        this.outPdf = outPdf;
        this.cmpPdf = cmpPdf;
        this.outPdfName = new File(outPdf).getName();
        this.cmpPdfName = new File(cmpPdf).getName();
        this.outImage = this.outPdfName + "-%03d.png";
        if (!this.cmpPdfName.startsWith("cmp_")) {
            this.cmpImage = "cmp_" + this.cmpPdfName + "-%03d.png";
        } else {
            this.cmpImage = this.cmpPdfName + "-%03d.png";
        }
    }

    private void setPassword(byte[] outPass, byte[] cmpPass) {
        if (outPass != null) {
            getOutReaderProperties().setPassword(outPass);
        }
        if (cmpPass != null) {
            getCmpReaderProperties().setPassword(outPass);
        }
    }

    private String compareVisually(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws InterruptedException, IOException {
        return compareVisually(outPath, differenceImagePrefix, ignoredAreas, (List<Integer>) null);
    }

    private String compareVisually(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas, List<Integer> equalPages) throws InterruptedException, IOException {
        if (this.gsExec == null) {
            throw new CompareToolExecutionException(undefinedGsPath);
        }
        if (!new File(this.gsExec).canExecute()) {
            throw new CompareToolExecutionException(new File(this.gsExec).getAbsolutePath() + " is not an executable program");
        }
        if (!outPath.endsWith("/")) {
            outPath = outPath + "/";
        }
        if (differenceImagePrefix == null) {
            String fileBasedPrefix = "";
            if (this.outPdfName != null) {
                fileBasedPrefix = this.outPdfName + "_";
            }
            differenceImagePrefix = "diff_" + fileBasedPrefix;
        }
        prepareOutputDirs(outPath, differenceImagePrefix);
        System.out.println("Comparing visually..........");
        if (ignoredAreas != null && !ignoredAreas.isEmpty()) {
            createIgnoredAreasPdfs(outPath, ignoredAreas);
        }
        runGhostScriptImageGeneration(outPath);
        return compareImagesOfPdfs(outPath, differenceImagePrefix, equalPages);
    }

    private String compareImagesOfPdfs(String outPath, String differenceImagePrefix, List<Integer> equalPages) throws InterruptedException, IOException {
        File[] imageFiles = FileUtil.listFilesInDirectoryByFilter(outPath, new PngFileFilter());
        File[] cmpImageFiles = FileUtil.listFilesInDirectoryByFilter(outPath, new CmpPngFileFilter());
        boolean bUnexpectedNumberOfPages = false;
        if (imageFiles.length != cmpImageFiles.length) {
            bUnexpectedNumberOfPages = true;
        }
        int cnt = Math.min(imageFiles.length, cmpImageFiles.length);
        if (cnt < 1) {
            throw new CompareToolExecutionException("No files for comparing. The result or sample pdf file is not processed by GhostScript.");
        }
        Arrays.sort(imageFiles, new ImageNameComparator());
        Arrays.sort(cmpImageFiles, new ImageNameComparator());
        String differentPagesFail = null;
        boolean compareExecIsOk = this.compareExec != null && new File(this.compareExec).canExecute();
        if (this.compareExec != null && !compareExecIsOk) {
            throw new CompareToolExecutionException(new File(this.compareExec).getAbsolutePath() + " is not an executable program");
        }
        List<Integer> diffPages = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            if (equalPages == null || !equalPages.contains(Integer.valueOf(i))) {
                System.out.println("Comparing page " + Integer.toString(i + 1) + ": file:///" + UrlUtil.toNormalizedURI(imageFiles[i]).getPath() + " ...");
                FileInputStream is1 = new FileInputStream(imageFiles[i].getAbsolutePath());
                FileInputStream is2 = new FileInputStream(cmpImageFiles[i].getAbsolutePath());
                boolean cmpResult = compareStreams(is1, is2);
                is1.close();
                is2.close();
                if (!cmpResult) {
                    differentPagesFail = "Page is different!";
                    diffPages.add(Integer.valueOf(i + 1));
                    if (compareExecIsOk) {
                        String currCompareParams = compareParams.replace("<image1>", imageFiles[i].getAbsolutePath()).replace("<image2>", cmpImageFiles[i].getAbsolutePath()).replace("<difference>", outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png");
                        if (!SystemUtil.runProcessAndWait(this.compareExec, currCompareParams)) {
                            differentPagesFail = differentPagesFail + "\nPlease, examine " + outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png for more details.";
                        }
                    }
                    System.out.println(differentPagesFail);
                } else {
                    System.out.println(" done.");
                }
            }
        }
        if (differentPagesFail != null) {
            String errorMessage = differentPages.replace("<filename>", UrlUtil.toNormalizedURI(this.outPdf).getPath()).replace("<pagenumber>", listDiffPagesAsString(diffPages));
            if (!compareExecIsOk) {
                errorMessage = errorMessage + "\nYou can optionally specify path to ImageMagick compare tool (e.g. -DcompareExec=\"C:/Program Files/ImageMagick-6.5.4-2/compare.exe\") to visualize differences.";
            }
            return errorMessage;
        }
        if (bUnexpectedNumberOfPages) {
            return unexpectedNumberOfPages.replace("<filename>", this.outPdf);
        }
        return null;
    }

    private String listDiffPagesAsString(List<Integer> diffPages) {
        StringBuilder sb = new StringBuilder(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int i = 0; i < diffPages.size(); i++) {
            sb.append(diffPages.get(i));
            if (i < diffPages.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private void createIgnoredAreasPdfs(String outPath, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException {
        PdfWriter outWriter = new PdfWriter(outPath + ignoredAreasPrefix + this.outPdfName);
        PdfWriter cmpWriter = new PdfWriter(outPath + ignoredAreasPrefix + this.cmpPdfName);
        StampingProperties properties = new StampingProperties();
        properties.setEventCountingMetaInfo(this.metaInfo);
        PdfDocument pdfOutDoc = new PdfDocument(new PdfReader(this.outPdf), outWriter, properties);
        PdfDocument pdfCmpDoc = new PdfDocument(new PdfReader(this.cmpPdf), cmpWriter, properties);
        for (Map.Entry<Integer, List<Rectangle>> entry : ignoredAreas.entrySet()) {
            int pageNumber = entry.getKey().intValue();
            List<Rectangle> rectangles = entry.getValue();
            if (rectangles != null && !rectangles.isEmpty()) {
                PdfCanvas outCanvas = new PdfCanvas(pdfOutDoc.getPage(pageNumber));
                PdfCanvas cmpCanvas = new PdfCanvas(pdfCmpDoc.getPage(pageNumber));
                outCanvas.saveState();
                cmpCanvas.saveState();
                for (Rectangle rect : rectangles) {
                    outCanvas.rectangle(rect).fill();
                    cmpCanvas.rectangle(rect).fill();
                }
                outCanvas.restoreState();
                cmpCanvas.restoreState();
            }
        }
        pdfOutDoc.close();
        pdfCmpDoc.close();
        init(outPath + ignoredAreasPrefix + this.outPdfName, outPath + ignoredAreasPrefix + this.cmpPdfName);
    }

    private void prepareOutputDirs(String outPath, String differenceImagePrefix) {
        if (!FileUtil.directoryExists(outPath)) {
            FileUtil.createDirectories(outPath);
            return;
        }
        File[] imageFiles = FileUtil.listFilesInDirectoryByFilter(outPath, new PngFileFilter());
        for (File file : imageFiles) {
            file.delete();
        }
        File[] cmpImageFiles = FileUtil.listFilesInDirectoryByFilter(outPath, new CmpPngFileFilter());
        for (File file2 : cmpImageFiles) {
            file2.delete();
        }
        File[] diffFiles = FileUtil.listFilesInDirectoryByFilter(outPath, new DiffPngFileFilter(differenceImagePrefix));
        for (File file3 : diffFiles) {
            file3.delete();
        }
    }

    private void runGhostScriptImageGeneration(String outPath) throws InterruptedException, IOException {
        if (!FileUtil.directoryExists(outPath)) {
            throw new CompareToolExecutionException(cannotOpenOutputDirectory.replace("<filename>", this.outPdf));
        }
        String currGsParams = gsParams.replace("<outputfile>", outPath + this.cmpImage).replace("<inputfile>", this.cmpPdf);
        if (!SystemUtil.runProcessAndWait(this.gsExec, currGsParams)) {
            throw new CompareToolExecutionException(gsFailed.replace("<filename>", this.cmpPdf));
        }
        String currGsParams2 = gsParams.replace("<outputfile>", outPath + this.outImage).replace("<inputfile>", this.outPdf);
        if (!SystemUtil.runProcessAndWait(this.gsExec, currGsParams2)) {
            throw new CompareToolExecutionException(gsFailed.replace("<filename>", this.outPdf));
        }
    }

    private void printOutCmpDirectories() {
        System.out.println("Out file folder: file:///" + UrlUtil.toNormalizedURI(new File(this.outPdf).getParentFile()).getPath());
        System.out.println("Cmp file folder: file:///" + UrlUtil.toNormalizedURI(new File(this.cmpPdf).getParentFile()).getPath());
    }

    private String compareByContent(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws InterruptedException, IOException {
        printOutCmpDirectories();
        System.out.print("Comparing by content..........");
        try {
            PdfDocument outDocument = new PdfDocument(new PdfReader(this.outPdf, getOutReaderProperties()), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
            List<PdfDictionary> outPages = new ArrayList<>();
            this.outPagesRef = new ArrayList();
            loadPagesFromReader(outDocument, outPages, this.outPagesRef);
            try {
                PdfDocument cmpDocument = new PdfDocument(new PdfReader(this.cmpPdf, getCmpReaderProperties()), new DocumentProperties().setEventCountingMetaInfo(this.metaInfo));
                List<PdfDictionary> cmpPages = new ArrayList<>();
                this.cmpPagesRef = new ArrayList();
                loadPagesFromReader(cmpDocument, cmpPages, this.cmpPagesRef);
                if (outPages.size() != cmpPages.size()) {
                    return compareVisuallyAndCombineReports("Documents have different numbers of pages.", outPath, differenceImagePrefix, ignoredAreas, null);
                }
                CompareResult compareResult = new CompareResult(this.compareByContentErrorsLimit);
                List<Integer> equalPages = new ArrayList<>(cmpPages.size());
                for (int i = 0; i < cmpPages.size(); i++) {
                    ObjectPath currentPath = new ObjectPath(this.cmpPagesRef.get(i), this.outPagesRef.get(i));
                    if (compareDictionariesExtended(outPages.get(i), cmpPages.get(i), currentPath, compareResult)) {
                        equalPages.add(Integer.valueOf(i));
                    }
                }
                ObjectPath catalogPath = new ObjectPath(cmpDocument.getCatalog().getPdfObject().getIndirectReference(), outDocument.getCatalog().getPdfObject().getIndirectReference());
                Set<PdfName> ignoredCatalogEntries = new LinkedHashSet<>(Arrays.asList(PdfName.Pages, PdfName.Metadata));
                compareDictionariesExtended(outDocument.getCatalog().getPdfObject(), cmpDocument.getCatalog().getPdfObject(), catalogPath, compareResult, ignoredCatalogEntries);
                if (this.encryptionCompareEnabled) {
                    compareDocumentsEncryption(outDocument, cmpDocument, compareResult);
                }
                outDocument.close();
                cmpDocument.close();
                if (this.generateCompareByContentXmlReport) {
                    String outPdfName = new File(this.outPdf).getName();
                    FileOutputStream xml = new FileOutputStream(outPath + "/" + outPdfName.substring(0, outPdfName.length() - 3) + "report.xml");
                    try {
                        try {
                            compareResult.writeReportToXml(xml);
                            xml.close();
                        } catch (Throwable th) {
                            xml.close();
                            throw th;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                if (equalPages.size() == cmpPages.size() && compareResult.isOk()) {
                    System.out.println("OK");
                    System.out.flush();
                    return null;
                }
                return compareVisuallyAndCombineReports(compareResult.getReport(), outPath, differenceImagePrefix, ignoredAreas, equalPages);
            } catch (IOException e2) {
                throw new IOException("File \"" + this.cmpPdf + "\" not found", e2);
            }
        } catch (IOException e3) {
            throw new IOException("File \"" + this.outPdf + "\" not found", e3);
        }
    }

    private String compareVisuallyAndCombineReports(String compareByFailContentReason, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas, List<Integer> equalPages) throws InterruptedException, IOException {
        System.out.println("Fail");
        System.out.flush();
        String compareByContentReport = "Compare by content report:\n" + compareByFailContentReason;
        System.out.println(compareByContentReport);
        System.out.flush();
        String message = compareVisually(outPath, differenceImagePrefix, ignoredAreas, equalPages);
        if (message == null || message.length() == 0) {
            return "Compare by content fails. No visual differences";
        }
        return message;
    }

    private void loadPagesFromReader(PdfDocument doc, List<PdfDictionary> pages, List<PdfIndirectReference> pagesRef) {
        int numOfPages = doc.getNumberOfPages();
        for (int i = 0; i < numOfPages; i++) {
            pages.add(doc.getPage(i + 1).getPdfObject());
            pagesRef.add(pages.get(i).getIndirectReference());
        }
    }

    private void compareDocumentsEncryption(PdfDocument outDocument, PdfDocument cmpDocument, CompareResult compareResult) {
        PdfDictionary outEncrypt = outDocument.getTrailer().getAsDictionary(PdfName.Encrypt);
        PdfDictionary cmpEncrypt = cmpDocument.getTrailer().getAsDictionary(PdfName.Encrypt);
        if (outEncrypt == null && cmpEncrypt == null) {
            return;
        }
        TrailerPath trailerPath = new TrailerPath(cmpDocument, outDocument);
        if (outEncrypt == null) {
            compareResult.addError(trailerPath, "Expected encrypted document.");
            return;
        }
        if (cmpEncrypt == null) {
            compareResult.addError(trailerPath, "Expected not encrypted document.");
            return;
        }
        Set<PdfName> ignoredEncryptEntries = new LinkedHashSet<>(Arrays.asList(PdfName.O, PdfName.U, PdfName.OE, PdfName.UE, PdfName.Perms, PdfName.CF, PdfName.Recipients));
        ObjectPath objectPath = new ObjectPath(outEncrypt.getIndirectReference(), cmpEncrypt.getIndirectReference());
        compareDictionariesExtended(outEncrypt, cmpEncrypt, objectPath, compareResult, ignoredEncryptEntries);
        PdfDictionary outCfDict = outEncrypt.getAsDictionary(PdfName.CF);
        PdfDictionary cmpCfDict = cmpEncrypt.getAsDictionary(PdfName.CF);
        if (cmpCfDict != null || outCfDict != null) {
            if ((cmpCfDict != null && outCfDict == null) || cmpCfDict == null) {
                compareResult.addError(objectPath, "One of the dictionaries is null, the other is not.");
                return;
            }
            Set<PdfName> mergedKeys = new TreeSet<>(outCfDict.keySet());
            mergedKeys.addAll(cmpCfDict.keySet());
            for (PdfName key : mergedKeys) {
                objectPath.pushDictItemToPath(key);
                LinkedHashSet<PdfName> excludedKeys = new LinkedHashSet<>(Arrays.asList(PdfName.Recipients));
                compareDictionariesExtended(outCfDict.getAsDictionary(key), cmpCfDict.getAsDictionary(key), objectPath, compareResult, excludedKeys);
                objectPath.pop();
            }
        }
    }

    private boolean compareStreams(InputStream is1, InputStream is2) throws IOException {
        int len1;
        byte[] buffer1 = new byte[65536];
        byte[] buffer2 = new byte[65536];
        do {
            len1 = is1.read(buffer1);
            int len2 = is2.read(buffer2);
            if (len1 != len2 || !Arrays.equals(buffer1, buffer2)) {
                return false;
            }
        } while (len1 != -1);
        return true;
    }

    private boolean compareDictionariesExtended(PdfDictionary outDict, PdfDictionary cmpDict, ObjectPath currentPath, CompareResult compareResult) {
        return compareDictionariesExtended(outDict, cmpDict, currentPath, compareResult, null);
    }

    private boolean compareDictionariesExtended(PdfDictionary outDict, PdfDictionary cmpDict, ObjectPath currentPath, CompareResult compareResult, Set<PdfName> excludedKeys) {
        PdfObject cmpObj;
        if ((cmpDict != null && outDict == null) || (outDict != null && cmpDict == null)) {
            compareResult.addError(currentPath, "One of the dictionaries is null, the other is not.");
            return false;
        }
        boolean dictsAreSame = true;
        Set<PdfName> mergedKeys = new TreeSet<>(cmpDict.keySet());
        mergedKeys.addAll(outDict.keySet());
        for (PdfName key : mergedKeys) {
            if (!dictsAreSame && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached())) {
                return false;
            }
            if (excludedKeys == null || !excludedKeys.contains(key)) {
                if (!key.equals(PdfName.Parent) && !key.equals(PdfName.P) && !key.equals(PdfName.ModDate) && (!outDict.isStream() || !cmpDict.isStream() || (!key.equals(PdfName.Filter) && !key.equals(PdfName.Length)))) {
                    if ((key.equals(PdfName.BaseFont) || key.equals(PdfName.FontName)) && (cmpObj = cmpDict.get(key)) != null && cmpObj.isName() && cmpObj.toString().indexOf(43) > 0) {
                        PdfObject outObj = outDict.get(key);
                        if (!outObj.isName() || outObj.toString().indexOf(43) == -1) {
                            if (compareResult != null && currentPath != null) {
                                compareResult.addError(currentPath, MessageFormatUtil.format("PdfDictionary {0} entry: Expected: {1}. Found: {2}", key.toString(), cmpObj.toString(), outObj.toString()));
                            }
                            dictsAreSame = false;
                        } else {
                            String cmpName = cmpObj.toString().substring(cmpObj.toString().indexOf(43));
                            String outName = outObj.toString().substring(outObj.toString().indexOf(43));
                            if (!cmpName.equals(outName)) {
                                if (compareResult != null && currentPath != null) {
                                    compareResult.addError(currentPath, MessageFormatUtil.format("PdfDictionary {0} entry: Expected: {1}. Found: {2}", key.toString(), cmpObj.toString(), outObj.toString()));
                                }
                                dictsAreSame = false;
                            }
                        }
                    } else if (key.equals(PdfName.ParentTree) || key.equals(PdfName.PageLabels)) {
                        if (currentPath != null) {
                            currentPath.pushDictItemToPath(key);
                        }
                        PdfDictionary outNumTree = outDict.getAsDictionary(key);
                        PdfDictionary cmpNumTree = cmpDict.getAsDictionary(key);
                        LinkedList<PdfObject> outItems = new LinkedList<>();
                        LinkedList<PdfObject> cmpItems = new LinkedList<>();
                        PdfNumber outLeftover = flattenNumTree(outNumTree, null, outItems);
                        PdfNumber cmpLeftover = flattenNumTree(cmpNumTree, null, cmpItems);
                        if (outLeftover != null) {
                            LoggerFactory.getLogger((Class<?>) CompareTool.class).warn(LogMessageConstant.NUM_TREE_SHALL_NOT_END_WITH_KEY);
                            if (cmpLeftover == null) {
                                if (compareResult != null && currentPath != null) {
                                    compareResult.addError(currentPath, "Number tree unexpectedly ends with a key");
                                }
                                dictsAreSame = false;
                            }
                        }
                        if (cmpLeftover != null) {
                            LoggerFactory.getLogger((Class<?>) CompareTool.class).warn(LogMessageConstant.NUM_TREE_SHALL_NOT_END_WITH_KEY);
                            if (outLeftover == null) {
                                if (compareResult != null && currentPath != null) {
                                    compareResult.addError(currentPath, "Number tree was expected to end with a key (although it is invalid according to the specification), but ended with a value");
                                }
                                dictsAreSame = false;
                            }
                        }
                        if (outLeftover != null && cmpLeftover != null && !compareNumbers(outLeftover, cmpLeftover)) {
                            if (compareResult != null && currentPath != null) {
                                compareResult.addError(currentPath, "Number tree was expected to end with a different key (although it is invalid according to the specification)");
                            }
                            dictsAreSame = false;
                        }
                        PdfArray outArray = new PdfArray(outItems, outItems.size());
                        PdfArray cmpArray = new PdfArray(cmpItems, cmpItems.size());
                        if (!compareArraysExtended(outArray, cmpArray, currentPath, compareResult)) {
                            if (compareResult != null && currentPath != null) {
                                compareResult.addError(currentPath, "Number trees were flattened, compared and found to be different.");
                            }
                            dictsAreSame = false;
                        }
                        if (currentPath != null) {
                            currentPath.pop();
                        }
                    } else {
                        if (currentPath != null) {
                            currentPath.pushDictItemToPath(key);
                        }
                        dictsAreSame = compareObjects(outDict.get(key, false), cmpDict.get(key, false), currentPath, compareResult) && dictsAreSame;
                        if (currentPath != null) {
                            currentPath.pop();
                        }
                    }
                }
            }
        }
        return dictsAreSame;
    }

    private PdfNumber flattenNumTree(PdfDictionary dictionary, PdfNumber leftOver, LinkedList<PdfObject> items) {
        PdfNumber number;
        PdfArray nums = dictionary.getAsArray(PdfName.Nums);
        if (nums != null) {
            int k = 0;
            while (k < nums.size()) {
                if (leftOver == null) {
                    int i = k;
                    k++;
                    number = nums.getAsNumber(i);
                } else {
                    number = leftOver;
                    leftOver = null;
                }
                if (k < nums.size()) {
                    items.addLast(number);
                    items.addLast(nums.get(k, false));
                    k++;
                } else {
                    return number;
                }
            }
            return null;
        }
        PdfArray nums2 = dictionary.getAsArray(PdfName.Kids);
        if (nums2 != null) {
            for (int k2 = 0; k2 < nums2.size(); k2++) {
                PdfDictionary kid = nums2.getAsDictionary(k2);
                leftOver = flattenNumTree(kid, leftOver, items);
            }
            return null;
        }
        return null;
    }

    private boolean compareObjects(PdfObject outObj, PdfObject cmpObj, ObjectPath currentPath, CompareResult compareResult) {
        PdfObject outDirectObj = null;
        PdfObject cmpDirectObj = null;
        if (outObj != null) {
            outDirectObj = outObj.isIndirectReference() ? ((PdfIndirectReference) outObj).getRefersTo(false) : outObj;
        }
        if (cmpObj != null) {
            cmpDirectObj = cmpObj.isIndirectReference() ? ((PdfIndirectReference) cmpObj).getRefersTo(false) : cmpObj;
        }
        if (cmpDirectObj == null && outDirectObj == null) {
            return true;
        }
        if (outDirectObj == null) {
            compareResult.addError(currentPath, "Expected object was not found.");
            return false;
        }
        if (cmpDirectObj == null) {
            compareResult.addError(currentPath, "Found object which was not expected to be found.");
            return false;
        }
        if (cmpDirectObj.getType() != outDirectObj.getType()) {
            compareResult.addError(currentPath, MessageFormatUtil.format("Types do not match. Expected: {0}. Found: {1}.", cmpDirectObj.getClass().getSimpleName(), outDirectObj.getClass().getSimpleName()));
            return false;
        }
        if (cmpObj.isIndirectReference() && !outObj.isIndirectReference()) {
            compareResult.addError(currentPath, "Expected indirect object.");
            return false;
        }
        if (!cmpObj.isIndirectReference() && outObj.isIndirectReference()) {
            compareResult.addError(currentPath, "Expected direct object.");
            return false;
        }
        if (currentPath != null && cmpObj.isIndirectReference() && outObj.isIndirectReference()) {
            if (currentPath.isComparing((PdfIndirectReference) cmpObj, (PdfIndirectReference) outObj)) {
                return true;
            }
            currentPath = currentPath.resetDirectPath((PdfIndirectReference) cmpObj, (PdfIndirectReference) outObj);
        }
        if (cmpDirectObj.isDictionary() && PdfName.Page.equals(((PdfDictionary) cmpDirectObj).getAsName(PdfName.Type)) && this.useCachedPagesForComparison) {
            if (!outDirectObj.isDictionary() || !PdfName.Page.equals(((PdfDictionary) outDirectObj).getAsName(PdfName.Type))) {
                if (compareResult != null && currentPath != null) {
                    compareResult.addError(currentPath, "Expected a page. Found not a page.");
                    return false;
                }
                return false;
            }
            PdfIndirectReference cmpRefKey = cmpObj.isIndirectReference() ? (PdfIndirectReference) cmpObj : cmpObj.getIndirectReference();
            PdfIndirectReference outRefKey = outObj.isIndirectReference() ? (PdfIndirectReference) outObj : outObj.getIndirectReference();
            if (this.cmpPagesRef == null) {
                this.cmpPagesRef = new ArrayList();
                for (int i = 1; i <= cmpRefKey.getDocument().getNumberOfPages(); i++) {
                    this.cmpPagesRef.add(cmpRefKey.getDocument().getPage(i).getPdfObject().getIndirectReference());
                }
            }
            if (this.outPagesRef == null) {
                this.outPagesRef = new ArrayList();
                for (int i2 = 1; i2 <= outRefKey.getDocument().getNumberOfPages(); i2++) {
                    this.outPagesRef.add(outRefKey.getDocument().getPage(i2).getPdfObject().getIndirectReference());
                }
            }
            if (this.cmpPagesRef.contains(cmpRefKey) || this.outPagesRef.contains(outRefKey)) {
                if (this.cmpPagesRef.contains(cmpRefKey) && this.cmpPagesRef.indexOf(cmpRefKey) == this.outPagesRef.indexOf(outRefKey)) {
                    return true;
                }
                if (compareResult != null && currentPath != null) {
                    compareResult.addError(currentPath, MessageFormatUtil.format("The dictionaries refer to different pages. Expected page number: {0}. Found: {1}", Integer.valueOf(this.cmpPagesRef.indexOf(cmpRefKey) + 1), Integer.valueOf(this.outPagesRef.indexOf(outRefKey) + 1)));
                    return false;
                }
                return false;
            }
        }
        if (cmpDirectObj.isDictionary()) {
            return compareDictionariesExtended((PdfDictionary) outDirectObj, (PdfDictionary) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isStream()) {
            return compareStreamsExtended((PdfStream) outDirectObj, (PdfStream) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isArray()) {
            return compareArraysExtended((PdfArray) outDirectObj, (PdfArray) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isName()) {
            return compareNamesExtended((PdfName) outDirectObj, (PdfName) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isNumber()) {
            return compareNumbersExtended((PdfNumber) outDirectObj, (PdfNumber) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isString()) {
            return compareStringsExtended((PdfString) outDirectObj, (PdfString) cmpDirectObj, currentPath, compareResult);
        }
        if (cmpDirectObj.isBoolean()) {
            return compareBooleansExtended((PdfBoolean) outDirectObj, (PdfBoolean) cmpDirectObj, currentPath, compareResult);
        }
        if (outDirectObj.isNull() && cmpDirectObj.isNull()) {
            return true;
        }
        throw new UnsupportedOperationException();
    }

    private boolean compareStreamsExtended(PdfStream outStream, PdfStream cmpStream, ObjectPath currentPath, CompareResult compareResult) {
        boolean toDecode = PdfName.FlateDecode.equals(outStream.get(PdfName.Filter));
        byte[] outStreamBytes = outStream.getBytes(toDecode);
        byte[] cmpStreamBytes = cmpStream.getBytes(toDecode);
        if (Arrays.equals(outStreamBytes, cmpStreamBytes)) {
            return compareDictionariesExtended(outStream, cmpStream, currentPath, compareResult);
        }
        StringBuilder errorMessage = new StringBuilder();
        if (cmpStreamBytes.length != outStreamBytes.length) {
            errorMessage.append(MessageFormatUtil.format("PdfStream. Lengths are different. Expected: {0}. Found: {1}\n", Integer.valueOf(cmpStreamBytes.length), Integer.valueOf(outStreamBytes.length)));
        } else {
            errorMessage.append("PdfStream. Bytes are different.\n");
        }
        int firstDifferenceOffset = findBytesDifference(outStreamBytes, cmpStreamBytes, errorMessage);
        if (compareResult != null && currentPath != null) {
            currentPath.pushOffsetToPath(firstDifferenceOffset);
            compareResult.addError(currentPath, errorMessage.toString());
            currentPath.pop();
            return false;
        }
        return false;
    }

    private int findBytesDifference(byte[] outStreamBytes, byte[] cmpStreamBytes, StringBuilder errorMessage) {
        String bytesDifference;
        int numberOfDifferentBytes = 0;
        int firstDifferenceOffset = 0;
        int minLength = Math.min(cmpStreamBytes.length, outStreamBytes.length);
        for (int i = 0; i < minLength; i++) {
            if (cmpStreamBytes[i] != outStreamBytes[i]) {
                numberOfDifferentBytes++;
                if (numberOfDifferentBytes == 1) {
                    firstDifferenceOffset = i;
                }
            }
        }
        if (numberOfDifferentBytes > 0) {
            int lCmp = Math.max(0, firstDifferenceOffset - 10);
            int rCmp = Math.min(cmpStreamBytes.length, firstDifferenceOffset + 10);
            int lOut = Math.max(0, firstDifferenceOffset - 10);
            int rOut = Math.min(outStreamBytes.length, firstDifferenceOffset + 10);
            String cmpByte = new String(new byte[]{cmpStreamBytes[firstDifferenceOffset]}, StandardCharsets.ISO_8859_1);
            String cmpByteNeighbours = new String(cmpStreamBytes, lCmp, rCmp - lCmp, StandardCharsets.ISO_8859_1).replaceAll(NEW_LINES, SymbolConstants.SPACE_SYMBOL);
            String outByte = new String(new byte[]{outStreamBytes[firstDifferenceOffset]}, StandardCharsets.ISO_8859_1);
            String outBytesNeighbours = new String(outStreamBytes, lOut, rOut - lOut, StandardCharsets.ISO_8859_1).replaceAll(NEW_LINES, SymbolConstants.SPACE_SYMBOL);
            bytesDifference = MessageFormatUtil.format("First bytes difference is encountered at index {0}. Expected: {1} ({2}). Found: {3} ({4}). Total number of different bytes: {5}", Integer.valueOf(firstDifferenceOffset).toString(), cmpByte, cmpByteNeighbours, outByte, outBytesNeighbours, Integer.valueOf(numberOfDifferentBytes));
        } else {
            firstDifferenceOffset = minLength;
            bytesDifference = MessageFormatUtil.format("Bytes of the shorter array are the same as the first {0} bytes of the longer one.", Integer.valueOf(minLength));
        }
        errorMessage.append(bytesDifference);
        return firstDifferenceOffset;
    }

    private boolean compareArraysExtended(PdfArray outArray, PdfArray cmpArray, ObjectPath currentPath, CompareResult compareResult) {
        if (outArray == null) {
            if (compareResult != null && currentPath != null) {
                compareResult.addError(currentPath, "Found null. Expected PdfArray.");
                return false;
            }
            return false;
        }
        if (outArray.size() != cmpArray.size()) {
            if (compareResult != null && currentPath != null) {
                compareResult.addError(currentPath, MessageFormatUtil.format("PdfArrays. Lengths are different. Expected: {0}. Found: {1}.", Integer.valueOf(cmpArray.size()), Integer.valueOf(outArray.size())));
                return false;
            }
            return false;
        }
        boolean arraysAreEqual = true;
        for (int i = 0; i < cmpArray.size(); i++) {
            if (currentPath != null) {
                currentPath.pushArrayItemToPath(i);
            }
            arraysAreEqual = compareObjects(outArray.get(i, false), cmpArray.get(i, false), currentPath, compareResult) && arraysAreEqual;
            if (currentPath != null) {
                currentPath.pop();
            }
            if (!arraysAreEqual && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached())) {
                return false;
            }
        }
        return arraysAreEqual;
    }

    private boolean compareNamesExtended(PdfName outName, PdfName cmpName, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpName.equals(outName)) {
            return true;
        }
        if (compareResult != null && currentPath != null) {
            compareResult.addError(currentPath, MessageFormatUtil.format("PdfName. Expected: {0}. Found: {1}", cmpName.toString(), outName.toString()));
            return false;
        }
        return false;
    }

    private boolean compareNumbersExtended(PdfNumber outNumber, PdfNumber cmpNumber, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpNumber.getValue() == outNumber.getValue()) {
            return true;
        }
        if (compareResult != null && currentPath != null) {
            compareResult.addError(currentPath, MessageFormatUtil.format("PdfNumber. Expected: {0}. Found: {1}", cmpNumber, outNumber));
            return false;
        }
        return false;
    }

    private boolean compareStringsExtended(PdfString outString, PdfString cmpString, ObjectPath currentPath, CompareResult compareResult) {
        if (Arrays.equals(convertPdfStringToBytes(cmpString), convertPdfStringToBytes(outString))) {
            return true;
        }
        String cmpStr = cmpString.toUnicodeString();
        String outStr = outString.toUnicodeString();
        StringBuilder errorMessage = new StringBuilder();
        if (cmpStr.length() != outStr.length()) {
            errorMessage.append(MessageFormatUtil.format("PdfString. Lengths are different. Expected: {0}. Found: {1}\n", Integer.valueOf(cmpStr.length()), Integer.valueOf(outStr.length())));
        } else {
            errorMessage.append("PdfString. Characters are different.\n");
        }
        int firstDifferenceOffset = findStringDifference(outStr, cmpStr, errorMessage);
        if (compareResult != null && currentPath != null) {
            currentPath.pushOffsetToPath(firstDifferenceOffset);
            compareResult.addError(currentPath, errorMessage.toString());
            currentPath.pop();
            return false;
        }
        return false;
    }

    private int findStringDifference(String outString, String cmpString, StringBuilder errorMessage) {
        String stringDifference;
        int numberOfDifferentChars = 0;
        int firstDifferenceOffset = 0;
        int minLength = Math.min(cmpString.length(), outString.length());
        for (int i = 0; i < minLength; i++) {
            if (cmpString.charAt(i) != outString.charAt(i)) {
                numberOfDifferentChars++;
                if (numberOfDifferentChars == 1) {
                    firstDifferenceOffset = i;
                }
            }
        }
        if (numberOfDifferentChars > 0) {
            int lCmp = Math.max(0, firstDifferenceOffset - 15);
            int rCmp = Math.min(cmpString.length(), firstDifferenceOffset + 15);
            int lOut = Math.max(0, firstDifferenceOffset - 15);
            int rOut = Math.min(outString.length(), firstDifferenceOffset + 15);
            String cmpByte = String.valueOf(cmpString.charAt(firstDifferenceOffset));
            String cmpByteNeighbours = cmpString.substring(lCmp, rCmp).replaceAll(NEW_LINES, SymbolConstants.SPACE_SYMBOL);
            String outByte = String.valueOf(outString.charAt(firstDifferenceOffset));
            String outBytesNeighbours = outString.substring(lOut, rOut).replaceAll(NEW_LINES, SymbolConstants.SPACE_SYMBOL);
            stringDifference = MessageFormatUtil.format("First characters difference is encountered at index {0}.\nExpected: {1} ({2}).\nFound: {3} ({4}).\nTotal number of different characters: {5}", Integer.valueOf(firstDifferenceOffset).toString(), cmpByte, cmpByteNeighbours, outByte, outBytesNeighbours, Integer.valueOf(numberOfDifferentChars));
        } else {
            firstDifferenceOffset = minLength;
            stringDifference = MessageFormatUtil.format("All characters of the shorter string are the same as the first {0} characters of the longer one.", Integer.valueOf(minLength));
        }
        errorMessage.append(stringDifference);
        return firstDifferenceOffset;
    }

    private byte[] convertPdfStringToBytes(PdfString pdfString) {
        byte[] bytes;
        String value = pdfString.getValue();
        String encoding = pdfString.getEncoding();
        if (encoding != null && PdfEncodings.UNICODE_BIG.equals(encoding) && PdfEncodings.isPdfDocEncoding(value)) {
            bytes = PdfEncodings.convertToBytes(value, PdfEncodings.PDF_DOC_ENCODING);
        } else {
            bytes = PdfEncodings.convertToBytes(value, encoding);
        }
        return bytes;
    }

    private boolean compareBooleansExtended(PdfBoolean outBoolean, PdfBoolean cmpBoolean, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpBoolean.getValue() == outBoolean.getValue()) {
            return true;
        }
        if (compareResult != null && currentPath != null) {
            compareResult.addError(currentPath, MessageFormatUtil.format("PdfBoolean. Expected: {0}. Found: {1}.", Boolean.valueOf(cmpBoolean.getValue()), Boolean.valueOf(outBoolean.getValue())));
            return false;
        }
        return false;
    }

    private List<PdfLinkAnnotation> getLinkAnnotations(int pageNum, PdfDocument document) {
        List<PdfLinkAnnotation> linkAnnotations = new ArrayList<>();
        List<PdfAnnotation> annotations = document.getPage(pageNum).getAnnotations();
        for (PdfAnnotation annotation : annotations) {
            if (PdfName.Link.equals(annotation.getSubtype())) {
                linkAnnotations.add((PdfLinkAnnotation) annotation);
            }
        }
        return linkAnnotations;
    }

    private boolean compareLinkAnnotations(PdfLinkAnnotation cmpLink, PdfLinkAnnotation outLink, PdfDocument cmpDocument, PdfDocument outDocument) {
        PdfObject cmpDestObject = cmpLink.getDestinationObject();
        PdfObject outDestObject = outLink.getDestinationObject();
        if (cmpDestObject != null && outDestObject != null) {
            if (cmpDestObject.getType() != outDestObject.getType()) {
                return false;
            }
            PdfArray explicitCmpDest = null;
            PdfArray explicitOutDest = null;
            Map<String, PdfObject> cmpNamedDestinations = cmpDocument.getCatalog().getNameTree(PdfName.Dests).getNames();
            Map<String, PdfObject> outNamedDestinations = outDocument.getCatalog().getNameTree(PdfName.Dests).getNames();
            switch (cmpDestObject.getType()) {
                case 1:
                    explicitCmpDest = (PdfArray) cmpDestObject;
                    explicitOutDest = (PdfArray) outDestObject;
                    break;
                case 6:
                    explicitCmpDest = (PdfArray) cmpNamedDestinations.get(((PdfName) cmpDestObject).getValue());
                    explicitOutDest = (PdfArray) outNamedDestinations.get(((PdfName) outDestObject).getValue());
                    break;
                case 10:
                    explicitCmpDest = (PdfArray) cmpNamedDestinations.get(((PdfString) cmpDestObject).toUnicodeString());
                    explicitOutDest = (PdfArray) outNamedDestinations.get(((PdfString) outDestObject).toUnicodeString());
                    break;
            }
            if (getExplicitDestinationPageNum(explicitCmpDest) != getExplicitDestinationPageNum(explicitOutDest)) {
                return false;
            }
        }
        PdfDictionary cmpDict = cmpLink.getPdfObject();
        PdfDictionary outDict = outLink.getPdfObject();
        if (cmpDict.size() != outDict.size()) {
            return false;
        }
        Rectangle cmpRect = cmpDict.getAsRectangle(PdfName.Rect);
        Rectangle outRect = outDict.getAsRectangle(PdfName.Rect);
        if (cmpRect.getHeight() != outRect.getHeight() || cmpRect.getWidth() != outRect.getWidth() || cmpRect.getX() != outRect.getX() || cmpRect.getY() != outRect.getY()) {
            return false;
        }
        for (Map.Entry<PdfName, PdfObject> cmpEntry : cmpDict.entrySet()) {
            PdfObject cmpObj = cmpEntry.getValue();
            if (!outDict.containsKey(cmpEntry.getKey())) {
                return false;
            }
            PdfObject outObj = outDict.get(cmpEntry.getKey());
            if (cmpObj.getType() != outObj.getType()) {
                return false;
            }
            switch (cmpObj.getType()) {
                case 2:
                case 6:
                case 7:
                case 8:
                case 10:
                    if (!cmpObj.toString().equals(outObj.toString())) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    private int getExplicitDestinationPageNum(PdfArray explicitDest) {
        PdfIndirectReference pageReference = (PdfIndirectReference) explicitDest.get(0, false);
        PdfDocument doc = pageReference.getDocument();
        for (int i = 1; i <= doc.getNumberOfPages(); i++) {
            if (doc.getPage(i).getPdfObject().getIndirectReference().equals(pageReference)) {
                return i;
            }
        }
        throw new IllegalArgumentException("PdfLinkAnnotation comparison: Page not found.");
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$PngFileFilter.class */
    private class PngFileFilter implements FileFilter {
        private PngFileFilter() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File pathname) {
            String ap = pathname.getName();
            boolean b1 = ap.endsWith(".png");
            boolean b2 = ap.contains("cmp_");
            return b1 && !b2 && ap.contains(CompareTool.this.outPdfName);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$CmpPngFileFilter.class */
    private class CmpPngFileFilter implements FileFilter {
        private CmpPngFileFilter() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File pathname) {
            String ap = pathname.getName();
            boolean b1 = ap.endsWith(".png");
            boolean b2 = ap.contains("cmp_");
            return b1 && b2 && ap.contains(CompareTool.this.cmpPdfName);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$DiffPngFileFilter.class */
    private class DiffPngFileFilter implements FileFilter {
        private String differenceImagePrefix;

        public DiffPngFileFilter(String differenceImagePrefix) {
            this.differenceImagePrefix = differenceImagePrefix;
        }

        @Override // java.io.FileFilter
        public boolean accept(File pathname) {
            String ap = pathname.getName();
            boolean b1 = ap.endsWith(".png");
            boolean b2 = ap.startsWith(this.differenceImagePrefix);
            return b1 && b2;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ImageNameComparator.class */
    private class ImageNameComparator implements Comparator<File> {
        private ImageNameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(File f1, File f2) {
            String f1Name = f1.getName();
            String f2Name = f2.getName();
            return f1Name.compareTo(f2Name);
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$CompareResult.class */
    public class CompareResult {
        protected Map<ObjectPath, String> differences = new LinkedHashMap();
        protected int messageLimit;

        public CompareResult(int messageLimit) {
            this.messageLimit = 1;
            this.messageLimit = messageLimit;
        }

        public boolean isOk() {
            return this.differences.size() == 0;
        }

        public int getErrorCount() {
            return this.differences.size();
        }

        public String getReport() {
            StringBuilder sb = new StringBuilder();
            boolean firstEntry = true;
            for (Map.Entry<ObjectPath, String> entry : this.differences.entrySet()) {
                if (!firstEntry) {
                    sb.append("-----------------------------").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                }
                ObjectPath diffPath = entry.getKey();
                sb.append(entry.getValue()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR).append(diffPath.toString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                firstEntry = false;
            }
            return sb.toString();
        }

        public Map<ObjectPath, String> getDifferences() {
            return this.differences;
        }

        public void writeReportToXml(OutputStream stream) throws TransformerException, ParserConfigurationException, DOMException, TransformerFactoryConfigurationError, IllegalArgumentException {
            Document xmlReport = XmlUtils.initNewXmlDocument();
            Element root = xmlReport.createElement("report");
            Element errors = xmlReport.createElement(BindErrorsTag.ERRORS_VARIABLE_NAME);
            errors.setAttribute("count", String.valueOf(this.differences.size()));
            root.appendChild(errors);
            for (Map.Entry<ObjectPath, String> entry : this.differences.entrySet()) {
                Node errorNode = xmlReport.createElement(AsmRelationshipUtils.DECLARE_ERROR);
                Node message = xmlReport.createElement(ConstraintHelper.MESSAGE);
                message.appendChild(xmlReport.createTextNode(entry.getValue()));
                Node path = entry.getKey().toXmlNode(xmlReport);
                errorNode.appendChild(message);
                errorNode.appendChild(path);
                errors.appendChild(errorNode);
            }
            xmlReport.appendChild(root);
            XmlUtils.writeXmlDocToStream(xmlReport, stream);
        }

        protected boolean isMessageLimitReached() {
            return this.differences.size() >= this.messageLimit;
        }

        protected void addError(ObjectPath path, String message) {
            if (this.differences.size() < this.messageLimit) {
                this.differences.put((ObjectPath) path.clone(), message);
            }
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath.class */
    public class ObjectPath {
        protected PdfIndirectReference baseCmpObject;
        protected PdfIndirectReference baseOutObject;
        protected Stack<LocalPathItem> path;
        protected Stack<IndirectPathItem> indirects;

        public ObjectPath() {
            this.path = new Stack<>();
            this.indirects = new Stack<>();
        }

        protected ObjectPath(PdfIndirectReference baseCmpObject, PdfIndirectReference baseOutObject) {
            this.path = new Stack<>();
            this.indirects = new Stack<>();
            this.baseCmpObject = baseCmpObject;
            this.baseOutObject = baseOutObject;
            this.indirects.push(new IndirectPathItem(baseCmpObject, baseOutObject));
        }

        private ObjectPath(PdfIndirectReference baseCmpObject, PdfIndirectReference baseOutObject, Stack<LocalPathItem> path, Stack<IndirectPathItem> indirects) {
            this.path = new Stack<>();
            this.indirects = new Stack<>();
            this.baseCmpObject = baseCmpObject;
            this.baseOutObject = baseOutObject;
            this.path = path;
            this.indirects = indirects;
        }

        public ObjectPath resetDirectPath(PdfIndirectReference baseCmpObject, PdfIndirectReference baseOutObject) {
            ObjectPath newPath = CompareTool.this.new ObjectPath(baseCmpObject, baseOutObject, new Stack(), (Stack) this.indirects.clone());
            newPath.indirects.push(new IndirectPathItem(baseCmpObject, baseOutObject));
            return newPath;
        }

        public boolean isComparing(PdfIndirectReference cmpObject, PdfIndirectReference outObject) {
            return this.indirects.contains(new IndirectPathItem(cmpObject, outObject));
        }

        public void pushArrayItemToPath(int index) {
            this.path.push(new ArrayPathItem(index));
        }

        public void pushDictItemToPath(PdfName key) {
            this.path.push(new DictPathItem(key));
        }

        public void pushOffsetToPath(int offset) {
            this.path.push(new OffsetPathItem(offset));
        }

        public void pop() {
            this.path.pop();
        }

        public Stack<LocalPathItem> getLocalPath() {
            return this.path;
        }

        public Stack<IndirectPathItem> getIndirectPath() {
            return this.indirects;
        }

        public PdfIndirectReference getBaseCmpObject() {
            return this.baseCmpObject;
        }

        public PdfIndirectReference getBaseOutObject() {
            return this.baseOutObject;
        }

        public Node toXmlNode(Document document) throws DOMException {
            Element element = document.createElement(Cookie2.PATH);
            Element baseNode = document.createElement("base");
            baseNode.setAttribute("cmp", MessageFormatUtil.format("{0} {1} obj", Integer.valueOf(this.baseCmpObject.getObjNumber()), Integer.valueOf(this.baseCmpObject.getGenNumber())));
            baseNode.setAttribute("out", MessageFormatUtil.format("{0} {1} obj", Integer.valueOf(this.baseOutObject.getObjNumber()), Integer.valueOf(this.baseOutObject.getGenNumber())));
            element.appendChild(baseNode);
            Stack<LocalPathItem> pathClone = (Stack) this.path.clone();
            List<LocalPathItem> localPathItems = new ArrayList<>(this.path.size());
            for (int i = 0; i < this.path.size(); i++) {
                localPathItems.add(pathClone.pop());
            }
            for (int i2 = localPathItems.size() - 1; i2 >= 0; i2--) {
                element.appendChild(localPathItems.get(i2).toXmlNode(document));
            }
            return element;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(MessageFormatUtil.format("Base cmp object: {0} obj. Base out object: {1} obj", this.baseCmpObject, this.baseOutObject));
            Stack<LocalPathItem> pathClone = (Stack) this.path.clone();
            List<LocalPathItem> localPathItems = new ArrayList<>(this.path.size());
            for (int i = 0; i < this.path.size(); i++) {
                localPathItems.add(pathClone.pop());
            }
            for (int i2 = localPathItems.size() - 1; i2 >= 0; i2--) {
                sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                sb.append(localPathItems.get(i2).toString());
            }
            return sb.toString();
        }

        public int hashCode() {
            int hashCode = ((this.baseCmpObject != null ? this.baseCmpObject.hashCode() : 0) * 31) + (this.baseOutObject != null ? this.baseOutObject.hashCode() : 0);
            Iterator<LocalPathItem> it = this.path.iterator();
            while (it.hasNext()) {
                LocalPathItem pathItem = it.next();
                hashCode = (hashCode * 31) + pathItem.hashCode();
            }
            return hashCode;
        }

        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.baseCmpObject.equals(((ObjectPath) obj).baseCmpObject) && this.baseOutObject.equals(((ObjectPath) obj).baseOutObject) && this.path.equals(((ObjectPath) obj).path);
        }

        protected Object clone() {
            return CompareTool.this.new ObjectPath(this.baseCmpObject, this.baseOutObject, (Stack) this.path.clone(), (Stack) this.indirects.clone());
        }

        /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath$IndirectPathItem.class */
        public class IndirectPathItem {
            private PdfIndirectReference cmpObject;
            private PdfIndirectReference outObject;

            public IndirectPathItem(PdfIndirectReference cmpObject, PdfIndirectReference outObject) {
                this.cmpObject = cmpObject;
                this.outObject = outObject;
            }

            public PdfIndirectReference getCmpObject() {
                return this.cmpObject;
            }

            public PdfIndirectReference getOutObject() {
                return this.outObject;
            }

            public int hashCode() {
                return (this.cmpObject.hashCode() * 31) + this.outObject.hashCode();
            }

            public boolean equals(Object obj) {
                return obj.getClass() == getClass() && this.cmpObject.equals(((IndirectPathItem) obj).cmpObject) && this.outObject.equals(((IndirectPathItem) obj).outObject);
            }
        }

        /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath$LocalPathItem.class */
        public abstract class LocalPathItem {
            protected abstract Node toXmlNode(Document document);

            public LocalPathItem() {
            }
        }

        /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath$DictPathItem.class */
        public class DictPathItem extends LocalPathItem {
            PdfName key;

            public DictPathItem(PdfName key) {
                super();
                this.key = key;
            }

            public String toString() {
                return "Dict key: " + this.key;
            }

            public int hashCode() {
                return this.key.hashCode();
            }

            public boolean equals(Object obj) {
                return obj.getClass() == getClass() && this.key.equals(((DictPathItem) obj).key);
            }

            public PdfName getKey() {
                return this.key;
            }

            @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath.LocalPathItem
            protected Node toXmlNode(Document document) throws DOMException {
                Element element = document.createElement("dictKey");
                element.appendChild(document.createTextNode(this.key.toString()));
                return element;
            }
        }

        /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath$ArrayPathItem.class */
        public class ArrayPathItem extends LocalPathItem {
            int index;

            public ArrayPathItem(int index) {
                super();
                this.index = index;
            }

            public String toString() {
                return "Array index: " + String.valueOf(this.index);
            }

            public int hashCode() {
                return this.index;
            }

            public boolean equals(Object obj) {
                return obj.getClass() == getClass() && this.index == ((ArrayPathItem) obj).index;
            }

            public int getIndex() {
                return this.index;
            }

            @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath.LocalPathItem
            protected Node toXmlNode(Document document) throws DOMException {
                Element element = document.createElement("arrayIndex");
                element.appendChild(document.createTextNode(String.valueOf(this.index)));
                return element;
            }
        }

        /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$ObjectPath$OffsetPathItem.class */
        public class OffsetPathItem extends LocalPathItem {
            int offset;

            public OffsetPathItem(int offset) {
                super();
                this.offset = offset;
            }

            public int getOffset() {
                return this.offset;
            }

            public String toString() {
                return "Offset: " + String.valueOf(this.offset);
            }

            public int hashCode() {
                return this.offset;
            }

            public boolean equals(Object obj) {
                return obj.getClass() == getClass() && this.offset == ((OffsetPathItem) obj).offset;
            }

            @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath.LocalPathItem
            protected Node toXmlNode(Document document) throws DOMException {
                Element element = document.createElement("offset");
                element.appendChild(document.createTextNode(String.valueOf(this.offset)));
                return element;
            }
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$TrailerPath.class */
    private class TrailerPath extends ObjectPath {
        private PdfDocument outDocument;
        private PdfDocument cmpDocument;

        public TrailerPath(PdfDocument cmpDoc, PdfDocument outDoc) {
            super();
            this.outDocument = outDoc;
            this.cmpDocument = cmpDoc;
        }

        public TrailerPath(PdfDocument cmpDoc, PdfDocument outDoc, Stack<ObjectPath.LocalPathItem> path) {
            super();
            this.outDocument = outDoc;
            this.cmpDocument = cmpDoc;
            this.path = path;
        }

        @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath
        public Node toXmlNode(Document document) throws DOMException {
            Element element = document.createElement(Cookie2.PATH);
            Element baseNode = document.createElement("base");
            baseNode.setAttribute("cmp", "trailer");
            baseNode.setAttribute("out", "trailer");
            element.appendChild(baseNode);
            Iterator<ObjectPath.LocalPathItem> it = this.path.iterator();
            while (it.hasNext()) {
                ObjectPath.LocalPathItem pathItem = it.next();
                element.appendChild(pathItem.toXmlNode(document));
            }
            return element;
        }

        @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Base cmp object: trailer. Base out object: trailer");
            Iterator<ObjectPath.LocalPathItem> it = this.path.iterator();
            while (it.hasNext()) {
                ObjectPath.LocalPathItem pathItem = it.next();
                sb.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
                sb.append(pathItem.toString());
            }
            return sb.toString();
        }

        @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath
        public int hashCode() {
            int hashCode = (this.outDocument.hashCode() * 31) + this.cmpDocument.hashCode();
            Iterator<ObjectPath.LocalPathItem> it = this.path.iterator();
            while (it.hasNext()) {
                ObjectPath.LocalPathItem pathItem = it.next();
                hashCode = (hashCode * 31) + pathItem.hashCode();
            }
            return hashCode;
        }

        @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.outDocument.equals(((TrailerPath) obj).outDocument) && this.cmpDocument.equals(((TrailerPath) obj).cmpDocument) && this.path.equals(((ObjectPath) obj).path);
        }

        @Override // com.itextpdf.kernel.utils.CompareTool.ObjectPath
        protected Object clone() {
            return CompareTool.this.new TrailerPath(this.cmpDocument, this.outDocument, (Stack) this.path.clone());
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/CompareTool$CompareToolExecutionException.class */
    public class CompareToolExecutionException extends RuntimeException {
        public CompareToolExecutionException(String msg) {
            super(msg);
        }
    }
}
