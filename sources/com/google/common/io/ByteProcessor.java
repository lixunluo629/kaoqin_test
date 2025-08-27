package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/io/ByteProcessor.class */
public interface ByteProcessor<T> {
    boolean processBytes(byte[] bArr, int i, int i2) throws IOException;

    T getResult();
}
