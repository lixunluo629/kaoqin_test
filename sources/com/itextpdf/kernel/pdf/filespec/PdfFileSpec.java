package com.itextpdf.kernel.pdf.filespec;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.collection.PdfCollectionItem;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/filespec/PdfFileSpec.class */
public class PdfFileSpec extends PdfObjectWrapper<PdfObject> {
    private static final long serialVersionUID = 126861971006090239L;

    protected PdfFileSpec(PdfObject pdfObject) {
        super(pdfObject);
    }

    public static PdfFileSpec wrapFileSpecObject(PdfObject fileSpecObject) {
        if (fileSpecObject != null) {
            if (fileSpecObject.isString()) {
                return new PdfStringFS((PdfString) fileSpecObject);
            }
            if (fileSpecObject.isDictionary()) {
                return new PdfDictionaryFS((PdfDictionary) fileSpecObject);
            }
            return null;
        }
        return null;
    }

    public static PdfFileSpec createExternalFileSpec(PdfDocument doc, String filePath, PdfName afRelationshipValue) {
        PdfDictionary dict = new PdfDictionary();
        dict.put(PdfName.Type, PdfName.Filespec);
        dict.put(PdfName.F, new PdfString(filePath));
        dict.put(PdfName.UF, new PdfString(filePath, PdfEncodings.UNICODE_BIG));
        if (afRelationshipValue != null) {
            dict.put(PdfName.AFRelationship, afRelationshipValue);
        } else {
            dict.put(PdfName.AFRelationship, PdfName.Unspecified);
        }
        return (PdfFileSpec) new PdfFileSpec(dict).makeIndirect(doc);
    }

