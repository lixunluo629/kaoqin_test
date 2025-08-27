package com.itextpdf.kernel.pdf;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.ProductInfo;
import com.itextpdf.kernel.VersionInfo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfXrefTable.class */
class PdfXrefTable implements Serializable {
    private static final long serialVersionUID = 4171655392492002944L;
    private static final int INITIAL_CAPACITY = 32;
    private static final int MAX_GENERATION = 65535;
    private static final byte[] freeXRefEntry;
    private static final byte[] inUseXRefEntry;
    private PdfIndirectReference[] xref;
    private int count;
    private boolean readingCompleted;
    private final TreeMap<Integer, PdfIndirectReference> freeReferencesLinkedList;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PdfXrefTable.class.desiredAssertionStatus();
        freeXRefEntry = ByteUtils.getIsoBytes("f \n");
        inUseXRefEntry = ByteUtils.getIsoBytes("n \n");
    }

    public PdfXrefTable() {
        this(32);
    }

    public PdfXrefTable(int capacity) {
        this.count = 0;
        this.xref = new PdfIndirectReference[capacity < 1 ? 32 : capacity];
        this.freeReferencesLinkedList = new TreeMap<>();
        add((PdfIndirectReference) new PdfIndirectReference(null, 0, 65535, 0L).setState((short) 2));
    }

    public PdfIndirectReference add(PdfIndirectReference reference) {
        if (reference == null) {
            return null;
        }
        int objNr = reference.getObjNumber();
        this.count = Math.max(this.count, objNr);
        ensureCount(objNr);
        this.xref[objNr] = reference;
        return reference;
    }

    public int size() {
        return this.count + 1;
    }

    public PdfIndirectReference get(int index) {
        if (index > this.count) {
            return null;
        }
        return this.xref[index];
    }

    void markReadingCompleted() {
        this.readingCompleted = true;
    }

    boolean isReadingCompleted() {
        return this.readingCompleted;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void initFreeReferencesList(com.itextpdf.kernel.pdf.PdfDocument r9) {
        /*
            Method dump skipped, instructions count: 362
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.kernel.pdf.PdfXrefTable.initFreeReferencesList(com.itextpdf.kernel.pdf.PdfDocument):void");
    }

    PdfIndirectReference createNewIndirectReference(PdfDocument document) {
        int i = this.count + 1;
        this.count = i;
        PdfIndirectReference reference = new PdfIndirectReference(document, i);
        add(reference);
        return (PdfIndirectReference) reference.setState((short) 8);
    }

    protected PdfIndirectReference createNextIndirectReference(PdfDocument document) {
        int i = this.count + 1;
        this.count = i;
        PdfIndirectReference reference = new PdfIndirectReference(document, i);
        add(reference);
        return (PdfIndirectReference) reference.setState((short) 8);
    }

    protected void freeReference(PdfIndirectReference reference) {
        if (reference.isFree()) {
            return;
        }
        if (reference.checkState((short) 32)) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfXrefTable.class);
            logger.error(LogMessageConstant.INDIRECT_REFERENCE_USED_IN_FLUSHED_OBJECT_MADE_FREE);
        } else {
            if (reference.checkState((short) 1)) {
                Logger logger2 = LoggerFactory.getLogger((Class<?>) PdfXrefTable.class);
                logger2.error(LogMessageConstant.ALREADY_FLUSHED_INDIRECT_OBJECT_MADE_FREE);
                return;
            }
            reference.setState((short) 2).setState((short) 8);
            appendNewRefToFreeList(reference);
            if (reference.getGenNumber() < 65535) {
                reference.genNr++;
            }
        }
    }

    protected void setCapacity(int capacity) {
        if (capacity > this.xref.length) {
            extendXref(capacity);
        }
    }

    protected void writeXrefTableAndTrailer(PdfDocument document, PdfObject fileId, PdfObject crypto) throws IOException {
        PdfIndirectReference lastRef;
        PdfWriter writer = document.getWriter();
        if (!document.properties.appendMode) {
            for (int i = this.count; i > 0 && ((lastRef = this.xref[i]) == null || lastRef.isFree()); i--) {
                removeFreeRefFromList(i);
                this.count--;
            }
        }
        List<Integer> sections = createSections(document, false);
        if (document.properties.appendMode && sections.size() == 0) {
            this.xref = null;
            return;
        }
        long startxref = writer.getCurrentPos();
        long xRefStmPos = -1;
        if (writer.isFullCompression()) {
            PdfStream xrefStream = (PdfStream) new PdfStream().makeIndirect(document);
            xrefStream.makeIndirect(document);
            xrefStream.put(PdfName.Type, PdfName.XRef);
            xrefStream.put(PdfName.ID, fileId);
            if (crypto != null) {
                xrefStream.put(PdfName.Encrypt, crypto);
            }
            xrefStream.put(PdfName.Size, new PdfNumber(size()));
            int offsetSize = getOffsetSize(Math.max(startxref, size()));
            xrefStream.put(PdfName.W, new PdfArray((List<? extends PdfObject>) Arrays.asList(new PdfNumber(1), new PdfNumber(offsetSize), new PdfNumber(2))));
            xrefStream.put(PdfName.Info, document.getDocumentInfo().getPdfObject());
            xrefStream.put(PdfName.Root, document.getCatalog().getPdfObject());
            PdfArray index = new PdfArray();
            for (Integer section : sections) {
                index.add(new PdfNumber(section.intValue()));
            }
            if (document.properties.appendMode && !document.reader.hybridXref) {
                PdfNumber lastXref = new PdfNumber(document.reader.getLastXref());
                xrefStream.put(PdfName.Prev, lastXref);
            }
            xrefStream.put(PdfName.Index, index);
            PdfXrefTable xrefTable = document.getXref();
            for (int k = 0; k < sections.size(); k += 2) {
                int first = sections.get(k).intValue();
                int len = sections.get(k + 1).intValue();
                for (int i2 = first; i2 < first + len; i2++) {
                    PdfIndirectReference reference = xrefTable.get(i2);
                    if (reference.isFree()) {
                        xrefStream.getOutputStream().write(0);
                        xrefStream.getOutputStream().write(reference.getOffset(), offsetSize);
                        xrefStream.getOutputStream().write(reference.getGenNumber(), 2);
                    } else if (reference.getObjStreamNumber() == 0) {
                        xrefStream.getOutputStream().write(1);
                        xrefStream.getOutputStream().write(reference.getOffset(), offsetSize);
                        xrefStream.getOutputStream().write(reference.getGenNumber(), 2);
                    } else {
                        xrefStream.getOutputStream().write(2);
                        xrefStream.getOutputStream().write(reference.getObjStreamNumber(), offsetSize);
                        xrefStream.getOutputStream().write(reference.getIndex(), 2);
                    }
                }
            }
            xrefStream.flush();
            xRefStmPos = startxref;
        }
        boolean needsRegularXref = !writer.isFullCompression() || (document.properties.appendMode && document.reader.hybridXref);
        if (needsRegularXref) {
            startxref = writer.getCurrentPos();
            writer.writeString("xref\n");
            PdfXrefTable xrefTable2 = document.getXref();
            if (xRefStmPos != -1) {
                sections = createSections(document, true);
            }
            for (int k2 = 0; k2 < sections.size(); k2 += 2) {
                int first2 = sections.get(k2).intValue();
                int len2 = sections.get(k2 + 1).intValue();
                writer.writeInteger(first2).writeSpace().writeInteger(len2).writeByte((byte) 10);
                for (int i3 = first2; i3 < first2 + len2; i3++) {
                    PdfIndirectReference reference2 = xrefTable2.get(i3);
                    StringBuilder off = new StringBuilder("0000000000").append(reference2.getOffset());
                    StringBuilder gen = new StringBuilder("00000").append(reference2.getGenNumber());
                    writer.writeString(off.substring(off.length() - 10, off.length())).writeSpace().writeString(gen.substring(gen.length() - 5, gen.length())).writeSpace();
                    if (reference2.isFree()) {
                        writer.writeBytes(freeXRefEntry);
                    } else {
                        writer.writeBytes(inUseXRefEntry);
                    }
                }
            }
            PdfDictionary trailer = document.getTrailer();
            trailer.remove(PdfName.W);
            trailer.remove(PdfName.Index);
            trailer.remove(PdfName.Type);
            trailer.remove(PdfName.Length);
            trailer.put(PdfName.Size, new PdfNumber(size()));
            trailer.put(PdfName.ID, fileId);
            if (xRefStmPos != -1) {
                trailer.put(PdfName.XRefStm, new PdfNumber(xRefStmPos));
            }
            if (crypto != null) {
                trailer.put(PdfName.Encrypt, crypto);
            }
            writer.writeString("trailer\n");
            if (document.properties.appendMode) {
                PdfNumber lastXref2 = new PdfNumber(document.reader.getLastXref());
                trailer.put(PdfName.Prev, lastXref2);
            }
            writer.write((PdfObject) document.getTrailer());
            writer.write(10);
        }
        writeKeyInfo(document);
        writer.writeString("startxref\n").writeLong(startxref).writeString("\n%%EOF\n");
        this.xref = null;
        this.freeReferencesLinkedList.clear();
    }

    void clear() {
        for (int i = 1; i <= this.count; i++) {
            if (this.xref[i] == null || !this.xref[i].isFree()) {
                this.xref[i] = null;
            }
        }
        this.count = 1;
    }

    private List<Integer> createSections(PdfDocument document, boolean dropObjectsFromObjectStream) {
        List<Integer> sections = new ArrayList<>();
        int first = 0;
        int len = 0;
        for (int i = 0; i < size(); i++) {
            PdfIndirectReference reference = this.xref[i];
            if (document.properties.appendMode && reference != null && (!reference.checkState((short) 8) || (dropObjectsFromObjectStream && reference.getObjStreamNumber() != 0))) {
                reference = null;
            }
            if (reference == null) {
                if (len > 0) {
                    sections.add(Integer.valueOf(first));
                    sections.add(Integer.valueOf(len));
                }
                len = 0;
            } else if (len > 0) {
                len++;
            } else {
                first = i;
                len = 1;
            }
        }
        if (len > 0) {
            sections.add(Integer.valueOf(first));
            sections.add(Integer.valueOf(len));
        }
        return sections;
    }

    private int getOffsetSize(long startxref) {
        if (!$assertionsDisabled && (startxref < 0 || startxref >= FileUtils.ONE_TB)) {
            throw new AssertionError();
        }
        int size = 5;
        long mask = 1095216660480L;
        while (size > 1 && (mask & startxref) == 0) {
            mask >>= 8;
            size--;
        }
        return size;
    }

    protected static void writeKeyInfo(PdfDocument document) {
        PdfWriter writer = document.getWriter();
        FingerPrint fingerPrint = document.getFingerPrint();
        VersionInfo versionInfo = document.getVersionInfo();
        String k = versionInfo.getKey();
        if (k == null) {
            k = "iText";
        }
        writer.writeString(MessageFormatUtil.format("%{0}-{1}{2}\n", k, versionInfo.getRelease(), ""));
        for (ProductInfo productInfo : fingerPrint.getProducts()) {
            writer.writeString(MessageFormatUtil.format("%{0}\n", productInfo));
        }
    }

    private void appendNewRefToFreeList(PdfIndirectReference reference) {
        reference.setOffset(0L);
        if (this.freeReferencesLinkedList.isEmpty()) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } else {
            PdfIndirectReference lastFreeRef = this.freeReferencesLinkedList.get(0);
            ((PdfIndirectReference) lastFreeRef.setState((short) 8)).setOffset(reference.getObjNumber());
            this.freeReferencesLinkedList.put(Integer.valueOf(reference.getObjNumber()), lastFreeRef);
            this.freeReferencesLinkedList.put(0, reference);
        }
    }

    private PdfIndirectReference removeFreeRefFromList(int freeRefObjNr) {
        if (this.freeReferencesLinkedList.isEmpty()) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
        if (freeRefObjNr == 0) {
            return null;
        }
        if (freeRefObjNr < 0) {
            Integer leastFreeRefObjNum = null;
            Iterator<Map.Entry<Integer, PdfIndirectReference>> it = this.freeReferencesLinkedList.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Integer, PdfIndirectReference> entry = it.next();
                if (entry.getKey().intValue() > 0 && this.xref[entry.getKey().intValue()].getGenNumber() < 65535) {
                    leastFreeRefObjNum = entry.getKey();
                    break;
                }
            }
            if (leastFreeRefObjNum == null) {
                return null;
            }
            freeRefObjNr = leastFreeRefObjNum.intValue();
        }
        PdfIndirectReference freeRef = this.xref[freeRefObjNr];
        if (!freeRef.isFree()) {
            return null;
        }
        PdfIndirectReference prevFreeRef = this.freeReferencesLinkedList.remove(Integer.valueOf(freeRef.getObjNumber()));
        if (prevFreeRef != null) {
            this.freeReferencesLinkedList.put(Integer.valueOf((int) freeRef.getOffset()), prevFreeRef);
            ((PdfIndirectReference) prevFreeRef.setState((short) 8)).setOffset(freeRef.getOffset());
        }
        return freeRef;
    }

    private void ensureCount(int count) {
        if (count >= this.xref.length) {
            extendXref(count << 1);
        }
    }

    private void extendXref(int capacity) {
        PdfIndirectReference[] newXref = new PdfIndirectReference[capacity];
        System.arraycopy(this.xref, 0, newXref, 0, this.xref.length);
        this.xref = newXref;
    }
}
