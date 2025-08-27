package org.terracotta.offheapstore.paging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.buffersource.BufferSource;
import org.terracotta.offheapstore.storage.allocator.PowerOfTwoAllocator;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.MemoryUnit;
import org.terracotta.offheapstore.util.PhysicalMemory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/UpfrontAllocatingPageSource.class */
public class UpfrontAllocatingPageSource implements PageSource {
    private static final double PROGRESS_LOGGING_STEP_SIZE = 0.1d;
    private final SortedMap<Long, Runnable> risingThresholds;
    private final SortedMap<Long, Runnable> fallingThresholds;
    private final List<PowerOfTwoAllocator> sliceAllocators;
    private final List<PowerOfTwoAllocator> victimAllocators;
    private final List<ByteBuffer> buffers;
    private final List<NavigableSet<Page>> victims;
    private volatile int availableSet;
    public static final String ALLOCATION_LOG_LOCATION = UpfrontAllocatingPageSource.class.getName() + ".allocationDump";
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) UpfrontAllocatingPageSource.class);
    private static final long PROGRESS_LOGGING_THRESHOLD = MemoryUnit.GIGABYTES.toBytes(4L);
    private static final Comparator<Page> REGION_COMPARATOR = new Comparator<Page>() { // from class: org.terracotta.offheapstore.paging.UpfrontAllocatingPageSource.1
        @Override // java.util.Comparator
        public int compare(Page a, Page b) {
            if (a.address() == b.address()) {
                return a.size() - b.size();
            }
            return a.address() - b.address();
        }
    };

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/UpfrontAllocatingPageSource$ThresholdDirection.class */
    public enum ThresholdDirection {
        RISING,
        FALLING
    }

    public UpfrontAllocatingPageSource(BufferSource source, long toAllocate, int chunkSize) {
        this(source, toAllocate, chunkSize, -1, true);
    }

    public UpfrontAllocatingPageSource(BufferSource source, long toAllocate, int maxChunk, int minChunk) {
        this(source, toAllocate, maxChunk, minChunk, false);
    }

    private UpfrontAllocatingPageSource(BufferSource source, long toAllocate, int maxChunk, int minChunk, boolean fixed) {
        this.risingThresholds = new TreeMap();
        this.fallingThresholds = new TreeMap();
        this.sliceAllocators = new ArrayList();
        this.victimAllocators = new ArrayList();
        this.buffers = new ArrayList();
        this.victims = new ArrayList();
        this.availableSet = -1;
        Long totalPhysical = PhysicalMemory.totalPhysicalMemory();
        Long freePhysical = PhysicalMemory.freePhysicalMemory();
        if (totalPhysical != null && toAllocate > totalPhysical.longValue()) {
            throw new IllegalArgumentException("Attempting to allocate " + DebuggingUtils.toBase2SuffixedString(toAllocate) + "B of memory when the host only contains " + DebuggingUtils.toBase2SuffixedString(totalPhysical.longValue()) + "B of physical memory");
        }
        if (freePhysical != null && toAllocate > freePhysical.longValue()) {
            LOGGER.warn("Attempting to allocate {}B of offheap when there is only {}B of free physical memory - some paging will therefore occur.", DebuggingUtils.toBase2SuffixedString(toAllocate), DebuggingUtils.toBase2SuffixedString(freePhysical.longValue()));
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Allocating {}B in chunks", DebuggingUtils.toBase2SuffixedString(toAllocate));
        }
        for (ByteBuffer buffer : allocateBackingBuffers(source, toAllocate, maxChunk, minChunk, fixed)) {
            this.sliceAllocators.add(new PowerOfTwoAllocator(buffer.capacity()));
            this.victimAllocators.add(new PowerOfTwoAllocator(buffer.capacity()));
            this.victims.add(new TreeSet(REGION_COMPARATOR));
            this.buffers.add(buffer);
        }
    }

    public long getCapacity() {
        long capacity = 0;
        for (ByteBuffer buffer : this.buffers) {
            capacity += buffer.capacity();
        }
        return capacity;
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public Page allocate(int size, boolean thief, boolean victim, OffHeapStorageArea owner) {
        if (thief) {
            return allocateAsThief(size, victim, owner);
        }
        return allocateFromFree(size, victim, owner);
    }

    private Page allocateAsThief(int size, boolean victim, OffHeapStorageArea owner) {
        Page free = allocateFromFree(size, victim, owner);
        if (free != null) {
            return free;
        }
        PowerOfTwoAllocator victimAllocator = null;
        PowerOfTwoAllocator sliceAllocator = null;
        List<Page> targets = Collections.emptyList();
        Collection<AllocatedRegion> tempHolds = new ArrayList<>();
        Map<OffHeapStorageArea, Collection<Page>> releases = new IdentityHashMap<>();
        synchronized (this) {
            int i = 0;
            while (true) {
                if (i >= this.victimAllocators.size()) {
                    break;
                }
                int address = this.victimAllocators.get(i).find(size, victim ? PowerOfTwoAllocator.Packing.CEILING : PowerOfTwoAllocator.Packing.FLOOR);
                if (address < 0) {
                    i++;
                } else {
                    victimAllocator = this.victimAllocators.get(i);
                    sliceAllocator = this.sliceAllocators.get(i);
                    targets = findVictimPages(i, address, size);
                    int claimAddress = address;
                    for (Page p : targets) {
                        victimAllocator.claim(p.address(), p.size());
                        int claimSize = p.address() - claimAddress;
                        if (claimSize > 0) {
                            tempHolds.add(new AllocatedRegion(claimAddress, claimSize));
                            sliceAllocator.claim(claimAddress, claimSize);
                            victimAllocator.claim(claimAddress, claimSize);
                        }
                        claimAddress = p.address() + p.size();
                    }
                    int claimSize2 = (address + size) - claimAddress;
                    if (claimSize2 > 0) {
                        tempHolds.add(new AllocatedRegion(claimAddress, claimSize2));
                        sliceAllocator.claim(claimAddress, claimSize2);
                        victimAllocator.claim(claimAddress, claimSize2);
                    }
                }
            }
            for (Page p2 : targets) {
                OffHeapStorageArea a = p2.binding();
                Collection<Page> c = releases.get(a);
                if (c == null) {
                    Collection<Page> c2 = new LinkedList<>();
                    c2.add(p2);
                    releases.put(a, c2);
                } else {
                    c.add(p2);
                }
            }
        }
        Collection<Page> results = new LinkedList<>();
        for (Map.Entry<OffHeapStorageArea, Collection<Page>> e : releases.entrySet()) {
            results.addAll(e.getKey().release(e.getValue()));
        }
        List<Page> failedReleases = new ArrayList<>();
        synchronized (this) {
            for (AllocatedRegion r : tempHolds) {
                sliceAllocator.free(r.address, r.size);
                victimAllocator.free(r.address, r.size);
            }
            if (results.size() == targets.size()) {
                for (Page p3 : targets) {
                    victimAllocator.free(p3.address(), p3.size());
                    free(p3);
                }
                return allocateFromFree(size, victim, owner);
            }
            for (Page p4 : targets) {
                if (results.contains(p4)) {
                    victimAllocator.free(p4.address(), p4.size());
                    free(p4);
                } else {
                    failedReleases.add(p4);
                }
            }
            try {
                Page pageAllocateAsThief = allocateAsThief(size, victim, owner);
                synchronized (this) {
                    for (Page p5 : failedReleases) {
                        if (this.victims.get(p5.index()).floor(p5) == p5) {
                            victimAllocator.free(p5.address(), p5.size());
                        }
                    }
                }
                return pageAllocateAsThief;
            } catch (Throwable th) {
                synchronized (this) {
                    for (Page p6 : failedReleases) {
                        if (this.victims.get(p6.index()).floor(p6) == p6) {
                            victimAllocator.free(p6.address(), p6.size());
                        }
                    }
                    throw th;
                }
            }
        }
    }

    private List<Page> findVictimPages(int chunk, int address, int size) {
        return new ArrayList(this.victims.get(chunk).subSet(new Page(null, -1, address, null), new Page(null, -1, address + size, null)));
    }

    private Page allocateFromFree(int size, boolean victim, OffHeapStorageArea owner) {
        if (Integer.bitCount(size) != 1) {
            int rounded = Integer.highestOneBit(size) << 1;
            LOGGER.debug("Request to allocate {}B will allocate {}B", Integer.valueOf(size), DebuggingUtils.toBase2SuffixedString(rounded));
            size = rounded;
        }
        if (isUnavailable(size)) {
            return null;
        }
        synchronized (this) {
            for (int i = 0; i < this.sliceAllocators.size(); i++) {
                int address = this.sliceAllocators.get(i).allocate(size, victim ? PowerOfTwoAllocator.Packing.CEILING : PowerOfTwoAllocator.Packing.FLOOR);
                if (address >= 0) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Allocating a {}B buffer from chunk {} &{}", DebuggingUtils.toBase2SuffixedString(size), Integer.valueOf(i), Integer.valueOf(address));
                    }
                    ByteBuffer b = ((ByteBuffer) this.buffers.get(i).limit(address + size).position(address)).slice();
                    Page p = new Page(b, i, address, owner);
                    if (victim) {
                        this.victims.get(i).add(p);
                    } else {
                        this.victimAllocators.get(i).claim(address, size);
                    }
                    if (!this.risingThresholds.isEmpty()) {
                        long allocated = getAllocatedSize();
                        fireThresholds(allocated - size, allocated);
                    }
                    return p;
                }
            }
            markUnavailable(size);
            return null;
        }
    }

    @Override // org.terracotta.offheapstore.paging.PageSource
    public synchronized void free(Page page) {
        if (page.isFreeable()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Freeing a {}B buffer from chunk {} &{}", DebuggingUtils.toBase2SuffixedString(page.size()), Integer.valueOf(page.index()), Integer.valueOf(page.address()));
            }
            markAllAvailable();
            this.sliceAllocators.get(page.index()).free(page.address(), page.size());
            this.victims.get(page.index()).remove(page);
            this.victimAllocators.get(page.index()).tryFree(page.address(), page.size());
            if (!this.fallingThresholds.isEmpty()) {
                long allocated = getAllocatedSize();
                fireThresholds(allocated + page.size(), allocated);
            }
        }
    }

    public synchronized long getAllocatedSize() {
        long sum = 0;
        for (PowerOfTwoAllocator a : this.sliceAllocators) {
            sum += a.occupied();
        }
        return sum;
    }

    public long getAllocatedSizeUnSync() {
        long sum = 0;
        for (PowerOfTwoAllocator a : this.sliceAllocators) {
            sum += a.occupied();
        }
        return sum;
    }

    private boolean isUnavailable(int size) {
        return (this.availableSet & size) == 0;
    }

    private synchronized void markAllAvailable() {
        this.availableSet = -1;
    }

    private synchronized void markUnavailable(int size) {
        this.availableSet &= size ^ (-1);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder("UpfrontAllocatingPageSource");
        for (int i = 0; i < this.buffers.size(); i++) {
            sb.append("\nChunk ").append(i + 1).append('\n');
            sb.append("Size             : ").append(DebuggingUtils.toBase2SuffixedString(this.buffers.get(i).capacity())).append("B\n");
            sb.append("Free Allocator   : ").append(this.sliceAllocators.get(i)).append('\n');
            sb.append("Victim Allocator : ").append(this.victimAllocators.get(i));
        }
        return sb.toString();
    }

    private synchronized void fireThresholds(long incoming, long outgoing) {
        Collection<Runnable> thresholds;
        if (outgoing > incoming) {
            thresholds = this.risingThresholds.subMap(Long.valueOf(incoming), Long.valueOf(outgoing)).values();
        } else if (outgoing < incoming) {
            thresholds = this.fallingThresholds.subMap(Long.valueOf(outgoing), Long.valueOf(incoming)).values();
        } else {
            thresholds = Collections.emptyList();
        }
        for (Runnable r : thresholds) {
            try {
                r.run();
            } catch (Throwable t) {
                LOGGER.error("Throwable thrown by threshold action", t);
            }
        }
    }

    public synchronized Runnable addAllocationThreshold(ThresholdDirection direction, long threshold, Runnable action) {
        switch (direction) {
            case RISING:
                return this.risingThresholds.put(Long.valueOf(threshold), action);
            case FALLING:
                return this.fallingThresholds.put(Long.valueOf(threshold), action);
            default:
                throw new AssertionError();
        }
    }

    public synchronized Runnable removeAllocationThreshold(ThresholdDirection direction, long threshold) {
        switch (direction) {
            case RISING:
                return this.risingThresholds.remove(Long.valueOf(threshold));
            case FALLING:
                return this.fallingThresholds.remove(Long.valueOf(threshold));
            default:
                throw new AssertionError();
        }
    }

    /* JADX WARN: Finally extract failed */
    private static Collection<ByteBuffer> allocateBackingBuffers(final BufferSource source, long toAllocate, int maxChunk, final int minChunk, final boolean fixed) throws IOException {
        final long start = LOGGER.isDebugEnabled() ? System.nanoTime() : 0L;
        final PrintStream allocatorLog = createAllocatorLog(toAllocate, maxChunk, minChunk);
        Collection<ByteBuffer> buffers = new ArrayList<>((int) ((toAllocate / maxChunk) + 10));
        if (allocatorLog != null) {
            try {
                allocatorLog.printf("timestamp,threadid,duration,size,physfree,totalswap,freeswap,committed%n", new Object[0]);
            } finally {
                if (allocatorLog != null) {
                    allocatorLog.close();
                }
            }
        }
        List<Future<Collection<ByteBuffer>>> futures = new ArrayList<>((int) ((toAllocate / maxChunk) + 1));
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        long dispatched = 0;
        while (dispatched < toAllocate) {
            try {
                final int currentChunkSize = (int) Math.min(maxChunk, toAllocate - dispatched);
                futures.add(executorService.submit(new Callable<Collection<ByteBuffer>>() { // from class: org.terracotta.offheapstore.paging.UpfrontAllocatingPageSource.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Collection<ByteBuffer> call() throws Exception {
                        return UpfrontAllocatingPageSource.bufferAllocation(source, currentChunkSize, minChunk, fixed, allocatorLog, start);
                    }
                }));
                dispatched += currentChunkSize;
            } catch (Throwable th) {
                executorService.shutdown();
                throw th;
            }
        }
        executorService.shutdown();
        long allocated = 0;
        long progressStep = Math.max(PROGRESS_LOGGING_THRESHOLD, (long) (toAllocate * PROGRESS_LOGGING_STEP_SIZE));
        long nextProgressLogAt = progressStep;
        for (Future<Collection<ByteBuffer>> future : futures) {
            Collection<ByteBuffer> result = (Collection) uninterruptibleGet(future);
            buffers.addAll(result);
            for (ByteBuffer buffer : result) {
                allocated += buffer.capacity();
                if (allocated > nextProgressLogAt) {
                    LOGGER.info("Allocation {}% complete", Long.valueOf((100 * allocated) / toAllocate));
                    nextProgressLogAt += progressStep;
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            long duration = System.nanoTime() - start;
            LOGGER.debug("Took {} ms to create off-heap storage of {}B.", Long.valueOf(TimeUnit.NANOSECONDS.toMillis(duration)), DebuggingUtils.toBase2SuffixedString(toAllocate));
        }
        return Collections.unmodifiableCollection(buffers);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<ByteBuffer> bufferAllocation(BufferSource source, int toAllocate, int minChunk, boolean fixed, PrintStream allocatorLog, long start) {
        long allocated = 0;
        long currentChunkSize = toAllocate;
        Collection<ByteBuffer> buffers = new ArrayList<>();
        while (allocated < toAllocate) {
            long blockStart = System.nanoTime();
            int currentAllocation = (int) Math.min(currentChunkSize, toAllocate - allocated);
            ByteBuffer b = source.allocateBuffer(currentAllocation);
            long blockDuration = System.nanoTime() - blockStart;
            if (b == null) {
                if (fixed || (currentChunkSize >>> 1) < minChunk) {
                    throw new IllegalArgumentException("An attempt was made to allocate more off-heap memory than the JVM can allow. The limit on off-heap memory size is given by the -XX:MaxDirectMemorySize command (or equivalent).");
                }
                currentChunkSize >>>= 1;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Allocated failed at {}B, trying  {}B chunks.", DebuggingUtils.toBase2SuffixedString(currentAllocation), DebuggingUtils.toBase2SuffixedString(currentChunkSize));
                }
            } else {
                buffers.add(b);
                allocated += currentAllocation;
                if (allocatorLog != null) {
                    allocatorLog.printf("%d,%d,%d,%d,%d,%d,%d,%d%n", Long.valueOf(System.nanoTime() - start), Long.valueOf(Thread.currentThread().getId()), Long.valueOf(blockDuration), Integer.valueOf(currentAllocation), PhysicalMemory.freePhysicalMemory(), PhysicalMemory.totalSwapSpace(), PhysicalMemory.freeSwapSpace(), PhysicalMemory.ourCommittedVirtualMemory());
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{}B chunk allocated", DebuggingUtils.toBase2SuffixedString(currentAllocation));
                }
            }
        }
        return buffers;
    }

    private static <T> T uninterruptibleGet(Future<T> future) {
        T t;
        boolean wasInterrupted = false;
        while (true) {
            try {
                try {
                    t = future.get();
                    break;
                } catch (InterruptedException e) {
                    wasInterrupted = true;
                } catch (ExecutionException e2) {
                    if (e2.getCause() instanceof RuntimeException) {
                        throw ((RuntimeException) e2.getCause());
                    }
                    throw new RuntimeException(e2);
                }
            } catch (Throwable th) {
                if (wasInterrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
        return t;
    }

    private static PrintStream createAllocatorLog(long max, int maxChunk, int minChunk) throws IOException {
        String path = System.getProperty(ALLOCATION_LOG_LOCATION);
        if (path == null) {
            return null;
        }
        try {
            File allocatorLogFile = File.createTempFile("allocation", ".csv", new File(path));
            PrintStream allocatorLogStream = new PrintStream(allocatorLogFile, "US-ASCII");
            allocatorLogStream.printf("Timestamp: %s%n", new Date());
            allocatorLogStream.printf("Allocating: %sB%n", DebuggingUtils.toBase2SuffixedString(max));
            allocatorLogStream.printf("Max Chunk: %sB%n", DebuggingUtils.toBase2SuffixedString(maxChunk));
            allocatorLogStream.printf("Min Chunk: %sB%n", DebuggingUtils.toBase2SuffixedString(minChunk));
            return allocatorLogStream;
        } catch (IOException e) {
            LOGGER.warn("Exception creating allocation log", (Throwable) e);
            return null;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/paging/UpfrontAllocatingPageSource$AllocatedRegion.class */
    static class AllocatedRegion {
        private final int address;
        private final int size;

        AllocatedRegion(int address, int size) {
            this.address = address;
            this.size = size;
        }
    }
}