    public static PdfFileSpec createExternalFileSpec(PdfDocument doc, String filePath) {
        return createExternalFileSpec(doc, filePath, null);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, byte[] fileStore, String description, String fileDisplay, PdfName mimeType, PdfDictionary fileParameter, PdfName afRelationshipValue) {
        PdfStream stream = (PdfStream) new PdfStream(fileStore).makeIndirect(doc);
        PdfDictionary params = new PdfDictionary();
        if (fileParameter != null) {
            params.mergeDifferent(fileParameter);
        }
        if (!params.containsKey(PdfName.ModDate)) {
            params.put(PdfName.ModDate, new PdfDate().getPdfObject());
        }
        if (fileStore != null) {
            params.put(PdfName.Size, new PdfNumber(stream.getBytes().length));
        }
        stream.put(PdfName.Params, params);
        return createEmbeddedFileSpec(doc, stream, description, fileDisplay, mimeType, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, byte[] fileStore, String description, String fileDisplay, PdfDictionary fileParameter, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, fileStore, description, fileDisplay, (PdfName) null, fileParameter, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, byte[] fileStore, String fileDisplay, PdfDictionary fileParameter, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, fileStore, (String) null, fileDisplay, (PdfName) null, fileParameter, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, byte[] fileStore, String fileDisplay, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, fileStore, (String) null, fileDisplay, (PdfName) null, (PdfDictionary) null, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, byte[] fileStore, String description, String fileDisplay, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, fileStore, description, fileDisplay, (PdfName) null, (PdfDictionary) null, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, String filePath, String description, String fileDisplay, PdfName mimeType, PdfDictionary fileParameter, PdfName afRelationshipValue) throws IOException {
        PdfStream stream = new PdfStream(doc, UrlUtil.toURL(filePath).openStream());
        PdfDictionary params = new PdfDictionary();
        if (fileParameter != null) {
            params.mergeDifferent(fileParameter);
        }
        if (!params.containsKey(PdfName.ModDate)) {
            params.put(PdfName.ModDate, new PdfDate().getPdfObject());
        }
        stream.put(PdfName.Params, params);
        return createEmbeddedFileSpec(doc, stream, description, fileDisplay, mimeType, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, String filePath, String description, String fileDisplay, PdfName mimeType, PdfName afRelationshipValue) throws IOException {
        return createEmbeddedFileSpec(doc, filePath, description, fileDisplay, mimeType, (PdfDictionary) null, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, String filePath, String description, String fileDisplay, PdfName afRelationshipValue) throws IOException {
        return createEmbeddedFileSpec(doc, filePath, description, fileDisplay, (PdfName) null, (PdfDictionary) null, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, String filePath, String fileDisplay, PdfName afRelationshipValue) throws IOException {
        return createEmbeddedFileSpec(doc, filePath, (String) null, fileDisplay, (PdfName) null, (PdfDictionary) null, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, InputStream is, String description, String fileDisplay, PdfName mimeType, PdfDictionary fileParameter, PdfName afRelationshipValue) {
        PdfStream stream = new PdfStream(doc, is);
        PdfDictionary params = new PdfDictionary();
        if (fileParameter != null) {
            params.mergeDifferent(fileParameter);
        }
        if (!params.containsKey(PdfName.ModDate)) {
            params.put(PdfName.ModDate, new PdfDate().getPdfObject());
        }
        stream.put(PdfName.Params, params);
        return createEmbeddedFileSpec(doc, stream, description, fileDisplay, mimeType, afRelationshipValue);
    }

    public static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, InputStream is, String description, String fileDisplay, PdfName mimeType, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, is, description, fileDisplay, mimeType, (PdfDictionary) null, afRelationshipValue);
    }

    private static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, PdfStream stream, String description, String fileDisplay, PdfName mimeType, PdfName afRelationshipValue) {
        PdfDictionary dict = new PdfDictionary();
        stream.put(PdfName.Type, PdfName.EmbeddedFile);
        if (afRelationshipValue != null) {
            dict.put(PdfName.AFRelationship, afRelationshipValue);
        } else {
            dict.put(PdfName.AFRelationship, PdfName.Unspecified);
        }
        if (mimeType != null) {
            stream.put(PdfName.Subtype, mimeType);
        } else {
            stream.put(PdfName.Subtype, PdfName.ApplicationOctetStream);
        }
        if (description != null) {
            dict.put(PdfName.Desc, new PdfString(description));
        }
        dict.put(PdfName.Type, PdfName.Filespec);
        dict.put(PdfName.F, new PdfString(fileDisplay));
        dict.put(PdfName.UF, new PdfString(fileDisplay, PdfEncodings.UNICODE_BIG));
        PdfDictionary ef = new PdfDictionary();
        ef.put(PdfName.F, stream);
        ef.put(PdfName.UF, stream);
        dict.put(PdfName.EF, ef);
        return (PdfFileSpec) new PdfFileSpec(dict).makeIndirect(doc);
    }

    private static PdfFileSpec createEmbeddedFileSpec(PdfDocument doc, PdfStream stream, String description, String fileDisplay, PdfName afRelationshipValue) {
        return createEmbeddedFileSpec(doc, stream, description, fileDisplay, (PdfName) null, afRelationshipValue);
    }

    public PdfFileSpec setFileIdentifier(PdfArray fileIdentifier) {
        return put(PdfName.ID, fileIdentifier);
    }

    public PdfArray getFileIdentifier() {
        return ((PdfDictionary) getPdfObject()).getAsArray(PdfName.ID);
    }

    public PdfFileSpec setVolatile(PdfBoolean isVolatile) {
        return put(PdfName.Volatile, isVolatile);
    }

    public PdfBoolean isVolatile() {
        return ((PdfDictionary) getPdfObject()).getAsBoolean(PdfName.Volatile);
    }

    public PdfFileSpec setCollectionItem(PdfCollectionItem item) {
        return put(PdfName.CI, item.getPdfObject());
    }

    public PdfFileSpec setThumbnailImage(PdfImageXObject thumbnailImage) {
        return put(PdfName.Thumb, thumbnailImage.getPdfObject());
    }

    public PdfImageXObject getThumbnailImage() {
        PdfStream thumbnailStream = ((PdfDictionary) getPdfObject()).getAsStream(PdfName.Thumb);
        if (thumbnailStream != null) {
            return new PdfImageXObject(thumbnailStream);
        }
        return null;
    }

    public PdfFileSpec put(PdfName key, PdfObject value) {
        ((PdfDictionary) getPdfObject()).put(key, value);
        setModified();
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }
}
