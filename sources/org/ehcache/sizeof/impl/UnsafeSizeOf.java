package org.ehcache.sizeof.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.ehcache.sizeof.SizeOf;
import org.ehcache.sizeof.filters.SizeOfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/UnsafeSizeOf.class */
public class UnsafeSizeOf extends SizeOf {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) UnsafeSizeOf.class);
    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Throwable th) {
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    public UnsafeSizeOf() throws UnsupportedOperationException {
        this(new PassThroughFilter());
    }

    public UnsafeSizeOf(SizeOfFilter filter) throws UnsupportedOperationException {
        this(filter, true, true);
    }

    public UnsafeSizeOf(SizeOfFilter filter, boolean caching, boolean bypassFlyweight) throws UnsupportedOperationException {
        super(filter, caching, bypassFlyweight);
        if (UNSAFE == null) {
            throw new UnsupportedOperationException("sun.misc.Unsafe instance not accessible");
        }
        if (!JvmInformation.CURRENT_JVM_INFORMATION.supportsUnsafeSizeOf()) {
            LOGGER.warn("UnsafeSizeOf is not always accurate on the JVM (" + JvmInformation.CURRENT_JVM_INFORMATION.getJvmDescription() + ").  Please consider enabling AgentSizeOf.");
        }
    }

    @Override // org.ehcache.sizeof.SizeOf
    public long sizeOf(Object obj) {
        if (obj.getClass().isArray()) {
            Class<?> klazz = obj.getClass();
            int base = UNSAFE.arrayBaseOffset(klazz);
            int scale = UNSAFE.arrayIndexScale(klazz);
            long size = base + (scale * Array.getLength(obj)) + JvmInformation.CURRENT_JVM_INFORMATION.getFieldOffsetAdjustment();
            if (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() != 0) {
                size += JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() - (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment());
            }
            return Math.max(JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize(), size);
        }
        Class<?> superclass = obj.getClass();
        while (true) {
            Class<?> klazz2 = superclass;
            if (klazz2 != null) {
                long lastFieldOffset = -1;
                Field[] arr$ = klazz2.getDeclaredFields();
                for (Field f : arr$) {
                    if (!Modifier.isStatic(f.getModifiers())) {
                        lastFieldOffset = Math.max(lastFieldOffset, UNSAFE.objectFieldOffset(f));
                    }
                }
                if (lastFieldOffset <= 0) {
                    superclass = klazz2.getSuperclass();
                } else {
                    long lastFieldOffset2 = lastFieldOffset + JvmInformation.CURRENT_JVM_INFORMATION.getFieldOffsetAdjustment() + 1;
                    if (lastFieldOffset2 % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() != 0) {
                        lastFieldOffset2 += JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() - (lastFieldOffset2 % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment());
                    }
                    return Math.max(JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize(), lastFieldOffset2);
                }
            } else {
                long size2 = JvmInformation.CURRENT_JVM_INFORMATION.getObjectHeaderSize();
                if (size2 % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() != 0) {
                    size2 += JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() - (size2 % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment());
                }
                return Math.max(JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize(), size2);
            }
        }
    }
}
