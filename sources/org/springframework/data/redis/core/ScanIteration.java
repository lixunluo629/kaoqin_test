package org.springframework.data.redis.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ScanIteration.class */
public class ScanIteration<T> implements Iterable<T> {
    private final long cursorId;
    private final Collection<T> items;

    public ScanIteration(long cursorId, Collection<T> items) {
        this.cursorId = cursorId;
        this.items = items != null ? new ArrayList<>(items) : Collections.emptyList();
    }

    public long getCursorId() {
        return this.cursorId;
    }

    public Collection<T> getItems() {
        return this.items;
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.items.iterator();
    }
}
