package com.itextpdf.io.source;

import java.io.IOException;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/IRandomAccessSource.class */
public interface IRandomAccessSource {
    int get(long j) throws IOException;

    int get(long j, byte[] bArr, int i, int i2) throws IOException;

    long length();

    void close() throws IOException;
}
