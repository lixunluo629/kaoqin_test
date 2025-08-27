package org.apache.commons.io.output;

import java.io.PrintStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/NullPrintStream.class */
public class NullPrintStream extends PrintStream {
    public static final NullPrintStream INSTANCE = new NullPrintStream();

    @Deprecated
    public static final NullPrintStream NULL_PRINT_STREAM = INSTANCE;

    @Deprecated
    public NullPrintStream() {
        super(NullOutputStream.INSTANCE);
    }
}
