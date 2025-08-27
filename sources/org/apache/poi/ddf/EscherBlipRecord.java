package org.apache.poi.ddf;

import org.apache.poi.util.LittleEndian;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherBlipRecord.class */
public class EscherBlipRecord extends EscherRecord {
    public static final short RECORD_ID_START = -4072;
    public static final short RECORD_ID_END = -3817;
    public static final String RECORD_DESCRIPTION = "msofbtBlip";
    private static final int HEADER_SIZE = 8;
    private byte[] field_pictureData;

    @Override // org.apache.poi.ddf.EscherRecord
    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesAfterHeader = readHeader(data, offset);
        int pos = offset + 8;
        this.field_pictureData = new byte[bytesAfterHeader];
        System.arraycopy(data, pos, this.field_pictureData, 0, bytesAfterHeader);
        return bytesAfterHeader + 8;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        System.arraycopy(this.field_pictureData, 0, data, offset + 4, this.field_pictureData.length);
        listener.afterRecordSerialize(offset + 4 + this.field_pictureData.length, getRecordId(), this.field_pictureData.length + 4, this);
        return this.field_pictureData.length + 4;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public int getRecordSize() {
        return this.field_pictureData.length + 8;
    }

    @Override // org.apache.poi.ddf.EscherRecord
    public String getRecordName() {
        return "Blip";
    }

    public byte[] getPicturedata() {
        return this.field_pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        setPictureData(pictureData, 0, pictureData == null ? 0 : pictureData.length);
    }

    public void setPictureData(byte[] pictureData, int offset, int length) {
        if (pictureData == null || offset < 0 || length < 0 || pictureData.length < offset + length) {
            throw new IllegalArgumentException("picture data can't be null");
        }
        this.field_pictureData = new byte[length];
        System.arraycopy(pictureData, offset, this.field_pictureData, 0, length);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // org.apache.poi.ddf.EscherRecord
    protected Object[][] getAttributeMap() {
        return new Object[]{new Object[]{"Extra Data", getPicturedata()}};
    }
}
