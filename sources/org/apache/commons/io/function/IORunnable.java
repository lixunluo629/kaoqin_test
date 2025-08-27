package org.apache.commons.io.function;

import java.io.IOException;

@FunctionalInterface
/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/IORunnable.class */
public interface IORunnable {
    void run() throws IOException;

    static IORunnable noop() {
        return Constants.IO_RUNNABLE;
    }

    default Runnable asRunnable() {
        return () -> {
            Uncheck.run(this);
        };
    }
}
