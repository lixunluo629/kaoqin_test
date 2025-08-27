package org.apache.commons.pool2.impl;

import java.io.PrintWriter;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/CallStack.class */
public interface CallStack {
    boolean printStackTrace(PrintWriter printWriter);

    void fillInStackTrace();

    void clear();
}
