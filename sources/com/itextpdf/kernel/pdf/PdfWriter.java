package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.util.FileUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfWriter.class */
public class PdfWriter extends PdfOutputStream implements Serializable {
    private static final long serialVersionUID = -6875544505477707103L;
    private static final byte[] obj = ByteUtils.getIsoBytes(" obj\n");
    private static final byte[] endobj = ByteUtils.getIsoBytes("\nendobj\n");
    private PdfOutputStream duplicateStream;
    protected WriterProperties properties;
    PdfObjectStream objectStream;
    private Map<PdfDocument.IndirectRefDescription, PdfIndirectReference> copiedObjects;
    private SmartModePdfObjectsSerializer smartModeSerializer;
    protected boolean isUserWarnedAboutAcroFormCopying;

    public PdfWriter(File file) throws FileNotFoundException {
        this(file.getAbsolutePath());
    }

    public PdfWriter(OutputStream os) {
        this(os, new WriterProperties());
    }

    public PdfWriter(OutputStream os, WriterProperties properties) {
        super(FileUtil.wrapWithBufferedOutputStream(os));
        this.duplicateStream = null;
        this.objectStream = null;
        this.copiedObjects = new LinkedHashMap();
        this.smartModeSerializer = new SmartModePdfObjectsSerializer();
        this.properties = properties;
        if (properties.debugMode) {
            setDebugMode();
        }
    }

    public PdfWriter(String filename) throws FileNotFoundException {
        this(filename, new WriterProperties());
    }

    public PdfWriter(String filename, WriterProperties properties) throws FileNotFoundException {
        this(FileUtil.getBufferedOutputStream(filename), properties);
    }

    public boolean isFullCompression() {
        if (this.properties.isFullCompression != null) {
            return this.properties.isFullCompression.booleanValue();
        }
        return false;
    }

    public int getCompressionLevel() {
        return this.properties.compressionLevel;
    }

    public PdfWriter setCompressionLevel(int compressionLevel) {
        this.properties.setCompressionLevel(compressionLevel);
        return this;
    }

    public PdfWriter setSmartMode(boolean smartMode) {
        this.properties.smartMode = smartMode;
        return this;
    }

    @Override // com.itextpdf.io.source.OutputStream, java.io.OutputStream
    public void write(int b) throws IOException {
        super.write(b);
        if (this.duplicateStream != null) {
            this.duplicateStream.write(b);
        }
    }

    @Override // com.itextpdf.io.source.OutputStream, java.io.OutputStream
    public void write(byte[] b) throws IOException {
        super.write(b);
        if (this.duplicateStream != null) {
            this.duplicateStream.write(b);
        }
    }

