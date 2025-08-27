package com.google.common.io;

import java.io.DataOutput;

/* loaded from: guava-18.0.jar:com/google/common/io/ByteArrayDataOutput.class */
public interface ByteArrayDataOutput extends DataOutput {
    @Override // java.io.DataOutput
    void write(int i);

    @Override // java.io.DataOutput
    void write(byte[] bArr);

    @Override // java.io.DataOutput
    void write(byte[] bArr, int i, int i2);

    @Override // java.io.DataOutput
    void writeBoolean(boolean z);

    @Override // java.io.DataOutput
    void writeByte(int i);

    @Override // java.io.DataOutput
    void writeShort(int i);

    @Override // java.io.DataOutput
    void writeChar(int i);

    @Override // java.io.DataOutput
    void writeInt(int i);

    @Override // java.io.DataOutput
    void writeLong(long j);

    @Override // java.io.DataOutput
    void writeFloat(float f);

    @Override // java.io.DataOutput
    void writeDouble(double d);

    @Override // java.io.DataOutput
    void writeChars(String str);

    @Override // java.io.DataOutput
    void writeUTF(String str);

    @Override // java.io.DataOutput
    @Deprecated
    void writeBytes(String str);

    byte[] toByteArray();
}
