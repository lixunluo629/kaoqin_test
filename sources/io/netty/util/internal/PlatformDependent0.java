package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.springframework.objenesis.strategy.PlatformDescription;
import sun.misc.Unsafe;

@SuppressJava6Requirement(reason = "Unsafe access is guarded")
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PlatformDependent0.class */
final class PlatformDependent0 {
    private static final InternalLogger logger;
    private static final long ADDRESS_FIELD_OFFSET;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final long INT_ARRAY_BASE_OFFSET;
    private static final long INT_ARRAY_INDEX_SCALE;
    private static final long LONG_ARRAY_BASE_OFFSET;
    private static final long LONG_ARRAY_INDEX_SCALE;
    private static final Constructor<?> DIRECT_BUFFER_CONSTRUCTOR;
    private static final Throwable EXPLICIT_NO_UNSAFE_CAUSE;
    private static final Method ALLOCATE_ARRAY_METHOD;
    private static final int JAVA_VERSION;
    private static final boolean IS_ANDROID;
    private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
    private static final Object INTERNAL_UNSAFE;
    private static final boolean IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
    static final Unsafe UNSAFE;
    static final int HASH_CODE_ASCII_SEED = -1028477387;
    static final int HASH_CODE_C1 = -862048943;
    static final int HASH_CODE_C2 = 461845907;
    private static final long UNSAFE_COPY_THRESHOLD = 1048576;
    private static final boolean UNALIGNED;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        final ByteBuffer direct;
        Unsafe unsafe;
        Constructor<?> directBufferConstructor;
        boolean unaligned;
        $assertionsDisabled = !PlatformDependent0.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) PlatformDependent0.class);
        EXPLICIT_NO_UNSAFE_CAUSE = explicitNoUnsafeCause0();
        JAVA_VERSION = javaVersion0();
        IS_ANDROID = isAndroid0();
        IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE = explicitTryReflectionSetAccessible0();
        Field addressField = null;
        Method allocateArrayMethod = null;
        final Object internalUnsafe = null;
        Throwable th = EXPLICIT_NO_UNSAFE_CAUSE;
        Throwable unsafeUnavailabilityCause = th;
        if (th != null) {
            direct = null;
            addressField = null;
            unsafe = null;
            internalUnsafe = null;
        } else {
            direct = ByteBuffer.allocateDirect(1);
            Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.1
                @Override // java.security.PrivilegedAction
                public Object run() throws NoSuchFieldException {
                    try {
                        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                        Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
                        if (cause != null) {
                            return cause;
                        }
                        return unsafeField.get(null);
                    } catch (IllegalAccessException e) {
                        return e;
                    } catch (NoClassDefFoundError e2) {
                        return e2;
                    } catch (NoSuchFieldException e3) {
                        return e3;
                    } catch (SecurityException e4) {
                        return e4;
                    }
                }
            });
            if (maybeUnsafe instanceof Throwable) {
                unsafe = null;
                unsafeUnavailabilityCause = (Throwable) maybeUnsafe;
                logger.debug("sun.misc.Unsafe.theUnsafe: unavailable", (Throwable) maybeUnsafe);
            } else {
                unsafe = (Unsafe) maybeUnsafe;
                logger.debug("sun.misc.Unsafe.theUnsafe: available");
            }
            if (unsafe != null) {
                final Unsafe finalUnsafe = unsafe;
                Object maybeException = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.2
                    @Override // java.security.PrivilegedAction
                    public Object run() throws NoSuchMethodException, SecurityException {
                        try {
                            finalUnsafe.getClass().getDeclaredMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
                            return null;
                        } catch (NoSuchMethodException e) {
                            return e;
                        } catch (SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeException == null) {
                    logger.debug("sun.misc.Unsafe.copyMemory: available");
                } else {
                    unsafe = null;
                    unsafeUnavailabilityCause = (Throwable) maybeException;
                    logger.debug("sun.misc.Unsafe.copyMemory: unavailable", (Throwable) maybeException);
                }
            }
            if (unsafe != null) {
                final Unsafe finalUnsafe2 = unsafe;
                Object maybeAddressField = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.3
                    @Override // java.security.PrivilegedAction
                    public Object run() throws NoSuchFieldException {
                        try {
                            Field field = Buffer.class.getDeclaredField("address");
                            long offset = finalUnsafe2.objectFieldOffset(field);
                            long address = finalUnsafe2.getLong(direct, offset);
                            if (address == 0) {
                                return null;
                            }
                            return field;
                        } catch (NoSuchFieldException e) {
                            return e;
                        } catch (SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeAddressField instanceof Field) {
                    addressField = (Field) maybeAddressField;
                    logger.debug("java.nio.Buffer.address: available");
                } else {
                    unsafeUnavailabilityCause = (Throwable) maybeAddressField;
                    logger.debug("java.nio.Buffer.address: unavailable", (Throwable) maybeAddressField);
                    unsafe = null;
                }
            }
            if (unsafe != null) {
                long byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
                if (byteArrayIndexScale != 1) {
                    logger.debug("unsafe.arrayIndexScale is {} (expected: 1). Not using unsafe.", Long.valueOf(byteArrayIndexScale));
                    unsafeUnavailabilityCause = new UnsupportedOperationException("Unexpected unsafe.arrayIndexScale");
                    unsafe = null;
                }
            }
        }
        UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause;
        UNSAFE = unsafe;
        if (unsafe == null) {
            ADDRESS_FIELD_OFFSET = -1L;
            BYTE_ARRAY_BASE_OFFSET = -1L;
            LONG_ARRAY_BASE_OFFSET = -1L;
            LONG_ARRAY_INDEX_SCALE = -1L;
            INT_ARRAY_BASE_OFFSET = -1L;
            INT_ARRAY_INDEX_SCALE = -1L;
            UNALIGNED = false;
            DIRECT_BUFFER_CONSTRUCTOR = null;
            ALLOCATE_ARRAY_METHOD = null;
        } else {
            long address = -1;
            try {
                final ByteBuffer byteBuffer = direct;
                Object maybeDirectBufferConstructor = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.4
                    @Override // java.security.PrivilegedAction
                    public Object run() throws NoSuchMethodException, SecurityException {
                        try {
                            Constructor<?> constructor = byteBuffer.getClass().getDeclaredConstructor(Long.TYPE, Integer.TYPE);
                            Throwable cause = ReflectionUtil.trySetAccessible(constructor, true);
                            if (cause != null) {
                                return cause;
                            }
                            return constructor;
                        } catch (NoSuchMethodException e) {
                            return e;
                        } catch (SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeDirectBufferConstructor instanceof Constructor) {
                    address = UNSAFE.allocateMemory(1L);
                    try {
                        ((Constructor) maybeDirectBufferConstructor).newInstance(Long.valueOf(address), 1);
                        directBufferConstructor = (Constructor) maybeDirectBufferConstructor;
                        logger.debug("direct buffer constructor: available");
                    } catch (IllegalAccessException e) {
                        directBufferConstructor = null;
                    } catch (InstantiationException e2) {
                        directBufferConstructor = null;
                    } catch (InvocationTargetException e3) {
                        directBufferConstructor = null;
                    }
                } else {
                    logger.debug("direct buffer constructor: unavailable", (Throwable) maybeDirectBufferConstructor);
                    directBufferConstructor = null;
                }
                if (address != -1) {
                    UNSAFE.freeMemory(address);
                }
                DIRECT_BUFFER_CONSTRUCTOR = directBufferConstructor;
                ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
                BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
                INT_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
                INT_ARRAY_INDEX_SCALE = UNSAFE.arrayIndexScale(int[].class);
                LONG_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
                LONG_ARRAY_INDEX_SCALE = UNSAFE.arrayIndexScale(long[].class);
                Object maybeUnaligned = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.5
                    @Override // java.security.PrivilegedAction
                    public Object run() throws NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, SecurityException {
                        try {
                            Class<?> bitsClass = Class.forName("java.nio.Bits", false, PlatformDependent0.getSystemClassLoader());
                            int version = PlatformDependent0.javaVersion();
                            if (version >= 9) {
                                String fieldName = version >= 11 ? "UNALIGNED" : "unaligned";
                                try {
                                    Field unalignedField = bitsClass.getDeclaredField(fieldName);
                                    if (unalignedField.getType() == Boolean.TYPE) {
                                        long offset = PlatformDependent0.UNSAFE.staticFieldOffset(unalignedField);
                                        Object object = PlatformDependent0.UNSAFE.staticFieldBase(unalignedField);
                                        return Boolean.valueOf(PlatformDependent0.UNSAFE.getBoolean(object, offset));
                                    }
                                } catch (NoSuchFieldException e4) {
                                }
                            }
                            Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", new Class[0]);
                            Throwable cause = ReflectionUtil.trySetAccessible(unalignedMethod, true);
                            if (cause != null) {
                                return cause;
                            }
                            return unalignedMethod.invoke(null, new Object[0]);
                        } catch (ClassNotFoundException e5) {
                            return e5;
                        } catch (IllegalAccessException e6) {
                            return e6;
                        } catch (NoSuchMethodException e7) {
                            return e7;
                        } catch (SecurityException e8) {
                            return e8;
                        } catch (InvocationTargetException e9) {
                            return e9;
                        }
                    }
                });
                if (maybeUnaligned instanceof Boolean) {
                    unaligned = ((Boolean) maybeUnaligned).booleanValue();
                    logger.debug("java.nio.Bits.unaligned: available, {}", Boolean.valueOf(unaligned));
                } else {
                    String arch = SystemPropertyUtil.get("os.arch", "");
                    unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
                    Throwable t = (Throwable) maybeUnaligned;
                    logger.debug("java.nio.Bits.unaligned: unavailable {}", Boolean.valueOf(unaligned), t);
                }
                UNALIGNED = unaligned;
                if (javaVersion() >= 9) {
                    Object maybeException2 = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.6
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            try {
                                Class<?> internalUnsafeClass = PlatformDependent0.getClassLoader(PlatformDependent0.class).loadClass("jdk.internal.misc.Unsafe");
                                Method method = internalUnsafeClass.getDeclaredMethod("getUnsafe", new Class[0]);
                                return method.invoke(null, new Object[0]);
                            } catch (Throwable e4) {
                                return e4;
                            }
                        }
                    });
                    if (!(maybeException2 instanceof Throwable)) {
                        internalUnsafe = maybeException2;
                        maybeException2 = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.util.internal.PlatformDependent0.7
                            @Override // java.security.PrivilegedAction
                            public Object run() {
                                try {
                                    return internalUnsafe.getClass().getDeclaredMethod("allocateUninitializedArray", Class.class, Integer.TYPE);
                                } catch (NoSuchMethodException e4) {
                                    return e4;
                                } catch (SecurityException e5) {
                                    return e5;
                                }
                            }
                        });
                        if (maybeException2 instanceof Method) {
                            try {
                                Method m = (Method) maybeException2;
                                byte[] bytes = (byte[]) m.invoke(internalUnsafe, Byte.TYPE, 8);
                                if (!$assertionsDisabled && bytes.length != 8) {
                                    throw new AssertionError();
                                }
                                allocateArrayMethod = m;
                            } catch (IllegalAccessException e4) {
                                maybeException2 = e4;
                            } catch (InvocationTargetException e5) {
                                maybeException2 = e5;
                            }
                        }
                    }
                    if (maybeException2 instanceof Throwable) {
                        logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable", (Throwable) maybeException2);
                    } else {
                        logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): available");
                    }
                } else {
                    logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable prior to Java9");
                }
                ALLOCATE_ARRAY_METHOD = allocateArrayMethod;
            } catch (Throwable th2) {
                if (-1 != -1) {
                    UNSAFE.freeMemory(-1L);
                }
                throw th2;
            }
        }
        INTERNAL_UNSAFE = internalUnsafe;
        logger.debug("java.nio.DirectByteBuffer.<init>(long, int): {}", DIRECT_BUFFER_CONSTRUCTOR != null ? "available" : "unavailable");
    }

    static boolean isExplicitNoUnsafe() {
        return EXPLICIT_NO_UNSAFE_CAUSE != null;
    }

    private static Throwable explicitNoUnsafeCause0() {
        String unsafePropName;
        boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(noUnsafe));
        if (noUnsafe) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
        }
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            unsafePropName = "io.netty.tryUnsafe";
        } else {
            unsafePropName = "org.jboss.netty.tryUnsafe";
        }
        if (!SystemPropertyUtil.getBoolean(unsafePropName, true)) {
            String msg = "sun.misc.Unsafe: unavailable (" + unsafePropName + ")";
            logger.debug(msg);
            return new UnsupportedOperationException(msg);
        }
        return null;
    }

    static boolean isUnaligned() {
        return UNALIGNED;
    }

    static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    static Throwable getUnsafeUnavailabilityCause() {
        return UNSAFE_UNAVAILABILITY_CAUSE;
    }

    static boolean unalignedAccess() {
        return UNALIGNED;
    }

    static void throwException(Throwable cause) {
        UNSAFE.throwException((Throwable) ObjectUtil.checkNotNull(cause, "cause"));
    }

    static boolean hasDirectBufferNoCleanerConstructor() {
        return DIRECT_BUFFER_CONSTRUCTOR != null;
    }

    static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
        return newDirectBuffer(UNSAFE.reallocateMemory(directBufferAddress(buffer), capacity), capacity);
    }

    static ByteBuffer allocateDirectNoCleaner(int capacity) {
        return newDirectBuffer(UNSAFE.allocateMemory(Math.max(1, capacity)), capacity);
    }

    static boolean hasAllocateArrayMethod() {
        return ALLOCATE_ARRAY_METHOD != null;
    }

    static byte[] allocateUninitializedArray(int size) {
        try {
            return (byte[]) ALLOCATE_ARRAY_METHOD.invoke(INTERNAL_UNSAFE, Byte.TYPE, Integer.valueOf(size));
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e2) {
            throw new Error(e2);
        }
    }

    static ByteBuffer newDirectBuffer(long address, int capacity) {
        ObjectUtil.checkPositiveOrZero(capacity, "capacity");
        try {
            return (ByteBuffer) DIRECT_BUFFER_CONSTRUCTOR.newInstance(Long.valueOf(address), Integer.valueOf(capacity));
        } catch (Throwable cause) {
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new Error(cause);
        }
    }

    static long directBufferAddress(ByteBuffer buffer) {
        return getLong(buffer, ADDRESS_FIELD_OFFSET);
    }

    static long byteArrayBaseOffset() {
        return BYTE_ARRAY_BASE_OFFSET;
    }

    static Object getObject(Object object, long fieldOffset) {
        return UNSAFE.getObject(object, fieldOffset);
    }

    static int getInt(Object object, long fieldOffset) {
        return UNSAFE.getInt(object, fieldOffset);
    }

    private static long getLong(Object object, long fieldOffset) {
        return UNSAFE.getLong(object, fieldOffset);
    }

    static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    static byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    static byte getByte(byte[] data, int index) {
        return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    static byte getByte(byte[] data, long index) {
        return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    static short getShort(byte[] data, int index) {
        return UNSAFE.getShort(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    static int getInt(byte[] data, int index) {
        return UNSAFE.getInt(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    static int getInt(int[] data, long index) {
        return UNSAFE.getInt(data, INT_ARRAY_BASE_OFFSET + (INT_ARRAY_INDEX_SCALE * index));
    }

    static long getLong(byte[] data, int index) {
        return UNSAFE.getLong(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    static long getLong(long[] data, long index) {
        return UNSAFE.getLong(data, LONG_ARRAY_BASE_OFFSET + (LONG_ARRAY_INDEX_SCALE * index));
    }

    static void putByte(long address, byte value) {
        UNSAFE.putByte(address, value);
    }

    static void putShort(long address, short value) {
        UNSAFE.putShort(address, value);
    }

    static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    static void putByte(byte[] data, int index, byte value) {
        UNSAFE.putByte(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    static void putShort(byte[] data, int index, short value) {
        UNSAFE.putShort(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    static void putInt(byte[] data, int index, int value) {
        UNSAFE.putInt(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    static void putLong(byte[] data, int index, long value) {
        UNSAFE.putLong(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    static void putObject(Object o, long offset, Object x) {
        UNSAFE.putObject(o, offset, x);
    }

    static void copyMemory(long srcAddr, long dstAddr, long length) {
        if (javaVersion() <= 8) {
            copyMemoryWithSafePointPolling(srcAddr, dstAddr, length);
        } else {
            UNSAFE.copyMemory(srcAddr, dstAddr, length);
        }
    }

    private static void copyMemoryWithSafePointPolling(long srcAddr, long dstAddr, long length) {
        while (length > 0) {
            long size = Math.min(length, 1048576L);
            UNSAFE.copyMemory(srcAddr, dstAddr, size);
            length -= size;
            srcAddr += size;
            dstAddr += size;
        }
    }

    static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length) {
        if (javaVersion() <= 8) {
            copyMemoryWithSafePointPolling(src, srcOffset, dst, dstOffset, length);
        } else {
            UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, length);
        }
    }

    private static void copyMemoryWithSafePointPolling(Object src, long srcOffset, Object dst, long dstOffset, long length) {
        while (length > 0) {
            long size = Math.min(length, 1048576L);
            UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
            length -= size;
            srcOffset += size;
            dstOffset += size;
        }
    }

    static void setMemory(long address, long bytes, byte value) {
        UNSAFE.setMemory(address, bytes, value);
    }

    static void setMemory(Object o, long offset, long bytes, byte value) {
        UNSAFE.setMemory(o, offset, bytes, value);
    }

    static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        int remainingBytes = length & 7;
        long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
        long diff = startPos2 - startPos1;
        if (length >= 8) {
            long end = baseOffset1 + remainingBytes;
            long j = (baseOffset1 - 8) + length;
            while (true) {
                long i = j;
                if (i < end) {
                    break;
                }
                if (UNSAFE.getLong(bytes1, i) == UNSAFE.getLong(bytes2, i + diff)) {
                    j = i - 8;
                } else {
                    return false;
                }
            }
        }
        if (remainingBytes >= 4) {
            remainingBytes -= 4;
            long pos = baseOffset1 + remainingBytes;
            if (UNSAFE.getInt(bytes1, pos) != UNSAFE.getInt(bytes2, pos + diff)) {
                return false;
            }
        }
        long baseOffset2 = baseOffset1 + diff;
        return remainingBytes >= 2 ? UNSAFE.getChar(bytes1, baseOffset1) == UNSAFE.getChar(bytes2, baseOffset2) && (remainingBytes == 2 || UNSAFE.getByte(bytes1, baseOffset1 + 2) == UNSAFE.getByte(bytes2, baseOffset2 + 2)) : remainingBytes == 0 || UNSAFE.getByte(bytes1, baseOffset1) == UNSAFE.getByte(bytes2, baseOffset2);
    }

    static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        long result = 0;
        long remainingBytes = length & 7;
        long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
        long end = baseOffset1 + remainingBytes;
        long diff = startPos2 - startPos1;
        long j = (baseOffset1 - 8) + length;
        while (true) {
            long i = j;
            if (i < end) {
                break;
            }
            result |= UNSAFE.getLong(bytes1, i) ^ UNSAFE.getLong(bytes2, i + diff);
            j = i - 8;
        }
        if (remainingBytes >= 4) {
            result |= UNSAFE.getInt(bytes1, baseOffset1) ^ UNSAFE.getInt(bytes2, baseOffset1 + diff);
            remainingBytes -= 4;
        }
        if (remainingBytes >= 2) {
            long pos = end - remainingBytes;
            result |= UNSAFE.getChar(bytes1, pos) ^ UNSAFE.getChar(bytes2, pos + diff);
            remainingBytes -= 2;
        }
        if (remainingBytes == 1) {
            long pos2 = end - 1;
            result |= UNSAFE.getByte(bytes1, pos2) ^ UNSAFE.getByte(bytes2, pos2 + diff);
        }
        return ConstantTimeUtils.equalsConstantTime(result, 0L);
    }

    static boolean isZero(byte[] bytes, int startPos, int length) {
        if (length <= 0) {
            return true;
        }
        long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
        int remainingBytes = length & 7;
        long end = baseOffset + remainingBytes;
        long j = (baseOffset - 8) + length;
        while (true) {
            long i = j;
            if (i < end) {
                if (remainingBytes >= 4) {
                    remainingBytes -= 4;
                    if (UNSAFE.getInt(bytes, baseOffset + remainingBytes) != 0) {
                        return false;
                    }
                }
                return remainingBytes >= 2 ? UNSAFE.getChar(bytes, baseOffset) == 0 && (remainingBytes == 2 || bytes[startPos + 2] == 0) : bytes[startPos] == 0;
            }
            if (UNSAFE.getLong(bytes, i) == 0) {
                j = i - 8;
            } else {
                return false;
            }
        }
    }

    static int hashCodeAscii(byte[] bytes, int startPos, int length) {
        int hash = HASH_CODE_ASCII_SEED;
        long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
        int remainingBytes = length & 7;
        long end = baseOffset + remainingBytes;
        long j = (baseOffset - 8) + length;
        while (true) {
            long i = j;
            if (i < end) {
                break;
            }
            hash = hashCodeAsciiCompute(UNSAFE.getLong(bytes, i), hash);
            j = i - 8;
        }
        if (remainingBytes == 0) {
            return hash;
        }
        int hcConst = HASH_CODE_C1;
        if ((remainingBytes != 2) & (remainingBytes != 4) & (remainingBytes != 6)) {
            hash = (hash * HASH_CODE_C1) + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset));
            hcConst = HASH_CODE_C2;
            baseOffset++;
        }
        if ((remainingBytes != 1) & (remainingBytes != 4) & (remainingBytes != 5)) {
            hash = (hash * hcConst) + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset));
            hcConst = hcConst == HASH_CODE_C1 ? HASH_CODE_C2 : HASH_CODE_C1;
            baseOffset += 2;
        }
        if (remainingBytes >= 4) {
            return (hash * hcConst) + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset));
        }
        return hash;
    }

    static int hashCodeAsciiCompute(long value, int hash) {
        return (hash * HASH_CODE_C1) + (hashCodeAsciiSanitize((int) value) * HASH_CODE_C2) + ((int) ((value & 2242545357458243584L) >>> 32));
    }

    static int hashCodeAsciiSanitize(int value) {
        return value & 522133279;
    }

    static int hashCodeAsciiSanitize(short value) {
        return value & 7967;
    }

    static int hashCodeAsciiSanitize(byte value) {
        return value & 31;
    }

    static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    static int addressSize() {
        return UNSAFE.addressSize();
    }

    static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    static void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }

    static long reallocateMemory(long address, long newSize) {
        return UNSAFE.reallocateMemory(address, newSize);
    }

    static boolean isAndroid() {
        return IS_ANDROID;
    }

    private static boolean isAndroid0() {
        String vmName = SystemPropertyUtil.get("java.vm.name");
        boolean isAndroid = PlatformDescription.DALVIK.equals(vmName);
        if (isAndroid) {
            logger.debug("Platform: Android");
        }
        return isAndroid;
    }

    private static boolean explicitTryReflectionSetAccessible0() {
        return SystemPropertyUtil.getBoolean("io.netty.tryReflectionSetAccessible", javaVersion() < 9);
    }

    static boolean isExplicitTryReflectionSetAccessible() {
        return IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
    }

    static int javaVersion() {
        return JAVA_VERSION;
    }

    private static int javaVersion0() {
        int majorVersion;
        if (isAndroid0()) {
            majorVersion = 6;
        } else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }
        logger.debug("Java version: {}", Integer.valueOf(majorVersion));
        return majorVersion;
    }

    static int majorVersionFromJavaSpecificationVersion() {
        return majorVersion(SystemPropertyUtil.get("java.specification.version", "1.6"));
    }

    static int majorVersion(String javaSpecVersion) {
        String[] components = javaSpecVersion.split("\\.");
        int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            version[i] = Integer.parseInt(components[i]);
        }
        if (version[0] == 1) {
            if ($assertionsDisabled || version[1] >= 6) {
                return version[1];
            }
            throw new AssertionError();
        }
        return version[0];
    }

    private PlatformDependent0() {
    }
}
