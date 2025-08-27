package org.ehcache.impl.internal.store.offheap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/MemorySizeParser.class */
public class MemorySizeParser {
    private static final long BYTE = 1;
    private static final long KILOBYTE = 1024;
    private static final long MEGABYTE = 1048576;
    private static final long GIGABYTE = 1073741824;
    private static final long TERABYTE = 1099511627776L;

    public static long parse(String configuredMemorySize) throws IllegalArgumentException {
        MemorySize size = parseIncludingUnit(configuredMemorySize);
        return size.calculateMemorySizeInBytes();
    }

    private static MemorySize parseIncludingUnit(String configuredMemorySize) throws IllegalArgumentException {
        MemorySize memorySize;
        if (configuredMemorySize == null || "".equals(configuredMemorySize)) {
            return new MemorySize("0", BYTE);
        }
        char unit = configuredMemorySize.charAt(configuredMemorySize.length() - 1);
        switch (unit) {
            case 'G':
            case 'g':
                memorySize = toMemorySize(configuredMemorySize, 1073741824L);
                break;
            case 'K':
            case 'k':
                memorySize = toMemorySize(configuredMemorySize, 1024L);
                break;
            case 'M':
            case 'm':
                memorySize = toMemorySize(configuredMemorySize, 1048576L);
                break;
            case 'T':
            case 't':
                memorySize = toMemorySize(configuredMemorySize, 1099511627776L);
                break;
            default:
                try {
                    Integer.parseInt("" + unit);
                    memorySize = new MemorySize(configuredMemorySize, BYTE);
                    break;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("invalid format for memory size [" + configuredMemorySize + "]");
                }
        }
        return memorySize;
    }

    private static MemorySize toMemorySize(String configuredMemorySize, long unitMultiplier) {
        if (configuredMemorySize.length() < 2) {
            throw new IllegalArgumentException("invalid format for memory size [" + configuredMemorySize + "]");
        }
        return new MemorySize(configuredMemorySize.substring(0, configuredMemorySize.length() - 1), unitMultiplier);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/MemorySizeParser$MemorySize.class */
    private static final class MemorySize {
        private String configuredMemorySizeWithoutUnit;
        private long multiplicationFactor;

        private MemorySize(String configuredMemorySizeWithoutUnit, long multiplicationFactor) {
            this.configuredMemorySizeWithoutUnit = configuredMemorySizeWithoutUnit;
            this.multiplicationFactor = multiplicationFactor;
        }

        public long calculateMemorySizeInBytes() throws IllegalArgumentException {
            try {
                long memorySizeLong = Long.parseLong(this.configuredMemorySizeWithoutUnit);
                long result = memorySizeLong * this.multiplicationFactor;
                if (result < 0) {
                    throw new IllegalArgumentException("memory size cannot be negative");
                }
                return result;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid format for memory size");
            }
        }
    }
}