    @Override // com.itextpdf.io.source.OutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        if (this.duplicateStream != null) {
            this.duplicateStream.write(b, off, len);
        }
    }

    @Override // com.itextpdf.io.source.OutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            try {
                if (this.duplicateStream != null) {
                    this.duplicateStream.close();
                }
            } catch (Exception ex) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfWriter.class);
                logger.error("Closing of the duplicatedStream failed.", (Throwable) ex);
            }
        }
    }

    PdfObjectStream getObjectStream() {
        if (!isFullCompression()) {
            return null;
        }
        if (this.objectStream == null) {
            this.objectStream = new PdfObjectStream(this.document);
        } else if (this.objectStream.getSize() == 200) {
            this.objectStream.flush();
            this.objectStream = new PdfObjectStream(this.objectStream);
        }
        return this.objectStream;
    }

    protected void initCryptoIfSpecified(PdfVersion version) {
        EncryptionProperties encryptProps = this.properties.encryptionProperties;
        if (this.properties.isStandardEncryptionUsed()) {
            this.crypto = new PdfEncryption(encryptProps.userPassword, encryptProps.ownerPassword, encryptProps.standardEncryptPermissions, encryptProps.encryptionAlgorithm, ByteUtils.getIsoBytes(this.document.getOriginalDocumentId().getValue()), version);
        } else if (this.properties.isPublicKeyEncryptionUsed()) {
            this.crypto = new PdfEncryption(encryptProps.publicCertificates, encryptProps.publicKeyEncryptPermissions, encryptProps.encryptionAlgorithm, version);
        }
    }

    protected void flushObject(PdfObject pdfObject, boolean canBeInObjStm) throws IOException {
        PdfIndirectReference indirectReference = pdfObject.getIndirectReference();
        if (isFullCompression() && canBeInObjStm) {
            PdfObjectStream objectStream = getObjectStream();
            objectStream.addObject(pdfObject);
        } else {
            indirectReference.setOffset(getCurrentPos());
            writeToBody(pdfObject);
        }
        indirectReference.setState((short) 1).clearState((short) 32);
        switch (pdfObject.getType()) {
            case 1:
                PdfArray array = (PdfArray) pdfObject;
                markArrayContentToFlush(array);
                array.releaseContent();
                break;
            case 2:
            case 6:
            case 7:
            case 8:
            case 10:
                ((PdfPrimitiveObject) pdfObject).content = null;
                break;
            case 3:
            case 9:
                PdfDictionary dictionary = (PdfDictionary) pdfObject;
                markDictionaryContentToFlush(dictionary);
                dictionary.releaseContent();
                break;
            case 5:
                markObjectToFlush(((PdfIndirectReference) pdfObject).getRefersTo(false));
                break;
        }
    }

    protected PdfObject copyObject(PdfObject obj2, PdfDocument documentTo, boolean allowDuplicating) {
        if (obj2 instanceof PdfIndirectReference) {
            obj2 = ((PdfIndirectReference) obj2).getRefersTo();
        }
        if (obj2 == null) {
            obj2 = PdfNull.PDF_NULL;
        }
        if (checkTypeOfPdfDictionary(obj2, PdfName.Catalog)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfReader.class);
            logger.warn(LogMessageConstant.MAKE_COPY_OF_CATALOG_DICTIONARY_IS_FORBIDDEN);
            obj2 = PdfNull.PDF_NULL;
        }
        PdfIndirectReference indirectReference = obj2.getIndirectReference();
        PdfDocument.IndirectRefDescription copiedObjectKey = null;
        boolean tryToFindDuplicate = (allowDuplicating || indirectReference == null) ? false : true;
        if (tryToFindDuplicate) {
            copiedObjectKey = new PdfDocument.IndirectRefDescription(indirectReference);
            PdfIndirectReference copiedIndirectReference = this.copiedObjects.get(copiedObjectKey);
            if (copiedIndirectReference != null) {
                return copiedIndirectReference.getRefersTo();
            }
        }
        SerializedObjectContent serializedContent = null;
        if (this.properties.smartMode && tryToFindDuplicate && !checkTypeOfPdfDictionary(obj2, PdfName.Page)) {
            serializedContent = this.smartModeSerializer.serializeObject(obj2);
            PdfIndirectReference objectRef = this.smartModeSerializer.getSavedSerializedObject(serializedContent);
            if (objectRef != null) {
                this.copiedObjects.put(copiedObjectKey, objectRef);
                return objectRef.refersTo;
            }
        }
        PdfObject newObject = obj2.newInstance();
        if (indirectReference != null) {
            if (copiedObjectKey == null) {
                copiedObjectKey = new PdfDocument.IndirectRefDescription(indirectReference);
            }
            PdfIndirectReference indRef = newObject.makeIndirect(documentTo).getIndirectReference();
            if (serializedContent != null) {
                this.smartModeSerializer.saveSerializedObject(serializedContent, indRef);
            }
            this.copiedObjects.put(copiedObjectKey, indRef);
        }
        newObject.copyContent(obj2, documentTo);
        return newObject;
    }

    protected void writeToBody(PdfObject pdfObj) throws IOException {
        if (this.crypto != null) {
            this.crypto.setHashKeyForNextObject(pdfObj.getIndirectReference().getObjNumber(), pdfObj.getIndirectReference().getGenNumber());
        }
        writeInteger(pdfObj.getIndirectReference().getObjNumber()).writeSpace().writeInteger(pdfObj.getIndirectReference().getGenNumber()).writeBytes(obj);
        write(pdfObj);
        writeBytes(endobj);
    }

    protected void writeHeader() {
        writeByte(37).writeString(this.document.getPdfVersion().toString()).writeString("\n%âãÏÓ\n");
    }

    protected void flushWaitingObjects(Set<PdfIndirectReference> forbiddenToFlush) {
        PdfObject obj2;
        PdfXrefTable xref = this.document.getXref();
        boolean needFlush = true;
        while (needFlush) {
            needFlush = false;
            for (int i = 1; i < xref.size(); i++) {
                PdfIndirectReference indirectReference = xref.get(i);
                if (indirectReference != null && !indirectReference.isFree() && indirectReference.checkState((short) 32) && !forbiddenToFlush.contains(indirectReference) && (obj2 = indirectReference.getRefersTo(false)) != null) {
                    obj2.flush();
                    needFlush = true;
                }
            }
        }
        if (this.objectStream != null && this.objectStream.getSize() > 0) {
            this.objectStream.flush();
            this.objectStream = null;
        }
    }

    protected void flushModifiedWaitingObjects(Set<PdfIndirectReference> forbiddenToFlush) {
        PdfObject obj2;
        PdfXrefTable xref = this.document.getXref();
        for (int i = 1; i < xref.size(); i++) {
            PdfIndirectReference indirectReference = xref.get(i);
            if (null != indirectReference && !indirectReference.isFree() && !forbiddenToFlush.contains(indirectReference)) {
                boolean isModified = indirectReference.checkState((short) 8);
                if (isModified && (obj2 = indirectReference.getRefersTo(false)) != null && !obj2.equals(this.objectStream)) {
                    obj2.flush();
                }
            }
        }
        if (this.objectStream != null && this.objectStream.getSize() > 0) {
            this.objectStream.flush();
            this.objectStream = null;
        }
    }

    void flushCopiedObjects(long docId) {
        List<PdfDocument.IndirectRefDescription> remove = new ArrayList<>();
        for (Map.Entry<PdfDocument.IndirectRefDescription, PdfIndirectReference> copiedObject : this.copiedObjects.entrySet()) {
            if (copiedObject.getKey().docId == docId && copiedObject.getValue().refersTo != null) {
                copiedObject.getValue().refersTo.flush();
                remove.add(copiedObject.getKey());
            }
        }
        for (PdfDocument.IndirectRefDescription ird : remove) {
            this.copiedObjects.remove(ird);
        }
    }

    private void markArrayContentToFlush(PdfArray array) {
        for (int i = 0; i < array.size(); i++) {
            markObjectToFlush(array.get(i, false));
        }
    }

    private void markDictionaryContentToFlush(PdfDictionary dictionary) {
        for (PdfObject item : dictionary.values(false)) {
            markObjectToFlush(item);
        }
    }

    private void markObjectToFlush(PdfObject pdfObject) {
        if (pdfObject != null) {
            PdfIndirectReference indirectReference = pdfObject.getIndirectReference();
            if (indirectReference != null) {
                if (!indirectReference.checkState((short) 1)) {
                    indirectReference.setState((short) 32);
                }
            } else if (pdfObject.getType() == 5) {
                if (!pdfObject.checkState((short) 1)) {
                    pdfObject.setState((short) 32);
                }
            } else if (pdfObject.getType() == 1) {
                markArrayContentToFlush((PdfArray) pdfObject);
            } else if (pdfObject.getType() == 3) {
                markDictionaryContentToFlush((PdfDictionary) pdfObject);
            }
        }
    }

    private PdfWriter setDebugMode() {
        this.duplicateStream = new PdfOutputStream(new ByteArrayOutputStream());
        return this;
    }

    private byte[] getDebugBytes() throws IOException {
        if (this.duplicateStream != null) {
            this.duplicateStream.flush();
            return ((ByteArrayOutputStream) this.duplicateStream.getOutputStream()).toByteArray();
        }
        return null;
    }

    private static boolean checkTypeOfPdfDictionary(PdfObject dictionary, PdfName expectedType) {
        return dictionary.isDictionary() && expectedType.equals(((PdfDictionary) dictionary).getAsName(PdfName.Type));
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.outputStream == null) {
            this.outputStream = new ByteArrayOutputStream().assignBytes(getDebugBytes());
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (this.duplicateStream == null) {
            throw new NotSerializableException(getClass().getName() + ": debug mode is disabled!");
        }
        OutputStream tempOutputStream = this.outputStream;
        this.outputStream = null;
        out.defaultWriteObject();
        this.outputStream = tempOutputStream;
    }
}
