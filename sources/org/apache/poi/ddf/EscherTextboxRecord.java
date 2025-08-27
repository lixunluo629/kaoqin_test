package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.RecordFormatException;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherTextboxRecord.class */
public final class EscherTextboxRecord extends EscherRecord implements Cloneable {
    public static final short RECORD_ID = -4083;
    public static final String RECORD_DESCRIPTION = "msofbtClientTextbox";
    private static final byte[] NO_BYTES = new byte[0];
    private byte[] thedata = NO_BYTES;

    @Override // org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        this.thedata = new byte[bytesRemaining];
        System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int remainingBytes = this.thedata.length;
        LittleEndian.putInt(data, offset + 4, remainingBytes);
        System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
        int pos = offset + 8 + this.thedata.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        int size = pos - offset;
        if (size != getRecordSize()) {
            throw new RecordFormatException(size + " bytes written but getRecordSize() reports " + getRecordSize());
        }
        return size;
    }

    public byte[] getData() {
        return this.thedata;
    }

    public void setData(byte[] b, int start, int length) {
        this.thedata = new byte[length];
        System.arraycopy(b, start, this.thedata, 0, length);
    }

    public void setData(byte[] b) {
        setData(b, 0, b.length);
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        return 8 + this.thedata.length;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    /* renamed from: clone */
    public EscherTextboxRecord mo3304clone() {
        EscherTextboxRecord etr = new EscherTextboxRecord();
        etr.setOptions(getOptions());
        etr.setRecordId(getRecordId());
        etr.thedata = (byte[]) this.thedata.clone();
        return etr;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        return "ClientTextbox";
    }

    /* JADX WARN: Type inference failed for: r0v14, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        int numCh = getChildRecords().size();
        List<Object> chLst = new ArrayList<>((numCh * 2) + 2);
        chLst.add("children");
        chLst.add(Integer.valueOf(numCh));
        for (EscherRecord er : getChildRecords()) {
            chLst.add(er.getRecordName());
            chLst.add(er);
        }
        return new Object[]{new Object[]{"isContainer", Boolean.valueOf(isContainerRecord())}, chLst.toArray(), new Object[]{"Extra Data", this.thedata}};
    }
}
