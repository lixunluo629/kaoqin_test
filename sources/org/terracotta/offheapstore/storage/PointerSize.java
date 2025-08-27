package org.terracotta.offheapstore.storage;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/PointerSize.class */
public enum PointerSize {
    INT(32),
    LONG(64);

    private final int size;

    PointerSize(int size) {
        this.size = size;
    }

    public int bitSize() {
        return this.size;
    }

    public int byteSize() {
        return this.size / 8;
    }
}
