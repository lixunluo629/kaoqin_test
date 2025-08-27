package org.apache.poi.ddf;

import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherClientDataRecord.class */
public class EscherClientDataRecord extends EscherRecord {
    public static final short RECORD_ID = -4079;
    public static final String RECORD_DESCRIPTION = "MsofbtClientData";
    private byte[] remainingData;

    @Override // org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos, this.remainingData, 0, bytesRemaining);
        return 8 + bytesRemaining;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this.remainingData == null) {
            this.remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, this.remainingData.length);
        System.arraycopy(this.remainingData, 0, data, offset + 8, this.remainingData.length);
        int pos = offset + 8 + this.remainingData.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        return 8 + (this.remainingData == null ? 0 : this.remainingData.length);
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public short getRecordId() {
        return (short) -4079;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        return "ClientData";
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public void setRemainingData(byte[] remainingData) {
        this.remainingData = remainingData == null ? new byte[0] : (byte[]) remainingData.clone();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        return new Object[]{new Object[]{"Extra Data", getRemainingData()}};
    }
}
