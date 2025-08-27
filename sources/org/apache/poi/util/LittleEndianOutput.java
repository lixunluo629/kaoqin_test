package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/LittleEndianOutput.class */
public interface LittleEndianOutput {
    void writeByte(int i);

    void writeShort(int i);

    void writeInt(int i);

    void writeLong(long j);

    void writeDouble(double d);

    void write(byte[] bArr);

    void write(byte[] bArr, int i, int i2);
}
