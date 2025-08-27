package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteBuffer;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.io.source.WindowRandomAccessSource;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.crypto.securityhandler.UnsupportedSecurityHandlerException;
import com.itextpdf.kernel.pdf.filters.FilterHandlers;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfReader.class */
public class PdfReader implements Closeable, Serializable {
    private static final long serialVersionUID = -3584187443691964939L;
    private static final String endstream2 = "\nendstream";
    private static final String endstream3 = "\r\nendstream";
    private static final String endstream4 = "\rendstream";
    private boolean unethicalReading;
    private boolean memorySavingMode;
    private PdfIndirectReference currentIndirectReference;
    private String sourcePath;
    protected PdfTokenizer tokens;
    protected PdfEncryption decrypt;
    protected PdfVersion headerPdfVersion;
    protected long lastXref;
    protected long eofPos;
    protected PdfDictionary trailer;
    protected PdfDocument pdfDocument;
    protected PdfAConformanceLevel pdfAConformanceLevel;
    protected ReaderProperties properties;
    protected boolean encrypted;
    protected boolean rebuiltXref;
    protected boolean hybridXref;
    protected boolean fixedXref;
    protected boolean xrefStm;
    private static final String endstream1 = "endstream";
    private static final byte[] endstream = ByteUtils.getIsoBytes(endstream1);
    private static final byte[] endobj = ByteUtils.getIsoBytes("endobj");
    protected static boolean correctStreamLength = true;

    public PdfReader(IRandomAccessSource byteSource, ReaderProperties properties) throws IOException {
        this.encrypted = false;
        this.rebuiltXref = false;
        this.hybridXref = false;
        this.fixedXref = false;
        this.xrefStm = false;
        this.properties = properties;
        this.tokens = getOffsetTokeniser(byteSource);
    }

    public PdfReader(InputStream is, ReaderProperties properties) throws IOException {
        this(new RandomAccessSourceFactory().createSource(is), properties);
    }

    public PdfReader(File file) throws IOException {
        this(file.getAbsolutePath());
    }

    public PdfReader(InputStream is) throws IOException {
        this(is, new ReaderProperties());
    }

    public PdfReader(String filename, ReaderProperties properties) throws IOException {
        this(new RandomAccessSourceFactory().setForceRead(false).createBestSource(filename), properties);
        this.sourcePath = filename;
    }

    public PdfReader(String filename) throws IOException {
        this(filename, new ReaderProperties());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.tokens.close();
    }

    public PdfReader setUnethicalReading(boolean unethicalReading) {
        this.unethicalReading = unethicalReading;
        return this;
    }

    public PdfReader setMemorySavingMode(boolean memorySavingMode) {
        this.memorySavingMode = memorySavingMode;
        return this;
    }

    public boolean isCloseStream() {
        return this.tokens.isCloseStream();
    }

    public void setCloseStream(boolean closeStream) {
        this.tokens.setCloseStream(closeStream);
    }

