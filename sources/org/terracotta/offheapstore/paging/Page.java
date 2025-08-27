package org.terracotta.offheapstore.paging;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/Page.class */
public class Page {
    private final ByteBuffer buffer;
    private final OffHeapStorageArea binding;
    private final int index;
    private final int address;
    private boolean freeable;

    public Page(ByteBuffer buffer, OffHeapStorageArea binding) {
        this.buffer = buffer;
        this.binding = binding;
        this.index = -1;
        this.address = -1;
        this.freeable = false;
    }

    public Page(ByteBuffer buffer, int index, int address, OffHeapStorageArea binding) {
        this.buffer = buffer;
        this.binding = binding;
        this.index = index;
        this.address = address;
        this.freeable = true;
    }

    public ByteBuffer asByteBuffer() {
        return this.buffer;
    }

    public IntBuffer asIntBuffer() {
        return this.buffer.asIntBuffer();
    }

    public int size() {
        if (this.buffer == null) {
            return 0;
        }
        return this.buffer.capacity();
    }

    public int index() {
        return this.index;
    }

    public int address() {
        return this.address;
    }

    public OffHeapStorageArea binding() {
        return this.binding;
    }

    synchronized boolean isFreeable() {
        if (this.freeable) {
            this.freeable = false;
            return true;
        }
        return false;
    }
}
