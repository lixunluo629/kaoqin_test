package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/NullOutputStream.class */
public class NullOutputStream extends OutputStream {
    public static final NullOutputStream INSTANCE = new NullOutputStream();

    @Deprecated
    public static final NullOutputStream NULL_OUTPUT_STREAM = INSTANCE;

    @Deprecated
    public NullOutputStream() {
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) {
    }

    @Override // java.io.OutputStream
    public void write(int b) {
    }
}
