package com.google.common.io;

import java.io.IOException;

@Deprecated
/* loaded from: guava-18.0.jar:com/google/common/io/InputSupplier.class */
public interface InputSupplier<T> {
    T getInput() throws IOException;
}
