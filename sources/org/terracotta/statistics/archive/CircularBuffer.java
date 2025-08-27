package org.terracotta.statistics.archive;

import java.util.Arrays;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/CircularBuffer.class */
public class CircularBuffer<E> {
    private final E[] buffer;
    private int writeIndex;
    private int size;

    public CircularBuffer(int i) {
        this.buffer = (E[]) new Object[i];
    }

    public int capacity() {
        return this.buffer.length;
    }

    public synchronized E insert(E object) {
        E old = this.buffer[this.writeIndex];
        this.buffer[this.writeIndex] = object;
        this.writeIndex++;
        this.size = Math.max(this.writeIndex, this.size);
        this.writeIndex %= this.buffer.length;
        return old;
    }

    public synchronized <T> T[] toArray(Class<T[]> cls) {
        if (this.size < this.buffer.length) {
            return (T[]) Arrays.copyOfRange(this.buffer, 0, this.writeIndex, cls);
        }
        T[] tArr = (T[]) Arrays.copyOfRange(this.buffer, this.writeIndex, this.writeIndex + this.buffer.length, cls);
        System.arraycopy(this.buffer, 0, tArr, this.buffer.length - this.writeIndex, this.writeIndex);
        return tArr;
    }
}
