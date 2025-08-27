package org.apache.poi.ddf;

import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherBitmapBlip.class */
public class EscherBitmapBlip extends EscherBlipRecord {
    public static final short RECORD_ID_JPEG = -4067;
    public static final short RECORD_ID_PNG = -4066;
    public static final short RECORD_ID_DIB = -4065;
    private static final int HEADER_SIZE = 8;
    private final byte[] field_1_UID = new byte[16];
    private byte field_2_marker = -1;

    @Override // org.apache.poi.ddf.EscherBlipRecord, org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        System.arraycopy(data, pos, this.field_1_UID, 0, 16);
        int pos2 = pos + 16;
        this.field_2_marker = data[pos2];
        setPictureData(data, pos2 + 1, bytesAfterHeader - 17);
        return bytesAfterHeader + 8;
    }

    @Override // org.apache.poi.ddf.EscherBlipRecord, org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, getRecordSize() - 8);
        int pos = offset + 8;
        System.arraycopy(this.field_1_UID, 0, data, pos, 16);
        data[pos + 16] = this.field_2_marker;
        byte[] pd = getPicturedata();
        System.arraycopy(pd, 0, data, pos + 17, pd.length);
        listener.afterRecordSerialize(offset + getRecordSize(), getRecordId(), getRecordSize(), this);
        return 25 + pd.length;
    }

    @Override // org.apache.poi.ddf.EscherBlipRecord, org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        return 25 + getPicturedata().length;
    }

    public byte[] getUID() {
        return this.field_1_UID;
    }

    public void setUID(byte[] field_1_UID) {
        if (field_1_UID == null || field_1_UID.length != 16) {
            throw new IllegalArgumentException("field_1_UID must be byte[16]");
        }
        System.arraycopy(field_1_UID, 0, this.field_1_UID, 0, 16);
    }

    public byte getMarker() {
        return this.field_2_marker;
    }

    public void setMarker(byte field_2_marker) {
        this.field_2_marker = field_2_marker;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherBlipRecord, org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        return new Object[]{new Object[]{"Marker", Byte.valueOf(this.field_2_marker)}, new Object[]{"Extra Data", getPicturedata()}};
    }
}
