package org.apache.poi.ddf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherContainerRecord.class */
public final class EscherContainerRecord extends EscherRecord implements Iterable<EscherRecord> {
    public static final short DGG_CONTAINER = -4096;
    public static final short BSTORE_CONTAINER = -4095;
    public static final short DG_CONTAINER = -4094;
    public static final short SPGR_CONTAINER = -4093;
    public static final short SP_CONTAINER = -4092;
    public static final short SOLVER_CONTAINER = -4091;
    private static final POILogger log = POILogFactory.getLogger((Class<?>) EscherContainerRecord.class);
    private int _remainingLength;
    private final List<EscherRecord> _childRecords = new ArrayList();

    @Override // org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int pOffset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, pOffset);
        int bytesWritten = 8;
        int offset = pOffset + 8;
        while (bytesRemaining > 0 && offset < data.length) {
            EscherRecord child = recordFactory.createRecord(data, offset);
            int childBytesWritten = child.fillFields(data, offset, recordFactory);
            bytesWritten += childBytesWritten;
            offset += childBytesWritten;
            bytesRemaining -= childBytesWritten;
            addChildRecord(child);
            if (offset >= data.length && bytesRemaining > 0) {
                this._remainingLength = bytesRemaining;
                if (log.check(5)) {
                    log.log(5, "Not enough Escher data: " + bytesRemaining + " bytes remaining but no space left");
                }
            }
        }
        return bytesWritten;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord r = i$.next();
            remainingBytes += r.getRecordSize();
        }
        LittleEndian.putInt(data, offset + 4, remainingBytes + this._remainingLength);
        int pos = offset + 8;
        Iterator i$2 = iterator();
        while (i$2.hasNext()) {
            EscherRecord r2 = i$2.next();
            pos += r2.serialize(pos, data, listener);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        int childRecordsSize = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord r = i$.next();
            childRecordsSize += r.getRecordSize();
        }
        return 8 + childRecordsSize;
    }

    public boolean hasChildOfType(short recordId) {
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord r = i$.next();
            if (r.getRecordId() == recordId) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public EscherRecord getChild(int index) {
        return this._childRecords.get(index);
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public List<EscherRecord> getChildRecords() {
        return new ArrayList(this._childRecords);
    }

    @Removal(version = "3.18")
    @Deprecated
    public Iterator<EscherRecord> getChildIterator() {
        return iterator();
    }

    @Override // java.lang.Iterable
    public Iterator<EscherRecord> iterator() {
        return Collections.unmodifiableList(this._childRecords).iterator();
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public void setChildRecords(List<EscherRecord> childRecords) {
        if (childRecords == this._childRecords) {
            throw new IllegalStateException("Child records private data member has escaped");
        }
        this._childRecords.clear();
        this._childRecords.addAll(childRecords);
    }

    public boolean removeChildRecord(EscherRecord toBeRemoved) {
        return this._childRecords.remove(toBeRemoved);
    }

    public List<EscherContainerRecord> getChildContainers() {
        List<EscherContainerRecord> containers = new ArrayList<>();
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord r = i$.next();
            if (r instanceof EscherContainerRecord) {
                containers.add((EscherContainerRecord) r);
            }
        }
        return containers;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        switch (getRecordId()) {
            case DGG_CONTAINER /* -4096 */:
                return "DggContainer";
            case BSTORE_CONTAINER /* -4095 */:
                return "BStoreContainer";
            case DG_CONTAINER /* -4094 */:
                return "DgContainer";
            case SPGR_CONTAINER /* -4093 */:
                return "SpgrContainer";
            case SP_CONTAINER /* -4092 */:
                return "SpContainer";
            case SOLVER_CONTAINER /* -4091 */:
                return "SolverContainer";
            default:
                return "Container 0x" + HexDump.toHex(getRecordId());
        }
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public void display(PrintWriter w, int indent) {
        super.display(w, indent);
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord escherRecord = i$.next();
            escherRecord.display(w, indent + 1);
        }
    }

    public void addChildRecord(EscherRecord record) {
        this._childRecords.add(record);
    }

    public void addChildBefore(EscherRecord record, int insertBeforeRecordId) {
        int idx = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord rec = i$.next();
            if (rec.getRecordId() == ((short) insertBeforeRecordId)) {
                break;
            } else {
                idx++;
            }
        }
        this._childRecords.add(idx, record);
    }

    public <T extends EscherRecord> T getChildById(short recordId) {
        Iterator<EscherRecord> i$ = iterator();
        while (i$.hasNext()) {
            T t = (T) i$.next();
            if (t.getRecordId() == recordId) {
                return t;
            }
        }
        return null;
    }

    public void getRecordsById(short recordId, List<EscherRecord> out) {
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord r = i$.next();
            if (r instanceof EscherContainerRecord) {
                EscherContainerRecord c = (EscherContainerRecord) r;
                c.getRecordsById(recordId, out);
            } else if (r.getRecordId() == recordId) {
                out.add(r);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        List<Object> chList = new ArrayList<>((this._childRecords.size() * 2) + 2);
        chList.add("children");
        chList.add(Integer.valueOf(this._childRecords.size()));
        int count = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            EscherRecord record = (EscherRecord) i$.next();
            chList.add("Child " + count);
            chList.add(record);
            count++;
        }
        return new Object[]{new Object[]{"isContainer", Boolean.valueOf(isContainerRecord())}, chList.toArray()};
    }
}
