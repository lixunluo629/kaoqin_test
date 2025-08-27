package org.springframework.data.util;

import java.io.Closeable;
import java.util.Iterator;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/CloseableIterator.class */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {
    void close();
}
