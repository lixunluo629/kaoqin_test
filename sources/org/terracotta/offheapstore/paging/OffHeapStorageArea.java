package org.terracotta.offheapstore.paging;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.terracotta.offheapstore.storage.PointerSize;
import org.terracotta.offheapstore.storage.allocator.Allocator;
import org.terracotta.offheapstore.storage.allocator.IntegerBestFitAllocator;
import org.terracotta.offheapstore.storage.allocator.LongBestFitAllocator;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.Validation;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/OffHeapStorageArea.class */
public class OffHeapStorageArea {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) OffHeapStorageArea.class);
    private static final boolean VALIDATING = Validation.shouldValidate(OffHeapStorageArea.class);
    private static final long LARGEST_POWER_OF_TWO = Integer.highestOneBit(Integer.MAX_VALUE);
    private static final ByteBuffer[] EMPTY_BUFFER_ARRAY = new ByteBuffer[0];
    private final int initialPageSize;
    private final int maximalPageSize;
    private final int pageGrowthAreaSize;
    private final float compressThreshold;
    private final Owner owner;
    private final PageSource pageSource;
    private final Allocator allocator;
    private final Random random;
    private Deque<Collection<Page>> released;
    private final Map<Integer, Page> pages;
    private final boolean thief;
    private final boolean victim;

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/OffHeapStorageArea$Owner.class */
    public interface Owner {
        boolean evictAtAddress(long j, boolean z);

        Lock writeLock();

        boolean isThief();

        boolean moved(long j, long j2);

        int sizeOf(long j);
    }

    public OffHeapStorageArea(PointerSize width, Owner owner, PageSource pageSource, int pageSize, boolean thief, boolean victim) {
        this(width, owner, pageSource, pageSize, pageSize, thief, victim);
    }

    public OffHeapStorageArea(PointerSize width, Owner owner, PageSource pageSource, int pageSize, boolean thief, boolean victim, float compressThreshold) {
        this(width, owner, pageSource, pageSize, pageSize, thief, victim, compressThreshold);
    }

    public OffHeapStorageArea(PointerSize width, Owner owner, PageSource pageSource, int initialPageSize, int maximalPageSize, boolean thief, boolean victim) {
        this(width, owner, pageSource, initialPageSize, maximalPageSize, thief, victim, 0.0f);
    }

    public OffHeapStorageArea(PointerSize width, Owner owner, PageSource pageSource, int initialPageSize, int maximalPageSize, boolean thief, boolean victim, float compressThreshold) {
        this.random = new Random();
        this.released = new LinkedList();
        this.pages = new ConcurrentHashMap(1, 0.75f, 1);
        if (victim && maximalPageSize != initialPageSize) {
            throw new IllegalArgumentException("Variable page-size offheap storage areas cannot be victims as they do not support stealing.");
        }
        this.owner = owner;
        this.pageSource = pageSource;
        switch (width) {
            case INT:
                this.allocator = new IntegerBestFitAllocator(this);
                break;
            case LONG:
                this.allocator = new LongBestFitAllocator(this);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        int initialPageSize2 = Math.max(this.allocator.getMinimalSize(), initialPageSize);
        if (Integer.bitCount(initialPageSize2) == 1) {
            this.initialPageSize = (int) Math.min(LARGEST_POWER_OF_TWO, initialPageSize2);
        } else {
            this.initialPageSize = (int) Math.min(LARGEST_POWER_OF_TWO, Long.highestOneBit(initialPageSize2) << 1);
        }
        if (maximalPageSize < initialPageSize2) {
            this.maximalPageSize = initialPageSize2;
        } else if (Integer.bitCount(maximalPageSize) == 1) {
            this.maximalPageSize = (int) Math.min(LARGEST_POWER_OF_TWO, maximalPageSize);
        } else {
            this.maximalPageSize = (int) Math.min(LARGEST_POWER_OF_TWO, Long.highestOneBit(maximalPageSize) << 1);
        }
        this.pageGrowthAreaSize = this.maximalPageSize - this.initialPageSize;
        this.compressThreshold = compressThreshold;
        this.thief = thief;
        this.victim = victim;
    }

    public void clear() {
        this.allocator.clear();
        Iterator<Page> it = this.pages.values().iterator();
        while (it.hasNext()) {
            Page p = it.next();
            it.remove();
            freePage(p);
        }
        validatePages();
    }

    public byte readByte(long address) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        return this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().get(pageAddress);
    }

    public short readShort(long address) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 2 <= pageSize) {
            return this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().getShort(pageAddress);
        }
        short value = 0;
        for (int i = 0; i < 2; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            value = (short) (value | ((255 & buffer.get(pageAddress)) << (8 * (1 - i))));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
        return value;
    }

    public int readInt(long address) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 4 <= pageSize) {
            return this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().getInt(pageAddress);
        }
        int value = 0;
        for (int i = 0; i < 4; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            value |= (255 & buffer.get(pageAddress)) << (8 * (3 - i));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
        return value;
    }

    public long readLong(long address) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 8 <= pageSize) {
            return this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().getLong(pageAddress);
        }
        long value = 0;
        for (int i = 0; i < 8; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            value |= (255 & buffer.get(pageAddress)) << (8 * (7 - i));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
        return value;
    }

    public ByteBuffer readBuffer(long address, int length) {
        ByteBuffer[] buffers = readBuffers(address, length);
        if (buffers.length == 1) {
            return buffers[0];
        }
        ByteBuffer copy = ByteBuffer.allocate(length);
        for (ByteBuffer b : buffers) {
            copy.put(b);
        }
        return ((ByteBuffer) copy.flip()).asReadOnlyBuffer();
    }

    public ByteBuffer[] readBuffers(long address, int length) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + length <= pageSize) {
            return new ByteBuffer[]{((ByteBuffer) this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().duplicate().limit(pageAddress + length).position(pageAddress)).slice().asReadOnlyBuffer()};
        }
        List<ByteBuffer> buffers = new ArrayList<>(length / pageSize);
        int remaining = length;
        while (remaining > 0) {
            ByteBuffer pageBuffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().duplicate();
            pageBuffer.position(pageAddress);
            if (pageBuffer.remaining() > remaining) {
                pageBuffer.limit(pageBuffer.position() + remaining);
            }
            ByteBuffer buffer = pageBuffer.slice().asReadOnlyBuffer();
            address += buffer.remaining();
            remaining -= buffer.remaining();
            buffers.add(buffer);
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
        return (ByteBuffer[]) buffers.toArray(EMPTY_BUFFER_ARRAY);
    }

    public void writeByte(long address, byte value) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().put(pageAddress, value);
    }

    public void writeShort(long address, short value) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 2 <= pageSize) {
            this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().putShort(pageAddress, value);
            return;
        }
        for (int i = 0; i < 2; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            buffer.position(pageAddress);
            buffer.put((byte) (value >> (8 * (1 - i))));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
    }

    public void writeInt(long address, int value) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 4 <= pageSize) {
            this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().putInt(pageAddress, value);
            return;
        }
        for (int i = 0; i < 4; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            buffer.position(pageAddress);
            buffer.put((byte) (value >> (8 * (3 - i))));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
    }

    public void writeLong(long address, long value) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + 8 <= pageSize) {
            this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer().putLong(pageAddress, value);
            return;
        }
        for (int i = 0; i < 8; i++) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            buffer.position(pageAddress);
            buffer.put((byte) (value >> (8 * (7 - i))));
            address++;
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
    }

    public void writeBuffer(long address, ByteBuffer data) {
        int pageIndex = pageIndexFor(address);
        int pageAddress = pageAddressFor(address);
        int pageSize = pageSizeFor(pageIndex);
        if (pageAddress + data.remaining() <= pageSize) {
            ByteBuffer buffer = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            buffer.position(pageAddress);
            buffer.put(data);
            return;
        }
        while (data.hasRemaining()) {
            ByteBuffer buffer2 = this.pages.get(Integer.valueOf(pageIndex)).asByteBuffer();
            buffer2.position(pageAddress);
            if (data.remaining() > buffer2.remaining()) {
                int originalLimit = data.limit();
                try {
                    data.limit(data.position() + buffer2.remaining());
                    address += data.remaining();
                    buffer2.put(data);
                    data.limit(originalLimit);
                } catch (Throwable th) {
                    data.limit(originalLimit);
                    throw th;
                }
            } else {
                address += data.remaining();
                buffer2.put(data);
            }
            pageIndex = pageIndexFor(address);
            pageAddress = pageAddressFor(address);
        }
    }

    public void writeBuffers(long address, ByteBuffer[] data) {
        for (ByteBuffer buffer : data) {
            int length = buffer.remaining();
            writeBuffer(address, buffer);
            address += length;
        }
    }

    public void free(long address) {
        this.allocator.free(address);
        if (this.compressThreshold > 0.0f) {
            float occupation = getOccupiedMemory() / this.allocator.getLastUsedAddress();
            if (occupation < this.compressThreshold) {
                compress();
            }
        }
    }

    private boolean compress() {
        long lastAddress = this.allocator.getLastUsedPointer();
        int sizeOfArea = this.owner.sizeOf(lastAddress);
        long compressed = this.allocator.allocate(sizeOfArea);
        if (compressed >= 0) {
            if (compressed < lastAddress) {
                writeBuffers(compressed, readBuffers(lastAddress, sizeOfArea));
                if (this.owner.moved(lastAddress, compressed)) {
                    this.allocator.free(lastAddress);
                    return true;
                }
            }
            this.allocator.free(compressed);
            return false;
        }
        return false;
    }

    public void destroy() {
        this.allocator.clear();
        Iterator<Page> it = this.pages.values().iterator();
        while (it.hasNext()) {
            Page p = it.next();
            it.remove();
            freePage(p);
        }
        validatePages();
    }

    public long allocate(long size) {
        do {
            long address = this.allocator.allocate(size);
            if (address >= 0) {
                return address;
            }
        } while (expandData());
        return -1L;
    }

    private boolean expandData() {
        int newPageSize = nextPageSize();
        if (getAllocatedMemory() + newPageSize > this.allocator.getMaximumAddress()) {
            return false;
        }
        Page newPage = this.pageSource.allocate(newPageSize, this.thief, this.victim, this);
        if (newPage == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Data area expansion from {} failed", Long.valueOf(getAllocatedMemory()));
                return false;
            }
            return false;
        }
        if (this.pages.put(Integer.valueOf(this.pages.size()), newPage) == null) {
            validatePages();
            this.allocator.expand(newPageSize);
            if (LOGGER.isDebugEnabled()) {
                long before = getAllocatedMemory();
                long after = before + newPageSize;
                LOGGER.debug("Data area expanded from {}B to {}B [occupation={}]", DebuggingUtils.toBase2SuffixedString(before), DebuggingUtils.toBase2SuffixedString(after), Float.valueOf(this.allocator.occupied() / after));
                return true;
            }
            return true;
        }
        freePage(newPage);
        validatePages();
        throw new AssertionError();
    }

    public long getAllocatedMemory() {
        return addressForPage(this.pages.size());
    }

    public long getOccupiedMemory() {
        return this.allocator.occupied();
    }

    public String toString() {
        Page q;
        StringBuilder sb = new StringBuilder("OffHeapStorageArea\n");
        int i = 0;
        while (i < this.pages.size()) {
            int i2 = i;
            i++;
            Page p = this.pages.get(Integer.valueOf(i2));
            if (p == null) {
                break;
            }
            int size = p.size();
            int count = 1;
            while (i < this.pages.size() && (q = this.pages.get(Integer.valueOf(i))) != null && q.size() == size) {
                count++;
                i++;
            }
            sb.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN).append(count).append(SymbolConstants.SPACE_SYMBOL).append(DebuggingUtils.toBase2SuffixedString(size)).append("B page").append(count == 1 ? ScriptUtils.FALLBACK_STATEMENT_SEPARATOR : "s\n");
        }
        sb.append("Allocator: ").append(this.allocator).append('\n');
        sb.append("Page Source: ").append(this.pageSource);
        return sb.toString();
    }

    private int pageIndexFor(long address) {
        if (address > this.pageGrowthAreaSize) {
            return (int) (((address - this.pageGrowthAreaSize) / this.maximalPageSize) + pageIndexFor(this.pageGrowthAreaSize));
        }
        return (64 - Long.numberOfLeadingZeros((address / this.initialPageSize) + 1)) - 1;
    }

    private long addressForPage(int index) {
        int postIndex = index - pageIndexFor(this.pageGrowthAreaSize);
        if (postIndex > 0) {
            return this.pageGrowthAreaSize + (this.maximalPageSize * postIndex);
        }
        return (this.initialPageSize << index) - this.initialPageSize;
    }

    private int pageAddressFor(long address) {
        return (int) (address - addressForPage(pageIndexFor(address)));
    }

    private int pageSizeFor(int index) {
        if (index < pageIndexFor(this.pageGrowthAreaSize)) {
            return this.initialPageSize << index;
        }
        return this.maximalPageSize;
    }

    private int nextPageSize() {
        return pageSizeFor(this.pages.size());
    }

    public void validateStorageArea() {
        this.allocator.validateAllocator();
    }

    public void release(long address) {
        int lastPage = pageIndexFor(address);
        for (int i = this.pages.size() - 1; i > lastPage; i--) {
            Page p = this.pages.remove(Integer.valueOf(i));
            this.allocator.expand(-p.size());
            freePage(p);
        }
        validatePages();
    }

    private void freePage(Page p) {
        if (this.released.isEmpty()) {
            this.pageSource.free(p);
        } else {
            this.released.peek().add(p);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0152, code lost:
    
        if (r0.isEmpty() == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0156, code lost:
    
        r5.released.pop();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0169, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0189, code lost:
    
        r0 = r0.iterator();
        r0 = r6.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01a1, code lost:
    
        if (r0.hasNext() == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01a4, code lost:
    
        r0 = r0.next();
        r0 = getIndexForPage(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01ba, code lost:
    
        if (r0 < 0) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01c4, code lost:
    
        if (r0.hasNext() == false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01c7, code lost:
    
        r0 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01d6, code lost:
    
        if (org.terracotta.offheapstore.paging.OffHeapStorageArea.VALIDATING == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01dd, code lost:
    
        if (r0 == r0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01e0, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01e4, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01e5, code lost:
    
        org.terracotta.offheapstore.util.Validation.validate(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01eb, code lost:
    
        if (org.terracotta.offheapstore.paging.OffHeapStorageArea.VALIDATING == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01f8, code lost:
    
        if (r0.size() != r0.size()) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01fb, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01ff, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0200, code lost:
    
        org.terracotta.offheapstore.util.Validation.validate(r0);
        ((java.nio.ByteBuffer) r0.asByteBuffer().clear()).put((java.nio.ByteBuffer) r0.asByteBuffer().clear());
        r5.pages.put(java.lang.Integer.valueOf(r0), r0);
        r0.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x023a, code lost:
    
        validatePages();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0245, code lost:
    
        if (r0.hasNext() == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0248, code lost:
    
        freePage(r0.next());
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0264, code lost:
    
        return r0;
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Collection<org.terracotta.offheapstore.paging.Page> release(java.util.Collection<org.terracotta.offheapstore.paging.Page> r6) {
        /*
            Method dump skipped, instructions count: 624
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.paging.OffHeapStorageArea.release(java.util.Collection):java.util.Collection");
    }

    private boolean moveAddressDown(long target) {
        int sizeOfArea = this.owner.sizeOf(target);
        long ceiling = addressForPage(Math.max(0, pageIndexFor(target) - 2)) + 1;
        long startAt = this.random.nextLong() % ceiling;
        Iterator<Long> pointers = this.allocator.iterator();
        while (pointers.hasNext() && pointers.next().longValue() < startAt) {
        }
        while (pointers.hasNext()) {
            long address = pointers.next().longValue();
            if (address < target && this.owner.evictAtAddress(address, false)) {
                long relocated = this.allocator.allocate(sizeOfArea);
                if (relocated < 0) {
                    continue;
                } else {
                    if (relocated < target) {
                        writeBuffers(relocated, readBuffers(target, sizeOfArea));
                        if (!this.owner.moved(target, relocated)) {
                            throw new AssertionError("Failure to move mapping during release");
                        }
                        this.allocator.free(target);
                        return true;
                    }
                    this.allocator.free(relocated);
                }
            }
        }
        LOGGER.debug("Random Eviction Failure Migration Failed - Using Biased Approach");
        Iterator i$ = this.allocator.iterator();
        while (i$.hasNext()) {
            long address2 = i$.next().longValue();
            if (address2 < target && this.owner.evictAtAddress(address2, false)) {
                long relocated2 = this.allocator.allocate(sizeOfArea);
                if (relocated2 < 0) {
                    continue;
                } else {
                    if (relocated2 < target) {
                        writeBuffer(relocated2, readBuffer(target, sizeOfArea));
                        this.owner.moved(target, relocated2);
                        this.allocator.free(target);
                        return true;
                    }
                    this.allocator.free(relocated2);
                }
            }
        }
        return false;
    }

    public boolean shrink() {
        Lock ownerLock = this.owner.writeLock();
        ownerLock.lock();
        try {
            if (this.pages.isEmpty()) {
                return false;
            }
            int initialSize = this.pages.size();
            for (Page p : release(new LinkedList(Collections.singletonList(this.pages.get(Integer.valueOf(this.pages.size() - 1)))))) {
                freePage(p);
            }
            boolean z = this.pages.size() < initialSize;
            ownerLock.unlock();
            return z;
        } finally {
            ownerLock.unlock();
        }
    }

    private int getIndexForPage(Page p) {
        for (Map.Entry<Integer, Page> e : this.pages.entrySet()) {
            if (e.getValue() == p) {
                return e.getKey().intValue();
            }
        }
        return -1;
    }

    private void validatePages() {
        if (VALIDATING) {
            for (int i = 0; i < this.pages.size(); i++) {
                if (this.pages.get(Integer.valueOf(i)) == null) {
                    List<Integer> pageIndices = new ArrayList<>(this.pages.keySet());
                    Collections.sort(pageIndices);
                    throw new AssertionError("Page Indices " + pageIndices);
                }
            }
        }
    }
}
