package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackDynamicTable.class */
final class HpackDynamicTable {
    HpackHeaderField[] hpackHeaderFields;
    int head;
    int tail;
    private long size;
    private long capacity = -1;

    HpackDynamicTable(long initialCapacity) {
        setCapacity(initialCapacity);
    }

    public int length() {
        int length;
        if (this.head < this.tail) {
            length = (this.hpackHeaderFields.length - this.tail) + this.head;
        } else {
            length = this.head - this.tail;
        }
        return length;
    }

    public long size() {
        return this.size;
    }

    public long capacity() {
        return this.capacity;
    }

    public HpackHeaderField getEntry(int index) {
        if (index <= 0 || index > length()) {
            throw new IndexOutOfBoundsException();
        }
        int i = this.head - index;
        if (i < 0) {
            return this.hpackHeaderFields[i + this.hpackHeaderFields.length];
        }
        return this.hpackHeaderFields[i];
    }

    public void add(HpackHeaderField header) {
        int headerSize = header.size();
        if (headerSize > this.capacity) {
            clear();
            return;
        }
        while (this.capacity - this.size < headerSize) {
            remove();
        }
        HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
        int i = this.head;
        this.head = i + 1;
        hpackHeaderFieldArr[i] = header;
        this.size += header.size();
        if (this.head == this.hpackHeaderFields.length) {
            this.head = 0;
        }
    }

    public HpackHeaderField remove() {
        HpackHeaderField removed = this.hpackHeaderFields[this.tail];
        if (removed == null) {
            return null;
        }
        this.size -= removed.size();
        HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
        int i = this.tail;
        this.tail = i + 1;
        hpackHeaderFieldArr[i] = null;
        if (this.tail == this.hpackHeaderFields.length) {
            this.tail = 0;
        }
        return removed;
    }

    public void clear() {
        while (this.tail != this.head) {
            HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
            int i = this.tail;
            this.tail = i + 1;
            hpackHeaderFieldArr[i] = null;
            if (this.tail == this.hpackHeaderFields.length) {
                this.tail = 0;
            }
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0L;
    }

    public void setCapacity(long capacity) {
        if (capacity < 0 || capacity > 4294967295L) {
            throw new IllegalArgumentException("capacity is invalid: " + capacity);
        }
        if (this.capacity == capacity) {
            return;
        }
        this.capacity = capacity;
        if (capacity == 0) {
            clear();
        } else {
            while (this.size > capacity) {
                remove();
            }
        }
        int maxEntries = (int) (capacity / 32);
        if (capacity % 32 != 0) {
            maxEntries++;
        }
        if (this.hpackHeaderFields != null && this.hpackHeaderFields.length == maxEntries) {
            return;
        }
        HpackHeaderField[] tmp = new HpackHeaderField[maxEntries];
        int len = length();
        int cursor = this.tail;
        for (int i = 0; i < len; i++) {
            int i2 = cursor;
            cursor++;
            HpackHeaderField entry = this.hpackHeaderFields[i2];
            tmp[i] = entry;
            if (cursor == this.hpackHeaderFields.length) {
                cursor = 0;
            }
        }
        this.tail = 0;
        this.head = this.tail + len;
        this.hpackHeaderFields = tmp;
    }
}
