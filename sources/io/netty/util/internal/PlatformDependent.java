package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscChunkedArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscGrowableAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.SpscLinkedAtomicQueue;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.poi.util.TempFile;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PlatformDependent.class */
public final class PlatformDependent {
    private static final InternalLogger logger;
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
    private static final boolean IS_WINDOWS;
    private static final boolean IS_OSX;
    private static final boolean IS_J9_JVM;
    private static final boolean IS_IVKVM_DOT_NET;
    private static final boolean MAYBE_SUPER_USER;
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
    private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final long MAX_DIRECT_MEMORY;
    private static final int MPSC_CHUNK_SIZE = 1024;
    private static final int MIN_MAX_MPSC_CAPACITY = 2048;
    private static final int MAX_ALLOWED_MPSC_CAPACITY = 1073741824;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final File TMPDIR;
    private static final int BIT_MODE;
    private static final String NORMALIZED_ARCH;
    private static final String NORMALIZED_OS;
    private static final String[] ALLOWED_LINUX_OS_CLASSIFIERS;
    private static final Set<String> LINUX_OS_CLASSIFIERS;
    private static final int ADDRESS_SIZE;
    private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
    private static final AtomicLong DIRECT_MEMORY_COUNTER;
    private static final long DIRECT_MEMORY_LIMIT;
    private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
    private static final Cleaner CLEANER;
    private static final int UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD;
    private static final String[] OS_RELEASE_FILES;
    private static final String LINUX_ID_PREFIX = "ID=";
    private static final String LINUX_ID_LIKE_PREFIX = "ID_LIKE=";
    public static final boolean BIG_ENDIAN_NATIVE_ORDER;
    private static final Cleaner NOOP;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PlatformDependent$ThreadLocalRandomProvider.class */
    private interface ThreadLocalRandomProvider {
        Random current();
    }

