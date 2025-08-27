package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.source.OutputStream;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.filters.FlateDecodeFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfOutputStream.class */
public class PdfOutputStream extends OutputStream<PdfOutputStream> {
    private static final long serialVersionUID = -548180479472231600L;
    private static final byte[] stream;
    private static final byte[] endstream;
    private static final byte[] openDict;
    private static final byte[] closeDict;
    private static final byte[] endIndirect;
    private static final byte[] endIndirectWithZeroGenNr;
    private byte[] duplicateContentBuffer;
    protected PdfDocument document;
    protected PdfEncryption crypto;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfOutputStream.class.desiredAssertionStatus();
        stream = ByteUtils.getIsoBytes("stream\n");
        endstream = ByteUtils.getIsoBytes("\nendstream");
        openDict = ByteUtils.getIsoBytes("<<");
        closeDict = ByteUtils.getIsoBytes(">>");
        endIndirect = ByteUtils.getIsoBytes(" R");
        endIndirectWithZeroGenNr = ByteUtils.getIsoBytes(" 0 R");
    }

    public PdfOutputStream(java.io.OutputStream outputStream) {
        super(outputStream);
        this.duplicateContentBuffer = null;
        this.document = null;
    }

    public PdfOutputStream write(PdfObject pdfObject) throws IOException {
        if (pdfObject.checkState((short) 64) && this.document != null) {
            pdfObject.makeIndirect(this.document);
            pdfObject = pdfObject.getIndirectReference();
        }
        if (pdfObject.checkState((short) 256)) {
            throw new PdfException(PdfException.CannotWriteObjectAfterItWasReleased);
        }
        switch (pdfObject.getType()) {
            case 1:
                write((PdfArray) pdfObject);
                break;
            case 2:
            case 7:
                write((PdfPrimitiveObject) pdfObject);
                break;
            case 3:
                write((PdfDictionary) pdfObject);
                break;
            case 4:
                write((PdfLiteral) pdfObject);
                break;
            case 5:
                write((PdfIndirectReference) pdfObject);
                break;
            case 6:
                write((PdfName) pdfObject);
                break;
            case 8:
                write((PdfNumber) pdfObject);
                break;
            case 9:
                write((PdfStream) pdfObject);
                break;
            case 10:
                write((PdfString) pdfObject);
                break;
        }
        return this;
    }

    void write(long bytes, int size) throws IOException {
        if (!$assertionsDisabled && bytes < 0) {
            throw new AssertionError();
        }
        while (true) {
            size--;
            if (size >= 0) {
                write((byte) ((bytes >> (8 * size)) & 255));
            } else {
                return;
            }
        }
    }

    void write(int bytes, int size) throws IOException {
        write(bytes & 4294967295L, size);
    }

    private void write(PdfArray pdfArray) {
        writeByte(91);
        for (int i = 0; i < pdfArray.size(); i++) {
            PdfObject value = pdfArray.get(i, false);
            PdfIndirectReference indirectReference = value.getIndirectReference();
            if (indirectReference != null) {
                write(indirectReference);
            } else {
                write(value);
            }
            if (i < pdfArray.size() - 1) {
                writeSpace();
            }
        }
        writeByte(93);
    }

    private void write(PdfDictionary pdfDictionary) {
        writeBytes(openDict);
        for (PdfName key : pdfDictionary.keySet()) {
            boolean isAlreadyWriteSpace = false;
            write(key);
            PdfObject value = pdfDictionary.get(key, false);
            if (value == null) {
                Logger logger = LoggerFactory.getLogger((Class<?>) PdfOutputStream.class);
                logger.warn(MessageFormatUtil.format(LogMessageConstant.INVALID_KEY_VALUE_KEY_0_HAS_NULL_VALUE, key));
                value = PdfNull.PDF_NULL;
            }
            if (value.getType() == 8 || value.getType() == 4 || value.getType() == 2 || value.getType() == 7 || value.getType() == 5 || value.checkState((short) 64)) {
                isAlreadyWriteSpace = true;
                writeSpace();
            }
            PdfIndirectReference indirectReference = value.getIndirectReference();
            if (indirectReference != null) {
                if (!isAlreadyWriteSpace) {
                    writeSpace();
                }
                write(indirectReference);
            } else {
                write(value);
            }
        }
        writeBytes(closeDict);
    }

    private void write(PdfIndirectReference indirectReference) {
        if (this.document != null && !indirectReference.getDocument().equals(this.document)) {
            throw new PdfException(PdfException.PdfIndirectObjectBelongsToOtherPdfDocument);
        }
        if (indirectReference.isFree()) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfOutputStream.class);
            logger.error(LogMessageConstant.FLUSHED_OBJECT_CONTAINS_FREE_REFERENCE);
            write((PdfPrimitiveObject) PdfNull.PDF_NULL);
        } else if (indirectReference.refersTo == null && (indirectReference.checkState((short) 8) || indirectReference.getReader() == null || (indirectReference.getOffset() <= 0 && indirectReference.getIndex() < 0))) {
            Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfOutputStream.class);
            logger2.error(LogMessageConstant.FLUSHED_OBJECT_CONTAINS_REFERENCE_WHICH_NOT_REFER_TO_ANY_OBJECT);
            write((PdfPrimitiveObject) PdfNull.PDF_NULL);
        } else if (indirectReference.getGenNumber() == 0) {
            writeInteger(indirectReference.getObjNumber()).writeBytes(endIndirectWithZeroGenNr);
        } else {
            writeInteger(indirectReference.getObjNumber()).writeSpace().writeInteger(indirectReference.getGenNumber()).writeBytes(endIndirect);
        }
    }

    private void write(PdfPrimitiveObject pdfPrimitive) {
        writeBytes(pdfPrimitive.getInternalContent());
    }

    private void write(PdfLiteral literal) {
        literal.setPosition(getCurrentPos());
        writeBytes(literal.getInternalContent());
    }

    private void write(PdfString pdfString) {
        pdfString.encrypt(this.crypto);
        if (pdfString.isHexWriting()) {
            writeByte(60);
            writeBytes(pdfString.getInternalContent());
            writeByte(62);
        } else {
            writeByte(40);
            writeBytes(pdfString.getInternalContent());
            writeByte(41);
        }
    }

    private void write(PdfName name) {
        writeByte(47);
        writeBytes(name.getInternalContent());
    }

    private void write(PdfNumber pdfNumber) {
        if (pdfNumber.hasContent()) {
            writeBytes(pdfNumber.getInternalContent());
        } else if (pdfNumber.isDoubleNumber()) {
            writeDouble(pdfNumber.getValue());
        } else {
            writeInteger(pdfNumber.intValue());
        }
    }

    private boolean isNotMetadataPdfStream(PdfStream pdfStream) {
        return pdfStream.getAsName(PdfName.Type) == null || !(pdfStream.getAsName(PdfName.Type) == null || pdfStream.getAsName(PdfName.Type).equals(PdfName.Metadata));
    }

    private boolean isXRefStream(PdfStream pdfStream) {
        return PdfName.XRef.equals(pdfStream.getAsName(PdfName.Type));
    }

    /* JADX WARN: Removed duplicated region for block: B:82:0x020b A[Catch: IOException -> 0x0296, IOException -> 0x02e4, TryCatch #0 {IOException -> 0x0296, blocks: (B:65:0x017f, B:71:0x0190, B:73:0x01b4, B:81:0x0203, B:92:0x0267, B:94:0x026f, B:74:0x01dd, B:76:0x01e3, B:78:0x01ea, B:79:0x01f3, B:80:0x01f4, B:82:0x020b, B:84:0x0212, B:85:0x0244, B:87:0x024a, B:89:0x0251, B:90:0x025a, B:91:0x025b), top: B:105:0x017f, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void write(com.itextpdf.kernel.pdf.PdfStream r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 754
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.pdf.PdfOutputStream.write(com.itextpdf.kernel.pdf.PdfStream):void");
    }

    protected boolean checkEncryption(PdfStream pdfStream) {
        if (this.crypto == null || this.crypto.isEmbeddedFilesOnly() || isXRefStream(pdfStream)) {
            return false;
        }
        PdfObject filter = pdfStream.get(PdfName.Filter, true);
        if (filter != null) {
            if (PdfName.Crypt.equals(filter)) {
                return false;
            }
            if (filter.getType() == 1) {
                PdfArray filters = (PdfArray) filter;
                if (!filters.isEmpty() && PdfName.Crypt.equals(filters.get(0, true))) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    protected boolean containsFlateFilter(PdfStream pdfStream) {
        PdfObject filter = pdfStream.get(PdfName.Filter);
        if (filter != null) {
            if (filter.getType() == 6) {
                if (PdfName.FlateDecode.equals(filter)) {
                    return true;
                }
                return false;
            }
            if (filter.getType() == 1) {
                if (((PdfArray) filter).contains(PdfName.FlateDecode)) {
                    return true;
                }
                return false;
            }
            throw new PdfException(PdfException.FilterIsNotANameOrArray);
        }
        return false;
    }

    protected void updateCompressionFilter(PdfStream pdfStream) {
        PdfObject filter = pdfStream.get(PdfName.Filter);
        if (filter == null) {
            pdfStream.put(PdfName.Filter, PdfName.FlateDecode);
            return;
        }
        PdfArray filters = new PdfArray();
        filters.add(PdfName.FlateDecode);
        if (filter instanceof PdfArray) {
            filters.addAll((PdfArray) filter);
        } else {
            filters.add(filter);
        }
        PdfObject decodeParms = pdfStream.get(PdfName.DecodeParms);
        if (decodeParms != null) {
            if (decodeParms instanceof PdfDictionary) {
                PdfArray array = new PdfArray();
                array.add(new PdfNull());
                array.add(decodeParms);
                pdfStream.put(PdfName.DecodeParms, array);
            } else {
                if (!(decodeParms instanceof PdfArray)) {
                    throw new PdfException(PdfException.DecodeParameterType1IsNotSupported).setMessageParams(decodeParms.getClass().toString());
                }
                ((PdfArray) decodeParms).add(0, new PdfNull());
            }
        }
        pdfStream.put(PdfName.Filter, filters);
    }

    protected byte[] decodeFlateBytes(PdfStream stream2, byte[] bytes) {
        PdfName filterName;
        PdfDictionary decodeParams;
        PdfObject filterObject = stream2.get(PdfName.Filter);
        if (filterObject == null) {
            return bytes;
        }
        PdfArray filtersArray = null;
        if (filterObject instanceof PdfName) {
            filterName = (PdfName) filterObject;
        } else if (filterObject instanceof PdfArray) {
            filtersArray = (PdfArray) filterObject;
            filterName = filtersArray.getAsName(0);
        } else {
            throw new PdfException(PdfException.FilterIsNotANameOrArray);
        }
        if (!PdfName.FlateDecode.equals(filterName)) {
            return bytes;
        }
        PdfArray decodeParamsArray = null;
        PdfObject decodeParamsObject = stream2.get(PdfName.DecodeParms);
        if (decodeParamsObject == null) {
            decodeParams = null;
        } else if (decodeParamsObject.getType() == 3) {
            decodeParams = (PdfDictionary) decodeParamsObject;
        } else {
            if (decodeParamsObject.getType() != 1) {
                throw new PdfException(PdfException.DecodeParameterType1IsNotSupported).setMessageParams(decodeParamsObject.getClass().toString());
            }
            decodeParamsArray = (PdfArray) decodeParamsObject;
            decodeParams = decodeParamsArray.getAsDictionary(0);
        }
        byte[] res = FlateDecodeFilter.flateDecode(bytes, true);
        if (res == null) {
            res = FlateDecodeFilter.flateDecode(bytes, false);
        }
        byte[] bytes2 = FlateDecodeFilter.decodePredictor(res, decodeParams);
        PdfObject filterObject2 = null;
        if (filtersArray != null) {
            filtersArray.remove(0);
            if (filtersArray.size() == 1) {
                filterObject2 = filtersArray.get(0);
            } else if (!filtersArray.isEmpty()) {
                filterObject2 = filtersArray;
            }
        }
        PdfObject decodeParamsObject2 = null;
        if (decodeParamsArray != null) {
            decodeParamsArray.remove(0);
            if (decodeParamsArray.size() == 1 && decodeParamsArray.get(0).getType() != 7) {
                decodeParamsObject2 = decodeParamsArray.get(0);
            } else if (!decodeParamsArray.isEmpty()) {
                decodeParamsObject2 = decodeParamsArray;
            }
        }
        if (filterObject2 == null) {
            stream2.remove(PdfName.Filter);
        } else {
            stream2.put(PdfName.Filter, filterObject2);
        }
        if (decodeParamsObject2 == null) {
            stream2.remove(PdfName.DecodeParms);
        } else {
            stream2.put(PdfName.DecodeParms, decodeParamsObject2);
        }
        return bytes2;
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.outputStream == null && this.duplicateContentBuffer != null) {
            this.outputStream = new ByteArrayOutputStream();
            write(this.duplicateContentBuffer);
            this.duplicateContentBuffer = null;
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        java.io.OutputStream tempOutputStream = this.outputStream;
        if (this.outputStream instanceof java.io.ByteArrayOutputStream) {
            this.duplicateContentBuffer = ((java.io.ByteArrayOutputStream) this.outputStream).toByteArray();
        }
        this.outputStream = null;
        out.defaultWriteObject();
        this.outputStream = tempOutputStream;
    }
}
