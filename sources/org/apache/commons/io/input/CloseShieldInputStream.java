package org.apache.commons.io.input;

import java.io.InputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/CloseShieldInputStream.class */
public class CloseShieldInputStream extends ProxyInputStream {
    public static InputStream systemIn(InputStream inputStream) {
        return inputStream == System.in ? wrap(inputStream) : inputStream;
    }

    public static CloseShieldInputStream wrap(InputStream inputStream) {
        return new CloseShieldInputStream(inputStream);
    }

    @Deprecated
    public CloseShieldInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.in = ClosedInputStream.INSTANCE;
    }
}
