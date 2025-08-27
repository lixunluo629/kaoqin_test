package org.apache.commons.compress.compressors.brotli;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/brotli/BrotliUtils.class */
public class BrotliUtils {
    private static volatile CachedAvailability cachedBrotliAvailability = CachedAvailability.DONT_CACHE;

    /* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/brotli/BrotliUtils$CachedAvailability.class */
    enum CachedAvailability {
        DONT_CACHE,
        CACHED_AVAILABLE,
        CACHED_UNAVAILABLE
    }

    static {
        try {
            Class.forName("org.osgi.framework.BundleEvent");
        } catch (Exception e) {
            setCacheBrotliAvailablity(true);
        }
    }

    private BrotliUtils() {
    }

    public static boolean isBrotliCompressionAvailable() {
        CachedAvailability cachedResult = cachedBrotliAvailability;
        if (cachedResult != CachedAvailability.DONT_CACHE) {
            return cachedResult == CachedAvailability.CACHED_AVAILABLE;
        }
        return internalIsBrotliCompressionAvailable();
    }

    private static boolean internalIsBrotliCompressionAvailable() throws ClassNotFoundException {
        try {
            Class.forName("org.brotli.dec.BrotliInputStream");
            return true;
        } catch (Exception | NoClassDefFoundError e) {
            return false;
        }
    }

    public static void setCacheBrotliAvailablity(boolean doCache) throws ClassNotFoundException {
        if (!doCache) {
            cachedBrotliAvailability = CachedAvailability.DONT_CACHE;
        } else if (cachedBrotliAvailability == CachedAvailability.DONT_CACHE) {
            boolean hasBrotli = internalIsBrotliCompressionAvailable();
            cachedBrotliAvailability = hasBrotli ? CachedAvailability.CACHED_AVAILABLE : CachedAvailability.CACHED_UNAVAILABLE;
        }
    }

    static CachedAvailability getCachedBrotliAvailability() {
        return cachedBrotliAvailability;
    }
}
