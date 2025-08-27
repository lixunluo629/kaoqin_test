package com.google.common.io;

import java.io.IOException;

@Deprecated
/* loaded from: guava-18.0.jar:com/google/common/io/OutputSupplier.class */
public interface OutputSupplier<T> {
    T getOutput() throws IOException;
}
