package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.ProductInfo;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.VersionInfo;
import com.itextpdf.kernel.counter.EventCounterHandler;
import com.itextpdf.kernel.counter.event.CoreEvent;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.EventDispatcher;
import com.itextpdf.kernel.events.IEventDispatcher;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.log.CounterManager;
import com.itextpdf.kernel.log.ICounter;
import com.itextpdf.kernel.numbering.EnglishAlphabetNumbering;
import com.itextpdf.kernel.numbering.RomanNumbering;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.canvas.CanvasGraphicsState;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfEncryptedPayloadFileSpecFactory;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagutils.TagStructureContext;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.kernel.xmp.options.SerializeOptions;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDocument.class */
public class PdfDocument implements IEventDispatcher, Closeable, Serializable {
    private static final long serialVersionUID = -7041578979319799646L;
    private static IPdfPageFactory pdfPageFactory;

    @Deprecated
    protected PdfPage currentPage;
    protected PageSize defaultPageSize;
    protected transient EventDispatcher eventDispatcher;
    protected PdfWriter writer;
    protected PdfReader reader;
    protected byte[] xmpMetadata;
    protected PdfCatalog catalog;
    protected PdfDictionary trailer;
    protected PdfDocumentInfo info;
    protected PdfVersion pdfVersion;
    private PdfString originalDocumentId;
    private PdfString modifiedDocumentId;
    final PdfXrefTable xref;
    protected FingerPrint fingerPrint;
    protected final StampingProperties properties;
    protected PdfStructTreeRoot structTreeRoot;
    protected int structParentIndex;
    protected boolean closeReader;
    protected boolean closeWriter;
    protected boolean isClosing;
    protected boolean closed;
    protected boolean flushUnusedObjects;
    private Map<PdfIndirectReference, PdfFont> documentFonts;
    private PdfFont defaultFont;
    protected transient TagStructureContext tagStructureContext;
    private static final AtomicLong lastDocumentId;
    private long documentId;
    private VersionInfo versionInfo;
    private LinkedHashMap<PdfPage, List<PdfLinkAnnotation>> linkAnnotations;
    Map<PdfIndirectReference, byte[]> serializedObjectsCache;
    MemoryLimitsAwareHandler memoryLimitsAwareHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfDocument.class.desiredAssertionStatus();
        pdfPageFactory = new PdfPageFactory();
        lastDocumentId = new AtomicLong();
    }

    public PdfDocument(PdfReader reader) {
        this(reader, new DocumentProperties());
    }

    public PdfDocument(PdfReader reader, DocumentProperties properties) {
        this.currentPage = null;
        this.defaultPageSize = PageSize.Default;
        this.eventDispatcher = new EventDispatcher();
        this.writer = null;
        this.reader = null;
        this.xmpMetadata = null;
        this.catalog = null;
        this.trailer = null;
        this.info = null;
        this.pdfVersion = PdfVersion.PDF_1_7;
        this.xref = new PdfXrefTable();
        this.structParentIndex = -1;
        this.closeReader = true;
        this.closeWriter = true;
        this.isClosing = false;
        this.closed = false;
        this.flushUnusedObjects = false;
        this.documentFonts = new HashMap();
        this.defaultFont = null;
        this.versionInfo = Version.getInstance().getInfo();
        this.linkAnnotations = new LinkedHashMap<>();
        this.serializedObjectsCache = new HashMap();
        this.memoryLimitsAwareHandler = null;
        if (reader == null) {
            throw new IllegalArgumentException("The reader in PdfDocument constructor can not be null.");
        }
        this.documentId = lastDocumentId.incrementAndGet();
        this.reader = reader;
        this.properties = new StampingProperties();
        this.properties.setEventCountingMetaInfo(properties.metaInfo);
        open(null);
    }

    public PdfDocument(PdfWriter writer) {
        this(writer, new DocumentProperties());
    }

    public PdfDocument(PdfWriter writer, DocumentProperties properties) {
        this.currentPage = null;
        this.defaultPageSize = PageSize.Default;
        this.eventDispatcher = new EventDispatcher();
        this.writer = null;
        this.reader = null;
        this.xmpMetadata = null;
        this.catalog = null;
        this.trailer = null;
        this.info = null;
        this.pdfVersion = PdfVersion.PDF_1_7;
        this.xref = new PdfXrefTable();
        this.structParentIndex = -1;
        this.closeReader = true;
        this.closeWriter = true;
        this.isClosing = false;
        this.closed = false;
        this.flushUnusedObjects = false;
        this.documentFonts = new HashMap();
        this.defaultFont = null;
        this.versionInfo = Version.getInstance().getInfo();
        this.linkAnnotations = new LinkedHashMap<>();
        this.serializedObjectsCache = new HashMap();
        this.memoryLimitsAwareHandler = null;
        if (writer == null) {
            throw new IllegalArgumentException("The writer in PdfDocument constructor can not be null.");
        }
        this.documentId = lastDocumentId.incrementAndGet();
        this.writer = writer;
        this.properties = new StampingProperties();
        this.properties.setEventCountingMetaInfo(properties.metaInfo);
        open(writer.properties.pdfVersion);
    }

    public PdfDocument(PdfReader reader, PdfWriter writer) {
        this(reader, writer, new StampingProperties());
    }

    public PdfDocument(PdfReader reader, PdfWriter writer, StampingProperties properties) {
        this.currentPage = null;
        this.defaultPageSize = PageSize.Default;
        this.eventDispatcher = new EventDispatcher();
        this.writer = null;
        this.reader = null;
        this.xmpMetadata = null;
        this.catalog = null;
        this.trailer = null;
        this.info = null;
        this.pdfVersion = PdfVersion.PDF_1_7;
        this.xref = new PdfXrefTable();
        this.structParentIndex = -1;
        this.closeReader = true;
        this.closeWriter = true;
        this.isClosing = false;
        this.closed = false;
        this.flushUnusedObjects = false;
        this.documentFonts = new HashMap();
        this.defaultFont = null;
        this.versionInfo = Version.getInstance().getInfo();
        this.linkAnnotations = new LinkedHashMap<>();
        this.serializedObjectsCache = new HashMap();
        this.memoryLimitsAwareHandler = null;
        if (reader == null) {
            throw new IllegalArgumentException("The reader in PdfDocument constructor can not be null.");
        }
        if (writer == null) {
            throw new IllegalArgumentException("The writer in PdfDocument constructor can not be null.");
        }
        this.documentId = lastDocumentId.incrementAndGet();
        this.reader = reader;
        this.writer = writer;
        this.properties = properties;
        boolean writerHasEncryption = writerHasEncryption();
        if (properties.appendMode && writerHasEncryption) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
            logger.warn(LogMessageConstant.WRITER_ENCRYPTION_IS_IGNORED_APPEND);
        }
        if (properties.preserveEncryption && writerHasEncryption) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
            logger2.warn(LogMessageConstant.WRITER_ENCRYPTION_IS_IGNORED_PRESERVE);
        }
        open(writer.properties.pdfVersion);
    }

    protected void setXmpMetadata(byte[] xmpMetadata) {
        this.xmpMetadata = xmpMetadata;
    }

    public void setXmpMetadata(XMPMeta xmpMeta, SerializeOptions serializeOptions) throws XMPException {
        setXmpMetadata(XMPMetaFactory.serializeToBuffer(xmpMeta, serializeOptions));
    }

    public void setXmpMetadata(XMPMeta xmpMeta) throws XMPException {
        SerializeOptions serializeOptions = new SerializeOptions();
        serializeOptions.setPadding(2000);
        setXmpMetadata(xmpMeta, serializeOptions);
    }

    public byte[] getXmpMetadata() {
        return getXmpMetadata(false);
    }

    public byte[] getXmpMetadata(boolean createNew) {
        if (this.xmpMetadata == null && createNew) {
            XMPMeta xmpMeta = XMPMetaFactory.create();
            xmpMeta.setObjectName("xmpmeta");
            xmpMeta.setObjectName("");
            addCustomMetadataExtensions(xmpMeta);
            try {
                xmpMeta.setProperty("http://purl.org/dc/elements/1.1/", "format", MediaType.APPLICATION_PDF_VALUE);
                xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Producer, this.versionInfo.getVersion());
                setXmpMetadata(xmpMeta);
            } catch (XMPException e) {
            }
        }
        return this.xmpMetadata;
    }

    public PdfObject getPdfObject(int objNum) {
        checkClosingStatus();
        PdfIndirectReference reference = this.xref.get(objNum);
        if (reference == null) {
            return null;
        }
        return reference.getRefersTo();
    }

    public int getNumberOfPdfObjects() {
        return this.xref.size();
    }

    public PdfPage getPage(int pageNum) {
        checkClosingStatus();
        return this.catalog.getPageTree().getPage(pageNum);
    }

    public PdfPage getPage(PdfDictionary pageDictionary) {
        checkClosingStatus();
        return this.catalog.getPageTree().getPage(pageDictionary);
    }

    public PdfPage getFirstPage() {
        checkClosingStatus();
        return getPage(1);
    }

    public PdfPage getLastPage() {
        return getPage(getNumberOfPages());
    }

    public PdfPage addNewPage() {
        return addNewPage(getDefaultPageSize());
    }

    public PdfPage addNewPage(PageSize pageSize) {
        checkClosingStatus();
        PdfPage page = getPageFactory().createPdfPage(this, pageSize);
        checkAndAddPage(page);
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.START_PAGE, page));
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.INSERT_PAGE, page));
        return page;
    }

    public PdfPage addNewPage(int index) {
        return addNewPage(index, getDefaultPageSize());
    }

    public PdfPage addNewPage(int index, PageSize pageSize) {
        checkClosingStatus();
        PdfPage page = getPageFactory().createPdfPage(this, pageSize);
        checkAndAddPage(index, page);
        this.currentPage = page;
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.START_PAGE, page));
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.INSERT_PAGE, page));
        return page;
    }

    public PdfPage addPage(PdfPage page) {
        checkClosingStatus();
        checkAndAddPage(page);
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.INSERT_PAGE, page));
        return page;
    }

    public PdfPage addPage(int index, PdfPage page) {
        checkClosingStatus();
        checkAndAddPage(index, page);
        this.currentPage = page;
        dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.INSERT_PAGE, page));
        return page;
    }

    public int getNumberOfPages() {
        checkClosingStatus();
        return this.catalog.getPageTree().getNumberOfPages();
    }

    public int getPageNumber(PdfPage page) {
        checkClosingStatus();
        return this.catalog.getPageTree().getPageNumber(page);
    }

    public int getPageNumber(PdfDictionary pageDictionary) {
        return this.catalog.getPageTree().getPageNumber(pageDictionary);
    }

    public boolean movePage(PdfPage page, int insertBefore) {
        checkClosingStatus();
        int pageNum = getPageNumber(page);
        if (pageNum > 0) {
            movePage(pageNum, insertBefore);
            return true;
        }
        return false;
    }

    public void movePage(int pageNumber, int insertBefore) {
        checkClosingStatus();
        if (insertBefore < 1 || insertBefore > getNumberOfPages() + 1) {
            throw new IndexOutOfBoundsException(MessageFormatUtil.format(PdfException.RequestedPageNumberIsOutOfBounds, Integer.valueOf(insertBefore)));
        }
        PdfPage page = getPage(pageNumber);
        if (isTagged()) {
            getStructTreeRoot().move(page, insertBefore);
            getTagStructureContext().normalizeDocumentRootTag();
        }
        PdfPage removedPage = this.catalog.getPageTree().removePage(pageNumber);
        if (insertBefore > pageNumber) {
            insertBefore--;
        }
        this.catalog.getPageTree().addPage(insertBefore, removedPage);
    }

    public boolean removePage(PdfPage page) {
        checkClosingStatus();
        int pageNum = getPageNumber(page);
        if (pageNum >= 1) {
            removePage(pageNum);
            return true;
        }
        return false;
    }

    public void removePage(int pageNum) {
        checkClosingStatus();
        PdfPage removedPage = this.catalog.getPageTree().removePage(pageNum);
        if (removedPage != null) {
            this.catalog.removeOutlines(removedPage);
            removeUnusedWidgetsFromFields(removedPage);
            if (isTagged()) {
                getTagStructureContext().removePageTags(removedPage);
            }
            if (!removedPage.getPdfObject().isFlushed()) {
                removedPage.getPdfObject().remove(PdfName.Parent);
                removedPage.getPdfObject().getIndirectReference().setFree();
            }
            dispatchEvent(new PdfDocumentEvent(PdfDocumentEvent.REMOVE_PAGE, removedPage));
        }
    }

    public PdfDocumentInfo getDocumentInfo() {
        checkClosingStatus();
        return this.info;
    }

    public PdfString getOriginalDocumentId() {
        return this.originalDocumentId;
    }

    public PdfString getModifiedDocumentId() {
        return this.modifiedDocumentId;
    }

    public PageSize getDefaultPageSize() {
        return this.defaultPageSize;
    }

    public void setDefaultPageSize(PageSize pageSize) {
        this.defaultPageSize = pageSize;
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public void addEventHandler(String type, IEventHandler handler) {
        this.eventDispatcher.addEventHandler(type, handler);
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public void dispatchEvent(Event event) {
        this.eventDispatcher.dispatchEvent(event);
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public void dispatchEvent(Event event, boolean delayed) {
        this.eventDispatcher.dispatchEvent(event, delayed);
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public boolean hasEventHandler(String type) {
        return this.eventDispatcher.hasEventHandler(type);
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public void removeEventHandler(String type, IEventHandler handler) {
        this.eventDispatcher.removeEventHandler(type, handler);
    }

    @Override // com.itextpdf.kernel.events.IEventDispatcher
    public void removeAllHandlers() {
        this.eventDispatcher.removeAllHandlers();
    }

    public PdfWriter getWriter() {
        checkClosingStatus();
        return this.writer;
    }

    public PdfReader getReader() {
        checkClosingStatus();
        return this.reader;
    }

    public boolean isAppendMode() {
        checkClosingStatus();
        return this.properties.appendMode;
    }

    public PdfIndirectReference createNextIndirectReference() {
        checkClosingStatus();
        return this.xref.createNextIndirectReference(this);
    }

    public PdfVersion getPdfVersion() {
        return this.pdfVersion;
    }

    public PdfCatalog getCatalog() {
        checkClosingStatus();
        return this.catalog;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        PdfObject object;
        if (this.closed) {
            return;
        }
        this.isClosing = true;
        try {
            try {
                if (this.writer != null) {
                    if (this.catalog.isFlushed()) {
                        throw new PdfException(PdfException.CannotCloseDocumentWithAlreadyFlushedPdfCatalog);
                    }
                    updateProducerInInfoDictionary();
                    updateXmpMetadata();
                    if (this.pdfVersion.compareTo(PdfVersion.PDF_2_0) >= 0) {
                        for (PdfName deprecatedKey : PdfDocumentInfo.PDF20_DEPRECATED_KEYS) {
                            this.info.getPdfObject().remove(deprecatedKey);
                        }
                    }
                    if (getXmpMetadata() != null) {
                        PdfStream xmp = this.catalog.getPdfObject().getAsStream(PdfName.Metadata);
                        if (isAppendMode() && xmp != null && !xmp.isFlushed() && xmp.getIndirectReference() != null) {
                            xmp.setData(this.xmpMetadata);
                            xmp.setModified();
                        } else {
                            xmp = (PdfStream) new PdfStream().makeIndirect(this);
                            xmp.getOutputStream().write(this.xmpMetadata);
                            this.catalog.getPdfObject().put(PdfName.Metadata, xmp);
                            this.catalog.setModified();
                        }
                        xmp.put(PdfName.Type, PdfName.Metadata);
                        xmp.put(PdfName.Subtype, PdfName.XML);
                        if (this.writer.crypto != null && !this.writer.crypto.isMetadataEncrypted()) {
                            PdfArray ar = new PdfArray();
                            ar.add(PdfName.Crypt);
                            xmp.put(PdfName.Filter, ar);
                        }
                    }
                    checkIsoConformance();
                    PdfObject crypto = null;
                    Set<PdfIndirectReference> forbiddenToFlush = new HashSet<>();
                    if (this.properties.appendMode) {
                        if (this.structTreeRoot != null) {
                            tryFlushTagStructure(true);
                        }
                        if (this.catalog.isOCPropertiesMayHaveChanged() && this.catalog.getOCProperties(false).getPdfObject().isModified()) {
                            this.catalog.getOCProperties(false).flush();
                        }
                        if (this.catalog.pageLabels != null) {
                            this.catalog.put(PdfName.PageLabels, this.catalog.pageLabels.buildTree());
                        }
                        for (Map.Entry<PdfName, PdfNameTree> entry : this.catalog.nameTrees.entrySet()) {
                            PdfNameTree tree = entry.getValue();
                            if (tree.isModified()) {
                                ensureTreeRootAddedToNames(tree.buildTree().makeIndirect(this), entry.getKey());
                            }
                        }
                        PdfObject pageRoot = this.catalog.getPageTree().generateTree();
                        if (this.catalog.getPdfObject().isModified() || pageRoot.isModified()) {
                            this.catalog.put(PdfName.Pages, pageRoot);
                            this.catalog.getPdfObject().flush(false);
                        }
                        if (this.info.getPdfObject().isModified()) {
                            this.info.getPdfObject().flush(false);
                        }
                        flushFonts();
                        if (this.writer.crypto != null) {
                            if (!$assertionsDisabled && this.reader.decrypt.getPdfObject() != this.writer.crypto.getPdfObject()) {
                                throw new AssertionError("Conflict with source encryption");
                            }
                            crypto = this.reader.decrypt.getPdfObject();
                            if (crypto.getIndirectReference() != null) {
                                forbiddenToFlush.add(crypto.getIndirectReference());
                            }
                        }
                        this.writer.flushModifiedWaitingObjects(forbiddenToFlush);
                        for (int i = 0; i < this.xref.size(); i++) {
                            PdfIndirectReference indirectReference = this.xref.get(i);
                            if (indirectReference != null && !indirectReference.isFree() && indirectReference.checkState((short) 8) && !indirectReference.checkState((short) 1) && !forbiddenToFlush.contains(indirectReference)) {
                                indirectReference.setFree();
                            }
                        }
                    } else {
                        if (this.catalog.isOCPropertiesMayHaveChanged()) {
                            this.catalog.getPdfObject().put(PdfName.OCProperties, this.catalog.getOCProperties(false).getPdfObject());
                            this.catalog.getOCProperties(false).flush();
                        }
                        if (this.catalog.pageLabels != null) {
                            this.catalog.put(PdfName.PageLabels, this.catalog.pageLabels.buildTree());
                        }
                        this.catalog.getPdfObject().put(PdfName.Pages, this.catalog.getPageTree().generateTree());
                        for (Map.Entry<PdfName, PdfNameTree> entry2 : this.catalog.nameTrees.entrySet()) {
                            PdfNameTree tree2 = entry2.getValue();
                            if (tree2.isModified()) {
                                ensureTreeRootAddedToNames(tree2.buildTree().makeIndirect(this), entry2.getKey());
                            }
                        }
                        for (int pageNum = 1; pageNum <= getNumberOfPages(); pageNum++) {
                            getPage(pageNum).flush();
                        }
                        if (this.structTreeRoot != null) {
                            tryFlushTagStructure(false);
                        }
                        this.catalog.getPdfObject().flush(false);
                        this.info.getPdfObject().flush(false);
                        flushFonts();
                        if (this.writer.crypto != null) {
                            crypto = this.writer.crypto.getPdfObject();
                            crypto.makeIndirect(this);
                            forbiddenToFlush.add(crypto.getIndirectReference());
                        }
                        this.writer.flushWaitingObjects(forbiddenToFlush);
                        for (int i2 = 0; i2 < this.xref.size(); i2++) {
                            PdfIndirectReference indirectReference2 = this.xref.get(i2);
                            if (indirectReference2 != null && !indirectReference2.isFree() && !indirectReference2.checkState((short) 1) && !forbiddenToFlush.contains(indirectReference2)) {
                                if (isFlushUnusedObjects() && !indirectReference2.checkState((short) 16) && (object = indirectReference2.getRefersTo(false)) != null) {
                                    object.flush();
                                } else {
                                    indirectReference2.setFree();
                                }
                            }
                        }
                    }
                    this.writer.crypto = null;
                    if (!this.properties.appendMode && crypto != null) {
                        crypto.flush(false);
                    }
                    this.trailer.put(PdfName.Root, this.catalog.getPdfObject());
                    this.trailer.put(PdfName.Info, this.info.getPdfObject());
                    PdfObject fileId = PdfEncryption.createInfoId(ByteUtils.getIsoBytes(this.originalDocumentId.getValue()), ByteUtils.getIsoBytes(this.modifiedDocumentId.getValue()));
                    this.xref.writeXrefTableAndTrailer(this, fileId, crypto);
                    this.writer.flush();
                    for (ICounter counter : getCounters()) {
                        counter.onDocumentWritten(this.writer.getCurrentPos());
                    }
                }
                this.catalog.getPageTree().clearPageRefs();
                removeAllHandlers();
                if (this.writer != null && isCloseWriter()) {
                    try {
                        this.writer.close();
                    } catch (Exception e) {
                        Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                        logger.error(LogMessageConstant.PDF_WRITER_CLOSING_FAILED, (Throwable) e);
                    }
                }
                if (this.reader != null && isCloseReader()) {
                    try {
                        this.reader.close();
                    } catch (Exception e2) {
                        Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                        logger2.error(LogMessageConstant.PDF_READER_CLOSING_FAILED, (Throwable) e2);
                    }
                }
                this.closed = true;
            } catch (IOException e3) {
                throw new PdfException(PdfException.CannotCloseDocument, e3, this);
            }
        } catch (Throwable th) {
            if (this.writer != null && isCloseWriter()) {
                try {
                    this.writer.close();
                } catch (Exception e4) {
                    Logger logger3 = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                    logger3.error(LogMessageConstant.PDF_WRITER_CLOSING_FAILED, (Throwable) e4);
                }
            }
            if (this.reader != null && isCloseReader()) {
                try {
                    this.reader.close();
                } catch (Exception e5) {
                    Logger logger4 = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                    logger4.error(LogMessageConstant.PDF_READER_CLOSING_FAILED, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isTagged() {
        return this.structTreeRoot != null;
    }

    public PdfDocument setTagged() {
        checkClosingStatus();
        if (this.structTreeRoot == null) {
            this.structTreeRoot = new PdfStructTreeRoot(this);
            this.catalog.getPdfObject().put(PdfName.StructTreeRoot, this.structTreeRoot.getPdfObject());
            updateValueInMarkInfoDict(PdfName.Marked, PdfBoolean.TRUE);
            this.structParentIndex = 0;
        }
        return this;
    }

    public PdfStructTreeRoot getStructTreeRoot() {
        return this.structTreeRoot;
    }

    public int getNextStructParentIndex() {
        if (this.structParentIndex < 0) {
            return -1;
        }
        int i = this.structParentIndex;
        this.structParentIndex = i + 1;
        return i;
    }

    public TagStructureContext getTagStructureContext() {
        checkClosingStatus();
        if (this.tagStructureContext == null) {
            if (!isTagged()) {
                throw new PdfException(PdfException.MustBeATaggedDocument);
            }
            initTagStructureContext();
        }
        return this.tagStructureContext;
    }

    public List<PdfPage> copyPagesTo(int pageFrom, int pageTo, PdfDocument toDocument, int insertBeforePage) {
        return copyPagesTo(pageFrom, pageTo, toDocument, insertBeforePage, null);
    }

    public List<PdfPage> copyPagesTo(int pageFrom, int pageTo, PdfDocument toDocument, int insertBeforePage, IPdfPageExtraCopier copier) {
        List<Integer> pages = new ArrayList<>();
        for (int i = pageFrom; i <= pageTo; i++) {
            pages.add(Integer.valueOf(i));
        }
        return copyPagesTo(pages, toDocument, insertBeforePage, copier);
    }

    public List<PdfPage> copyPagesTo(int pageFrom, int pageTo, PdfDocument toDocument) {
        return copyPagesTo(pageFrom, pageTo, toDocument, (IPdfPageExtraCopier) null);
    }

    public List<PdfPage> copyPagesTo(int pageFrom, int pageTo, PdfDocument toDocument, IPdfPageExtraCopier copier) {
        return copyPagesTo(pageFrom, pageTo, toDocument, toDocument.getNumberOfPages() + 1, copier);
    }

    public List<PdfPage> copyPagesTo(List<Integer> pagesToCopy, PdfDocument toDocument, int insertBeforePage) {
        return copyPagesTo(pagesToCopy, toDocument, insertBeforePage, (IPdfPageExtraCopier) null);
    }

    public List<PdfPage> copyPagesTo(List<Integer> pagesToCopy, PdfDocument toDocument, int insertBeforePage, IPdfPageExtraCopier copier) {
        List<PdfOutline> pageOutlines;
        if (pagesToCopy.isEmpty()) {
            return Collections.emptyList();
        }
        checkClosingStatus();
        List<PdfPage> copiedPages = new ArrayList<>();
        Map<PdfPage, PdfPage> page2page = new LinkedHashMap<>();
        Set<PdfOutline> outlinesToCopy = new HashSet<>();
        List<Map<PdfPage, PdfPage>> rangesOfPagesWithIncreasingNumbers = new ArrayList<>();
        int lastCopiedPageNum = pagesToCopy.get(0).intValue();
        int pageInsertIndex = insertBeforePage;
        boolean insertInBetween = insertBeforePage < toDocument.getNumberOfPages() + 1;
        for (Integer pageNum : pagesToCopy) {
            PdfPage page = getPage(pageNum.intValue());
            PdfPage newPage = page.copyTo(toDocument, copier);
            copiedPages.add(newPage);
            page2page.put(page, newPage);
            if (lastCopiedPageNum >= pageNum.intValue()) {
                rangesOfPagesWithIncreasingNumbers.add(new HashMap<>());
            }
            int lastRangeInd = rangesOfPagesWithIncreasingNumbers.size() - 1;
            rangesOfPagesWithIncreasingNumbers.get(lastRangeInd).put(page, newPage);
            if (insertInBetween) {
                toDocument.addPage(pageInsertIndex, newPage);
            } else {
                toDocument.addPage(newPage);
            }
            pageInsertIndex++;
            if (toDocument.hasOutlines() && (pageOutlines = page.getOutlines(false)) != null) {
                outlinesToCopy.addAll(pageOutlines);
            }
            lastCopiedPageNum = pageNum.intValue();
        }
        copyLinkAnnotations(toDocument, page2page);
        if (toDocument.isTagged()) {
            if (isTagged()) {
                try {
                    for (Map<PdfPage, PdfPage> increasingPagesRange : rangesOfPagesWithIncreasingNumbers) {
                        if (insertInBetween) {
                            getStructTreeRoot().copyTo(toDocument, insertBeforePage, increasingPagesRange);
                        } else {
                            getStructTreeRoot().copyTo(toDocument, increasingPagesRange);
                        }
                        insertBeforePage += increasingPagesRange.size();
                    }
                    toDocument.getTagStructureContext().normalizeDocumentRootTag();
                } catch (Exception ex) {
                    throw new PdfException(PdfException.TagStructureCopyingFailedItMightBeCorruptedInOneOfTheDocuments, (Throwable) ex);
                }
            } else {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                logger.warn(LogMessageConstant.NOT_TAGGED_PAGES_IN_TAGGED_DOCUMENT);
            }
        }
        if (this.catalog.isOutlineMode()) {
            copyOutlines(outlinesToCopy, toDocument, page2page);
        }
        return copiedPages;
    }

    public List<PdfPage> copyPagesTo(List<Integer> pagesToCopy, PdfDocument toDocument) {
        return copyPagesTo(pagesToCopy, toDocument, (IPdfPageExtraCopier) null);
    }

    public List<PdfPage> copyPagesTo(List<Integer> pagesToCopy, PdfDocument toDocument, IPdfPageExtraCopier copier) {
        return copyPagesTo(pagesToCopy, toDocument, toDocument.getNumberOfPages() + 1, copier);
    }

    public void flushCopiedObjects(PdfDocument sourceDoc) {
        if (getWriter() != null) {
            getWriter().flushCopiedObjects(sourceDoc.getDocumentId());
        }
    }

    public boolean isCloseReader() {
        return this.closeReader;
    }

    public void setCloseReader(boolean closeReader) {
        checkClosingStatus();
        this.closeReader = closeReader;
    }

    public boolean isCloseWriter() {
        return this.closeWriter;
    }

    public void setCloseWriter(boolean closeWriter) {
        checkClosingStatus();
        this.closeWriter = closeWriter;
    }

    public boolean isFlushUnusedObjects() {
        return this.flushUnusedObjects;
    }

    public void setFlushUnusedObjects(boolean flushUnusedObjects) {
        checkClosingStatus();
        this.flushUnusedObjects = flushUnusedObjects;
    }

    public PdfOutline getOutlines(boolean updateOutlines) {
        checkClosingStatus();
        return this.catalog.getOutlines(updateOutlines);
    }

    public void initializeOutlines() {
        checkClosingStatus();
        getOutlines(false);
    }

    public void addNamedDestination(String key, PdfObject value) {
        checkClosingStatus();
        if (value.isArray() && ((PdfArray) value).get(0).isNumber()) {
            LoggerFactory.getLogger((Class<?>) PdfDocument.class).warn(LogMessageConstant.INVALID_DESTINATION_TYPE);
        }
        this.catalog.addNamedDestination(key, value);
    }

    public List<PdfIndirectReference> listIndirectReferences() {
        checkClosingStatus();
        List<PdfIndirectReference> indRefs = new ArrayList<>(this.xref.size());
        for (int i = 0; i < this.xref.size(); i++) {
            PdfIndirectReference indref = this.xref.get(i);
            if (indref != null) {
                indRefs.add(indref);
            }
        }
        return indRefs;
    }

    public PdfDictionary getTrailer() {
        checkClosingStatus();
        return this.trailer;
    }

    public void addOutputIntent(PdfOutputIntent outputIntent) {
        checkClosingStatus();
        if (outputIntent == null) {
            return;
        }
        PdfArray outputIntents = this.catalog.getPdfObject().getAsArray(PdfName.OutputIntents);
        if (outputIntents == null) {
            outputIntents = new PdfArray();
            this.catalog.put(PdfName.OutputIntents, outputIntents);
        }
        outputIntents.add(outputIntent.getPdfObject());
    }

    public void checkIsoConformance(Object obj, IsoKey key) {
    }

    @Deprecated
    public void checkIsoConformance(Object obj, IsoKey key, PdfResources resources) {
    }

    public void checkIsoConformance(Object obj, IsoKey key, PdfResources resources, PdfStream contentStream) {
    }

    public void checkShowTextIsoConformance(CanvasGraphicsState gState, PdfResources resources) {
    }

    public void addFileAttachment(String key, PdfFileSpec fs) {
        checkClosingStatus();
        this.catalog.addNameToNameTree(key, fs.getPdfObject(), PdfName.EmbeddedFiles);
    }

    public void addAssociatedFile(String description, PdfFileSpec fs) {
        if (null == ((PdfDictionary) fs.getPdfObject()).get(PdfName.AFRelationship)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
            logger.error(LogMessageConstant.ASSOCIATED_FILE_SPEC_SHALL_INCLUDE_AFRELATIONSHIP);
        }
        PdfArray afArray = this.catalog.getPdfObject().getAsArray(PdfName.AF);
        if (afArray == null) {
            afArray = (PdfArray) new PdfArray().makeIndirect(this);
            this.catalog.put(PdfName.AF, afArray);
        }
        afArray.add(fs.getPdfObject());
        addFileAttachment(description, fs);
    }

    public PdfArray getAssociatedFiles() {
        checkClosingStatus();
        return this.catalog.getPdfObject().getAsArray(PdfName.AF);
    }

    public PdfEncryptedPayloadDocument getEncryptedPayloadDocument() {
        PdfCollection collection;
        if ((getReader() == null || !getReader().isEncrypted()) && (collection = getCatalog().getCollection()) != null && collection.isViewHidden()) {
            PdfString documentName = collection.getInitialDocument();
            PdfNameTree embeddedFiles = getCatalog().getNameTree(PdfName.EmbeddedFiles);
            String documentNameUnicode = documentName.toUnicodeString();
            PdfObject fileSpecObject = embeddedFiles.getNames().get(documentNameUnicode);
            if (fileSpecObject != null && fileSpecObject.isDictionary()) {
                try {
                    PdfFileSpec fileSpec = PdfEncryptedPayloadFileSpecFactory.wrap((PdfDictionary) fileSpecObject);
                    if (fileSpec != null) {
                        PdfDictionary embeddedDictionary = ((PdfDictionary) fileSpec.getPdfObject()).getAsDictionary(PdfName.EF);
                        PdfStream stream = embeddedDictionary.getAsStream(PdfName.UF);
                        if (stream == null) {
                            stream = embeddedDictionary.getAsStream(PdfName.F);
                        }
                        if (stream != null) {
                            return new PdfEncryptedPayloadDocument(stream, fileSpec, documentNameUnicode);
                        }
                        return null;
                    }
                    return null;
                } catch (PdfException e) {
                    LoggerFactory.getLogger(getClass()).error(e.getMessage());
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public void setEncryptedPayload(PdfFileSpec fs) {
        if (getWriter() == null) {
            throw new PdfException(PdfException.CannotSetEncryptedPayloadToDocumentOpenedInReadingMode);
        }
        if (writerHasEncryption()) {
            throw new PdfException(PdfException.CannotSetEncryptedPayloadToEncryptedDocument);
        }
        if (!PdfName.EncryptedPayload.equals(((PdfDictionary) fs.getPdfObject()).get(PdfName.AFRelationship))) {
            LoggerFactory.getLogger(getClass()).error(LogMessageConstant.ENCRYPTED_PAYLOAD_FILE_SPEC_SHALL_HAVE_AFRELATIONSHIP_FILED_EQUAL_TO_ENCRYPTED_PAYLOAD);
        }
        PdfEncryptedPayload encryptedPayload = PdfEncryptedPayload.extractFrom(fs);
        if (encryptedPayload == null) {
            throw new PdfException(PdfException.EncryptedPayloadFileSpecDoesntHaveEncryptedPayloadDictionary);
        }
        PdfCollection collection = getCatalog().getCollection();
        if (collection != null) {
            LoggerFactory.getLogger(getClass()).warn(LogMessageConstant.COLLECTION_DICTIONARY_ALREADY_EXISTS_IT_WILL_BE_MODIFIED);
        } else {
            collection = new PdfCollection();
            getCatalog().setCollection(collection);
        }
        collection.setView(2);
        String displayName = PdfEncryptedPayloadFileSpecFactory.generateFileDisplay(encryptedPayload);
        collection.setInitialDocument(displayName);
        addAssociatedFile(displayName, fs);
    }

    public String[] getPageLabels() {
        if (this.catalog.getPageLabelsTree(false) == null) {
            return null;
        }
        Map<Integer, PdfObject> pageLabels = this.catalog.getPageLabelsTree(false).getNumbers();
        if (pageLabels.size() == 0) {
            return null;
        }
        String[] labelStrings = new String[getNumberOfPages()];
        int pageCount = 1;
        String prefix = "";
        String type = "D";
        for (int i = 0; i < getNumberOfPages(); i++) {
            if (pageLabels.containsKey(Integer.valueOf(i))) {
                PdfDictionary labelDictionary = (PdfDictionary) pageLabels.get(Integer.valueOf(i));
                PdfNumber pageRange = labelDictionary.getAsNumber(PdfName.St);
                if (pageRange != null) {
                    pageCount = pageRange.intValue();
                } else {
                    pageCount = 1;
                }
                PdfString p = labelDictionary.getAsString(PdfName.P);
                if (p != null) {
                    prefix = p.toUnicodeString();
                } else {
                    prefix = "";
                }
                PdfName t = labelDictionary.getAsName(PdfName.S);
                if (t != null) {
                    type = t.getValue();
                } else {
                    type = "e";
                }
            }
            switch (type) {
                case "R":
                    labelStrings[i] = prefix + RomanNumbering.toRomanUpperCase(pageCount);
                    break;
                case "r":
                    labelStrings[i] = prefix + RomanNumbering.toRomanLowerCase(pageCount);
                    break;
                case "A":
                    labelStrings[i] = prefix + EnglishAlphabetNumbering.toLatinAlphabetNumberUpperCase(pageCount);
                    break;
                case "a":
                    labelStrings[i] = prefix + EnglishAlphabetNumbering.toLatinAlphabetNumberLowerCase(pageCount);
                    break;
                case "e":
                    labelStrings[i] = prefix;
                    break;
                default:
                    labelStrings[i] = prefix + pageCount;
                    break;
            }
            pageCount++;
        }
        return labelStrings;
    }

    public boolean hasOutlines() {
        return this.catalog.hasOutlines();
    }

    public void setUserProperties(boolean userProperties) {
        PdfBoolean userPropsVal = userProperties ? PdfBoolean.TRUE : PdfBoolean.FALSE;
        updateValueInMarkInfoDict(PdfName.UserProperties, userPropsVal);
    }

    public PdfFont getFont(PdfDictionary dictionary) {
        if (!$assertionsDisabled && dictionary.getIndirectReference() == null) {
            throw new AssertionError();
        }
        if (this.documentFonts.containsKey(dictionary.getIndirectReference())) {
            return this.documentFonts.get(dictionary.getIndirectReference());
        }
        return addFont(PdfFontFactory.createFont(dictionary));
    }

    public PdfFont getDefaultFont() {
        if (this.defaultFont == null) {
            try {
                this.defaultFont = PdfFontFactory.createFont();
                if (this.writer != null) {
                    this.defaultFont.makeIndirect(this);
                }
            } catch (IOException e) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                logger.error(LogMessageConstant.EXCEPTION_WHILE_CREATING_DEFAULT_FONT, (Throwable) e);
                this.defaultFont = null;
            }
        }
        return this.defaultFont;
    }

    public PdfFont addFont(PdfFont font) {
        font.makeIndirect(this);
        font.setForbidRelease();
        this.documentFonts.put(font.getPdfObject().getIndirectReference(), font);
        return font;
    }

    public boolean registerProduct(ProductInfo productInfo) {
        return this.fingerPrint.registerProduct(productInfo);
    }

    public FingerPrint getFingerPrint() {
        return this.fingerPrint;
    }

    public PdfFont findFont(String fontProgram, String encoding) {
        for (PdfFont font : this.documentFonts.values()) {
            if (!font.isFlushed() && font.isBuiltWith(fontProgram, encoding)) {
                return font;
            }
        }
        return null;
    }

    PdfXrefTable getXref() {
        return this.xref;
    }

    boolean isDocumentFont(PdfIndirectReference indRef) {
        return indRef != null && this.documentFonts.containsKey(indRef);
    }

    protected void initTagStructureContext() {
        this.tagStructureContext = new TagStructureContext(this);
    }

    protected void storeLinkAnnotation(PdfPage page, PdfLinkAnnotation annotation) {
        List<PdfLinkAnnotation> pageAnnotations = this.linkAnnotations.get(page);
        if (pageAnnotations == null) {
            pageAnnotations = new ArrayList();
            this.linkAnnotations.put(page, pageAnnotations);
        }
        pageAnnotations.add(annotation);
    }

    protected void checkIsoConformance() {
    }

    protected void markObjectAsMustBeFlushed(PdfObject pdfObject) {
        if (pdfObject.getIndirectReference() != null) {
            pdfObject.getIndirectReference().setState((short) 32);
        }
    }

    protected void flushObject(PdfObject pdfObject, boolean canBeInObjStm) throws IOException {
        this.writer.flushObject(pdfObject, canBeInObjStm);
    }

    protected void open(PdfVersion newPdfVersion) {
        PdfNumber r;
        this.fingerPrint = new FingerPrint();
        try {
            EventCounterHandler.getInstance().onEvent(CoreEvent.PROCESS, this.properties.metaInfo, getClass());
            if (this.reader != null) {
                if (this.reader.pdfDocument != null) {
                    throw new PdfException(PdfException.PdfReaderHasBeenAlreadyUtilized);
                }
                this.reader.pdfDocument = this;
                this.memoryLimitsAwareHandler = this.reader.properties.memoryLimitsAwareHandler;
                if (null == this.memoryLimitsAwareHandler) {
                    this.memoryLimitsAwareHandler = new MemoryLimitsAwareHandler(this.reader.tokens.getSafeFile().length());
                }
                this.reader.readPdf();
                for (ICounter counter : getCounters()) {
                    counter.onDocumentRead(this.reader.getFileLength());
                }
                this.pdfVersion = this.reader.headerPdfVersion;
                this.trailer = new PdfDictionary(this.reader.trailer);
                PdfArray id = this.reader.trailer.getAsArray(PdfName.ID);
                if (id != null) {
                    if (id.size() == 2) {
                        this.originalDocumentId = id.getAsString(0);
                        this.modifiedDocumentId = id.getAsString(1);
                    }
                    if (this.originalDocumentId == null || this.modifiedDocumentId == null) {
                        Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                        logger.error(LogMessageConstant.DOCUMENT_IDS_ARE_CORRUPTED);
                    }
                }
                this.catalog = new PdfCatalog((PdfDictionary) this.trailer.get(PdfName.Root, true));
                if (this.catalog.getPdfObject().containsKey(PdfName.Version)) {
                    PdfVersion catalogVersion = PdfVersion.fromPdfName(this.catalog.getPdfObject().getAsName(PdfName.Version));
                    if (catalogVersion.compareTo(this.pdfVersion) > 0) {
                        this.pdfVersion = catalogVersion;
                    }
                }
                PdfStream xmpMetadataStream = this.catalog.getPdfObject().getAsStream(PdfName.Metadata);
                if (xmpMetadataStream != null) {
                    this.xmpMetadata = xmpMetadataStream.getBytes();
                    try {
                        this.reader.pdfAConformanceLevel = PdfAConformanceLevel.getConformanceLevel(XMPMetaFactory.parseFromBuffer(this.xmpMetadata));
                    } catch (XMPException e) {
                    }
                }
                PdfObject infoDict = this.trailer.get(PdfName.Info);
                this.info = new PdfDocumentInfo(infoDict instanceof PdfDictionary ? (PdfDictionary) infoDict : new PdfDictionary(), this);
                XmpMetaInfoConverter.appendMetadataToInfo(this.xmpMetadata, this.info);
                PdfDictionary str = this.catalog.getPdfObject().getAsDictionary(PdfName.StructTreeRoot);
                if (str != null) {
                    tryInitTagStructure(str);
                }
                if (this.properties.appendMode && (this.reader.hasRebuiltXref() || this.reader.hasFixedXref())) {
                    throw new PdfException(PdfException.AppendModeRequiresADocumentWithoutErrorsEvenIfRecoveryWasPossible);
                }
            }
            this.xref.initFreeReferencesList(this);
            if (this.writer != null) {
                if (this.reader != null && this.reader.hasXrefStm() && this.writer.properties.isFullCompression == null) {
                    this.writer.properties.isFullCompression = true;
                }
                if (this.reader != null && !this.reader.isOpenedWithFullPermission()) {
                    throw new BadPasswordException(BadPasswordException.PdfReaderNotOpenedWithOwnerPassword);
                }
                if (this.reader != null && this.properties.preserveEncryption) {
                    this.writer.crypto = this.reader.decrypt;
                }
                this.writer.document = this;
                if (this.reader == null) {
                    this.catalog = new PdfCatalog(this);
                    this.info = new PdfDocumentInfo(this).addCreationDate();
                }
                updateProducerInInfoDictionary();
                this.info.addModDate();
                this.trailer = new PdfDictionary();
                this.trailer.put(PdfName.Root, this.catalog.getPdfObject().getIndirectReference());
                this.trailer.put(PdfName.Info, this.info.getPdfObject().getIndirectReference());
                if (this.reader != null && this.reader.trailer.containsKey(PdfName.ID)) {
                    this.trailer.put(PdfName.ID, this.reader.trailer.get(PdfName.ID));
                }
                if (this.writer.properties != null) {
                    PdfString readerModifiedId = this.modifiedDocumentId;
                    if (this.writer.properties.initialDocumentId != null && (this.reader == null || this.reader.decrypt == null || (!this.properties.appendMode && !this.properties.preserveEncryption))) {
                        this.originalDocumentId = this.writer.properties.initialDocumentId;
                    }
                    if (this.writer.properties.modifiedDocumentId != null) {
                        this.modifiedDocumentId = this.writer.properties.modifiedDocumentId;
                    }
                    if (this.originalDocumentId == null && this.modifiedDocumentId != null) {
                        this.originalDocumentId = this.modifiedDocumentId;
                    }
                    if (this.modifiedDocumentId == null) {
                        if (this.originalDocumentId == null) {
                            this.originalDocumentId = new PdfString(PdfEncryption.generateNewDocumentId());
                        }
                        this.modifiedDocumentId = this.originalDocumentId;
                    }
                    if (this.writer.properties.modifiedDocumentId == null && this.modifiedDocumentId.equals(readerModifiedId)) {
                        this.modifiedDocumentId = new PdfString(PdfEncryption.generateNewDocumentId());
                    }
                }
                if (!$assertionsDisabled && this.originalDocumentId == null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this.modifiedDocumentId == null) {
                    throw new AssertionError();
                }
            }
            if (this.properties.appendMode) {
                if (!$assertionsDisabled && this.reader == null) {
                    throw new AssertionError();
                }
                RandomAccessFileOrArray file = this.reader.tokens.getSafeFile();
                byte[] buffer = new byte[8192];
                while (true) {
                    int n = file.read(buffer);
                    if (n <= 0) {
                        break;
                    } else {
                        this.writer.write(buffer, 0, n);
                    }
                }
                file.close();
                this.writer.write(10);
                this.writer.properties.isFullCompression = Boolean.valueOf(this.reader.hasXrefStm());
                this.writer.crypto = this.reader.decrypt;
                if (newPdfVersion != null && this.pdfVersion.compareTo(PdfVersion.PDF_1_4) >= 0 && newPdfVersion.compareTo(this.reader.headerPdfVersion) > 0) {
                    this.catalog.put(PdfName.Version, newPdfVersion.toPdfName());
                    this.catalog.setModified();
                    this.pdfVersion = newPdfVersion;
                }
            } else if (this.writer != null) {
                if (newPdfVersion != null) {
                    this.pdfVersion = newPdfVersion;
                }
                this.writer.writeHeader();
                if (this.writer.crypto == null) {
                    this.writer.initCryptoIfSpecified(this.pdfVersion);
                }
                if (this.writer.crypto != null) {
                    if (this.writer.crypto.getCryptoMode() < 3) {
                        VersionConforming.validatePdfVersionForDeprecatedFeatureLogWarn(this, PdfVersion.PDF_2_0, VersionConforming.DEPRECATED_ENCRYPTION_ALGORITHMS);
                    } else if (this.writer.crypto.getCryptoMode() == 3 && (r = this.writer.crypto.getPdfObject().getAsNumber(PdfName.R)) != null && r.intValue() == 5) {
                        VersionConforming.validatePdfVersionForDeprecatedFeatureLogWarn(this, PdfVersion.PDF_2_0, VersionConforming.DEPRECATED_AES256_REVISION);
                    }
                }
            }
        } catch (IOException e2) {
            throw new PdfException(PdfException.CannotOpenDocument, e2, this);
        }
    }

    protected void addCustomMetadataExtensions(XMPMeta xmpMeta) {
    }

    protected void updateXmpMetadata() {
        try {
            if (this.xmpMetadata != null || this.writer.properties.addXmpMetadata || this.pdfVersion.compareTo(PdfVersion.PDF_2_0) >= 0) {
                setXmpMetadata(updateDefaultXmpMetadata());
            }
        } catch (XMPException e) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
            logger.error(LogMessageConstant.EXCEPTION_WHILE_UPDATING_XMPMETADATA, (Throwable) e);
        }
    }

    protected XMPMeta updateDefaultXmpMetadata() throws XMPException {
        XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(getXmpMetadata(true));
        XmpMetaInfoConverter.appendDocumentInfoToMetadata(this.info, xmpMeta);
        if (isTagged() && this.writer.properties.addUAXmpMetadata && !isXmpMetaHasProperty(xmpMeta, XMPConst.NS_PDFUA_ID, "part")) {
            xmpMeta.setPropertyInteger(XMPConst.NS_PDFUA_ID, "part", 1, new PropertyOptions(1073741824));
        }
        return xmpMeta;
    }

    protected Collection<PdfFont> getDocumentFonts() {
        return this.documentFonts.values();
    }

    protected void flushFonts() {
        if (this.properties.appendMode) {
            for (PdfFont font : getDocumentFonts()) {
                if (font.getPdfObject().checkState((short) 64) || font.getPdfObject().getIndirectReference().checkState((short) 8)) {
                    font.flush();
                }
            }
            return;
        }
        Iterator<PdfFont> it = getDocumentFonts().iterator();
        while (it.hasNext()) {
            it.next().flush();
        }
    }

    protected void checkAndAddPage(int index, PdfPage page) {
        if (page.isFlushed()) {
            throw new PdfException(PdfException.FlushedPageCannotBeAddedOrInserted, page);
        }
        if (page.getDocument() != null && this != page.getDocument()) {
            throw new PdfException(PdfException.Page1CannotBeAddedToDocument2BecauseItBelongsToDocument3).setMessageParams(page, this, page.getDocument());
        }
        this.catalog.getPageTree().addPage(index, page);
    }

    protected void checkAndAddPage(PdfPage page) {
        if (page.isFlushed()) {
            throw new PdfException(PdfException.FlushedPageCannotBeAddedOrInserted, page);
        }
        if (page.getDocument() != null && this != page.getDocument()) {
            throw new PdfException(PdfException.Page1CannotBeAddedToDocument2BecauseItBelongsToDocument3).setMessageParams(page, this, page.getDocument());
        }
        this.catalog.getPageTree().addPage(page);
    }

    protected void checkClosingStatus() {
        if (this.closed) {
            throw new PdfException(PdfException.DocumentClosedItIsImpossibleToExecuteAction);
        }
    }

    @Deprecated
    protected List<ICounter> getCounters() {
        return CounterManager.getInstance().getCounters(PdfDocument.class);
    }

    protected IPdfPageFactory getPageFactory() {
        return pdfPageFactory;
    }

    final VersionInfo getVersionInfo() {
        return this.versionInfo;
    }

    private void updateProducerInInfoDictionary() {
        String producer;
        String producer2 = null;
        if (this.reader == null) {
            producer = this.versionInfo.getVersion();
        } else {
            if (this.info.getPdfObject().containsKey(PdfName.Producer)) {
                producer2 = this.info.getPdfObject().getAsString(PdfName.Producer).toUnicodeString();
            }
            producer = addModifiedPostfix(producer2);
        }
        this.info.getPdfObject().put(PdfName.Producer, new PdfString(producer));
    }

    protected void tryInitTagStructure(PdfDictionary str) {
        try {
            this.structTreeRoot = new PdfStructTreeRoot(str, this);
            this.structParentIndex = getStructTreeRoot().getParentTreeNextKey();
        } catch (Exception ex) {
            this.structTreeRoot = null;
            this.structParentIndex = -1;
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
            logger.error(LogMessageConstant.TAG_STRUCTURE_INIT_FAILED, (Throwable) ex);
        }
    }

    private void tryFlushTagStructure(boolean isAppendMode) {
        try {
            if (this.tagStructureContext != null) {
                this.tagStructureContext.prepareToDocumentClosing();
            }
            if (!isAppendMode || this.structTreeRoot.getPdfObject().isModified()) {
                this.structTreeRoot.flush();
            }
        } catch (Exception ex) {
            throw new PdfException(PdfException.TagStructureFlushingFailedItMightBeCorrupted, (Throwable) ex);
        }
    }

    private void updateValueInMarkInfoDict(PdfName key, PdfObject value) {
        PdfDictionary markInfo = this.catalog.getPdfObject().getAsDictionary(PdfName.MarkInfo);
        if (markInfo == null) {
            markInfo = new PdfDictionary();
            this.catalog.getPdfObject().put(PdfName.MarkInfo, markInfo);
        }
        markInfo.put(key, value);
    }

    private void removeUnusedWidgetsFromFields(PdfPage page) {
        if (page.isFlushed()) {
            return;
        }
        List<PdfAnnotation> annots = page.getAnnotations();
        for (PdfAnnotation annot : annots) {
            if (annot.getSubtype().equals(PdfName.Widget)) {
                ((PdfWidgetAnnotation) annot).releaseFormFieldFromWidgetAnnotation();
            }
        }
    }

    private void copyLinkAnnotations(PdfDocument toDocument, Map<PdfPage, PdfPage> page2page) {
        List<PdfName> excludedKeys = new ArrayList<>();
        excludedKeys.add(PdfName.Dest);
        excludedKeys.add(PdfName.A);
        for (Map.Entry<PdfPage, List<PdfLinkAnnotation>> entry : this.linkAnnotations.entrySet()) {
            for (PdfLinkAnnotation annot : entry.getValue()) {
                boolean toCopyAnnot = true;
                PdfDestination copiedDest = null;
                PdfDictionary copiedAction = null;
                PdfObject dest = annot.getDestinationObject();
                if (dest != null) {
                    copiedDest = getCatalog().copyDestination(dest, page2page, toDocument);
                    toCopyAnnot = copiedDest != null;
                } else {
                    PdfDictionary action = annot.getAction();
                    if (action != null) {
                        if (PdfName.GoTo.equals(action.get(PdfName.S))) {
                            copiedAction = action.copyTo(toDocument, Arrays.asList(PdfName.D), false);
                            PdfDestination goToDest = getCatalog().copyDestination(action.get(PdfName.D), page2page, toDocument);
                            if (goToDest != null) {
                                copiedAction.put(PdfName.D, goToDest.getPdfObject());
                            } else {
                                toCopyAnnot = false;
                            }
                        } else {
                            copiedAction = (PdfDictionary) action.copyTo(toDocument, false);
                        }
                    }
                }
                if (toCopyAnnot) {
                    PdfLinkAnnotation newAnnot = (PdfLinkAnnotation) PdfAnnotation.makeAnnotation(annot.getPdfObject().copyTo(toDocument, excludedKeys, true));
                    if (copiedDest != null) {
                        newAnnot.setDestination(copiedDest);
                    }
                    if (copiedAction != null) {
                        newAnnot.setAction(copiedAction);
                    }
                    entry.getKey().addAnnotation(-1, newAnnot, false);
                }
            }
        }
        this.linkAnnotations.clear();
    }

    private void copyOutlines(Set<PdfOutline> outlines, PdfDocument toDocument, Map<PdfPage, PdfPage> page2page) {
        Set<PdfOutline> outlinesToCopy = new HashSet<>();
        outlinesToCopy.addAll(outlines);
        for (PdfOutline outline : outlines) {
            getAllOutlinesToCopy(outline, outlinesToCopy);
        }
        PdfOutline rootOutline = toDocument.getOutlines(false);
        if (rootOutline == null) {
            rootOutline = new PdfOutline(toDocument);
            rootOutline.setTitle("Outlines");
        }
        cloneOutlines(outlinesToCopy, rootOutline, getOutlines(false), page2page, toDocument);
    }

    private void getAllOutlinesToCopy(PdfOutline outline, Set<PdfOutline> outlinesToCopy) {
        PdfOutline parent = outline.getParent();
        if ("Outlines".equals(parent.getTitle()) || outlinesToCopy.contains(parent)) {
            return;
        }
        outlinesToCopy.add(parent);
        getAllOutlinesToCopy(parent, outlinesToCopy);
    }

    private void cloneOutlines(Set<PdfOutline> outlinesToCopy, PdfOutline newParent, PdfOutline oldParent, Map<PdfPage, PdfPage> page2page, PdfDocument toDocument) {
        if (null == oldParent) {
            return;
        }
        for (PdfOutline outline : oldParent.getAllChildren()) {
            if (outlinesToCopy.contains(outline)) {
                PdfDestination copiedDest = null;
                if (null != outline.getDestination()) {
                    PdfObject destObjToCopy = outline.getDestination().getPdfObject();
                    copiedDest = getCatalog().copyDestination(destObjToCopy, page2page, toDocument);
                }
                PdfOutline child = newParent.addOutline(outline.getTitle());
                if (copiedDest != null) {
                    child.addDestination(copiedDest);
                }
                cloneOutlines(outlinesToCopy, child, outline, page2page, toDocument);
            }
        }
    }

    private void ensureTreeRootAddedToNames(PdfObject treeRoot, PdfName treeType) {
        PdfDictionary names = this.catalog.getPdfObject().getAsDictionary(PdfName.Names);
        if (names == null) {
            names = new PdfDictionary();
            this.catalog.put(PdfName.Names, names);
            names.makeIndirect(this);
        }
        names.put(treeType, treeRoot);
        names.setModified();
    }

    private static boolean isXmpMetaHasProperty(XMPMeta xmpMeta, String schemaNS, String propName) throws XMPException {
        return xmpMeta.getProperty(schemaNS, propName) != null;
    }

    private byte[] getSerializedBytes() throws IOException {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            try {
                bos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(bos);
                oos.writeObject(this);
                oos.flush();
                byte[] byteArray = bos.toByteArray();
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e2) {
                    }
                }
                return byteArray;
            } catch (Exception e3) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfDocument.class);
                logger.warn(LogMessageConstant.DOCUMENT_SERIALIZATION_EXCEPTION_RAISED, (Throwable) e3);
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e4) {
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e5) {
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e6) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e7) {
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDocumentId() {
        return this.documentId;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.tagStructureContext != null) {
            LoggerFactory.getLogger(getClass()).warn(LogMessageConstant.TAG_STRUCTURE_CONTEXT_WILL_BE_REINITIALIZED_ON_SERIALIZATION);
        }
        out.defaultWriteObject();
    }

    private boolean writerHasEncryption() {
        return this.writer.properties.isStandardEncryptionUsed() || this.writer.properties.isPublicKeyEncryptionUsed();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfDocument$IndirectRefDescription.class */
    static class IndirectRefDescription {
        final long docId;
        final int objNr;
        final int genNr;

        IndirectRefDescription(PdfIndirectReference reference) {
            this.docId = reference.getDocument().getDocumentId();
            this.objNr = reference.getObjNumber();
            this.genNr = reference.getGenNumber();
        }

        public int hashCode() {
            int result = (int) this.docId;
            return (((result * 31) + this.objNr) * 31) + this.genNr;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IndirectRefDescription that = (IndirectRefDescription) o;
            return this.docId == that.docId && this.objNr == that.objNr && this.genNr == that.genNr;
        }
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.versionInfo == null) {
            this.versionInfo = Version.getInstance().getInfo();
        }
        this.eventDispatcher = new EventDispatcher();
    }

    private String addModifiedPostfix(String producer) {
        StringBuilder buf;
        if (producer == null || !this.versionInfo.getVersion().contains(this.versionInfo.getProduct())) {
            return this.versionInfo.getVersion();
        }
        int idx = producer.indexOf("; modified using");
        if (idx == -1) {
            buf = new StringBuilder(producer);
        } else {
            buf = new StringBuilder(producer.substring(0, idx));
        }
        buf.append("; modified using ");
        buf.append(this.versionInfo.getVersion());
        return buf.toString();
    }
}
