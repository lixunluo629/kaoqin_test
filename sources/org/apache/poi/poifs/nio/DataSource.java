package org.apache.poi.poifs.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/nio/DataSource.class */
public abstract class DataSource {
    public abstract ByteBuffer read(int i, long j) throws IOException;

    public abstract void write(ByteBuffer byteBuffer, long j) throws IOException;

    public abstract long size() throws IOException;

    public abstract void close() throws IOException;

    public abstract void copyTo(OutputStream outputStream) throws IOException;
}