    public boolean hasRebuiltXref() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.rebuiltXref;
    }

    public boolean hasHybridXref() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.hybridXref;
    }

    public boolean hasXrefStm() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.xrefStm;
    }

    public boolean hasFixedXref() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.fixedXref;
    }

    public long getLastXref() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.lastXref;
    }

    public byte[] readStreamBytes(PdfStream stream, boolean decode) throws IOException {
        byte[] b = readStreamBytesRaw(stream);
        if (decode && b != null) {
            return decodeBytes(b, stream);
        }
        return b;
    }

    public byte[] readStreamBytesRaw(PdfStream stream) throws IOException {
        PdfName type = stream.getAsName(PdfName.Type);
        if (!PdfName.XRefStm.equals(type) && !PdfName.ObjStm.equals(type)) {
            checkPdfStreamLength(stream);
        }
        long offset = stream.getOffset();
        if (offset <= 0) {
            return null;
        }
        int length = stream.getLength();
        if (length <= 0) {
            return new byte[0];
        }
        RandomAccessFileOrArray file = this.tokens.getSafeFile();
        try {
            file.seek(stream.getOffset());
            byte[] bytes = new byte[length];
            file.readFully(bytes);
            if (this.decrypt != null && !this.decrypt.isEmbeddedFilesOnly()) {
                PdfObject filter = stream.get(PdfName.Filter, true);
                boolean skip = false;
                if (filter != null) {
                    if (PdfName.Crypt.equals(filter)) {
                        skip = true;
                    } else if (filter.getType() == 1) {
                        PdfArray filters = (PdfArray) filter;
                        int k = 0;
                        while (true) {
                            if (k < filters.size()) {
                                if (filters.isEmpty() || !PdfName.Crypt.equals(filters.get(k, true))) {
                                    k++;
                                } else {
                                    skip = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    filter.release();
                }
                if (!skip) {
                    this.decrypt.setHashKeyForNextObject(stream.getIndirectReference().getObjNumber(), stream.getIndirectReference().getGenNumber());
                    bytes = this.decrypt.decryptByteArray(bytes);
                }
            }
            return bytes;
        } finally {
            try {
                file.close();
            } catch (Exception e) {
            }
        }
    }

    public InputStream readStream(PdfStream stream, boolean decode) throws IOException {
        byte[] bytes = readStreamBytes(stream, decode);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        }
        return null;
    }

    public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary) {
        return decodeBytes(b, streamDictionary, FilterHandlers.getDefaultFilterHandlers());
    }

    public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary, Map<PdfName, IFilterHandler> filterHandlers) {
        PdfDictionary decodeParams;
        PdfObject dpEntry;
        if (b == null) {
            return null;
        }
        PdfObject filter = streamDictionary.get(PdfName.Filter);
        PdfArray filters = new PdfArray();
        if (filter != null) {
            if (filter.getType() == 6) {
                filters.add(filter);
            } else if (filter.getType() == 1) {
                filters = (PdfArray) filter;
            }
        }
        MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;
        if (null != streamDictionary.getIndirectReference()) {
            memoryLimitsAwareHandler = streamDictionary.getIndirectReference().getDocument().memoryLimitsAwareHandler;
        }
        if (null != memoryLimitsAwareHandler) {
            HashSet<PdfName> filterSet = new HashSet<>();
            int index = 0;
            while (true) {
                if (index >= filters.size()) {
                    break;
                }
                if (filterSet.add(filters.getAsName(index))) {
                    index++;
                } else {
                    memoryLimitsAwareHandler.beginDecompressedPdfStreamProcessing();
                    break;
                }
            }
            if (index == filters.size()) {
                memoryLimitsAwareHandler = null;
            }
        }
        PdfArray dp = new PdfArray();
        PdfObject dpo = streamDictionary.get(PdfName.DecodeParms);
        if (dpo == null || (dpo.getType() != 3 && dpo.getType() != 1)) {
            if (dpo != null) {
                dpo.release();
            }
            dpo = streamDictionary.get(PdfName.DP);
        }
        if (dpo != null) {
            if (dpo.getType() == 3) {
                dp.add(dpo);
            } else if (dpo.getType() == 1) {
                dp = (PdfArray) dpo;
            }
            dpo.release();
        }
        for (int j = 0; j < filters.size(); j++) {
            PdfName filterName = (PdfName) filters.get(j);
            IFilterHandler filterHandler = filterHandlers.get(filterName);
            if (filterHandler == null) {
                throw new PdfException(PdfException.Filter1IsNotSupported).setMessageParams(filterName);
            }
            if (j >= dp.size() || (dpEntry = dp.get(j, true)) == null || dpEntry.getType() == 7) {
                decodeParams = null;
            } else {
                if (dpEntry.getType() != 3) {
                    throw new PdfException(PdfException.DecodeParameterType1IsNotSupported).setMessageParams(dpEntry.getClass().toString());
                }
                decodeParams = (PdfDictionary) dpEntry;
            }
            b = filterHandler.decode(b, filterName, decodeParams, streamDictionary);
            if (null != memoryLimitsAwareHandler) {
                memoryLimitsAwareHandler.considerBytesOccupiedByDecompressedPdfStream(b.length);
            }
        }
        if (null != memoryLimitsAwareHandler) {
            memoryLimitsAwareHandler.endDecompressedPdfStreamProcessing();
        }
        return b;
    }

    public RandomAccessFileOrArray getSafeFile() {
        return this.tokens.getSafeFile();
    }

    public long getFileLength() throws IOException {
        return this.tokens.getSafeFile().length();
    }

    public boolean isOpenedWithFullPermission() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return !this.encrypted || this.decrypt.isOpenedWithFullPermission() || this.unethicalReading;
    }

    public long getPermissions() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        long perm = 0;
        if (this.encrypted && this.decrypt.getPermissions() != null) {
            perm = this.decrypt.getPermissions().longValue();
        }
        return perm;
    }

    public int getCryptoMode() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        if (this.decrypt == null) {
            return -1;
        }
        return this.decrypt.getCryptoMode();
    }

    public PdfAConformanceLevel getPdfAConformanceLevel() {
        return this.pdfAConformanceLevel;
    }

    public byte[] computeUserPassword() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        if (!this.encrypted || !this.decrypt.isOpenedWithFullPermission()) {
            return null;
        }
        return this.decrypt.computeUserPassword(this.properties.password);
    }

    public byte[] getOriginalFileId() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        PdfArray id = this.trailer.getAsArray(PdfName.ID);
        if (id != null && id.size() == 2) {
            return ByteUtils.getIsoBytes(id.getAsString(0).getValue());
        }
        return new byte[0];
    }

    public byte[] getModifiedFileId() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        PdfArray id = this.trailer.getAsArray(PdfName.ID);
        if (id != null && id.size() == 2) {
            return ByteUtils.getIsoBytes(id.getAsString(1).getValue());
        }
        return new byte[0];
    }

    public boolean isEncrypted() {
        if (this.pdfDocument == null || !this.pdfDocument.getXref().isReadingCompleted()) {
            throw new PdfException(PdfException.DocumentHasNotBeenReadYet);
        }
        return this.encrypted;
    }

    protected void readPdf() throws IOException {
        String version = this.tokens.checkPdfHeader();
        try {
            this.headerPdfVersion = PdfVersion.fromString(version);
            try {
                readXref();
            } catch (RuntimeException ex) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfReader.class);
                logger.error("Error occurred while reading cross reference table. Cross reference table will be rebuilt.", (Throwable) ex);
                rebuildXref();
            }
            this.pdfDocument.getXref().markReadingCompleted();
            readDecryptObj();
        } catch (IllegalArgumentException e) {
            throw new PdfException(PdfException.PdfVersionNotValid, version);
        }
    }

    protected void readObjectStream(PdfStream objectStream) throws IOException {
        PdfObject obj;
        int objectStreamNumber = objectStream.getIndirectReference().getObjNumber();
        int first = objectStream.getAsNumber(PdfName.First).intValue();
        int n = objectStream.getAsNumber(PdfName.N).intValue();
        byte[] bytes = readStreamBytes(objectStream, true);
        PdfTokenizer saveTokens = this.tokens;
        try {
            this.tokens = new PdfTokenizer(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(bytes)));
            int[] address = new int[n];
            int[] objNumber = new int[n];
            boolean ok = true;
            int k = 0;
            while (true) {
                if (k >= n) {
                    break;
                }
                ok = this.tokens.nextToken();
                if (!ok) {
                    break;
                }
                if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Number) {
                    ok = false;
                    break;
                }
                objNumber[k] = this.tokens.getIntValue();
                ok = this.tokens.nextToken();
                if (!ok) {
                    break;
                }
                if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Number) {
                    ok = false;
                    break;
                } else {
                    address[k] = this.tokens.getIntValue() + first;
                    k++;
                }
            }
            if (!ok) {
                throw new PdfException(PdfException.ErrorWhileReadingObjectStream);
            }
            for (int k2 = 0; k2 < n; k2++) {
                this.tokens.seek(address[k2]);
                this.tokens.nextToken();
                PdfIndirectReference reference = this.pdfDocument.getXref().get(objNumber[k2]);
                if (reference.refersTo == null && reference.getObjStreamNumber() == objectStreamNumber) {
                    if (this.tokens.getTokenType() == PdfTokenizer.TokenType.Number) {
                        obj = new PdfNumber(this.tokens.getByteContent());
                    } else {
                        this.tokens.seek(address[k2]);
                        obj = readObject(false, true);
                    }
                    reference.setRefersTo(obj);
                    obj.setIndirectReference(reference);
                }
            }
            objectStream.getIndirectReference().setState((short) 16);
            this.tokens = saveTokens;
        } catch (Throwable th) {
            this.tokens = saveTokens;
            throw th;
        }
    }

    protected PdfObject readObject(PdfIndirectReference reference) {
        return readObject(reference, true);
    }

    protected PdfObject readObject(boolean readAsDirect) throws IOException {
        return readObject(readAsDirect, false);
    }

    protected PdfObject readReference(boolean readAsDirect) {
        int num = this.tokens.getObjNr();
        if (num < 0) {
            return createPdfNullInstance(readAsDirect);
        }
        PdfXrefTable table = this.pdfDocument.getXref();
        PdfIndirectReference reference = table.get(num);
        if (reference != null) {
            if (reference.isFree()) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfReader.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.INVALID_INDIRECT_REFERENCE, Integer.valueOf(this.tokens.getObjNr()), Integer.valueOf(this.tokens.getGenNr())));
                return createPdfNullInstance(readAsDirect);
            }
            if (reference.getGenNumber() != this.tokens.getGenNr()) {
                if (this.fixedXref) {
                    Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfReader.class);
                    logger2.warn(MessageFormatUtil.format(LogMessageConstant.INVALID_INDIRECT_REFERENCE, Integer.valueOf(this.tokens.getObjNr()), Integer.valueOf(this.tokens.getGenNr())));
                    return createPdfNullInstance(readAsDirect);
                }
                throw new PdfException(PdfException.InvalidIndirectReference1, MessageFormatUtil.format("{0} {1} R", Integer.valueOf(reference.getObjNumber()), Integer.valueOf(reference.getGenNumber())));
            }
        } else {
            if (table.isReadingCompleted()) {
                Logger logger3 = LoggerFactory.getLogger((Class<?>) PdfReader.class);
                logger3.warn(MessageFormatUtil.format(LogMessageConstant.INVALID_INDIRECT_REFERENCE, Integer.valueOf(this.tokens.getObjNr()), Integer.valueOf(this.tokens.getGenNr())));
                return createPdfNullInstance(readAsDirect);
            }
            reference = table.add((PdfIndirectReference) new PdfIndirectReference(this.pdfDocument, num, this.tokens.getGenNr(), 0L).setState((short) 4));
        }
        return reference;
    }

    protected PdfObject readObject(boolean readAsDirect, boolean objStm) throws IOException {
        boolean hasNext;
        this.tokens.nextValidToken();
        PdfTokenizer.TokenType type = this.tokens.getTokenType();
        switch (type) {
            case StartDic:
                PdfDictionary dict = readDictionary(objStm);
                long pos = this.tokens.getPosition();
                do {
                    hasNext = this.tokens.nextToken();
                    if (hasNext) {
                    }
                    if (hasNext || !this.tokens.tokenValueEqualsTo(PdfTokenizer.Stream)) {
                        this.tokens.seek(pos);
                        return dict;
                    }
                    while (true) {
                        int ch2 = this.tokens.read();
                        if (ch2 != 32 && ch2 != 9 && ch2 != 0 && ch2 != 12) {
                            if (ch2 != 10) {
                                ch2 = this.tokens.read();
                            }
                            if (ch2 != 10) {
                                this.tokens.backOnePosition(ch2);
                            }
                            return new PdfStream(this.tokens.getPosition(), dict);
                        }
                    }
                } while (this.tokens.getTokenType() == PdfTokenizer.TokenType.Comment);
                if (hasNext) {
                }
                this.tokens.seek(pos);
                return dict;
            case StartArray:
                return readArray(objStm);
            case Number:
                return new PdfNumber(this.tokens.getByteContent());
            case String:
                PdfString pdfString = new PdfString(this.tokens.getByteContent(), this.tokens.isHexString());
                if (this.encrypted && !this.decrypt.isEmbeddedFilesOnly() && !objStm) {
                    pdfString.setDecryption(this.currentIndirectReference.getObjNumber(), this.currentIndirectReference.getGenNumber(), this.decrypt);
                }
                return pdfString;
            case Name:
                return readPdfName(readAsDirect);
            case Ref:
                return readReference(readAsDirect);
            case EndOfFile:
                throw new PdfException(PdfException.UnexpectedEndOfFile);
            default:
                if (this.tokens.tokenValueEqualsTo(PdfTokenizer.Null)) {
                    return createPdfNullInstance(readAsDirect);
                }
                if (this.tokens.tokenValueEqualsTo(PdfTokenizer.True)) {
                    if (readAsDirect) {
                        return PdfBoolean.TRUE;
                    }
                    return new PdfBoolean(true);
                }
                if (this.tokens.tokenValueEqualsTo(PdfTokenizer.False)) {
                    if (readAsDirect) {
                        return PdfBoolean.FALSE;
                    }
                    return new PdfBoolean(false);
                }
                return null;
        }
    }

    protected PdfName readPdfName(boolean readAsDirect) {
        PdfName cachedName;
        if (readAsDirect && (cachedName = PdfName.staticNames.get(this.tokens.getStringValue())) != null) {
            return cachedName;
        }
        return new PdfName(this.tokens.getByteContent());
    }

    protected PdfDictionary readDictionary(boolean objStm) throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            this.tokens.nextValidToken();
            if (this.tokens.getTokenType() != PdfTokenizer.TokenType.EndDic) {
                if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Name) {
                    this.tokens.throwError(PdfException.DictionaryKey1IsNotAName, this.tokens.getStringValue());
                }
                PdfName name = readPdfName(true);
                PdfObject obj = readObject(true, objStm);
                if (obj == null) {
                    if (this.tokens.getTokenType() == PdfTokenizer.TokenType.EndDic) {
                        this.tokens.throwError(PdfException.UnexpectedGtGt, new Object[0]);
                    }
                    if (this.tokens.getTokenType() == PdfTokenizer.TokenType.EndArray) {
                        this.tokens.throwError("Unexpected close bracket.", new Object[0]);
                    }
                }
                dic.put(name, obj);
            } else {
                return dic;
            }
        }
    }

    protected PdfArray readArray(boolean objStm) throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readObject(true, objStm);
            if (obj == null) {
                if (this.tokens.getTokenType() != PdfTokenizer.TokenType.EndArray) {
                    if (this.tokens.getTokenType() == PdfTokenizer.TokenType.EndDic) {
                        this.tokens.throwError(PdfException.UnexpectedGtGt, new Object[0]);
                    }
                } else {
                    return array;
                }
            }
            array.add(obj);
        }
    }

    protected void readXref() throws IOException {
        this.tokens.seek(this.tokens.getStartxref());
        this.tokens.nextToken();
        if (!this.tokens.tokenValueEqualsTo(PdfTokenizer.Startxref)) {
            throw new PdfException("PDF startxref not found.", this.tokens);
        }
        this.tokens.nextToken();
        if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Number) {
            throw new PdfException(PdfException.PdfStartxrefIsNotFollowedByANumber, this.tokens);
        }
        long startxref = this.tokens.getLongValue();
        this.lastXref = startxref;
        this.eofPos = this.tokens.getPosition();
        try {
            if (readXrefStream(startxref)) {
                this.xrefStm = true;
                return;
            }
        } catch (Exception e) {
        }
        this.pdfDocument.getXref().clear();
        this.tokens.seek(startxref);
        this.trailer = readXrefSection();
        PdfDictionary xrefSection = this.trailer;
        while (true) {
            PdfDictionary trailer2 = xrefSection;
            PdfNumber prev = (PdfNumber) trailer2.get(PdfName.Prev);
            if (prev == null) {
                Integer xrefSize = this.trailer.getAsInt(PdfName.Size);
                if (xrefSize == null) {
                    throw new PdfException(PdfException.InvalidXrefTable);
                }
                return;
            } else {
                if (prev.longValue() == startxref) {
                    throw new PdfException(PdfException.TrailerPrevEntryPointsToItsOwnCrossReferenceSection);
                }
                startxref = prev.longValue();
                this.tokens.seek(startxref);
                xrefSection = readXrefSection();
            }
        }
    }

    protected PdfDictionary readXrefSection() throws IOException {
        this.tokens.nextValidToken();
        if (!this.tokens.tokenValueEqualsTo(PdfTokenizer.Xref)) {
            this.tokens.throwError(PdfException.XrefSubsectionNotFound, new Object[0]);
        }
        PdfXrefTable xref = this.pdfDocument.getXref();
        while (true) {
            this.tokens.nextValidToken();
            if (this.tokens.tokenValueEqualsTo(PdfTokenizer.Trailer)) {
                break;
            }
            if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Number) {
                this.tokens.throwError(PdfException.ObjectNumberOfTheFirstObjectInThisXrefSubsectionNotFound, new Object[0]);
            }
            int start = this.tokens.getIntValue();
            this.tokens.nextValidToken();
            if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Number) {
                this.tokens.throwError(PdfException.NumberOfEntriesInThisXrefSubsectionNotFound, new Object[0]);
            }
            int end = this.tokens.getIntValue() + start;
            int num = start;
            while (num < end) {
                this.tokens.nextValidToken();
                long pos = this.tokens.getLongValue();
                this.tokens.nextValidToken();
                int gen = this.tokens.getIntValue();
                this.tokens.nextValidToken();
                if (pos == 0 && gen == 65535 && num == 1 && start != 0) {
                    num = 0;
                    end--;
                } else {
                    PdfIndirectReference reference = xref.get(num);
                    boolean refReadingState = reference != null && reference.checkState((short) 4) && reference.getGenNumber() == gen;
                    boolean refFirstEncountered = reference == null || (!refReadingState && reference.getDocument() == null);
                    if (refFirstEncountered) {
                        reference = new PdfIndirectReference(this.pdfDocument, num, gen, pos);
                    } else if (refReadingState) {
                        reference.setOffset(pos);
                        reference.clearState((short) 4);
                    }
                    if (this.tokens.tokenValueEqualsTo(PdfTokenizer.N)) {
                        if (pos == 0) {
                            this.tokens.throwError(PdfException.FilePosition1CrossReferenceEntryInThisXrefSubsection, new Object[0]);
                        }
                    } else if (this.tokens.tokenValueEqualsTo(PdfTokenizer.F)) {
                        if (refFirstEncountered) {
                            reference.setState((short) 2);
                        }
                    } else {
                        this.tokens.throwError(PdfException.InvalidCrossReferenceEntryInThisXrefSubsection, new Object[0]);
                    }
                    if (refFirstEncountered) {
                        xref.add(reference);
                    }
                }
                num++;
            }
        }
        PdfDictionary trailer = (PdfDictionary) readObject(false);
        PdfObject xrs = trailer.get(PdfName.XRefStm);
        if (xrs != null && xrs.getType() == 8) {
            int loc = ((PdfNumber) xrs).intValue();
            try {
                readXrefStream(loc);
                this.xrefStm = true;
                this.hybridXref = true;
            } catch (IOException e) {
                xref.clear();
                throw e;
            }
        }
        return trailer;
    }

    /* JADX WARN: Code restructure failed: missing block: B:96:0x0333, code lost:
    
        r23 = r23 + 2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean readXrefStream(long r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 833
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.pdf.PdfReader.readXrefStream(long):boolean");
    }

    protected void fixXref() throws IOException {
        int[] obj;
        this.fixedXref = true;
        PdfXrefTable xref = this.pdfDocument.getXref();
        this.tokens.seek(0L);
        ByteBuffer buffer = new ByteBuffer(24);
        PdfTokenizer lineTokeniser = new PdfTokenizer(new RandomAccessFileOrArray(new ReusableRandomAccessSource(buffer)));
        while (true) {
            long pos = this.tokens.getPosition();
            buffer.reset();
            if (this.tokens.readLineSegment(buffer, true)) {
                if (buffer.get(0) >= 48 && buffer.get(0) <= 57 && (obj = PdfTokenizer.checkObjectStart(lineTokeniser)) != null) {
                    int num = obj[0];
                    int gen = obj[1];
                    PdfIndirectReference reference = xref.get(num);
                    if (reference != null && reference.getGenNumber() == gen) {
                        reference.fixOffset(pos);
                    }
                }
            } else {
                return;
            }
        }
    }

    protected void rebuildXref() throws IOException {
        int[] obj;
        this.xrefStm = false;
        this.hybridXref = false;
        this.rebuiltXref = true;
        PdfXrefTable xref = this.pdfDocument.getXref();
        xref.clear();
        this.tokens.seek(0L);
        this.trailer = null;
        ByteBuffer buffer = new ByteBuffer(24);
        PdfTokenizer lineTokeniser = new PdfTokenizer(new RandomAccessFileOrArray(new ReusableRandomAccessSource(buffer)));
        while (true) {
            long pos = this.tokens.getPosition();
            buffer.reset();
            if (!this.tokens.readLineSegment(buffer, true)) {
                break;
            }
            if (buffer.get(0) == 116) {
                if (PdfTokenizer.checkTrailer(buffer)) {
                    this.tokens.seek(pos);
                    this.tokens.nextToken();
                    long pos2 = this.tokens.getPosition();
                    try {
                        PdfDictionary dic = (PdfDictionary) readObject(false);
                        if (dic.get(PdfName.Root, false) != null) {
                            this.trailer = dic;
                        } else {
                            this.tokens.seek(pos2);
                        }
                    } catch (Exception e) {
                        this.tokens.seek(pos2);
                    }
                }
            } else if (buffer.get(0) >= 48 && buffer.get(0) <= 57 && (obj = PdfTokenizer.checkObjectStart(lineTokeniser)) != null) {
                int num = obj[0];
                int gen = obj[1];
                if (xref.get(num) == null || xref.get(num).getGenNumber() <= gen) {
                    xref.add(new PdfIndirectReference(this.pdfDocument, num, gen, pos));
                }
            }
        }
        if (this.trailer == null) {
            throw new PdfException(PdfException.TrailerNotFound);
        }
    }

    boolean isMemorySavingMode() {
        return this.memorySavingMode;
    }

    private void readDecryptObj() {
        PdfDictionary enc;
        if (this.encrypted || (enc = this.trailer.getAsDictionary(PdfName.Encrypt)) == null) {
            return;
        }
        this.encrypted = true;
        PdfName filter = enc.getAsName(PdfName.Filter);
        if (PdfName.Adobe_PubSec.equals(filter)) {
            if (this.properties.certificate == null) {
                throw new PdfException(PdfException.CertificateIsNotProvidedDocumentIsEncryptedWithPublicKeyCertificate);
            }
            this.decrypt = new PdfEncryption(enc, this.properties.certificateKey, this.properties.certificate, this.properties.certificateKeyProvider, this.properties.externalDecryptionProcess);
        } else {
            if (PdfName.Standard.equals(filter)) {
                this.decrypt = new PdfEncryption(enc, this.properties.password, getOriginalFileId());
                return;
            }
            throw new UnsupportedSecurityHandlerException(MessageFormatUtil.format(UnsupportedSecurityHandlerException.UnsupportedSecurityHandler, filter));
        }
    }

    private static PdfTokenizer getOffsetTokeniser(IRandomAccessSource byteSource) throws IOException {
        PdfTokenizer tok = new PdfTokenizer(new RandomAccessFileOrArray(byteSource));
        int offset = tok.getHeaderOffset();
        if (offset != 0) {
            IRandomAccessSource offsetSource = new WindowRandomAccessSource(byteSource, offset);
            tok = new PdfTokenizer(new RandomAccessFileOrArray(offsetSource));
        }
        return tok;
    }

    private PdfObject readObject(PdfIndirectReference reference, boolean fixXref) {
        PdfObject object;
        if (reference == null) {
            return null;
        }
        if (reference.refersTo != null) {
            return reference.refersTo;
        }
        try {
            this.currentIndirectReference = reference;
            if (reference.getObjStreamNumber() > 0) {
                PdfStream objectStream = (PdfStream) this.pdfDocument.getXref().get(reference.getObjStreamNumber()).getRefersTo(false);
                readObjectStream(objectStream);
                return reference.refersTo;
            }
            if (reference.getOffset() > 0) {
                try {
                    this.tokens.seek(reference.getOffset());
                    this.tokens.nextValidToken();
                    if (this.tokens.getTokenType() != PdfTokenizer.TokenType.Obj || this.tokens.getObjNr() != reference.getObjNumber() || this.tokens.getGenNr() != reference.getGenNumber()) {
                        this.tokens.throwError(PdfException.InvalidOffsetForObject1, reference.toString());
                    }
                    object = readObject(false);
                } catch (RuntimeException ex) {
                    if (fixXref && reference.getObjStreamNumber() == 0) {
                        fixXref();
                        object = readObject(reference, false);
                    } else {
                        throw ex;
                    }
                }
                if (object != null) {
                    return object.setIndirectReference(reference);
                }
                return null;
            }
            return null;
        } catch (IOException e) {
            throw new PdfException(PdfException.CannotReadPdfObject, (Throwable) e);
        }
    }

    private void checkPdfStreamLength(PdfStream pdfStream) throws IOException {
        long pos;
        if (!correctStreamLength) {
            return;
        }
        long fileLength = this.tokens.length();
        long start = pdfStream.getOffset();
        boolean calc = false;
        int streamLength = 0;
        PdfNumber pdfNumber = pdfStream.getAsNumber(PdfName.Length);
        if (pdfNumber != null) {
            streamLength = pdfNumber.intValue();
            if (streamLength + start > fileLength - 20) {
                calc = true;
            } else {
                this.tokens.seek(start + streamLength);
                String line = this.tokens.readString(20);
                if (!line.startsWith(endstream2) && !line.startsWith(endstream3) && !line.startsWith(endstream4) && !line.startsWith(endstream1)) {
                    calc = true;
                }
            }
        } else {
            pdfNumber = new PdfNumber(0);
            pdfStream.put(PdfName.Length, pdfNumber);
            calc = true;
        }
        if (calc) {
            ByteBuffer line2 = new ByteBuffer(16);
            this.tokens.seek(start);
            while (true) {
                pos = this.tokens.getPosition();
                line2.reset();
                if (!this.tokens.readLineSegment(line2, false)) {
                    break;
                }
                if (line2.startsWith(endstream)) {
                    streamLength = (int) (pos - start);
                    break;
                } else if (line2.startsWith(endobj)) {
                    this.tokens.seek(pos - 16);
                    String s = this.tokens.readString(16);
                    int index = s.indexOf(endstream1);
                    if (index >= 0) {
                        pos = (pos - 16) + index;
                    }
                    streamLength = (int) (pos - start);
                }
            }
            this.tokens.seek(pos - 2);
            if (this.tokens.read() == 13) {
                streamLength--;
            }
            this.tokens.seek(pos - 1);
            if (this.tokens.read() == 10) {
                streamLength--;
            }
            pdfNumber.setValue(streamLength);
            pdfStream.updateLength(streamLength);
        }
    }

    private PdfObject createPdfNullInstance(boolean readAsDirect) {
        if (readAsDirect) {
            return PdfNull.PDF_NULL;
        }
        return new PdfNull();
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.sourcePath != null && this.tokens == null) {
            this.tokens = getOffsetTokeniser(new RandomAccessSourceFactory().setForceRead(false).createBestSource(this.sourcePath));
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.sourcePath != null) {
            PdfTokenizer tempTokens = this.tokens;
            this.tokens = null;
            out.defaultWriteObject();
            this.tokens = tempTokens;
            return;
        }
        out.defaultWriteObject();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfReader$ReusableRandomAccessSource.class */
    protected static class ReusableRandomAccessSource implements IRandomAccessSource {
        private ByteBuffer buffer;

        public ReusableRandomAccessSource(ByteBuffer buffer) {
            if (buffer == null) {
                throw new IllegalArgumentException("Passed byte buffer can not be null.");
            }
            this.buffer = buffer;
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public int get(long offset) {
            if (offset >= this.buffer.size()) {
                return -1;
            }
            return 255 & this.buffer.getInternalBuffer()[(int) offset];
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public int get(long offset, byte[] bytes, int off, int len) {
            if (this.buffer == null) {
                throw new IllegalStateException("Already closed");
            }
            if (offset >= this.buffer.size()) {
                return -1;
            }
            if (offset + len > this.buffer.size()) {
                len = (int) (this.buffer.size() - offset);
            }
            System.arraycopy(this.buffer.getInternalBuffer(), (int) offset, bytes, off, len);
            return len;
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public long length() {
            return this.buffer.size();
        }

        @Override // com.itextpdf.io.source.IRandomAccessSource
        public void close() throws IOException {
            this.buffer = null;
        }
    }
}
