package org.ehcache.impl.internal.store.offheap;

import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.terracotta.offheapstore.util.DebuggingUtils;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/HeuristicConfiguration.class */
public class HeuristicConfiguration {
    private static final String MINIMUM_MAX_MEMORY = "1M";
    private static final long MINIMUM_MAX_MEMORY_IN_BYTES = MemorySizeParser.parse(MINIMUM_MAX_MEMORY);
    private static final int IDEAL_MAX_SEGMENT_SIZE = 33554432;
    private static final int MAXIMUM_CHUNK_SIZE = 1073741824;
    private static final int MINIMUM_SEGMENT_COUNT = 16;
    private static final int MAXIMUM_SEGMENT_COUNT = 16384;
    private static final int MAXIMAL_SEGMENT_SIZE_RATIO = 4;
    private static final int INITIAL_SEGMENT_SIZE_RATIO = 16;
    private static final int ASSUMED_KEY_VALUE_SIZE = 1024;
    private static final int AGGRESSIVE_INITIAL_SEGMENT_SIZE_RATIO = 1;
    private final long maximumSize;
    private final int idealMaxSegmentSize;
    private final int maximumChunkSize;
    private final int minimumSegmentCount;
    private final int maximumSegmentCount;
    private final int maximalSegmentSizeRatio;
    private final int initialSegmentSizeRatio;
    private final int assumedKeyValueSize;

    public HeuristicConfiguration(long maximumSize) {
        if (maximumSize < MINIMUM_MAX_MEMORY_IN_BYTES) {
            throw new IllegalArgumentException("The value of maxBytesLocalOffHeap is less than the minimum allowed value of 1M. Reconfigure maxBytesLocalOffHeap in ehcache.xml or programmatically.");
        }
        this.maximumSize = maximumSize;
        if (OffHeapStoreUtils.getAdvancedBooleanConfigProperty("aggressive", false)) {
            this.idealMaxSegmentSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("idealMaxSegmentSize", 33554432L);
            this.maximumChunkSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximumChunkSize", 1073741824L);
            this.minimumSegmentCount = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("minimumSegmentCount", 16L);
            this.maximumSegmentCount = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximumSegmentCount", 16384L);
            this.maximalSegmentSizeRatio = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximalSegmentSizeRatio", 4L);
            this.initialSegmentSizeRatio = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("initialSegmentSizeRatio", 1L);
            this.assumedKeyValueSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("assumedKeyValueSize", 1024L);
            return;
        }
        this.idealMaxSegmentSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("idealMaxSegmentSize", 33554432L);
        this.maximumChunkSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximumChunkSize", 1073741824L);
        this.minimumSegmentCount = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("minimumSegmentCount", 16L);
        this.maximumSegmentCount = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximumSegmentCount", 16384L);
        this.maximalSegmentSizeRatio = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("maximalSegmentSizeRatio", 4L);
        this.initialSegmentSizeRatio = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("initialSegmentSizeRatio", 16L);
        this.assumedKeyValueSize = (int) OffHeapStoreUtils.getAdvancedMemorySizeConfigProperty("assumedKeyValueSize", 1024L);
    }

    public long getMaximumSize() {
        return this.maximumSize;
    }

    public int getMinimumChunkSize() {
        return (int) Math.min(this.maximumChunkSize, this.maximalSegmentSizeRatio * (getMaximumSize() / getConcurrency()));
    }

    public int getMaximumChunkSize() {
        return (int) Math.min(getMaximumSize(), this.maximumChunkSize);
    }

    public int getConcurrency() {
        return Integer.highestOneBit((int) Math.min(this.maximumSegmentCount, Math.max(this.minimumSegmentCount, getMaximumSize() / this.idealMaxSegmentSize)));
    }

    public int getInitialSegmentTableSize() {
        return Math.max(1, getSegmentDataPageSize() / this.assumedKeyValueSize);
    }

    public int getSegmentDataPageSize() {
        return Integer.highestOneBit((int) Math.min(getMinimumChunkSize(), getInitialSegmentCapacity() * this.assumedKeyValueSize));
    }

    private long getInitialSegmentCapacity() {
        return getMaximumSize() / ((getConcurrency() * this.initialSegmentSizeRatio) * (this.assumedKeyValueSize + 16));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Heuristic Configuration: \n");
        sb.append("Maximum Size (specified)   : ").append(DebuggingUtils.toBase2SuffixedString(getMaximumSize())).append("B\n");
        sb.append("Minimum Chunk Size         : ").append(DebuggingUtils.toBase2SuffixedString(getMinimumChunkSize())).append("B\n");
        sb.append("Maximum Chunk Size         : ").append(DebuggingUtils.toBase2SuffixedString(getMaximumChunkSize())).append("B\n");
        sb.append("Concurrency                : ").append(getConcurrency()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        sb.append("Initial Segment Table Size : ").append(DebuggingUtils.toBase2SuffixedString(getInitialSegmentTableSize())).append(" slots\n");
        sb.append("Segment Data Page Size     : ").append(DebuggingUtils.toBase2SuffixedString(getSegmentDataPageSize())).append("B\n");
        return sb.toString();
    }
}
