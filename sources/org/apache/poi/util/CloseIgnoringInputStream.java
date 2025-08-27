package org.apache.poi.util;

import java.io.FilterInputStream;
import java.io.InputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/util/CloseIgnoringInputStream.class */
public class CloseIgnoringInputStream extends FilterInputStream {
    public CloseIgnoringInputStream(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
