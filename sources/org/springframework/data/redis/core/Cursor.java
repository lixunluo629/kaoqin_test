package org.springframework.data.redis.core;

import java.io.Closeable;
import java.util.Iterator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/Cursor.class */
public interface Cursor<T> extends Iterator<T>, Closeable {
    long getCursorId();

    boolean isClosed();

    Cursor<T> open();

    long getPosition();
}
