package org.apache.commons.io.output;

import java.io.OutputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/CloseShieldOutputStream.class */
public class CloseShieldOutputStream extends ProxyOutputStream {
    public static CloseShieldOutputStream wrap(OutputStream outputStream) {
        return new CloseShieldOutputStream(outputStream);
    }

    @Deprecated
    public CloseShieldOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out = ClosedOutputStream.INSTANCE;
    }
}
