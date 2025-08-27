package org.apache.ibatis.cursor;

import java.io.Closeable;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cursor/Cursor.class */
public interface Cursor<T> extends Closeable, Iterable<T> {
    boolean isOpen();

    boolean isConsumed();

    int getCurrentIndex();
}
