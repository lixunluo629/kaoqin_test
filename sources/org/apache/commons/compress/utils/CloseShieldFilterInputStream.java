package org.apache.commons.compress.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/CloseShieldFilterInputStream.class */
public class CloseShieldFilterInputStream extends FilterInputStream {
    public CloseShieldFilterInputStream(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
