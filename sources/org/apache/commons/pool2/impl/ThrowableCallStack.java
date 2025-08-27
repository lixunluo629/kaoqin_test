package org.apache.commons.pool2.impl;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/ThrowableCallStack.class */
public class ThrowableCallStack implements CallStack {
    private final String messageFormat;
    private final DateFormat dateFormat;
    private volatile Snapshot snapshot;

    public ThrowableCallStack(String messageFormat, boolean useTimestamp) {
        this.messageFormat = messageFormat;
        this.dateFormat = useTimestamp ? new SimpleDateFormat(messageFormat) : null;
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public synchronized boolean printStackTrace(PrintWriter writer) {
        String message;
        Snapshot snapshot = this.snapshot;
        if (snapshot == null) {
            return false;
        }
        if (this.dateFormat == null) {
            message = this.messageFormat;
        } else {
            synchronized (this.dateFormat) {
                message = this.dateFormat.format(Long.valueOf(snapshot.timestamp));
            }
        }
        writer.println(message);
        snapshot.printStackTrace(writer);
        return true;
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public void fillInStackTrace() {
        this.snapshot = new Snapshot();
    }

    @Override // org.apache.commons.pool2.impl.CallStack
    public void clear() {
        this.snapshot = null;
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/ThrowableCallStack$Snapshot.class */
    private static class Snapshot extends Throwable {
        private final long timestamp;

        private Snapshot() {
            this.timestamp = System.currentTimeMillis();
        }
    }
}
