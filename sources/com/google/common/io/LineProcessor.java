package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/io/LineProcessor.class */
public interface LineProcessor<T> {
    boolean processLine(String str) throws IOException;

    T getResult();
}