    static {
        $assertionsDisabled = !PlatformDependent.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) PlatformDependent.class);
        MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
        IS_WINDOWS = isWindows0();
        IS_OSX = isOsx0();
        IS_J9_JVM = isJ9Jvm0();
        IS_IVKVM_DOT_NET = isIkvmDotNet0();
        CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
        UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause0();
        MAX_DIRECT_MEMORY = maxDirectMemory0();
        BYTE_ARRAY_BASE_OFFSET = byteArrayBaseOffset0();
        TMPDIR = tmpdir0();
        BIT_MODE = bitMode0();
        NORMALIZED_ARCH = normalizeArch(SystemPropertyUtil.get("os.arch", ""));
        NORMALIZED_OS = normalizeOs(SystemPropertyUtil.get("os.name", ""));
        ALLOWED_LINUX_OS_CLASSIFIERS = new String[]{"fedora", "suse", "arch"};
        ADDRESS_SIZE = addressSize0();
        OS_RELEASE_FILES = new String[]{"/etc/os-release", "/usr/lib/os-release"};
        BIG_ENDIAN_NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        NOOP = new Cleaner() { // from class: io.netty.util.internal.PlatformDependent.1
            @Override // io.netty.util.internal.Cleaner
            public void freeDirectBuffer(ByteBuffer buffer) {
            }
        };
        if (javaVersion() >= 7) {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() { // from class: io.netty.util.internal.PlatformDependent.2
                @Override // io.netty.util.internal.PlatformDependent.ThreadLocalRandomProvider
                @SuppressJava6Requirement(reason = "Usage guarded by java version check")
                public Random current() {
                    return java.util.concurrent.ThreadLocalRandom.current();
                }
            };
        } else {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() { // from class: io.netty.util.internal.PlatformDependent.3
                @Override // io.netty.util.internal.PlatformDependent.ThreadLocalRandomProvider
                public Random current() {
                    return ThreadLocalRandom.current();
                }
            };
        }
        long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1L);
        if (maxDirectMemory == 0 || !hasUnsafe() || !PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            USE_DIRECT_BUFFER_NO_CLEANER = false;
            DIRECT_MEMORY_COUNTER = null;
        } else {
            USE_DIRECT_BUFFER_NO_CLEANER = true;
            if (maxDirectMemory < 0) {
                maxDirectMemory = MAX_DIRECT_MEMORY;
                if (maxDirectMemory <= 0) {
                    DIRECT_MEMORY_COUNTER = null;
                } else {
                    DIRECT_MEMORY_COUNTER = new AtomicLong();
                }
            } else {
                DIRECT_MEMORY_COUNTER = new AtomicLong();
            }
        }
        logger.debug("-Dio.netty.maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
        DIRECT_MEMORY_LIMIT = maxDirectMemory >= 1 ? maxDirectMemory : MAX_DIRECT_MEMORY;
        int tryAllocateUninitializedArray = SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
        UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = (javaVersion() < 9 || !PlatformDependent0.hasAllocateArrayMethod()) ? -1 : tryAllocateUninitializedArray;
        logger.debug("-Dio.netty.uninitializedArrayAllocationThreshold: {}", Integer.valueOf(UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD));
        MAYBE_SUPER_USER = maybeSuperUser0();
        if (!isAndroid()) {
            if (javaVersion() >= 9) {
                CLEANER = CleanerJava9.isSupported() ? new CleanerJava9() : NOOP;
            } else {
                CLEANER = CleanerJava6.isSupported() ? new CleanerJava6() : NOOP;
            }
        } else {
            CLEANER = NOOP;
        }
        DIRECT_BUFFER_PREFERRED = (CLEANER == NOOP || SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false)) ? false : true;
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noPreferDirect: {}", Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
        }
        if (CLEANER == NOOP && !PlatformDependent0.isExplicitNoUnsafe()) {
            logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system instability.");
        }
        final Set<String> allowedClassifiers = Collections.unmodifiableSet(new HashSet(Arrays.asList(ALLOWED_LINUX_OS_CLASSIFIERS)));
        final Set<String> availableClassifiers = new LinkedHashSet<>();
        for (final String osReleaseFileName : OS_RELEASE_FILES) {
            final File file = new File(osReleaseFileName);
            boolean found = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: io.netty.util.internal.PlatformDependent.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Boolean run() throws IOException {
                    try {
                        if (file.exists()) {
                            BufferedReader reader = null;
                            try {
                                try {
                                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), CharsetUtil.UTF_8));
                                    while (true) {
                                        String line = reader.readLine();
                                        if (line == null) {
                                            break;
                                        }
                                        if (line.startsWith(PlatformDependent.LINUX_ID_PREFIX)) {
                                            String id = PlatformDependent.normalizeOsReleaseVariableValue(line.substring(PlatformDependent.LINUX_ID_PREFIX.length()));
                                            PlatformDependent.addClassifier(allowedClassifiers, availableClassifiers, id);
                                        } else if (line.startsWith(PlatformDependent.LINUX_ID_LIKE_PREFIX)) {
                                            PlatformDependent.addClassifier(allowedClassifiers, availableClassifiers, PlatformDependent.normalizeOsReleaseVariableValue(line.substring(PlatformDependent.LINUX_ID_LIKE_PREFIX.length())).split("[ ]+"));
                                        }
                                    }
                                } catch (IOException e) {
                                    PlatformDependent.logger.debug("Error while reading content of {}", osReleaseFileName, e);
                                    if (reader != null) {
                                        try {
                                            reader.close();
                                        } catch (IOException e2) {
                                        }
                                    }
                                } catch (SecurityException e3) {
                                    PlatformDependent.logger.debug("Unable to read {}", osReleaseFileName, e3);
                                    if (reader != null) {
                                        try {
                                            reader.close();
                                        } catch (IOException e4) {
                                        }
                                    }
                                }
                                return true;
                            } finally {
                                if (0 != 0) {
                                    try {
                                        reader.close();
                                    } catch (IOException e5) {
                                    }
                                }
                            }
                        }
                    } catch (SecurityException e6) {
                        PlatformDependent.logger.debug("Unable to check if {} exists", osReleaseFileName, e6);
                    }
                    return false;
                }
            })).booleanValue();
            if (found) {
                break;
            }
        }
        LINUX_OS_CLASSIFIERS = Collections.unmodifiableSet(availableClassifiers);
    }

    public static boolean hasDirectBufferNoCleanerConstructor() {
        return PlatformDependent0.hasDirectBufferNoCleanerConstructor();
    }

    public static byte[] allocateUninitializedArray(int size) {
        return (UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD < 0 || UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD > size) ? new byte[size] : PlatformDependent0.allocateUninitializedArray(size);
    }

    public static boolean isAndroid() {
        return PlatformDependent0.isAndroid();
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static boolean isOsx() {
        return IS_OSX;
    }

    public static boolean maybeSuperUser() {
        return MAYBE_SUPER_USER;
    }

    public static int javaVersion() {
        return PlatformDependent0.javaVersion();
    }

    public static boolean canEnableTcpNoDelayByDefault() {
        return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }

    public static boolean hasUnsafe() {
        return UNSAFE_UNAVAILABILITY_CAUSE == null;
    }

    public static Throwable getUnsafeUnavailabilityCause() {
        return UNSAFE_UNAVAILABILITY_CAUSE;
    }

    public static boolean isUnaligned() {
        return PlatformDependent0.isUnaligned();
    }

    public static boolean directBufferPreferred() {
        return DIRECT_BUFFER_PREFERRED;
    }

    public static long maxDirectMemory() {
        return DIRECT_MEMORY_LIMIT;
    }

    public static long usedDirectMemory() {
        if (DIRECT_MEMORY_COUNTER != null) {
            return DIRECT_MEMORY_COUNTER.get();
        }
        return -1L;
    }

    public static File tmpdir() {
        return TMPDIR;
    }

    public static int bitMode() {
        return BIT_MODE;
    }

    public static int addressSize() {
        return ADDRESS_SIZE;
    }

    public static long allocateMemory(long size) {
        return PlatformDependent0.allocateMemory(size);
    }

    public static void freeMemory(long address) {
        PlatformDependent0.freeMemory(address);
    }

    public static long reallocateMemory(long address, long newSize) {
        return PlatformDependent0.reallocateMemory(address, newSize);
    }

    public static void throwException(Throwable t) {
        if (hasUnsafe()) {
            PlatformDependent0.throwException(t);
        } else {
            throwException0(t);
        }
    }

    private static <E extends Throwable> void throwException0(Throwable t) throws Throwable {
        throw t;
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap();
    }

    public static LongCounter newLongCounter() {
        if (javaVersion() >= 8) {
            return new LongAdderCounter();
        }
        return new AtomicLongCounter();
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity) {
        return new ConcurrentHashMap(initialCapacity);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor) {
        return new ConcurrentHashMap(initialCapacity, loadFactor);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        return new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
        return new ConcurrentHashMap(map);
    }

    public static void freeDirectBuffer(ByteBuffer buffer) {
        CLEANER.freeDirectBuffer(buffer);
    }

    public static long directBufferAddress(ByteBuffer buffer) {
        return PlatformDependent0.directBufferAddress(buffer);
    }

    public static ByteBuffer directBuffer(long memoryAddress, int size) {
        if (PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            return PlatformDependent0.newDirectBuffer(memoryAddress, size);
        }
        throw new UnsupportedOperationException("sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available");
    }

    public static Object getObject(Object object, long fieldOffset) {
        return PlatformDependent0.getObject(object, fieldOffset);
    }

    public static int getInt(Object object, long fieldOffset) {
        return PlatformDependent0.getInt(object, fieldOffset);
    }

    public static byte getByte(long address) {
        return PlatformDependent0.getByte(address);
    }

    public static short getShort(long address) {
        return PlatformDependent0.getShort(address);
    }

    public static int getInt(long address) {
        return PlatformDependent0.getInt(address);
    }

    public static long getLong(long address) {
        return PlatformDependent0.getLong(address);
    }

    public static byte getByte(byte[] data, int index) {
        return PlatformDependent0.getByte(data, index);
    }

    public static byte getByte(byte[] data, long index) {
        return PlatformDependent0.getByte(data, index);
    }

    public static short getShort(byte[] data, int index) {
        return PlatformDependent0.getShort(data, index);
    }

    public static int getInt(byte[] data, int index) {
        return PlatformDependent0.getInt(data, index);
    }

    public static int getInt(int[] data, long index) {
        return PlatformDependent0.getInt(data, index);
    }

    public static long getLong(byte[] data, int index) {
        return PlatformDependent0.getLong(data, index);
    }

    public static long getLong(long[] data, long index) {
        return PlatformDependent0.getLong(data, index);
    }

    private static long getLongSafe(byte[] bytes, int offset) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (bytes[offset] << 56) | ((bytes[offset + 1] & 255) << 48) | ((bytes[offset + 2] & 255) << 40) | ((bytes[offset + 3] & 255) << 32) | ((bytes[offset + 4] & 255) << 24) | ((bytes[offset + 5] & 255) << 16) | ((bytes[offset + 6] & 255) << 8) | (bytes[offset + 7] & 255);
        }
        return (bytes[offset] & 255) | ((bytes[offset + 1] & 255) << 8) | ((bytes[offset + 2] & 255) << 16) | ((bytes[offset + 3] & 255) << 24) | ((bytes[offset + 4] & 255) << 32) | ((bytes[offset + 5] & 255) << 40) | ((bytes[offset + 6] & 255) << 48) | (bytes[offset + 7] << 56);
    }

    private static int getIntSafe(byte[] bytes, int offset) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (bytes[offset] << 24) | ((bytes[offset + 1] & 255) << 16) | ((bytes[offset + 2] & 255) << 8) | (bytes[offset + 3] & 255);
        }
        return (bytes[offset] & 255) | ((bytes[offset + 1] & 255) << 8) | ((bytes[offset + 2] & 255) << 16) | (bytes[offset + 3] << 24);
    }

    private static short getShortSafe(byte[] bytes, int offset) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (short) ((bytes[offset] << 8) | (bytes[offset + 1] & 255));
        }
        return (short) ((bytes[offset] & 255) | (bytes[offset + 1] << 8));
    }

    private static int hashCodeAsciiCompute(CharSequence value, int offset, int hash) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (hash * (-862048943)) + (hashCodeAsciiSanitizeInt(value, offset + 4) * 461845907) + hashCodeAsciiSanitizeInt(value, offset);
        }
        return (hash * (-862048943)) + (hashCodeAsciiSanitizeInt(value, offset) * 461845907) + hashCodeAsciiSanitizeInt(value, offset + 4);
    }

    private static int hashCodeAsciiSanitizeInt(CharSequence value, int offset) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 3) & 31) | ((value.charAt(offset + 2) & 31) << 8) | ((value.charAt(offset + 1) & 31) << 16) | ((value.charAt(offset) & 31) << 24);
        }
        return ((value.charAt(offset + 3) & 31) << 24) | ((value.charAt(offset + 2) & 31) << 16) | ((value.charAt(offset + 1) & 31) << 8) | (value.charAt(offset) & 31);
    }

    private static int hashCodeAsciiSanitizeShort(CharSequence value, int offset) {
        if (BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 1) & 31) | ((value.charAt(offset) & 31) << 8);
        }
        return ((value.charAt(offset + 1) & 31) << 8) | (value.charAt(offset) & 31);
    }

    private static int hashCodeAsciiSanitizeByte(char value) {
        return value & 31;
    }

    public static void putByte(long address, byte value) {
        PlatformDependent0.putByte(address, value);
    }

    public static void putShort(long address, short value) {
        PlatformDependent0.putShort(address, value);
    }

    public static void putInt(long address, int value) {
        PlatformDependent0.putInt(address, value);
    }

    public static void putLong(long address, long value) {
        PlatformDependent0.putLong(address, value);
    }

    public static void putByte(byte[] data, int index, byte value) {
        PlatformDependent0.putByte(data, index, value);
    }

    public static void putShort(byte[] data, int index, short value) {
        PlatformDependent0.putShort(data, index, value);
    }

    public static void putInt(byte[] data, int index, int value) {
        PlatformDependent0.putInt(data, index, value);
    }

    public static void putLong(byte[] data, int index, long value) {
        PlatformDependent0.putLong(data, index, value);
    }

    public static void putObject(Object o, long offset, Object x) {
        PlatformDependent0.putObject(o, offset, x);
    }

    public static long objectFieldOffset(Field field) {
        return PlatformDependent0.objectFieldOffset(field);
    }

    public static void copyMemory(long srcAddr, long dstAddr, long length) {
        PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
    }

    public static void copyMemory(byte[] src, int srcIndex, long dstAddr, long length) {
        PlatformDependent0.copyMemory(src, BYTE_ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
    }

    public static void copyMemory(long srcAddr, byte[] dst, int dstIndex, long length) {
        PlatformDependent0.copyMemory(null, srcAddr, dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, length);
    }

    public static void setMemory(byte[] dst, int dstIndex, long bytes, byte value) {
        PlatformDependent0.setMemory(dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, bytes, value);
    }

    public static void setMemory(long address, long bytes, byte value) {
        PlatformDependent0.setMemory(address, bytes, value);
    }

    public static ByteBuffer allocateDirectNoCleaner(int capacity) {
        if (!$assertionsDisabled && !USE_DIRECT_BUFFER_NO_CLEANER) {
            throw new AssertionError();
        }
        incrementMemoryCounter(capacity);
        try {
            return PlatformDependent0.allocateDirectNoCleaner(capacity);
        } catch (Throwable e) {
            decrementMemoryCounter(capacity);
            throwException(e);
            return null;
        }
    }

    public static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
        if (!$assertionsDisabled && !USE_DIRECT_BUFFER_NO_CLEANER) {
            throw new AssertionError();
        }
        int len = capacity - buffer.capacity();
        incrementMemoryCounter(len);
        try {
            return PlatformDependent0.reallocateDirectNoCleaner(buffer, capacity);
        } catch (Throwable e) {
            decrementMemoryCounter(len);
            throwException(e);
            return null;
        }
    }

    public static void freeDirectNoCleaner(ByteBuffer buffer) {
        if (!$assertionsDisabled && !USE_DIRECT_BUFFER_NO_CLEANER) {
            throw new AssertionError();
        }
        int capacity = buffer.capacity();
        PlatformDependent0.freeMemory(PlatformDependent0.directBufferAddress(buffer));
        decrementMemoryCounter(capacity);
    }

    private static void incrementMemoryCounter(int capacity) {
        if (DIRECT_MEMORY_COUNTER != null) {
            long newUsedMemory = DIRECT_MEMORY_COUNTER.addAndGet(capacity);
            if (newUsedMemory > DIRECT_MEMORY_LIMIT) {
                DIRECT_MEMORY_COUNTER.addAndGet(-capacity);
                throw new OutOfDirectMemoryError("failed to allocate " + capacity + " byte(s) of direct memory (used: " + (newUsedMemory - capacity) + ", max: " + DIRECT_MEMORY_LIMIT + ')');
            }
        }
    }

    private static void decrementMemoryCounter(int capacity) {
        if (DIRECT_MEMORY_COUNTER != null) {
            long usedMemory = DIRECT_MEMORY_COUNTER.addAndGet(-capacity);
            if (!$assertionsDisabled && usedMemory < 0) {
                throw new AssertionError();
            }
        }
    }

    public static boolean useDirectBufferNoCleaner() {
        return USE_DIRECT_BUFFER_NO_CLEANER;
    }

    public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        if (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) {
            return equalsSafe(bytes1, startPos1, bytes2, startPos2, length);
        }
        return PlatformDependent0.equals(bytes1, startPos1, bytes2, startPos2, length);
    }

    public static boolean isZero(byte[] bytes, int startPos, int length) {
        if (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) {
            return isZeroSafe(bytes, startPos, length);
        }
        return PlatformDependent0.isZero(bytes, startPos, length);
    }

    public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        if (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) {
            return ConstantTimeUtils.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
        }
        return PlatformDependent0.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
    }

    public static int hashCodeAscii(byte[] bytes, int startPos, int length) {
        if (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) {
            return hashCodeAsciiSafe(bytes, startPos, length);
        }
        return PlatformDependent0.hashCodeAscii(bytes, startPos, length);
    }

    public static int hashCodeAscii(CharSequence bytes) {
        int length = bytes.length();
        int remainingBytes = length & 7;
        int hash = -1028477387;
        if (length >= 32) {
            for (int i = length - 8; i >= remainingBytes; i -= 8) {
                hash = hashCodeAsciiCompute(bytes, i, hash);
            }
        } else if (length >= 8) {
            hash = hashCodeAsciiCompute(bytes, length - 8, -1028477387);
            if (length >= 16) {
                hash = hashCodeAsciiCompute(bytes, length - 16, hash);
                if (length >= 24) {
                    hash = hashCodeAsciiCompute(bytes, length - 24, hash);
                }
            }
        }
        if (remainingBytes == 0) {
            return hash;
        }
        int offset = 0;
        if ((remainingBytes != 2) & (remainingBytes != 4) & (remainingBytes != 6)) {
            hash = (hash * (-862048943)) + hashCodeAsciiSanitizeByte(bytes.charAt(0));
            offset = 1;
        }
        if ((remainingBytes != 1) & (remainingBytes != 4) & (remainingBytes != 5)) {
            hash = (hash * (offset == 0 ? -862048943 : 461845907)) + PlatformDependent0.hashCodeAsciiSanitize(hashCodeAsciiSanitizeShort(bytes, offset));
            offset += 2;
        }
        if (remainingBytes >= 4) {
            return (hash * ((offset == 0) | (offset == 3) ? -862048943 : 461845907)) + hashCodeAsciiSanitizeInt(bytes, offset);
        }
        return hash;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PlatformDependent$Mpsc.class */
    private static final class Mpsc {
        private static final boolean USE_MPSC_CHUNKED_ARRAY_QUEUE;

        private Mpsc() {
        }

        static {
            Object unsafe = null;
            if (PlatformDependent.hasUnsafe()) {
                unsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent.Mpsc.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        return UnsafeAccess.UNSAFE;
                    }
                });
            }
            if (unsafe == null) {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: unavailable");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = false;
            } else {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: available");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = true;
            }
        }

        static <T> Queue<T> newMpscQueue(int maxCapacity) {
            int capacity = Math.max(Math.min(maxCapacity, 1073741824), 2048);
            return USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscChunkedArrayQueue(1024, capacity) : new MpscGrowableAtomicArrayQueue(1024, capacity);
        }

        static <T> Queue<T> newMpscQueue() {
            return USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscUnboundedArrayQueue(1024) : new MpscUnboundedAtomicArrayQueue(1024);
        }
    }

    public static <T> Queue<T> newMpscQueue() {
        return Mpsc.newMpscQueue();
    }

    public static <T> Queue<T> newMpscQueue(int maxCapacity) {
        return Mpsc.newMpscQueue(maxCapacity);
    }

    public static <T> Queue<T> newSpscQueue() {
        return hasUnsafe() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue();
    }

    public static <T> Queue<T> newFixedMpscQueue(int capacity) {
        return hasUnsafe() ? new MpscArrayQueue(capacity) : new MpscAtomicArrayQueue(capacity);
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }

    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }

    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }

    @SuppressJava6Requirement(reason = "Usage guarded by java version check")
    public static <C> Deque<C> newConcurrentDeque() {
        if (javaVersion() < 7) {
            return new LinkedBlockingDeque();
        }
        return new ConcurrentLinkedDeque();
    }

    public static Random threadLocalRandom() {
        return RANDOM_PROVIDER.current();
    }

    private static boolean isWindows0() {
        boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (windows) {
            logger.debug("Platform: Windows");
        }
        return windows;
    }

    private static boolean isOsx0() {
        String osname = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        boolean osx = osname.startsWith("macosx") || osname.startsWith("osx");
        if (osx) {
            logger.debug("Platform: MacOS");
        }
        return osx;
    }

    private static boolean maybeSuperUser0() {
        String username = SystemPropertyUtil.get("user.name");
        if (isWindows()) {
            return "Administrator".equals(username);
        }
        return OgnlContext.ROOT_CONTEXT_KEY.equals(username) || "toor".equals(username);
    }

    private static Throwable unsafeUnavailabilityCause0() {
        if (isAndroid()) {
            logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (Android)");
        }
        if (isIkvmDotNet()) {
            logger.debug("sun.misc.Unsafe: unavailable (IKVM.NET)");
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (IKVM.NET)");
        }
        Throwable cause = PlatformDependent0.getUnsafeUnavailabilityCause();
        if (cause != null) {
            return cause;
        }
        try {
            boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
            if (hasUnsafe) {
                return null;
            }
            return PlatformDependent0.getUnsafeUnavailabilityCause();
        } catch (Throwable t) {
            logger.trace("Could not determine if Unsafe is available", t);
            return new UnsupportedOperationException("Could not determine if Unsafe is available", t);
        }
    }

    public static boolean isJ9Jvm() {
        return IS_J9_JVM;
    }

    private static boolean isJ9Jvm0() {
        String vmName = SystemPropertyUtil.get("java.vm.name", "").toLowerCase();
        return vmName.startsWith("ibm j9") || vmName.startsWith("eclipse openj9");
    }

    public static boolean isIkvmDotNet() {
        return IS_IVKVM_DOT_NET;
    }

    private static boolean isIkvmDotNet0() {
        String vmName = SystemPropertyUtil.get("java.vm.name", "").toUpperCase(Locale.US);
        return vmName.equals("IKVM.NET");
    }

    private static long maxDirectMemory0() {
        long maxDirectMemory = 0;
        ClassLoader systemClassLoader = null;
        try {
            systemClassLoader = getSystemClassLoader();
            String vmName = SystemPropertyUtil.get("java.vm.name", "").toLowerCase();
            if (!vmName.startsWith("ibm j9") && !vmName.startsWith("eclipse openj9")) {
                Class<?> vmClass = Class.forName("sun.misc.VM", true, systemClassLoader);
                maxDirectMemory = ((Number) vmClass.getDeclaredMethod("maxDirectMemory", new Class[0]).invoke(null, new Object[0])).longValue();
            }
        } catch (Throwable th) {
        }
        if (maxDirectMemory > 0) {
            return maxDirectMemory;
        }
        try {
            Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, systemClassLoader);
            Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, systemClassLoader);
            Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
            List<String> vmArgs = (List) runtimeClass.getDeclaredMethod("getInputArguments", new Class[0]).invoke(runtime, new Object[0]);
            int i = vmArgs.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher(vmArgs.get(i));
                if (!m.matches()) {
                    i--;
                } else {
                    maxDirectMemory = Long.parseLong(m.group(1));
                    switch (m.group(2).charAt(0)) {
                        case 'G':
                        case 'g':
                            maxDirectMemory *= 1073741824;
                            break;
                        case 'K':
                        case 'k':
                            maxDirectMemory *= 1024;
                            break;
                        case 'M':
                        case 'm':
                            maxDirectMemory *= 1048576;
                            break;
                    }
                }
            }
        } catch (Throwable th2) {
        }
        if (maxDirectMemory <= 0) {
            maxDirectMemory = Runtime.getRuntime().maxMemory();
            logger.debug("maxDirectMemory: {} bytes (maybe)", Long.valueOf(maxDirectMemory));
        } else {
            logger.debug("maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
        }
        return maxDirectMemory;
    }

    private static File tmpdir0() {
        File f;
        File f2;
        try {
            f2 = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
        } catch (Throwable th) {
        }
        if (f2 != null) {
            logger.debug("-Dio.netty.tmpdir: {}", f2);
            return f2;
        }
        File f3 = toDirectory(SystemPropertyUtil.get(TempFile.JAVA_IO_TMPDIR));
        if (f3 != null) {
            logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f3);
            return f3;
        }
        if (isWindows()) {
            File f4 = toDirectory(System.getenv("TEMP"));
            if (f4 != null) {
                logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f4);
                return f4;
            }
            String userprofile = System.getenv("USERPROFILE");
            if (userprofile != null) {
                File f5 = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                if (f5 != null) {
                    logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f5);
                    return f5;
                }
                File f6 = toDirectory(userprofile + "\\Local Settings\\Temp");
                if (f6 != null) {
                    logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f6);
                    return f6;
                }
            }
        } else {
            File f7 = toDirectory(System.getenv("TMPDIR"));
            if (f7 != null) {
                logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f7);
                return f7;
            }
        }
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        } else {
            f = new File("/tmp");
        }
        logger.warn("Failed to get the temporary directory; falling back to: {}", f);
        return f;
    }

    private static File toDirectory(String path) {
        if (path == null) {
            return null;
        }
        File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        } catch (Exception e) {
            return f;
        }
    }

    private static int bitMode0() {
        int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (bitMode > 0) {
            logger.debug("-Dio.netty.bitMode: {}", Integer.valueOf(bitMode));
            return bitMode;
        }
        int bitMode2 = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (bitMode2 > 0) {
            logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", Integer.valueOf(bitMode2));
            return bitMode2;
        }
        int bitMode3 = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (bitMode3 > 0) {
            logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", Integer.valueOf(bitMode3));
            return bitMode3;
        }
        String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if ("amd64".equals(arch) || "x86_64".equals(arch)) {
            bitMode3 = 64;
        } else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
            bitMode3 = 32;
        }
        if (bitMode3 > 0) {
            logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(bitMode3), arch);
        }
        String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        Pattern bitPattern = Pattern.compile("([1-9][0-9]+)-?bit");
        Matcher m = bitPattern.matcher(vm);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 64;
    }

    private static int addressSize0() {
        if (!hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }

    private static long byteArrayBaseOffset0() {
        if (!hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.byteArrayBaseOffset();
    }

    private static boolean equalsSafe(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        int end = startPos1 + length;
        while (startPos1 < end) {
            if (bytes1[startPos1] == bytes2[startPos2]) {
                startPos1++;
                startPos2++;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean isZeroSafe(byte[] bytes, int startPos, int length) {
        int end = startPos + length;
        while (startPos < end) {
            if (bytes[startPos] == 0) {
                startPos++;
            } else {
                return false;
            }
        }
        return true;
    }

    static int hashCodeAsciiSafe(byte[] bytes, int startPos, int length) {
        int hash = -1028477387;
        int remainingBytes = length & 7;
        int end = startPos + remainingBytes;
        for (int i = (startPos - 8) + length; i >= end; i -= 8) {
            hash = PlatformDependent0.hashCodeAsciiCompute(getLongSafe(bytes, i), hash);
        }
        switch (remainingBytes) {
            case 1:
                return (hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos]);
            case 2:
                return (hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos));
            case 3:
                return (((hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907) + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1));
            case 4:
                return (hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos));
            case 5:
                return (((hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907) + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 1));
            case 6:
                return (((hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos))) * 461845907) + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 2));
            case 7:
                return (((((hash * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907) + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1))) * (-862048943)) + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 3));
            default:
                return hash;
        }
    }

    public static String normalizedArch() {
        return NORMALIZED_ARCH;
    }

    public static String normalizedOs() {
        return NORMALIZED_OS;
    }

    public static Set<String> normalizedLinuxClassifiers() {
        return LINUX_OS_CLASSIFIERS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addClassifier(Set<String> allowed, Set<String> dest, String... maybeClassifiers) {
        for (String id : maybeClassifiers) {
            if (allowed.contains(id)) {
                dest.add(id);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String normalizeOsReleaseVariableValue(String value) {
        return value.trim().replaceAll("[\"']", "");
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }

    private static String normalizeArch(String value) {
        String value2 = normalize(value);
        if (value2.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (value2.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (value2.matches("^(ia64|itanium64)$")) {
            return "itanium_64";
        }
        if (value2.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (value2.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (value2.matches("^(arm|arm32)$")) {
            return "arm_32";
        }
        if ("aarch64".equals(value2)) {
            return "aarch_64";
        }
        if (value2.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if ("ppc64".equals(value2)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(value2)) {
            return "ppcle_64";
        }
        if ("s390".equals(value2)) {
            return "s390_32";
        }
        if ("s390x".equals(value2)) {
            return "s390_64";
        }
        return "unknown";
    }

    private static String normalizeOs(String value) {
        String value2 = normalize(value);
        if (value2.startsWith("aix")) {
            return "aix";
        }
        if (value2.startsWith("hpux")) {
            return "hpux";
        }
        if (value2.startsWith("os400") && (value2.length() <= 5 || !Character.isDigit(value2.charAt(5)))) {
            return "os400";
        }
        if (value2.startsWith("linux")) {
            return "linux";
        }
        if (value2.startsWith("macosx") || value2.startsWith("osx")) {
            return "osx";
        }
        if (value2.startsWith("freebsd")) {
            return "freebsd";
        }
        if (value2.startsWith("openbsd")) {
            return "openbsd";
        }
        if (value2.startsWith("netbsd")) {
            return "netbsd";
        }
        if (value2.startsWith("solaris") || value2.startsWith("sunos")) {
            return "sunos";
        }
        if (value2.startsWith("windows")) {
            return "windows";
        }
        return "unknown";
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PlatformDependent$AtomicLongCounter.class */
    private static final class AtomicLongCounter extends AtomicLong implements LongCounter {
        private static final long serialVersionUID = 4074772784610639305L;

        private AtomicLongCounter() {
        }

        @Override // io.netty.util.internal.LongCounter
        public void add(long delta) {
            addAndGet(delta);
        }

        @Override // io.netty.util.internal.LongCounter
        public void increment() {
            incrementAndGet();
        }

        @Override // io.netty.util.internal.LongCounter
        public void decrement() {
            decrementAndGet();
        }

        @Override // io.netty.util.internal.LongCounter
        public long value() {
            return get();
        }
    }

    private PlatformDependent() {
    }
}
