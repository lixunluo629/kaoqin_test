package org.apache.poi.util;

/* loaded from: poi-3.17.jar:org/apache/poi/util/LittleEndianInput.class */
public interface LittleEndianInput {
    int available();

    byte readByte();

    int readUByte();

    short readShort();

    int readUShort();

    int readInt();

    long readLong();

    double readDouble();

    void readFully(byte[] bArr);

    void readFully(byte[] bArr, int i, int i2);

    void readPlain(byte[] bArr, int i, int i2);
}
