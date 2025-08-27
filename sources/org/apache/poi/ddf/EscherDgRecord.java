package org.apache.poi.ddf;

import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherDgRecord.class */
public class EscherDgRecord extends EscherRecord {
    public static final short RECORD_ID = -4088;
    public static final String RECORD_DESCRIPTION = "MsofbtDg";
    private int field_1_numShapes;
    private int field_2_lastMSOSPID;

    @Override // org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_numShapes = LittleEndian.getInt(data, pos + 0);
        int size = 0 + 4;
        this.field_2_lastMSOSPID = LittleEndian.getInt(data, pos + size);
        int i = size + 4;
        return getRecordSize();
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, 8);
        LittleEndian.putInt(data, offset + 8, this.field_1_numShapes);
        LittleEndian.putInt(data, offset + 12, this.field_2_lastMSOSPID);
        listener.afterRecordSerialize(offset + 16, getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        return 16;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public short getRecordId() {
        return (short) -4088;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        return "Dg";
    }

    public int getNumShapes() {
        return this.field_1_numShapes;
    }

    public void setNumShapes(int field_1_numShapes) {
        this.field_1_numShapes = field_1_numShapes;
    }

    public int getLastMSOSPID() {
        return this.field_2_lastMSOSPID;
    }

    public void setLastMSOSPID(int field_2_lastMSOSPID) {
        this.field_2_lastMSOSPID = field_2_lastMSOSPID;
    }

    public short getDrawingGroupId() {
        return (short) (getOptions() >> 4);
    }

    public void incrementShapeCount() {
        this.field_1_numShapes++;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        return new Object[]{new Object[]{"NumShapes", Integer.valueOf(this.field_1_numShapes)}, new Object[]{"LastMSOSPID", Integer.valueOf(this.field_2_lastMSOSPID)}};
    }
}
