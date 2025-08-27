package org.ehcache.sizeof.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Stack;
import org.ehcache.sizeof.SizeOf;
import org.ehcache.sizeof.filters.SizeOfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/ReflectionSizeOf.class */
public class ReflectionSizeOf extends SizeOf {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ReflectionSizeOf.class);

    public ReflectionSizeOf() {
        this(new PassThroughFilter());
    }

    public ReflectionSizeOf(SizeOfFilter fieldFilter) {
        this(fieldFilter, true, true);
    }

    public ReflectionSizeOf(SizeOfFilter fieldFilter, boolean caching, boolean bypassFlyweight) {
        super(fieldFilter, caching, bypassFlyweight);
        if (!JvmInformation.CURRENT_JVM_INFORMATION.supportsReflectionSizeOf()) {
            LOGGER.warn("ReflectionSizeOf is not always accurate on the JVM (" + JvmInformation.CURRENT_JVM_INFORMATION.getJvmDescription() + ").  Please consider enabling AgentSizeOf.");
        }
    }

    @Override // org.ehcache.sizeof.SizeOf
    public long sizeOf(Object obj) {
        if (obj == null) {
            return 0L;
        }
        Class<?> aClass = obj.getClass();
        if (aClass.isArray()) {
            return guessArraySize(obj);
        }
        long size = JvmInformation.CURRENT_JVM_INFORMATION.getObjectHeaderSize();
        Stack<Class<?>> classStack = new Stack<>();
        Class<?> superclass = aClass;
        while (true) {
            Class<?> klazz = superclass;
            if (klazz == null) {
                break;
            }
            classStack.push(klazz);
            superclass = klazz.getSuperclass();
        }
        while (!classStack.isEmpty()) {
            int oops = 0;
            int doubles = 0;
            int words = 0;
            int shorts = 0;
            int bytes = 0;
            Field[] arr$ = classStack.pop().getDeclaredFields();
            for (Field f : arr$) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    if (f.getType().isPrimitive()) {
                        switch (PrimitiveType.forType(f.getType())) {
                            case BOOLEAN:
                            case BYTE:
                                bytes++;
                                break;
                            case SHORT:
                            case CHAR:
                                shorts++;
                                break;
                            case INT:
                            case FLOAT:
                                words++;
                                break;
                            case DOUBLE:
                            case LONG:
                                doubles++;
                                break;
                            default:
                                throw new AssertionError();
                        }
                    } else {
                        oops++;
                    }
                }
            }
            if (doubles > 0 && size % PrimitiveType.LONG.getSize() != 0) {
                long length = PrimitiveType.LONG.getSize() - (size % PrimitiveType.LONG.getSize());
                size += PrimitiveType.LONG.getSize() - (size % PrimitiveType.LONG.getSize());
                while (length >= PrimitiveType.INT.getSize() && words > 0) {
                    length -= PrimitiveType.INT.getSize();
                    words--;
                }
                while (length >= PrimitiveType.SHORT.getSize() && shorts > 0) {
                    length -= PrimitiveType.SHORT.getSize();
                    shorts--;
                }
                while (length >= PrimitiveType.BYTE.getSize() && bytes > 0) {
                    length -= PrimitiveType.BYTE.getSize();
                    bytes--;
                }
                while (length >= PrimitiveType.getReferenceSize() && oops > 0) {
                    length -= PrimitiveType.getReferenceSize();
                    oops--;
                }
            }
            size = size + (PrimitiveType.DOUBLE.getSize() * doubles) + (PrimitiveType.INT.getSize() * words) + (PrimitiveType.SHORT.getSize() * shorts) + (PrimitiveType.BYTE.getSize() * bytes);
            if (oops > 0) {
                if (size % PrimitiveType.getReferenceSize() != 0) {
                    size += PrimitiveType.getReferenceSize() - (size % PrimitiveType.getReferenceSize());
                }
                size += oops * PrimitiveType.getReferenceSize();
            }
            if (doubles + words + shorts + bytes + oops > 0 && size % PrimitiveType.getReferenceSize() != 0) {
                size += PrimitiveType.getReferenceSize() - (size % PrimitiveType.getReferenceSize());
            }
        }
        if (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() != 0) {
            size += JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() - (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment());
        }
        return Math.max(size, JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize());
    }

    private long guessArraySize(Object obj) {
        long size = PrimitiveType.getArraySize();
        int length = Array.getLength(obj);
        if (length != 0) {
            Class<?> arrayElementClazz = obj.getClass().getComponentType();
            if (arrayElementClazz.isPrimitive()) {
                size += length * PrimitiveType.forType(arrayElementClazz).getSize();
            } else {
                size += length * PrimitiveType.getReferenceSize();
            }
        }
        if (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() != 0) {
            size += JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment() - (size % JvmInformation.CURRENT_JVM_INFORMATION.getObjectAlignment());
        }
        return Math.max(size, JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize());
    }
}
